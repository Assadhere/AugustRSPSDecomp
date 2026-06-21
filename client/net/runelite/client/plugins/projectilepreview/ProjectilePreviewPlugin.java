package net.runelite.client.plugins.projectilepreview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Projectile Preview",
   description = "Spawn client-only projectiles between local player and a chosen target, with live sliders to tune projectile parameters (height, slope, duration, etc.). Visual only — no server. Use the tuned values when authoring server-side projectiles.",
   tags = {"projectile", "preview", "dev", "tool"},
   developerPlugin = true,
   enabledByDefault = false
)
public class ProjectilePreviewPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ProjectilePreviewPlugin.class);
   private static final String SET_TARGET_OPTION = "Set projectile target";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ClientToolbar clientToolbar;
   private ProjectilePreviewPanel panel;
   private NavigationButton navButton;
   private volatile Actor target;
   private int lastFireCycle = Integer.MIN_VALUE;

   protected void startUp() {
      this.panel = new ProjectilePreviewPanel();
      this.panel.onFire(() -> {
         this.clientThread.invokeLater(this::fireOnce);
      });
      this.panel.onClearTarget(this::clearTarget);
      this.navButton = NavigationButton.builder().tooltip("Projectile Preview").icon(buildIcon()).priority(16).panel(this.panel).build();
      this.clientToolbar.addNavigation(this.navButton);
   }

   protected void shutDown() {
      if (this.navButton != null) {
         this.clientToolbar.removeNavigation(this.navButton);
         this.navButton = null;
      }

      this.panel = null;
      this.target = null;
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      MenuEntry me = event.getMenuEntry();
      MenuAction action = me.getType();
      boolean npcExamine = action == MenuAction.EXAMINE_NPC && me.getNpc() != null;
      boolean playerExamine = me.getPlayer() != null && "Examine".equals(me.getOption());
      if (npcExamine || playerExamine) {
         Actor a = npcExamine ? me.getNpc() : me.getPlayer();
         this.client.createMenuEntry(-1).setOption("Set projectile target").setTarget(me.getTarget()).setType(MenuAction.RUNELITE).onClick((c) -> {
            this.setTarget(a);
         });
      }
   }

   @Subscribe
   public void onClientTick(ClientTick event) {
      if (this.panel != null) {
         Snapshot s = this.panel.snapshot();
         if (s.loopTicks > 0) {
            if (this.target != null) {
               int now = this.client.getGameCycle();
               int interval = s.loopTicks * 30;
               if (now - this.lastFireCycle >= interval) {
                  this.spawn(s);
                  this.lastFireCycle = now;
               }
            }
         }
      }
   }

   private void fireOnce() {
      if (this.panel != null) {
         if (this.target == null) {
            this.notifyChat("Projectile Preview: no target. Right-click an entity and pick \"Set projectile target\".");
         } else {
            this.spawn(this.panel.snapshot());
            this.lastFireCycle = this.client.getGameCycle();
         }
      }
   }

   private void spawn(Snapshot s) {
      Player src = this.client.getLocalPlayer();
      if (src != null) {
         if (this.target != null) {
            WorldPoint srcWp = src.getWorldLocation();
            WorldPoint tgtWp = this.target.getWorldLocation();
            if (srcWp != null && tgtWp != null) {
               int startCycle = this.client.getGameCycle() + s.startDelay;
               int endCycle = startCycle + s.duration;

               try {
                  Projectile p = this.client.createProjectile(s.spotanimId, srcWp, s.srcHeight, src, tgtWp, s.tgtHeight, this.target, startCycle, endCycle, s.slope, s.startPos);
                  if (p == null) {
                     this.notifyChat("Projectile Preview: createProjectile returned null (bad spotanim id " + s.spotanimId + "?).");
                  }
               } catch (Throwable var8) {
                  Throwable t = var8;
                  log.warn("Projectile Preview: spawn failed", t);
                  this.notifyChat("Projectile Preview: spawn failed — " + t.getMessage());
               }

            }
         }
      }
   }

   private void setTarget(Actor a) {
      this.target = a;
      String name = a == null ? null : a.getName();
      if (a instanceof NPC && name != null) {
         name = name + " (#" + ((NPC)a).getId() + ")";
      }

      if (this.panel != null) {
         this.panel.setTargetText(name);
      }

      this.notifyChat("Projectile Preview target set: " + (name == null ? "?" : name));
   }

   private void clearTarget() {
      this.target = null;
      if (this.panel != null) {
         this.panel.setTargetText((String)null);
      }

   }

   private void notifyChat(String msg) {
      this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, (String)null);
   }

   private static BufferedImage buildIcon() {
      BufferedImage img = new BufferedImage(16, 16, 2);
      Graphics2D g = img.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g.setColor(new Color(200, 120, 60));
      g.fillOval(2, 7, 6, 6);
      g.setColor(new Color(220, 200, 80));
      g.fillOval(9, 4, 5, 5);
      g.setStroke(new BasicStroke(1.4F));
      g.setColor(new Color(255, 230, 120));
      g.drawLine(5, 10, 12, 6);
      g.setFont(new Font("SansSerif", 1, 8));
      g.dispose();
      return img;
   }

   static final class Snapshot {
      final int spotanimId;
      final int srcHeight;
      final int tgtHeight;
      final int startDelay;
      final int duration;
      final int slope;
      final int startPos;
      final int loopTicks;

      Snapshot(int spotanimId, int srcHeight, int tgtHeight, int startDelay, int duration, int slope, int startPos, int loopTicks) {
         this.spotanimId = spotanimId;
         this.srcHeight = srcHeight;
         this.tgtHeight = tgtHeight;
         this.startDelay = startDelay;
         this.duration = duration;
         this.slope = slope;
         this.startPos = startPos;
         this.loopTicks = loopTicks;
      }
   }
}
