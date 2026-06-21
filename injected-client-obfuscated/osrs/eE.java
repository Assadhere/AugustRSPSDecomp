package osrs;

import net.runelite.api.ChatMessageType;
import net.runelite.api.MessageNode;

public class eE extends aA implements MessageNode {
   public int a = (int)(System.currentTimeMillis() / 1000L);
   public gQ b;
   public gQ c;
   public int d;
   public int e;
   public int f;
   public I g;
   public String h;
   public String i;
   public String j;
   public String k;

   public eE(int var1, String var2, String var3, String var4) {
      this.b = gQ.a;
      this.c = gQ.a;
      this.a(var1, var2, var3, var4);
   }

   public void a(int var1, String var2, String var3, String var4) {
      this.f = iw.a();
      this.d = Client.x;
      this.e = var1;
      this.k = var2;
      this.g();
      this.i = var3;
      this.j = var4;
      this.a();
      this.d();
      this.b(var1, var2, var3, var4);
   }

   public void a() {
      this.b = gQ.a;
   }

   public final boolean b() {
      if (gQ.a == this.b) {
         this.c();
      }

      return gQ.b == this.b;
   }

   public void c() {
      this.b = bn.l.c.a(this.g) ? gQ.b : gQ.c;
   }

   public void d() {
      this.c = gQ.a;
   }

   public final boolean e() {
      if (gQ.a == this.c) {
         this.f();
      }

      return gQ.b == this.c;
   }

   public void f() {
      this.c = bn.l.d.a(this.g) ? gQ.b : gQ.c;
   }

   public final void g() {
      if (this.k != null) {
         this.g = new I(Client.f(this.k), bo.cp);
      } else {
         this.g = null;
      }

   }

   public String getRuneLiteFormatMessage() {
      return this.h;
   }

   public void setRuneLiteFormatMessage(String var1) {
      this.h = var1;
   }

   public void setName(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.k = var1;
         int var2 = var1.lastIndexOf(62);
         if (var2 != -1) {
            var1 = var1.substring(var2 + 1);
         }

         this.g = new I(var1, bo.cp);
      }
   }

   public void setValue(String var1) {
      ChatMessageType var2 = ChatMessageType.of(this.e);
      if ((ChatMessageType.CLAN_CHAT == var2 || ChatMessageType.CLAN_MESSAGE == var2) && this.j != null && this.j.startsWith("|")) {
         this.j = "|" + var1;
      } else {
         this.j = var1;
      }

   }

   public String getValue() {
      if (this.j != null && this.j.startsWith("|")) {
         ChatMessageType var1 = ChatMessageType.of(this.e);
         if (ChatMessageType.CLAN_CHAT == var1 || ChatMessageType.CLAN_MESSAGE == var1) {
            return this.j.substring(1);
         }
      }

      return this.j;
   }

   public void setTimestamp(int var1) {
      this.a = var1;
   }

   public ChatMessageType getType() {
      ChatMessageType var1 = ChatMessageType.of(this.e);
      if (ChatMessageType.CLAN_CHAT == var1) {
         if (this.j != null && this.j.startsWith("|")) {
            return ChatMessageType.CLAN_GIM_CHAT;
         }
      } else if (ChatMessageType.CLAN_MESSAGE == var1 && this.j != null && this.j.startsWith("|")) {
         return ChatMessageType.CLAN_GIM_MESSAGE;
      }

      return var1;
   }

   public void b(int var1, String var2, String var3, String var4) {
      this.h = null;
      this.a = (int)(System.currentTimeMillis() / 1000L);
      if (var2 != null) {
         int var5 = var2.lastIndexOf(62);
         if (var5 != -1) {
            this.g = new I(var2.substring(var5 + 1), bo.cp);
         }
      }

   }

   public int getTimestamp() {
      return this.a;
   }

   public String getSender() {
      return this.i;
   }

   public int getId() {
      return this.f;
   }

   public void setSender(String var1) {
      this.i = var1;
   }

   public String getName() {
      return this.k;
   }
}
