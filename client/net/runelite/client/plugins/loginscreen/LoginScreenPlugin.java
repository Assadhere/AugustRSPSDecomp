package net.runelite.client.plugins.loginscreen;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.inject.Provides;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.OSType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Login Screen",
   description = "Provides various enhancements for login screen"
)
public class LoginScreenPlugin extends Plugin implements KeyListener {
   private static final Logger log = LoggerFactory.getLogger(LoginScreenPlugin.class);
   private static final int MAX_USERNAME_LENGTH = 254;
   private static final int MAX_PIN_LENGTH = 6;
   private static final File CUSTOM_LOGIN_SCREEN_FILE;
   private static final File LOGINSCREENS;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private LoginScreenConfig config;
   @Inject
   private KeyManager keyManager;
   @Inject
   private OkHttpClient okHttpClient;
   @Inject
   @Named("runelite.static.base")
   private HttpUrl staticBase;
   private String usernameCache;

   protected void startUp() throws Exception {
      this.applyUsername();
      this.keyManager.registerKeyListener(this);
      this.clientThread.invoke(this::overrideLoginScreen);
   }

   protected void shutDown() throws Exception {
      if (this.config.syncUsername()) {
         this.client.getPreferences().setRememberedUsername(this.usernameCache);
      }

      this.keyManager.unregisterKeyListener(this);
      this.clientThread.invoke(() -> {
         this.restoreLoginScreen();
         this.client.setShouldRenderLoginScreenFire(true);
      });
   }

   @Provides
   LoginScreenConfig getConfig(ConfigManager configManager) {
      return (LoginScreenConfig)configManager.getConfig(LoginScreenConfig.class);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("loginscreen")) {
         this.clientThread.invoke(this::overrideLoginScreen);
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (this.config.syncUsername()) {
         if (event.getGameState() == GameState.LOGIN_SCREEN) {
            this.applyUsername();
         } else if (event.getGameState() == GameState.LOGGED_IN) {
            String username = "";
            if (this.client.getPreferences().getRememberedUsername() != null) {
               username = this.client.getUsername();
            }

            if (this.config.username().equals(username)) {
               return;
            }

            log.debug("Saving username: {}", username);
            this.config.username(username);
         }

      }
   }

   @Subscribe
   public void onProfileChanged(ProfileChanged profileChanged) {
      this.applyUsername();
   }

   private void applyUsername() {
      if (this.config.syncUsername()) {
         GameState gameState = this.client.getGameState();
         if (gameState == GameState.LOGIN_SCREEN) {
            String username = this.config.username();
            if (Strings.isNullOrEmpty(username)) {
               return;
            }

            if (this.usernameCache == null) {
               this.usernameCache = this.client.getPreferences().getRememberedUsername();
            }

            this.client.getPreferences().setRememberedUsername(username);
         }

      }
   }

   public boolean isEnabledOnLoginScreen() {
      return true;
   }

   public void keyTyped(KeyEvent e) {
   }

   public void keyPressed(KeyEvent e) {
      if (this.config.pasteEnabled() && (this.client.getGameState() == GameState.LOGIN_SCREEN || this.client.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR)) {
         boolean isModifierDown = OSType.getOSType() == OSType.MacOS ? e.isMetaDown() : e.isControlDown();
         if (e.getKeyCode() == 86 && isModifierDown) {
            try {
               String data = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().trim();
               switch (this.client.getLoginIndex()) {
                  case 2:
                     if (this.client.getCurrentLoginField() == 0) {
                        this.client.setUsername(data.substring(0, Math.min(data.length(), 254)));
                     }
                     break;
                  case 4:
                     data = CharMatcher.inRange('0', '9').retainFrom(data);
                     this.client.setOtp(data.substring(0, Math.min(data.length(), 6)));
               }
            } catch (IOException | UnsupportedFlavorException var4) {
               Exception ex = var4;
               log.warn("failed to fetch clipboard data", ex);
            }
         }

      }
   }

   public void keyReleased(KeyEvent e) {
   }

