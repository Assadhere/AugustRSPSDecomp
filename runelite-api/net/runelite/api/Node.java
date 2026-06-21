package net.runelite.api;

public interface Node {
   Node getNext();

   Node getPrevious();

   long getHash();
}
