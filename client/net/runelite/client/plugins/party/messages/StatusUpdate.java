package net.runelite.client.plugins.party.messages;

import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import net.runelite.client.party.messages.PartyMemberMessage;

public class StatusUpdate extends PartyMemberMessage {
   @SerializedName("n")
   private String characterName = null;
   @SerializedName("hc")
   private Integer healthCurrent = null;
   @SerializedName("hm")
   private Integer healthMax = null;
   @SerializedName("pc")
   private Integer prayerCurrent = null;
   @SerializedName("pm")
   private Integer prayerMax = null;
   @SerializedName("r")
   private Integer runEnergy = null;
   @SerializedName("s")
   private Integer specEnergy = null;
   @SerializedName("v")
   private Boolean vengeanceActive = null;
   @SerializedName("c")
   private Color memberColor = null;

   public String getCharacterName() {
      return this.characterName;
   }

   public Integer getHealthCurrent() {
      return this.healthCurrent;
   }

   public Integer getHealthMax() {
      return this.healthMax;
   }

   public Integer getPrayerCurrent() {
      return this.prayerCurrent;
   }

   public Integer getPrayerMax() {
      return this.prayerMax;
   }

   public Integer getRunEnergy() {
      return this.runEnergy;
   }

   public Integer getSpecEnergy() {
      return this.specEnergy;
   }

   public Boolean getVengeanceActive() {
      return this.vengeanceActive;
   }

   public Color getMemberColor() {
      return this.memberColor;
   }

   public void setCharacterName(String characterName) {
      this.characterName = characterName;
   }

   public void setHealthCurrent(Integer healthCurrent) {
      this.healthCurrent = healthCurrent;
   }

   public void setHealthMax(Integer healthMax) {
      this.healthMax = healthMax;
   }

   public void setPrayerCurrent(Integer prayerCurrent) {
      this.prayerCurrent = prayerCurrent;
   }

   public void setPrayerMax(Integer prayerMax) {
      this.prayerMax = prayerMax;
   }

   public void setRunEnergy(Integer runEnergy) {
      this.runEnergy = runEnergy;
   }

   public void setSpecEnergy(Integer specEnergy) {
      this.specEnergy = specEnergy;
   }

   public void setVengeanceActive(Boolean vengeanceActive) {
      this.vengeanceActive = vengeanceActive;
   }

   public void setMemberColor(Color memberColor) {
      this.memberColor = memberColor;
   }

   public String toString() {
      String var10000 = this.getCharacterName();
      return "StatusUpdate(characterName=" + var10000 + ", healthCurrent=" + this.getHealthCurrent() + ", healthMax=" + this.getHealthMax() + ", prayerCurrent=" + this.getPrayerCurrent() + ", prayerMax=" + this.getPrayerMax() + ", runEnergy=" + this.getRunEnergy() + ", specEnergy=" + this.getSpecEnergy() + ", vengeanceActive=" + this.getVengeanceActive() + ", memberColor=" + String.valueOf(this.getMemberColor()) + ")";
   }

   public StatusUpdate() {
   }

