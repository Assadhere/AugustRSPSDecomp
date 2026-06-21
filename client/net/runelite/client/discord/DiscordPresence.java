package net.runelite.client.discord;

import java.time.Instant;

public final class DiscordPresence {
   private final String state;
   private final String details;
   private final Instant startTimestamp;
   private final Instant endTimestamp;
   private final String largeImageKey;
   private final String largeImageText;
   private final String smallImageKey;
   private final String smallImageText;
   private final String partyId;
   private final int partySize;
   private final int partyMax;
   private final String matchSecret;
   private final String joinSecret;
   private final String spectateSecret;
   private final boolean instance;

   DiscordPresence(String state, String details, Instant startTimestamp, Instant endTimestamp, String largeImageKey, String largeImageText, String smallImageKey, String smallImageText, String partyId, int partySize, int partyMax, String matchSecret, String joinSecret, String spectateSecret, boolean instance) {
      this.state = state;
      this.details = details;
      this.startTimestamp = startTimestamp;
      this.endTimestamp = endTimestamp;
      this.largeImageKey = largeImageKey;
      this.largeImageText = largeImageText;
      this.smallImageKey = smallImageKey;
      this.smallImageText = smallImageText;
      this.partyId = partyId;
      this.partySize = partySize;
      this.partyMax = partyMax;
      this.matchSecret = matchSecret;
      this.joinSecret = joinSecret;
      this.spectateSecret = spectateSecret;
      this.instance = instance;
   }

   public static DiscordPresenceBuilder builder() {
      return new DiscordPresenceBuilder();
   }

   public DiscordPresenceBuilder toBuilder() {
      return (new DiscordPresenceBuilder()).state(this.state).details(this.details).startTimestamp(this.startTimestamp).endTimestamp(this.endTimestamp).largeImageKey(this.largeImageKey).largeImageText(this.largeImageText).smallImageKey(this.smallImageKey).smallImageText(this.smallImageText).partyId(this.partyId).partySize(this.partySize).partyMax(this.partyMax).matchSecret(this.matchSecret).joinSecret(this.joinSecret).spectateSecret(this.spectateSecret).instance(this.instance);
   }

   public String getState() {
      return this.state;
   }

   public String getDetails() {
      return this.details;
   }

   public Instant getStartTimestamp() {
      return this.startTimestamp;
   }

   public Instant getEndTimestamp() {
      return this.endTimestamp;
   }

   public String getLargeImageKey() {
      return this.largeImageKey;
   }

   public String getLargeImageText() {
      return this.largeImageText;
   }

   public String getSmallImageKey() {
      return this.smallImageKey;
   }

   public String getSmallImageText() {
      return this.smallImageText;
   }

   public String getPartyId() {
      return this.partyId;
   }

   public int getPartySize() {
      return this.partySize;
   }

   public int getPartyMax() {
      return this.partyMax;
   }

   public String getMatchSecret() {
      return this.matchSecret;
   }

   public String getJoinSecret() {
      return this.joinSecret;
   }

   public String getSpectateSecret() {
      return this.spectateSecret;
   }

