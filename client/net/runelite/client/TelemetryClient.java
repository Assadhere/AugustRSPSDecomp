package net.runelite.client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import net.runelite.client.util.OSType;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.telemetry.Telemetry;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelemetryClient {
   private static final Logger log = LoggerFactory.getLogger(TelemetryClient.class);
   private final OkHttpClient okHttpClient;
   private final Gson gson;
   private final HttpUrl apiBase;

   void submitTelemetry() {
      HttpUrl url = this.apiBase.newBuilder().addPathSegment("telemetry").build();
      Request request = (new Request.Builder()).url(url).post(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(buildTelemetry()))).build();
      this.okHttpClient.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            TelemetryClient.log.debug("Error submitting telemetry", e);
         }

         public void onResponse(Call call, Response response) {
            TelemetryClient.log.debug("Submitted telemetry");
            response.close();
         }
      });
   }

   void submitVmErrors(File logsDir) {
      try {
         long yesterday = System.currentTimeMillis() - Duration.ofDays(1L).toMillis();
         File[] var4 = logsDir.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File f = var4[var6];
            if (f.getName().startsWith("jvm_crash_") && f.getName().endsWith(".log") && !f.getName().endsWith("_r.log") && f.lastModified() >= yesterday) {
               String hsErr = Files.readString(f.toPath());
               String var10000 = f.getName();
               int var10002 = f.getName().length();
               String destName = var10000.substring(0, var10002 - 4) + "_r.log";
               File dest = new File(logsDir, destName);
               if (f.renameTo(dest)) {
                  String username = System.getProperty("user.name");
                  String home = System.getProperty("user.home");
                  hsErr = hsErr.replace(username, "%USERNAME%").replace(home, "%HOME%");
                  this.submitError("vm crash", hsErr, Collections.emptyMap());
               }
            }
         }
      } catch (Exception var13) {
         Exception ex = var13;
         log.error("error reporting errors", ex);
      }

   }

   public void submitError(String type, String error, Map<String, String> params) {
      HttpUrl.Builder urlBuilder = this.apiBase.newBuilder().addPathSegment("telemetry").addPathSegment("error").addQueryParameter("type", type).addQueryParameter("osname", System.getProperty("os.name")).addQueryParameter("osver", System.getProperty("os.version")).addQueryParameter("osarch", System.getProperty("os.arch")).addQueryParameter("javaversion", System.getProperty("java.version")).addQueryParameter("javavendor", System.getProperty("java.vendor")).addQueryParameter("cpumodel", cpuName());
      Objects.requireNonNull(urlBuilder);
      params.forEach(urlBuilder::addQueryParameter);
      HttpUrl url = urlBuilder.build();
      Request request = (new Request.Builder()).url(url).post(RequestBody.create(MediaType.get("text/plain"), error)).build();
      this.okHttpClient.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            TelemetryClient.log.debug("Error submitting error", e);
         }

         public void onResponse(Call call, Response response) {
            TelemetryClient.log.debug("Submitted error");
            response.close();
         }
      });
   }

   private static Telemetry buildTelemetry() {
      Telemetry telemetry = new Telemetry();
      telemetry.setJavaVendor(System.getProperty("java.vendor"));
      telemetry.setJavaVersion(System.getProperty("java.version"));
      telemetry.setOsName(System.getProperty("os.name"));
      telemetry.setOsVersion(System.getProperty("os.version"));
      telemetry.setOsArch(System.getProperty("os.arch"));
      telemetry.setLauncherVersion(System.getProperty("runelite.launcher.version"));
      OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
      if (operatingSystemMXBean instanceof com.sun.management.OperatingSystemMXBean) {
         long totalPhysicalMemorySize = ((com.sun.management.OperatingSystemMXBean)operatingSystemMXBean).getTotalPhysicalMemorySize();
         telemetry.setTotalMemory(totalPhysicalMemorySize);
      }

      telemetry.setCpuName(cpuName());
      telemetry.setJxAccount(System.getenv("JX_SESSION_ID") != null && System.getenv("JX_CHARACTER_ID") != null);
      return telemetry;
   }

   private static String cpuName() {
      if (OSType.getOSType() != OSType.Windows) {
         return null;
      } else {
         try {
            Process p = Runtime.getRuntime().exec("wmic cpu get name");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String var3;
            label60: {
               String line;
               try {
                  while((line = in.readLine()) != null) {
                     line = line.trim();
                     if (!line.isEmpty() && !line.equalsIgnoreCase("name")) {
                        var3 = line;
                        break label60;
                     }
                  }
               } catch (Throwable var5) {
                  try {
                     in.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }

                  throw var5;
               }

               in.close();
               return null;
            }

            in.close();
            return var3;
         } catch (IOException var6) {
            IOException ex = var6;
            log.debug("unable to get cpu name", ex);
            return null;
         }
      }
   }

   public TelemetryClient(OkHttpClient okHttpClient, Gson gson, HttpUrl apiBase) {
      this.okHttpClient = okHttpClient;
      this.gson = gson;
      this.apiBase = apiBase;
   }
}
