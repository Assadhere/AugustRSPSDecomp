package net.runelite.client.account;

import java.time.Instant;
import java.util.UUID;

public class AccountSession {
   private final UUID uuid;
   private final Instant created;
   private final String username;

   public AccountSession(UUID uuid, Instant created, String username) {
      this.uuid = uuid;
      this.created = created;
      this.username = username;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public Instant getCreated() {
      return this.created;
   }

   public String getUsername() {
      return this.username;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getUuid());
      return "AccountSession(uuid=" + var10000 + ", created=" + String.valueOf(this.getCreated()) + ", username=" + this.getUsername() + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AccountSession)) {
         return false;
      } else {
         AccountSession other = (AccountSession)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$uuid = this.getUuid();
            Object other$uuid = other.getUuid();
            if (this$uuid == null) {
               if (other$uuid != null) {
                  return false;
               }
            } else if (!this$uuid.equals(other$uuid)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof AccountSession;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $uuid = this.getUuid();
      result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
      return result;
   }
}
