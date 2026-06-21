package net.runelite.client.plugins.bank;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.vars.InputType;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import org.apache.commons.lang3.ArrayUtils;

@Singleton
public class BankSearch {
   private final Client client;
   private final ClientThread clientThread;

   @Inject
   private BankSearch(Client client, ClientThread clientThread) {
      this.client = client;
      this.clientThread = clientThread;
   }

   public void layoutBank() {
      Widget bankContainer = this.client.getWidget(786444);
      if (bankContainer != null) {
         Object[] scriptArgs = bankContainer.getOnInvTransmitListener();
         if (scriptArgs != null) {
            this.client.runScript(scriptArgs);
         }
      }
   }

   public void initSearch() {
      this.clientThread.invoke(() -> {
         Widget bankContainer = this.client.getWidget(786444);
         if (bankContainer != null && !bankContainer.isHidden()) {
            Object[] bankBuildArgs = bankContainer.getOnInvTransmitListener();
            if (bankBuildArgs != null) {
               Object[] searchToggleArgs = ArrayUtils.insert(1, bankBuildArgs, new Object[]{1});
               searchToggleArgs[0] = 281;
               this.reset(true);
               this.client.runScript(searchToggleArgs);
            }
         }
      });
   }

   public void reset(boolean closeChat) {
      this.clientThread.invoke(() -> {
         if (closeChat) {
            this.client.runScript(new Object[]{299, 1, 1, 0});
         } else {
            this.client.setVarcIntValue(5, InputType.NONE.getType());
            this.client.setVarcStrValue(359, "");
         }

         this.layoutBank();
      });
   }
}
