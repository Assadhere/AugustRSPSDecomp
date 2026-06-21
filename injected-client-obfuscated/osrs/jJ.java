package osrs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.MenuEntry;
import net.runelite.api.Skill;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.StatChanged;

public class jJ {
   public int a = 0;
   public int b = 0;
   public int c = 0;
   public int d = 0;
   public final List e = new ArrayList();
   public int f = 1;
   public final int[] g = new int[32];
   public int h = 0;
   public final int[] i = new int[32];
   public int j = 0;
   public final int[] k = new int[32];
   public int l = 0;
   public final int[] m = new int[32];
   public int n = 0;
   public int o = 0;
   public int p = 0;
   public int q = 0;
   public int r = 0;

   public void a() {
      this.v();
      ++this.f;
   }

   public void a(int var1) {
      this.g[++this.h - 1 & 31] = var1;
   }

   public void b(int var1) {
      this.h(var1);
      this.k[++this.l - 1 & 31] = var1;
   }

   public void c(int var1) {
      this.j(var1);
      this.i[++this.j - 1 & 31] = var1 & 32767;
   }

   public void d(int var1) {
      this.i(var1);
      this.m[++this.n - 1 & 31] = var1;
   }

   public void b() {
      this.o = this.f;
   }

   public void c() {
      this.q = this.f;
   }

   public void d() {
      this.r = this.f;
   }

   public void e() {
      this.a = this.f;
   }

   public void f() {
      this.b = this.f;
   }

   public void g() {
      this.c = this.f;
   }

   public void h() {
      this.d = this.f;
   }

   public void i() {
      this.h += 32;
   }

   public int j() {
      return this.f;
   }

   public int e(int var1) {
      return this.g[var1 & 31];
   }

   public int k() {
      return this.h;
   }

   public int f(int var1) {
      return this.i[var1 & 31];
   }

   public int l() {
      return this.j;
   }

   public int g(int var1) {
      return this.k[var1 & 31];
   }

   public int m() {
      return this.l;
   }

   public int n() {
      return this.o;
   }

   public int o() {
      return this.p;
   }

   public int p() {
      return this.q;
   }

   public int q() {
      return this.r;
   }

   public int r() {
      return this.a;
   }

   public int s() {
      return this.b;
   }

   public int t() {
      return this.c;
   }

   public int u() {
      return this.d;
   }

   public void v() {
      // $FF: Couldn't be decompiled
   }

   public void h(int var1) {
      Skill[] var2 = Skill.values();
      if (var1 >= 0 && var1 < var2.length) {
         int[] var3 = Client.s.getSkillExperiences();
         int[] var4 = Client.s.getBoostedSkillLevels();
         int[] var5 = Client.s.getRealSkillLevels();
         StatChanged var6 = new StatChanged(var2[var1], var3[var1], var5[var1], var4[var1]);
         Client.s.getCallbacks().post(var6);
      }

   }

   public void i(int var1) {
      this.l(var1);
   }

   public void j(int var1) {
      int var2 = var1 & 32767;
      fT var3 = Client.s.bE();
      im var4 = (im)var3.b((long)var2);
      im var5 = (im)var3.b((long)(var2 | '耀'));
      ItemContainerChanged var6;
      if (var4 != null) {
         var6 = new ItemContainerChanged(var2, var4);
         this.e.add(var6);
      }

      if (var5 != null) {
         var6 = new ItemContainerChanged(var2 | '耀', var5);
         this.e.add(var6);
      }

   }

   public void k(int var1) {
      this.a(var1);
   }

   public void l(int var1) {
      this.c(var1);
   }

   public void m(int var1) {
      this.b(var1);
   }
}
