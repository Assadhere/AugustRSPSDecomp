package net.runelite.client.events;

import java.awt.TrayIcon;
import net.runelite.client.config.Notification;

public final class NotificationFired {
   private final Notification notification;
   private final String message;
   private final TrayIcon.MessageType type;

   public NotificationFired(Notification notification, String message, TrayIcon.MessageType type) {
      this.notification = notification;
      this.message = message;
      this.type = type;
   }

   public Notification getNotification() {
      return this.notification;
   }

   public String getMessage() {
      return this.message;
   }

   public TrayIcon.MessageType getType() {
      return this.type;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NotificationFired)) {
         return false;
      } else {
         NotificationFired other;
         label44: {
            other = (NotificationFired)o;
            Object this$notification = this.getNotification();
            Object other$notification = other.getNotification();
            if (this$notification == null) {
               if (other$notification == null) {
                  break label44;
               }
            } else if (this$notification.equals(other$notification)) {
               break label44;
            }

            return false;
         }

         Object this$message = this.getMessage();
         Object other$message = other.getMessage();
         if (this$message == null) {
            if (other$message != null) {
               return false;
            }
         } else if (!this$message.equals(other$message)) {
            return false;
         }

         Object this$type = this.getType();
         Object other$type = other.getType();
         if (this$type == null) {
            if (other$type != null) {
               return false;
            }
         } else if (!this$type.equals(other$type)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $notification = this.getNotification();
      result = result * 59 + ($notification == null ? 43 : $notification.hashCode());
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getNotification());
      return "NotificationFired(notification=" + var10000 + ", message=" + this.getMessage() + ", type=" + String.valueOf(this.getType()) + ")";
   }
}
