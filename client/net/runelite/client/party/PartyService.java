package net.runelite.client.party;

import com.google.common.base.CharMatcher;
import com.google.common.hash.Hashing;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PartyChanged;
import net.runelite.client.events.PartyMemberAvatar;
import net.runelite.client.party.events.UserJoin;
import net.runelite.client.party.events.UserPart;
import net.runelite.client.party.messages.PartyChatMessage;
import net.runelite.client.party.messages.PartyMessage;
import net.runelite.client.party.messages.UserSync;
import net.runelite.client.party.messages.WebsocketMessage;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PartyService {
   private static final Logger log = LoggerFactory.getLogger(PartyService.class);
   private static final int MAX_MESSAGE_LEN = 150;
   private static final String ALPHABET = "bcdfghjklmnpqrstvwxyz";
   private final Client client;
   private final WSClient wsClient;
   private final EventBus eventBus;
   private final ChatMessageManager chat;
   private final List<PartyMember> members = new ArrayList();
   private long partyId;
   private long memberId = randomMemberId();
   private String partyPassphrase;

   @Inject
   private PartyService(Client client, WSClient wsClient, EventBus eventBus, ChatMessageManager chat) {
      this.client = client;
      this.wsClient = wsClient;
      this.eventBus = eventBus;
      this.chat = chat;
      eventBus.register(this);
   }

   public String generatePassphrase() {
      assert this.client.isClientThread();

      Random r = new Random();
      StringBuilder sb = new StringBuilder();
      int len;
      if (this.client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState()) {
         len = 0;
         CharMatcher matcher = CharMatcher.javaLetter();

         do {
            int itemId = r.nextInt(this.client.getItemCount());
            ItemComposition def = this.client.getItemDefinition(itemId);
            String name = def.getMembersName();
            if (name != null && !name.isEmpty() && !name.equalsIgnoreCase("null")) {
               String[] split = name.split(" ");
               String token = split[r.nextInt(split.length)];
               if (matcher.matchesAllOf(token) && token.length() > 2) {
                  if (sb.length() > 0) {
                     sb.append('-');
                  }

                  sb.append(token.toLowerCase(Locale.US));
                  ++len;
               }
            }
         } while(len < 4);
      } else {
         len = 0;

         do {
            if (sb.length() > 0) {
               sb.append('-');
            }

            for(int i = 0; i < 5; ++i) {
               sb.append("bcdfghjklmnpqrstvwxyz".charAt(r.nextInt("bcdfghjklmnpqrstvwxyz".length())));
            }

            ++len;
         } while(len < 4);
      }

      String partyPassphrase = sb.toString();
      log.debug("Generated party passphrase {}", partyPassphrase);
      return partyPassphrase;
   }

   public void changeParty(@Nullable String passphrase) {
      if (this.wsClient.sessionExists()) {
         this.wsClient.part();
         this.memberId = randomMemberId();
      }

      long id = passphrase != null ? passphraseToId(passphrase) : 0L;
      log.debug("Party change to {} (id {})", passphrase, id);
      this.members.clear();
      this.partyId = id;
      this.partyPassphrase = passphrase;
      if (passphrase == null) {
         this.wsClient.changeSession((UUID)null);
         this.eventBus.post(new PartyChanged(this.partyPassphrase, (Long)null));
      } else {
         if (!this.wsClient.sessionExists()) {
            this.wsClient.changeSession(UUID.randomUUID());
         }

         this.wsClient.join(this.partyId, this.memberId);
         this.eventBus.post(new PartyChanged(this.partyPassphrase, this.partyId));
      }
   }

   public <T extends PartyMessage> void send(T message) {
      if (!this.wsClient.isOpen()) {
         log.debug("Reconnecting to server");
         this.members.clear();
         this.wsClient.connect();
         this.wsClient.join(this.partyId, this.memberId);
      }

      this.wsClient.send((WebsocketMessage)message);
   }

   @Subscribe(
      priority = 1.0F
   )
   public void onUserJoin(UserJoin message) {
      if (this.partyId == message.getPartyId()) {
         PartyMember partyMember = this.getMemberById(message.getMemberId());
         if (partyMember == null) {
            partyMember = new PartyMember(message.getMemberId());
            this.members.add(partyMember);
            log.debug("User {} joins party, {} members", partyMember, this.members.size());
         }

         PartyMember localMember = this.getLocalMember();
         if (localMember != null && localMember == partyMember) {
            log.debug("Requesting sync");
            UserSync userSync = new UserSync();
            this.wsClient.send((WebsocketMessage)userSync);
         }

      }
   }

   @Subscribe(
      priority = 1.0F
   )
   public void onUserPart(UserPart message) {
      if (this.members.removeIf((member) -> {
         return member.getMemberId() == message.getMemberId();
      })) {
         log.debug("User {} leaves party, {} members", message.getMemberId(), this.members.size());
      }

   }

   @Subscribe
   public void onPartyChatMessage(PartyChatMessage message) {
      PartyMember member = this.getMemberById(message.getMemberId());
      if (member != null && member.isLoggedIn()) {
         String sentMesage = Text.JAGEX_PRINTABLE_CHAR_MATCHER.retainFrom(message.getValue()).replaceAll("<img=.+>", "");
         if (sentMesage.length() > 150) {
            sentMesage = sentMesage.substring(0, 150);
         }

         this.chat.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHAT).sender("Party").name(member.getDisplayName()).runeLiteFormattedMessage(sentMesage).build());
      } else {
         log.debug("Dropping party chat from non logged-in member");
      }
   }

   public PartyMember getLocalMember() {
      return this.getMemberById(this.memberId);
   }

   public PartyMember getMemberById(long id) {
      Iterator var3 = this.members.iterator();

      PartyMember member;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         member = (PartyMember)var3.next();
      } while(id != member.getMemberId());

      return member;
   }

   public PartyMember getMemberByDisplayName(String name) {
      String sanitized = Text.removeTags(Text.toJagexName(name));
      Iterator var3 = this.members.iterator();

      PartyMember member;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         member = (PartyMember)var3.next();
      } while(!member.isLoggedIn() || !sanitized.equals(member.getDisplayName()));

      return member;
   }

   public List<PartyMember> getMembers() {
      return Collections.unmodifiableList(this.members);
   }

   public boolean isInParty() {
      return this.partyId != 0L;
   }

   public void setPartyMemberAvatar(long memberID, BufferedImage image) {
      PartyMember memberById = this.getMemberById(memberID);
      if (memberById != null) {
         memberById.setAvatar(image);
         this.eventBus.post(new PartyMemberAvatar(memberID, image));
      }

   }

   private static long passphraseToId(String passphrase) {
      return Hashing.sha256().hashBytes(passphrase.getBytes(StandardCharsets.UTF_8)).asLong() & Long.MAX_VALUE;
   }

   private static long randomMemberId() {
      return (new Random()).nextLong() & Long.MAX_VALUE;
   }

   public long getPartyId() {
      return this.partyId;
   }

   public String getPartyPassphrase() {
      return this.partyPassphrase;
   }
}