   private void overrideLoginScreen() {
      this.client.setShouldRenderLoginScreenFire(this.config.showLoginFire());
      LoginScreenOverride loginScreen = this.config.loginScreen();
      if (loginScreen == LoginScreenOverride.OFF) {
         this.restoreLoginScreen();
      } else {
         if (loginScreen == LoginScreenOverride.CUSTOM) {
            if (CUSTOM_LOGIN_SCREEN_FILE.exists()) {
               try {
                  Class var3 = ImageIO.class;
                  BufferedImage image;
                  synchronized(ImageIO.class) {
                     image = ImageIO.read(CUSTOM_LOGIN_SCREEN_FILE);
                  }

                  if (image.getHeight() > 503) {
                     double scalar = 503.0 / (double)image.getHeight();
                     image = ImageUtil.resizeImage(image, (int)((double)image.getWidth() * scalar), 503);
                  }

                  SpritePixels pixels = ImageUtil.getImageSpritePixels(image, this.client);
                  this.client.setLoginScreen(pixels);
               } catch (IOException var6) {
                  IOException e = var6;
                  log.error("error loading custom login screen", e);
                  this.restoreLoginScreen();
               }
            }
         } else {
            if (loginScreen == LoginScreenOverride.RANDOM) {
               LoginScreenOverride[] filtered = (LoginScreenOverride[])Arrays.stream(LoginScreenOverride.values()).filter((screen) -> {
                  return screen.getFileName() != null;
               }).toArray((x$0) -> {
                  return new LoginScreenOverride[x$0];
               });
               loginScreen = filtered[(new Random()).nextInt(filtered.length)];
            }

            this.fetchLoginScreenImage(loginScreen, (imagex) -> {
               this.clientThread.invoke(() -> {
                  SpritePixels pixels = ImageUtil.getImageSpritePixels(imagex, this.client);
                  this.client.setLoginScreen(pixels);
               });
            });
         }

      }
   }

   private void restoreLoginScreen() {
      this.client.setLoginScreen((SpritePixels)null);
   }

   private void fetchLoginScreenImage(final LoginScreenOverride ls, final Consumer<BufferedImage> imageConsumer) {
      final File imagePath = new File(LOGINSCREENS, ls.getFileName());

      try {
         if (imagePath.exists()) {
            String hash = Files.asByteSource(imagePath).hash(Hashing.sha256()).toString();
            if (hash.equals(ls.getHash())) {
               InputStream in = Files.asByteSource(imagePath).openStream();

               BufferedImage image;
               try {
                  Class var7 = ImageIO.class;
                  synchronized(ImageIO.class) {
                     image = ImageIO.read(in);
                  }
               } catch (Throwable var11) {
                  if (in != null) {
                     try {
                        in.close();
                     } catch (Throwable var9) {
                        var11.addSuppressed(var9);
                     }
                  }

                  throw var11;
               }

               if (in != null) {
                  in.close();
               }

               log.debug("Using cached login screen {}", ls.getFileName());
               imageConsumer.accept(image);
               return;
            }
         }
      } catch (IOException var12) {
         IOException ex = var12;
         log.debug((String)null, ex);
      }

      log.info("Downloading login screen {}", ls.getFileName());
      HttpUrl url = this.staticBase.newBuilder().addPathSegments("loginscreens/" + ls.getFileName()).build();
      Request request = (new Request.Builder()).url(url).build();
      this.okHttpClient.newCall(request).enqueue(new Callback() {
         public void onResponse(Call call, Response response) throws IOException {
            LoginScreenPlugin.LOGINSCREENS.mkdirs();
            Response var3 = response;

            InputStream in;
            try {
               in = response.body().byteStream();

               try {
                  Files.asByteSink(imagePath, new FileWriteMode[0]).writeFrom(in);
               } catch (Throwable var13) {
                  if (in != null) {
                     try {
                        in.close();
                     } catch (Throwable var10) {
                        var13.addSuppressed(var10);
                     }
                  }

                  throw var13;
               }

               if (in != null) {
                  in.close();
               }
            } catch (Throwable var14) {
               if (response != null) {
                  try {
                     var3.close();
                  } catch (Throwable var9) {
                     var14.addSuppressed(var9);
                  }
               }

               throw var14;
            }

            if (response != null) {
               response.close();
            }

            in = Files.asByteSource(imagePath).openStream();

            BufferedImage image;
            try {
               Class var5 = ImageIO.class;
               synchronized(ImageIO.class) {
                  image = ImageIO.read(in);
               }
            } catch (Throwable var12) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var8) {
                     var12.addSuppressed(var8);
                  }
               }

               throw var12;
            }

            if (in != null) {
               in.close();
            }

            imageConsumer.accept(image);
         }

         public void onFailure(Call call, IOException e) {
            LoginScreenPlugin.log.error("unable to download login screen {}", ls, e);
         }
      });
   }

   static {
      CUSTOM_LOGIN_SCREEN_FILE = new File(RuneLite.RUNELITE_DIR, "login.png");
      LOGINSCREENS = new File(RuneLite.CACHE_DIR, "loginscreens");
   }
}
