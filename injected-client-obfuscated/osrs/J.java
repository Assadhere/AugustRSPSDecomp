package osrs;

public class J {
   public static final char[] a = new char[]{' ', ' ', '_', '-', 'à', 'á', 'â', 'ä', 'ã', 'À', 'Á', 'Â', 'Ä', 'Ã', 'è', 'é', 'ê', 'ë', 'È', 'É', 'Ê', 'Ë', 'í', 'î', 'ï', 'Í', 'Î', 'Ï', 'ò', 'ó', 'ô', 'ö', 'õ', 'Ò', 'Ó', 'Ô', 'Ö', 'Õ', 'ù', 'ú', 'û', 'ü', 'Ù', 'Ú', 'Û', 'Ü', 'ç', 'Ç', 'ÿ', 'Ÿ', 'ñ', 'Ñ', 'ß'};
   public static final char[] b = new char[]{'[', ']', '#'};

   public static final boolean a(char var0) {
      if (Character.isISOControl(var0)) {
         return false;
      } else if (br.c(var0)) {
         return true;
      } else {
         char[] var1 = a;

         int var3;
         for(int var2 = 0; var2 < var1.length; ++var2) {
            var3 = var1[var2];
            if (var0 == var3) {
               return true;
            }
         }

         char[] var5 = b;

         for(var3 = 0; var3 < var5.length; ++var3) {
            char var4 = var5[var3];
            if (var0 == var4) {
               return true;
            }
         }

         return false;
      }
   }

   public static final boolean b(char var0) {
      return var0 == 160 || var0 == ' ' || var0 == '_' || var0 == '-';
   }

   public static String a(CharSequence var0, aM var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = 0;

         int var3;
         for(var3 = var0.length(); var2 < var3 && b(var0.charAt(var2)); ++var2) {
         }

         while(var3 > var2 && b(var0.charAt(var3 - 1))) {
            --var3;
         }

         int var4 = var3 - var2;
         if (var4 >= 1) {
            byte var5;
            if (var1 == null) {
               var5 = 12;
            } else {
               switch (var1.j) {
                  case 7:
                     var5 = 20;
                     break;
                  default:
                     var5 = 12;
               }
            }

            if (var4 <= var5) {
               StringBuilder var6 = new StringBuilder(var4);

               for(int var7 = var2; var7 < var3; ++var7) {
                  char var8 = var0.charAt(var7);
                  if (a(var8)) {
                     char var9;
                     switch (var8) {
                        case ' ':
                        case '-':
                        case '_':
                        case ' ':
                           var9 = '_';
                           break;
                        case '#':
                        case '[':
                        case ']':
                           var9 = var8;
                           break;
                        case 'À':
                        case 'Á':
                        case 'Â':
                        case 'Ã':
                        case 'Ä':
                        case 'à':
                        case 'á':
                        case 'â':
                        case 'ã':
                        case 'ä':
                           var9 = 'a';
                           break;
                        case 'Ç':
                        case 'ç':
                           var9 = 'c';
                           break;
                        case 'È':
                        case 'É':
                        case 'Ê':
                        case 'Ë':
                        case 'è':
                        case 'é':
                        case 'ê':
                        case 'ë':
                           var9 = 'e';
                           break;
                        case 'Í':
                        case 'Î':
                        case 'Ï':
                        case 'í':
                        case 'î':
                        case 'ï':
                           var9 = 'i';
                           break;
                        case 'Ñ':
                        case 'ñ':
                           var9 = 'n';
                           break;
                        case 'Ò':
                        case 'Ó':
                        case 'Ô':
                        case 'Õ':
                        case 'Ö':
                        case 'ò':
                        case 'ó':
                        case 'ô':
                        case 'õ':
                        case 'ö':
                           var9 = 'o';
                           break;
                        case 'Ù':
                        case 'Ú':
                        case 'Û':
                        case 'Ü':
                        case 'ù':
                        case 'ú':
                        case 'û':
                        case 'ü':
                           var9 = 'u';
                           break;
                        case 'ß':
                           var9 = 'b';
                           break;
                        case 'ÿ':
                        case 'Ÿ':
                           var9 = 'y';
                           break;
                        default:
                           var9 = Character.toLowerCase(var8);
                     }

                     if (var9 != 0) {
                        var6.append(var9);
                     }
                  }
               }

               if (var6.length() == 0) {
                  return null;
               }

               return var6.toString();
            }
         }

         return null;
      }
   }

   public static String a(String var0) {
      return var0 != null && !var0.isEmpty() && var0.charAt(0) != '#' ? var0 : "";
   }
}
