package net.runelite.api.events;

import java.util.Arrays;

public final class CommandExecuted {
   private final String command;
   private final String[] arguments;

   public CommandExecuted(String command, String[] arguments) {
      this.command = command;
      this.arguments = arguments;
   }

   public String getCommand() {
      return this.command;
   }

   public String[] getArguments() {
      return this.arguments;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CommandExecuted)) {
         return false;
      } else {
         CommandExecuted other = (CommandExecuted)o;
         Object this$command = this.getCommand();
         Object other$command = other.getCommand();
         if (this$command == null) {
            if (other$command == null) {
               return Arrays.deepEquals(this.getArguments(), other.getArguments());
            }
         } else if (this$command.equals(other$command)) {
            return Arrays.deepEquals(this.getArguments(), other.getArguments());
         }

         return false;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $command = this.getCommand();
      result = result * 59 + ($command == null ? 43 : $command.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getArguments());
      return result;
   }

   public String toString() {
      String var10000 = this.getCommand();
      return "CommandExecuted(command=" + var10000 + ", arguments=" + Arrays.deepToString(this.getArguments()) + ")";
   }
}
