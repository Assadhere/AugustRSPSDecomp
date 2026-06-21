package net.runelite.client.discord.events;

public final class DiscordJoinGame {
   private final String joinSecret;

   public DiscordJoinGame(String joinSecret) {
      this.joinSecret = joinSecret;
   }

   public String getJoinSecret() {
      return this.joinSecret;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DiscordJoinGame)) {
         return false;
      } else {
         DiscordJoinGame other = (DiscordJoinGame)o;
         Object this$joinSecret = this.getJoinSecret();
         Object other$joinSecret = other.getJoinSecret();
         if (this$joinSecret == null) {
            if (other$joinSecret != null) {
               return false;
            }
         } else if (!this$joinSecret.equals(other$joinSecret)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $joinSecret = this.getJoinSecret();
      result = result * 59 + ($joinSecret == null ? 43 : $joinSecret.hashCode());
      return result;
   }

   public String toString() {
      return "DiscordJoinGame(joinSecret=" + this.getJoinSecret() + ")";
   }
}
