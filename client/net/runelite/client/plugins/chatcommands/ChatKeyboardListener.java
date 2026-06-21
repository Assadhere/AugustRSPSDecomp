package net.runelite.client.plugins.chatcommands;

import java.awt.event.KeyEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.vars.InputType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;

@Singleton
public class ChatKeyboardListener implements KeyListener {
   @Inject
   private ChatCommandsConfig chatCommandsConfig;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;

   public void keyTyped(KeyEvent e) {
   }

   public void keyPressed(KeyEvent e) {
      int inputTye;
      if (this.chatCommandsConfig.clearSingleWord().matches(e)) {
         inputTye = this.client.getVarcIntValue(5);
         String input = inputTye == InputType.NONE.getType() ? this.client.getVarcStrValue(335) : this.client.getVarcStrValue(359);
         if (input != null) {
            e.consume();

            while(input.endsWith(" ")) {
               input = input.substring(0, input.length() - 1);
            }

            int idx = input.lastIndexOf(32) + 1;
            String replacement = input.substring(0, idx);
            this.clientThread.invoke(() -> {
               this.applyText(inputTye, replacement);
            });
         }
      } else if (this.chatCommandsConfig.clearChatBox().matches(e)) {
         e.consume();
         inputTye = this.client.getVarcIntValue(5);
         this.clientThread.invoke(() -> {
            this.applyText(inputTye, "");
         });
      }

   }

   private void applyText(int inputType, String replacement) {
      if (inputType == InputType.NONE.getType()) {
         this.client.setVarcStrValue(335, replacement);
         this.client.runScript(new Object[]{223});
      } else if (inputType == InputType.PRIVATE_MESSAGE.getType()) {
         this.client.setVarcStrValue(359, replacement);
         this.client.runScript(new Object[]{222, ""});
      }

   }

   public void keyReleased(KeyEvent e) {
   }
}
