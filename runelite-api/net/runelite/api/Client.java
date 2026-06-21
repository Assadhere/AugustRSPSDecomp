package net.runelite.api;

import com.jagex.oldscape.pub.OAuthApi;
import java.awt.Canvas;
import java.awt.Dimension;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.dbtable.DBRowConfig;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetConfigNode;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.worldmap.MapElementConfig;
import net.runelite.api.worldmap.WorldMap;

public interface Client extends OAuthApi, GameEngine {
   int DRAW_2D_ALL = -1;
   int DRAW_2D_NONE = 0;
   int DRAW_2D_OVERHEAD_TEXT = 1;
   int DRAW_2D_OTHERS = 1073741824;

   Callbacks getCallbacks();

   DrawCallbacks getDrawCallbacks();

   void setDrawCallbacks(DrawCallbacks var1);

   String getBuildID();

   int getEnvironment();

   int getBoostedSkillLevel(Skill var1);

   int getRealSkillLevel(Skill var1);

   int getTotalLevel();

   MessageNode addChatMessage(ChatMessageType var1, @Nonnull String var2, String var3, String var4);

   MessageNode addChatMessage(ChatMessageType var1, @Nonnull String var2, String var3, String var4, boolean var5);

   GameState getGameState();

   void setGameState(GameState var1);

   void stopNow();

   @Nullable
   String getLauncherDisplayName();

   /** @deprecated */
   @Deprecated
   String getUsername();

   void setUsername(String var1);

   void setPassword(String var1);

   void setOtp(String var1);

   int getCurrentLoginField();

   int getLoginIndex();

   /** @deprecated */
   @Deprecated
   AccountType getAccountType();

   Canvas getCanvas();

   int getFPS();

   int getCameraX();

   double getCameraFpX();

   int getCameraY();

   double getCameraFpY();

   int getCameraZ();

   double getCameraFpZ();

   int getCameraPitch();

   double getCameraFpPitch();

   int getCameraYaw();

   double getCameraFpYaw();

   int getWorld();

   int getCanvasHeight();

   int getCanvasWidth();

   int getViewportHeight();

   int getViewportWidth();

   int getViewportXOffset();

   int getViewportYOffset();

   int getScale();

   Point getMouseCanvasPosition();

   Player getLocalPlayer();

   @Nullable
   NPC getFollower();

   @Nonnull
   ItemComposition getItemDefinition(int var1);

   @Nullable
   SpritePixels createItemSprite(int var1, int var2, int var3, int var4, int var5, boolean var6, int var7);

   NodeCache getItemModelCache();

   NodeCache getItemSpriteCache();

   @Nullable
   SpritePixels[] getSprites(IndexDataBase var1, int var2, int var3);

   IndexDataBase getIndexSprites();

   IndexDataBase getIndexScripts();

   IndexDataBase getIndexConfig();

   IndexDataBase getIndex(int var1);

   int getMouseCurrentButton();

   boolean isDraggingWidget();

   @Nullable
   Widget getDraggedWidget();

   @Nullable
   Widget getDraggedOnWidget();

   void setDraggedOnWidget(Widget var1);

   int getDragTime();

   int getTopLevelInterfaceId();

   Widget[] getWidgetRoots();

   /** @deprecated */
   @Nullable
   @Deprecated
   Widget getWidget(WidgetInfo var1);

   @Nullable
   Widget getWidget(int var1, int var2);

   @Nullable
   Widget getWidget(int var1);

   int getEnergy();

   int getWeight();

   String[] getPlayerOptions();

   boolean[] getPlayerOptionsPriorities();

   int[] getPlayerMenuTypes();

   World[] getWorldList();

   @Nonnull
   Menu getMenu();

   /** @deprecated */
   @Deprecated
   MenuEntry createMenuEntry(int var1);

   /** @deprecated */
   @Deprecated
   MenuEntry[] getMenuEntries();

   /** @deprecated */
   @Deprecated
   void setMenuEntries(MenuEntry[] var1);

   boolean isMenuOpen();

   boolean isMenuScrollable();

   int getMenuScroll();

   void setMenuScroll(int var1);

   /** @deprecated */
   @Deprecated
   int getMenuX();

   /** @deprecated */
   @Deprecated
   int getMenuY();

   /** @deprecated */
   @Deprecated
   int getMenuHeight();

   /** @deprecated */
   @Deprecated
   int getMenuWidth();

   /** @deprecated */
   @Deprecated
   int getMapAngle();

   boolean isResized();

   int getRevision();

   int[] getVarps();

   int[] getServerVarps();

   Map<Integer, Object> getVarcMap();

