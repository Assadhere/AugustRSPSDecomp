package net.runelite.launcher;

import javax.net.ssl.SSLHandshakeException;
import sun.security.provider.certpath.AdjacencyList;
import sun.security.provider.certpath.SunCertPathBuilderException;
import sun.security.validator.ValidatorException;

class CertPathExtractor {
   static String extract(Throwable ex) {
      try {
         if (ex instanceof SSLHandshakeException) {
            ex = ex.getCause();
            if (ex instanceof ValidatorException) {
               ex = ex.getCause();
               if (ex instanceof SunCertPathBuilderException) {
                  SunCertPathBuilderException pathBuilderEx = (SunCertPathBuilderException)ex;
                  AdjacencyList adjList = pathBuilderEx.getAdjacencyList();
                  return adjList.toString();
               }
            }
         }
      } catch (Throwable var3) {
      }

      return null;
   }
}
