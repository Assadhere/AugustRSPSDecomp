package net.runelite.api;

import java.net.URL;

public interface ClientConfiguration {
   URL getCodeBase();

   String getParameter(String var1);

   void onError(String var1);
}
