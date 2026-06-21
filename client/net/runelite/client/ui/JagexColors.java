package net.runelite.client.ui;

import java.awt.Color;

public class JagexColors {
   public static final Color CHAT_PUBLIC_TEXT_OPAQUE_BACKGROUND;
   public static final Color CHAT_PRIVATE_MESSAGE_TEXT_OPAQUE_BACKGROUND;
   public static final Color CHAT_FC_TEXT_OPAQUE_BACKGROUND;
   public static final Color CHAT_FC_NAME_OPAQUE_BACKGROUND;
   public static final Color CHAT_GAME_EXAMINE_TEXT_OPAQUE_BACKGROUND;
   public static final Color CHAT_TYPED_TEXT_OPAQUE_BACKGROUND;
   public static final Color CHAT_PUBLIC_TEXT_TRANSPARENT_BACKGROUND;
   public static final Color CHAT_PRIVATE_MESSAGE_TEXT_TRANSPARENT_BACKGROUND;
   public static final Color CHAT_FC_TEXT_TRANSPARENT_BACKGROUND;
   public static final Color CHAT_FC_NAME_TRANSPARENT_BACKGROUND;
   public static final Color CHAT_GAME_EXAMINE_TEXT_TRANSPARENT_BACKGROUND;
   public static final Color CHAT_TYPED_TEXT_TRANSPARENT_BACKGROUND;
   public static final Color MENU_TARGET;
   public static final String MENU_TARGET_TAG = "<col=ff9040>";
   public static final Color TOOLTIP_BACKGROUND;
   public static final Color TOOLTIP_BORDER;
   public static final Color TOOLTIP_TEXT;
   public static final Color DARK_ORANGE_INTERFACE_TEXT;
   public static final Color YELLOW_INTERFACE_TEXT;

   static {
      CHAT_PUBLIC_TEXT_OPAQUE_BACKGROUND = Color.BLUE;
      CHAT_PRIVATE_MESSAGE_TEXT_OPAQUE_BACKGROUND = new Color(8323072);
      CHAT_FC_TEXT_OPAQUE_BACKGROUND = new Color(127, 0, 0);
      CHAT_FC_NAME_OPAQUE_BACKGROUND = Color.BLUE;
      CHAT_GAME_EXAMINE_TEXT_OPAQUE_BACKGROUND = Color.BLACK;
      CHAT_TYPED_TEXT_OPAQUE_BACKGROUND = Color.BLUE;
      CHAT_PUBLIC_TEXT_TRANSPARENT_BACKGROUND = new Color(144, 144, 255);
      CHAT_PRIVATE_MESSAGE_TEXT_TRANSPARENT_BACKGROUND = new Color(12525600);
      CHAT_FC_TEXT_TRANSPARENT_BACKGROUND = new Color(239, 80, 80);
      CHAT_FC_NAME_TRANSPARENT_BACKGROUND = new Color(144, 112, 255);
      CHAT_GAME_EXAMINE_TEXT_TRANSPARENT_BACKGROUND = Color.WHITE;
      CHAT_TYPED_TEXT_TRANSPARENT_BACKGROUND = new Color(144, 144, 255);
      MENU_TARGET = new Color(16748608);
      TOOLTIP_BACKGROUND = new Color(255, 255, 160);
      TOOLTIP_BORDER = Color.BLACK;
      TOOLTIP_TEXT = Color.BLACK;
      DARK_ORANGE_INTERFACE_TEXT = new Color(255, 152, 31);
      YELLOW_INTERFACE_TEXT = Color.YELLOW;
   }
}
