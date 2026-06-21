package net.runelite.client.externalplugins;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.util.VerificationException;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalPluginClient {
   private static final Logger log = LoggerFactory.getLogger(ExternalPluginClient.class);
   private final OkHttpClient okHttpClient;
   private final Gson gson;
   private final HttpUrl apiBase;
   private final HttpUrl pluginHubBase;

   @Inject
   private ExternalPluginClient(OkHttpClient okHttpClient, Gson gson, @Named("runelite.api.base") HttpUrl apiBase, @Named("runelite.pluginhub.url") HttpUrl pluginHubBase) {
      this.okHttpClient = okHttpClient;
      this.gson = gson;
      this.apiBase = apiBase;
      this.pluginHubBase = pluginHubBase;
   }

   public PluginHubManifest.ManifestLite downloadManifestLite() throws IOException, VerificationException {
      return (PluginHubManifest.ManifestLite)this.downloadManifest("lite", PluginHubManifest.ManifestLite.class);
   }

   public PluginHubManifest.ManifestFull downloadManifestFull() throws IOException, VerificationException {
      return (PluginHubManifest.ManifestFull)this.downloadManifest("full", PluginHubManifest.ManifestFull.class);
   }

   private <T> T downloadManifest(String name, Class<T> clazz) throws IOException, VerificationException {
      HttpUrl.Builder var10000 = this.pluginHubBase.newBuilder().addPathSegment("manifest");
      String var10001 = RuneLiteProperties.getPluginHubVersion();
      HttpUrl manifest = var10000.addPathSegment(var10001 + "_" + name + ".js").build();

      try {
         Response res = this.okHttpClient.newCall((new Request.Builder()).url(manifest).build()).execute();

         Object var9;
         try {
            if (res.code() != 200) {
               throw new IOException("Non-OK response code: " + res.code());
            }

            BufferedSource src = res.body().source();
            byte[] signature = new byte[src.readInt()];
            src.readFully(signature);
            byte[] data = src.readByteArray();
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initVerify(loadCertificate());
            s.update(data);
            if (!s.verify(signature)) {
               throw new VerificationException("Unable to verify external plugin manifest");
            }

            var9 = this.gson.fromJson(new String(data, StandardCharsets.UTF_8), clazz);
         } catch (Throwable var11) {
            if (res != null) {
               try {
                  res.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (res != null) {
            res.close();
         }

         return var9;
      } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException var12) {
         GeneralSecurityException e = var12;
         throw new VerificationException(e);
      }
   }

   public BufferedImage downloadIcon(PluginHubManifest.DisplayData plugin) throws IOException {
      if (plugin.getIconHash() == null) {
         return null;
      } else {
         HttpUrl.Builder var10000 = this.pluginHubBase.newBuilder().addPathSegment("icon");
         String var10001 = plugin.getInternalName();
         HttpUrl url = var10000.addPathSegment(var10001 + "_" + plugin.getIconHash() + ".png").build();
         Response res = this.okHttpClient.newCall((new Request.Builder()).url(url).build()).execute();

         BufferedImage var6;
         try {
            byte[] bytes = res.body().bytes();
            Class var5 = ImageIO.class;
            synchronized(ImageIO.class) {
               var6 = ImageIO.read(new ByteArrayInputStream(bytes));
            }
         } catch (Throwable var10) {
            if (res != null) {
               try {
                  res.close();
               } catch (Throwable var8) {
                  var10.addSuppressed(var8);
               }
            }

            throw var10;
         }

         if (res != null) {
            res.close();
         }

         return var6;
      }
   }

   HttpUrl getJarURL(PluginHubManifest.JarData plugin) {
      HttpUrl.Builder var10000 = this.pluginHubBase.newBuilder().addPathSegment("jar");
      String var10001 = plugin.getInternalName();
      return var10000.addPathSegment(var10001 + "_" + plugin.getJarHash() + ".jar").build();
   }

   private static Certificate loadCertificate() {
      try {
         InputStream in = ExternalPluginClient.class.getResourceAsStream("externalplugins.crt");

         Certificate var2;
         try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            var2 = certFactory.generateCertificate(in);
         } catch (Throwable var4) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var3) {
                  var4.addSuppressed(var3);
               }
            }

            throw var4;
         }

         if (in != null) {
            in.close();
         }

         return var2;
      } catch (IOException | CertificateException var5) {
         Exception e = var5;
         throw new RuntimeException(e);
      }
   }

   void submitPlugins(List<String> plugins) {
      if (!plugins.isEmpty()) {
         HttpUrl url = this.apiBase.newBuilder().addPathSegment("pluginhub").build();
         Request request = (new Request.Builder()).url(url).post(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(plugins))).build();
         this.okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
               ExternalPluginClient.log.debug("Error submitting plugins", e);
            }

            public void onResponse(Call call, Response response) {
               ExternalPluginClient.log.debug("Submitted plugin list");
               response.close();
            }
         });
      }
   }

   public Map<String, Integer> getPluginCounts() throws IOException {
      HttpUrl url = this.apiBase.newBuilder().addPathSegments("pluginhub").build();

      try {
         Response res = this.okHttpClient.newCall((new Request.Builder()).url(url).build()).execute();

         Map var3;
         try {
            if (res.code() != 200) {
               throw new IOException("Non-OK response code: " + res.code());
            }

            var3 = (Map)this.gson.fromJson(res.body().string(), (new TypeToken<Map<String, Integer>>() {
            }).getType());
         } catch (Throwable var6) {
            if (res != null) {
               try {
                  res.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (res != null) {
            res.close();
         }

         return var3;
      } catch (JsonSyntaxException var7) {
         JsonSyntaxException ex = var7;
         throw new IOException(ex);
      }
   }
}
