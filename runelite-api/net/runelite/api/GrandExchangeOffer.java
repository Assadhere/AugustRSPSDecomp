package net.runelite.api;

public interface GrandExchangeOffer {
   int getQuantitySold();

   int getItemId();

   int getTotalQuantity();

   int getPrice();

   int getSpent();

   GrandExchangeOfferState getState();
}
