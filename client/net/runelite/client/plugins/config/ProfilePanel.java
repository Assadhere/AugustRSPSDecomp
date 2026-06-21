package net.runelite.client.plugins.config;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import net.runelite.client.RuneLite;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.ConfigProfile;
import net.runelite.client.config.ProfileManager;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.plugins.screenmarkers.ScreenMarkerPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.DragAndDropReorderPane;
import net.runelite.client.ui.components.MouseDragEventForwarder;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProfilePanel extends PluginPanel {
   private static final Logger log = LoggerFactory.getLogger(ProfilePanel.class);
   private static final int MAX_PROFILES = 20;
   private static final ImageIcon ADD_ICON = new ImageIcon(ImageUtil.loadImageResource(ScreenMarkerPlugin.class, "add_icon.png"));
   private static final ImageIcon DELETE_ICON = new ImageIcon(ImageUtil.loadImageResource(ProfilePanel.class, "mdi_delete.png"));
   private static final ImageIcon EXPORT_ICON = new ImageIcon(ImageUtil.loadImageResource(ProfilePanel.class, "mdi_export.png"));
   private static final ImageIcon RENAME_ICON;
   private static final ImageIcon RENAME_ACTIVE_ICON;
   private static final ImageIcon CLONE_ICON = new ImageIcon(ImageUtil.loadImageResource(ProfilePanel.class, "mdi_content-duplicate.png"));
   private static final ImageIcon LINK_ICON;
   private static final ImageIcon LINK_ACTIVE_ICON;
   private static final ImageIcon ARROW_RIGHT_ICON = new ImageIcon(ImageUtil.loadImageResource(ProfilePanel.class, "/util/arrow_right.png"));
   private static final ImageIcon SYNC_ICON;
   private static final ImageIcon SYNC_ACTIVE_ICON;
   private final ConfigManager configManager;
   private final ProfileManager profileManager;
   private final SessionManager sessionManager;
   private final ScheduledExecutorService executor;
   private final DragAndDropReorderPane profilesList;
   private final JButton addButton;
   private final JButton importButton;
   private Map<Long, ProfileCard> cards = new HashMap();
   private File lastFileChooserDirectory;
   private boolean active;

   @Inject
   ProfilePanel(ConfigManager configManager, ProfileManager profileManager, SessionManager sessionManager, ScheduledExecutorService executor) {
      this.lastFileChooserDirectory = RuneLite.RUNELITE_DIR;
      this.profileManager = profileManager;
      this.configManager = configManager;
      this.sessionManager = sessionManager;
      this.executor = executor;
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      GroupLayout layout = new GroupLayout(this);
      this.setLayout(layout);
      this.profilesList = new DragAndDropReorderPane();
      this.profilesList.addDragListener(this::handleDrag);
      this.addButton = new JButton("New Profile", ADD_ICON);
      this.addButton.addActionListener((ev) -> {
         this.createProfile();
      });
      this.importButton = new JButton("Import Profile");
      this.importButton.addActionListener((ev) -> {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setDialogTitle("Profile import");
         fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("RuneLite properties", new String[]{"properties"}));
         fileChooser.setAcceptAllFileFilterUsed(false);
         fileChooser.setCurrentDirectory(this.lastFileChooserDirectory);
         int selection = fileChooser.showOpenDialog(this);
         if (selection == 0) {
            File file = fileChooser.getSelectedFile();
            this.lastFileChooserDirectory = file.getParentFile();
            this.importProfile(file);
         }

      });
      JLabel info = new JLabel("<html>Profiles are separate sets of plugins and settings that you can switch between at any time.");
      layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.profilesList).addGap(8).addGroup(layout.createParallelGroup().addComponent(this.addButton).addComponent(this.importButton)).addGap(8).addComponent(info));
      layout.setHorizontalGroup(layout.createParallelGroup().addComponent(this.profilesList).addGroup(layout.createSequentialGroup().addComponent(this.addButton).addGap(8).addComponent(this.importButton)).addComponent(info));
      Object refresh = "this could just be a lambda, but no, it has to be abstracted";
      this.getInputMap(1).put(KeyStroke.getKeyStroke(116, 0), refresh);
      this.getActionMap().put(refresh, new AbstractAction() {
         public void actionPerformed(ActionEvent e) {
            ProfilePanel.this.reload();
         }
      });
   }

   public void onActivate() {
      this.active = true;
      this.reload();
   }

   public void onDeactivate() {
      this.active = false;
      SwingUtil.fastRemoveAll(this.profilesList);
      this.cards.clear();
   }

   @Subscribe
   private void onProfileChanged(ProfileChanged ev) {
      if (this.active) {
         SwingUtilities.invokeLater(() -> {
            Iterator var1 = this.cards.values().iterator();

            while(var1.hasNext()) {
               ProfileCard cardx = (ProfileCard)var1.next();
               cardx.setActive(false);
            }

            ProfileCard card = (ProfileCard)this.cards.get(this.configManager.getProfile().getId());
            if (card == null) {
               this.reload();
            } else {
               card.setActive(true);
            }
         });
      }
   }

   @Subscribe
   private void onRuneScapeProfileChanged(RuneScapeProfileChanged ev) {
      if (this.active) {
         this.reload();
      }
   }

   @Subscribe
   public void onSessionOpen(SessionOpen sessionOpen) {
      if (this.active) {
         this.reload();
      }
   }

   @Subscribe
   public void onSessionClose(SessionClose sessionClose) {
      if (this.active) {
         this.reload();
      }
   }

   private void reload() {
      this.executor.submit(() -> {
         ProfileManager.Lock lock = this.profileManager.lock();

         try {
            this.reload(lock.getProfiles());
         } catch (Throwable var5) {
            if (lock != null) {
               try {
                  lock.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (lock != null) {
            lock.close();
         }

      });
   }

   private void reload(List<ConfigProfile> profiles) {
      SwingUtilities.invokeLater(() -> {
         SwingUtil.fastRemoveAll(this.profilesList);
         Map<Long, ProfileCard> prevCards = this.cards;
         this.cards = new HashMap();
         long activePanel = this.configManager.getProfile().getId();
         String rsProfileKey = this.configManager.getRSProfileKey();
         boolean limited = profiles.stream().filter((v) -> {
            return !v.isInternal();
         }).count() >= 20L;
         Iterator var7 = profiles.iterator();

         while(true) {
            ConfigProfile profile;
            do {
               if (!var7.hasNext()) {
                  this.addButton.setEnabled(!limited);
                  this.importButton.setEnabled(!limited);
                  this.profilesList.revalidate();
                  return;
               }

               profile = (ConfigProfile)var7.next();
            } while(profile.isInternal());

            ProfileCard prev = (ProfileCard)prevCards.get(profile.getId());
            long id = profile.getId();
            List<String> defaultForRsProfiles = profile.getDefaultForRsProfiles();
            ProfileCard pc = new ProfileCard(profile, activePanel == id, defaultForRsProfiles != null && defaultForRsProfiles.contains(rsProfileKey), limited, prev);
            this.cards.put(profile.getId(), pc);
            this.profilesList.add(pc);
         }
      });
   }

   private void createProfile() {
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         String name = "New Profile";
         int number = 1;

         while(true) {
            if (lock.findProfile(name) == null) {
               log.info("Creating new profile: {}", name);
               lock.createProfile(name);
               this.reload(lock.getProfiles());
               break;
            }

            int var10000 = number++;
            name = "New Profile (" + var10000 + ")";
         }
      } catch (Throwable var5) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (lock != null) {
         lock.close();
      }

   }

   private void deleteProfile(ConfigProfile profile) {
      log.info("Deleting profile {}", profile.getName());
      this.configManager.toggleSync(profile, false);
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         lock.removeProfile(profile.getId());
         this.reload(lock.getProfiles());
      } catch (Throwable var6) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (lock != null) {
         lock.close();
      }

   }

   private void renameProfile(long id, String name) {
      ProfileManager.Lock lock = this.profileManager.lock();

      label43: {
         try {
            ConfigProfile profile = lock.findProfile(id);
            if (profile == null) {
               log.warn("rename for nonexistent profile {}", id);
               this.reload(lock.getProfiles());
               break label43;
            }

            log.info("Renaming profile {} ({}) to {}", new Object[]{profile, profile.getId(), name});
            lock.renameProfile(profile, name);
            this.configManager.renameProfile(profile, name);
            this.reload(lock.getProfiles());
         } catch (Throwable var8) {
            if (lock != null) {
               try {
                  lock.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (lock != null) {
            lock.close();
         }

         return;
      }

      if (lock != null) {
         lock.close();
      }

   }

   private void switchToProfile(long id) {
      ProfileManager.Lock lock = this.profileManager.lock();

      label43: {
         ConfigProfile profile;
         try {
            profile = lock.findProfile(id);
            if (profile == null) {
               log.warn("change to nonexistent profile {}", id);
               this.reload(lock.getProfiles());
               break label43;
            }

            log.debug("Switching profile to {}", profile.getName());
            lock.getProfiles().forEach((p) -> {
               p.setActive(false);
            });
            profile.setActive(true);
            lock.dirty();
         } catch (Throwable var8) {
            if (lock != null) {
               try {
                  lock.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (lock != null) {
            lock.close();
         }

         this.executor.submit(() -> {
            this.configManager.switchProfile(profile);
         });
         return;
      }

      if (lock != null) {
         lock.close();
      }

   }

   private void unsetRsProfileDefaultProfile() {
      this.setRsProfileDefaultProfile(-1L);
   }

   private void setRsProfileDefaultProfile(long id) {
      this.executor.submit(() -> {
         boolean switchProfile = false;
         ProfileManager.Lock lock = this.profileManager.lock();

         label86: {
            label87: {
               try {
                  String rsProfileKey = this.configManager.getRSProfileKey();
                  if (rsProfileKey != null) {
                     Iterator var6 = lock.getProfiles().iterator();

                     while(var6.hasNext()) {
                        ConfigProfile profilex = (ConfigProfile)var6.next();
                        List<String> defaultForRsProfiles = profilex.getDefaultForRsProfiles();
                        if (defaultForRsProfiles != null && profilex.getDefaultForRsProfiles().remove(rsProfileKey)) {
                           lock.dirty();
                        }
                     }

                     if (id == -1L) {
                        log.debug("Unsetting default profile for rsProfile {}", rsProfileKey);
                     } else {
                        ConfigProfile profile = lock.findProfile(id);
                        if (profile == null) {
                           log.warn("setting nonexistent profile {} as default for rsprofile", id);
                           this.reload(lock.getProfiles());
                           break label86;
                        }

                        log.debug("Setting profile {} as default for rsProfile {}", profile.getName(), rsProfileKey);
                        if (profile.getDefaultForRsProfiles() == null) {
                           profile.setDefaultForRsProfiles(new ArrayList());
                        }

                        profile.getDefaultForRsProfiles().add(rsProfileKey);
                        switchProfile = !profile.isActive();
                        lock.dirty();
                     }

                     this.reload(lock.getProfiles());
                     break label87;
                  }
               } catch (Throwable var10) {
                  if (lock != null) {
                     try {
                        lock.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (lock != null) {
                  lock.close();
               }

               return;
            }

            if (lock != null) {
               lock.close();
            }

            if (switchProfile) {
               this.switchToProfile(id);
            }

            return;
         }

         if (lock != null) {
            lock.close();
         }

      });
   }

   private void exportProfile(ConfigProfile profile, File file) {
      log.info("Exporting profile {} to {}", profile.getName(), file);
      this.executor.execute(() -> {
         this.configManager.sendConfig();
         File source = ProfileManager.profileConfigFile(profile);
         if (!source.exists()) {
            SwingUtilities.invokeLater(() -> {
               JOptionPane.showMessageDialog((Component)null, "Profile '" + profile.getName() + "' can not be exported because it has no settings.");
            });
         } else {
            try {
               Files.copy(source.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException var5) {
               IOException e = var5;
               log.error("error performing profile export", e);
            }

         }
      });
   }

   private void importProfile(File file) {
      log.info("Importing profile from {}", file);
      this.executor.execute(() -> {
         ProfileManager.Lock lock = this.profileManager.lock();

         try {
            String name = "Imported Profile";
            int number = 1;

            while(true) {
               if (lock.findProfile(name) == null) {
                  log.debug("selected new profile name: {}", name);
                  ConfigProfile profile = lock.createProfile(name);
                  this.reload(lock.getProfiles());
                  this.configManager.importAndMigrate(lock, file, profile);
                  break;
               }

               int var10000 = number++;
               name = "Imported Profile (" + var10000 + ")";
            }
         } catch (Throwable var7) {
            if (lock != null) {
               try {
                  lock.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (lock != null) {
            lock.close();
         }

      });
   }

   private void cloneProfile(ConfigProfile profile) {
      this.executor.execute(() -> {
         this.configManager.sendConfig();
         ProfileManager.Lock lock = this.profileManager.lock();

         try {
            int num = 1;

            String name;
            do {
               String var10000 = profile.getName();
               name = var10000 + " (" + num++ + ")";
            } while(lock.findProfile(name) != null);

            log.info("Cloning profile {} to {}", profile.getName(), name);
            ConfigProfile clonedProfile = lock.createProfile(name);
            this.reload(lock.getProfiles());
            File from = ProfileManager.profileConfigFile(profile);
            File to = ProfileManager.profileConfigFile(clonedProfile);
            if (from.exists()) {
               try {
                  Files.copy(from.toPath(), to.toPath());
               } catch (IOException var10) {
                  IOException e = var10;
                  log.error("error cloning profile", e);
               }
            }
         } catch (Throwable var11) {
            if (lock != null) {
               try {
                  lock.close();
               } catch (Throwable var9) {
                  var11.addSuppressed(var9);
               }
            }

            throw var11;
         }

         if (lock != null) {
            lock.close();
         }

      });
   }

   private void toggleSync(ActionEvent event, ConfigProfile profile, boolean sync) {
      log.info("{} sync for: {}", sync ? "Enabling" : "Disabling", profile.getName());
      this.configManager.toggleSync(profile, sync);
      ((JToggleButton)event.getSource()).setToolTipText(sync ? "Disable cloud sync" : "Enable cloud sync");
   }

   private void handleDrag(Component component) {
      ProfileCard c = (ProfileCard)component;
      int newPosition = this.profilesList.getPosition(component);
      log.debug("Drag profile {} to position {}", c.profile.getName(), newPosition);
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         List<ConfigProfile> profiles = lock.getProfiles();
         profiles.sort(Comparator.comparing((p) -> {
            Component[] components = this.profilesList.getComponents();

            for(int idx = 0; idx < components.length; ++idx) {
               ProfileCard card = (ProfileCard)components[idx];
               if (card.profile.getId() == p.getId()) {
                  return idx;
               }
            }

            return -1;
         }));
         lock.dirty();
         this.reload(profiles);
      } catch (Throwable var8) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (lock != null) {
         lock.close();
      }

   }

   static {
      BufferedImage rename = ImageUtil.loadImageResource(ProfilePanel.class, "mdi_rename.png");
      RENAME_ICON = new ImageIcon(rename);
      RENAME_ACTIVE_ICON = new ImageIcon(ImageUtil.recolorImage(rename, ColorScheme.BRAND_ORANGE));
      BufferedImage link = ImageUtil.loadImageResource(ProfilePanel.class, "/util/link.png");
      LINK_ICON = new ImageIcon(link);
      LINK_ACTIVE_ICON = new ImageIcon(ImageUtil.recolorImage(link, ColorScheme.BRAND_ORANGE));
      BufferedImage sync = ImageUtil.loadImageResource(ProfilePanel.class, "cloud_sync.png");
      SYNC_ICON = new ImageIcon(sync);
      SYNC_ACTIVE_ICON = new ImageIcon(ImageUtil.recolorImage(sync, ColorScheme.BRAND_ORANGE));
   }

   private class ProfileCard extends JPanel {
      private static final int CARD_WIDTH = 227;
      private static final int LEFT_BORDER_WIDTH = 4;
      private static final int LEFT_GAP = 4;
      private final ConfigProfile profile;
      private final JButton delete;
      private final JTextField name;
      private final JButton activate;
      private final JPanel buttons;
      private final JToggleButton rename;
      private boolean expanded;
      private boolean active;

      private ProfileCard(final ConfigProfile profile, boolean isActive, boolean rsProfileDefault, boolean limited, ProfileCard prev) {
         this.profile = profile;
         this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         this.name = new JTextField();
         this.name.setText(profile.getName());
         this.name.setEditable(false);
         this.name.setEnabled(false);
         this.name.setOpaque(false);
         this.name.setBorder((Border)null);
         this.name.addActionListener((ev) -> {
            this.stopRenaming(true);
         });
         this.name.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == 27) {
                  ProfileCard.this.stopRenaming(false);
               }

            }
         });
         ((AbstractDocument)this.name.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
               super.insertString(fb, offset, this.filter(string), attr);
            }

            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
               super.replace(fb, offset, length, this.filter(text), attrs);
            }

            private String filter(String in) {
               return CharMatcher.noneOf("/\\<>:\"|?*\r\n\u0000$").retainFrom(in);
            }
         });
         this.activate = new JButton(ProfilePanel.ARROW_RIGHT_ICON);
         this.activate.setDisabledIcon(ProfilePanel.ARROW_RIGHT_ICON);
         this.activate.addActionListener((ev) -> {
            ProfilePanel.this.switchToProfile(profile.getId());
         });
         SwingUtil.removeButtonDecorations(this.activate);
         this.buttons = new JPanel();
         this.buttons.setOpaque(false);
         this.buttons.setLayout(new GridLayout(1, 0, 0, 0));
         this.rename = new JToggleButton(ProfilePanel.RENAME_ICON);
         this.rename.setSelectedIcon(ProfilePanel.RENAME_ACTIVE_ICON);
         this.rename.setToolTipText("Rename profile");
         SwingUtil.removeButtonDecorations(this.rename);
         this.rename.addActionListener((ev) -> {
            if (this.rename.isSelected()) {
               this.startRenaming();
            } else {
               this.stopRenaming(true);
            }

         });
         this.buttons.add(this.rename);
         JButton clone = new JButton(ProfilePanel.CLONE_ICON);
         clone.setToolTipText("Duplicate profile");
         SwingUtil.removeButtonDecorations(clone);
         clone.addActionListener((ev) -> {
            ProfilePanel.this.cloneProfile(profile);
         });
         clone.setEnabled(!limited);
         this.buttons.add(clone);
         JButton export = new JButton(ProfilePanel.EXPORT_ICON);
         export.setToolTipText("Export profile");
         SwingUtil.removeButtonDecorations(export);
         export.addActionListener((ev) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Profile export");
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("RuneLite properties", new String[]{"properties"}));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setCurrentDirectory(ProfilePanel.this.lastFileChooserDirectory);
            fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), profile.getName() + ".properties"));
            int selection = fileChooser.showSaveDialog(this);
            if (selection == 0) {
               File file = fileChooser.getSelectedFile();
               ProfilePanel.this.lastFileChooserDirectory = file.getParentFile();
               if (!file.getName().endsWith(".properties")) {
                  file = new File(file.getParentFile(), file.getName() + ".properties");
               }

               ProfilePanel.this.exportProfile(profile, file);
            }

         });
         this.buttons.add(export);
         JToggleButton defaultForRsProfile;
         if (ProfilePanel.this.configManager.getRSProfileKey() != null) {
            defaultForRsProfile = new JToggleButton(ProfilePanel.LINK_ICON);
            SwingUtil.removeButtonDecorations(defaultForRsProfile);
            defaultForRsProfile.setSelectedIcon(ProfilePanel.LINK_ACTIVE_ICON);
            defaultForRsProfile.setSelected(rsProfileDefault);
            List<String> defaultForRsProfiles = profile.getDefaultForRsProfiles();
            StringBuilder tooltip = new StringBuilder("<html>");
            if (defaultForRsProfiles != null && !defaultForRsProfiles.isEmpty()) {
               tooltip.append("This profile is the default for the following RuneScape accounts:");
               Iterator var12 = profile.getDefaultForRsProfiles().iterator();

               while(var12.hasNext()) {
                  String rsProfileKey = (String)var12.next();
                  String ign = ProfilePanel.this.configManager.getConfiguration("rsprofile", rsProfileKey, "displayName");
                  if (!Strings.isNullOrEmpty(ign)) {
                     RuneScapeProfileType worldType = (RuneScapeProfileType)ProfilePanel.this.configManager.getConfiguration((String)"rsprofile", rsProfileKey, "type", (Type)RuneScapeProfileType.class);
                     tooltip.append("<br>");
                     tooltip.append(ign);
                     if (worldType != RuneScapeProfileType.STANDARD) {
                        tooltip.append(" (").append(Text.titleCase(worldType)).append(')');
                     }
                  }
               }
            } else {
               tooltip.append("Set profile as default for the current RuneScape account");
            }

            tooltip.append("</html>");
            defaultForRsProfile.setToolTipText(tooltip.toString());
            defaultForRsProfile.addActionListener((ev) -> {
               if (rsProfileDefault) {
                  ProfilePanel.this.unsetRsProfileDefaultProfile();
               } else {
                  ProfilePanel.this.setRsProfileDefaultProfile(profile.getId());
               }

            });
            this.buttons.add(defaultForRsProfile);
         }

         if (ProfilePanel.this.sessionManager.getAccountSession() != null) {
            defaultForRsProfile = new JToggleButton(ProfilePanel.SYNC_ICON);
            SwingUtil.removeButtonDecorations(defaultForRsProfile);
            defaultForRsProfile.setSelectedIcon(ProfilePanel.SYNC_ACTIVE_ICON);
            defaultForRsProfile.setToolTipText(profile.isSync() ? "Disable cloud sync" : "Enable cloud sync");
            defaultForRsProfile.setSelected(profile.isSync());
            defaultForRsProfile.addActionListener((ev) -> {
               ProfilePanel.this.toggleSync(ev, profile, defaultForRsProfile.isSelected());
            });
            this.buttons.add(defaultForRsProfile);
         }

         this.delete = new JButton(ProfilePanel.DELETE_ICON);
         this.delete.setToolTipText("Delete profile");
         SwingUtil.removeButtonDecorations(this.delete);
         this.delete.addActionListener((ev) -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this profile?", "Warning", 2);
            if (confirm == 0) {
               ProfilePanel.this.deleteProfile(profile);
            }

         });
         this.buttons.add(this.delete);
         int maxButtonsWidth = 219 - this.activate.getPreferredSize().width;
         if (this.buttons.getPreferredSize().width > maxButtonsWidth) {
            this.buttons.setMinimumSize(new Dimension(maxButtonsWidth, this.buttons.getMinimumSize().height));
            this.buttons.setPreferredSize(new Dimension(maxButtonsWidth, this.buttons.getPreferredSize().height));
         }

         GroupLayout layout = new GroupLayout(this);
         this.setLayout(layout);
         layout.setVerticalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addComponent(this.name, 24, 24, 24).addComponent(this.buttons)).addComponent(this.activate, -1, -1, 32767));
         layout.setHorizontalGroup(layout.createSequentialGroup().addGap(4).addGroup(layout.createParallelGroup().addComponent(this.name).addComponent(this.buttons)).addComponent(this.activate));
         MouseAdapter expandListener = new MouseDragEventForwarder(ProfilePanel.this.profilesList) {
            public void mouseClicked(MouseEvent ev) {
               if (this.disabled(ev)) {
                  if (ev.getClickCount() == 2) {
                     if (!ProfileCard.this.active) {
                        ProfilePanel.this.switchToProfile(profile.getId());
                     }
                  } else {
                     ProfileCard.this.setExpanded(!ProfileCard.this.expanded);
                  }
               }

            }

            public void mouseEntered(MouseEvent ev) {
               if (this.disabled(ev)) {
                  ProfileCard.this.setBackground(ColorScheme.DARK_GRAY_COLOR);
               }

            }

            public void mouseExited(MouseEvent ev) {
               if (this.disabled(ev)) {
                  ProfileCard.this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
               }

            }

            private boolean disabled(MouseEvent ev) {
               Component target = ev.getComponent();
               if (target instanceof JButton) {
                  return !target.isEnabled();
               } else if (target instanceof JTextField) {
                  return !((JTextField)target).isEditable();
               } else {
                  return true;
               }
            }
         };
         this.addMouseListener(expandListener);
         this.addMouseMotionListener(expandListener);
         this.name.addMouseListener(expandListener);
         this.name.addMouseMotionListener(expandListener);
         this.activate.addMouseListener(expandListener);
         this.activate.addMouseMotionListener(expandListener);
         this.setActive(isActive);
         this.setExpanded(prev != null && prev.expanded);
      }

      void setActive(boolean active) {
         this.active = active;
         this.setBorder(new MatteBorder(0, 4, 0, 0, active ? ColorScheme.BRAND_ORANGE : ColorScheme.DARKER_GRAY_COLOR));
         this.delete.setEnabled(!active);
         this.activate.setEnabled(this.expanded && !active);
      }

      void setExpanded(boolean expanded) {
         this.expanded = expanded;
         this.buttons.setVisible(expanded);
         this.activate.setEnabled(expanded && !this.active);
         if (this.rename.isSelected()) {
            this.stopRenaming(true);
         }

         this.revalidate();
      }

      private void startRenaming() {
         this.name.setEnabled(true);
         this.name.setEditable(true);
         this.name.setOpaque(true);
         this.name.requestFocusInWindow();
         this.name.selectAll();
      }

      private void stopRenaming(boolean save) {
         this.name.setEditable(false);
         this.name.setEnabled(false);
         this.name.setOpaque(false);
         this.rename.setSelected(false);
         if (save) {
            ProfilePanel.this.renameProfile(this.profile.getId(), this.name.getText().trim());
         } else {
            this.name.setText(this.profile.getName());
         }

      }
   }
}
