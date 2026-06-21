package net.runelite.api;

public interface ChatLineBuffer {
   MessageNode[] getLines();

   int getLength();

   void removeMessageNode(MessageNode var1);
}
