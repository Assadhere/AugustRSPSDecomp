package net.runelite.client.plugins.xptracker;

import com.google.gson.Gson;
import com.google.inject.Inject;
import net.runelite.client.config.Serializer;

class XpSaveSerializer implements Serializer<XpSave> {
   private final Gson gson;

   @Inject
   private XpSaveSerializer(Gson gson) {
      this.gson = gson;
   }

   public String serialize(XpSave value) {
      return this.gson.toJson(value);
   }

   public XpSave deserialize(String s) {
      return (XpSave)this.gson.fromJson(s, XpSave.class);
   }
}
