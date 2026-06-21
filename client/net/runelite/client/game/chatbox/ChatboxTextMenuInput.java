package net.runelite.client.game.chatbox;

import com.google.inject.Inject;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.widgets.Widget;
import net.runelite.client.input.KeyListener;

public class ChatboxTextMenuInput extends ChatboxInput implements KeyListener {
   private final ChatboxPanelManager chatboxPanelManager;
   private String title;
   private final List<Entry> options = new ArrayList();
   private Runnable onClose;

   @Inject
   protected ChatboxTextMenuInput(ChatboxPanelManager chatboxPanelManager) {
      this.chatboxPanelManager = chatboxPanelManager;
   }

   public ChatboxTextMenuInput title(String title) {
      this.title = title;
      return this;
   }

   public ChatboxTextMenuInput option(String text, Runnable callback) {
      this.options.add(new Entry(text, callback));
      return this;
   }

   public ChatboxTextMenuInput onClose(Runnable onClose) {
      this.onClose = onClose;
      return this;
   }

   public ChatboxTextMenuInput build() {
      if (this.title == null) {
         throw new IllegalStateException("Title must be set");
      } else if (this.options.size() < 1) {
         throw new IllegalStateException("You must have atleast 1 option");
      } else {
         this.chatboxPanelManager.openInput(this);
         return this;
      }
   }

   protected void open() {
      Widget container = this.chatboxPanelManager.getContainerWidget();
      Widget prompt = container.createChild(-1, 4);
      prompt.setText(this.title);
      prompt.setTextColor(8388608);
      prompt.setFontId(497);
      prompt.setXPositionMode(1);
      prompt.setOriginalX(0);
      prompt.setYPositionMode(0);
      prompt.setOriginalY(8);
      prompt.setOriginalHeight(24);
      prompt.setXTextAlignment(1);
      prompt.setYTextAlignment(1);
      prompt.setWidthMode(1);
      prompt.revalidate();
      int y = prompt.getRelativeX() + prompt.getHeight() + 6;
      int height = container.getHeight() - y - 8;
      int step = height / this.options.size();
      int maxStep = this.options.size() >= 3 ? 25 : 30;
      if (step > maxStep) {
         int ds = step - maxStep;
         step = maxStep;
         y += ds * this.options.size() / 2;
      }

      for(Iterator var10 = this.options.iterator(); var10.hasNext(); y += step) {
         Entry option = (Entry)var10.next();
         Widget optWidget = container.createChild(-1, 4);
         optWidget.setText(option.text);
         optWidget.setFontId(497);
         optWidget.setXPositionMode(1);
         optWidget.setOriginalX(0);
         optWidget.setYPositionMode(0);
         optWidget.setOriginalY(y);
         optWidget.setOriginalHeight(24);
         optWidget.setXTextAlignment(1);
         optWidget.setYTextAlignment(1);
         optWidget.setWidthMode(1);
         optWidget.setAction(0, "Continue");
         optWidget.setOnOpListener(new Object[]{(ev) -> {
            this.callback(option);
         }});
         optWidget.setOnMouseOverListener(new Object[]{(ev) -> {
            optWidget.setTextColor(16777215);
         }});
         optWidget.setOnMouseLeaveListener(new Object[]{(ev) -> {
            optWidget.setTextColor(0);
         }});
         optWidget.setHasListener(true);
         optWidget.revalidate();
      }

   }

   private void callback(Entry entry) {
      Widget container = this.chatboxPanelManager.getContainerWidget();
      container.setOnKeyListener((Object[])null);
      this.chatboxPanelManager.close();
      entry.callback.run();
   }

   protected void close() {
      if (this.onClose != null) {
         this.onClose.run();
      }

   }

   public void keyTyped(KeyEvent e) {
      if (this.chatboxPanelManager.shouldTakeInput()) {
         char c = e.getKeyChar();
         if (c == 27) {
            this.chatboxPanelManager.close();
            e.consume();
         } else {
            int n = c - 49;
            if (n >= 0 && n < this.options.size()) {
               this.callback((Entry)this.options.get(n));
               e.consume();
            }

         }
      }
   }

   public void keyPressed(KeyEvent e) {
      if (this.chatboxPanelManager.shouldTakeInput()) {
         if (e.getKeyCode() == 27) {
            e.consume();
         }

      }
   }

   public void keyReleased(KeyEvent e) {
   }

   public String getTitle() {
      return this.title;
   }

   public List<Entry> getOptions() {
      return this.options;
   }

   public Runnable getOnClose() {
      return this.onClose;
   }

   private static final class Entry {
      private String text;
      private Runnable callback;

      public String getText() {
         return this.text;
      }

      public Runnable getCallback() {
         return this.callback;
      }

      public void setText(String text) {
         this.text = text;
      }

      public void setCallback(Runnable callback) {
         this.callback = callback;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry other = (Entry)o;
            Object this$text = this.getText();
            Object other$text = other.getText();
            if (this$text == null) {
               if (other$text != null) {
                  return false;
               }
            } else if (!this$text.equals(other$text)) {
               return false;
            }

            Object this$callback = this.getCallback();
            Object other$callback = other.getCallback();
            if (this$callback == null) {
               if (other$callback != null) {
                  return false;
               }
            } else if (!this$callback.equals(other$callback)) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $text = this.getText();
         result = result * 59 + ($text == null ? 43 : $text.hashCode());
         Object $callback = this.getCallback();
         result = result * 59 + ($callback == null ? 43 : $callback.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = this.getText();
         return "ChatboxTextMenuInput.Entry(text=" + var10000 + ", callback=" + String.valueOf(this.getCallback()) + ")";
      }

      public Entry(String text, Runnable callback) {
         this.text = text;
         this.callback = callback;
      }
   }
}
