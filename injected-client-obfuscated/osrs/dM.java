package osrs;

public interface dM {
   boolean a(int var1);

   int b(int var1);

   default int c(int var1) {
      return this.b(var1);
   }

   int[] d(int var1);
}
