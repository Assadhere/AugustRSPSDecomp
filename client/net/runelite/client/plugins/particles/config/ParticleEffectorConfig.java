package net.runelite.client.plugins.particles.config;

import net.runelite.client.plugins.particles.core.ParticleEmitter;

public class ParticleEffectorConfig {
   private int id;
   private String name;
   private int spreadAngle;
   private int forceDirectionX;
   private int forceDirectionY;
   private int forceDirectionZ;
   private int falloffType;
   private int falloffRate;
   private int scope;
   private boolean directPositionMode;
   private boolean radialForceMode;
   private boolean invertDirection;
   private int coneAngleCosine;
   private int forceMagnitude;
   private long maxRangeSquared;
   private float invForceMagnitude;
   private float coneCheckMultiplier;

   public ParticleEffectorConfig() {
   }

   public ParticleEffectorConfig(int id) {
      this.id = id;
   }

   public void postDecode() {
      this.coneAngleCosine = ParticleEmitter.COSINE_TABLE[this.spreadAngle << 3];
      long fx = (long)this.forceDirectionX;
      long fy = (long)this.forceDirectionY;
      long fz = (long)this.forceDirectionZ;
      this.forceMagnitude = (int)Math.sqrt((double)(fx * fx + fy * fy + fz * fz));
      if (this.falloffRate == 0) {
         this.falloffRate = 1;
      }

      if (this.falloffType == 0) {
         this.maxRangeSquared = Long.MAX_VALUE;
      } else if (this.falloffType == 1) {
         long range = (long)this.forceMagnitude * 8L / (long)this.falloffRate;
         this.maxRangeSquared = range * range;
      } else if (this.falloffType == 2) {
         this.maxRangeSquared = (long)this.forceMagnitude * 8L / (long)this.falloffRate;
      }

      if (this.invertDirection) {
         this.forceMagnitude = -this.forceMagnitude;
      }

      if (this.forceMagnitude != 0) {
         this.invForceMagnitude = 1.0F / (float)this.forceMagnitude;
         this.coneCheckMultiplier = 65535.0F * this.invForceMagnitude;
      } else {
         this.invForceMagnitude = 1.0F;
         this.coneCheckMultiplier = 65535.0F;
      }

   }

   public boolean isInRange(int particleX, int particleY, int particleZ, int effectorX, int effectorY, int effectorZ) {
      double dx = (double)(particleX - effectorX);
      double dy = (double)(particleY - effectorY);
      double dz = (double)(particleZ - effectorZ);
      double distanceSquared = dx * dx + dy * dy + dz * dz;
      if (distanceSquared > (double)this.maxRangeSquared) {
         return false;
      } else {
         double distance = Math.sqrt(distanceSquared);
         if (distance == 0.0) {
            return true;
         } else {
            double dotProduct = (dx * (double)this.forceDirectionX + dy * (double)this.forceDirectionY + dz * (double)this.forceDirectionZ) * 65535.0 / ((double)this.forceMagnitude * distance);
            return dotProduct >= (double)this.coneAngleCosine;
         }
      }
   }

   public double[] calculateForce(int particleX, int particleY, int particleZ, int effectorX, int effectorY, int effectorZ, int deltaTime) {
      double dx = (double)(particleX - effectorX);
      double dy = (double)(particleY - effectorY);
      double dz = (double)(particleZ - effectorZ);
      double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
      if (distance == 0.0) {
         distance = 1.0;
      }

      double falloff = 0.0;
      if (this.falloffType == 1) {
         falloff = distance / 16.0 * (double)this.falloffRate;
      } else if (this.falloffType == 2) {
         falloff = distance / 16.0 * (distance / 16.0) * (double)this.falloffRate;
      }

      double forceX;
      double forceY;
      double forceZ;
      if (this.radialForceMode) {
         forceX = dx / distance * (double)this.forceMagnitude;
         forceY = dy / distance * (double)this.forceMagnitude;
         forceZ = dz / distance * (double)this.forceMagnitude;
      } else {
         forceX = (double)this.forceDirectionX - falloff;
         forceY = (double)this.forceDirectionY - falloff;
         forceZ = (double)this.forceDirectionZ - falloff;
      }

      return new double[]{forceX * (double)deltaTime, forceY * (double)deltaTime, forceZ * (double)deltaTime};
   }

   public ParticleEffectorConfig copy() {
      ParticleEffectorConfig c = new ParticleEffectorConfig(this.id);
      c.name = this.name;
      c.spreadAngle = this.spreadAngle;
      c.forceDirectionX = this.forceDirectionX;
      c.forceDirectionY = this.forceDirectionY;
      c.forceDirectionZ = this.forceDirectionZ;
      c.falloffType = this.falloffType;
      c.falloffRate = this.falloffRate;
      c.scope = this.scope;
      c.directPositionMode = this.directPositionMode;
      c.radialForceMode = this.radialForceMode;
      c.invertDirection = this.invertDirection;
      c.postDecode();
      return c;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getSpreadAngle() {
      return this.spreadAngle;
   }

   public int getForceDirectionX() {
      return this.forceDirectionX;
   }

   public int getForceDirectionY() {
      return this.forceDirectionY;
   }

   public int getForceDirectionZ() {
      return this.forceDirectionZ;
   }

   public int getFalloffType() {
      return this.falloffType;
   }

   public int getFalloffRate() {
      return this.falloffRate;
   }

   public int getScope() {
      return this.scope;
   }

   public boolean isDirectPositionMode() {
      return this.directPositionMode;
   }

   public boolean isRadialForceMode() {
      return this.radialForceMode;
   }

   public boolean isInvertDirection() {
      return this.invertDirection;
   }

   public int getConeAngleCosine() {
      return this.coneAngleCosine;
   }

   public int getForceMagnitude() {
      return this.forceMagnitude;
   }

   public long getMaxRangeSquared() {
      return this.maxRangeSquared;
   }

   public float getInvForceMagnitude() {
      return this.invForceMagnitude;
   }

   public float getConeCheckMultiplier() {
      return this.coneCheckMultiplier;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSpreadAngle(int spreadAngle) {
      this.spreadAngle = spreadAngle;
   }

   public void setForceDirectionX(int forceDirectionX) {
      this.forceDirectionX = forceDirectionX;
   }

   public void setForceDirectionY(int forceDirectionY) {
      this.forceDirectionY = forceDirectionY;
   }

   public void setForceDirectionZ(int forceDirectionZ) {
      this.forceDirectionZ = forceDirectionZ;
   }

   public void setFalloffType(int falloffType) {
      this.falloffType = falloffType;
   }

   public void setFalloffRate(int falloffRate) {
      this.falloffRate = falloffRate;
   }

   public void setScope(int scope) {
      this.scope = scope;
   }

   public void setDirectPositionMode(boolean directPositionMode) {
      this.directPositionMode = directPositionMode;
   }

   public void setRadialForceMode(boolean radialForceMode) {
      this.radialForceMode = radialForceMode;
   }

   public void setInvertDirection(boolean invertDirection) {
      this.invertDirection = invertDirection;
   }
}
