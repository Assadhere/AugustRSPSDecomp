package net.runelite.client.externalplugins;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.annotation.Nullable;
import net.runelite.client.RuneLite;

public class PluginHubManifest {
   public static final Base64.Encoder HASH_ENCODER = Base64.getUrlEncoder().withoutPadding();

   public static class Stub extends DisplayData {
      private String[] plugins;

      public String[] getPlugins() {
         return this.plugins;
      }

      public void setPlugins(String[] plugins) {
         this.plugins = plugins;
      }

      public String toString() {
         return "PluginHubManifest.Stub(plugins=" + Arrays.deepToString(this.getPlugins()) + ")";
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Stub)) {
            return false;
         } else {
            Stub other = (Stub)o;
            if (!other.canEqual(this)) {
               return false;
            } else if (!super.equals(o)) {
               return false;
            } else {
               return Arrays.deepEquals(this.getPlugins(), other.getPlugins());
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof Stub;
      }

      public int hashCode() {
         int PRIME = true;
         int result = super.hashCode();
         result = result * 59 + Arrays.deepHashCode(this.getPlugins());
         return result;
      }
   }

   public static class DisplayData {
      private String internalName;
      private String displayName;
      private String version;
      @Nullable
      private String iconHash;
      private long createdAt;
      private long lastUpdatedAt;
      private String author;
      @Nullable
      private String description;
      @Nullable
      private String warning;
      @Nullable
      private String[] tags;
      @Nullable
      private String unavailableReason;

      public boolean hasIcon() {
         return this.iconHash != null;
      }

