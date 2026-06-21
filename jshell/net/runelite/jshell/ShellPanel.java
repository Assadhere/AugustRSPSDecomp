package net.runelite.jshell;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;
import jdk.jshell.DeclarationSnippet;
import jdk.jshell.Diag;
import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.SourceCodeAnalysis;
import jdk.jshell.Snippet.Status;
import jdk.jshell.SourceCodeAnalysis.Completeness;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ShellPanel extends JPanel {
   private static final Logger log = LoggerFactory.getLogger(ShellPanel.class);
   private final ScheduledExecutorService executor;
   private final RSyntaxTextArea textArea;
   private final JTextArea console = new JTextArea();
   private final Logger shellLogger;
   private final List<Runnable> cleanup = new ArrayList();
   private RLShellExecutionControl exec;
   private JShell shell;
   private Set<Snippet> prelude;
   private Injector injector;
   private AutoCompletion autoCompletion;
   public static ShellPanel INSTANCE;

   public ShellPanel(ScheduledExecutorService executor) {
      this.executor = executor;
      Font codeFont = (Font)Stream.of("Source code pro", "DejaVu Sans Code", "Consolas", "Monospaced").map((name) -> {
         return new Font(name, 0, 12);
      }).filter((fx) -> {
         return !"Dialog.plain".equals(fx.getFontName());
      }).findFirst().get();
      this.setLayout(new BorderLayout());
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new FlowLayout(2));
      JButton run = new JButton("⯈");
      run.setToolTipText("Run");
      run.addActionListener((ev) -> {
         this.run();
      });
      topPanel.add(run);
      JButton clear = new JButton("\ud83d\uddd1");
      clear.setToolTipText("Clear console");
      clear.addActionListener((ev) -> {
         this.console.setText("");
      });
      topPanel.add(clear);
      this.add(topPanel, "North");
      this.textArea = new RSyntaxTextArea();

      try {
         HashMap<RenderingHints.Key, Object> map = new HashMap();
         map.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         Field f = RSyntaxTextArea.class.getDeclaredField("aaHints");
         f.setAccessible(true);
         f.set(this.textArea, map);
      } catch (ReflectiveOperationException var11) {
         ReflectiveOperationException e = var11;
         throw new RuntimeException(e);
      }

      this.textArea.setFont(codeFont);
      this.textArea.setSyntaxEditingStyle("text/java");
      this.textArea.setAutoIndentEnabled(true);
      this.textArea.setPaintTabLines(true);
      this.textArea.setShowMatchedBracketPopup(true);
      this.textArea.setCloseCurlyBraces(false);
      this.textArea.setTabSize(2);
      this.textArea.setMarkOccurrences(true);
      this.textArea.setMarkOccurrencesDelay(200);
      this.textArea.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 82 && (e.getModifiersEx() & 128) != 0) {
               ShellPanel.this.run();
               e.consume();
            }

            if (e.getKeyCode() == 121) {
               ShellPanel.this.run();
               e.consume();
            }

         }
      });
      RTextScrollPane textScrollArea = new RTextScrollPane(this.textArea);

      try {
         Theme.load(ShellPanel.class.getResourceAsStream("darcula.xml"), codeFont).apply(this.textArea);
         InputStream is = ShellPanel.class.getResourceAsStream("default.jsh");

         try {
            this.textArea.setText(new String(is.readAllBytes(), StandardCharsets.UTF_8));
         } catch (Throwable var12) {
            if (is != null) {
               try {
                  is.close();
               } catch (Throwable var10) {
                  var12.addSuppressed(var10);
               }
            }

            throw var12;
         }

         if (is != null) {
            is.close();
         }
      } catch (IOException var13) {
         IOException e = var13;
         throw new RuntimeException(e);
      }

      this.console.setFont(codeFont);
      this.console.setEditable(false);
      this.console.setOpaque(false);
      JSplitPane split = new JSplitPane(0, textScrollArea, new JScrollPane(this.console));
      split.setResizeWeight(0.8);
      split.setPreferredSize(new Dimension(800, 800));
      this.add(split, "Center");
      this.shellLogger = new TeeLogger(LoggerFactory.getLogger("Shell"), this::logToConsole);
      INSTANCE = this;
      JShell.builder();
   }

   public void switchContext(Injector injector) {
      this.freeContext();
      this.injector = injector;
      this.exec = new RLShellExecutionControl() {
         protected String invoke(Method doitMethod) throws Exception {
            AtomicReference<Object> result = new AtomicReference();
            Semaphore sema = new Semaphore(0);
            ShellPanel.this.invokeOnClientThread(() -> {
               try {
                  result.set(super.invoke(doitMethod));
               } catch (Exception var8) {
                  Exception e = var8;
                  result.set(e);
               } finally {
                  sema.release();
               }

            });
            sema.acquire();
            if (result.get() instanceof String) {
               return (String)result.get();
            } else {
               throw (Exception)result.get();
            }
         }
      };
      this.shell = JShell.builder().executionEngine(this.exec, (Map)null).build();

      String preludeStr;
      try {
         InputStream is = ShellPanel.class.getResourceAsStream("prelude.jsh");

         try {
            preludeStr = new String(is.readAllBytes(), StandardCharsets.UTF_8);
         } catch (Throwable var7) {
            if (is != null) {
               try {
                  is.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (is != null) {
            is.close();
         }
      } catch (IOException var8) {
         IOException e = var8;
         throw new RuntimeException(e);
      }

      this.prelude = ImmutableSet.copyOf(this.eval(preludeStr, false));
      JShellAutocompleteProvider cp = new JShellAutocompleteProvider(this.shell);
      this.autoCompletion = new AutoCompletion(cp);
      this.autoCompletion.setAutoActivationDelay(200);
      this.autoCompletion.setAutoActivationEnabled(true);
      this.autoCompletion.setAutoCompleteSingleChoices(false);
      this.autoCompletion.install(this.textArea);
   }

   public void logToConsole(String message) {
      SwingUtilities.invokeLater(() -> {
         try {
            Document doc = this.console.getDocument();
            if (doc.getLength() > 100000) {
               Segment seg = new Segment();

               int i;
               for(i = doc.getLength() - 75000; i < doc.getLength(); ++i) {
                  doc.getText(i, 1, seg);
                  if (seg.array[0] == '\n') {
                     break;
                  }
               }

               doc.remove(0, i);
            }

            doc.insertString(doc.getLength(), message + "\n", (AttributeSet)null);
            this.console.setCaretPosition(doc.getLength());
         } catch (BadLocationException var5) {
            BadLocationException e = var5;
            throw new RuntimeException(e);
         }
      });
   }

   private List<Snippet> eval(String src, boolean isUserCode) {
      ArrayList<Snippet> out = new ArrayList();
      HashMap<String, Integer> offsets = new HashMap();
      String output = null;
      int offset = 0;

      label80:
      while(offset < src.length()) {
         while(src.charAt(offset) == '\n') {
            ++offset;
         }

         SourceCodeAnalysis.CompletionInfo ci = this.shell.sourceCodeAnalysis().analyzeCompletion(src.substring(offset));
         int thisOffset = offset;
         offset = src.length() - ci.remaining().length();
         if (ci.completeness() == Completeness.EMPTY) {
            break;
         }

         List<SnippetEvent> evs = this.shell.eval(ci.source());
         Iterator var10 = evs.iterator();

         while(var10.hasNext()) {
            SnippetEvent ev = (SnippetEvent)var10.next();
            Snippet snip = ev.snippet();
            offsets.put("#" + snip.id(), thisOffset);
            if (ev.status() != Status.VALID) {
               boolean handled = false;
               List<Diag> diags = (List)this.shell.diagnostics(snip).collect(Collectors.toList());
               Iterator var15 = diags.iterator();

               String ident;
               while(var15.hasNext()) {
                  Diag diag = (Diag)var15.next();
                  handled = true;
                  ident = this.toStringDiagnostic(src, thisOffset, diag);
                  if (!isUserCode) {
                     throw new RuntimeException("prelude error: " + ident);
                  }

                  this.logToConsole(ident);
               }

               if (snip instanceof DeclarationSnippet) {
                  List<String> unresolved = (List)this.shell.unresolvedDependencies((DeclarationSnippet)snip).collect(Collectors.toList());
                  Iterator var19 = unresolved.iterator();

                  while(var19.hasNext()) {
                     ident = (String)var19.next();
                     handled = true;
                     this.logToConsole("Unresolved symbol: " + ident);
                  }
               }

               if (!handled) {
                  this.logToConsole("bad snippet" + String.valueOf(ev.status()));
               }

               if (ev.status() != Status.RECOVERABLE_DEFINED) {
                  break label80;
               }
            }

            if (ev.exception() != null) {
               if (!isUserCode) {
                  throw new RuntimeException("prelude error", ev.exception());
               }

               this.shellLogger.error("", new RemappingThrowable(src, offsets, ev.exception()));
            }

            output = ev.value();
            out.add(snip);
         }
      }

      if (isUserCode && !Strings.isNullOrEmpty(output)) {
         this.logToConsole("[OUTPUT] " + output);
      }

      return out;
   }

   private String toStringDiagnostic(String source, int offset, Diag diag) {
      int line = 1;
      int column = 1;
      offset += (int)diag.getPosition();

      for(int i = 0; i < offset && i < source.length(); ++i) {
         if (source.charAt(i) == '\n') {
            ++line;
            column = 1;
         } else {
            ++column;
         }
      }

      return "" + line + ":" + column + ": " + diag.getMessage(Locale.getDefault());
   }

   protected void run() {
      String text = this.textArea.getText();
      this.executor.submit(() -> {
         Stream var10000 = this.shell.snippets().filter((v) -> {
            return !this.prelude.contains(v);
         });
         JShell var10001 = this.shell;
         Objects.requireNonNull(var10001);
         var10000.forEach(var10001::drop);
         this.cleanup();
         this.eval(text, true);
      });
   }

   public void freeContext() {
      this.cleanup();
      this.exec = null;
      this.shell = null;
      this.prelude = null;
      this.injector = null;
      if (this.autoCompletion != null) {
         this.autoCompletion.uninstall();
      }

      this.autoCompletion = null;
      this.console.setText("");
   }

   private void cleanup() {
      ArrayList<Runnable> todo = new ArrayList(this.cleanup);
      this.cleanup.clear();
      this.invokeOnClientThread(() -> {
         Iterator var2 = todo.iterator();

         while(var2.hasNext()) {
            Runnable c = (Runnable)var2.next();

            try {
               c.run();
            } catch (Exception var5) {
               Exception e = var5;
               this.shellLogger.error("Cleanup threw:", e);
            }
         }

      });
   }

   protected abstract void invokeOnClientThread(Runnable var1);

   public <T> T inject(Class<T> clazz) {
      return this.injector.getInstance(clazz);
   }

   public void cleanup(Runnable r) {
      this.cleanup.add(r);
   }

   public Logger getShellLogger() {
      return this.shellLogger;
   }
}
