package net.runelite.api.events;

import net.runelite.api.Script;

public class ScriptCallbackEvent {
   private Script script;
   private String eventName;

   public Script getScript() {
      return this.script;
   }

   public String getEventName() {
      return this.eventName;
   }

   public void setScript(Script script) {
      this.script = script;
   }

   public void setEventName(String eventName) {
      this.eventName = eventName;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ScriptCallbackEvent)) {
         return false;
      } else {
         ScriptCallbackEvent other = (ScriptCallbackEvent)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$script = this.getScript();
            Object other$script = other.getScript();
            if (this$script == null) {
               if (other$script != null) {
                  return false;
               }
            } else if (!this$script.equals(other$script)) {
               return false;
            }

            Object this$eventName = this.getEventName();
            Object other$eventName = other.getEventName();
            if (this$eventName == null) {
               if (other$eventName != null) {
                  return false;
               }
            } else if (!this$eventName.equals(other$eventName)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ScriptCallbackEvent;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $script = this.getScript();
      result = result * 59 + ($script == null ? 43 : $script.hashCode());
      Object $eventName = this.getEventName();
      result = result * 59 + ($eventName == null ? 43 : $eventName.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getScript());
      return "ScriptCallbackEvent(script=" + var10000 + ", eventName=" + this.getEventName() + ")";
   }
}
