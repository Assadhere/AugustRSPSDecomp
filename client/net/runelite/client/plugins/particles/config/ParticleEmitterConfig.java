package net.runelite.client.plugins.particles.config;

public class ParticleEmitterConfig {
   private int id;
   private String name;
   private int spreadYawMin;
   private int spreadYawMax;
   private int spreadPitchMin;
   private int spreadPitchMax;
   private int minSpeed;
   private int maxSpeed;
   private int targetSpeed = -1;
   private int speedTransitionPercent = 100;
   private int minScale;
   private int maxScale;
   private int targetScale = -1;
   private int scaleTransitionPercent = 100;
   private int minRotation;
   private int maxRotation;
   private int targetRotation;
   private int rotationTransitionPercent = 100;
   private int minColourArgb;
   private int maxColourArgb;
   private int targetColourArgb;
   private int colourTransitionPercent = 100;
   private int alphaTransitionPercent = 100;
   private int minLifetime;
   private int maxLifetime;
   private int minSpawnCount;
   private int maxSpawnCount;
   private int initialSpawnCount;
   private boolean emitOnlyBeforeTime;
   private int emissionTimeThreshold;
   private int emissionCycleDuration = -1;
   private boolean loopEmission;
   private int distanceFalloffType;
   private int distanceFalloffStrength;
   private int upperBoundLevel = -2;
   private int lowerBoundLevel = -2;
   private int[] localEffectorFilter;
   private int[] embeddedEffectors;
   private int[] globalEffectors;
   private String texture;
   private int textureIndex = -1;
   private int spriteColumns = 1;
   private int spriteRows = 1;
   private int spriteFrameCount = 1;
   private int minGraphicsQuality;
   private boolean uniformColourVariation = true;
   private boolean forceTextureOnSoftwareRenderer;
   private boolean useSceneAmbientLight = true;
   private boolean collidesWithObjects;
   private boolean clipToTerrain = true;
   private boolean localSpace;
   private int fallbackEmitterType = -1;
   private int startRed;
   private int startGreen;
   private int startBlue;
   private int startAlpha;
   private int deltaRed;
   private int deltaGreen;
   private int deltaBlue;
   private int deltaAlpha;
   private int speedTransitionTicks;
   private int scaleTransitionTicks;
   private int colourTransitionTicks;
   private int alphaTransitionTicks;
   private int speedIncrementPerTick;
   private int scaleIncrementPerTick;
   private int rotationTransitionTicks;
   private int rotationIncrementPerTick;
   private int redIncrementPerTick;
   private int greenIncrementPerTick;
   private int blueIncrementPerTick;
   private int alphaIncrementPerTick;
   private boolean hasHeightBounds;
   private int scaledDistanceFalloffStrength;

   public ParticleEmitterConfig() {
   }

   public ParticleEmitterConfig(int id) {
      this.id = id;
   }

   public void normalize() {
      this.spreadYawMin <<= 3;
      this.spreadYawMax <<= 3;
      this.spreadPitchMin <<= 3;
      this.spreadPitchMax <<= 3;
      this.minScale <<= 14;
      this.maxScale <<= 14;
      if (this.targetScale != -1) {
         this.targetScale <<= 14;
      }

      this.minSpeed >>= 2;
      this.maxSpeed >>= 2;
      if (this.targetSpeed != -1) {
         this.targetSpeed >>= 2;
      }

   }

   public void postDecode() {
      this.recomputeColourFields();
      this.hasHeightBounds = this.upperBoundLevel > -2 || this.lowerBoundLevel > -2;
      int avgRotation;
      if (this.targetScale != -1) {
         this.scaleTransitionTicks = this.scaleTransitionPercent * this.maxLifetime / 100;
         if (this.scaleTransitionTicks == 0) {
            this.scaleTransitionTicks = 1;
         }

         avgRotation = (this.minScale + this.maxScale) / 2;
         this.scaleIncrementPerTick = (this.targetScale - avgRotation) / this.scaleTransitionTicks;
      }

      if (this.targetSpeed != -1) {
         this.speedTransitionTicks = this.maxLifetime * this.speedTransitionPercent / 100;
         if (this.speedTransitionTicks == 0) {
            this.speedTransitionTicks = 1;
         }

         avgRotation = (this.minSpeed + this.maxSpeed) / 2;
         this.speedIncrementPerTick = (this.targetSpeed - avgRotation) / this.speedTransitionTicks;
      }

      this.rotationTransitionTicks = this.maxLifetime * this.rotationTransitionPercent / 100;
      if (this.rotationTransitionTicks == 0) {
         this.rotationTransitionTicks = 1;
      }

      avgRotation = (this.minRotation + this.maxRotation) / 2;
      this.rotationIncrementPerTick = (this.targetRotation - avgRotation) / this.rotationTransitionTicks;
      this.scaledDistanceFalloffStrength = this.distanceFalloffStrength << 2;
   }

