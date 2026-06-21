package net.runelite.client.plugins.twitch.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchIRCClient extends Thread implements AutoCloseable {
   private static final Logger log = LoggerFactory.getLogger(TwitchIRCClient.class);
   private static final String HOST = "irc.chat.twitch.tv";
   private static final int PORT = 6697;
   private static final int READ_TIMEOUT = 60000;
   private static final int PING_TIMEOUT = 20000;
   private final TwitchListener twitchListener;
   private final String username;
   private final String password;
   private final String channel;
   private Socket socket;
   private BufferedReader in;
   private Writer out;
   private long last;
   private boolean pingSent;

   public TwitchIRCClient(TwitchListener twitchListener, String username, String password, String channel) {
      this.setName("Twitch");
      this.twitchListener = twitchListener;
      this.username = username;
      this.password = password;
      this.channel = channel;
   }

   public void close() {
      try {
         if (this.socket != null) {
            this.socket.close();
         }
      } catch (IOException var2) {
         IOException ex = var2;
         log.warn("error closing socket", ex);
      }

      this.in = null;
      this.out = null;
   }

   public void run() {
      IOException ex;
      try {
         SocketFactory socketFactory = SSLSocketFactory.getDefault();
         this.socket = socketFactory.createSocket("irc.chat.twitch.tv", 6697);
         this.socket.setSoTimeout(60000);
         this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
         this.out = new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8);
      } catch (IOException var14) {
         ex = var14;
         log.warn("unable to setup irc client", ex);
         return;
      }

      try {
         this.register(this.username, this.password);
         this.join(this.channel);

         String line;
         while((line = this.read()) != null) {
            log.debug("<- {}", line);
            this.last = System.currentTimeMillis();
            this.pingSent = false;
            Message message = Message.parse(line);
            switch (message.getCommand()) {
               case "PING":
                  this.send("PONG", message.getArguments()[0]);
                  break;
               case "PRIVMSG":
                  this.twitchListener.privmsg(message.getSource().substring(0, message.getSource().indexOf(33)), message.getTags(), message.getArguments()[1]);
                  break;
               case "ROOMSTATE":
                  this.twitchListener.roomstate(message.getTags());
                  break;
               case "USERNOTICE":
                  this.twitchListener.usernotice(message.getTags(), message.getArguments().length > 0 ? message.getArguments()[0] : null);
            }
         }
      } catch (IOException var15) {
         ex = var15;
         log.debug("error in twitch irc client", ex);
      } finally {
         try {
            this.socket.close();
         } catch (IOException var13) {
            IOException e = var13;
            log.warn((String)null, e);
         }

      }

   }

   public boolean isConnected() {
      return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
   }

   public void pingCheck() {
      if (this.out != null) {
         if (!this.pingSent && System.currentTimeMillis() - this.last >= 20000L) {
            try {
               this.ping("twitch");
               this.pingSent = true;
            } catch (IOException var2) {
               IOException e = var2;
               log.debug("Ping failure, disconnecting.", e);
               this.close();
            }
         } else if (this.pingSent) {
            log.debug("Ping timeout, disconnecting.");
            this.close();
         }

      }
   }

   private void register(String username, String oauth) throws IOException {
      this.send("CAP", "REQ", "twitch.tv/commands twitch.tv/tags");
      this.send("PASS", oauth);
      this.send("USER", "runelite", ".", "irc.chat.twitch.tv", "runelite");
      this.send("NICK", username);
   }

   private void join(String channel) throws IOException {
      this.send("JOIN", channel);
   }

   private void ping(String destination) throws IOException {
      this.send("PING", destination);
   }

   public void privmsg(String message) throws IOException {
      this.send("PRIVMSG", this.channel, message);
   }

   private void send(String command, String... args) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(command);

      for(int i = 0; i < args.length; ++i) {
         stringBuilder.append(' ');
         if (i + 1 == args.length) {
            stringBuilder.append(':');
         }

         stringBuilder.append(args[i]);
      }

      log.debug("-> {}", stringBuilder.toString());
      stringBuilder.append("\r\n");
      this.out.write(stringBuilder.toString());
      this.out.flush();
   }

   private String read() throws IOException {
      String line = this.in.readLine();
      if (line == null) {
         return null;
      } else {
         int len;
         for(len = line.length(); len > 0 && (line.charAt(len - 1) == '\r' || line.charAt(len - 1) == '\n'); --len) {
         }

         return line.substring(0, len);
      }
   }
}
