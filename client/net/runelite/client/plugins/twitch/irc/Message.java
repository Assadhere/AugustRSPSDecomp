package net.runelite.client.plugins.twitch.irc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Message {
   private final Map<String, String> tags = new HashMap();
   private String source;
   private String command;
   private String[] arguments;

   public static Message parse(String in) {
      Message message = new Message();
      if (in.startsWith("@")) {
         String[] tags = in.substring(1).split(";");
         String[] var3 = tags;
         int var4 = tags.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String tag = var3[var5];
            int eq = tag.indexOf(61);
            if (eq != -1) {
               String key = tag.substring(0, eq);
               String value = tag.substring(eq + 1).replace("\\:", ";").replace("\\s", " ").replace("\\\\", "\\").replace("\\r", "\r").replace("\\n", "\n");
               message.tags.put(key, value);
            }
         }

         int sp = in.indexOf(32);
         in = in.substring(sp + 1);
      }

      int sp;
      if (in.startsWith(":")) {
         sp = in.indexOf(32);
         message.source = in.substring(1, sp);
         in = in.substring(sp + 1);
      }

      sp = in.indexOf(32);
      if (sp == -1) {
         message.command = in;
         message.arguments = new String[0];
         return message;
      } else {
         message.command = in.substring(0, sp);
         String args = in.substring(sp + 1);
         List<String> argList = new ArrayList();

         do {
            String arg;
            if (args.startsWith(":")) {
               arg = args.substring(1);
               sp = -1;
            } else {
               sp = args.indexOf(32);
               arg = sp == -1 ? args : args.substring(0, sp);
            }

            args = args.substring(sp + 1);
            argList.add(arg);
         } while(sp != -1);

         message.arguments = (String[])argList.toArray(new String[0]);
         return message;
      }
   }

   public Map<String, String> getTags() {
      return this.tags;
   }

   public String getSource() {
      return this.source;
   }

   public String getCommand() {
      return this.command;
   }

   public String[] getArguments() {
      return this.arguments;
   }
}
