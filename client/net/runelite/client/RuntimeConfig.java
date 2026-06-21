package net.runelite.client;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.util.LinkBrowser;

public class RuntimeConfig {
   private Map<String, ?> props = Collections.emptyMap();
   private Map<String, String> sysProps = Collections.emptyMap();
   private JsonArray compilerControl;
   private String outageMessage;
   private Map<String, String> outageLinks;
   private Instant outageStart;
   private Instant outageEnd;
   private Set<Integer> ignoreDeadNpcs;
   private Set<Integer> forceDeadNpcs;
   private Set<Integer> resetDeadOnChangeNpcs;
   private Set<Integer> forceDeadAnimations;
   private Set<Integer> healthCheckDeadNpcs;
   private Set<String> outdatedClientVersions;
   private String[] updateLauncherWinVers;
   private double updateRollout;

   public boolean showOutageMessage() {
      if (!Strings.isNullOrEmpty(this.getOutageMessage()) && (this.outageStart == null || !Instant.now().isBefore(this.outageStart)) && (this.outageEnd == null || !Instant.now().isAfter(this.outageEnd))) {
         SwingUtilities.invokeLater(() -> {
            FatalErrorDialog fed = new FatalErrorDialog(this.getOutageMessage());
            if (this.getOutageLinks() != null) {
               Iterator var2 = this.getOutageLinks().entrySet().iterator();

               while(var2.hasNext()) {
                  Map.Entry<String, String> e = (Map.Entry)var2.next();
                  fed.addButton((String)e.getKey(), () -> {
                     LinkBrowser.browse((String)e.getValue());
                  });
               }
            } else {
               fed.addButton("OSRS Twitter", () -> {
                  LinkBrowser.browse(RuneLiteProperties.getOSRSTwitterLink());
               });
            }

            fed.open();
         });
         return true;
      } else {
         return false;
      }
   }

   void refresh(RuntimeConfig config) {
      this.ignoreDeadNpcs = config.ignoreDeadNpcs;
      this.forceDeadNpcs = config.forceDeadNpcs;
      this.resetDeadOnChangeNpcs = config.resetDeadOnChangeNpcs;
      this.forceDeadAnimations = config.forceDeadAnimations;
      this.healthCheckDeadNpcs = config.healthCheckDeadNpcs;
      this.outdatedClientVersions = config.outdatedClientVersions;
   }

   public Map<String, ?> getProps() {
      return this.props;
   }

   public Map<String, String> getSysProps() {
      return this.sysProps;
   }

   public JsonArray getCompilerControl() {
      return this.compilerControl;
   }

   public String getOutageMessage() {
      return this.outageMessage;
   }

   public Map<String, String> getOutageLinks() {
      return this.outageLinks;
   }

   public Instant getOutageStart() {
      return this.outageStart;
   }

   public Instant getOutageEnd() {
      return this.outageEnd;
   }

   public Set<Integer> getIgnoreDeadNpcs() {
      return this.ignoreDeadNpcs;
   }

   public Set<Integer> getForceDeadNpcs() {
      return this.forceDeadNpcs;
   }

   public Set<Integer> getResetDeadOnChangeNpcs() {
      return this.resetDeadOnChangeNpcs;
   }

   public Set<Integer> getForceDeadAnimations() {
      return this.forceDeadAnimations;
   }

   public Set<Integer> getHealthCheckDeadNpcs() {
      return this.healthCheckDeadNpcs;
   }

   public Set<String> getOutdatedClientVersions() {
      return this.outdatedClientVersions;
   }

   public String[] getUpdateLauncherWinVers() {
      return this.updateLauncherWinVers;
   }

   public double getUpdateRollout() {
      return this.updateRollout;
   }

   public void setProps(Map<String, ?> props) {
      this.props = props;
   }

   public void setSysProps(Map<String, String> sysProps) {
      this.sysProps = sysProps;
   }

   public void setCompilerControl(JsonArray compilerControl) {
      this.compilerControl = compilerControl;
   }

   public void setOutageMessage(String outageMessage) {
      this.outageMessage = outageMessage;
   }

   public void setOutageLinks(Map<String, String> outageLinks) {
      this.outageLinks = outageLinks;
   }

   public void setOutageStart(Instant outageStart) {
      this.outageStart = outageStart;
   }

   public void setOutageEnd(Instant outageEnd) {
      this.outageEnd = outageEnd;
   }

   public void setIgnoreDeadNpcs(Set<Integer> ignoreDeadNpcs) {
      this.ignoreDeadNpcs = ignoreDeadNpcs;
   }

   public void setForceDeadNpcs(Set<Integer> forceDeadNpcs) {
      this.forceDeadNpcs = forceDeadNpcs;
   }

