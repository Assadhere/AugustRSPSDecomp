package net.runelite.launcher;

import ch.qos.logback.classic.Level;
import com.google.archivepatcher.applier.FileByFileV1DeltaApplier;
import com.google.archivepatcher.shared.DefaultDeflateCompatibilityWindow;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Runtime.Version;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import javax.swing.SwingUtilities;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.runelite.launcher.beans.Artifact;
import net.runelite.launcher.beans.Bootstrap;
import net.runelite.launcher.beans.Diff;
import net.runelite.launcher.beans.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
   private static final Logger log = LoggerFactory.getLogger(Launcher.class);
   private static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".augustrunelite");
   public static final File LOGS_DIR;
   private static final File REPO_DIR;
   public static final File CRASH_FILES;
   private static final String USER_AGENT;
   static final String LAUNCHER_EXECUTABLE_NAME_WIN = "AugustRSPS.exe";
   static final String LAUNCHER_EXECUTABLE_NAME_OSX = "AugustRSPS";

   public static void main(String[] args) {
      OptionParser parser = new OptionParser(false);
      parser.allowsUnrecognizedOptions();
      parser.accepts("postinstall", "Perform post-install tasks");
      parser.accepts("debug", "Enable debug logging");
      parser.accepts("nodiff", "Always download full artifacts instead of diffs");
      parser.accepts("insecure-skip-tls-verification", "Disable TLS certificate and hostname verification");
      parser.accepts("scale", "Custom scale factor for Java 2D").withRequiredArg();
      parser.accepts("noupdate", "Skips the launcher self-update");
      parser.accepts("help", "Show this text (use -- --help for client help)").forHelp();
      parser.accepts("classpath", "Classpath for the client").withRequiredArg();
      parser.accepts("J", "JVM argument (FORK or JVM launch mode only)").withRequiredArg();
      parser.accepts("configure", "Opens configuration GUI");
      parser.accepts("launch-mode", "JVM launch method (JVM, FORK, REFLECT)").withRequiredArg().ofType(LaunchMode.class);
      parser.accepts("hw-accel", "Java 2D hardware acceleration mode (OFF, DIRECTDRAW, OPENGL, METAL)").withRequiredArg().ofType(HardwareAccelerationMode.class);
      parser.accepts("mode", "Alias of hw-accel").withRequiredArg().ofType(HardwareAccelerationMode.class);
      if (OS.getOs() == OS.OSType.MacOS) {
         parser.accepts("p").withRequiredArg();
      }

      OptionSet options;
      try {
         options = parser.parse(args);
      } catch (OptionException var24) {
         OptionException ex = var24;
         log.error("unable to parse arguments", ex);
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("AugustRSPS was unable to parse the provided application arguments: " + ex.getMessage())).open();
         });
         throw ex;
      }

      if (options.has("help")) {
         try {
            parser.printHelpOn(System.out);
         } catch (IOException var23) {
            IOException e = var23;
            log.error((String)null, e);
         }

         System.exit(0);
      }

      if (options.has("configure")) {
         ConfigurationFrame.open();
      } else {
         LauncherSettings settings = LauncherSettings.loadSettings();
         settings.apply(options);
         boolean postInstall = options.has("postinstall");
         LOGS_DIR.mkdirs();
         if (settings.isDebug()) {
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("ROOT");
            logger.setLevel(Level.DEBUG);
         }

         initDll();
         initDllBlacklist();

         try {
            if (!options.has("classpath")) {
               Map<String, String> jvmProps = new LinkedHashMap();
               if (settings.scale != null) {
                  jvmProps.put("sun.java2d.dpiaware", "true");
                  jvmProps.put("sun.java2d.uiScale", Double.toString(settings.scale));
               }

               HardwareAccelerationMode hardwareAccelMode = settings.hardwareAccelerationMode == HardwareAccelerationMode.AUTO ? HardwareAccelerationMode.defaultMode(OS.getOs()) : settings.hardwareAccelerationMode;
               jvmProps.putAll(hardwareAccelMode.toParams(OS.getOs()));
               if (OS.getOs() == OS.OSType.MacOS) {
                  jvmProps.put("apple.awt.application.appearance", "system");
               }

               jvmProps.put(LauncherProperties.getVersionKey(), LauncherProperties.getVersion());
               if (settings.isSkipTlsVerification()) {
                  jvmProps.put("runelite.insecure-skip-tls-verification", "true");
               }

               log.info("AugustRSPS Launcher version {}", LauncherProperties.getVersion());
               log.info("Launcher configuration:" + System.lineSeparator() + "{}", settings.configurationStr());
               log.info("Using hardware acceleration mode: {}", hardwareAccelMode);
               setJvmParams(jvmProps);
               if (settings.isSkipTlsVerification()) {
                  TrustManagerUtil.setupInsecureTrustManager();
               } else {
                  TrustManagerUtil.setupTrustManager();
               }

               if (postInstall) {
                  postInstall();
                  return;
               }

               SplashScreen.init();
               SplashScreen.stage(0.0, "Preparing", "Setting up environment");
               if (log.isDebugEnabled()) {
                  RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                  log.debug("Command line arguments: {}", String.join(" ", args));
                  log.debug("Java VM arguments: {}", String.join(" ", runtime.getInputArguments()));
                  log.debug("Java Environment:");
                  Properties p = System.getProperties();
                  Enumeration<Object> keys = p.keys();

                  while(keys.hasMoreElements()) {
                     String key = (String)keys.nextElement();
                     String value = (String)p.get(key);
                     log.debug("  {}: {}", key, value);
                  }
               }

               SplashScreen.stage(0.05, (String)null, "Downloading bootstrap");

               Bootstrap bootstrap;
               try {
                  bootstrap = getBootstrap();
               } catch (VerificationException | CertificateException | SignatureException | InvalidKeyException | NoSuchAlgorithmException | IOException var26) {
                  Exception ex = var26;
                  log.error("error fetching bootstrap", ex);
                  String extract = CertPathExtractor.extract(ex);
                  if (extract != null) {
                     log.error("untrusted certificate chain: {}", extract);
                  }

                  SwingUtilities.invokeLater(() -> {
                     FatalErrorDialog.showNetErrorWindow("downloading the bootstrap", ex);
                  });
                  return;
               }

               SplashScreen.stage(0.07, (String)null, "Checking for updates");
               Updater.update(bootstrap, settings, args);
               SplashScreen.stage(0.1, (String)null, "Tidying the cache");
               if (jvmOutdated(bootstrap)) {
                  return;
               }

               PackrConfig.updateLauncherArgs(bootstrap);
               if (!REPO_DIR.exists() && !REPO_DIR.mkdirs()) {
                  log.error("unable to create repo directory {}", REPO_DIR);
                  SwingUtilities.invokeLater(() -> {
                     (new FatalErrorDialog("Unable to create AugustRSPS directory " + REPO_DIR.getAbsolutePath() + ". Check your filesystem permissions are correct.")).open();
                  });
                  return;
               }

               List<Artifact> artifacts = (List)Arrays.stream(bootstrap.getArtifacts()).filter((a) -> {
                  if (a.getPlatform() == null) {
                     return true;
                  } else {
                     String os = System.getProperty("os.name");
                     String arch = System.getProperty("os.arch");
                     Platform[] var3 = a.getPlatform();
                     int var4 = var3.length;

                     for(int var5 = 0; var5 < var4; ++var5) {
                        Platform platform = var3[var5];
                        if (platform.getName() != null) {
                           OS.OSType platformOs = OS.parseOs(platform.getName());
                           if (platformOs == OS.OSType.Other) {
                              if (!platform.getName().equals(os)) {
                                 continue;
                              }
                           } else if (platformOs != OS.getOs()) {
                              continue;
                           }

                           if (platform.getArch() == null || platform.getArch().equals(arch)) {
                              return true;
                           }
                        }
                     }

                     return false;
                  }
               }).collect(Collectors.toList());
               clean(artifacts);

               try {
                  download(artifacts, settings.isNodiffs());
               } catch (IOException var25) {
                  IOException ex = var25;
                  log.error("unable to download artifacts", ex);
                  SwingUtilities.invokeLater(() -> {
                     FatalErrorDialog.showNetErrorWindow("downloading the client", ex);
                  });
                  return;
               }

               SplashScreen.stage(0.8, (String)null, "Verifying");

               try {
                  verifyJarHashes(artifacts);
               } catch (VerificationException var27) {
                  VerificationException ex = var27;
                  log.error("Unable to verify artifacts", ex);
                  SwingUtilities.invokeLater(() -> {
                     FatalErrorDialog.showNetErrorWindow("verifying downloaded files", ex);
                  });
                  return;
               }

               Collection<String> clientArgs = getClientArgs(settings);
               SplashScreen.stage(0.9, "Starting the client", "");
               List<File> classpath = (List)artifacts.stream().map((dep) -> {
                  return new File(REPO_DIR, dep.getName());
               }).collect(Collectors.toList());
               List<String> jvmParams = new ArrayList();
               log.debug("Setting JVM crash log location to {}", CRASH_FILES);
               jvmParams.add("-XX:ErrorFile=" + CRASH_FILES.getAbsolutePath());
               jvmParams.addAll(getJvmArgs(settings));
               if (settings.launchMode == LaunchMode.REFLECT) {
                  log.debug("Using launch mode: REFLECT");
                  ReflectionLauncher.launch(classpath, clientArgs);
                  return;
               } else {
                  if (settings.launchMode != LaunchMode.FORK && (settings.launchMode != LaunchMode.AUTO || !ForkLauncher.canForkLaunch())) {
                     log.debug("Using launch mode: JVM");
                     JvmLauncher.launch(bootstrap, classpath, clientArgs, jvmProps, jvmParams);
                  } else {
                     log.debug("Using launch mode: FORK");
                     ForkLauncher.launch(bootstrap, classpath, clientArgs, jvmProps, jvmParams);
                  }

                  return;
               }
            }

            TrustManagerUtil.setupTrustManager();
            String classpathOpt = String.valueOf(options.valueOf("classpath"));
            List<File> classpath = (List)Streams.stream(Splitter.on(File.pathSeparatorChar).split(classpathOpt)).map((name) -> {
               return new File(REPO_DIR, name);
            }).collect(Collectors.toList());

            try {
               ReflectionLauncher.launch(classpath, getClientArgs(settings));
            } catch (Exception var22) {
               Exception e = var22;
               log.error("error launching client", e);
            }
         } catch (Exception var28) {
            Exception e = var28;
            log.error("Failure during startup", e);
            if (!postInstall) {
               SwingUtilities.invokeLater(() -> {
                  (new FatalErrorDialog("AugustRSPS has encountered an unexpected error during startup.")).open();
               });
            }

            return;
         } catch (Error var29) {
            Error e = var29;
            log.error("Failure during startup", e);
            throw e;
         } finally {
            SplashScreen.stop();
         }

      }
   }

   private static void setJvmParams(Map<String, String> params) {
      Iterator var1 = params.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var1.next();
         System.setProperty((String)entry.getKey(), (String)entry.getValue());
      }

   }

   private static Bootstrap getBootstrap() throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, VerificationException {
      URL u = new URL(LauncherProperties.getBootstrap());
      URLConnection conn = u.openConnection();
      conn.setRequestProperty("User-Agent", USER_AGENT);
      InputStream i = conn.getInputStream();

      Bootstrap var7;
      try {
         byte[] bytes = ByteStreams.toByteArray(i);
         Certificate certificate = getCertificate();
         Signature s = Signature.getInstance("SHA256withRSA");
         s.initVerify(certificate);
         s.update(bytes);
         Gson g = new Gson();
         var7 = (Bootstrap)g.fromJson(new InputStreamReader(new ByteArrayInputStream(bytes)), Bootstrap.class);
      } catch (Throwable var9) {
         if (i != null) {
            try {
               i.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (i != null) {
         i.close();
      }

      return var7;
   }

   private static boolean jvmOutdated(Bootstrap bootstrap) {
      boolean launcherTooOld = bootstrap.getRequiredLauncherVersion() != null && compareVersion(bootstrap.getRequiredLauncherVersion(), LauncherProperties.getVersion()) > 0;
      boolean jvmTooOld = false;

      try {
         if (bootstrap.getRequiredJVMVersion() != null) {
            jvmTooOld = Version.parse(bootstrap.getRequiredJVMVersion()).compareTo(Runtime.version()) > 0;
         }
      } catch (IllegalArgumentException var4) {
         IllegalArgumentException e = var4;
         log.warn("Unable to parse bootstrap version", e);
      }

      if (launcherTooOld) {
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("Your launcher is too old to start AugustRSPS. Please download and install a more recent one from august-rsps.com.")).addButton("august-rsps.com", () -> {
               LinkBrowser.browse(LauncherProperties.getDownloadLink());
            }).open();
         });
         return true;
      } else if (jvmTooOld) {
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("Your Java installation is too old. AugustRSPS now requires Java " + bootstrap.getRequiredJVMVersion() + " to run. You can get a platform specific version from august-rsps.com, or install a newer version of Java.")).addButton("august-rsps.com", () -> {
               LinkBrowser.browse(LauncherProperties.getDownloadLink());
            }).open();
         });
         return true;
      } else {
         return false;
      }
   }

   private static Collection<String> getClientArgs(LauncherSettings settings) {
      ArrayList<String> args = new ArrayList(settings.clientArguments);
      String clientArgs = System.getenv("RUNELITE_ARGS");
      if (!Strings.isNullOrEmpty(clientArgs)) {
         args.addAll(Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(clientArgs));
      }

      if (settings.debug) {
         args.add("--debug");
      }

      if (settings.safemode) {
         args.add("--safe-mode");
      }

      return args;
   }

   private static List<String> getJvmArgs(LauncherSettings settings) {
      ArrayList<String> args = new ArrayList(settings.jvmArguments);
      String envArgs = System.getenv("RUNELITE_VMARGS");
      if (!Strings.isNullOrEmpty(envArgs)) {
         args.addAll(Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(envArgs));
      }

      return args;
   }

   private static void download(List<Artifact> artifacts, boolean nodiff) throws IOException {
      List<Artifact> toDownload = new ArrayList(artifacts.size());
      Map<Artifact, Diff> diffs = new HashMap();
      int totalDownloadBytes = 0;
      boolean isCompatible = (new DefaultDeflateCompatibilityWindow()).isCompatible();
      if (!isCompatible && !nodiff) {
         log.debug("System zlib is not compatible with archive-patcher; not using diffs");
         nodiff = true;
      }

      Iterator var6 = artifacts.iterator();

      int total;
      while(var6.hasNext()) {
         Artifact artifact = (Artifact)var6.next();
         File dest = new File(REPO_DIR, artifact.getName());

         String hash;
         try {
            hash = hash(dest);
         } catch (FileNotFoundException var28) {
            hash = null;
         }

         if (Objects.equals(hash, artifact.getHash())) {
            log.debug("Hash for {} up to date", artifact.getName());
         } else {
            int downloadSize = artifact.getSize();
            if (!nodiff && artifact.getDiffs() != null) {
               Diff[] var11 = artifact.getDiffs();
               total = var11.length;

               for(int var13 = 0; var13 < total; ++var13) {
                  Diff diff = var11[var13];
                  File old = new File(REPO_DIR, diff.getFrom());

                  String oldhash;
                  try {
                     oldhash = hash(old);
                  } catch (FileNotFoundException var27) {
                     oldhash = null;
                  }

                  if (diff.getFromHash().equals(oldhash)) {
                     diffs.put(artifact, diff);
                     downloadSize = diff.getSize();
                  }
               }
            }

            toDownload.add(artifact);
            totalDownloadBytes += downloadSize;
         }
      }

      double START_PROGRESS = 0.15;
      int downloaded = 0;
      SplashScreen.stage(0.15, "Downloading", "");
      Iterator var34 = toDownload.iterator();

      while(true) {
         Artifact artifact;
         File dest;
         while(true) {
            if (!var34.hasNext()) {
               return;
            }

            artifact = (Artifact)var34.next();
            dest = new File(REPO_DIR, artifact.getName());
            total = downloaded;
            Diff diff = (Diff)diffs.get(artifact);
            if (diff == null) {
               break;
            }

            log.debug("Downloading diff {}", diff.getName());

            try {
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               download(diff.getPath(), diff.getHash(), (completed) -> {
                  SplashScreen.stage(0.15, 0.8, (String)null, diff.getName(), total + completed, totalDownloadBytes, true);
               }, out);
               downloaded += diff.getSize();
               File old = new File(REPO_DIR, diff.getFrom());
               InputStream patchStream = new GZIPInputStream(new ByteArrayInputStream(out.toByteArray()));

               HashCode hash;
               try {
                  HashingOutputStream fout = new HashingOutputStream(Hashing.sha256(), Files.newOutputStream(dest.toPath()));

                  try {
                     (new FileByFileV1DeltaApplier()).applyDelta(old, patchStream, fout);
                     hash = fout.hash();
                  } catch (Throwable var25) {
                     try {
                        fout.close();
                     } catch (Throwable var24) {
                        var25.addSuppressed(var24);
                     }

                     throw var25;
                  }

                  fout.close();
               } catch (Throwable var26) {
                  try {
                     ((InputStream)patchStream).close();
                  } catch (Throwable var23) {
                     var26.addSuppressed(var23);
                  }

                  throw var26;
               }

               ((InputStream)patchStream).close();
               if (artifact.getHash().equals(hash.toString())) {
                  log.debug("Patching successful for {}", artifact.getName());
                  continue;
               }

               log.debug("Patched artifact hash mismatches! {}: got {} expected {}", new Object[]{artifact.getName(), hash.toString(), artifact.getHash()});
            } catch (VerificationException | IOException var29) {
               log.warn("unable to download patch {}", diff.getName(), var29);
            }

            totalDownloadBytes -= diff.getSize();
            totalDownloadBytes += artifact.getSize();
            break;
         }

         log.debug("Downloading {}", artifact.getName());

         try {
            OutputStream fout = Files.newOutputStream(dest.toPath());

            try {
               download(artifact.getPath(), artifact.getHash(), (completed) -> {
                  SplashScreen.stage(0.15, 0.8, (String)null, artifact.getName(), total + completed, totalDownloadBytes, true);
               }, fout);
               downloaded += artifact.getSize();
            } catch (Throwable var30) {
               if (fout != null) {
                  try {
                     fout.close();
                  } catch (Throwable var22) {
                     var30.addSuppressed(var22);
                  }
               }

               throw var30;
            }

            if (fout != null) {
               fout.close();
            }
         } catch (VerificationException var31) {
            log.warn("unable to verify jar {}", artifact.getName(), var31);
         }
      }
   }

   private static void clean(List<Artifact> artifacts) {
      File[] existingFiles = REPO_DIR.listFiles();
      if (existingFiles != null) {
         Set<String> artifactNames = new HashSet();
         Iterator var3 = artifacts.iterator();

         while(true) {
            Artifact artifact;
            do {
               if (!var3.hasNext()) {
                  File[] var9 = existingFiles;
                  int var10 = existingFiles.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     File file = var9[var11];
                     if (file.isFile() && !artifactNames.contains(file.getName())) {
                        if (file.delete()) {
                           log.debug("Deleted old artifact {}", file);
                        } else {
                           log.warn("Unable to delete old artifact {}", file);
                        }
                     }
                  }

                  return;
               }

               artifact = (Artifact)var3.next();
               artifactNames.add(artifact.getName());
            } while(artifact.getDiffs() == null);

            Diff[] var5 = artifact.getDiffs();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Diff diff = var5[var7];
               artifactNames.add(diff.getFrom());
            }
         }
      }
   }

   private static void verifyJarHashes(List<Artifact> artifacts) throws VerificationException {
      Iterator var1 = artifacts.iterator();

      while(var1.hasNext()) {
         Artifact artifact = (Artifact)var1.next();
         String expectedHash = artifact.getHash();

         String fileHash;
         try {
            fileHash = hash(new File(REPO_DIR, artifact.getName()));
         } catch (IOException var6) {
            IOException e = var6;
            throw new VerificationException("unable to hash file", e);
         }

         if (!fileHash.equals(expectedHash)) {
            log.warn("Expected {} for {} but got {}", new Object[]{expectedHash, artifact.getName(), fileHash});
            throw new VerificationException("Expected " + expectedHash + " for " + artifact.getName() + " but got " + fileHash);
         }

         log.info("Verified hash of {}", artifact.getName());
      }

   }

   private static String hash(File file) throws IOException {
      HashFunction sha256 = Hashing.sha256();
      return com.google.common.io.Files.asByteSource(file).hash(sha256).toString();
   }

   private static Certificate getCertificate() throws CertificateException {
      CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
      Certificate certificate = certFactory.generateCertificate(Launcher.class.getResourceAsStream("runelite.crt"));
      return certificate;
   }

   static int compareVersion(String a, String b) {
      Pattern tok = Pattern.compile("[^0-9a-zA-Z]");
      return Arrays.compare(tok.split(a), tok.split(b), (x, y) -> {
         Integer ix = null;

         try {
            ix = Integer.parseInt(x);
         } catch (NumberFormatException var6) {
         }

         Integer iy = null;

         try {
            iy = Integer.parseInt(y);
         } catch (NumberFormatException var5) {
         }

         if (ix == null && iy == null) {
            return x.compareToIgnoreCase(y);
         } else if (ix == null) {
            return -1;
         } else if (iy == null) {
            return 1;
         } else if (ix > iy) {
            return 1;
         } else {
            return ix < iy ? -1 : 0;
         }
      });
   }

   static void download(String path, String hash, IntConsumer progress, OutputStream out) throws IOException, VerificationException {
      URL url = new URL(path);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestProperty("User-Agent", USER_AGENT);
      conn.getResponseCode();
      InputStream err = conn.getErrorStream();
      if (err != null) {
         err.close();
         throw new IOException("Unable to download " + path + " - " + conn.getResponseMessage());
      } else {
         int downloaded = 0;
         HashingOutputStream hout = new HashingOutputStream(Hashing.sha256(), out);
         InputStream in = conn.getInputStream();

         try {
            byte[] buffer = new byte[1048576];

            int i;
            while((i = in.read(buffer)) != -1) {
               hout.write(buffer, 0, i);
               downloaded += i;
               progress.accept(downloaded);
            }
         } catch (Throwable var13) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }
            }

            throw var13;
         }

         if (in != null) {
            in.close();
         }

         HashCode hashCode = hout.hash();
         if (!hash.equals(hashCode.toString())) {
            throw new VerificationException("Unable to verify resource " + path + " - expected " + hash + " got " + hashCode.toString());
         }
      }
   }

   static boolean isJava17() {
      return Runtime.version().feature() >= 16;
   }

   private static void postInstall() {
      Bootstrap bootstrap;
      try {
         bootstrap = getBootstrap();
      } catch (VerificationException | CertificateException | SignatureException | InvalidKeyException | NoSuchAlgorithmException | IOException var2) {
         Exception ex = var2;
         log.error("error fetching bootstrap", ex);
         return;
      }

      PackrConfig.updateLauncherArgs(bootstrap);
      log.info("Performed postinstall steps");
   }

   private static void initDll() {
      if (OS.getOs() == OS.OSType.Windows) {
         String arch = System.getProperty("os.arch");
         if (!Set.of("x86", "amd64", "aarch64").contains(arch)) {
            log.debug("System architecture is not supported for launcher natives: {}", arch);
         } else {
            try {
               System.loadLibrary("launcher_" + arch);
               log.debug("Loaded launcher native launcher_{}", arch);
            } catch (Error var2) {
               Error ex = var2;
               log.debug("Error loading launcher native", ex);
            }

         }
      }
   }

   private static void initDllBlacklist() {
      String blacklistedDlls = System.getProperty("runelite.launcher.blacklistedDlls");
      if (blacklistedDlls != null && !blacklistedDlls.isEmpty()) {
         String[] dlls = blacklistedDlls.split(",");

         try {
            log.debug("Setting blacklisted dlls: {}", blacklistedDlls);
            setBlacklistedDlls(dlls);
         } catch (UnsatisfiedLinkError var3) {
            UnsatisfiedLinkError ex = var3;
            log.debug("Error setting dll blacklist", ex);
         }

      }
   }

   private static native void setBlacklistedDlls(String[] var0);

   static native String regQueryString(String var0, String var1);

   static {
      LOGS_DIR = new File(RUNELITE_DIR, "logs");
      REPO_DIR = new File(RUNELITE_DIR, "repository2");
      CRASH_FILES = new File(LOGS_DIR, "jvm_crash_pid_%p.log");
      USER_AGENT = "AugustRSPS/" + LauncherProperties.getVersion();
   }
}