   /** @deprecated */
   @Deprecated
   int getVar(int var1);

   int getVarbitValue(int var1);

   int getServerVarbitValue(int var1);

   int getVarpValue(int var1);

   int getServerVarpValue(int var1);

   int getVarcIntValue(int var1);

   String getVarcStrValue(int var1);

   void setVarcStrValue(int var1, String var2);

   void setVarcIntValue(int var1, int var2);

   void setVarbit(int var1, int var2);

   @Nullable
   VarbitComposition getVarbit(int var1);

   int getVarbitValue(int[] var1, int var2);

   void setVarbitValue(int[] var1, int var2, int var3);

   void queueChangedVarp(int var1);

   WidgetNode openInterface(int var1, int var2, int var3);

   void closeInterface(WidgetNode var1, boolean var2);

   HashTable<WidgetConfigNode> getWidgetFlags();

   @Nullable
   WidgetConfigNode getWidgetConfig(Widget var1);

   HashTable<WidgetNode> getComponentTable();

   GrandExchangeOffer[] getGrandExchangeOffers();

   /** @deprecated */
   @Deprecated
   boolean isPrayerActive(Prayer var1);

   int getSkillExperience(Skill var1);

   long getOverallExperience();

   void refreshChat();

   Map<Integer, ChatLineBuffer> getChatLineMap();

   IterableHashTable<MessageNode> getMessages();

   ObjectComposition getObjectDefinition(int var1);

   NPCComposition getNpcDefinition(int var1);

   StructComposition getStructComposition(int var1);

   NodeCache getStructCompositionCache();

   Object[] getDBTableField(int var1, int var2, int var3);

   DBRowConfig getDBRowConfig(int var1);

   List<Integer> getDBRowsByValue(int var1, int var2, int var3, Object var4);

   List<Integer> getDBTableRows(int var1);

   MapElementConfig getMapElementConfig(int var1);

   IndexedSprite[] getMapScene();

   SpritePixels[] getMapDots();

   int getGameCycle();

   SpritePixels[] getMapIcons();

   IndexedSprite[] getModIcons();

   void setModIcons(IndexedSprite[] var1);

   IndexedSprite createIndexedSprite();

   SpritePixels createSpritePixels(int[] var1, int var2, int var3);

   @Nullable
   LocalPoint getLocalDestinationLocation();

   RuneLiteObject createRuneLiteObject();

   void registerRuneLiteObject(RuneLiteObjectController var1);

   void removeRuneLiteObject(RuneLiteObjectController var1);

   boolean isRuneLiteObjectRegistered(RuneLiteObjectController var1);

   @Nullable
   ModelData loadModelData(int var1);

   ModelData loadModelData(byte[] var1);

   ModelData mergeModels(ModelData[] var1, int var2);

   ModelData mergeModels(ModelData... var1);

   Model mergeModels(Model[] var1, int var2);

   Model mergeModels(Model... var1);

   @Nullable
   Model loadModel(int var1);

   @Nullable
   Model loadModel(int var1, short[] var2, short[] var3);

   Animation loadAnimation(int var1);

   int getMusicVolume();

   void setMusicVolume(int var1);

   void playSoundEffect(int var1);

   void playSoundEffect(int var1, int var2, int var3, int var4);

   void playSoundEffect(int var1, int var2, int var3, int var4, int var5);

   void playSoundEffect(int var1, int var2);

   List<MidiRequest> getActiveMidiRequests();

   BufferProvider getBufferProvider();

   int getMouseIdleTicks();

   long getMouseLastPressedMillis();

   int getKeyboardIdleTicks();

   void changeMemoryMode(boolean var1);

   @Nullable
   ItemContainer getItemContainer(InventoryID var1);

   @Nullable
   ItemContainer getItemContainer(int var1);

   HashTable<ItemContainer> getItemContainers();

   int getIntStackSize();

   void setIntStackSize(int var1);

   int[] getIntStack();

   int getObjectStackSize();

   void setObjectStackSize(int var1);

   Object[] getObjectStack();

   int getArraySizes(int var1);

   int[] getArray(int var1);

   Widget getScriptActiveWidget();

   Widget getScriptDotWidget();

   boolean isFriended(String var1, boolean var2);

   @Nullable
   FriendsChatManager getFriendsChatManager();

   FriendContainer getFriendContainer();

   NameableContainer<Ignore> getIgnoreContainer();

   Preferences getPreferences();

   int getCameraYawTarget();

   int getCameraPitchTarget();

   void setCameraYawTarget(int var1);

   void setCameraPitchTarget(int var1);

   void setCameraSpeed(float var1);

   void setCameraMouseButtonMask(int var1);

