package net.runelite.client.plugins.banktags.tabs;

import java.util.Arrays;
import lombok.NonNull;

public class Layout {
   private final String tag;
   private int[] layout;

   public Layout(String tag) {
      this.tag = tag;
      this.layout = new int[0];
   }

   public Layout(String tag, @NonNull int[] layout) {
      if (layout == null) {
         throw new NullPointerException("layout is marked non-null but is null");
      } else {
         this.tag = tag;
         this.layout = layout;
      }
   }

   public Layout(Layout other) {
      this.tag = other.tag;
      this.layout = (int[])other.layout.clone();
   }

   public int[] getLayout() {
      return (int[])this.layout.clone();
   }

   public int getItemAtPos(int pos) {
      return pos >= 0 && pos < this.layout.length ? this.layout[pos] : -1;
   }

   public void setItemAtPos(int itemId, int pos) {
      if (pos >= 0) {
         if (this.layout == null) {
            this.layout = new int[pos + 1];
            Arrays.fill(this.layout, -1);
         } else if (pos >= this.layout.length) {
            int[] n = Arrays.copyOf(this.layout, pos + 1);
            Arrays.fill(n, this.layout.length, n.length, -1);
            this.layout = n;
         }

         this.layout[pos] = itemId;
      }
   }

   public void addItem(int itemId) {
      this.addItemAfter(itemId, 0);
   }

   public void addItemAfter(int itemId, int pos) {
      int i;
      for(i = pos; i < this.layout.length; ++i) {
         if (this.layout[i] == -1) {
            this.layout[i] = itemId;
            return;
         }
      }

      this.resize(Math.max(pos + 1, this.layout.length + 1));
      this.layout[i] = itemId;
   }

   public void removeItem(int itemId) {
      for(int i = 0; i < this.layout.length; ++i) {
         if (this.layout[i] == itemId) {
            this.layout[i] = -1;
         }
      }

   }

   public void removeItemAtPos(int pos) {
      if (pos >= 0 && pos < this.layout.length) {
         this.layout[pos] = -1;
      }
   }

   void swap(int sidx, int tidx) {
      int sid = this.layout[sidx];
      this.layout[sidx] = this.layout[tidx];
      this.layout[tidx] = sid;
   }

   void insert(int sidx, int tidx) {
      int sid = this.layout[sidx];
      int i;
      if (sidx < tidx) {
         for(i = tidx; i > sidx && this.layout[i] > -1; --i) {
         }

         this.layout[sidx] = -1;
         System.arraycopy(this.layout, i + 1, this.layout, i, tidx - i);
         this.layout[tidx] = sid;
      } else if (sidx > tidx) {
         for(i = tidx; i < sidx && this.layout[i] > -1; ++i) {
         }

         this.layout[sidx] = -1;
         System.arraycopy(this.layout, tidx, this.layout, tidx + 1, i - tidx);
         this.layout[tidx] = sid;
      }

   }

   public int count(int itemId) {
      int c = 0;
      int[] var3 = this.layout;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int value = var3[var5];
         if (value == itemId) {
            ++c;
         }
      }

      return c;
   }

   public int size() {
      return this.layout.length;
   }

   public void resize(int size) {
      int[] n = Arrays.copyOf(this.layout, size);
      if (size > this.layout.length) {
         Arrays.fill(n, this.layout.length, size, -1);
      }

      this.layout = n;
   }

   public String getTag() {
      return this.tag;
   }
}
