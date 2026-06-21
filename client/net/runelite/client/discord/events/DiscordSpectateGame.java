package net.runelite.client.discord.events;

public final class DiscordSpectateGame {
   private final String spectateSecret;

   public DiscordSpectateGame(String spectateSecret) {
      this.spectateSecret = spectateSecret;
   }

   public String getSpectateSecret() {
      return this.spectateSecret;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DiscordSpectateGame)) {
         return false;
      } else {
         DiscordSpectateGame other = (DiscordSpectateGame)o;
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

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $spectateSecret = this.getSpectateSecret();
      result = result * 59 + ($spectateSecret == null ? 43 : $spectateSecret.hashCode());
      return result;
   }

   public String toString() {
      return "DiscordSpectateGame(spectateSecret=" + this.getSpectateSecret() + ")";
   }
}
