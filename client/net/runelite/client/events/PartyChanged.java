package net.runelite.client.events;

public final class PartyChanged {
   private final String passphrase;
   private final Long partyId;

   public PartyChanged(String passphrase, Long partyId) {
      this.passphrase = passphrase;
      this.partyId = partyId;
   }

   public String getPassphrase() {
      return this.passphrase;
   }

   public Long getPartyId() {
      return this.partyId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PartyChanged)) {
         return false;
      } else {
         PartyChanged other = (PartyChanged)o;
         Object this$partyId = this.getPartyId();
         Object other$partyId = other.getPartyId();
         if (this$partyId == null) {
            if (other$partyId != null) {
               return false;
            }
         } else if (!this$partyId.equals(other$partyId)) {
            return false;
         }

         Object this$passphrase = this.getPassphrase();
         Object other$passphrase = other.getPassphrase();
         if (this$passphrase == null) {
            if (other$passphrase != null) {
               return false;
            }
         } else if (!this$passphrase.equals(other$passphrase)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $partyId = this.getPartyId();
      result = result * 59 + ($partyId == null ? 43 : $partyId.hashCode());
      Object $passphrase = this.getPassphrase();
      result = result * 59 + ($passphrase == null ? 43 : $passphrase.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getPassphrase();
      return "PartyChanged(passphrase=" + var10000 + ", partyId=" + this.getPartyId() + ")";
   }
}
