package net.runelite.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.runelite.launcher.beans.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JvmLauncher {
   private static final Logger log = LoggerFactory.getLogger(JvmLauncher.class);
   private static final Logger logger = LoggerFactory.getLogger(JvmLauncher.class);

   private static String getJava() throws FileNotFoundException {
      Path javaHome = Paths.get(System.getProperty("java.home"));
      if (!Files.exists(javaHome, new LinkOption[0])) {
         throw new FileNotFoundException("JAVA_HOME is not set correctly! directory \"" + javaHome + "\" does not exist.");
      } else {
         Path javaPath = Paths.get(javaHome.toString(), "bin", "java.exe");
         if (!Files.exists(javaPath, new LinkOption[0])) {
            javaPath = Paths.get(javaHome.toString(), "bin", "java");
         }

         if (!Files.exists(javaPath, new LinkOption[0])) {
            throw new FileNotFoundException("java executable not found in directory \"" + javaPath.getParent() + "\"");
         } else {
            return javaPath.toAbsolutePath().toString();
         }
      }
   }

   static void launch(Bootstrap bootstrap, List<File> classpath, Collection<String> clientArgs, Map<String, String> jvmProps, List<String> jvmArgs) throws IOException {
      StringBuilder classPath = new StringBuilder();

      File f;
      for(Iterator var6 = classpath.iterator(); var6.hasNext(); classPath.append(f.getAbsolutePath())) {
         f = (File)var6.next();
         if (classPath.length() > 0) {
            classPath.append(File.pathSeparatorChar);
         }
      }

      String javaExePath;
      try {
         javaExePath = getJava();
      } catch (FileNotFoundException var13) {
         FileNotFoundException ex = var13;
         logger.error("Unable to find java executable", ex);
         return;
      }

      List<String> arguments = new ArrayList();
      arguments.add(javaExePath);
      arguments.add("-cp");
      arguments.add(classPath.toString());
      String[] jvmArguments = getJvmArguments(bootstrap);
      if (jvmArguments != null) {
         arguments.addAll(Arrays.asList(jvmArguments));
      }

      Iterator var9 = jvmProps.entrySet().iterator();

      while(var9.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var9.next();
         String var10001 = (String)entry.getKey();
         arguments.add("-D" + var10001 + "=" + (String)entry.getValue());
      }

      arguments.addAll(jvmArgs);
      arguments.add(LauncherProperties.getMain());
      arguments.addAll(clientArgs);
      logger.info("Running {}", arguments);
      ProcessBuilder builder = new ProcessBuilder((String[])arguments.toArray(new String[0]));
      builder.inheritIO();
      Process process = builder.start();
      if (log.isDebugEnabled()) {
         SplashScreen.stop();

         try {
            process.waitFor();
         } catch (InterruptedException var12) {
            InterruptedException e = var12;
            throw new RuntimeException(e);
         }
      }

   }

   static String[] getJvmArguments(Bootstrap bootstrap) {
      if (Launcher.isJava17()) {
         String[] args;
         switch (OS.getOs()) {
            case Windows:
               args = bootstrap.getClientJvm17WindowsArguments();
               return args != null ? args : bootstrap.getClientJvm17Arguments();
            case MacOS:
               args = bootstrap.getClientJvm17MacArguments();
               return args != null ? args : bootstrap.getClientJvm17Arguments();
            default:
               return bootstrap.getClientJvm17Arguments();
         }
      } else {
         return bootstrap.getClientJvm9Arguments();
      }
   }
}
