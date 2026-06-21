package net.runelite.client.plugins.particles.core;

import java.util.Arrays;
import java.util.Objects;
import net.runelite.api.AABB;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Model;
import net.runelite.api.Renderable;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.api.WorldView;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.util.CollisionContext;
import net.runelite.client.plugins.particles.util.EffectorSnapshot;

public class Particle {
   private int positionX;
   private int positionY;
   private int positionZ;
   private short directionX;
   private short directionY;
   private short directionZ;
   private int speed;
   private int colour;
   private int colourFractional;
   private int scale;
   private int rotation;
   private int rotationIncrementPerTick;
   private short initialLifetime;
   private short remainingLife;
   private boolean alive = true;
   private ParticleEmitter emitter;
   private int textureId = -1;
   private final byte priority = 5;
   private int cachedEncodedColour;
   private int lastEncodedArgb = -1;
   private static final int MAX_PLANES = 4;
   private static final int EXT_SIZE = 184;
   private static final int EXT_SCENE_OFFSET = 40;
   private static final int EXT_PLANE_STRIDE = 33856;
   private static final int[] EXT_HEIGHT_CACHE = new int[135424];

   public void init(ParticleEmitter emitter, int x, int y, int z, int dirX, int dirY, int dirZ, int speed, int lifetime, int colour, int scale, int rotation, int targetRotation, int rotationTransitionTicks, int textureId) {
      this.emitter = emitter;
      this.positionX = x << 12;
      this.positionY = y << 12;
      this.positionZ = z << 12;
      this.directionX = (short)dirX;
      this.directionY = (short)dirY;
      this.directionZ = (short)dirZ;
      this.speed = speed;
      this.initialLifetime = this.remainingLife = (short)lifetime;
      this.colour = colour;
      this.scale = scale;
      this.rotation = rotation;
      this.colourFractional = 0;
      this.textureId = textureId;
      this.alive = true;
      if (rotationTransitionTicks > 0) {
         int delta;
         for(delta = targetRotation - rotation; delta > 16384; delta -= 32768) {
         }

         while(delta < -16384) {
            delta += 32768;
         }

         this.rotationIncrementPerTick = delta / rotationTransitionTicks;
      } else {
         this.rotationIncrementPerTick = 0;
      }

   }

   public boolean update(int deltaTime, EffectorSnapshot[] snapshots, int snapshotCount) {
      return this.updateInternal(deltaTime, snapshots, snapshotCount);
   }