   public StatusUpdate(String characterName, Integer healthCurrent, Integer healthMax, Integer prayerCurrent, Integer prayerMax, Integer runEnergy, Integer specEnergy, Boolean vengeanceActive, Color memberColor) {
      this.characterName = characterName;
      this.healthCurrent = healthCurrent;
      this.healthMax = healthMax;
      this.prayerCurrent = prayerCurrent;
      this.prayerMax = prayerMax;
      this.runEnergy = runEnergy;
      this.specEnergy = specEnergy;
      this.vengeanceActive = vengeanceActive;
      this.memberColor = memberColor;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof StatusUpdate)) {
         return false;
      } else {
         StatusUpdate other = (StatusUpdate)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else {
            label121: {
               Object this$healthCurrent = this.getHealthCurrent();
               Object other$healthCurrent = other.getHealthCurrent();
               if (this$healthCurrent == null) {
                  if (other$healthCurrent == null) {
                     break label121;
                  }
               } else if (this$healthCurrent.equals(other$healthCurrent)) {
                  break label121;
               }

               return false;
            }

            Object this$healthMax = this.getHealthMax();
            Object other$healthMax = other.getHealthMax();
            if (this$healthMax == null) {
               if (other$healthMax != null) {
                  return false;
               }
            } else if (!this$healthMax.equals(other$healthMax)) {
               return false;
            }

            label107: {
               Object this$prayerCurrent = this.getPrayerCurrent();
               Object other$prayerCurrent = other.getPrayerCurrent();
               if (this$prayerCurrent == null) {
                  if (other$prayerCurrent == null) {
                     break label107;
                  }
               } else if (this$prayerCurrent.equals(other$prayerCurrent)) {
                  break label107;
               }

               return false;
            }

            Object this$prayerMax = this.getPrayerMax();
            Object other$prayerMax = other.getPrayerMax();
            if (this$prayerMax == null) {
               if (other$prayerMax != null) {
                  return false;
               }
            } else if (!this$prayerMax.equals(other$prayerMax)) {
               return false;
            }

            Object this$runEnergy = this.getRunEnergy();
            Object other$runEnergy = other.getRunEnergy();
            if (this$runEnergy == null) {
               if (other$runEnergy != null) {
                  return false;
               }
            } else if (!this$runEnergy.equals(other$runEnergy)) {
               return false;
            }

            label86: {
               Object this$specEnergy = this.getSpecEnergy();
               Object other$specEnergy = other.getSpecEnergy();
               if (this$specEnergy == null) {
                  if (other$specEnergy == null) {
                     break label86;
                  }
               } else if (this$specEnergy.equals(other$specEnergy)) {
                  break label86;
               }

               return false;
            }

            label79: {
               Object this$vengeanceActive = this.getVengeanceActive();
               Object other$vengeanceActive = other.getVengeanceActive();
               if (this$vengeanceActive == null) {
                  if (other$vengeanceActive == null) {
                     break label79;
                  }
               } else if (this$vengeanceActive.equals(other$vengeanceActive)) {
                  break label79;
               }

               return false;
            }

            Object this$characterName = this.getCharacterName();
            Object other$characterName = other.getCharacterName();
            if (this$characterName == null) {
               if (other$characterName != null) {
                  return false;
               }
            } else if (!this$characterName.equals(other$characterName)) {
               return false;
            }

            Object this$memberColor = this.getMemberColor();
            Object other$memberColor = other.getMemberColor();
            if (this$memberColor == null) {
               if (other$memberColor != null) {
                  return false;
               }
            } else if (!this$memberColor.equals(other$memberColor)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof StatusUpdate;
   }

   public int hashCode() {
      int PRIME = true;
      int result = super.hashCode();
      Object $healthCurrent = this.getHealthCurrent();
      result = result * 59 + ($healthCurrent == null ? 43 : $healthCurrent.hashCode());
      Object $healthMax = this.getHealthMax();
      result = result * 59 + ($healthMax == null ? 43 : $healthMax.hashCode());
      Object $prayerCurrent = this.getPrayerCurrent();
      result = result * 59 + ($prayerCurrent == null ? 43 : $prayerCurrent.hashCode());
      Object $prayerMax = this.getPrayerMax();
      result = result * 59 + ($prayerMax == null ? 43 : $prayerMax.hashCode());
      Object $runEnergy = this.getRunEnergy();
      result = result * 59 + ($runEnergy == null ? 43 : $runEnergy.hashCode());
      Object $specEnergy = this.getSpecEnergy();
      result = result * 59 + ($specEnergy == null ? 43 : $specEnergy.hashCode());
      Object $vengeanceActive = this.getVengeanceActive();
      result = result * 59 + ($vengeanceActive == null ? 43 : $vengeanceActive.hashCode());
      Object $characterName = this.getCharacterName();
      result = result * 59 + ($characterName == null ? 43 : $characterName.hashCode());
      Object $memberColor = this.getMemberColor();
      result = result * 59 + ($memberColor == null ? 43 : $memberColor.hashCode());
      return result;
   }
}
