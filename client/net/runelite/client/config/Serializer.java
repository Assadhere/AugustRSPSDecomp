package net.runelite.client.config;

public interface Serializer<T> {
   String serialize(T var1);

   T deserialize(String var1);
}
