package net.runelite.client.plugins.attackstyles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

class AttackStylesOverlay extends OverlayPanel {
   private final AttackStylesPlugin plugin;
   private final AttackStylesConfig config;

   @Inject
   private AttackStylesOverlay(AttackStylesPlugin plugin, AttackStylesConfig config) {
      super(plugin);
      this.setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
      this.plugin = plugin;
      this.config = config;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Attack style overlay");
   }

   public Dimension render(Graphics2D graphics) {
      boolean warnedSkillSelected = this.plugin.isWarnedSkillSelected();
      if (!warnedSkillSelected && !this.config.alwaysShowStyle()) {
         return null;
      } else {
         AttackStyle attackStyle = this.plugin.getAttackStyle();
         if (attackStyle == null) {
            return null;
         } else {
            String attackStyleString = attackStyle.getName();
            this.panelComponent.getChildren().add(TitleComponent.builder().text(attackStyleString).color(warnedSkillSelected ? Color.RED : Color.WHITE).build());
            this.panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(attackStyleString) + 10, 0));
            return super.render(graphics);
         }
      }
   }
}