   private boolean updateInternal(int deltaTime, EffectorSnapshot[] snapshots, int snapshotCount) {
      if (!this.alive) {
         return false;
      } else {
         this.remainingLife = (short)(this.remainingLife - deltaTime);
         if (this.remainingLife <= 0) {
            this.kill();
            return false;
         } else {
            int worldX = this.positionX >> 12;
            int worldY = this.positionY >> 12;
            int worldZ = this.positionZ >> 12;
            ParticleEmitterConfig config = this.emitter.getConfig();
            int elapsedTime;
            if (config.getTargetColourArgb() != 0) {
               elapsedTime = this.initialLifetime - this.remainingLife;
               int redAccumulator;
               if (elapsedTime <= config.getColourTransitionTicks()) {
                  redAccumulator = (this.colour >> 8 & '\uff00') + (this.colourFractional >> 16 & 255) + config.getRedIncrementPerTick() * deltaTime;
                  int greenAccumulator = (this.colour & '\uff00') + (this.colourFractional >> 8 & 255) + config.getGreenIncrementPerTick() * deltaTime;
                  int blueAccumulator = (this.colour << 8 & '\uff00') + (this.colourFractional & 255) + config.getBlueIncrementPerTick() * deltaTime;
                  if (redAccumulator < 0) {
                     redAccumulator = 0;
                  } else if (redAccumulator > 65535) {
                     redAccumulator = 65535;
                  }

                  if (greenAccumulator < 0) {
                     greenAccumulator = 0;
                  } else if (greenAccumulator > 65535) {
                     greenAccumulator = 65535;
                  }

                  if (blueAccumulator < 0) {
                     blueAccumulator = 0;
                  } else if (blueAccumulator > 65535) {
                     blueAccumulator = 65535;
                  }

                  this.colour &= -16777216;
                  this.colour |= ((redAccumulator & '\uff00') << 8) + (greenAccumulator & '\uff00') + ((blueAccumulator & '\uff00') >> 8);
                  this.colourFractional &= -16777216;
                  this.colourFractional |= ((redAccumulator & 255) << 16) + ((greenAccumulator & 255) << 8) + (blueAccumulator & 255);
               }

               if (elapsedTime <= config.getAlphaTransitionTicks()) {
                  redAccumulator = (this.colour >> 16 & '\uff00') + (this.colourFractional >> 24 & 255) + config.getAlphaIncrementPerTick() * deltaTime;
                  if (redAccumulator < 0) {
                     redAccumulator = 0;
                  } else if (redAccumulator > 65535) {
                     redAccumulator = 65535;
                  }

                  this.colour &= 16777215;
                  this.colour |= (redAccumulator & '\uff00') << 16;
                  this.colourFractional &= 16777215;
                  this.colourFractional |= (redAccumulator & 255) << 24;
               }
            }

            elapsedTime = this.initialLifetime - this.remainingLife;
            if (config.getTargetSpeed() != -1 && elapsedTime <= config.getSpeedTransitionTicks()) {
               this.speed += config.getSpeedIncrementPerTick() * deltaTime;
            }

            if (config.getTargetScale() != -1 && elapsedTime <= config.getScaleTransitionTicks()) {
               this.scale += config.getScaleIncrementPerTick() * deltaTime;
            }

            if (elapsedTime <= config.getRotationTransitionTicks()) {
               this.rotation += this.rotationIncrementPerTick * deltaTime;
            }

            double velocityX = (double)this.directionX;
            double velocityY = (double)this.directionY;
            double velocityZ = (double)this.directionZ;
            boolean velocityModified = false;
            int distanceFalloffType = config.getDistanceFalloffType();
            int i;
            if (distanceFalloffType != 0) {
               i = worldX - this.emitter.getWorldX();
               int deltaY = worldY - this.emitter.getWorldY();
               int deltaZ = worldZ - this.emitter.getWorldZ();
               int distanceSquared = i * i + deltaY * deltaY + deltaZ * deltaZ;
               if (distanceFalloffType == 1) {
                  double invDistance = distanceSquared > 0 ? fastInvSqrt((double)distanceSquared) : 1.0;
                  int distance = (int)(1.0 / invDistance) >> 2;
                  long falloffAmount = (long)config.getScaledDistanceFalloffStrength() * (long)distance * (long)deltaTime;
                  this.speed = (int)((long)this.speed - ((long)this.speed * falloffAmount >> 18));
               } else if (distanceFalloffType == 2) {
                  long falloffAmount = (long)config.getScaledDistanceFalloffStrength() * (long)distanceSquared * (long)deltaTime;
                  this.speed = (int)((long)this.speed - ((long)this.speed * falloffAmount >> 28));
               }
            }

            if (snapshots != null && snapshotCount > 0) {
               for(i = 0; i < snapshotCount; ++i) {
                  EffectorSnapshot eff = snapshots[i];
                  if (eff != null) {
                     float deltaX = (float)(worldX - eff.worldX);
                     float deltaY = (float)(worldY - eff.worldY);
                     float deltaZ = (float)(worldZ - eff.worldZ);
                     float distanceSquared = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                     if (!(distanceSquared > (float)eff.maxRangeSquared)) {
                        float invDistance = distanceSquared > 0.0F ? fastInvSqrtF(distanceSquared) : 1.0F;
                        float dotProduct = (deltaX * (float)eff.forceX + deltaY * (float)eff.forceY + deltaZ * (float)eff.forceZ) * eff.coneCheckMultiplier * invDistance;
                        if (!(dotProduct < (float)eff.coneAngleCosine)) {
                           float falloffValue = 0.0F;
                           if (eff.falloffType == 1) {
                              falloffValue = 1.0F / invDistance * 0.0625F * (float)eff.falloffRate;
                           } else if (eff.falloffType == 2) {
                              falloffValue = distanceSquared * 0.00390625F * (float)eff.falloffRate;
                           }

                           float fx;
                           float fy;
                           float fz;
                           if (!eff.radialMode) {
                              fx = ((float)eff.forceX - falloffValue) * (float)deltaTime;
                              fy = ((float)eff.forceY - falloffValue) * (float)deltaTime;
                              fz = ((float)eff.forceZ - falloffValue) * (float)deltaTime;
                              if (!eff.directPositionMode) {
                                 velocityX += (double)fx;
                                 velocityY += (double)fy;
                                 velocityZ += (double)fz;
                                 velocityModified = true;
                              } else {
                                 this.positionX = (int)((float)this.positionX + fx);
                                 this.positionY = (int)((float)this.positionY + fy);
                                 this.positionZ = (int)((float)this.positionZ + fz);
                              }
                           } else {
                              fx = invDistance * (float)eff.forceMagnitude;
                              fy = deltaX * fx * (float)deltaTime;
                              fz = deltaY * fx * (float)deltaTime;
                              float fz = deltaZ * fx * (float)deltaTime;
                              if (!eff.directPositionMode) {
                                 velocityX += (double)fy;
                                 velocityY += (double)fz;
                                 velocityZ += (double)fz;
                                 velocityModified = true;
                              } else {
                                 this.positionX = (int)((float)this.positionX + fy);
                                 this.positionY = (int)((float)this.positionY + fz);
                                 this.positionZ = (int)((float)this.positionZ + fz);
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (velocityModified) {
               while(true) {
                  if (!(velocityX > 32767.0) && !(velocityY > 32767.0) && !(velocityZ > 32767.0) && !(velocityX < -32767.0) && !(velocityY < -32767.0) && !(velocityZ < -32767.0)) {
                     this.directionX = (short)((int)velocityX);
                     this.directionY = (short)((int)velocityY);
                     this.directionZ = (short)((int)velocityZ);
                     break;
                  }

                  velocityX /= 2.0;
                  velocityY /= 2.0;
                  velocityZ /= 2.0;
                  this.speed <<= 1;
               }
            }

            this.positionX = (int)((long)this.positionX + ((long)this.directionX * (long)(this.speed << 2) >> 23) * (long)deltaTime);
            this.positionY = (int)((long)this.positionY + ((long)this.directionY * (long)(this.speed << 2) >> 23) * (long)deltaTime);
            this.positionZ = (int)((long)this.positionZ + ((long)this.directionZ * (long)(this.speed << 2) >> 23) * (long)deltaTime);
            return this.alive;
         }
      }
   }

   public void checkAliveStatus(CollisionContext ctx) {
      if (this.alive) {
         if (!ctx.valid || this.shouldKillParticle(ctx, this.positionX, this.positionY, this.positionZ)) {
            this.kill();
         }

      }
   }

   public static void clearExtendedHeightCache() {
      Arrays.fill(EXT_HEIGHT_CACHE, 0, 33856, Integer.MIN_VALUE);
   }

   public static void populateExtendedHeightCache(WorldView worldView) {
      if (worldView != null && worldView.isTopLevel()) {
         int limit = true;

         for(int tileX = -40; tileX < 144; ++tileX) {
            for(int tileZ = -40; tileZ < 144; ++tileZ) {
               if (tileX < 0 || tileX >= 104 || tileZ < 0 || tileZ >= 104) {
                  int tileBase = (tileX + 40) * 184 + tileZ + 40;
                  if (EXT_HEIGHT_CACHE[tileBase] == Integer.MIN_VALUE) {
                     int cornerX = tileX << 7;
                     int cornerZ = tileZ << 7;
                     EXT_HEIGHT_CACHE[tileBase] = worldView.getTileHeight(cornerX, cornerZ, 0);
                     EXT_HEIGHT_CACHE['葀' + tileBase] = worldView.getTileHeight(cornerX, cornerZ, 1);
                     EXT_HEIGHT_CACHE[67712 + tileBase] = worldView.getTileHeight(cornerX, cornerZ, 2);
                     EXT_HEIGHT_CACHE[101568 + tileBase] = worldView.getTileHeight(cornerX, cornerZ, 3);
                  }
               }
            }
         }

      }
   }

   private boolean shouldKillParticle(CollisionContext ctx, int posX, int posY, int posZ) {
      int localX = posX >> 12;
      int localZ = posZ >> 12;
      int y = posY >> 12;
      int tileX = localX >> 7;
      int tileZ = localZ >> 7;
      if (tileX >= -ctx.sceneOffset && tileX < ctx.tilesX + ctx.sceneOffset && tileZ >= -ctx.sceneOffset && tileZ < ctx.tilesZ + ctx.sceneOffset) {
         if (tileX >= 0 && tileZ >= 0 && tileX < ctx.tilesX && tileZ < ctx.tilesZ) {
            if (!ctx.needsAnyCheck) {
               return false;
            } else {
               Tile[][][] tiles = ctx.tiles;
               int[][][] tileHeights = ctx.tileHeights;
               int plane = ctx.plane;
               Tile baseTile = tiles[plane][tileX][tileZ];
               int renderPlane = plane;
               if (baseTile != null) {
                  renderPlane = baseTile.getRenderLevel();
               }

               int h0 = tileHeights[0][tileX][tileZ];
               int h1 = tileHeights[1][tileX][tileZ];
               int h2 = tileHeights[2][tileX][tileZ];
               int h3 = tileHeights[3][tileX][tileZ];
               int i;
               int wallOrientationA;
               int wallX;
               int wallZ;
               int objZ;
               if (ctx.hasHeightBounds) {
                  int hHere = renderPlane == 0 ? h0 : (renderPlane == 1 ? h1 : (renderPlane == 2 ? h2 : h3));
                  int hBelow = renderPlane < 3 ? (renderPlane == 0 ? h1 : (renderPlane == 1 ? h2 : h3)) : hHere - 1024;
                  if (ctx.upperBoundLevel == -1 && y > hHere) {
                     return true;
                  }

                  int templateFloorHeight;
                  if (ctx.upperBoundLevel >= 0 && ctx.upperBoundLevel < 3) {
                     templateFloorHeight = h0 - h1;
                     if (templateFloorHeight <= 0) {
                        templateFloorHeight = h0 - hBelow;
                     }

                     i = 0;

                     for(wallOrientationA = 0; wallOrientationA <= ctx.upperBoundLevel; ++wallOrientationA) {
                        wallX = wallOrientationA == 0 ? h0 : (wallOrientationA == 1 ? h1 : (wallOrientationA == 2 ? h2 : h3));
                        wallZ = wallOrientationA == 0 ? h1 : (wallOrientationA == 1 ? h2 : h3);
                        objZ = wallX - wallZ;
                        i += objZ > 0 ? objZ : templateFloorHeight;
                     }

                     if (y < h0 - i) {
                        return true;
                     }
                  }

                  if (ctx.lowerBoundLevel == -1 && y < hBelow) {
                     return true;
                  }

                  if (ctx.lowerBoundLevel >= 0) {
                     if (ctx.lowerBoundLevel == 0) {
                        if (y > h0) {
                           return true;
                        }
                     } else {
                        templateFloorHeight = h0 - h1;
                        if (templateFloorHeight <= 0) {
                           templateFloorHeight = h0 - hBelow;
                        }

                        i = 0;

                        for(wallOrientationA = 0; wallOrientationA < ctx.lowerBoundLevel && wallOrientationA < 3; ++wallOrientationA) {
                           wallX = wallOrientationA == 0 ? h0 : (wallOrientationA == 1 ? h1 : (wallOrientationA == 2 ? h2 : h3));
                           wallZ = wallOrientationA == 0 ? h1 : (wallOrientationA == 1 ? h2 : h3);
                           objZ = wallX - wallZ;
                           i += objZ > 0 ? objZ : templateFloorHeight;
                        }

                        if (y > h0 - i) {
                           return true;
                        }
                     }
                  }
               }

               if (!ctx.needsPlaneDetermination) {
                  return false;
               } else {
                  byte actualPlane;
                  if (y <= h3) {
                     actualPlane = 3;
                  } else if (y <= h2) {
                     actualPlane = 2;
                  } else if (y <= h1) {
                     actualPlane = 1;
                  } else {
                     actualPlane = 0;
                  }

                  if (ctx.clipToTerrain && actualPlane == 0 && y > h0) {
                     return true;
                  } else if (actualPlane == 3 && h3 - y > 1024) {
                     return true;
                  } else {
                     Tile actualTile = tiles[actualPlane][tileX][tileZ];
                     if (actualPlane == 3 && actualTile != null && actualTile.getBridge() != null) {
                        return true;
                     } else {
                        if (ctx.collidesWithObjects && actualTile != null) {
                           GameObject[] gameObjects = actualTile.getGameObjects();
                           if (gameObjects != null) {
                              i = 0;

                              for(wallOrientationA = gameObjects.length; i < wallOrientationA; ++i) {
                                 GameObject obj = gameObjects[i];
                                 if (obj != null && this.checkAABBCollision(obj.getRenderable(), obj.getX(), obj.getZ(), obj.getY(), obj.getOrientation(), localX, y, localZ)) {
                                    return true;
                                 }
                              }
                           }

                           WallObject wallObject = actualTile.getWallObject();
                           if (wallObject != null) {
                              wallOrientationA = wallObject.getOrientationA();
                              wallX = wallObject.getX();
                              wallZ = wallObject.getY();
                              int offset = 112;
                              if ((wallOrientationA & 1) != 0) {
                                 wallX -= offset;
                              } else if ((wallOrientationA & 4) != 0) {
                                 wallX += offset;
                              }

                              if ((wallOrientationA & 2) != 0) {
                                 wallZ += offset;
                              } else if ((wallOrientationA & 8) != 0) {
                                 wallZ -= offset;
                              }

                              if (this.checkAABBCollision(wallObject.getRenderable1(), wallX, wallObject.getZ(), wallZ, 0, localX, y, localZ) || this.checkAABBCollision(wallObject.getRenderable2(), wallX, wallObject.getZ(), wallZ, 0, localX, y, localZ)) {
                                 return true;
                              }

                              int wallOrientationB = wallObject.getOrientationB();
                              wallX = wallObject.getX();
                              wallZ = wallObject.getY();
                              if ((wallOrientationB & 1) != 0) {
                                 wallX -= offset;
                              } else if ((wallOrientationB & 4) != 0) {
                                 wallX += offset;
                              }

                              if ((wallOrientationB & 2) != 0) {
                                 wallZ += offset;
                              } else if ((wallOrientationB & 8) != 0) {
                                 wallZ -= offset;
                              }

                              if (this.checkAABBCollision(wallObject.getRenderable2(), wallX, wallObject.getZ(), wallZ, 0, localX, y, localZ)) {
                                 return true;
                              }
                           }

                           DecorativeObject decorativeObject = actualTile.getDecorativeObject();
                           if (decorativeObject != null) {
                              wallX = decorativeObject.getConfig() >>> 6 & 3;
                              wallZ = decorativeObject.getX() + decorativeObject.getXOffset();
                              objZ = decorativeObject.getY() + decorativeObject.getYOffset();
                              if (this.checkAABBCollision(decorativeObject.getRenderable(), wallZ, decorativeObject.getZ(), objZ, wallX, localX, y, localZ) || this.checkAABBCollision(decorativeObject.getRenderable2(), wallZ, decorativeObject.getZ(), objZ, wallX, localX, y, localZ)) {
                                 return true;
                              }
                           }

                           GroundObject groundObject = actualTile.getGroundObject();
                           if (groundObject != null) {
                              wallZ = groundObject.getConfig() >>> 6 & 3;
                              return this.checkAABBCollision(groundObject.getRenderable(), groundObject.getX(), groundObject.getZ(), groundObject.getY(), wallZ, localX, y, localZ);
                           }
                        }

                        return false;
                     }
                  }
               }
            }
         } else {
            return ctx.needsAnyCheck && ctx.worldView != null ? this.shouldKillExtendedParticle(ctx, localX, localZ, y) : false;
         }
      } else {
         return true;
      }
   }

   private boolean shouldKillExtendedParticle(CollisionContext ctx, int localX, int localZ, int y) {
      int plane = ctx.plane;
      int tileX = localX >> 7;
      int tileZ = localZ >> 7;
      int tileBase = (tileX + 40) * 184 + tileZ + 40;
      if (EXT_HEIGHT_CACHE[tileBase] == Integer.MIN_VALUE) {
         return false;
      } else {
         int ph0 = EXT_HEIGHT_CACHE[tileBase];
         int ph1 = EXT_HEIGHT_CACHE['葀' + tileBase];
         int ph2 = EXT_HEIGHT_CACHE[67712 + tileBase];
         int ph3 = EXT_HEIGHT_CACHE[101568 + tileBase];
         int hHere = plane == 0 ? ph0 : (plane == 1 ? ph1 : (plane == 2 ? ph2 : ph3));
         int hBelow = plane < 3 ? (plane == 0 ? ph1 : (plane == 1 ? ph2 : ph3)) : hHere - 1024;
         if (ctx.hasHeightBounds) {
            if (ctx.upperBoundLevel == -1 && y > hHere) {
               return true;
            }

            int templateFloorHeight;
            int cumulativeHeight;
            int p;
            int pHeight;
            int p1Height;
            int floorHeight;
            if (ctx.upperBoundLevel >= 0 && ctx.upperBoundLevel < 3) {
               templateFloorHeight = ph0 - ph1;
               if (templateFloorHeight <= 0) {
                  templateFloorHeight = ph0 - hBelow;
               }

               cumulativeHeight = 0;

               for(p = 0; p <= ctx.upperBoundLevel; ++p) {
                  pHeight = p == 0 ? ph0 : (p == 1 ? ph1 : (p == 2 ? ph2 : ph3));
                  p1Height = p == 0 ? ph1 : (p == 1 ? ph2 : ph3);
                  floorHeight = pHeight - p1Height;
                  cumulativeHeight += floorHeight > 0 ? floorHeight : templateFloorHeight;
               }

               if (y < ph0 - cumulativeHeight) {
                  return true;
               }
            }

            if (ctx.lowerBoundLevel == -1 && y < hBelow) {
               return true;
            }

            if (ctx.lowerBoundLevel >= 0) {
               if (ctx.lowerBoundLevel == 0) {
                  if (y > ph0) {
                     return true;
                  }
               } else {
                  templateFloorHeight = ph0 - ph1;
                  if (templateFloorHeight <= 0) {
                     templateFloorHeight = ph0 - hBelow;
                  }

                  cumulativeHeight = 0;

                  for(p = 0; p < ctx.lowerBoundLevel && p < 3; ++p) {
                     pHeight = p == 0 ? ph0 : (p == 1 ? ph1 : (p == 2 ? ph2 : ph3));
                     p1Height = p == 0 ? ph1 : (p == 1 ? ph2 : ph3);
                     floorHeight = pHeight - p1Height;
                     cumulativeHeight += floorHeight > 0 ? floorHeight : templateFloorHeight;
                  }

                  if (y > ph0 - cumulativeHeight) {
                     return true;
                  }
               }
            }
         }

         if (!ctx.needsPlaneDetermination) {
            return false;
         } else {
            byte actualPlane;
            if (y <= ph3) {
               actualPlane = 3;
            } else if (y <= ph2) {
               actualPlane = 2;
            } else if (y <= ph1) {
               actualPlane = 1;
            } else {
               actualPlane = 0;
            }

            if (ctx.clipToTerrain && actualPlane == 0 && y > ph0) {
               return true;
            } else if (actualPlane == 3) {
               return ph3 - y > 1024;
            } else {
               return false;
            }
         }
      }
   }

   private boolean checkAABBCollision(Renderable renderable, int objX, int objY, int objZ, int orientation, int particleX, int particleY, int particleZ) {
      if (!(renderable instanceof Model)) {
         return false;
      } else {
         Model model = (Model)renderable;
         AABB aabb = model.getAABB(orientation);
         int centerX = objX + aabb.getCenterX();
         int centerY = objY + aabb.getCenterY();
         int centerZ = objZ + aabb.getCenterZ();
         int extentX = aabb.getExtremeX();
         int extentY = aabb.getExtremeY();
         int extentZ = aabb.getExtremeZ();
         return particleX >= centerX - extentX && particleX <= centerX + extentX && particleY >= centerY - extentY && particleY <= centerY + extentY && particleZ >= centerZ - extentZ && particleZ <= centerZ + extentZ;
      }
   }

   public void kill() {
      this.alive = false;
   }

   private static double fastInvSqrt(double x) {
      double xhalf = 0.5 * x;
      long i = Double.doubleToRawLongBits(x);
      i = 6910469410427058089L - (i >> 1);
      x = Double.longBitsToDouble(i);
      x *= 1.5 - xhalf * x * x;
      return x;
   }

   private static float fastInvSqrtF(float x) {
      float xhalf = 0.5F * x;
      int i = Float.floatToRawIntBits(x);
      i = 1597463007 - (i >> 1);
      x = Float.intBitsToFloat(i);
      x *= 1.5F - xhalf * x * x;
      return x;
   }

   public int getWorldX() {
      return this.positionX >> 12;
   }

   public void shiftPosition(int dx, int dz) {
      this.positionX += dx << 12;
      this.positionZ += dz << 12;
   }

   public void shiftPosition(int dx, int dy, int dz) {
      this.positionX += dx << 12;
      this.positionY += dy << 12;
      this.positionZ += dz << 12;
   }

   public int getWorldY() {
      return this.positionY >> 12;
   }

   public int getWorldZ() {
      return this.positionZ >> 12;
   }

   public float getRenderScale() {
      return (float)this.scale / 16384.0F;
   }

   public int getRenderColour() {
      return this.colour;
   }

   public int getEncodedColour() {
      if (this.colour != this.lastEncodedArgb) {
         this.lastEncodedArgb = this.colour;
         this.cachedEncodedColour = encodeColorToAbhsl(this.colour);
      }

      return this.cachedEncodedColour;
   }

   private static int encodeColorToAbhsl(int argb) {
      int alpha = argb >> 24 & 255;
      int red = argb >> 16 & 255;
      int green = argb >> 8 & 255;
      int blue = argb & 255;
      int invertedAlpha = 255 - alpha;
      int hsl = rgbToHslFast(red, green, blue);
      return invertedAlpha << 24 | hsl & '\uffff';
   }

   private static int rgbToHslFast(int r, int g, int b) {
      int max = r > g ? Math.max(r, b) : Math.max(g, b);
      int min = r < g ? Math.min(r, b) : Math.min(g, b);
      int lum = max + min >> 2;
      if (lum > 127) {
         lum = 127;
      }

      if (max == min) {
         return lum;
      } else {
         int delta = max - min;
         int sat = lum < 64 ? delta * 7 / (max + min) : delta * 7 / (510 - max - min);
         if (sat > 7) {
            sat = 7;
         }

         int hue;
         if (max == r) {
            hue = (g - b) * 10 / delta;
            if (g < b) {
               hue += 63;
            }
         } else if (max == g) {
            hue = 21 + (b - r) * 10 / delta;
         } else {
            hue = 42 + (r - g) * 10 / delta;
         }

         hue = (hue + 63) % 63;
         if (hue < 0) {
            hue = 0;
         }

         return hue << 10 | sat << 7 | lum;
      }
   }

   public int getAge() {
      return this.initialLifetime - this.remainingLife;
   }

   public int getPositionX() {
      return this.positionX;
   }

   public int getPositionY() {
      return this.positionY;
   }

   public int getPositionZ() {
      return this.positionZ;
   }

   public short getDirectionX() {
      return this.directionX;
   }

   public short getDirectionY() {
      return this.directionY;
   }

   public short getDirectionZ() {
      return this.directionZ;
   }

   public int getSpeed() {
      return this.speed;
   }

   public int getColour() {
      return this.colour;
   }

   public int getColourFractional() {
      return this.colourFractional;
   }

   public int getScale() {
      return this.scale;
   }

   public int getRotation() {
      return this.rotation;
   }

   public int getRotationIncrementPerTick() {
      return this.rotationIncrementPerTick;
   }

   public short getInitialLifetime() {
      return this.initialLifetime;
   }

   public short getRemainingLife() {
      return this.remainingLife;
   }

   public boolean isAlive() {
      return this.alive;
   }

   public ParticleEmitter getEmitter() {
      return this.emitter;
   }

   public int getTextureId() {
      return this.textureId;
   }

   public byte getPriority() {
      Objects.requireNonNull(this);
      return 5;
   }

   public int getCachedEncodedColour() {
      return this.cachedEncodedColour;
   }

   public int getLastEncodedArgb() {
      return this.lastEncodedArgb;
   }

   static {
      Arrays.fill(EXT_HEIGHT_CACHE, 0, 33856, Integer.MIN_VALUE);
   }
}
