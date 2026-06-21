package net.runelite.client.config;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigData {
   private static final Logger log = LoggerFactory.getLogger(ConfigData.class);
   private final File configPath;
   private final ConcurrentHashMap<String, String> properties;
   private Map<String, String> patchChanges = new HashMap();

   ConfigData(File configPath) {
      this.configPath = configPath;
      Properties props = new Properties();

      try {
         FileInputStream in = new FileInputStream(configPath);

         try {
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

            try {
               props.load(reader);
            } catch (Throwable var9) {
               try {
                  reader.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            reader.close();
         } catch (Throwable var10) {
            try {
               in.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         in.close();
      } catch (FileNotFoundException var11) {
      } catch (Exception var12) {
         Exception ex = var12;
         throw new RuntimeException(ex);
      }

      this.properties = new ConcurrentHashMap(props.size());
      props.forEach((k, v) -> {
         this.properties.put((String)k, (String)v);
      });
   }

   String getProperty(String key) {
      return (String)this.properties.get(key);
   }

   synchronized String setProperty(String key, String value) {
      String old = (String)this.properties.put(key, value);
      if (!Objects.equals(old, value)) {
         this.patchChanges.put(key, value);
      }

      return old;
   }

   synchronized String unset(String key) {
      String old = (String)this.properties.remove(key);
      if (old != null) {
         this.patchChanges.put(key, (Object)null);
      }

      return old;
   }

   synchronized void putAll(Map<String, String> values) {
      this.patchChanges.putAll(values);
      this.properties.putAll(values);
   }

   Set<String> keySet() {
      return this.properties.keySet();
   }

   Map<String, String> get() {
      return Collections.unmodifiableMap(this.properties);
   }

   synchronized Map<String, String> swapChanges() {
      if (this.patchChanges.isEmpty()) {
         return Collections.emptyMap();
      } else {
         Map<String, String> p = this.patchChanges;
         this.patchChanges = new HashMap();
         return p;
      }
   }

   void patch(Map<String, String> patch) {
      File lckFile = new File(this.configPath.getParentFile(), this.configPath.getName() + ".lck");

      try {
         FileOutputStream lockOut = new FileOutputStream(lckFile);

         try {
            FileChannel lckChannel = lockOut.getChannel();

            try {
               lckChannel.lock();
               Properties tempProps = new Properties();

               try {
                  FileInputStream in = new FileInputStream(this.configPath);

                  try {
                     InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

                     try {
                        tempProps.load(reader);
                     } catch (Throwable var21) {
                        try {
                           reader.close();
                        } catch (Throwable var20) {
                           var21.addSuppressed(var20);
                        }

                        throw var21;
                     }

                     reader.close();
                  } catch (Throwable var22) {
                     try {
                        in.close();
                     } catch (Throwable var19) {
                        var22.addSuppressed(var19);
                     }

                     throw var22;
                  }

                  in.close();
               } catch (FileNotFoundException var23) {
                  log.debug("config file {} does not exist", this.configPath);
               }

               if (tempProps.isEmpty()) {
                  tempProps.putAll(this.properties);
               } else {
                  Iterator var30 = patch.entrySet().iterator();

                  while(var30.hasNext()) {
                     Map.Entry<String, String> entry = (Map.Entry)var30.next();
                     if (entry.getValue() == null) {
                        tempProps.remove(entry.getKey());
                     } else {
                        tempProps.put(entry.getKey(), entry.getValue());
                     }
                  }
               }

               File tempFile = File.createTempFile("runelite_config", (String)null, this.configPath.getParentFile());
               FileOutputStream out = new FileOutputStream(tempFile);

               try {
                  FileChannel channel = out.getChannel();

                  try {
                     OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

                     try {
                        channel.lock();
                        tempProps.store(writer, "RuneLite configuration");
                        writer.flush();
                        channel.force(true);
                     } catch (Throwable var18) {
                        try {
                           writer.close();
                        } catch (Throwable var16) {
                           var18.addSuppressed(var16);
                        }

                        throw var18;
                     }

                     writer.close();
                  } catch (Throwable var24) {
                     if (channel != null) {
                        try {
                           channel.close();
                        } catch (Throwable var15) {
                           var24.addSuppressed(var15);
                        }
                     }

                     throw var24;
                  }

                  if (channel != null) {
                     channel.close();
                  }
               } catch (Throwable var25) {
                  try {
                     out.close();
                  } catch (Throwable var14) {
                     var25.addSuppressed(var14);
                  }

                  throw var25;
               }

               out.close();

               try {
                  Files.move(tempFile.toPath(), this.configPath.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
               } catch (AtomicMoveNotSupportedException var17) {
                  AtomicMoveNotSupportedException ex = var17;
                  log.debug("atomic move not supported", ex);
                  Files.move(tempFile.toPath(), this.configPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
               }
            } catch (Throwable var26) {
               if (lckChannel != null) {
                  try {
                     lckChannel.close();
                  } catch (Throwable var13) {
                     var26.addSuppressed(var13);
                  }
               }

               throw var26;
            }

            if (lckChannel != null) {
               lckChannel.close();
            }
         } catch (Throwable var27) {
            try {
               lockOut.close();
            } catch (Throwable var12) {
               var27.addSuppressed(var12);
            }

            throw var27;
         }

         lockOut.close();
      } catch (IOException var28) {
         IOException ex = var28;
         log.error("unable to save configuration file", ex);
      }

      lckFile.delete();
   }
}
