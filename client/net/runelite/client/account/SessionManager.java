package net.runelite.client.account;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.util.LinkBrowser;
import net.runelite.http.api.account.OAuthResponse;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SessionManager {
   private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
   private AccountSession accountSession;
   private final EventBus eventBus;
   private final File sessionFile;
   private final AccountClient accountClient;
   private final Gson gson;
   private final String oauthRedirect;
   private final ScheduledExecutorService scheduledExecutorService;
   private HttpServer server;

   @Inject
   private SessionManager(@Named("sessionfile") File sessionfile, EventBus eventBus, AccountClient accountClient, Gson gson, @Named("runelite.oauth.redirect") String oauthRedirect, ScheduledExecutorService scheduledExecutorService) {
      this.eventBus = eventBus;
      this.sessionFile = sessionfile;
      this.accountClient = accountClient;
      this.gson = gson;
      this.oauthRedirect = oauthRedirect;
      this.scheduledExecutorService = scheduledExecutorService;
      eventBus.register(this);
   }

   public void loadSession() {
      if (!this.sessionFile.exists()) {
         log.info("No session file exists");
      } else {
         AccountSession session;
         try {
            FileInputStream in = new FileInputStream(this.sessionFile);

            try {
               session = (AccountSession)this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), AccountSession.class);
               log.debug("Loaded session for {}", session.getUsername());
            } catch (Throwable var6) {
               try {
                  in.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            in.close();
         } catch (Exception var7) {
            Exception ex = var7;
            log.warn("Unable to load session file", ex);
            return;
         }

         this.accountClient.setUuid(session.getUuid());
         if (!this.accountClient.sessionCheck()) {
            log.debug("Loaded session {} is invalid", session.getUuid());
         } else {
            this.accountSession = session;
         }
      }
   }

   private void saveSession() {
      if (this.accountSession != null) {
         try {
            Writer fw = new OutputStreamWriter(Files.newOutputStream(this.sessionFile.toPath()), StandardCharsets.UTF_8);

            try {
               this.gson.toJson(this.accountSession, fw);
               log.debug("Saved session to {}", this.sessionFile);
            } catch (Throwable var5) {
               try {
                  ((Writer)fw).close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            ((Writer)fw).close();
         } catch (IOException var6) {
            IOException ex = var6;
            log.warn("Unable to save session file", ex);
         }

      }
   }

   private void deleteSession() {
      this.sessionFile.delete();
   }

   private void openSession(AccountSession session) {
      this.accountSession = session;
      this.eventBus.post(new SessionOpen());
   }

   private void closeSession() {
      if (this.accountSession != null) {
         log.debug("Logging out of account {}", this.accountSession.getUsername());
         this.accountClient.setUuid(this.accountSession.getUuid());

         try {
            this.accountClient.logout();
         } catch (IOException var2) {
            IOException ex = var2;
            log.warn("Unable to sign out of session", ex);
         }

         this.accountSession = null;
         this.eventBus.post(new SessionClose());
      }
   }

   public void login() {
      if (this.server == null) {
         try {
            this.startServer();
         } catch (IOException var4) {
            IOException ex = var4;
            log.error("Unable to start http server", ex);
            return;
         }
      }

      OAuthResponse login;
      try {
         login = this.accountClient.login(this.server.getAddress().getPort());
      } catch (IOException var3) {
         IOException ex = var3;
         log.error("Unable to get oauth url", ex);
         return;
      }

      LinkBrowser.browse(login.getOauthUrl());
   }

   public void logout() {
      this.closeSession();
      this.deleteSession();
   }

   private void startServer() throws IOException {
      this.server = HttpServer.create(new InetSocketAddress("localhost", 0), 1);
      this.server.createContext("/oauth", (req) -> {
         try {
            HttpUrl url = HttpUrl.get("http://localhost" + String.valueOf(req.getRequestURI()));
            String username = url.queryParameter("username");
            UUID sessionId = UUID.fromString(url.queryParameter("sessionId"));
            log.debug("Now signed in as {}", username);
            AccountSession session = new AccountSession(sessionId, Instant.now(), username);
            this.openSession(session);
            this.saveSession();
            HttpUrl redirect = HttpUrl.get(this.oauthRedirect).newBuilder().addQueryParameter("username", username).addQueryParameter("sessionId", sessionId.toString()).build();
            req.getResponseHeaders().set("Location", redirect.toString());
            req.sendResponseHeaders(302, 0L);
         } catch (Exception var10) {
            Exception e = var10;
            log.warn("failure serving oauth response", e);
            req.sendResponseHeaders(400, 0L);
            req.getResponseBody().write(e.getMessage().getBytes(StandardCharsets.UTF_8));
         } finally {
            req.close();
            this.scheduledExecutorService.execute(this::stopServer);
         }

      });
      log.debug("Starting server {}", this.server);
      this.server.start();
   }

   private void stopServer() {
      log.debug("Stopping server {}", this.server);
      this.server.stop(0);
      this.server = null;
   }

   public AccountSession getAccountSession() {
      return this.accountSession;
   }
}
