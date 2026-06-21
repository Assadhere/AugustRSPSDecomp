package net.runelite.client.plugins.devtools;

import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.jshell.ShellPanel;

@Singleton
class ShellFrame extends DevToolsFrame {
   private final ShellPanel shellPanel;

   @Inject
   ShellFrame(final ClientThread clientThread, ScheduledExecutorService executor) {
      this.shellPanel = new ShellPanel(executor) {
         protected void invokeOnClientThread(Runnable r) {
            clientThread.invoke(r);
         }
      };
      this.setContentPane(this.shellPanel);
      this.setTitle("RuneLite Shell");
      this.pack();
   }

   public void open() {
      this.shellPanel.switchContext(RuneLite.getInjector());
      super.open();
   }

   public void close() {
      super.close();
      this.shellPanel.freeContext();
   }
}
