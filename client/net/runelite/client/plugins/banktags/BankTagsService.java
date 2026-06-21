package net.runelite.client.plugins.banktags;

import javax.annotation.Nullable;
import net.runelite.client.plugins.banktags.tabs.Layout;

public interface BankTagsService {
   int OPTION_ALLOW_MODIFICATIONS = 1;
   int OPTION_HIDE_TAG_NAME = 2;
   int OPTION_NO_LAYOUT = 4;

   void openBankTag(String var1, int var2);

   void closeBankTag();

   @Nullable
   String getActiveTag();

   @Nullable
   BankTag getActiveBankTag();

   @Nullable
   Layout getActiveLayout();
}
