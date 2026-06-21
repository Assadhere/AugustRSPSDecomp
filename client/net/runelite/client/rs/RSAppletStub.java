package net.runelite.client.rs;

import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.SwingUtilities;
import net.runelite.api.ClientConfiguration;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.RuntimeConfigLoader;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.util.LinkBrowser;

class RSAppletStub implements ClientConfiguration {
   private final RSConfig config;
   private final RuntimeConfigLoader runtimeConfigLoader;

   public URL getCodeBase() {
      try {
         return new URL(this.config.getCodeBase());
      } catch (MalformedURLException var2) {
         return null;
      }
   }

   public String getParameter(String name) {
      return (String)this.config.getAppletProperties().get(name);
   }

   public void onError(String code) {
      try {
         RuntimeConfig rtc = this.runtimeConfigLoader.get();
         if (rtc.showOutageMessage()) {
            return;
         }
      } catch (Exception var3) {
      }

      if (code.equals("error_game_js5connect")) {
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("RuneLite is unable to connect to the RuneScape update server. RuneScape might be offline for an update, check the game status page. If the game is online, then either a firewall is blocking RuneLite, or you don't have internet access.")).setTitle("RuneLite", "Unable to connect to update server").addButton("Game Status", () -> {
               LinkBrowser.browse("https://secure.runescape.com/m=news/game-status-information-centre?oldschool=1");
            }).open();
         });
      } else if (code.equals("error_game_js5io")) {
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("OldSchool RuneScape is unable to retrieve updates from its update server. This is likely due to a firewall blocking the RuneScape server. Try disabling your firewall, or use a different network.")).setTitle("RuneLite", "Unable to connect to update server").addHelpButtons().open();
         });
      } else if (code.equals("error_game_crash")) {
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("OldSchool RuneScape has crashed. Crashes are most commonly caused by plugin hub plugins, but can also be caused by RuneLite or Jagex client bugs. If you receive this message commonly, try playing in safe mode to eliminate the potential of plugins causing the crash. The client log file will contain additional information about the crash.")).setTitle("RuneLite", "OldSchool RuneScape has crashed").addHelpButtons().open();
         });
      } else {
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("OldSchool RuneScape has crashed with the message: " + code)).setTitle("RuneLite", "OldSchool RuneScape has crashed").addHelpButtons().open();
         });
      }

   }

   public RSAppletStub(RSConfig config, RuntimeConfigLoader runtimeConfigLoader) {
      this.config = config;
      this.runtimeConfigLoader = runtimeConfigLoader;
   }
}
