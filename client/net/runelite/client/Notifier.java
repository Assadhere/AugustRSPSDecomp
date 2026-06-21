package net.runelite.client;

import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.FlashNotification;
import net.runelite.client.config.Notification;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.NotificationFired;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.util.OSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Notifier {
   private static final Logger log = LoggerFactory.getLogger(Notifier.class);
   private static final String DOUBLE_QUOTE = "\"";
   private static final Escaper SHELL_ESCAPE = Escapers.builder().addEscape('"', "'").build();
   private static final int MINIMUM_FLASH_DURATION_MILLIS = 2000;
   private static final int MINIMUM_FLASH_DURATION_TICKS = 100;
   private static final File NOTIFICATION_FILE;
   private static final File NOTIFICATIONS_DIR;
   private final Client client;
   private final RuneLiteConfig runeLiteConfig;
   private final ClientUI clientUI;
   private final ScheduledExecutorService executorService;
   private final ChatMessageManager chatMessageManager;
   private final EventBus eventBus;
   private final AudioPlayer audioPlayer;
   private final String appName;
   private final Path notifyIconPath;
   private boolean terminalNotifierAvailable;
   private Instant flashStart;
   private FlashNotification flashNotification;
   private Color flashColor;
   private long mouseLastPressedMillis;

   @Inject
   private Notifier(ClientUI clientUI, Client client, RuneLiteConfig runeliteConfig, ScheduledExecutorService executorService, ChatMessageManager chatMessageManager, EventBus eventBus, AudioPlayer audioPlayer, @Named("runelite.title") String appName) {
      this.client = client;
      this.clientUI = clientUI;
      this.runeLiteConfig = runeliteConfig;
      this.executorService = executorService;
      this.chatMessageManager = chatMessageManager;
      this.eventBus = eventBus;
      this.audioPlayer = audioPlayer;
      this.appName = appName;
      this.notifyIconPath = RuneLite.RUNELITE_DIR.toPath().resolve("icon.png");
      if (!Strings.isNullOrEmpty(RuneLiteProperties.getLauncherVersion()) && OSType.getOSType() == OSType.MacOS) {
         executorService.execute(() -> {
            this.terminalNotifierAvailable = this.isTerminalNotifierAvailable();
         });
      }

      this.storeIcon();
      NOTIFICATIONS_DIR.mkdirs();
   }

   private Notification defaultNotification(TrayIcon.MessageType trayMessageType) {
      return new Notification(true, true, true, this.runeLiteConfig.enableTrayNotifications(), trayMessageType, this.runeLiteConfig.notificationRequestFocus(), this.runeLiteConfig.notificationSound(), (String)null, this.runeLiteConfig.notificationVolume(), this.runeLiteConfig.notificationTimeout(), this.runeLiteConfig.enableGameMessageNotification(), this.runeLiteConfig.flashNotification(), this.runeLiteConfig.notificationFlashColor(), this.runeLiteConfig.sendNotificationsWhenFocused());
   }

   public void notify(String message) {
      Notification notif = this.defaultNotification(MessageType.NONE);
      this.notify(notif, message);
   }

   public void notify(String message, TrayIcon.MessageType type) {
      Notification notif = this.defaultNotification(type);
      this.notify(notif, message);
   }

   public void notify(Notification notification, String message) {
      if (notification.isEnabled()) {
         if (!notification.isOverride() || !notification.isInitialized()) {
            notification = this.defaultNotification(notification.getTrayIconType());
         }

         assert notification.isInitialized();

         log.debug("{}", message);
         this.eventBus.post(new NotificationFired(notification, message, notification.getTrayIconType()));
         if (notification.isSendWhenFocused() || !this.clientUI.isFocused()) {
            switch (notification.getRequestFocus()) {
               case REQUEST:
                  this.clientUI.requestFocus();
                  break;
               case TASKBAR:
                  this.clientUI.flashTaskbar();
                  break;
               case FORCE:
                  this.clientUI.forceFocus();
            }

            if (notification.isTray()) {
               this.sendNotification(notification, this.buildTitle(), message);
            }

            switch (notification.getSound()) {
               case NATIVE:
                  Toolkit.getDefaultToolkit().beep();
                  break;
               case CUSTOM:
                  Notification n = notification;
                  this.executorService.submit(() -> {
                     this.tryPlayCustomSound(n);
                  });
            }

            if (notification.isGameMessage() && this.client.getGameState() == GameState.LOGGED_IN) {
               String formattedMessage = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append(message).build();
               this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).name(this.appName).runeLiteFormattedMessage(formattedMessage).build());
            }

            if (notification.getFlash() != FlashNotification.DISABLED) {
               this.flashNotification = notification.getFlash();
               this.flashColor = notification.getFlashColor();
               this.flashStart = Instant.now();
               this.mouseLastPressedMillis = this.client.getMouseLastPressedMillis();
            }

         }
      }
   }

   private String buildTitle() {
      Player player = this.client.getLocalPlayer();
      if (player == null) {
         return this.appName;
      } else {
         String name = player.getName();
         return Strings.isNullOrEmpty(name) ? this.appName : this.appName + " - " + name;
      }
   }

   public void processFlash(Graphics2D graphics) {
      if (this.flashStart != null && this.flashNotification != null && this.flashColor != null && this.client.getGameState() == GameState.LOGGED_IN && this.flashNotification != FlashNotification.DISABLED) {
         if (Instant.now().minusMillis(2000L).isAfter(this.flashStart)) {
            switch (this.flashNotification) {
               case FLASH_TWO_SECONDS:
               case SOLID_TWO_SECONDS:
                  this.flashStart = null;
                  this.flashNotification = null;
                  this.flashColor = null;
                  return;
               case SOLID_UNTIL_CANCELLED:
               case FLASH_UNTIL_CANCELLED:
                  if ((this.client.getMouseIdleTicks() < 100 || this.client.getKeyboardIdleTicks() < 100 || this.client.getMouseLastPressedMillis() > this.mouseLastPressedMillis) && this.clientUI.isFocused()) {
                     this.flashStart = null;
                     this.flashNotification = null;
                     this.flashColor = null;
                     return;
                  }
            }
         }

         if (this.client.getGameCycle() % 40 < 20 || this.flashNotification != FlashNotification.FLASH_TWO_SECONDS && this.flashNotification != FlashNotification.FLASH_UNTIL_CANCELLED) {
            Color color = graphics.getColor();
            graphics.setColor(this.flashColor);
            graphics.fill(new Rectangle(this.client.getCanvas().getSize()));
            graphics.setColor(color);
         }
      } else {
         this.flashStart = null;
         this.flashNotification = null;
         this.flashColor = null;
      }
   }

   private void sendNotification(Notification notification, String title, String message) {
      switch (OSType.getOSType()) {
         case Linux:
            this.sendLinuxNotification(notification, title, message);
            break;
         case MacOS:
            this.sendMacNotification(title, message);
            break;
         default:
            this.sendTrayNotification(notification, title, message);
      }

   }

   private void sendTrayNotification(Notification notification, String title, String message) {
      if (this.clientUI.getTrayIcon() != null) {
         this.clientUI.getTrayIcon().displayMessage(title, message, notification.getTrayIconType());
      }

   }

   private void sendLinuxNotification(Notification notification, String title, String message) {
      List<String> commands = new ArrayList();
      commands.add("notify-send");
      commands.add(title);
      commands.add(message);
      commands.add("-a");
      commands.add(SHELL_ESCAPE.escape(this.appName));
      commands.add("-i");
      commands.add(SHELL_ESCAPE.escape(this.notifyIconPath.toAbsolutePath().toString()));
      commands.add("-u");
      commands.add(toUrgency(notification.getTrayIconType()));
      if (notification.getTimeout() > 0) {
         commands.add("-t");
         commands.add(String.valueOf(notification.getTimeout()));
      }

      this.executorService.submit(() -> {
         try {
            Process notificationProcess = sendCommand(commands);
            boolean exited = notificationProcess.waitFor(500L, TimeUnit.MILLISECONDS);
            if (exited && notificationProcess.exitValue() == 0) {
               return;
            }
         } catch (InterruptedException | IOException var7) {
            Exception ex = var7;
            log.debug("error sending notification", ex);
         }

         this.sendTrayNotification(notification, title, message);
      });
   }

   private void sendMacNotification(String title, String message) {
      List<String> commands = new ArrayList();
      if (this.terminalNotifierAvailable) {
         Collections.addAll(commands, new String[]{"sh", "-lc", "\"$@\"", "--", "terminal-notifier", "-title", title, "-message", message, "-group", "net.runelite.launcher", "-sender", "net.runelite.launcher"});
      } else {
         commands.add("osascript");
         commands.add("-e");
         String var10000 = SHELL_ESCAPE.escape(message);
         String script = "display notification \"" + var10000 + "\" with title \"" + SHELL_ESCAPE.escape(title) + "\"";
         commands.add(script);
      }

      try {
         sendCommand(commands);
      } catch (IOException var5) {
         IOException ex = var5;
         log.warn("error sending notification", ex);
      }

   }

   private static Process sendCommand(List<String> commands) throws IOException {
      return (new ProcessBuilder(commands)).redirectErrorStream(true).start();
   }

   private void storeIcon() {
      if (OSType.getOSType() == OSType.Linux && !Files.exists(this.notifyIconPath, new LinkOption[0])) {
         try {
            InputStream stream = Notifier.class.getResourceAsStream("/net/runelite/client/ui/runelite_128.png");

            try {
               Files.copy(stream, this.notifyIconPath, new CopyOption[0]);
            } catch (Throwable var5) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (stream != null) {
               stream.close();
            }
         } catch (IOException var6) {
            IOException ex = var6;
            log.warn((String)null, ex);
         }
      }

   }

   private boolean isTerminalNotifierAvailable() {
      try {
         Process exec = Runtime.getRuntime().exec(new String[]{"sh", "-lc", "terminal-notifier -help"});
         if (!exec.waitFor(2L, TimeUnit.SECONDS)) {
            return false;
         } else {
            return exec.exitValue() == 0;
         }
      } catch (InterruptedException | IOException var2) {
         return false;
      }
   }

   private static String toUrgency(TrayIcon.MessageType type) {
      switch (type) {
         case WARNING:
         case ERROR:
            return "critical";
         default:
            return "normal";
      }
   }

   private void tryPlayCustomSound(Notification notification) {
      float volume = (float)notification.getVolume() / 100.0F;
      float gainDB = (float)Math.log10((double)volume) * 20.0F;

      try {
         if (notification.getSoundName() != null) {
            this.audioPlayer.play(new File(NOTIFICATIONS_DIR, notification.getSoundName()), gainDB);
         } else if (NOTIFICATION_FILE.exists()) {
            this.audioPlayer.play(NOTIFICATION_FILE, gainDB);
         } else {
            this.audioPlayer.play(Notifier.class, "notification.wav", gainDB);
         }
      } catch (Exception var5) {
         Exception e = var5;
         log.warn("Unable to play notification sound", e);
      }

   }

   static {
      NOTIFICATION_FILE = new File(RuneLite.RUNELITE_DIR, "notification.wav");
      NOTIFICATIONS_DIR = RuneLite.NOTIFICATIONS_DIR;
   }
}
