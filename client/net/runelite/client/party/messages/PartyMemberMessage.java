package net.runelite.client.party.messages;

public abstract class PartyMemberMessage extends PartyMessage {
   private transient long memberId;

   public long getMemberId() {
      return this.memberId;
   }

   public void setMemberId(long memberId) {
      this.memberId = memberId;
   }
}
