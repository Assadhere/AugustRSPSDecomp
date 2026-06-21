package net.runelite.client.plugins.config;

import com.google.common.collect.ImmutableList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigDescriptor;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.MultiplexingPluginPanel;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class PluginListPanel extends PluginPanel {
   private static final Logger log = LoggerFactory.getLogger(PluginListPanel.class);
   private static final String RUNELITE_GROUP_NAME = ((ConfigGroup)RuneLiteConfig.class.getAnnotation(ConfigGroup.class)).value();
   private static final String PINNED_PLUGINS_CONFIG_KEY = "pinnedPlugins";
   private static final ImmutableList<String> HIDDEN_WHITELISTED_PLUGINS = ImmutableList.of("August Donator Info", "August Events", "August Prestige Info");
   private static final ImmutableList<String> CATEGORY_TAGS = ImmutableList.of("Combat", "Chat", "Item", "Minigame", "Notification", "Plugin Hub", "Skilling", "XP");
   private final ConfigManager configManager;
   private final PluginManager pluginManager;
   private final Provider<ConfigPanel> configPanelProvider;
   private final List<PluginConfigurationDescriptor> fakePlugins = new ArrayList();
   private final ExternalPluginManager externalPluginManager;
   private final MultiplexingPluginPanel muxer;
   private final IconTextField searchBar;
   private final JScrollPane scrollPane;
   private final FixedWidthPanel mainPanel;
   private List<PluginListItem> pluginList;

   @Inject
   public PluginListPanel(ConfigManager configManager, PluginManager pluginManager, ExternalPluginManager externalPluginManager, final EventBus eventBus, Provider<ConfigPanel> configPanelProvider) {
      super(false);
      this.configManager = configManager;
      this.pluginManager = pluginManager;
      this.externalPluginManager = externalPluginManager;
      this.configPanelProvider = configPanelProvider;
      this.muxer = new MultiplexingPluginPanel(this) {
         protected void onAdd(PluginPanel p) {
            eventBus.register(p);
         }

         protected void onRemove(PluginPanel p) {
            eventBus.unregister((Object)p);
         }
      };
      this.searchBar = new IconTextField();
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      this.searchBar.setPreferredSize(new Dimension(205, 30));
      this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
      this.searchBar.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent e) {
            PluginListPanel.this.onSearchBarChanged();
         }

         public void removeUpdate(DocumentEvent e) {
            PluginListPanel.this.onSearchBarChanged();
         }

         public void changedUpdate(DocumentEvent e) {
            PluginListPanel.this.onSearchBarChanged();
         }
      });
      ImmutableList var10000 = CATEGORY_TAGS;
      DefaultListModel var10001 = this.searchBar.getSuggestionListModel();
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::addElement);
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel topPanel = new JPanel();
      topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      topPanel.setLayout(new BorderLayout(0, 6));
      topPanel.add(this.searchBar, "Center");
      this.add(topPanel, "North");
      this.mainPanel = new FixedWidthPanel();
      this.mainPanel.setBorder(new EmptyBorder(8, 10, 10, 10));
      this.mainPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));
      this.mainPanel.setAlignmentX(0.0F);
      JPanel northPanel = new FixedWidthPanel();
      ((JPanel)northPanel).setLayout(new BorderLayout());
      ((JPanel)northPanel).add(this.mainPanel, "North");
      this.scrollPane = new JScrollPane(northPanel);
      this.scrollPane.setHorizontalScrollBarPolicy(31);
      this.add(this.scrollPane, "Center");
   }

   void rebuildPluginList() {
      List<String> pinnedPlugins = this.getPinnedPluginNames();
      this.pluginList = (List)Stream.concat(this.fakePlugins.stream(), this.pluginManager.getPlugins().stream().filter((plugin) -> {
         return !((PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class)).hidden();
      }).filter((plugin) -> {
         return !HIDDEN_WHITELISTED_PLUGINS.contains(((PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class)).name());
      }).map((plugin) -> {
         PluginDescriptor descriptor = (PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class);
         Config config = this.pluginManager.getPluginConfigProxy(plugin);
         ConfigDescriptor configDescriptor = config == null ? null : this.configManager.getConfigDescriptor(config);
         List<String> conflicts = (List)this.pluginManager.conflictsForPlugin(plugin).stream().map(Plugin::getName).collect(Collectors.toList());
         return new PluginConfigurationDescriptor(descriptor.name(), descriptor.description(), descriptor.tags(), plugin, config, configDescriptor, conflicts);
      })).map((desc) -> {
         PluginListItem listItem = new PluginListItem(this, desc);
         listItem.setPinned(pinnedPlugins.contains(desc.getName().replace(",", "")));
         return listItem;
      }).sorted(Comparator.comparing((p) -> {
         return p.getPluginConfig().getName();
      })).collect(Collectors.toList());
      this.mainPanel.removeAll();
      this.refresh();
   }

   void addFakePlugin(PluginConfigurationDescriptor... descriptor) {
      Collections.addAll(this.fakePlugins, descriptor);
   }

   void refresh() {
      this.pluginList.forEach((listItem) -> {
         Plugin plugin = listItem.getPluginConfig().getPlugin();
         if (plugin != null) {
            listItem.setPluginEnabled(this.pluginManager.isPluginActive(plugin));
         }

      });
      int scrollBarPosition = this.scrollPane.getVerticalScrollBar().getValue();
      this.onSearchBarChanged();
      this.searchBar.requestFocusInWindow();
      this.validate();
      this.scrollPane.getVerticalScrollBar().setValue(scrollBarPosition);
   }

   void openWithFilter(String filter) {
      this.searchBar.setText(filter);
      this.onSearchBarChanged();
      this.muxer.pushState(this);
   }

   private void onSearchBarChanged() {
      String text = this.searchBar.getText();
      List var10000 = this.pluginList;
      FixedWidthPanel var10001 = this.mainPanel;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::remove);
      var10000 = PluginSearch.search(this.pluginList, text);
      var10001 = this.mainPanel;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::add);
      this.revalidate();
   }

   void openConfigurationPanel(String configGroup) {
      Iterator var2 = this.pluginList.iterator();

      while(var2.hasNext()) {
         PluginListItem pluginListItem = (PluginListItem)var2.next();
         if (pluginListItem.getPluginConfig().getName().equals(configGroup)) {
            this.openConfigurationPanel(pluginListItem.getPluginConfig());
            break;
         }
      }

   }

   void openConfigurationPanel(Plugin plugin) {
      Iterator var2 = this.pluginList.iterator();

      while(var2.hasNext()) {
         PluginListItem pluginListItem = (PluginListItem)var2.next();
         if (pluginListItem.getPluginConfig().getPlugin() == plugin) {
            this.openConfigurationPanel(pluginListItem.getPluginConfig());
            break;
         }
      }

   }

   void openConfigurationPanel(PluginConfigurationDescriptor plugin) {
      ConfigPanel panel = (ConfigPanel)this.configPanelProvider.get();
      panel.init(plugin);
      this.muxer.pushState(this);
      this.muxer.pushState(panel);
   }

   void startPlugin(Plugin plugin) {
      this.pluginManager.setPluginEnabled(plugin, true);

      try {
         this.pluginManager.startPlugin(plugin);
      } catch (PluginInstantiationException var3) {
         PluginInstantiationException ex = var3;
         log.warn("Error when starting plugin {}", plugin.getClass().getSimpleName(), ex);
      }

   }

   void stopPlugin(Plugin plugin) {
      this.pluginManager.setPluginEnabled(plugin, false);

      try {
         this.pluginManager.stopPlugin(plugin);
      } catch (PluginInstantiationException var3) {
         PluginInstantiationException ex = var3;
         log.warn("Error when stopping plugin {}", plugin.getClass().getSimpleName(), ex);
      }

   }

   private List<String> getPinnedPluginNames() {
      String config = this.configManager.getConfiguration(RUNELITE_GROUP_NAME, "pinnedPlugins");
      return config == null ? Collections.emptyList() : Text.fromCSV(config);
   }

   void savePinnedPlugins() {
      String value = (String)this.pluginList.stream().filter(PluginListItem::isPinned).map((p) -> {
         return p.getPluginConfig().getName().replace(",", "");
      }).collect(Collectors.joining(","));
      this.configManager.setConfiguration(RUNELITE_GROUP_NAME, "pinnedPlugins", value);
   }

   @Subscribe
   public void onPluginChanged(PluginChanged event) {
      SwingUtilities.invokeLater(this::refresh);
   }

   public Dimension getPreferredSize() {
      return new Dimension(242, super.getPreferredSize().height);
   }

   public void onActivate() {
      super.onActivate();
      if (this.searchBar.getParent() != null) {
         this.searchBar.requestFocusInWindow();
      }

   }

   @Subscribe
   private void onExternalPluginsChanged(ExternalPluginsChanged ev) {
      SwingUtilities.invokeLater(this::rebuildPluginList);
   }

   @Subscribe
   private void onProfileChanged(ProfileChanged ev) {
      SwingUtilities.invokeLater(this::rebuildPluginList);
   }

   public ExternalPluginManager getExternalPluginManager() {
      return this.externalPluginManager;
   }

   public MultiplexingPluginPanel getMuxer() {
      return this.muxer;
   }
}
