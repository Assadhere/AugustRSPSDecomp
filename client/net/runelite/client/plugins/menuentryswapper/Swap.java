package net.runelite.client.plugins.menuentryswapper;

import java.util.function.Predicate;
import java.util.function.Supplier;

final class Swap {
   private final Predicate<String> optionPredicate;
   private final Predicate<String> targetPredicate;
   private final String swappedOption;
   private final Supplier<Boolean> enabled;
   private final boolean strict;

   public Swap(Predicate<String> optionPredicate, Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled, boolean strict) {
      this.optionPredicate = optionPredicate;
      this.targetPredicate = targetPredicate;
      this.swappedOption = swappedOption;
      this.enabled = enabled;
      this.strict = strict;
   }

   public Predicate<String> getOptionPredicate() {
      return this.optionPredicate;
   }

   public Predicate<String> getTargetPredicate() {
      return this.targetPredicate;
   }

   public String getSwappedOption() {
      return this.swappedOption;
   }

   public Supplier<Boolean> getEnabled() {
      return this.enabled;
   }

   public boolean isStrict() {
      return this.strict;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Swap)) {
         return false;
      } else {
         Swap other = (Swap)o;
         if (this.isStrict() != other.isStrict()) {
            return false;
         } else {
            label59: {
               Object this$optionPredicate = this.getOptionPredicate();
               Object other$optionPredicate = other.getOptionPredicate();
               if (this$optionPredicate == null) {
                  if (other$optionPredicate == null) {
                     break label59;
                  }
               } else if (this$optionPredicate.equals(other$optionPredicate)) {
                  break label59;
               }

               return false;
            }

            Object this$targetPredicate = this.getTargetPredicate();
            Object other$targetPredicate = other.getTargetPredicate();
            if (this$targetPredicate == null) {
               if (other$targetPredicate != null) {
                  return false;
               }
            } else if (!this$targetPredicate.equals(other$targetPredicate)) {
               return false;
            }

            Object this$swappedOption = this.getSwappedOption();
            Object other$swappedOption = other.getSwappedOption();
            if (this$swappedOption == null) {
               if (other$swappedOption != null) {
                  return false;
               }
            } else if (!this$swappedOption.equals(other$swappedOption)) {
               return false;
            }

            Object this$enabled = this.getEnabled();
            Object other$enabled = other.getEnabled();
            if (this$enabled == null) {
               if (other$enabled != null) {
                  return false;
               }
            } else if (!this$enabled.equals(other$enabled)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isStrict() ? 79 : 97);
      Object $optionPredicate = this.getOptionPredicate();
      result = result * 59 + ($optionPredicate == null ? 43 : $optionPredicate.hashCode());
      Object $targetPredicate = this.getTargetPredicate();
      result = result * 59 + ($targetPredicate == null ? 43 : $targetPredicate.hashCode());
      Object $swappedOption = this.getSwappedOption();
      result = result * 59 + ($swappedOption == null ? 43 : $swappedOption.hashCode());
      Object $enabled = this.getEnabled();
      result = result * 59 + ($enabled == null ? 43 : $enabled.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getOptionPredicate());
      return "Swap(optionPredicate=" + var10000 + ", targetPredicate=" + String.valueOf(this.getTargetPredicate()) + ", swappedOption=" + this.getSwappedOption() + ", enabled=" + String.valueOf(this.getEnabled()) + ", strict=" + this.isStrict() + ")";
   }
}
