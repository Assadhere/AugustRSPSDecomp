package net.runelite.client.plugins.hiscore;

import com.google.common.collect.EvictingQueue;
import com.google.inject.Inject;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import lombok.NonNull;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Friend;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.Nameable;
import net.runelite.api.NameableContainer;
import net.runelite.api.WorldView;
import net.runelite.api.clan.ClanMember;
import net.runelite.api.clan.ClanSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class NameAutocompleter implements KeyListener {
   private static final Logger log = LoggerFactory.getLogger(NameAutocompleter.class);
   private static final String NBSP = Character.toString(' ');
   private static final Pattern INVALID_CHARS = Pattern.compile("[^a-zA-Z0-9_ -]");
   private static final int MAX_SEARCH_HISTORY = 25;
   private final Client client;
   private final HiscoreConfig hiscoreConfig;
   private final EvictingQueue<String> searchHistory = EvictingQueue.create(25);
   private String autocompleteName;
   private Pattern autocompleteNamePattern;

   @Inject
   private NameAutocompleter(Client client, HiscoreConfig hiscoreConfig) {
      this.client = client;
      this.hiscoreConfig = hiscoreConfig;
   }

   public void keyPressed(KeyEvent e) {
   }

   public void keyReleased(KeyEvent e) {
   }

   public void keyTyped(KeyEvent e) {
   }

   private void newAutocomplete(KeyEvent e) {
      JTextComponent input = (JTextComponent)e.getSource();
      String inputText = input.getText();
      String var10000 = inputText.substring(0, input.getSelectionStart());
      String nameStart = var10000 + e.getKeyChar();
      if (this.findAutocompleteName(nameStart)) {
         String name = this.autocompleteName;
         SwingUtilities.invokeLater(() -> {
            try {
               input.getDocument().insertString(nameStart.length(), name.substring(nameStart.length()), (AttributeSet)null);
               input.select(nameStart.length(), name.length());
            } catch (BadLocationException var4) {
               BadLocationException ex = var4;
               log.warn("Could not autocomplete name.", ex);
            }

         });
      }

   }

   private boolean findAutocompleteName(String nameStart) {
      Pattern pattern = Pattern.compile("(?i)^" + nameStart.replaceAll("[ _-]", "[ _" + NBSP + "-]") + ".+?");
      Optional<String> autocompleteName = this.searchHistory.stream().filter((n) -> {
         return pattern.matcher(n).matches();
      }).findFirst();
      if (!autocompleteName.isPresent()) {
         NameableContainer<Friend> friendContainer = this.client.getFriendContainer();
         if (friendContainer != null) {
            autocompleteName = Arrays.stream((Friend[])friendContainer.getMembers()).map(Nameable::getName).filter((n) -> {
               return pattern.matcher(n).matches();
            }).findFirst();
         }
      }

      if (!autocompleteName.isPresent()) {
         FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
         if (friendsChatManager != null) {
            autocompleteName = Arrays.stream((FriendsChatMember[])friendsChatManager.getMembers()).map(Nameable::getName).filter((n) -> {
               return pattern.matcher(n).matches();
            }).findFirst();
         }
      }

      if (!autocompleteName.isPresent()) {
         ClanSettings[] clanSettings = new ClanSettings[]{this.client.getClanSettings(0), this.client.getClanSettings(1), this.client.getGuestClanSettings()};
         autocompleteName = Arrays.stream(clanSettings).filter(Objects::nonNull).flatMap((cs) -> {
            return cs.getMembers().stream();
         }).map(ClanMember::getName).filter((n) -> {
            return pattern.matcher(n).matches();
         }).findFirst();
      }

      if (!autocompleteName.isPresent()) {
         WorldView wv = this.client.getTopLevelWorldView();
         autocompleteName = wv.players().stream().filter(Objects::nonNull).map(Actor::getName).filter(Objects::nonNull).filter((n) -> {
            return pattern.matcher(n).matches();
         }).findFirst();
      }

      if (autocompleteName.isPresent()) {
         this.autocompleteName = ((String)autocompleteName.get()).replace(NBSP, " ");
         this.autocompleteNamePattern = Pattern.compile("(?i)^" + this.autocompleteName.replaceAll("[ _-]", "[ _-]") + "$");
      } else {
         this.autocompleteName = null;
         this.autocompleteNamePattern = null;
      }

      return autocompleteName.isPresent();
   }

   void addToSearchHistory(@NonNull String name) {
      if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else {
         if (!this.searchHistory.contains(name)) {
            this.searchHistory.offer(name);
         }

      }
   }

   private boolean isExpectedNext(JTextComponent input, String nextChar) {
      String expected;
      if (input.getSelectionStart() < input.getSelectionEnd()) {
         try {
            expected = input.getText(input.getSelectionStart(), 1);
         } catch (BadLocationException var5) {
            BadLocationException ex = var5;
            log.warn("Could not get first character from input selection.", ex);
            return false;
         }
      } else {
         expected = "";
      }

      return nextChar.equalsIgnoreCase(expected);
   }
}
