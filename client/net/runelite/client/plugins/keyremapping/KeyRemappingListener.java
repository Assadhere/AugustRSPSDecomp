package net.runelite.client.plugins.keyremapping;

import com.google.common.base.Strings;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;

class KeyRemappingListener implements KeyListener {
   @Inject
   private KeyRemappingPlugin plugin;
   @Inject
   private KeyRemappingConfig config;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   private final Map<Integer, Integer> modified = new HashMap();
   private final Set<Character> blockedChars = new HashSet();

   public void keyTyped(KeyEvent e) {
      char keyChar = e.getKeyChar();
      if (keyChar != '\uffff' && this.blockedChars.contains(keyChar) && this.plugin.chatboxFocused()) {
         e.consume();
      }

   }

   public void keyPressed(KeyEvent e) {
      if (this.plugin.chatboxFocused()) {
         ClientThread var10000;
         KeyRemappingPlugin var10001;
         if (!this.plugin.isTyping()) {
            int mappedKeyCode = 0;
            if (this.config.cameraRemap()) {
               if (this.config.up().matches(e)) {
                  mappedKeyCode = 38;
               } else if (this.config.down().matches(e)) {
                  mappedKeyCode = 40;
               } else if (this.config.left().matches(e)) {
                  mappedKeyCode = 37;
               } else if (this.config.right().matches(e)) {
                  mappedKeyCode = 39;
               }
            }

            if (this.config.fkeyRemap() && !this.plugin.isDialogOpen()) {
               if (this.config.f1().matches(e)) {
                  mappedKeyCode = 112;
               } else if (this.config.f2().matches(e)) {
                  mappedKeyCode = 113;
               } else if (this.config.f3().matches(e)) {
                  mappedKeyCode = 114;
               } else if (this.config.f4().matches(e)) {
                  mappedKeyCode = 115;
               } else if (this.config.f5().matches(e)) {
                  mappedKeyCode = 116;
               } else if (this.config.f6().matches(e)) {
                  mappedKeyCode = 117;
               } else if (this.config.f7().matches(e)) {
                  mappedKeyCode = 118;
               } else if (this.config.f8().matches(e)) {
                  mappedKeyCode = 119;
               } else if (this.config.f9().matches(e)) {
                  mappedKeyCode = 120;
               } else if (this.config.f10().matches(e)) {
                  mappedKeyCode = 121;
               } else if (this.config.f11().matches(e)) {
                  mappedKeyCode = 122;
               } else if (this.config.f12().matches(e)) {
                  mappedKeyCode = 123;
               } else if (this.config.esc().matches(e)) {
                  mappedKeyCode = 27;
               }
            }

            if (this.plugin.isDialogOpen() && !this.plugin.isOptionsDialogOpen() && this.config.space().matches(e)) {
               mappedKeyCode = 32;
            }

            if (!this.plugin.isOptionsDialogOpen() && this.config.control().matches(e)) {
               mappedKeyCode = 17;
            }

            if (mappedKeyCode != 0 && mappedKeyCode != e.getKeyCode()) {
               char keyChar = e.getKeyChar();
               this.modified.put(e.getKeyCode(), Integer.valueOf(mappedKeyCode));
               e.setKeyCode(mappedKeyCode);
               e.setKeyChar('\uffff');
               if (keyChar != '\uffff') {
                  this.blockedChars.add(keyChar);
               }
            }

            switch (e.getKeyCode()) {
               case 10:
               case 47:
               case 513:
                  this.plugin.setTyping(true);
                  var10000 = this.clientThread;
                  var10001 = this.plugin;
                  Objects.requireNonNull(var10001);
                  var10000.invoke(var10001::unlockChat);
            }
         } else {
            switch (e.getKeyCode()) {
               case 8:
                  if (Strings.isNullOrEmpty(this.client.getVarcStrValue(335))) {
                     this.plugin.setTyping(false);
                     var10000 = this.clientThread;
                     var10001 = this.plugin;
                     Objects.requireNonNull(var10001);
                     var10000.invoke(var10001::lockChat);
                  }
                  break;
               case 10:
                  this.plugin.setTyping(false);
                  var10000 = this.clientThread;
                  var10001 = this.plugin;
                  Objects.requireNonNull(var10001);
                  var10000.invoke(var10001::lockChat);
                  break;
               case 27:
                  e.consume();
                  this.plugin.setTyping(false);
                  this.clientThread.invoke(() -> {
                     this.client.setVarcStrValue(335, "");
                     this.plugin.lockChat();
                  });
            }
         }

      }
   }

   public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      char keyChar = e.getKeyChar();
      if (keyChar != '\uffff') {
         this.blockedChars.remove(keyChar);
      }

      Integer mappedKeyCode = (Integer)this.modified.remove(keyCode);
      if (mappedKeyCode != null) {
         e.setKeyCode(mappedKeyCode);
         e.setKeyChar('\uffff');
      }

   }
}
