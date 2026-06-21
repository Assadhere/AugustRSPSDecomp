package net.runelite.launcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.runelite.launcher.beans.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ForkLauncher {
   private static final Logger log = LoggerFactory.getLogger(ForkLauncher.class);

   static boolean canForkLaunch() {
      OS.OSType os = OS.getOs();
      if (os != OS.OSType.Windows && os != OS.OSType.MacOS) {
         return false;
      } else {
         ProcessHandle current = ProcessHandle.current();
         Optional<String> command = current.info().command();
         if (command.isEmpty()) {
            return false;
         } else {
            Path path = Paths.get((String)command.get());
            String name = path.getFileName().toString();
            return name.equals("AugustRSPS.exe") || name.equals("AugustRSPS");
         }
      }
   }

   static void launch(Bootstrap bootstrap, List<File> classpath, Collection<String> clientArgs, Map<String, String> jvmProps, List<String> jvmArgs) throws IOException {
      ProcessHandle current = ProcessHandle.current();
      Path path = Paths.get((String)current.info().command().get());
      if (OS.getOs() == OS.OSType.MacOS) {
         path = path.normalize().resolveSibling(Path.of("..", "MacOS", path.getFileName().toString())).normalize();
      }

      ArrayList<Object> commands = new ArrayList();
      commands.add(path.toAbsolutePath().toString());
      commands.add("-c");
      String[] clientJvmArgs = JvmLauncher.getJvmArguments(bootstrap);
      if (clientJvmArgs != null) {
         String[] var9 = clientJvmArgs;
         int var10 = clientJvmArgs.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String arg = var9[var11];
            commands.add("-J");
            commands.add(arg);
         }
      }

      Iterator var13 = jvmProps.entrySet().iterator();

      while(var13.hasNext()) {
         Map.Entry<String, String> prop = (Map.Entry)var13.next();
         commands.add("-J");
         String var10001 = (String)prop.getKey();
         commands.add("-D" + var10001 + "=" + (String)prop.getValue());
      }

      var13 = jvmArgs.iterator();

      while(var13.hasNext()) {
         String arg = (String)var13.next();
         commands.add("-J");
         commands.add(arg);
      }

      commands.add("--");
      if (classpath.isEmpty()) {
         throw new RuntimeException("cannot fork launch with an empty classpath");
      } else {
         commands.add("--classpath");
         StringBuilder sb = new StringBuilder();

         File f;
         for(Iterator var17 = classpath.iterator(); var17.hasNext(); sb.append(f.getName())) {
            f = (File)var17.next();
            if (sb.length() > 0) {
               sb.append(File.pathSeparatorChar);
            }
         }

         commands.add(sb.toString());
         commands.addAll(clientArgs);
         log.debug("Running process: {}", commands);
         ProcessBuilder builder = new ProcessBuilder((String[])commands.toArray(new String[0]));
         builder.start();
      }
   }
}
