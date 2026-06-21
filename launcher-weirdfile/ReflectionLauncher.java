package net.runelite.launcher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionLauncher {
   private static final Logger log = LoggerFactory.getLogger(ReflectionLauncher.class);

   static void launch(List<File> classpath, Collection<String> clientArgs) throws MalformedURLException {
      URL[] jarUrls = new URL[classpath.size()];
      int i = 0;

      File file;
      for(Iterator var4 = classpath.iterator(); var4.hasNext(); jarUrls[i++] = file.toURI().toURL()) {
         file = (File)var4.next();
         log.debug("Adding jar: {}", file);
      }

      ClassLoader parent = ClassLoader.getPlatformClassLoader();
      URLClassLoader loader = new URLClassLoader(jarUrls, parent);
      UIManager.put("ClassLoader", loader);
      Thread thread = new Thread(() -> {
         try {
            Class<?> mainClass = loader.loadClass(LauncherProperties.getMain());
            Method main = mainClass.getMethod("main", String[].class);
            main.invoke((Object)null, (Object)clientArgs.toArray(new String[0]));
         } catch (Exception var4) {
            Exception ex = var4;
            log.error("Unable to launch client", ex);
         }

      });
      thread.setName("AugustRSPS");
      thread.start();
   }
}