   public void setResetDeadOnChangeNpcs(Set<Integer> resetDeadOnChangeNpcs) {
      this.resetDeadOnChangeNpcs = resetDeadOnChangeNpcs;
   }

   public void setForceDeadAnimations(Set<Integer> forceDeadAnimations) {
      this.forceDeadAnimations = forceDeadAnimations;
   }

   public void setHealthCheckDeadNpcs(Set<Integer> healthCheckDeadNpcs) {
      this.healthCheckDeadNpcs = healthCheckDeadNpcs;
   }

   public void setOutdatedClientVersions(Set<String> outdatedClientVersions) {
      this.outdatedClientVersions = outdatedClientVersions;
   }

   public void setUpdateLauncherWinVers(String[] updateLauncherWinVers) {
      this.updateLauncherWinVers = updateLauncherWinVers;
   }

   public void setUpdateRollout(double updateRollout) {
      this.updateRollout = updateRollout;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RuntimeConfig)) {
         return false;
      } else {
         RuntimeConfig other = (RuntimeConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getUpdateRollout(), other.getUpdateRollout()) != 0) {
            return false;
         } else {
            label173: {
               Object this$props = this.getProps();
               Object other$props = other.getProps();
               if (this$props == null) {
                  if (other$props == null) {
                     break label173;
                  }
               } else if (this$props.equals(other$props)) {
                  break label173;
               }

               return false;
            }

            label166: {
               Object this$sysProps = this.getSysProps();
               Object other$sysProps = other.getSysProps();
               if (this$sysProps == null) {
                  if (other$sysProps == null) {
                     break label166;
                  }
               } else if (this$sysProps.equals(other$sysProps)) {
                  break label166;
               }

               return false;
            }

            Object this$compilerControl = this.getCompilerControl();
            Object other$compilerControl = other.getCompilerControl();
            if (this$compilerControl == null) {
               if (other$compilerControl != null) {
                  return false;
               }
            } else if (!this$compilerControl.equals(other$compilerControl)) {
               return false;
            }

            label152: {
               Object this$outageMessage = this.getOutageMessage();
               Object other$outageMessage = other.getOutageMessage();
               if (this$outageMessage == null) {
                  if (other$outageMessage == null) {
                     break label152;
                  }
               } else if (this$outageMessage.equals(other$outageMessage)) {
                  break label152;
               }

               return false;
            }

            Object this$outageLinks = this.getOutageLinks();
            Object other$outageLinks = other.getOutageLinks();
            if (this$outageLinks == null) {
               if (other$outageLinks != null) {
                  return false;
               }
            } else if (!this$outageLinks.equals(other$outageLinks)) {
               return false;
            }

            label138: {
               Object this$outageStart = this.getOutageStart();
               Object other$outageStart = other.getOutageStart();
               if (this$outageStart == null) {
                  if (other$outageStart == null) {
                     break label138;
                  }
               } else if (this$outageStart.equals(other$outageStart)) {
                  break label138;
               }

               return false;
            }

            Object this$outageEnd = this.getOutageEnd();
            Object other$outageEnd = other.getOutageEnd();
            if (this$outageEnd == null) {
               if (other$outageEnd != null) {
                  return false;
               }
            } else if (!this$outageEnd.equals(other$outageEnd)) {
               return false;
            }

            Object this$ignoreDeadNpcs = this.getIgnoreDeadNpcs();
            Object other$ignoreDeadNpcs = other.getIgnoreDeadNpcs();
            if (this$ignoreDeadNpcs == null) {
               if (other$ignoreDeadNpcs != null) {
                  return false;
               }
            } else if (!this$ignoreDeadNpcs.equals(other$ignoreDeadNpcs)) {
               return false;
            }

            Object this$forceDeadNpcs = this.getForceDeadNpcs();
            Object other$forceDeadNpcs = other.getForceDeadNpcs();
            if (this$forceDeadNpcs == null) {
               if (other$forceDeadNpcs != null) {
                  return false;
               }
            } else if (!this$forceDeadNpcs.equals(other$forceDeadNpcs)) {
               return false;
            }

            label110: {
               Object this$resetDeadOnChangeNpcs = this.getResetDeadOnChangeNpcs();
               Object other$resetDeadOnChangeNpcs = other.getResetDeadOnChangeNpcs();
               if (this$resetDeadOnChangeNpcs == null) {
                  if (other$resetDeadOnChangeNpcs == null) {
                     break label110;
                  }
               } else if (this$resetDeadOnChangeNpcs.equals(other$resetDeadOnChangeNpcs)) {
                  break label110;
               }

               return false;
            }

            label103: {
               Object this$forceDeadAnimations = this.getForceDeadAnimations();
               Object other$forceDeadAnimations = other.getForceDeadAnimations();
               if (this$forceDeadAnimations == null) {
                  if (other$forceDeadAnimations == null) {
                     break label103;
                  }
               } else if (this$forceDeadAnimations.equals(other$forceDeadAnimations)) {
                  break label103;
               }

               return false;
            }

            Object this$healthCheckDeadNpcs = this.getHealthCheckDeadNpcs();
            Object other$healthCheckDeadNpcs = other.getHealthCheckDeadNpcs();
            if (this$healthCheckDeadNpcs == null) {
               if (other$healthCheckDeadNpcs != null) {
                  return false;
               }
            } else if (!this$healthCheckDeadNpcs.equals(other$healthCheckDeadNpcs)) {
               return false;
            }

            label89: {
               Object this$outdatedClientVersions = this.getOutdatedClientVersions();
               Object other$outdatedClientVersions = other.getOutdatedClientVersions();
               if (this$outdatedClientVersions == null) {
                  if (other$outdatedClientVersions == null) {
                     break label89;
                  }
               } else if (this$outdatedClientVersions.equals(other$outdatedClientVersions)) {
                  break label89;
               }

               return false;
            }

            if (!Arrays.deepEquals(this.getUpdateLauncherWinVers(), other.getUpdateLauncherWinVers())) {
               return false;
            } else {
               return true;
            }
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof RuntimeConfig;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $updateRollout = Double.doubleToLongBits(this.getUpdateRollout());
      result = result * 59 + (int)($updateRollout >>> 32 ^ $updateRollout);
      Object $props = this.getProps();
      result = result * 59 + ($props == null ? 43 : $props.hashCode());
      Object $sysProps = this.getSysProps();
      result = result * 59 + ($sysProps == null ? 43 : $sysProps.hashCode());
      Object $compilerControl = this.getCompilerControl();
      result = result * 59 + ($compilerControl == null ? 43 : $compilerControl.hashCode());
      Object $outageMessage = this.getOutageMessage();
      result = result * 59 + ($outageMessage == null ? 43 : $outageMessage.hashCode());
      Object $outageLinks = this.getOutageLinks();
      result = result * 59 + ($outageLinks == null ? 43 : $outageLinks.hashCode());
      Object $outageStart = this.getOutageStart();
      result = result * 59 + ($outageStart == null ? 43 : $outageStart.hashCode());
      Object $outageEnd = this.getOutageEnd();
      result = result * 59 + ($outageEnd == null ? 43 : $outageEnd.hashCode());
      Object $ignoreDeadNpcs = this.getIgnoreDeadNpcs();
      result = result * 59 + ($ignoreDeadNpcs == null ? 43 : $ignoreDeadNpcs.hashCode());
      Object $forceDeadNpcs = this.getForceDeadNpcs();
      result = result * 59 + ($forceDeadNpcs == null ? 43 : $forceDeadNpcs.hashCode());
      Object $resetDeadOnChangeNpcs = this.getResetDeadOnChangeNpcs();
      result = result * 59 + ($resetDeadOnChangeNpcs == null ? 43 : $resetDeadOnChangeNpcs.hashCode());
      Object $forceDeadAnimations = this.getForceDeadAnimations();
      result = result * 59 + ($forceDeadAnimations == null ? 43 : $forceDeadAnimations.hashCode());
      Object $healthCheckDeadNpcs = this.getHealthCheckDeadNpcs();
      result = result * 59 + ($healthCheckDeadNpcs == null ? 43 : $healthCheckDeadNpcs.hashCode());
      Object $outdatedClientVersions = this.getOutdatedClientVersions();
      result = result * 59 + ($outdatedClientVersions == null ? 43 : $outdatedClientVersions.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getUpdateLauncherWinVers());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getProps());
      return "RuntimeConfig(props=" + var10000 + ", sysProps=" + String.valueOf(this.getSysProps()) + ", compilerControl=" + String.valueOf(this.getCompilerControl()) + ", outageMessage=" + this.getOutageMessage() + ", outageLinks=" + String.valueOf(this.getOutageLinks()) + ", outageStart=" + String.valueOf(this.getOutageStart()) + ", outageEnd=" + String.valueOf(this.getOutageEnd()) + ", ignoreDeadNpcs=" + String.valueOf(this.getIgnoreDeadNpcs()) + ", forceDeadNpcs=" + String.valueOf(this.getForceDeadNpcs()) + ", resetDeadOnChangeNpcs=" + String.valueOf(this.getResetDeadOnChangeNpcs()) + ", forceDeadAnimations=" + String.valueOf(this.getForceDeadAnimations()) + ", healthCheckDeadNpcs=" + String.valueOf(this.getHealthCheckDeadNpcs()) + ", outdatedClientVersions=" + String.valueOf(this.getOutdatedClientVersions()) + ", updateLauncherWinVers=" + Arrays.deepToString(this.getUpdateLauncherWinVers()) + ", updateRollout=" + this.getUpdateRollout() + ")";
   }
}
