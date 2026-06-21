package net.runelite.client.plugins.keyremapping;

import com.google.inject.Provides;
import java.awt.Color;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(
   name = "Key Remapping",
   description = "Allows use of WASD keys for camera movement with 'Press Enter to Chat', and remapping number keys to F-keys",
   tags = {"enter", "chat", "wasd", "camera"},
   enabledByDefault = false
)
public class KeyRemappingPlugin extends Plugin {
   private static final String PRESS_ENTER_TO_CHAT = "Press Enter to Chat...";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private KeyManager keyManager;
   @Inject
   private KeyRemappingListener inputListener;
   private boolean typing;

   protected void startUp() throws Exception {
      this.typing = false;
      this.keyManager.registerKeyListener(this.inputListener);
      this.clientThread.invoke(() -> {
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.lockChat();
            this.client.setVarcStrValue(335, "");
         }

      });
   }

   protected void shutDown() throws Exception {
      this.clientThread.invoke(() -> {
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.unlockChat();
         }

      });
      this.keyManager.unregisterKeyListener(this.inputListener);
   }

   @Provides
   KeyRemappingConfig getConfig(ConfigManager configManager) {
      return (KeyRemappingConfig)configManager.getConfig(KeyRemappingConfig.class);
   }

   boolean chatboxFocused() {
      Widget chatboxParent = this.client.getWidget(10616832);
      if (chatboxParent != null && chatboxParent.getOnKeyListener() != null) {
         Widget worldMapSearch = this.client.getWidget(38993946);
         if (worldMapSearch != null && this.client.getVarcIntValue(190) == 1) {
            return false;
         } else {
            Widget report = this.client.getWidget(57344000);
            if (report != null) {
               return false;
            } else {
               return this.client.getFocusedInputFieldWidget() == null;
            }
         }
      } else {
         return false;
      }
   }

   boolean isDialogOpen() {
      return this.isHidden(10616887) || this.isHidden(10616888) || !this.isHidden(13959168);
   }

   boolean isOptionsDialogOpen() {
      return this.client.getWidget(14352385) != null;
   }

   private boolean isHidden(int component) {
      Widget w = this.client.getWidget(component);
      return w == null || w.isSelfHidden();
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent) {
      switch (scriptCallbackEvent.getEventName()) {
         case "setChatboxInput":
            Widget chatboxInput = this.client.getWidget(10616889);
            if (chatboxInput != null && !this.typing) {
               this.setChatboxWidgetInput(chatboxInput, "Press Enter to Chat...");
            }
            break;
         case "blockChatInput":
            if (!this.typing) {
               int[] intStack = this.client.getIntStack();
               int intStackSize = this.client.getIntStackSize();
               intStack[intStackSize - 1] = 1;
            }
      }

   }

   void lockChat() {
      Widget chatboxInput = this.client.getWidget(10616889);
      if (chatboxInput != null) {
         this.setChatboxWidgetInput(chatboxInput, "Press Enter to Chat...");
      }

   }

   void unlockChat() {
      Widget chatboxInput = this.client.getWidget(10616889);
      if (chatboxInput != null && this.client.getGameState() == GameState.LOGGED_IN) {
         boolean isChatboxTransparent = this.client.isResized() && this.client.getVarbitValue(4608) == 1;
         Color textColor = isChatboxTransparent ? JagexColors.CHAT_TYPED_TEXT_TRANSPARENT_BACKGROUND : JagexColors.CHAT_TYPED_TEXT_OPAQUE_BACKGROUND;
         this.setChatboxWidgetInput(chatboxInput, ColorUtil.wrapWithColorTag(this.client.getVarcStrValue(335) + "*", textColor));
      }

   }

   private void setChatboxWidgetInput(Widget widget, String input) {
      String text = widget.getText();
      int idx = text.indexOf(58);
      if (idx != -1) {
         String var10000 = text.substring(0, idx);
         String newText = var10000 + ": " + input;
         widget.setText(newText);
      }

   }

   boolean isTyping() {
      return this.typing;
   }

   void setTyping(boolean typing) {
      this.typing = typing;
   }
}