   void setCameraPitchRelaxerEnabled(boolean var1);

   void setInvertYaw(boolean var1);

   void setInvertPitch(boolean var1);

   /** @deprecated */
   @Deprecated
   RenderOverview getRenderOverview();

   WorldMap getWorldMap();

   boolean isStretchedEnabled();

   void setStretchedEnabled(boolean var1);

   boolean isStretchedFast();

   void setStretchedFast(boolean var1);

   void setStretchedIntegerScaling(boolean var1);

   void setStretchedKeepAspectRatio(boolean var1);

   void setScalingFactor(int var1);

   void invalidateStretching(boolean var1);

   Dimension getStretchedDimensions();

   Dimension getRealDimensions();

   void changeWorld(World var1);

   World createWorld();

   SpritePixels drawInstanceMap(int var1);

   void runScript(Object... var1);

   ScriptEventBuilder createScriptEventBuilder(Object... var1);

   boolean hasHintArrow();

   int getHintArrowType();

   void clearHintArrow();

   void setHintArrow(WorldPoint var1);

   void setHintArrow(LocalPoint var1);

   void setHintArrow(Player var1);

   void setHintArrow(NPC var1);

   WorldPoint getHintArrowPoint();

   Player getHintArrowPlayer();

   NPC getHintArrowNpc();

   IntPredicate getAnimationInterpolationFilter();

   void setAnimationInterpolationFilter(IntPredicate var1);

   int[] getBoostedSkillLevels();

   int[] getRealSkillLevels();

   int[] getSkillExperiences();

   void queueChangedSkill(Skill var1);

   Map<Integer, SpritePixels> getSpriteOverrides();

   Map<Integer, SpritePixels> getWidgetSpriteOverrides();

   void setCompass(SpritePixels var1);

   NodeCache getWidgetSpriteCache();

   int getTickCount();

   void setTickCount(int var1);

   /** @deprecated */
   @Deprecated
   void setInventoryDragDelay(int var1);

   String getWorldHost();

   EnumSet<WorldType> getWorldType();

   int getCameraMode();

   void setCameraMode(int var1);

   double getCameraFocalPointX();

   void setCameraFocalPointX(double var1);

   double getCameraFocalPointY();

   void setCameraFocalPointY(double var1);

   double getCameraFocalPointZ();

   void setCameraFocalPointZ(double var1);

   void setFreeCameraSpeed(int var1);

   /** @deprecated */
   @Deprecated
   int getOculusOrbState();

   /** @deprecated */
   @Deprecated
   void setOculusOrbState(int var1);

   /** @deprecated */
   @Deprecated
   void setOculusOrbNormalSpeed(int var1);

   /** @deprecated */
   @Deprecated
   int getOculusOrbFocalPointX();

   /** @deprecated */
   @Deprecated
   int getOculusOrbFocalPointY();

   void openWorldHopper();

   void hopToWorld(World var1);

   void setSkyboxColor(int var1);

   int getSkyboxColor();

   boolean isGpu();

   void setGpuFlags(int var1);

   void setExpandedMapLoading(int var1);

   int getExpandedMapLoading();

   int get3dZoom();

   int getCenterX();

   int getCenterY();

   TextureProvider getTextureProvider();

   int getRasterizer3D_clipMidX2();

   int getRasterizer3D_clipNegativeMidX();

   int getRasterizer3D_clipNegativeMidY();

   int getRasterizer3D_clipMidY2();

   void checkClickbox(Projection var1, Model var2, int var3, int var4, int var5, int var6, long var7);

   boolean isWidgetSelected();

   void setWidgetSelected(boolean var1);

   @Nullable
   Widget getSelectedWidget();

   @Nullable
   Widget getFocusedInputFieldWidget();

   NodeCache getItemCompositionCache();

   NodeCache getObjectCompositionCache();

   NodeCache getAnimationCache();

   SpritePixels[] getCrossSprites();

   EnumComposition getEnum(int var1);

   void draw2010Menu(int var1);

   void drawOriginalMenu(int var1);

   void resetHealthBarCaches();

   int getItemCount();

   void setAllWidgetsAreOpTargetable(boolean var1);

   void setGeSearchResultCount(int var1);

   void setGeSearchResultIds(short[] var1);

   void setGeSearchResultIndex(int var1);

   void setLoginScreen(SpritePixels var1);

   void setShouldRenderLoginScreenFire(boolean var1);

   boolean isKeyPressed(int var1);

   long[] getCrossWorldMessageIds();

   int getCrossWorldMessageIdsIndex();

   @Nullable
   ClanChannel getClanChannel();

