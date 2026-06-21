package net.runelite.client.hiscore;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HiscoreManager {
   static final HiscoreResult EMPTY = new HiscoreResult((String)null, ImmutableMap.of());
   static final HiscoreResult NONE = new HiscoreResult((String)null, ImmutableMap.of());
   private final LoadingCache<HiscoreKey, HiscoreResult> hiscoreCache;
   private final HiscoreClient hiscoreClient;

   @Inject
   private HiscoreManager(ScheduledExecutorService executor, HiscoreClient hiscoreClient) {
      this.hiscoreClient = hiscoreClient;
      this.hiscoreCache = CacheBuilder.newBuilder().maximumSize(128L).expireAfterWrite(1L, TimeUnit.HOURS).build(new HiscoreLoader(executor, hiscoreClient));
   }

   public HiscoreResult lookup(String username, HiscoreEndpoint endpoint) throws IOException {
      HiscoreKey hiscoreKey = new HiscoreKey(username, endpoint);
      HiscoreResult hiscoreResult = (HiscoreResult)this.hiscoreCache.getIfPresent(hiscoreKey);
      if (hiscoreResult != null && hiscoreResult != EMPTY) {
         return hiscoreResult == NONE ? null : hiscoreResult;
      } else {
         hiscoreResult = this.hiscoreClient.lookup(username, endpoint);
         if (hiscoreResult == null) {
            this.hiscoreCache.put(hiscoreKey, NONE);
            return null;
         } else {
            this.hiscoreCache.put(hiscoreKey, hiscoreResult);
            return hiscoreResult;
         }
      }
   }

   public HiscoreResult lookupAsync(String username, HiscoreEndpoint endpoint) {
      HiscoreKey hiscoreKey = new HiscoreKey(username, endpoint);
      HiscoreResult hiscoreResult = (HiscoreResult)this.hiscoreCache.getIfPresent(hiscoreKey);
      if (hiscoreResult != null && hiscoreResult != EMPTY) {
         return hiscoreResult == NONE ? null : hiscoreResult;
      } else {
         this.hiscoreCache.refresh(hiscoreKey);
         return null;
      }
   }

   static class HiscoreKey {
      String username;
      HiscoreEndpoint type;

      public HiscoreKey(String username, HiscoreEndpoint type) {
         this.username = username;
         this.type = type;
      }

      public String getUsername() {
         return this.username;
      }

      public HiscoreEndpoint getType() {
         return this.type;
      }

      public void setUsername(String username) {
         this.username = username;
      }

      public void setType(HiscoreEndpoint type) {
         this.type = type;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof HiscoreKey)) {
            return false;
         } else {
            HiscoreKey other = (HiscoreKey)o;
            if (!other.canEqual(this)) {
               return false;
            } else {
               Object this$username = this.getUsername();
               Object other$username = other.getUsername();
               if (this$username == null) {
                  if (other$username != null) {
                     return false;
                  }
               } else if (!this$username.equals(other$username)) {
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
      }

      protected boolean canEqual(Object other) {
         return other instanceof HiscoreKey;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $username = this.getUsername();
         result = result * 59 + ($username == null ? 43 : $username.hashCode());
         Object $type = this.getType();
         result = result * 59 + ($type == null ? 43 : $type.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = this.getUsername();
         return "HiscoreManager.HiscoreKey(username=" + var10000 + ", type=" + String.valueOf(this.getType()) + ")";
      }
   }
}
