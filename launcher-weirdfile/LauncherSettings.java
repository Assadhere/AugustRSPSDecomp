package net.runelite.launcher;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

class LauncherSettings {
   private static final Logger log = LoggerFactory.getLogger(LauncherSettings.class);
   private static final String LAUNCHER_SETTINGS = "settings.json";
   long lastUpdateAttemptTime;
   String lastUpdateHash;
   int lastUpdateAttemptNum;
   boolean debug;
   boolean nodiffs;
   boolean skipTlsVerification;
   boolean noupdates;
   boolean safemode;
   @Nullable
   Double scale;
   List<String> clientArguments = Collections.emptyList();
   List<String> jvmArguments = Collections.emptyList();
   HardwareAccelerationMode hardwareAccelerationMode;
   LaunchMode launchMode;

   void apply(OptionSet options) {
      if (options.has("debug")) {
         this.debug = true;
      }

      if (options.has("nodiff")) {
         this.nodiffs = true;
      }

      if (options.has("insecure-skip-tls-verification")) {
         this.skipTlsVerification = true;
      }

      if (options.has("noupdate")) {
         this.noupdates = true;
      }

      if (options.has("scale")) {
         this.scale = Double.parseDouble(String.valueOf(options.valueOf("scale")));
      }

      Stream var10001;
      if (options.has("J")) {
         var10001 = options.valuesOf("J").stream();
         Objects.requireNonNull(String.class);
         var10001 = var10001.filter(String.class::isInstance);
         Objects.requireNonNull(String.class);
         this.jvmArguments = (List)var10001.map(String.class::cast).collect(Collectors.toList());
      }

      if (!options.nonOptionArguments().isEmpty()) {
         var10001 = options.nonOptionArguments().stream();
         Objects.requireNonNull(String.class);
         var10001 = var10001.filter(String.class::isInstance);
         Objects.requireNonNull(String.class);
         this.clientArguments = (List)var10001.map(String.class::cast).collect(Collectors.toList());
      }

      if (options.has("hw-accel")) {
         this.hardwareAccelerationMode = (HardwareAccelerationMode)options.valueOf("hw-accel");
      } else if (options.has("mode")) {
         this.hardwareAccelerationMode = (HardwareAccelerationMode)options.valueOf("mode");
      }

      if ("true".equals(System.getProperty("runelite.launcher.reflect"))) {
         this.launchMode = LaunchMode.REFLECT;
      } else if (options.has("launch-mode")) {
         this.launchMode = (LaunchMode)options.valueOf("launch-mode");
      }

   }

   String configurationStr() {
      String var10000 = System.lineSeparator();
      return MessageFormatter.arrayFormat(" debug: {}" + var10000 + " nodiffs: {}" + System.lineSeparator() + " skip tls verification: {}" + System.lineSeparator() + " noupdates: {}" + System.lineSeparator() + " safe mode: {}" + System.lineSeparator() + " scale: {}" + System.lineSeparator() + " client arguments: {}" + System.lineSeparator() + " jvm arguments: {}" + System.lineSeparator() + " hardware acceleration mode: {}" + System.lineSeparator() + " launch mode: {}", new Object[]{this.debug, this.nodiffs, this.skipTlsVerification, this.noupdates, this.safemode, this.scale == null ? "system" : this.scale, this.clientArguments.isEmpty() ? "none" : this.clientArguments, this.jvmArguments.isEmpty() ? "none" : this.jvmArguments, this.hardwareAccelerationMode, this.launchMode}).getMessage();
   }

