package osrs;

public class hO implements fF {
   public static final hO a = new hO(32768);
   public static final hO b = new hO(65536);
   public static final hO c = new hO(131072);
   public static final hO d = new hO(262144);
   public static final hO e = new hO(524288);
   public static final hO f = new hO(1048576);
   public static final hO g = new hO(2097152);
   public static final hO h = new hO(4194304);
   public static final hO i = new hO(8388608);
   public static final hO j = new hO(16777216);
   public static final hO k = new hO(33554432);
   public static final hO l = new hO(67108864, true);
   public static final hO m = new hO(134217728);
   public static final hO n = new hO(268435456);
   public static final hO o = new hO(536870912, true);
   public static final hO p = new hO(1073741824, true);
   public static final hO q = new hO(Integer.MIN_VALUE);
   public final int r;
   public static final hO s = new hO(1);
   public static final hO t = new hO(2);
   public static final hO u = new hO(4);
   public static final hO v = new hO(8);
   public static final hO w = new hO(16);
   public static final hO x = new hO(32);
   public static final hO y = new hO(64, true);
   public static final hO z = new hO(128);
   public static final hO A = new hO(256, true);
   public static final hO B = new hO(512);
   public static final hO C = new hO(1024);
   public static final hO D = new hO(2048);
   public static final hO E = new hO(4096);
   public static final hO F = new hO(8192);
   public static final hO G = new hO(16384);

   public hO(int var1, boolean var2) {
      this.r = var1;
   }

   public hO(int var1) {
      this(var1, false);
   }

   public int b() {
      return this.r;
   }
}
