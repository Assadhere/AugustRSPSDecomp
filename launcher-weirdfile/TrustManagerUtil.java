package net.runelite.launcher;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

class TrustManagerUtil {
   private static TrustManager[] loadTrustManagers(String trustStoreType) throws KeyStoreException, NoSuchAlgorithmException {
      String old;
      if (trustStoreType != null) {
         old = System.setProperty("javax.net.ssl.trustStoreType", trustStoreType);
      } else {
         old = System.clearProperty("javax.net.ssl.trustStoreType");
      }

      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init((KeyStore)null);
      TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
      if (old == null) {
         System.clearProperty("javax.net.ssl.trustStoreType");
      } else {
         System.setProperty("javax.net.ssl.trustStoreType", old);
      }

      return trustManagers;
   }

   static void setupTrustManager() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      if (OS.getOs() == OS.OSType.Windows) {
         TrustManager[] jreTms = loadTrustManagers((String)null);
         TrustManager[] windowsTms = loadTrustManagers("Windows-ROOT");
         final TrustManager[] trustManagers = new TrustManager[jreTms.length + windowsTms.length];
         System.arraycopy(jreTms, 0, trustManagers, 0, jreTms.length);
         System.arraycopy(windowsTms, 0, trustManagers, jreTms.length, windowsTms.length);
         <undefinedtype> combiningTrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
               CertificateException exception = null;
               TrustManager[] var4 = trustManagers;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  TrustManager trustManager = var4[var6];
                  if (trustManager instanceof X509TrustManager) {
                     try {
                        ((X509TrustManager)trustManager).checkClientTrusted(chain, authType);
                        return;
                     } catch (CertificateException var9) {
                        CertificateException ex = var9;
                        exception = ex;
                     }
                  }
               }

               if (exception != null) {
                  throw exception;
               } else {
                  throw new CertificateException("no X509TrustManagers present");
               }
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
               CertificateException exception = null;
               TrustManager[] var4 = trustManagers;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  TrustManager trustManager = var4[var6];
                  if (trustManager instanceof X509TrustManager) {
                     try {
                        ((X509TrustManager)trustManager).checkServerTrusted(chain, authType);
                        return;
                     } catch (CertificateException var9) {
                        CertificateException ex = var9;
                        exception = ex;
                     }
                  }
               }

               if (exception != null) {
                  throw exception;
               } else {
                  throw new CertificateException("no X509TrustManagers present");
               }
            }

            public X509Certificate[] getAcceptedIssuers() {
               ArrayList<X509Certificate> certificates = new ArrayList();
               TrustManager[] var2 = trustManagers;
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  TrustManager trustManager = var2[var4];
                  if (trustManager instanceof X509TrustManager) {
                     certificates.addAll(Arrays.asList(((X509TrustManager)trustManager).getAcceptedIssuers()));
                  }
               }

               return (X509Certificate[])certificates.toArray(new X509Certificate[0]);
            }
         };
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init((KeyManager[])null, new TrustManager[]{combiningTrustManager}, new SecureRandom());
         SSLContext.setDefault(sc);
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      }
   }

   static void setupInsecureTrustManager() throws NoSuchAlgorithmException, KeyManagementException {
      TrustManager trustManager = new X509TrustManager() {
         public void checkClientTrusted(X509Certificate[] chain, String authType) {
         }

         public void checkServerTrusted(X509Certificate[] chain, String authType) {
         }

         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }
      };
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init((KeyManager[])null, new TrustManager[]{trustManager}, new SecureRandom());
      SSLContext.setDefault(sc);
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> {
         return true;
      });
   }
}
