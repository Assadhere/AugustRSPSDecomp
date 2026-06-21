package net.runelite.client.plugins.friendnotes;

final class HoveredFriend {
   private final String friendName;
   private final String note;

   public HoveredFriend(String friendName, String note) {
      this.friendName = friendName;
      this.note = note;
   }

   public String getFriendName() {
      return this.friendName;
   }

   public String getNote() {
      return this.note;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HoveredFriend)) {
         return false;
      } else {
         HoveredFriend other = (HoveredFriend)o;
         Object this$friendName = this.getFriendName();
         Object other$friendName = other.getFriendName();
         if (this$friendName == null) {
            if (other$friendName != null) {
               return false;
            }
         } else if (!this$friendName.equals(other$friendName)) {
            return false;
         }

         Object this$note = this.getNote();
         Object other$note = other.getNote();
         if (this$note == null) {
            if (other$note != null) {
               return false;
            }
         } else if (!this$note.equals(other$note)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $friendName = this.getFriendName();
      result = result * 59 + ($friendName == null ? 43 : $friendName.hashCode());
      Object $note = this.getNote();
      result = result * 59 + ($note == null ? 43 : $note.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getFriendName();
      return "HoveredFriend(friendName=" + var10000 + ", note=" + this.getNote() + ")";
   }
}
