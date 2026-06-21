package net.runelite.api;

public interface MessageNode extends Node {
   int getId();

   ChatMessageType getType();

   String getName();

   void setName(String var1);

   String getSender();

   void setSender(String var1);

   String getValue();

   void setValue(String var1);

   String getRuneLiteFormatMessage();

   void setRuneLiteFormatMessage(String var1);

   int getTimestamp();

   void setTimestamp(int var1);
}