   @Nullable
   ClanChannel getGuestClanChannel();

   @Nullable
   ClanSettings getClanSettings();

   @Nullable
   ClanSettings getGuestClanSettings();

   @Nullable
   ClanChannel getClanChannel(int var1);

   @Nullable
   ClanSettings getClanSettings(int var1);

   void setUnlockedFps(boolean var1);

   void setUnlockedFpsTarget(int var1);

   /** @deprecated */
   @Deprecated
   Deque<AmbientSoundEffect> getAmbientSoundEffects();

   void setIdleTimeout(int var1);

   int getIdleTimeout();

   boolean isMinimapZoom();

   void setMinimapZoom(boolean var1);

   double getMinimapZoom();

   void setMinimapZoom(double var1);

   void setMinimapTileDrawer(TileFunction var1);

   Rasterizer getRasterizer();

   void menuAction(int var1, int var2, MenuAction var3, int var4, int var5, String var6, String var7);

   void sendIfScriptTrigger(int var1, int var2, int var3, Object... var4);

   WorldView getWorldView(int var1);

   WorldView getTopLevelWorldView();

   boolean isCameraShakeDisabled();

   void setCameraShakeDisabled(boolean var1);

   int getDraw2DMask();

   void setDraw2DMask(int var1);

   /** @deprecated */
   @Deprecated
   int[][][] getInstanceTemplateChunks();

   /** @deprecated */
   @Deprecated
   int[][] getXteaKeys();

   /** @deprecated */
   @Deprecated
   boolean isInInstancedRegion();

   /** @deprecated */
   @Deprecated
   int[] getMapRegions();

   /** @deprecated */
   @Deprecated
   default Scene getScene() {
      WorldView wv = this.getTopLevelWorldView();
      return wv == null ? null : wv.getScene();
   }

   /** @deprecated */
   @Deprecated
   default List<Player> getPlayers() {
      WorldView wv = this.getTopLevelWorldView();
      return wv == null ? Collections.emptyList() : (List)wv.players().stream().collect(Collectors.toCollection(ArrayList::new));
   }

   /** @deprecated */
   @Deprecated
   default List<NPC> getNpcs() {
      WorldView wv = this.getTopLevelWorldView();
      return wv == null ? Collections.emptyList() : (List)wv.npcs().stream().collect(Collectors.toCollection(ArrayList::new));
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   default CollisionData[] getCollisionMaps() {
      return this.getTopLevelWorldView().getCollisionMaps();
   }

   /** @deprecated */
   @Deprecated
   default int getPlane() {
      return this.getTopLevelWorldView().getPlane();
   }

   /** @deprecated */
   @Deprecated
   default int[][][] getTileHeights() {
      return this.getTopLevelWorldView().getTileHeights();
   }

   /** @deprecated */
   @Deprecated
   default byte[][][] getTileSettings() {
      return this.getTopLevelWorldView().getTileSettings();
   }

   /** @deprecated */
   @Deprecated
   default int getBaseX() {
      WorldView wv = this.getTopLevelWorldView();
      return wv == null ? 0 : wv.getBaseX();
   }

   /** @deprecated */
   @Deprecated
   default int getBaseY() {
      WorldView wv = this.getTopLevelWorldView();
      return wv == null ? 0 : wv.getBaseY();
   }

   /** @deprecated */
   @Deprecated
   Projectile createProjectile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, @Nullable Actor var11, int var12, int var13);

   Projectile createProjectile(int var1, WorldPoint var2, int var3, @Nullable Actor var4, WorldPoint var5, int var6, @Nullable Actor var7, int var8, int var9, int var10, int var11);

   Deque<Projectile> getProjectiles();

   /** @deprecated */
   @Deprecated
   default Deque<GraphicsObject> getGraphicsObjects() {
      return this.getTopLevelWorldView().getGraphicsObjects();
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   default Tile getSelectedSceneTile() {
      return this.getTopLevelWorldView().getSelectedSceneTile();
   }

   Model applyTransformations(Model var1, @Nullable Animation var2, int var3, @Nullable Animation var4, int var5);

   SceneTilePaint createSceneTilePaint(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7);

   CameraFocusableEntity getCameraFocusEntity();

   @Nonnull
   WorldView findWorldViewFromWorldPoint(WorldPoint var1);

   @Nullable
   FileDescriptor getSocketFD();

   void setDevelopmentInterfaceLocation(String var1);

   void setDevelopmentScriptLocation(String var1);

   void clearDevelopmentScriptCache();

   void clearDevelopmentComponentCache();

   void setDevGameServerHost(String var1);

   void setDevGameServerPort(int var1);
}
