package net.runelite.client.plugins.particles.render;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;
import net.runelite.api.Client;
import net.runelite.api.IndexDataBase;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GL42C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleTextureManager {
   private static final Logger log = LoggerFactory.getLogger(ParticleTextureManager.class);
   public static final int TIER_64 = 0;
   public static final int TIER_128 = 1;
   public static final int TIER_256 = 2;
   public static final int TIER_1024 = 3;
   public static final int NUM_TIERS = 4;
   private static final int[] TIER_SIZES = new int[]{64, 128, 256, 1024};
   private static final int[] TIER_MIP_LEVELS = new int[]{4, 5, 6, 8};
   private static final int USER_UPLOAD_BUFFER = 5;
   private final int[] maxTexturesPerTier = new int[4];
   private static final int PARTICLE_INDEX = 10;
   private static final int PARTICLE_GROUP = 32767;
   private static final char[] CHARACTERS = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};
   private final int[] textureArrayIds = new int[4];
   private final int[] textureCounts = new int[4];
   private final ByteBuffer[] uploadBuffers = new ByteBuffer[4];
   private final Map<String, TextureInfo> textureInfoByName = new HashMap();
   private final Map<Integer, TextureInfo> fileIdToTextureInfo = new HashMap();
   private final Map<Integer, String> fileIdToName = new HashMap();
   private final Map<String, Integer> nameToFileId = new HashMap();
   private final List<String> textureNames = new ArrayList();
   private final Set<Integer> attemptedFileIds = new HashSet();
   private final Queue<PendingUpload> pendingUploads = new ConcurrentLinkedQueue();
   private static final int PREVIEW_SIZE = 24;
   private final Map<String, BufferedImage> texturePreviewImages = new HashMap();
   private final Map<String, BufferedImage> customSourceImages = new HashMap();
   private volatile boolean initialized = false;

   private static TextureHeader parseTextureHeader(byte[] data) {
      if (data != null && data.length >= 3) {
         int rows = data[0] & 255;
         int columns = data[1] & 255;
         int frameCount = data[2] & 255;
         int stringStart = 3;
         int terminatorIndex = findStringTerminatorIndex(data, stringStart);
         String name = readString(data, stringStart, terminatorIndex);
         int imageDataOffset = terminatorIndex + 1;
         return new TextureHeader(rows, columns, frameCount, name, imageDataOffset);
      } else {
         return null;
      }
   }

   private static int findStringTerminatorIndex(byte[] data, int startOffset) {
      for(int i = startOffset; i < data.length; ++i) {
         byte value = data[i];
         if (value == 0) {
            return i;
         }
      }

      throw new IllegalStateException("N string terminator.");
   }

   private static String readString(byte[] data, int start, int terminator) {
      StringBuilder sb = new StringBuilder();

      for(int i = start; i < terminator; ++i) {
         int ch = data[i] & 255;
         if (ch == 0) {
            break;
         }

         if (ch >= 128 && ch < 160) {
            char var7 = CHARACTERS[ch - 128];
            if (0 == var7) {
               var7 = '?';
            }

            ch = var7;
         }

         sb.append((char)ch);
      }

      return sb.toString();
   }

   public int[] scanCacheForTierCounts(Client client) {
      int[] counts = new int[4];
      IndexDataBase index = client.getIndex(10);
      if (index == null) {
         log.warn("Could not get particle index for tier scan");
         return counts;
      } else {
         int[] fileIds = index.getFileIds(32767);
         if (fileIds != null && fileIds.length != 0) {
            int[] var5 = fileIds;
            int var6 = fileIds.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               int fileId = var5[var7];
               byte[] data = index.loadData(32767, fileId);
               TextureHeader header = parseTextureHeader(data);
               if (header != null && header.isValid()) {
                  try {
                     InputStream is = new ByteArrayInputStream(data, header.getImageDataOffset(), data.length - header.getImageDataOffset());

                     try {
                        BufferedImage image = ImageIO.read(is);
                        if (image != null) {
                           int tier = this.selectTier(image.getWidth(), image.getHeight());
                           int var10002 = counts[tier]++;
                        }
                     } catch (Throwable var15) {
                        try {
                           ((InputStream)is).close();
                        } catch (Throwable var14) {
                           var15.addSuppressed(var14);
                        }

                        throw var15;
                     }

                     ((InputStream)is).close();
                  } catch (IOException var16) {
                  }
               }
            }

            log.info("Cache texture scan: tier64={}, tier128={}, tier256={}, tier1024={}", new Object[]{counts[0], counts[1], counts[2], counts[3]});
            return counts;
         } else {
            return counts;
         }
      }
   }

   public static int computeDataHash(Client client) {
      IndexDataBase index = client.getIndex(10);
      if (index == null) {
         return 0;
      } else {
         int[] fileIds = index.getFileIds(32767);
         if (fileIds != null && fileIds.length != 0) {
            int hash = fileIds.length;
            int[] var4 = fileIds;
            int var5 = fileIds.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               int fileId = var4[var6];
               byte[] data = index.loadData(32767, fileId);
               if (data != null) {
                  hash = hash * 31 + Arrays.hashCode(data);
               }
            }

            return hash;
         } else {
            return 0;
         }
      }
   }

   public void init(int[] tierCounts) {
      if (!this.initialized) {
         for(int tier = 0; tier < 4; ++tier) {
            int cacheCount = tierCounts != null && tier < tierCounts.length ? tierCounts[tier] : 0;
            this.maxTexturesPerTier[tier] = cacheCount + 5;
            if (this.maxTexturesPerTier[tier] < 5) {
               this.maxTexturesPerTier[tier] = 5;
            }

            int size = TIER_SIZES[tier];
            int mipLevels = TIER_MIP_LEVELS[tier];
            this.textureArrayIds[tier] = GL33C.glGenTextures();
            GL33C.glBindTexture(35866, this.textureArrayIds[tier]);
            int maxTextures = this.maxTexturesPerTier[tier];
            if (GL.getCapabilities().glTexStorage3D != 0L) {
               GL42C.glTexStorage3D(35866, mipLevels, 32856, size, size, maxTextures);
            } else {
               int mipSize = size;

               for(int i = 0; i < mipLevels; ++i) {
                  GL33C.glTexImage3D(35866, i, 32856, mipSize, mipSize, maxTextures, 0, 6408, 5121, 0L);
                  mipSize /= 2;
               }
            }

            GL33C.glTexParameteri(35866, 10241, 9987);
            GL33C.glTexParameteri(35866, 10240, 9729);
            GL33C.glTexParameteri(35866, 10242, 33071);
            GL33C.glTexParameteri(35866, 10243, 33071);
            this.textureCounts[tier] = 0;
         }

         this.initialized = true;
         log.info("Particle texture arrays allocated: tier64={}, tier128={}, tier256={}, tier1024={} (includes {} user slots each)", new Object[]{this.maxTexturesPerTier[0], this.maxTexturesPerTier[1], this.maxTexturesPerTier[2], this.maxTexturesPerTier[3], 5});
      }
   }

   public void init() {
      this.init((int[])null);
   }

   private int selectTier(int width, int height) {
      int maxDim = Math.max(width, height);
      if (maxDim <= 64) {
         return 0;
      } else if (maxDim <= 128) {
         return 1;
      } else {
         return maxDim <= 256 ? 2 : 3;
      }
   }

   public TextureInfo loadTextureById(int fileId, Client client) {
      if (!this.initialized) {
         log.warn("Cannot load texture - not initialized");
         return null;
      } else {
         TextureInfo existing = (TextureInfo)this.fileIdToTextureInfo.get(fileId);
         if (existing != null) {
            return existing;
         } else if (this.attemptedFileIds.contains(fileId)) {
            log.debug("Texture {} already attempted and failed, skipping", fileId);
            return null;
         } else {
            this.attemptedFileIds.add(fileId);
            IndexDataBase index = client.getIndex(10);
            if (index == null) {
               log.warn("Could not get particle index for texture {}", fileId);
               return null;
            } else {
               byte[] data = index.loadData(32767, fileId);
               TextureHeader header = parseTextureHeader(data);
               if (header == null) {
                  log.warn("No data for particle texture {}", fileId);
                  return null;
               } else if (!header.isValid()) {
                  log.warn("Invalid texture metadata for file {}: rows={}, columns={}, frameCount={} (data length={})", new Object[]{fileId, header.getRows(), header.getColumns(), header.getFrameCount(), data.length});
                  return null;
               } else {
                  try {
                     InputStream is = new ByteArrayInputStream(data, header.getImageDataOffset(), data.length - header.getImageDataOffset());

                     Object var22;
                     label80: {
                        String textureName;
                        label79: {
                           TextureInfo var17;
                           try {
                              BufferedImage image = ImageIO.read(is);
                              if (image == null) {
                                 log.warn("Failed to decode texture for file {}", fileId);
                                 var22 = null;
                                 break label80;
                              }

                              int width = image.getWidth();
                              int height = image.getHeight();
                              int tier = this.selectTier(width, height);
                              if (this.textureCounts[tier] >= this.maxTexturesPerTier[tier]) {
                                 log.warn("Tier {} full (max {}), cannot load texture {}", new Object[]{tier, this.maxTexturesPerTier[tier], fileId});
                                 textureName = null;
                                 break label79;
                              }

                              textureName = header.getName() != null && !header.getName().isEmpty() ? header.getName() : String.valueOf(fileId);
                              if (header.getColumns() <= 1 && header.getRows() <= 1) {
                                 this.createPreview(textureName, image);
                              } else {
                                 this.createSpritesheetPreview(textureName, image, header.getColumns(), header.getRows());
                              }

                              int tierSize = TIER_SIZES[tier];
                              byte[] pixels = this.convertImageToRGBA(image, tierSize);
                              int index_in_tier = this.textureCounts[tier];
                              TextureInfo info = new TextureInfo(tier, index_in_tier, header.getRows(), header.getColumns(), header.getFrameCount());
                              this.fileIdToTextureInfo.put(fileId, info);
                              this.fileIdToName.put(fileId, textureName);
                              this.nameToFileId.put(textureName, fileId);
                              this.textureInfoByName.put(textureName, info);
                              this.textureNames.add(textureName);
                              int var10002 = this.textureCounts[tier]++;
                              this.pendingUploads.add(new PendingUpload(fileId, textureName, pixels, width, height, header.getRows(), header.getColumns(), header.getFrameCount(), tier));
                              log.info("Queued texture '{}' (id={}) for upload (tier {} [{}px], image {}x{}, grid {}x{}, {} frames)", new Object[]{textureName, fileId, tier, TIER_SIZES[tier], width, height, header.getColumns(), header.getRows(), header.getFrameCount()});
                              var17 = info;
                           } catch (Throwable var19) {
                              try {
                                 ((InputStream)is).close();
                              } catch (Throwable var18) {
                                 var19.addSuppressed(var18);
                              }

                              throw var19;
                           }

                           ((InputStream)is).close();
                           return var17;
                        }

                        ((InputStream)is).close();
                        return textureName;
                     }

                     ((InputStream)is).close();
                     return (TextureInfo)var22;
                  } catch (IOException var20) {
                     IOException e = var20;
                     log.warn("Failed to load texture {}", fileId, e);
                     return null;
                  }
               }
            }
         }
      }
   }

   public int loadTexture(String name, String resourcePath) {
      if (!this.initialized) {
         log.warn("Cannot load texture - not initialized");
         return -1;
      } else {
         TextureInfo existing = (TextureInfo)this.textureInfoByName.get(name);
         if (existing != null) {
            return existing.getEncodedId();
         } else {
            try {
               InputStream is = this.getClass().getResourceAsStream(resourcePath);

               byte var11;
               label70: {
                  byte var12;
                  label71: {
                     int var6;
                     try {
                        if (is == null) {
                           log.warn("Texture not found: {}", resourcePath);
                           var11 = -1;
                           break label70;
                        }

                        BufferedImage image = ImageIO.read(is);
                        if (image == null) {
                           log.warn("Failed to read texture: {}", resourcePath);
                           var12 = -1;
                           break label71;
                        }

                        var6 = this.loadTexture(name, image);
                     } catch (Throwable var8) {
                        if (is != null) {
                           try {
                              is.close();
                           } catch (Throwable var7) {
                              var8.addSuppressed(var7);
                           }
                        }

                        throw var8;
                     }

                     if (is != null) {
                        is.close();
                     }

                     return var6;
                  }

                  if (is != null) {
                     is.close();
                  }

                  return var12;
               }

               if (is != null) {
                  is.close();
               }

               return var11;
            } catch (IOException var9) {
               IOException e = var9;
               log.warn("Failed to load texture: {}", resourcePath, e);
               return -1;
            }
         }
      }
   }

   public int loadTexture(String name, BufferedImage image) {
      return this.loadTexture(name, image, 1, 1, 1);
   }

   public int loadTexture(String name, BufferedImage image, int columns, int rows, int frameCount) {
      if (!this.initialized) {
         log.warn("Cannot load texture - not initialized");
         return -1;
      } else {
         TextureInfo existing = (TextureInfo)this.textureInfoByName.get(name);
         if (existing != null) {
            return existing.getEncodedId();
         } else {
            int width = image.getWidth();
            int height = image.getHeight();
            int tier = this.selectTier(width, height);
            if (this.textureCounts[tier] >= this.maxTexturesPerTier[tier]) {
               log.warn("Tier {} full (max {}), cannot load texture '{}'", new Object[]{tier, this.maxTexturesPerTier[tier], name});
               return -1;
            } else {
               if (columns <= 1 && rows <= 1) {
                  this.createPreview(name, image);
               } else {
                  this.createSpritesheetPreview(name, image, columns, rows);
               }

               this.customSourceImages.put(name, image);
               int tierSize = TIER_SIZES[tier];
               byte[] pixels = this.convertImageToRGBA(image, tierSize);
               int index = this.textureCounts[tier];
               TextureInfo info = new TextureInfo(tier, index, rows, columns, frameCount);
               this.textureInfoByName.put(name, info);
               this.textureNames.add(name);
               int var10002 = this.textureCounts[tier]++;
               this.pendingUploads.add(new PendingUpload(-1, name, pixels, width, height, rows, columns, frameCount, tier));
               return info.getEncodedId();
            }
         }
      }
   }

   public void uploadPendingTextures() {
      if (this.initialized && !this.pendingUploads.isEmpty()) {
         int[] uploadsThisFrame = new int[4];

         PendingUpload upload;
         int tier;
         while((upload = (PendingUpload)this.pendingUploads.poll()) != null) {
            tier = upload.tier;
            int tierSize = TIER_SIZES[tier];
            int bufferSize = tierSize * tierSize * 4;
            if (this.uploadBuffers[tier] == null || this.uploadBuffers[tier].capacity() < bufferSize) {
               this.uploadBuffers[tier] = ByteBuffer.allocateDirect(bufferSize);
            }

            int index;
            TextureInfo info;
            if (upload.fileId >= 0) {
               info = (TextureInfo)this.fileIdToTextureInfo.get(upload.fileId);
               index = info.getIndexInTier();
            } else {
               info = (TextureInfo)this.textureInfoByName.get(upload.name);
               index = info.getIndexInTier();
            }

            ByteBuffer buffer = this.uploadBuffers[tier];
            buffer.clear();
            buffer.put(upload.pixels);
            buffer.flip();
            GL33C.glBindTexture(35866, this.textureArrayIds[tier]);
            GL33C.glTexSubImage3D(35866, 0, 0, 0, index, tierSize, tierSize, 1, 6408, 5121, buffer);
            int var10002 = uploadsThisFrame[tier]++;
            log.info("Uploaded texture to tier {} [{}px] index {}", new Object[]{tier, tierSize, index});
         }

         for(tier = 0; tier < 4; ++tier) {
            if (uploadsThisFrame[tier] > 0) {
               GL33C.glBindTexture(35866, this.textureArrayIds[tier]);
               GL33C.glGenerateMipmap(35866);
            }
         }

      }
   }

   private byte[] convertImageToRGBA(BufferedImage image, int targetSize) {
      BufferedImage sized = image;
      if (image.getWidth() != targetSize || image.getHeight() != targetSize) {
         sized = new BufferedImage(targetSize, targetSize, 2);
         Graphics2D g = sized.createGraphics();
         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         g.drawImage(image, 0, 0, targetSize, targetSize, (ImageObserver)null);
         g.dispose();
      }

      int[] argbPixels = sized.getRGB(0, 0, targetSize, targetSize, (int[])null, 0, targetSize);
      byte[] pixels = new byte[targetSize * targetSize * 4];
      int idx = 0;
      int[] var7 = argbPixels;
      int var8 = argbPixels.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         int argb = var7[var9];
         pixels[idx++] = (byte)(argb >> 16 & 255);
         pixels[idx++] = (byte)(argb >> 8 & 255);
         pixels[idx++] = (byte)(argb & 255);
         pixels[idx++] = (byte)(argb >> 24 & 255);
      }

      return pixels;
   }

   private void createPreview(String name, BufferedImage image) {
      BufferedImage preview = new BufferedImage(24, 24, 2);
      Graphics2D g = preview.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.drawImage(image, 0, 0, 24, 24, (ImageObserver)null);
      g.dispose();
      this.texturePreviewImages.put(name, preview);
   }

   private void createSpritesheetPreview(String name, BufferedImage image, int columns, int rows) {
      int frameWidth = image.getWidth() / columns;
      int frameHeight = image.getHeight() / rows;
      BufferedImage firstFrame = image.getSubimage(0, 0, frameWidth, frameHeight);
      this.createPreview(name, firstFrame);
   }

   public List<Integer> preloadPreviewsFromCache(Client client) {
      List<Integer> availableIds = new ArrayList();
      IndexDataBase index = client.getIndex(10);
      if (index == null) {
         log.warn("Could not get particle index for preview preload");
         return availableIds;
      } else {
         int[] fileIds;
         try {
            fileIds = index.getFileIds(32767);
         } catch (Exception var16) {
            Exception e = var16;
            log.debug("Cache not ready for texture preview preload: {}", e.getMessage());
            return availableIds;
         }

         if (fileIds != null && fileIds.length != 0) {
            int[] var19 = fileIds;
            int var6 = fileIds.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               int fileId = var19[var7];
               byte[] data = index.loadData(32767, fileId);
               TextureHeader header = parseTextureHeader(data);
               if (header != null && header.isValid()) {
                  String name = header.getName() != null && !header.getName().isEmpty() ? header.getName() : String.valueOf(fileId);
                  if (this.texturePreviewImages.containsKey(name)) {
                     availableIds.add(fileId);
                     this.fileIdToName.put(fileId, name);
                     this.nameToFileId.put(name, fileId);
                     if (!this.textureNames.contains(name)) {
                        this.textureNames.add(name);
                     }
                  } else {
                     try {
                        InputStream is = new ByteArrayInputStream(data, header.getImageDataOffset(), data.length - header.getImageDataOffset());

                        try {
                           BufferedImage image = ImageIO.read(is);
                           if (image != null) {
                              if (header.getColumns() <= 1 && header.getRows() <= 1) {
                                 this.createPreview(name, image);
                              } else {
                                 this.createSpritesheetPreview(name, image, header.getColumns(), header.getRows());
                              }

                              availableIds.add(fileId);
                              this.fileIdToName.put(fileId, name);
                              this.nameToFileId.put(name, fileId);
                              if (!this.textureNames.contains(name)) {
                                 this.textureNames.add(name);
                              }
                           }
                        } catch (Throwable var17) {
                           try {
                              ((InputStream)is).close();
                           } catch (Throwable var15) {
                              var17.addSuppressed(var15);
                           }

                           throw var17;
                        }

                        ((InputStream)is).close();
                     } catch (IOException var18) {
                        log.debug("Failed to create preview for texture {} ({})", name, fileId);
                     }
                  }
               }
            }

            log.info("Preloaded {} texture previews from cache", availableIds.size());
            return availableIds;
         } else {
            return availableIds;
         }
      }
   }

   public List<Integer> getAvailableTextureIds(Client client) {
      List<Integer> ids = new ArrayList();
      IndexDataBase index = client.getIndex(10);
      if (index != null) {
         int[] fileIds = index.getFileIds(32767);
         if (fileIds != null) {
            int[] var5 = fileIds;
            int var6 = fileIds.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               int id = var5[var7];
               ids.add(id);
            }
         }
      }

      return ids;
   }

   public int getTextureArrayId(int tier) {
      return tier >= 0 && tier < 4 ? this.textureArrayIds[tier] : -1;
   }

   public TextureInfo getTextureInfoById(int fileId) {
      return (TextureInfo)this.fileIdToTextureInfo.get(fileId);
   }

   public TextureInfo getTextureInfoByName(String name) {
      return (TextureInfo)this.textureInfoByName.get(name);
   }

   public int getTextureIndex(String name) {
      TextureInfo info = (TextureInfo)this.textureInfoByName.get(name);
      return info != null ? info.getEncodedId() : -1;
   }

   public int getTextureIndexById(int fileId) {
      TextureInfo info = (TextureInfo)this.fileIdToTextureInfo.get(fileId);
      return info != null ? info.getEncodedId() : -1;
   }

   public String getTextureNameById(int fileId) {
      return (String)this.fileIdToName.get(fileId);
   }

   public Integer getFileIdByName(String name) {
      return (Integer)this.nameToFileId.get(name);
   }

   public List<String> getTextureNames() {
      return Collections.unmodifiableList(this.textureNames);
   }

   public BufferedImage getTexturePreview(String name) {
      return (BufferedImage)this.texturePreviewImages.get(name);
   }

   public TextureInfo getTextureInfo(String name) {
      return (TextureInfo)this.textureInfoByName.get(name);
   }

   public BufferedImage getTextureSourceImage(String name, Client client) {
      BufferedImage customImage = (BufferedImage)this.customSourceImages.get(name);
      if (customImage != null) {
         return customImage;
      } else {
         Integer fileId = (Integer)this.nameToFileId.get(name);
         if (fileId != null && client != null) {
            IndexDataBase index = client.getIndex(10);
            if (index == null) {
               return null;
            } else {
               byte[] data = index.loadData(32767, fileId);
               if (data == null) {
                  return null;
               } else {
                  TextureHeader header = parseTextureHeader(data);
                  if (header != null && header.isValid()) {
                     try {
                        InputStream is = new ByteArrayInputStream(data, header.getImageDataOffset(), data.length - header.getImageDataOffset());

                        BufferedImage var9;
                        try {
                           var9 = ImageIO.read(is);
                        } catch (Throwable var12) {
                           try {
                              ((InputStream)is).close();
                           } catch (Throwable var11) {
                              var12.addSuppressed(var11);
                           }

                           throw var12;
                        }

                        ((InputStream)is).close();
                        return var9;
                     } catch (IOException var13) {
                        IOException e = var13;
                        log.warn("Failed to reload texture {} from cache", name, e);
                        return null;
                     }
                  } else {
                     return null;
                  }
               }
            }
         } else {
            return null;
         }
      }
   }

   public Map<String, CustomTextureData> snapshotCustomTextures() {
      Map<String, CustomTextureData> result = new HashMap();
      Iterator var2 = this.customSourceImages.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<String, BufferedImage> entry = (Map.Entry)var2.next();
         TextureInfo info = (TextureInfo)this.textureInfoByName.get(entry.getKey());
         if (info != null) {
            result.put((String)entry.getKey(), new CustomTextureData((BufferedImage)entry.getValue(), info.getColumns(), info.getRows(), info.getFrameCount()));
         }
      }

      return result;
   }

   public Set<Integer> getLoadedFileIds() {
      return Collections.unmodifiableSet(this.fileIdToTextureInfo.keySet());
   }

   public int getTextureCount() {
      int total = 0;
      int[] var2 = this.textureCounts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int count = var2[var4];
         total += count;
      }

      return total;
   }

   public boolean hasPendingUploads() {
      return !this.pendingUploads.isEmpty();
   }

   public void bind(int tier, int textureUnit) {
      if (this.initialized && tier >= 0 && tier < 4) {
         GL33C.glActiveTexture(textureUnit);
         GL33C.glBindTexture(35866, this.textureArrayIds[tier]);
      }
   }

   public void bindAll(int baseUnit) {
      for(int tier = 0; tier < 4; ++tier) {
         this.bind(tier, baseUnit + tier);
      }

   }

   public void reset() {
      for(int i = 0; i < 4; ++i) {
         this.textureArrayIds[i] = -1;
         this.textureCounts[i] = 0;
         this.uploadBuffers[i] = null;
      }

      this.textureInfoByName.clear();
      this.fileIdToTextureInfo.clear();
      this.fileIdToName.clear();
      this.nameToFileId.clear();
      this.textureNames.clear();
      this.attemptedFileIds.clear();
      this.pendingUploads.clear();
      this.texturePreviewImages.clear();
      this.initialized = false;
      log.debug("Particle texture manager reset");
   }

   public void cleanup() {
      for(int tier = 0; tier < 4; ++tier) {
         if (this.textureArrayIds[tier] != -1) {
            GL33C.glDeleteTextures(this.textureArrayIds[tier]);
         }
      }

      this.reset();
      log.debug("Particle texture manager cleaned up");
   }

   public void expandCapacity(int[] additionalPerTier, Client client) {
      if (!this.initialized) {
         log.warn("Cannot expand capacity - not initialized");
      } else {
         int[] newMaxPerTier = new int[4];
         boolean needsExpansion = false;

         int tier;
         int size;
         for(int tier = 0; tier < 4; ++tier) {
            int additional = additionalPerTier != null && tier < additionalPerTier.length ? additionalPerTier[tier] : 0;
            int currentUsed = this.textureCounts[tier];
            tier = this.maxTexturesPerTier[tier];
            size = currentUsed + additional;
            if (size > tier) {
               newMaxPerTier[tier] = size + 5;
               needsExpansion = true;
            } else {
               newMaxPerTier[tier] = tier;
            }
         }

         if (!needsExpansion) {
            log.info("No texture array expansion needed");
         } else {
            log.info("Expanding texture arrays: tier64 {}→{}, tier128 {}→{}, tier256 {}→{}, tier1024 {}→{}", new Object[]{this.maxTexturesPerTier[0], newMaxPerTier[0], this.maxTexturesPerTier[1], newMaxPerTier[1], this.maxTexturesPerTier[2], newMaxPerTier[2], this.maxTexturesPerTier[3], newMaxPerTier[3]});
            Map<Integer, TextureInfo> savedFileIdToInfo = new HashMap(this.fileIdToTextureInfo);
            Map<Integer, String> savedFileIdToName = new HashMap(this.fileIdToName);
            int[] savedTextureCounts = (int[])this.textureCounts.clone();

            for(tier = 0; tier < 4; ++tier) {
               if (this.textureArrayIds[tier] != -1) {
                  GL33C.glDeleteTextures(this.textureArrayIds[tier]);
               }
            }

            this.fileIdToTextureInfo.clear();
            this.attemptedFileIds.clear();
            this.pendingUploads.clear();

            int fileId;
            for(tier = 0; tier < 4; ++tier) {
               this.maxTexturesPerTier[tier] = newMaxPerTier[tier];
               this.textureCounts[tier] = 0;
               size = TIER_SIZES[tier];
               int mipLevels = TIER_MIP_LEVELS[tier];
               this.textureArrayIds[tier] = GL33C.glGenTextures();
               GL33C.glBindTexture(35866, this.textureArrayIds[tier]);
               fileId = this.maxTexturesPerTier[tier];
               if (GL.getCapabilities().glTexStorage3D != 0L) {
                  GL42C.glTexStorage3D(35866, mipLevels, 32856, size, size, fileId);
               } else {
                  int mipSize = size;

                  for(int i = 0; i < mipLevels; ++i) {
                     GL33C.glTexImage3D(35866, i, 32856, mipSize, mipSize, fileId, 0, 6408, 5121, 0L);
                     mipSize /= 2;
                  }
               }

               GL33C.glTexParameteri(35866, 10241, 9987);
               GL33C.glTexParameteri(35866, 10240, 9729);
               GL33C.glTexParameteri(35866, 10242, 33071);
               GL33C.glTexParameteri(35866, 10243, 33071);
            }

            if (client != null) {
               IndexDataBase index = client.getIndex(10);
               if (index != null) {
                  Iterator var30 = savedFileIdToInfo.entrySet().iterator();

                  label105:
                  while(true) {
                     while(true) {
                        if (!var30.hasNext()) {
                           break label105;
                        }

                        Map.Entry<Integer, TextureInfo> entry = (Map.Entry)var30.next();
                        fileId = (Integer)entry.getKey();
                        TextureInfo oldInfo = (TextureInfo)entry.getValue();
                        String textureName = (String)savedFileIdToName.get(fileId);
                        byte[] data = index.loadData(32767, fileId);
                        TextureHeader header = parseTextureHeader(data);
                        if (header != null && header.isValid()) {
                           try {
                              InputStream is = new ByteArrayInputStream(data, header.getImageDataOffset(), data.length - header.getImageDataOffset());

                              label100: {
                                 try {
                                    BufferedImage image = ImageIO.read(is);
                                    if (image == null) {
                                       break label100;
                                    }

                                    int tier = oldInfo.getTier();
                                    int tierSize = TIER_SIZES[tier];
                                    byte[] pixels = this.convertImageToRGBA(image, tierSize);
                                    int originalIndex = oldInfo.getIndexInTier();
                                    TextureInfo newInfo = new TextureInfo(tier, originalIndex, oldInfo.getRows(), oldInfo.getColumns(), oldInfo.getFrameCount());
                                    this.fileIdToTextureInfo.put(fileId, newInfo);
                                    this.fileIdToName.put(fileId, textureName);
                                    this.nameToFileId.put(textureName, fileId);
                                    this.textureInfoByName.put(textureName, newInfo);
                                    if (this.textureCounts[tier] <= originalIndex) {
                                       this.textureCounts[tier] = originalIndex + 1;
                                    }

                                    this.pendingUploads.add(new PendingUpload(fileId, textureName, pixels, image.getWidth(), image.getHeight(), oldInfo.getRows(), oldInfo.getColumns(), oldInfo.getFrameCount(), tier));
                                 } catch (Throwable var24) {
                                    try {
                                       ((InputStream)is).close();
                                    } catch (Throwable var23) {
                                       var24.addSuppressed(var23);
                                    }

                                    throw var24;
                                 }

                                 ((InputStream)is).close();
                                 continue;
                              }

                              ((InputStream)is).close();
                           } catch (IOException var25) {
                              IOException e = var25;
                              log.warn("Failed to reload texture {} during expansion", fileId, e);
                           }
                        } else {
                           log.warn("Failed to reload texture {} during expansion", fileId);
                        }
                     }
                  }
               }
            }

            for(tier = 0; tier < 4; ++tier) {
               if (savedTextureCounts[tier] > this.textureCounts[tier]) {
                  this.textureCounts[tier] = savedTextureCounts[tier];
               }
            }

            log.info("Texture arrays expanded successfully, restored {} textures", savedFileIdToInfo.size());
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public void expandCapacity(int[] additionalPerTier) {
      this.expandCapacity(additionalPerTier, (Client)null);
   }

   public int[] getCapacityPerTier() {
      return (int[])this.maxTexturesPerTier.clone();
   }

   public int[] getUsagePerTier() {
      return (int[])this.textureCounts.clone();
   }

   public boolean hasCapacity(int[] additionalPerTier) {
      for(int tier = 0; tier < 4; ++tier) {
         int additional = additionalPerTier != null && tier < additionalPerTier.length ? additionalPerTier[tier] : 0;
         if (this.textureCounts[tier] + additional > this.maxTexturesPerTier[tier]) {
            return false;
         }
      }

      return true;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public static class CustomTextureData {
      private final BufferedImage image;
      private final int columns;
      private final int rows;
      private final int frameCount;

      public CustomTextureData(BufferedImage image, int columns, int rows, int frameCount) {
         this.image = image;
         this.columns = columns;
         this.rows = rows;
         this.frameCount = frameCount;
      }

      public BufferedImage getImage() {
         return this.image;
      }

      public int getColumns() {
         return this.columns;
      }

      public int getRows() {
         return this.rows;
      }

      public int getFrameCount() {
         return this.frameCount;
      }
   }

   private static class PendingUpload {
      final int fileId;
      final String name;
      final byte[] pixels;
      final int width;
      final int height;
      final int rows;
      final int columns;
      final int frameCount;
      final int tier;

      PendingUpload(int fileId, String name, byte[] pixels, int width, int height, int rows, int columns, int frameCount, int tier) {
         this.fileId = fileId;
         this.name = name;
         this.pixels = pixels;
         this.width = width;
         this.height = height;
         this.rows = rows;
         this.columns = columns;
         this.frameCount = frameCount;
         this.tier = tier;
      }
   }

   public static class TextureHeader {
      private final int rows;
      private final int columns;
      private final int frameCount;
      private final String name;
      private final int imageDataOffset;

      public TextureHeader(int rows, int columns, int frameCount, String name, int imageDataOffset) {
         this.rows = rows;
         this.columns = columns;
         this.frameCount = frameCount;
         this.name = name;
         this.imageDataOffset = imageDataOffset;
      }

      public boolean isValid() {
         return this.rows > 0 && this.columns > 0 && this.frameCount > 0;
      }

      public int getRows() {
         return this.rows;
      }

      public int getColumns() {
         return this.columns;
      }

      public int getFrameCount() {
         return this.frameCount;
      }

      public String getName() {
         return this.name;
      }

      public int getImageDataOffset() {
         return this.imageDataOffset;
      }
   }

   public static class TextureInfo {
      private final int tier;
      private final int indexInTier;
      private final int rows;
      private final int columns;
      private final int frameCount;

      public TextureInfo(int tier, int indexInTier, int rows, int columns, int frameCount) {
         this.tier = tier;
         this.indexInTier = indexInTier;
         this.rows = rows;
         this.columns = columns;
         this.frameCount = frameCount;
      }

      public int getEncodedId() {
         return this.tier << 14 | this.indexInTier;
      }

      public int getTier() {
         return this.tier;
      }

      public int getIndexInTier() {
         return this.indexInTier;
      }

      public int getRows() {
         return this.rows;
      }

      public int getColumns() {
         return this.columns;
      }

      public int getFrameCount() {
         return this.frameCount;
      }
   }
}
