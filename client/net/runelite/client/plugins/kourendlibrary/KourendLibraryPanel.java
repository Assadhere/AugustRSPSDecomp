package net.runelite.client.plugins.kourendlibrary;

import com.google.inject.Inject;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

@Singleton
class KourendLibraryPanel extends PluginPanel {
   private static final ImageIcon RESET_ICON;
   private static final ImageIcon RESET_HOVER_ICON;
   private final KourendLibraryPlugin plugin;
   private final Library library;
   private final HashMap<Book, BookPanel> bookPanels = new HashMap();

   @Inject
   KourendLibraryPanel(KourendLibraryPlugin plugin, Library library) {
      this.plugin = plugin;
      this.library = library;
   }

   void init() {
      this.setLayout(new BorderLayout(0, 5));
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel books = new JPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.fill = 2;
      c.weightx = 1.0;
      c.gridx = 0;
      c.gridy = 0;
      Stream.of(Book.values()).filter((b) -> {
         return b != Book.VARLAMORE_ENVOY || this.plugin.showVarlamoreEnvoy();
      }).sorted(Comparator.comparing(Book::getShortName)).forEach((b) -> {
         BookPanel p = new BookPanel(b);
         this.bookPanels.put(b, p);
         books.add(p, c);
         ++c.gridy;
      });
      JButton reset = new JButton("Reset", RESET_ICON);
      reset.setRolloverIcon(RESET_HOVER_ICON);
      reset.addActionListener((ev) -> {
         this.library.reset();
         this.update();
      });
      this.add(reset, "North");
      this.add(books, "Center");
      this.update();
   }

   void update() {
      SwingUtilities.invokeLater(() -> {
         Book customerBook = this.library.getCustomerBook();
         Iterator var2 = this.bookPanels.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry<Book, BookPanel> b = (Map.Entry)var2.next();
            Book book = (Book)b.getKey();
            BookPanel panel = (BookPanel)b.getValue();
            panel.setIsTarget(customerBook == book);
            panel.setIsHeld(this.plugin.doesPlayerContainBook(book));
         }

         HashMap<Book, HashSet<String>> bookLocations = new HashMap();
         Iterator var8 = this.library.getBookcases().iterator();

         while(true) {
            while(var8.hasNext()) {
               Bookcase bookcase = (Bookcase)var8.next();
               if (bookcase.getBook() != null) {
                  ((HashSet)bookLocations.computeIfAbsent(bookcase.getBook(), (a) -> {
                     return new HashSet();
                  })).add(bookcase.getLocationString());
               } else {
                  Iterator var11 = bookcase.getPossibleBooks().iterator();

                  while(var11.hasNext()) {
                     Book bookx = (Book)var11.next();
                     if (bookx != null) {
                        ((HashSet)bookLocations.computeIfAbsent(bookx, (a) -> {
                           return new HashSet();
                        })).add(bookcase.getLocationString());
                     }
                  }
               }
            }

            var8 = this.bookPanels.entrySet().iterator();

            while(true) {
               while(var8.hasNext()) {
                  Map.Entry<Book, BookPanel> e = (Map.Entry)var8.next();
                  HashSet<String> locs = (HashSet)bookLocations.get(e.getKey());
                  if (locs != null && locs.size() <= 3) {
                     BookPanel var10000 = (BookPanel)e.getValue();
                     Stream var10001 = locs.stream();
                     var10000.setLocation("<html>" + (String)var10001.collect(Collectors.joining("<br>")) + "</html>");
                  } else {
                     ((BookPanel)e.getValue()).setLocation("Unknown");
                  }
               }

               return;
            }
         }
      });
   }

   void reload() {
      this.bookPanels.clear();
      this.removeAll();
      this.init();
   }

   static {
      BufferedImage resetIcon = ImageUtil.loadImageResource(KourendLibraryPanel.class, "/util/reset.png");
      RESET_ICON = new ImageIcon(resetIcon);
      RESET_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(resetIcon, -100));
   }
}
