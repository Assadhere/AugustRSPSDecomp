package net.runelite.client.plugins.augustcustom.events;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.LinkBrowser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class AugustEventsPanel extends PluginPanel {
   private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
   private volatile String serverInfoContent = "";
   private final JTextArea serverInfoTextArea = new JTextArea();
   private final Consumer<Integer> bonusXpClickHandler;
   private final Consumer<Integer> votePageClickHandler;
   private final Consumer<Integer> donatePageClickHandler;
   private final JPanel bonusXpGridPanel = new JPanel();
   private final List<BonusXpInfo> currentBonusXp = new ArrayList();
   private static final DateTimeFormatter HH_MM_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
   private final JPanel mainEventDisplayPanel = new JPanel();
   private final JScrollPane eventScrollPane;
   private final Map<Integer, Map<EventKey, EventPanel>> worldEventPanels = new TreeMap();

   AugustEventsPanel(AugustEventsPlugin augustEventsPlugin, Client client, Consumer<Integer> bonusXpClickHandler, Consumer<Integer> votePageClickHandler, Consumer<Integer> donatePageClickHandler) {
      super(false);
      this.bonusXpClickHandler = bonusXpClickHandler;
      this.votePageClickHandler = votePageClickHandler;
      this.donatePageClickHandler = donatePageClickHandler;
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BoxLayout(topPanel, 1));
      topPanel.setBorder(new EmptyBorder(6, 6, 0, 6));
      topPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JButton openVotePage = new JButton("Click here to Vote");
      openVotePage.addActionListener((e) -> {
         votePageClickHandler.accept(-1);
      });
      JButton openDonationPage = new JButton("Click here to Donate");
      openDonationPage.addActionListener((e) -> {
         donatePageClickHandler.accept(-1);
      });
      JButton openWikiPage = new JButton("Click here to open Wiki");
      openWikiPage.addActionListener((e) -> {
         LinkBrowser.browse("https://wiki.august.games");
      });
      Dimension buttonMaxSize = new Dimension(Integer.MAX_VALUE, openVotePage.getPreferredSize().height);
      this.styleButton(openVotePage, buttonMaxSize);
      this.styleButton(openDonationPage, buttonMaxSize);
      this.styleButton(openWikiPage, buttonMaxSize);
      this.serverInfoTextArea.setLineWrap(true);
      this.serverInfoTextArea.setWrapStyleWord(true);
      this.serverInfoTextArea.setOpaque(true);
      this.serverInfoTextArea.setEditable(false);
      this.serverInfoTextArea.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.serverInfoTextArea.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.serverInfoTextArea.setFont(FontManager.getRunescapeSmallFont());
      this.serverInfoTextArea.setBorder(new EmptyBorder(5, 5, 5, 5));
      JLabel bonusXpTitleLabel = new JLabel("Bonus XP");
      bonusXpTitleLabel.setFont(FontManager.getRunescapeBoldFont());
      bonusXpTitleLabel.setForeground(Color.WHITE);
      bonusXpTitleLabel.setAlignmentX(0.5F);
      bonusXpTitleLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
      this.bonusXpGridPanel.setLayout(new GridLayout(0, 3, 4, 4));
      this.bonusXpGridPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.bonusXpGridPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
      topPanel.add(openVotePage);
      topPanel.add(Box.createVerticalStrut(5));
      topPanel.add(openDonationPage);
      topPanel.add(Box.createVerticalStrut(5));
      topPanel.add(openWikiPage);
      topPanel.add(Box.createVerticalStrut(10));
      topPanel.add(this.serverInfoTextArea);
      topPanel.add(bonusXpTitleLabel);
      topPanel.add(this.bonusXpGridPanel);
      JLabel eventsTitleLabel = new JLabel("Events");
      eventsTitleLabel.setFont(FontManager.getRunescapeBoldFont());
      eventsTitleLabel.setForeground(Color.WHITE);
      eventsTitleLabel.setAlignmentX(0.5F);
      eventsTitleLabel.setBorder(new EmptyBorder(10, 0, 2, 0));
      topPanel.add(eventsTitleLabel);
      JLabel eventsSubtitleLabel = new JLabel("(Hover event for info)");
      eventsSubtitleLabel.setFont(FontManager.getRunescapeSmallFont());
      eventsSubtitleLabel.setForeground(Color.LIGHT_GRAY);
      eventsSubtitleLabel.setAlignmentX(0.5F);
      eventsSubtitleLabel.setBorder(new EmptyBorder(2, 0, 6, 0));
      topPanel.add(eventsSubtitleLabel);
      this.add(topPanel, "North");
      this.mainEventDisplayPanel.setLayout(new GridBagLayout());
      this.mainEventDisplayPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.mainEventDisplayPanel.setBorder(new CompoundBorder(new EmptyBorder(4, 4, 4, 8), new EventPanel.RoundedLineBorder(ColorScheme.MEDIUM_GRAY_COLOR, 1, 5)));
      this.eventScrollPane = new JScrollPane(this.mainEventDisplayPanel);
      this.eventScrollPane.setBorder(BorderFactory.createEmptyBorder());
      this.eventScrollPane.setHorizontalScrollBarPolicy(31);
      this.eventScrollPane.setVerticalScrollBarPolicy(20);
      this.eventScrollPane.getVerticalScrollBar().setUnitIncrement(16);
      this.eventScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
      this.eventScrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.add(this.eventScrollPane, "Center");
      this.startUpdaters();
      this.rebuildEventUI();
   }

   private void styleButton(JButton button, Dimension maxSize) {
      button.setAlignmentX(0.5F);
      button.setMaximumSize(maxSize);
      button.setFocusPainted(false);
   }

   public void addOrUpdateEventPanel(int worldId, int renderOrder, String eventId, EventPanel newEventPanel) {
      Map<EventKey, EventPanel> worldMap = (Map)this.worldEventPanels.computeIfAbsent(worldId, (k) -> {
         return new TreeMap();
      });
      EventKey key = new EventKey(renderOrder, eventId);
      EventPanel oldPanel = (EventPanel)worldMap.put(key, newEventPanel);
      if (oldPanel != null && oldPanel != newEventPanel) {
         oldPanel.cancelTimer();
      }

   }

   public void removeEventPanel(int worldId, String eventId) {
      Map<EventKey, EventPanel> events = (Map)this.worldEventPanels.get(worldId);
      if (events != null) {
         EventPanel removedPanel = null;
         EventKey keyToRemove = null;
         Iterator var6 = events.entrySet().iterator();

         while(var6.hasNext()) {
            Map.Entry<EventKey, EventPanel> entry = (Map.Entry)var6.next();
            if (((EventKey)entry.getKey()).eventId.equals(eventId)) {
               keyToRemove = (EventKey)entry.getKey();
               removedPanel = (EventPanel)entry.getValue();
               break;
            }
         }

         if (keyToRemove != null) {
            events.remove(keyToRemove);
            if (removedPanel != null) {
               removedPanel.cancelTimer();
            }

            if (events.isEmpty()) {
               this.worldEventPanels.remove(worldId);
            }
         }
      }

   }

   public boolean hasEventPanel(int worldId, String eventId) {
      Map<EventKey, EventPanel> events = (Map)this.worldEventPanels.get(worldId);
      if (events == null) {
         return false;
      } else {
         Iterator var4 = events.keySet().iterator();

         EventKey key;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            key = (EventKey)var4.next();
         } while(!key.eventId.equals(eventId));

         return true;
      }
   }

   public void clearWorldEvents(int worldId) {
      Map<EventKey, EventPanel> removedWorldMap = (Map)this.worldEventPanels.remove(worldId);
      if (removedWorldMap != null) {
         Iterator var3 = removedWorldMap.values().iterator();

         while(var3.hasNext()) {
            EventPanel panel = (EventPanel)var3.next();
            panel.cancelTimer();
         }
      }

   }

   public void clearAllEvents() {
      Set<Integer> worldIdsToClear = new HashSet(this.worldEventPanels.keySet());
      Iterator var2 = worldIdsToClear.iterator();

      while(var2.hasNext()) {
         int worldId = (Integer)var2.next();
         this.clearWorldEvents(worldId);
      }

      this.worldEventPanels.clear();
   }

   public Set<Integer> getWorldEventPanelKeys() {
      return new HashSet(this.worldEventPanels.keySet());
   }

   public Set<String> getEventKeysForWorld(int worldId) {
      Map<EventKey, EventPanel> worldMap = (Map)this.worldEventPanels.get(worldId);
      if (worldMap != null && !worldMap.isEmpty()) {
         Set<String> eventIds = new HashSet();
         Iterator var4 = worldMap.keySet().iterator();

         while(var4.hasNext()) {
            EventKey key = (EventKey)var4.next();
            eventIds.add(key.eventId);
         }

         return eventIds;
      } else {
         return Collections.emptySet();
      }
   }

   public void rebuildEventUI() {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(this::rebuildEventUI);
      } else {
         int previousScrollValue = this.eventScrollPane.getVerticalScrollBar().getValue();
         this.mainEventDisplayPanel.removeAll();
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 1.0;
         gbc.fill = 2;
         gbc.anchor = 11;
         gbc.insets = new Insets(0, 0, 0, 0);
         Set<Integer> worldIds = this.worldEventPanels.keySet();
         if (worldIds.isEmpty()) {
            JLabel noEventsLabel = new JLabel("No active events.", 0);
            noEventsLabel.setFont(FontManager.getRunescapeFont());
            noEventsLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            gbc.weighty = 0.0;
            this.mainEventDisplayPanel.add(noEventsLabel, gbc);
            ++gbc.gridy;
            gbc.weighty = 1.0;
            gbc.fill = 3;
            this.mainEventDisplayPanel.add(Box.createVerticalGlue(), gbc);
         } else {
            List<Integer> sortedWorldIds = new ArrayList(worldIds);
            Collections.sort(sortedWorldIds);
            Iterator var5 = sortedWorldIds.iterator();

            while(var5.hasNext()) {
               int worldId = (Integer)var5.next();
               Map<EventKey, EventPanel> events = (Map)this.worldEventPanels.get(worldId);
               if (events != null && !events.isEmpty()) {
                  JLabel worldTitleLabel = new JLabel("World " + worldId);
                  worldTitleLabel.setFont(FontManager.getRunescapeSmallFont());
                  worldTitleLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
                  worldTitleLabel.setHorizontalAlignment(0);
                  worldTitleLabel.setOpaque(false);
                  JPanel titleWrapper = new JPanel(new BorderLayout());
                  titleWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
                  titleWrapper.setOpaque(true);
                  titleWrapper.add(worldTitleLabel, "Center");
                  titleWrapper.setBorder(new EmptyBorder(8, 2, 4, 2));
                  gbc.insets = new Insets(0, 0, 0, 0);
                  gbc.weighty = 0.0;
                  gbc.fill = 2;
                  this.mainEventDisplayPanel.add(titleWrapper, gbc);
                  ++gbc.gridy;
                  JPanel gridPanel = this.createWorldEventGridPanel(worldId);
                  gbc.insets = new Insets(0, 0, 10, 0);
                  gbc.weighty = 0.0;
                  gbc.fill = 2;
                  this.mainEventDisplayPanel.add(gridPanel, gbc);
                  ++gbc.gridy;
               }
            }

            ++gbc.gridy;
            gbc.weighty = 1.0;
            gbc.fill = 3;
            gbc.insets = new Insets(0, 0, 0, 0);
            this.mainEventDisplayPanel.add(Box.createVerticalGlue(), gbc);
         }

         this.mainEventDisplayPanel.revalidate();
         this.mainEventDisplayPanel.repaint();
         this.eventScrollPane.revalidate();
         this.eventScrollPane.repaint();
         this.restoreScrollPosition(this.eventScrollPane, previousScrollValue);
      }
   }

   private JPanel createWorldEventGridPanel(int worldId) {
      JPanel wrapPanel = new JPanel();
      wrapPanel.setLayout(new WrapLayout(0, 5, 5));
      wrapPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      Map<EventKey, EventPanel> events = (Map)this.worldEventPanels.get(worldId);
      if (events != null && !events.isEmpty()) {
         Iterator var6 = events.values().iterator();

         while(var6.hasNext()) {
            EventPanel panel = (EventPanel)var6.next();
            wrapPanel.add(panel);
         }
      } else {
         JLabel noEventsLabel = new JLabel("No active events for World " + worldId + ".");
         noEventsLabel.setFont(FontManager.getRunescapeSmallFont());
         noEventsLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
         noEventsLabel.setHorizontalAlignment(0);
         noEventsLabel.setPreferredSize(new Dimension(205, 30));
         wrapPanel.add(noEventsLabel);
      }

      return wrapPanel;
   }

   private void restoreScrollPosition(@Nullable JScrollPane scrollPane, int value) {
      if (scrollPane != null) {
         SwingUtilities.invokeLater(() -> {
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            int safeValue = Math.min(value, verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount());
            safeValue = Math.max(0, safeValue);
            verticalScrollBar.setValue(safeValue);
            scrollPane.revalidate();
            scrollPane.repaint();
         });
      }

   }

   public void updateBonusXpDisplay(List<BonusXpInfo> bonusXpData) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(() -> {
            this.updateBonusXpDisplay(bonusXpData);
         });
      } else {
         this.currentBonusXp.clear();
         this.currentBonusXp.addAll(bonusXpData);
         this.bonusXpGridPanel.removeAll();
         Font smallFont = FontManager.getRunescapeSmallFont();
         Iterator var3 = this.currentBonusXp.iterator();

         while(var3.hasNext()) {
            final BonusXpInfo info = (BonusXpInfo)var3.next();
            final JPanel cellPanel = new JPanel();
            cellPanel.setLayout(new BoxLayout(cellPanel, 1));
            cellPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            cellPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorScheme.MEDIUM_GRAY_COLOR, 1), new EmptyBorder(3, 3, 3, 3)));
            cellPanel.setAlignmentX(0.5F);
            cellPanel.setToolTipText("Hop to world " + info.getWorldId());
            JLabel worldLabel = new JLabel("W" + info.getWorldId());
            worldLabel.setFont(smallFont);
            worldLabel.setForeground(Color.WHITE);
            worldLabel.setAlignmentX(0.5F);
            JLabel iconLabel = new JLabel(new ImageIcon(info.getSkillIcon()));
            iconLabel.setAlignmentX(0.5F);
            JLabel timeLabel = new JLabel("00:00");
            timeLabel.setFont(smallFont);
            timeLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            timeLabel.setAlignmentX(0.5F);
            info.setTimeLabel(timeLabel);
            cellPanel.add(worldLabel);
            cellPanel.add(Box.createVerticalStrut(2));
            cellPanel.add(iconLabel);
            cellPanel.add(Box.createVerticalStrut(2));
            cellPanel.add(timeLabel);
            cellPanel.addMouseListener(new MouseAdapter() {
               public void mousePressed(MouseEvent e) {
                  cellPanel.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
               }

               public void mouseReleased(MouseEvent e) {
                  cellPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                  if (cellPanel.contains(e.getPoint()) && e.getButton() == 1 && AugustEventsPanel.this.bonusXpClickHandler != null) {
                     AugustEventsPanel.this.bonusXpClickHandler.accept(info.getWorldId());
                  }

               }

               public void mouseExited(MouseEvent e) {
                  cellPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
               }
            });
            this.bonusXpGridPanel.add(cellPanel);
         }

         this.updateBonusXpTimes();
         this.bonusXpGridPanel.revalidate();
         this.bonusXpGridPanel.repaint();
      }
   }

   public void setServerInfoText(String text) {
      this.serverInfoContent = text;
      this.updateServerInfoArea();
   }

   private void startUpdaters() {
      DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("UTC"));
      this.scheduler.scheduleAtFixedRate(() -> {
         this.updateServerInfoArea(utcFormatter);
         this.updateBonusXpTimes();
      }, 0L, 1L, TimeUnit.SECONDS);
   }

   private void updateServerInfoArea() {
      this.updateServerInfoArea(DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("UTC")));
   }

   private void updateServerInfoArea(DateTimeFormatter utcFormatter) {
      String currentTime = utcFormatter.format(Instant.now());
      String updatedText = "Server time: " + currentTime;
      if (this.serverInfoContent != null && !this.serverInfoContent.trim().isEmpty()) {
         updatedText = updatedText + "\n" + this.serverInfoContent;
      }

      String textSnapshot = updatedText;
      SwingUtilities.invokeLater(() -> {
         this.serverInfoTextArea.setText(textSnapshot);
      });
   }

   private void updateBonusXpTimes() {
      Instant now = Instant.now();
      SwingUtilities.invokeLater(() -> {
         Iterator var2 = this.currentBonusXp.iterator();

         while(true) {
            BonusXpInfo info;
            JLabel label;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               info = (BonusXpInfo)var2.next();
               label = info.getTimeLabel();
            } while(label == null);

            Duration remaining = Duration.between(now, info.getEndTime());
            String timeText;
            if (!remaining.isNegative() && !remaining.isZero()) {
               long totalMinutes = remaining.toMinutes();
               long hours = totalMinutes / 60L;
               long minutes = totalMinutes % 60L;
               timeText = String.format("%02d:%02d", hours, minutes);
            } else {
               timeText = "Ended";
            }

            label.setText(timeText);
         }
      });
   }

   public void shutdown() {
      this.scheduler.shutdownNow();
      this.clearAllEvents();
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.clearAllEvents();
      this.rebuildEventUI();
   }

   private static class EventKey implements Comparable<EventKey> {
      int renderOrder;
      String eventId;

      public EventKey(int renderOrder, String eventId) {
         this.renderOrder = renderOrder;
         this.eventId = eventId;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            EventKey eventKey = (EventKey)o;
            return this.renderOrder == eventKey.renderOrder && Objects.equals(this.eventId, eventKey.eventId);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.renderOrder, this.eventId});
      }

      public int compareTo(@NotNull @NotNull EventKey o) {
         int orderComparison = Integer.compare(this.renderOrder, o.renderOrder);
         return orderComparison != 0 ? orderComparison : this.eventId.compareTo(o.eventId);
      }
   }
}