   private void recomputeColourFields() {
      this.startRed = this.minColourArgb >> 16 & 255;
      this.startGreen = this.minColourArgb >> 8 & 255;
      this.startBlue = this.minColourArgb & 255;
      this.startAlpha = this.minColourArgb >> 24 & 255;
      int maxRed = this.maxColourArgb >> 16 & 255;
      int maxGreen = this.maxColourArgb >> 8 & 255;
      int maxBlue = this.maxColourArgb & 255;
      int maxAlpha = this.maxColourArgb >> 24 & 255;
      this.deltaRed = maxRed - this.startRed;
      this.deltaGreen = maxGreen - this.startGreen;
      this.deltaBlue = maxBlue - this.startBlue;
      this.deltaAlpha = maxAlpha - this.startAlpha;
      this.redIncrementPerTick = 0;
      this.greenIncrementPerTick = 0;
      this.blueIncrementPerTick = 0;
      this.alphaIncrementPerTick = 0;
      this.colourTransitionTicks = 0;
      this.alphaTransitionTicks = 0;
      if (this.targetColourArgb != 0) {
         this.alphaTransitionTicks = this.alphaTransitionPercent * this.maxLifetime / 100;
         this.colourTransitionTicks = this.maxLifetime * this.colourTransitionPercent / 100;
         if (this.colourTransitionTicks == 0) {
            this.colourTransitionTicks = 1;
         }

         if (this.alphaTransitionTicks == 0) {
            this.alphaTransitionTicks = 1;
         }

         int targetRed = this.targetColourArgb >> 16 & 255;
         int targetGreen = this.targetColourArgb >> 8 & 255;
         int targetBlue = this.targetColourArgb & 255;
         int targetAlpha = this.targetColourArgb >> 24 & 255;
         this.redIncrementPerTick = (targetRed - this.startRed - this.deltaRed / 2 << 8) / this.colourTransitionTicks;
         this.greenIncrementPerTick = (targetGreen - this.startGreen - this.deltaGreen / 2 << 8) / this.colourTransitionTicks;
         this.blueIncrementPerTick = (targetBlue - this.startBlue - this.deltaBlue / 2 << 8) / this.colourTransitionTicks;
         this.alphaIncrementPerTick = (targetAlpha - this.startAlpha - this.deltaAlpha / 2 << 8) / this.alphaTransitionTicks;
         this.blueIncrementPerTick += this.blueIncrementPerTick <= 0 ? 4 : -4;
         this.redIncrementPerTick += this.redIncrementPerTick <= 0 ? 4 : -4;
         this.greenIncrementPerTick += this.greenIncrementPerTick <= 0 ? 4 : -4;
         this.alphaIncrementPerTick += this.alphaIncrementPerTick <= 0 ? 4 : -4;
      }

   }

   public ParticleEmitterConfig copyWithColourOverride(int minArgb, int maxArgb, int targetArgb) {
      ParticleEmitterConfig c = this.copy();
      c.minColourArgb = minArgb;
      c.maxColourArgb = maxArgb;
      c.targetColourArgb = targetArgb;
      c.recomputeColourFields();
      return c;
   }

