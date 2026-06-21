package net.runelite.client.party;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.runelite.client.party.messages.PartyChatMessage;
import net.runelite.client.party.messages.UserSync;
import net.runelite.client.party.messages.WebsocketMessage;
import net.runelite.client.util.RuntimeTypeAdapterFactory;
import net.runelite.http.api.RuneLiteAPI;

class WebsocketGsonFactory {
   private static final Collection<Class<? extends WebsocketMessage>> MESSAGES;

   public static RuntimeTypeAdapterFactory<WebsocketMessage> factory(Collection<Class<? extends WebsocketMessage>> messages) {
      RuntimeTypeAdapterFactory<WebsocketMessage> factory = RuntimeTypeAdapterFactory.of(WebsocketMessage.class);
      Iterator var2 = MESSAGES.iterator();

      Class message;
      while(var2.hasNext()) {
         message = (Class)var2.next();
         factory.registerSubtype(message);
      }

      var2 = messages.iterator();

      while(var2.hasNext()) {
         message = (Class)var2.next();
         factory.registerSubtype(message);
      }

      return factory;
   }

   public static Gson build(RuntimeTypeAdapterFactory<WebsocketMessage> factory) {
      return RuneLiteAPI.GSON.newBuilder().registerTypeAdapterFactory(factory).create();
   }

   public static Gson build() {
      return build(factory(Collections.emptyList()));
   }

   static {
      List<Class<? extends WebsocketMessage>> messages = new ArrayList();
      messages.add(UserSync.class);
      messages.add(PartyChatMessage.class);
      MESSAGES = messages;
   }
}
