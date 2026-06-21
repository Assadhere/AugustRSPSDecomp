package net.runelite.client.config;

import com.google.gson.Gson;
import com.google.inject.Inject;

class NotificationSerializer implements Serializer<Notification> {
   private final Gson gson;

   @Inject
   private NotificationSerializer(Gson gson) {
      this.gson = gson;
   }

   public String serialize(Notification value) {
      return this.gson.toJson(value);
   }

   public Notification deserialize(String s) {
      return !"true".equals(s) && !"false".equals(s) ? (Notification)this.gson.fromJson(s, Notification.class) : (new Notification()).withEnabled(Boolean.parseBoolean(s));
   }
}