   public ParticleEmitterConfig copy() {
      ParticleEmitterConfig c = new ParticleEmitterConfig(this.id);
      c.name = this.name;
      c.spreadYawMin = this.spreadYawMin;
      c.spreadYawMax = this.spreadYawMax;
      c.spreadPitchMin = this.spreadPitchMin;
      c.spreadPitchMax = this.spreadPitchMax;
      c.minSpeed = this.minSpeed;
      c.maxSpeed = this.maxSpeed;
      c.targetSpeed = this.targetSpeed;
      c.speedTransitionPercent = this.speedTransitionPercent;
      c.minScale = this.minScale;
      c.maxScale = this.maxScale;
      c.targetScale = this.targetScale;
      c.scaleTransitionPercent = this.scaleTransitionPercent;
      c.minRotation = this.minRotation;
      c.maxRotation = this.maxRotation;
      c.targetRotation = this.targetRotation;
      c.rotationTransitionPercent = this.rotationTransitionPercent;
      c.minColourArgb = this.minColourArgb;
      c.maxColourArgb = this.maxColourArgb;
      c.targetColourArgb = this.targetColourArgb;
      c.colourTransitionPercent = this.colourTransitionPercent;
      c.alphaTransitionPercent = this.alphaTransitionPercent;
      c.minLifetime = this.minLifetime;
      c.maxLifetime = this.maxLifetime;
      c.minSpawnCount = this.minSpawnCount;
      c.maxSpawnCount = this.maxSpawnCount;
      c.initialSpawnCount = this.initialSpawnCount;
      c.emitOnlyBeforeTime = this.emitOnlyBeforeTime;
      c.emissionTimeThreshold = this.emissionTimeThreshold;
      c.emissionCycleDuration = this.emissionCycleDuration;
      c.loopEmission = this.loopEmission;
      c.distanceFalloffType = this.distanceFalloffType;
      c.distanceFalloffStrength = this.distanceFalloffStrength;
      c.upperBoundLevel = this.upperBoundLevel;
      c.lowerBoundLevel = this.lowerBoundLevel;
      c.localEffectorFilter = this.localEffectorFilter != null ? (int[])this.localEffectorFilter.clone() : null;
      c.embeddedEffectors = this.embeddedEffectors != null ? (int[])this.embeddedEffectors.clone() : null;
      c.globalEffectors = this.globalEffectors != null ? (int[])this.globalEffectors.clone() : null;
      c.texture = this.texture;
      c.textureIndex = this.textureIndex;
      c.spriteColumns = this.spriteColumns;
      c.spriteRows = this.spriteRows;
      c.spriteFrameCount = this.spriteFrameCount;
      c.minGraphicsQuality = this.minGraphicsQuality;
      c.uniformColourVariation = this.uniformColourVariation;
      c.forceTextureOnSoftwareRenderer = this.forceTextureOnSoftwareRenderer;
      c.useSceneAmbientLight = this.useSceneAmbientLight;
      c.collidesWithObjects = this.collidesWithObjects;
      c.clipToTerrain = this.clipToTerrain;
      c.localSpace = this.localSpace;
      c.fallbackEmitterType = this.fallbackEmitterType;
      c.postDecode();
      return c;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getSpreadYawMin() {
      return this.spreadYawMin;
   }

   public int getSpreadYawMax() {
      return this.spreadYawMax;
   }

   public int getSpreadPitchMin() {
      return this.spreadPitchMin;
   }

   public int getSpreadPitchMax() {
      return this.spreadPitchMax;
   }

   public int getMinSpeed() {
      return this.minSpeed;
   }

   public int getMaxSpeed() {
      return this.maxSpeed;
   }

   public int getTargetSpeed() {
      return this.targetSpeed;
   }

   public int getSpeedTransitionPercent() {
      return this.speedTransitionPercent;
   }

   public int getMinScale() {
      return this.minScale;
   }

   public int getMaxScale() {
      return this.maxScale;
   }

   public int getTargetScale() {
      return this.targetScale;
   }

   public int getScaleTransitionPercent() {
      return this.scaleTransitionPercent;
   }

   public int getMinRotation() {
      return this.minRotation;
   }

   public int getMaxRotation() {
      return this.maxRotation;
   }

   public int getTargetRotation() {
      return this.targetRotation;
   }

   public int getRotationTransitionPercent() {
      return this.rotationTransitionPercent;
   }

   public int getMinColourArgb() {
      return this.minColourArgb;
   }

   public int getMaxColourArgb() {
      return this.maxColourArgb;
   }

   public int getTargetColourArgb() {
      return this.targetColourArgb;
   }

   public int getColourTransitionPercent() {
      return this.colourTransitionPercent;
   }

   public int getAlphaTransitionPercent() {
      return this.alphaTransitionPercent;
   }

   public int getMinLifetime() {
      return this.minLifetime;
   }

   public int getMaxLifetime() {
      return this.maxLifetime;
   }

   public int getMinSpawnCount() {
      return this.minSpawnCount;
   }

   public int getMaxSpawnCount() {
      return this.maxSpawnCount;
   }

   public int getInitialSpawnCount() {
      return this.initialSpawnCount;
   }

   public boolean isEmitOnlyBeforeTime() {
      return this.emitOnlyBeforeTime;
   }

   public int getEmissionTimeThreshold() {
      return this.emissionTimeThreshold;
   }

   public int getEmissionCycleDuration() {
      return this.emissionCycleDuration;
   }

   public boolean isLoopEmission() {
      return this.loopEmission;
   }

   public int getDistanceFalloffType() {
      return this.distanceFalloffType;
   }

   public int getDistanceFalloffStrength() {
      return this.distanceFalloffStrength;
   }

   public int getUpperBoundLevel() {
      return this.upperBoundLevel;
   }

   public int getLowerBoundLevel() {
      return this.lowerBoundLevel;
   }

   public int[] getLocalEffectorFilter() {
      return this.localEffectorFilter;
   }

   public int[] getEmbeddedEffectors() {
      return this.embeddedEffectors;
   }

   public int[] getGlobalEffectors() {
      return this.globalEffectors;
   }

   public String getTexture() {
      return this.texture;
   }

   public int getTextureIndex() {
      return this.textureIndex;
   }

   public int getSpriteColumns() {
      return this.spriteColumns;
   }

   public int getSpriteRows() {
      return this.spriteRows;
   }

   public int getSpriteFrameCount() {
      return this.spriteFrameCount;
   }

   public int getMinGraphicsQuality() {
      return this.minGraphicsQuality;
   }

   public boolean isUniformColourVariation() {
      return this.uniformColourVariation;
   }

   public boolean isForceTextureOnSoftwareRenderer() {
      return this.forceTextureOnSoftwareRenderer;
   }

   public boolean isUseSceneAmbientLight() {
      return this.useSceneAmbientLight;
   }

   public boolean isCollidesWithObjects() {
      return this.collidesWithObjects;
   }

   public boolean isClipToTerrain() {
      return this.clipToTerrain;
   }

   public boolean isLocalSpace() {
      return this.localSpace;
   }

   public int getFallbackEmitterType() {
      return this.fallbackEmitterType;
   }

   public int getStartRed() {
      return this.startRed;
   }

   public int getStartGreen() {
      return this.startGreen;
   }

   public int getStartBlue() {
      return this.startBlue;
   }

   public int getStartAlpha() {
      return this.startAlpha;
   }

   public int getDeltaRed() {
      return this.deltaRed;
   }

   public int getDeltaGreen() {
      return this.deltaGreen;
   }

   public int getDeltaBlue() {
      return this.deltaBlue;
   }

   public int getDeltaAlpha() {
      return this.deltaAlpha;
   }

   public int getSpeedTransitionTicks() {
      return this.speedTransitionTicks;
   }

   public int getScaleTransitionTicks() {
      return this.scaleTransitionTicks;
   }

   public int getColourTransitionTicks() {
      return this.colourTransitionTicks;
   }

   public int getAlphaTransitionTicks() {
      return this.alphaTransitionTicks;
   }

   public int getSpeedIncrementPerTick() {
      return this.speedIncrementPerTick;
   }

   public int getScaleIncrementPerTick() {
      return this.scaleIncrementPerTick;
   }

   public int getRotationTransitionTicks() {
      return this.rotationTransitionTicks;
   }

   public int getRotationIncrementPerTick() {
      return this.rotationIncrementPerTick;
   }

   public int getRedIncrementPerTick() {
      return this.redIncrementPerTick;
   }

   public int getGreenIncrementPerTick() {
      return this.greenIncrementPerTick;
   }

   public int getBlueIncrementPerTick() {
      return this.blueIncrementPerTick;
   }

   public int getAlphaIncrementPerTick() {
      return this.alphaIncrementPerTick;
   }

   public boolean isHasHeightBounds() {
      return this.hasHeightBounds;
   }

   public int getScaledDistanceFalloffStrength() {
      return this.scaledDistanceFalloffStrength;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSpreadYawMin(int spreadYawMin) {
      this.spreadYawMin = spreadYawMin;
   }

   public void setSpreadYawMax(int spreadYawMax) {
      this.spreadYawMax = spreadYawMax;
   }

   public void setSpreadPitchMin(int spreadPitchMin) {
      this.spreadPitchMin = spreadPitchMin;
   }

   public void setSpreadPitchMax(int spreadPitchMax) {
      this.spreadPitchMax = spreadPitchMax;
   }

   public void setMinSpeed(int minSpeed) {
      this.minSpeed = minSpeed;
   }

   public void setMaxSpeed(int maxSpeed) {
      this.maxSpeed = maxSpeed;
   }

   public void setTargetSpeed(int targetSpeed) {
      this.targetSpeed = targetSpeed;
   }

   public void setSpeedTransitionPercent(int speedTransitionPercent) {
      this.speedTransitionPercent = speedTransitionPercent;
   }

   public void setMinScale(int minScale) {
      this.minScale = minScale;
   }

   public void setMaxScale(int maxScale) {
      this.maxScale = maxScale;
   }

   public void setTargetScale(int targetScale) {
      this.targetScale = targetScale;
   }

   public void setScaleTransitionPercent(int scaleTransitionPercent) {
      this.scaleTransitionPercent = scaleTransitionPercent;
   }

   public void setMinRotation(int minRotation) {
      this.minRotation = minRotation;
   }

   public void setMaxRotation(int maxRotation) {
      this.maxRotation = maxRotation;
   }

   public void setTargetRotation(int targetRotation) {
      this.targetRotation = targetRotation;
   }

   public void setRotationTransitionPercent(int rotationTransitionPercent) {
      this.rotationTransitionPercent = rotationTransitionPercent;
   }

   public void setMinColourArgb(int minColourArgb) {
      this.minColourArgb = minColourArgb;
   }

   public void setMaxColourArgb(int maxColourArgb) {
      this.maxColourArgb = maxColourArgb;
   }

   public void setTargetColourArgb(int targetColourArgb) {
      this.targetColourArgb = targetColourArgb;
   }

   public void setColourTransitionPercent(int colourTransitionPercent) {
      this.colourTransitionPercent = colourTransitionPercent;
   }

   public void setAlphaTransitionPercent(int alphaTransitionPercent) {
      this.alphaTransitionPercent = alphaTransitionPercent;
   }

   public void setMinLifetime(int minLifetime) {
      this.minLifetime = minLifetime;
   }

   public void setMaxLifetime(int maxLifetime) {
      this.maxLifetime = maxLifetime;
   }

   public void setMinSpawnCount(int minSpawnCount) {
      this.minSpawnCount = minSpawnCount;
   }

   public void setMaxSpawnCount(int maxSpawnCount) {
      this.maxSpawnCount = maxSpawnCount;
   }

   public void setInitialSpawnCount(int initialSpawnCount) {
      this.initialSpawnCount = initialSpawnCount;
   }

   public void setEmitOnlyBeforeTime(boolean emitOnlyBeforeTime) {
      this.emitOnlyBeforeTime = emitOnlyBeforeTime;
   }

   public void setEmissionTimeThreshold(int emissionTimeThreshold) {
      this.emissionTimeThreshold = emissionTimeThreshold;
   }

   public void setEmissionCycleDuration(int emissionCycleDuration) {
      this.emissionCycleDuration = emissionCycleDuration;
   }

   public void setLoopEmission(boolean loopEmission) {
      this.loopEmission = loopEmission;
   }

   public void setDistanceFalloffType(int distanceFalloffType) {
      this.distanceFalloffType = distanceFalloffType;
   }

   public void setDistanceFalloffStrength(int distanceFalloffStrength) {
      this.distanceFalloffStrength = distanceFalloffStrength;
   }

   public void setUpperBoundLevel(int upperBoundLevel) {
      this.upperBoundLevel = upperBoundLevel;
   }

   public void setLowerBoundLevel(int lowerBoundLevel) {
      this.lowerBoundLevel = lowerBoundLevel;
   }

   public void setLocalEffectorFilter(int[] localEffectorFilter) {
      this.localEffectorFilter = localEffectorFilter;
   }

   public void setEmbeddedEffectors(int[] embeddedEffectors) {
      this.embeddedEffectors = embeddedEffectors;
   }

   public void setGlobalEffectors(int[] globalEffectors) {
      this.globalEffectors = globalEffectors;
   }

   public void setTexture(String texture) {
      this.texture = texture;
   }

   public void setTextureIndex(int textureIndex) {
      this.textureIndex = textureIndex;
   }

   public void setSpriteColumns(int spriteColumns) {
      this.spriteColumns = spriteColumns;
   }

   public void setSpriteRows(int spriteRows) {
      this.spriteRows = spriteRows;
   }

   public void setSpriteFrameCount(int spriteFrameCount) {
      this.spriteFrameCount = spriteFrameCount;
   }

   public void setMinGraphicsQuality(int minGraphicsQuality) {
      this.minGraphicsQuality = minGraphicsQuality;
   }

   public void setUniformColourVariation(boolean uniformColourVariation) {
      this.uniformColourVariation = uniformColourVariation;
   }

   public void setForceTextureOnSoftwareRenderer(boolean forceTextureOnSoftwareRenderer) {
      this.forceTextureOnSoftwareRenderer = forceTextureOnSoftwareRenderer;
   }

   public void setUseSceneAmbientLight(boolean useSceneAmbientLight) {
      this.useSceneAmbientLight = useSceneAmbientLight;
   }

   public void setCollidesWithObjects(boolean collidesWithObjects) {
      this.collidesWithObjects = collidesWithObjects;
   }

   public void setClipToTerrain(boolean clipToTerrain) {
      this.clipToTerrain = clipToTerrain;
   }

   public void setLocalSpace(boolean localSpace) {
      this.localSpace = localSpace;
   }

   public void setFallbackEmitterType(int fallbackEmitterType) {
      this.fallbackEmitterType = fallbackEmitterType;
   }

   public void setStartRed(int startRed) {
      this.startRed = startRed;
   }

   public void setStartGreen(int startGreen) {
      this.startGreen = startGreen;
   }

   public void setStartBlue(int startBlue) {
      this.startBlue = startBlue;
   }

   public void setStartAlpha(int startAlpha) {
      this.startAlpha = startAlpha;
   }

   public void setDeltaRed(int deltaRed) {
      this.deltaRed = deltaRed;
   }

   public void setDeltaGreen(int deltaGreen) {
      this.deltaGreen = deltaGreen;
   }

   public void setDeltaBlue(int deltaBlue) {
      this.deltaBlue = deltaBlue;
   }

   public void setDeltaAlpha(int deltaAlpha) {
      this.deltaAlpha = deltaAlpha;
   }

   public void setSpeedTransitionTicks(int speedTransitionTicks) {
      this.speedTransitionTicks = speedTransitionTicks;
   }

   public void setScaleTransitionTicks(int scaleTransitionTicks) {
      this.scaleTransitionTicks = scaleTransitionTicks;
   }

   public void setColourTransitionTicks(int colourTransitionTicks) {
      this.colourTransitionTicks = colourTransitionTicks;
   }

   public void setAlphaTransitionTicks(int alphaTransitionTicks) {
      this.alphaTransitionTicks = alphaTransitionTicks;
   }

   public void setSpeedIncrementPerTick(int speedIncrementPerTick) {
      this.speedIncrementPerTick = speedIncrementPerTick;
   }

   public void setScaleIncrementPerTick(int scaleIncrementPerTick) {
      this.scaleIncrementPerTick = scaleIncrementPerTick;
   }

   public void setRotationTransitionTicks(int rotationTransitionTicks) {
      this.rotationTransitionTicks = rotationTransitionTicks;
   }

   public void setRotationIncrementPerTick(int rotationIncrementPerTick) {
      this.rotationIncrementPerTick = rotationIncrementPerTick;
   }

   public void setRedIncrementPerTick(int redIncrementPerTick) {
      this.redIncrementPerTick = redIncrementPerTick;
   }

   public void setGreenIncrementPerTick(int greenIncrementPerTick) {
      this.greenIncrementPerTick = greenIncrementPerTick;
   }

   public void setBlueIncrementPerTick(int blueIncrementPerTick) {
      this.blueIncrementPerTick = blueIncrementPerTick;
   }

   public void setAlphaIncrementPerTick(int alphaIncrementPerTick) {
      this.alphaIncrementPerTick = alphaIncrementPerTick;
   }

   public void setHasHeightBounds(boolean hasHeightBounds) {
      this.hasHeightBounds = hasHeightBounds;
   }

   public void setScaledDistanceFalloffStrength(int scaledDistanceFalloffStrength) {
      this.scaledDistanceFalloffStrength = scaledDistanceFalloffStrength;
   }
}
