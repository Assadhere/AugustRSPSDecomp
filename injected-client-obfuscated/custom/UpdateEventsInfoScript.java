package custom;

import java.util.Arrays;
import javax.annotation.Nullable;

public class UpdateEventsInfoScript {
   private final EventPanelUpdateMessage[] messages;

   public UpdateEventsInfoScript(EventPanelUpdateMessage[] var1) {
      this.messages = var1;
   }

   public EventPanelUpdateMessage[] getMessages() {
      return this.messages;
   }

   public String toString() {
      return "UpdateEventsInfoScript(messages=" + Arrays.deepToString(this.getMessages()) + ")";
   }

   public static class EventProgressBar {
      private final int progress;
      private final int total;

      public EventProgressBar(int var1, int var2) {
         this.progress = var1;
         this.total = var2;
      }

      public int getProgress() {
         return this.progress;
      }

      public int getTotal() {
         return this.total;
      }

      public String toString() {
         int var10000 = this.getProgress();
         return "UpdateEventsInfoScript.EventProgressBar(progress=" + var10000 + ", total=" + this.getTotal() + ")";
      }
   }

   public static class EventPanelButton {
      private final int interfaceHashToSend;
      private final String buttonText;

      public EventPanelButton(int var1, String var2) {
         this.interfaceHashToSend = var1;
         this.buttonText = var2;
      }

      public int getInterfaceHashToSend() {
         return this.interfaceHashToSend;
      }

      public String getButtonText() {
         return this.buttonText;
      }

      public String toString() {
         int var10000 = this.getInterfaceHashToSend();
         return "UpdateEventsInfoScript.EventPanelButton(interfaceHashToSend=" + var10000 + ", buttonText=" + this.getButtonText() + ")";
      }
   }

   public static class EventPanel {
      private final String id;
      private final int ticksRemaining;
      private final String name;
      private final String description;
      private final int spriteArchive;
      private final int spriteFile;
      @Nullable
      private final EventPanelButton button;
      @Nullable
      private final EventProgressBar progress;

      public EventPanel(String var1, int var2, String var3, String var4, int var5, int var6, @Nullable EventPanelButton var7, @Nullable EventProgressBar var8) {
         this.id = var1;
         this.ticksRemaining = var2;
         this.name = var3;
         this.description = var4;
         this.spriteArchive = var5;
         this.spriteFile = var6;
         this.button = var7;
         this.progress = var8;
      }

      public String getId() {
         return this.id;
      }

      public int getTicksRemaining() {
         return this.ticksRemaining;
      }

      public String getName() {
         return this.name;
      }

      public String getDescription() {
         return this.description;
      }

      public int getSpriteArchive() {
         return this.spriteArchive;
      }

      public int getSpriteFile() {
         return this.spriteFile;
      }

      @Nullable
      public EventPanelButton getButton() {
         return this.button;
      }

      @Nullable
      public EventProgressBar getProgress() {
         return this.progress;
      }

      public String toString() {
         String var10000 = this.getId();
         return "UpdateEventsInfoScript.EventPanel(id=" + var10000 + ", ticksRemaining=" + this.getTicksRemaining() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", spriteArchive=" + this.getSpriteArchive() + ", spriteFile=" + this.getSpriteFile() + ", button=" + String.valueOf(this.getButton()) + ", progress=" + String.valueOf(this.getProgress()) + ")";
      }
   }

   public static class EventPanelUpdateMessage {
      private final int renderOrder;
      private final int worldId;
      private final boolean add;
      private final String id;
      private final EventPanel panel;

      public EventPanelUpdateMessage(int var1, int var2, boolean var3, String var4, EventPanel var5) {
         this.renderOrder = var1;
         this.worldId = var2;
         this.add = var3;
         this.id = var4;
         this.panel = var5;
      }

      public int getRenderOrder() {
         return this.renderOrder;
      }

      public int getWorldId() {
         return this.worldId;
      }

      public boolean isAdd() {
         return this.add;
      }

      public String getId() {
         return this.id;
      }

      public EventPanel getPanel() {
         return this.panel;
      }

      public String toString() {
         int var10000 = this.getRenderOrder();
         return "UpdateEventsInfoScript.EventPanelUpdateMessage(renderOrder=" + var10000 + ", worldId=" + this.getWorldId() + ", add=" + this.isAdd() + ", id=" + this.getId() + ", panel=" + String.valueOf(this.getPanel()) + ")";
      }
   }
}