   public boolean isInstance() {
      return this.instance;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DiscordPresence)) {
         return false;
      } else {
         DiscordPresence other = (DiscordPresence)o;
         if (this.getPartySize() != other.getPartySize()) {
            return false;
         } else if (this.getPartyMax() != other.getPartyMax()) {
            return false;
         } else if (this.isInstance() != other.isInstance()) {
            return false;
         } else {
            label160: {
               Object this$state = this.getState();
               Object other$state = other.getState();
               if (this$state == null) {
                  if (other$state == null) {
                     break label160;
                  }
               } else if (this$state.equals(other$state)) {
                  break label160;
               }

               return false;
            }

            label153: {
               Object this$details = this.getDetails();
               Object other$details = other.getDetails();
               if (this$details == null) {
                  if (other$details == null) {
                     break label153;
                  }
               } else if (this$details.equals(other$details)) {
                  break label153;
               }

               return false;
            }

            Object this$startTimestamp = this.getStartTimestamp();
            Object other$startTimestamp = other.getStartTimestamp();
            if (this$startTimestamp == null) {
               if (other$startTimestamp != null) {
                  return false;
               }
            } else if (!this$startTimestamp.equals(other$startTimestamp)) {
               return false;
            }

            label139: {
               Object this$endTimestamp = this.getEndTimestamp();
               Object other$endTimestamp = other.getEndTimestamp();
               if (this$endTimestamp == null) {
                  if (other$endTimestamp == null) {
                     break label139;
                  }
               } else if (this$endTimestamp.equals(other$endTimestamp)) {
                  break label139;
               }

               return false;
            }

            Object this$largeImageKey = this.getLargeImageKey();
            Object other$largeImageKey = other.getLargeImageKey();
            if (this$largeImageKey == null) {
               if (other$largeImageKey != null) {
                  return false;
               }
            } else if (!this$largeImageKey.equals(other$largeImageKey)) {
               return false;
            }

            label125: {
               Object this$largeImageText = this.getLargeImageText();
               Object other$largeImageText = other.getLargeImageText();
               if (this$largeImageText == null) {
                  if (other$largeImageText == null) {
                     break label125;
                  }
               } else if (this$largeImageText.equals(other$largeImageText)) {
                  break label125;
               }

               return false;
            }

            label118: {
               Object this$smallImageKey = this.getSmallImageKey();
               Object other$smallImageKey = other.getSmallImageKey();
               if (this$smallImageKey == null) {
                  if (other$smallImageKey == null) {
                     break label118;
                  }
               } else if (this$smallImageKey.equals(other$smallImageKey)) {
                  break label118;
               }

               return false;
            }

            Object this$smallImageText = this.getSmallImageText();
            Object other$smallImageText = other.getSmallImageText();
            if (this$smallImageText == null) {
               if (other$smallImageText != null) {
                  return false;
               }
            } else if (!this$smallImageText.equals(other$smallImageText)) {
               return false;
            }

            label104: {
               Object this$partyId = this.getPartyId();
               Object other$partyId = other.getPartyId();
               if (this$partyId == null) {
                  if (other$partyId == null) {
                     break label104;
                  }
               } else if (this$partyId.equals(other$partyId)) {
                  break label104;
               }

               return false;
            }

            label97: {
               Object this$matchSecret = this.getMatchSecret();
               Object other$matchSecret = other.getMatchSecret();
               if (this$matchSecret == null) {
                  if (other$matchSecret == null) {
                     break label97;
                  }
               } else if (this$matchSecret.equals(other$matchSecret)) {
                  break label97;
               }

               return false;
            }

            Object this$joinSecret = this.getJoinSecret();
            Object other$joinSecret = other.getJoinSecret();
            if (this$joinSecret == null) {
               if (other$joinSecret != null) {
                  return false;
               }
            } else if (!this$joinSecret.equals(other$joinSecret)) {
               return false;
            }

            Object this$spectateSecret = this.getSpectateSecret();
            Object other$spectateSecret = other.getSpectateSecret();
            if (this$spectateSecret == null) {
               if (other$spectateSecret != null) {
                  return false;
               }
            } else if (!this$spectateSecret.equals(other$spectateSecret)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getPartySize();
      result = result * 59 + this.getPartyMax();
      result = result * 59 + (this.isInstance() ? 79 : 97);
      Object $state = this.getState();
      result = result * 59 + ($state == null ? 43 : $state.hashCode());
      Object $details = this.getDetails();
      result = result * 59 + ($details == null ? 43 : $details.hashCode());
      Object $startTimestamp = this.getStartTimestamp();
      result = result * 59 + ($startTimestamp == null ? 43 : $startTimestamp.hashCode());
      Object $endTimestamp = this.getEndTimestamp();
      result = result * 59 + ($endTimestamp == null ? 43 : $endTimestamp.hashCode());
      Object $largeImageKey = this.getLargeImageKey();
      result = result * 59 + ($largeImageKey == null ? 43 : $largeImageKey.hashCode());
      Object $largeImageText = this.getLargeImageText();
      result = result * 59 + ($largeImageText == null ? 43 : $largeImageText.hashCode());
      Object $smallImageKey = this.getSmallImageKey();
      result = result * 59 + ($smallImageKey == null ? 43 : $smallImageKey.hashCode());
      Object $smallImageText = this.getSmallImageText();
      result = result * 59 + ($smallImageText == null ? 43 : $smallImageText.hashCode());
      Object $partyId = this.getPartyId();
      result = result * 59 + ($partyId == null ? 43 : $partyId.hashCode());
      Object $matchSecret = this.getMatchSecret();
      result = result * 59 + ($matchSecret == null ? 43 : $matchSecret.hashCode());
      Object $joinSecret = this.getJoinSecret();
      result = result * 59 + ($joinSecret == null ? 43 : $joinSecret.hashCode());
      Object $spectateSecret = this.getSpectateSecret();
      result = result * 59 + ($spectateSecret == null ? 43 : $spectateSecret.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getState();
      return "DiscordPresence(state=" + var10000 + ", details=" + this.getDetails() + ", startTimestamp=" + String.valueOf(this.getStartTimestamp()) + ", endTimestamp=" + String.valueOf(this.getEndTimestamp()) + ", largeImageKey=" + this.getLargeImageKey() + ", largeImageText=" + this.getLargeImageText() + ", smallImageKey=" + this.getSmallImageKey() + ", smallImageText=" + this.getSmallImageText() + ", partyId=" + this.getPartyId() + ", partySize=" + this.getPartySize() + ", partyMax=" + this.getPartyMax() + ", matchSecret=" + this.getMatchSecret() + ", joinSecret=" + this.getJoinSecret() + ", spectateSecret=" + this.getSpectateSecret() + ", instance=" + this.isInstance() + ")";
   }

   public static class DiscordPresenceBuilder {
      private String state;
      private String details;
      private Instant startTimestamp;
      private Instant endTimestamp;
      private String largeImageKey;
      private String largeImageText;
      private String smallImageKey;
      private String smallImageText;
      private String partyId;
      private int partySize;
      private int partyMax;
      private String matchSecret;
      private String joinSecret;
      private String spectateSecret;
      private boolean instance;

      DiscordPresenceBuilder() {
      }

      public DiscordPresenceBuilder state(String state) {
         this.state = state;
         return this;
      }

      public DiscordPresenceBuilder details(String details) {
         this.details = details;
         return this;
      }

      public DiscordPresenceBuilder startTimestamp(Instant startTimestamp) {
         this.startTimestamp = startTimestamp;
         return this;
      }

      public DiscordPresenceBuilder endTimestamp(Instant endTimestamp) {
         this.endTimestamp = endTimestamp;
         return this;
      }

      public DiscordPresenceBuilder largeImageKey(String largeImageKey) {
         this.largeImageKey = largeImageKey;
         return this;
      }

      public DiscordPresenceBuilder largeImageText(String largeImageText) {
         this.largeImageText = largeImageText;
         return this;
      }

      public DiscordPresenceBuilder smallImageKey(String smallImageKey) {
         this.smallImageKey = smallImageKey;
         return this;
      }

      public DiscordPresenceBuilder smallImageText(String smallImageText) {
         this.smallImageText = smallImageText;
         return this;
      }

      public DiscordPresenceBuilder partyId(String partyId) {
         this.partyId = partyId;
         return this;
      }

      public DiscordPresenceBuilder partySize(int partySize) {
         this.partySize = partySize;
         return this;
      }

      public DiscordPresenceBuilder partyMax(int partyMax) {
         this.partyMax = partyMax;
         return this;
      }

      public DiscordPresenceBuilder matchSecret(String matchSecret) {
         this.matchSecret = matchSecret;
         return this;
      }

      public DiscordPresenceBuilder joinSecret(String joinSecret) {
         this.joinSecret = joinSecret;
         return this;
      }

      public DiscordPresenceBuilder spectateSecret(String spectateSecret) {
         this.spectateSecret = spectateSecret;
         return this;
      }

      public DiscordPresenceBuilder instance(boolean instance) {
         this.instance = instance;
         return this;
      }

      public DiscordPresence build() {
         return new DiscordPresence(this.state, this.details, this.startTimestamp, this.endTimestamp, this.largeImageKey, this.largeImageText, this.smallImageKey, this.smallImageText, this.partyId, this.partySize, this.partyMax, this.matchSecret, this.joinSecret, this.spectateSecret, this.instance);
      }

      public String toString() {
         String var10000 = this.state;
         return "DiscordPresence.DiscordPresenceBuilder(state=" + var10000 + ", details=" + this.details + ", startTimestamp=" + String.valueOf(this.startTimestamp) + ", endTimestamp=" + String.valueOf(this.endTimestamp) + ", largeImageKey=" + this.largeImageKey + ", largeImageText=" + this.largeImageText + ", smallImageKey=" + this.smallImageKey + ", smallImageText=" + this.smallImageText + ", partyId=" + this.partyId + ", partySize=" + this.partySize + ", partyMax=" + this.partyMax + ", matchSecret=" + this.matchSecret + ", joinSecret=" + this.joinSecret + ", spectateSecret=" + this.spectateSecret + ", instance=" + this.instance + ")";
      }
   }
}