      public String getInternalName() {
         return this.internalName;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public String getVersion() {
         return this.version;
      }

      @Nullable
      public String getIconHash() {
         return this.iconHash;
      }

      public long getCreatedAt() {
         return this.createdAt;
      }

      public long getLastUpdatedAt() {
         return this.lastUpdatedAt;
      }

      public String getAuthor() {
         return this.author;
      }

      @Nullable
      public String getDescription() {
         return this.description;
      }

      @Nullable
      public String getWarning() {
         return this.warning;
      }

      @Nullable
      public String[] getTags() {
         return this.tags;
      }

      @Nullable
      public String getUnavailableReason() {
         return this.unavailableReason;
      }

      public void setInternalName(String internalName) {
         this.internalName = internalName;
      }

      public void setDisplayName(String displayName) {
         this.displayName = displayName;
      }

      public void setVersion(String version) {
         this.version = version;
      }

      public void setIconHash(@Nullable String iconHash) {
         this.iconHash = iconHash;
      }

      public void setCreatedAt(long createdAt) {
         this.createdAt = createdAt;
      }

      public void setLastUpdatedAt(long lastUpdatedAt) {
         this.lastUpdatedAt = lastUpdatedAt;
      }

      public void setAuthor(String author) {
         this.author = author;
      }

      public void setDescription(@Nullable String description) {
         this.description = description;
      }

      public void setWarning(@Nullable String warning) {
         this.warning = warning;
      }

      public void setTags(@Nullable String[] tags) {
         this.tags = tags;
      }

      public void setUnavailableReason(@Nullable String unavailableReason) {
         this.unavailableReason = unavailableReason;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof DisplayData)) {
            return false;
         } else {
            DisplayData other = (DisplayData)o;
            if (!other.canEqual(this)) {
               return false;
            } else if (this.getCreatedAt() != other.getCreatedAt()) {
               return false;
            } else if (this.getLastUpdatedAt() != other.getLastUpdatedAt()) {
               return false;
            } else {
               label116: {
                  Object this$internalName = this.getInternalName();
                  Object other$internalName = other.getInternalName();
                  if (this$internalName == null) {
                     if (other$internalName == null) {
                        break label116;
                     }
                  } else if (this$internalName.equals(other$internalName)) {
                     break label116;
                  }

                  return false;
               }

               Object this$displayName = this.getDisplayName();
               Object other$displayName = other.getDisplayName();
               if (this$displayName == null) {
                  if (other$displayName != null) {
                     return false;
                  }
               } else if (!this$displayName.equals(other$displayName)) {
                  return false;
               }

               label102: {
                  Object this$version = this.getVersion();
                  Object other$version = other.getVersion();
                  if (this$version == null) {
                     if (other$version == null) {
                        break label102;
                     }
                  } else if (this$version.equals(other$version)) {
                     break label102;
                  }

                  return false;
               }

               label95: {
                  Object this$iconHash = this.getIconHash();
                  Object other$iconHash = other.getIconHash();
                  if (this$iconHash == null) {
                     if (other$iconHash == null) {
                        break label95;
                     }
                  } else if (this$iconHash.equals(other$iconHash)) {
                     break label95;
                  }

                  return false;
               }

               label88: {
                  Object this$author = this.getAuthor();
                  Object other$author = other.getAuthor();
                  if (this$author == null) {
                     if (other$author == null) {
                        break label88;
                     }
                  } else if (this$author.equals(other$author)) {
                     break label88;
                  }

                  return false;
               }

               Object this$description = this.getDescription();
               Object other$description = other.getDescription();
               if (this$description == null) {
                  if (other$description != null) {
                     return false;
                  }
               } else if (!this$description.equals(other$description)) {
                  return false;
               }

               label74: {
                  Object this$warning = this.getWarning();
                  Object other$warning = other.getWarning();
                  if (this$warning == null) {
                     if (other$warning == null) {
                        break label74;
                     }
                  } else if (this$warning.equals(other$warning)) {
                     break label74;
                  }

                  return false;
               }

               if (!Arrays.deepEquals(this.getTags(), other.getTags())) {
                  return false;
               } else {
                  Object this$unavailableReason = this.getUnavailableReason();
                  Object other$unavailableReason = other.getUnavailableReason();
                  if (this$unavailableReason == null) {
                     if (other$unavailableReason != null) {
                        return false;
                     }
                  } else if (!this$unavailableReason.equals(other$unavailableReason)) {
                     return false;
                  }

                  return true;
               }
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof DisplayData;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         long $createdAt = this.getCreatedAt();
         result = result * 59 + (int)($createdAt >>> 32 ^ $createdAt);
         long $lastUpdatedAt = this.getLastUpdatedAt();
         result = result * 59 + (int)($lastUpdatedAt >>> 32 ^ $lastUpdatedAt);
         Object $internalName = this.getInternalName();
         result = result * 59 + ($internalName == null ? 43 : $internalName.hashCode());
         Object $displayName = this.getDisplayName();
         result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
         Object $version = this.getVersion();
         result = result * 59 + ($version == null ? 43 : $version.hashCode());
         Object $iconHash = this.getIconHash();
         result = result * 59 + ($iconHash == null ? 43 : $iconHash.hashCode());
         Object $author = this.getAuthor();
         result = result * 59 + ($author == null ? 43 : $author.hashCode());
         Object $description = this.getDescription();
         result = result * 59 + ($description == null ? 43 : $description.hashCode());
         Object $warning = this.getWarning();
         result = result * 59 + ($warning == null ? 43 : $warning.hashCode());
         result = result * 59 + Arrays.deepHashCode(this.getTags());
         Object $unavailableReason = this.getUnavailableReason();
         result = result * 59 + ($unavailableReason == null ? 43 : $unavailableReason.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = this.getInternalName();
         return "PluginHubManifest.DisplayData(internalName=" + var10000 + ", displayName=" + this.getDisplayName() + ", version=" + this.getVersion() + ", iconHash=" + this.getIconHash() + ", createdAt=" + this.getCreatedAt() + ", lastUpdatedAt=" + this.getLastUpdatedAt() + ", author=" + this.getAuthor() + ", description=" + this.getDescription() + ", warning=" + this.getWarning() + ", tags=" + Arrays.deepToString(this.getTags()) + ", unavailableReason=" + this.getUnavailableReason() + ")";
      }
   }

   public static class ManifestFull extends ManifestLite {
      private List<DisplayData> display;

      public List<DisplayData> getDisplay() {
         return this.display;
      }

      public void setDisplay(List<DisplayData> display) {
         this.display = display;
      }

      public String toString() {
         return "PluginHubManifest.ManifestFull(display=" + String.valueOf(this.getDisplay()) + ")";
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof ManifestFull)) {
            return false;
         } else {
            ManifestFull other = (ManifestFull)o;
            if (!other.canEqual(this)) {
               return false;
            } else if (!super.equals(o)) {
               return false;
            } else {
               Object this$display = this.getDisplay();
               Object other$display = other.getDisplay();
               if (this$display == null) {
                  if (other$display != null) {
                     return false;
                  }
               } else if (!this$display.equals(other$display)) {
                  return false;
               }

               return true;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof ManifestFull;
      }

      public int hashCode() {
         int PRIME = true;
         int result = super.hashCode();
         Object $display = this.getDisplay();
         result = result * 59 + ($display == null ? 43 : $display.hashCode());
         return result;
      }
   }

   public static class ManifestLite {
      private List<JarData> jars;

      public List<JarData> getJars() {
         return this.jars;
      }

      public void setJars(List<JarData> jars) {
         this.jars = jars;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof ManifestLite)) {
            return false;
         } else {
            ManifestLite other = (ManifestLite)o;
            if (!other.canEqual(this)) {
               return false;
            } else {
               Object this$jars = this.getJars();
               Object other$jars = other.getJars();
               if (this$jars == null) {
                  if (other$jars != null) {
                     return false;
                  }
               } else if (!this$jars.equals(other$jars)) {
                  return false;
               }

               return true;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof ManifestLite;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $jars = this.getJars();
         result = result * 59 + ($jars == null ? 43 : $jars.hashCode());
         return result;
      }

      public String toString() {
         return "PluginHubManifest.ManifestLite(jars=" + String.valueOf(this.getJars()) + ")";
      }
   }

   public static class JarData {
      private String internalName;
      private String displayName;
      private String jarHash;
      private int jarSize;

      File getJarFile() {
         return new File(RuneLite.PLUGINS_DIR, this.internalName + "_" + this.jarHash + ".jar");
      }

      boolean isValid() {
         File file = this.getJarFile();

         try {
            if (file.exists()) {
               HashCode hash = Files.asByteSource(file).hash(Hashing.sha256());
               if (this.jarHash.equals(PluginHubManifest.HASH_ENCODER.encodeToString(hash.asBytes()))) {
                  return true;
               }
            }
         } catch (IOException var3) {
         }

         return false;
      }

      public String getInternalName() {
         return this.internalName;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public String getJarHash() {
         return this.jarHash;
      }

      public int getJarSize() {
         return this.jarSize;
      }

      public void setInternalName(String internalName) {
         this.internalName = internalName;
      }

      public void setDisplayName(String displayName) {
         this.displayName = displayName;
      }

      public void setJarHash(String jarHash) {
         this.jarHash = jarHash;
      }

      public void setJarSize(int jarSize) {
         this.jarSize = jarSize;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof JarData)) {
            return false;
         } else {
            JarData other = (JarData)o;
            if (!other.canEqual(this)) {
               return false;
            } else if (this.getJarSize() != other.getJarSize()) {
               return false;
            } else {
               label49: {
                  Object this$internalName = this.getInternalName();
                  Object other$internalName = other.getInternalName();
                  if (this$internalName == null) {
                     if (other$internalName == null) {
                        break label49;
                     }
                  } else if (this$internalName.equals(other$internalName)) {
                     break label49;
                  }

                  return false;
               }

               Object this$displayName = this.getDisplayName();
               Object other$displayName = other.getDisplayName();
               if (this$displayName == null) {
                  if (other$displayName != null) {
                     return false;
                  }
               } else if (!this$displayName.equals(other$displayName)) {
                  return false;
               }

               Object this$jarHash = this.getJarHash();
               Object other$jarHash = other.getJarHash();
               if (this$jarHash == null) {
                  if (other$jarHash != null) {
                     return false;
                  }
               } else if (!this$jarHash.equals(other$jarHash)) {
                  return false;
               }

               return true;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof JarData;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + this.getJarSize();
         Object $internalName = this.getInternalName();
         result = result * 59 + ($internalName == null ? 43 : $internalName.hashCode());
         Object $displayName = this.getDisplayName();
         result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
         Object $jarHash = this.getJarHash();
         result = result * 59 + ($jarHash == null ? 43 : $jarHash.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = this.getInternalName();
         return "PluginHubManifest.JarData(internalName=" + var10000 + ", displayName=" + this.getDisplayName() + ", jarHash=" + this.getJarHash() + ", jarSize=" + this.getJarSize() + ")";
      }
   }
}