   @Nonnull
   static LauncherSettings loadSettings() {
      File settingsFile = (new File("settings.json")).getAbsoluteFile();

      try {
         InputStreamReader in = new InputStreamReader(new FileInputStream(settingsFile), StandardCharsets.UTF_8);

         LauncherSettings var3;
         try {
            LauncherSettings settings = (LauncherSettings)(new Gson()).fromJson(in, LauncherSettings.class);
            var3 = (LauncherSettings)MoreObjects.firstNonNull(settings, new LauncherSettings());
         } catch (Throwable var5) {
            try {
               in.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         in.close();
         return var3;
      } catch (FileNotFoundException var6) {
         log.debug("unable to load settings, file does not exist");
         return new LauncherSettings();
      } catch (JsonParseException | IOException var7) {
         Exception e = var7;
         log.warn("unable to load settings", e);
         return new LauncherSettings();
      }
   }

   static void saveSettings(LauncherSettings settings) {
      File settingsFile = (new File("settings.json")).getAbsoluteFile();

      try {
         File tmpFile = File.createTempFile("settings.json", "json");
         Gson gson = new Gson();
         FileOutputStream fout = new FileOutputStream(tmpFile);

         try {
            FileChannel channel = fout.getChannel();

            try {
               OutputStreamWriter writer = new OutputStreamWriter(fout, StandardCharsets.UTF_8);

               try {
                  channel.lock();
                  gson.toJson(settings, writer);
                  writer.flush();
                  channel.force(true);
               } catch (Throwable var13) {
                  try {
                     writer.close();
                  } catch (Throwable var11) {
                     var13.addSuppressed(var11);
                  }

                  throw var13;
               }

               writer.close();
            } catch (Throwable var14) {
               if (channel != null) {
                  try {
                     channel.close();
                  } catch (Throwable var10) {
                     var14.addSuppressed(var10);
                  }
               }

               throw var14;
            }

            if (channel != null) {
               channel.close();
            }
         } catch (Throwable var15) {
            try {
               fout.close();
            } catch (Throwable var9) {
               var15.addSuppressed(var9);
            }

            throw var15;
         }

         fout.close();

         try {
            Files.move(tmpFile.toPath(), settingsFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (AtomicMoveNotSupportedException var12) {
            AtomicMoveNotSupportedException ex = var12;
            log.debug("atomic move not supported", ex);
            Files.move(tmpFile.toPath(), settingsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var16) {
         IOException e = var16;
         log.error("unable to save launcher settings!", e);
         settingsFile.delete();
      }

   }

   public LauncherSettings() {
      this.hardwareAccelerationMode = HardwareAccelerationMode.AUTO;
      this.launchMode = LaunchMode.AUTO;
   }

   public long getLastUpdateAttemptTime() {
      return this.lastUpdateAttemptTime;
   }

   public String getLastUpdateHash() {
      return this.lastUpdateHash;
   }

   public int getLastUpdateAttemptNum() {
      return this.lastUpdateAttemptNum;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public boolean isNodiffs() {
      return this.nodiffs;
   }

   public boolean isSkipTlsVerification() {
      return this.skipTlsVerification;
   }

   public boolean isNoupdates() {
      return this.noupdates;
   }

   public boolean isSafemode() {
      return this.safemode;
   }

   @Nullable
   public Double getScale() {
      return this.scale;
   }

   public List<String> getClientArguments() {
      return this.clientArguments;
   }

   public List<String> getJvmArguments() {
      return this.jvmArguments;
   }

   public HardwareAccelerationMode getHardwareAccelerationMode() {
      return this.hardwareAccelerationMode;
   }

   public LaunchMode getLaunchMode() {
      return this.launchMode;
   }

   public void setLastUpdateAttemptTime(long lastUpdateAttemptTime) {
      this.lastUpdateAttemptTime = lastUpdateAttemptTime;
   }

   public void setLastUpdateHash(String lastUpdateHash) {
      this.lastUpdateHash = lastUpdateHash;
   }

   public void setLastUpdateAttemptNum(int lastUpdateAttemptNum) {
      this.lastUpdateAttemptNum = lastUpdateAttemptNum;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public void setNodiffs(boolean nodiffs) {
      this.nodiffs = nodiffs;
   }

   public void setSkipTlsVerification(boolean skipTlsVerification) {
      this.skipTlsVerification = skipTlsVerification;
   }

   public void setNoupdates(boolean noupdates) {
      this.noupdates = noupdates;
   }

   public void setSafemode(boolean safemode) {
      this.safemode = safemode;
   }

   public void setScale(@Nullable Double scale) {
      this.scale = scale;
   }

   public void setClientArguments(List<String> clientArguments) {
      this.clientArguments = clientArguments;
   }

   public void setJvmArguments(List<String> jvmArguments) {
      this.jvmArguments = jvmArguments;
   }

   public void setHardwareAccelerationMode(HardwareAccelerationMode hardwareAccelerationMode) {
      this.hardwareAccelerationMode = hardwareAccelerationMode;
   }

   public void setLaunchMode(LaunchMode launchMode) {
      this.launchMode = launchMode;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LauncherSettings)) {
         return false;
      } else {
         LauncherSettings other = (LauncherSettings)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getLastUpdateAttemptTime() != other.getLastUpdateAttemptTime()) {
            return false;
         } else if (this.getLastUpdateAttemptNum() != other.getLastUpdateAttemptNum()) {
            return false;
         } else if (this.isDebug() != other.isDebug()) {
            return false;
         } else if (this.isNodiffs() != other.isNodiffs()) {
            return false;
         } else if (this.isSkipTlsVerification() != other.isSkipTlsVerification()) {
            return false;
         } else if (this.isNoupdates() != other.isNoupdates()) {
            return false;
         } else if (this.isSafemode() != other.isSafemode()) {
            return false;
         } else {
            Object this$scale = this.getScale();
            Object other$scale = other.getScale();
            if (this$scale == null) {
               if (other$scale != null) {
                  return false;
               }
            } else if (!this$scale.equals(other$scale)) {
               return false;
            }

            label93: {
               Object this$lastUpdateHash = this.getLastUpdateHash();
               Object other$lastUpdateHash = other.getLastUpdateHash();
               if (this$lastUpdateHash == null) {
                  if (other$lastUpdateHash == null) {
                     break label93;
                  }
               } else if (this$lastUpdateHash.equals(other$lastUpdateHash)) {
                  break label93;
               }

               return false;
            }

            label86: {
               Object this$clientArguments = this.getClientArguments();
               Object other$clientArguments = other.getClientArguments();
               if (this$clientArguments == null) {
                  if (other$clientArguments == null) {
                     break label86;
                  }
               } else if (this$clientArguments.equals(other$clientArguments)) {
                  break label86;
               }

               return false;
            }

            Object this$jvmArguments = this.getJvmArguments();
            Object other$jvmArguments = other.getJvmArguments();
            if (this$jvmArguments == null) {
               if (other$jvmArguments != null) {
                  return false;
               }
            } else if (!this$jvmArguments.equals(other$jvmArguments)) {
               return false;
            }

            label72: {
               Object this$hardwareAccelerationMode = this.getHardwareAccelerationMode();
               Object other$hardwareAccelerationMode = other.getHardwareAccelerationMode();
               if (this$hardwareAccelerationMode == null) {
                  if (other$hardwareAccelerationMode == null) {
                     break label72;
                  }
               } else if (this$hardwareAccelerationMode.equals(other$hardwareAccelerationMode)) {
                  break label72;
               }

               return false;
            }

            Object this$launchMode = this.getLaunchMode();
            Object other$launchMode = other.getLaunchMode();
            if (this$launchMode == null) {
               if (other$launchMode != null) {
                  return false;
               }
            } else if (!this$launchMode.equals(other$launchMode)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof LauncherSettings;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $lastUpdateAttemptTime = this.getLastUpdateAttemptTime();
      result = result * 59 + (int)($lastUpdateAttemptTime >>> 32 ^ $lastUpdateAttemptTime);
      result = result * 59 + this.getLastUpdateAttemptNum();
      result = result * 59 + (this.isDebug() ? 79 : 97);
      result = result * 59 + (this.isNodiffs() ? 79 : 97);
      result = result * 59 + (this.isSkipTlsVerification() ? 79 : 97);
      result = result * 59 + (this.isNoupdates() ? 79 : 97);
      result = result * 59 + (this.isSafemode() ? 79 : 97);
      Object $scale = this.getScale();
      result = result * 59 + ($scale == null ? 43 : $scale.hashCode());
      Object $lastUpdateHash = this.getLastUpdateHash();
      result = result * 59 + ($lastUpdateHash == null ? 43 : $lastUpdateHash.hashCode());
      Object $clientArguments = this.getClientArguments();
      result = result * 59 + ($clientArguments == null ? 43 : $clientArguments.hashCode());
      Object $jvmArguments = this.getJvmArguments();
      result = result * 59 + ($jvmArguments == null ? 43 : $jvmArguments.hashCode());
      Object $hardwareAccelerationMode = this.getHardwareAccelerationMode();
      result = result * 59 + ($hardwareAccelerationMode == null ? 43 : $hardwareAccelerationMode.hashCode());
      Object $launchMode = this.getLaunchMode();
      result = result * 59 + ($launchMode == null ? 43 : $launchMode.hashCode());
      return result;
   }

   public String toString() {
      long var10000 = this.getLastUpdateAttemptTime();
      return "LauncherSettings(lastUpdateAttemptTime=" + var10000 + ", lastUpdateHash=" + this.getLastUpdateHash() + ", lastUpdateAttemptNum=" + this.getLastUpdateAttemptNum() + ", debug=" + this.isDebug() + ", nodiffs=" + this.isNodiffs() + ", skipTlsVerification=" + this.isSkipTlsVerification() + ", noupdates=" + this.isNoupdates() + ", safemode=" + this.isSafemode() + ", scale=" + this.getScale() + ", clientArguments=" + this.getClientArguments() + ", jvmArguments=" + this.getJvmArguments() + ", hardwareAccelerationMode=" + this.getHardwareAccelerationMode() + ", launchMode=" + this.getLaunchMode() + ")";
   }
}
