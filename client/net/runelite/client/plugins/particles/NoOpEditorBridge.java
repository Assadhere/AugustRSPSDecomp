package net.runelite.client.plugins.particles;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.core.ParticleEffector;
import net.runelite.client.ui.PluginPanel;

public class NoOpEditorBridge implements ParticleEditorBridge {
   public boolean isPlacementMode() {
      return false;
   }

   public boolean isPlacingEmitter() {
      return false;
   }

   public ParticleEmitterConfig getEditingEmitterConfig() {
      return null;
   }

   public ParticleEffectorConfig getEditingEffectorConfig() {
      return null;
   }

   public boolean handlePlacementClick(MouseEvent event) {
      return false;
   }

   public boolean handlePlacementMouseMoved(MouseEvent event) {
      return false;
   }

   public boolean isModelEditorActive() {
      return false;
   }

   public boolean handleModelEditorMousePressed(MouseEvent event) {
      return false;
   }

   public boolean handleModelEditorMouseClick(MouseEvent event) {
      return false;
   }

   public boolean handleModelEditorMouseReleased(MouseEvent event) {
      return false;
   }

   public boolean handleModelEditorMouseDragged(MouseEvent event) {
      return false;
   }

   public void handleModelEditorMouseMoved(MouseEvent event) {
   }

   public boolean handleModelEditorKeyPressed(KeyEvent event) {
      return false;
   }

   public void syncModelEditorModeButtons() {
   }

   public ParticleEffector findEffectorAtScreen(int x, int y) {
      return null;
   }

   public void inspectEffectorConfig(ParticleEffectorConfig config) {
   }

   public void onConfigsReloaded() {
   }

   public void updatePlacementButtonState() {
   }

   public void clearModelViewer() {
   }

   public void repositionEditorObjects(int shiftX, int shiftY) {
   }

   public void revalidateEditorObjects() {
   }

   public void resetAll() {
   }

   public void initialize(ParticleEditorBridge.EditorDependencies dependencies) {
   }

   public void cleanup() {
   }

   public PluginPanel getEditorPanel() {
      return null;
   }
}
