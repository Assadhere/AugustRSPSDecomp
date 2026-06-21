package net.runelite.api.events;

import net.runelite.api.ScriptEvent;

public class ScriptPreFired {
   private final int scriptId;
   private ScriptEvent scriptEvent;

   public ScriptPreFired(int scriptId) {
      this.scriptId = scriptId;
   }

   public int getScriptId() {
      return this.scriptId;
   }

   public ScriptEvent getScriptEvent() {
      return this.scriptEvent;
   }

   public void setScriptEvent(ScriptEvent scriptEvent) {
      this.scriptEvent = scriptEvent;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ScriptPreFired)) {
         return false;
      } else {
         ScriptPreFired other = (ScriptPreFired)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getScriptId() != other.getScriptId()) {
            return false;
         } else {
            Object this$scriptEvent = this.getScriptEvent();
            Object other$scriptEvent = other.getScriptEvent();
            if (this$scriptEvent == null) {
               if (other$scriptEvent != null) {
                  return false;
               }
            } else if (!this$scriptEvent.equals(other$scriptEvent)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ScriptPreFired;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getScriptId();
      Object $scriptEvent = this.getScriptEvent();
      result = result * 59 + ($scriptEvent == null ? 43 : $scriptEvent.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getScriptId();
      return "ScriptPreFired(scriptId=" + var10000 + ", scriptEvent=" + String.valueOf(this.getScriptEvent()) + ")";
   }
}
