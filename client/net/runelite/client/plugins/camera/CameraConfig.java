package net.runelite.client.plugins.camera;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("zoom")
public interface CameraConfig extends Config {
   int OUTER_LIMIT_MIN = -400;
   int OUTER_LIMIT_MAX = 400;
   int INNER_ZOOM_LIMIT = 1400;
   @ConfigSection(
      name = "Mouse settings",
      description = "Mouse settings.",
      position = 10
   )
   String mouseSettingsSection = "mouseSettings";

   @ConfigItem(
      keyName = "inner",
      name = "Expand inner zoom limit",
      description = "Configures whether or not the inner zoom limit is reduced.",
      position = 1
   )
   default boolean innerLimit() {
      return false;
   }

   @Range(
      min = -400,
      max = 400
   )
   @ConfigItem(
      keyName = "outerLimit",
      name = "Expand outer zoom limit",
      description = "Configures how much the outer zoom limit is adjusted.",
      position = 2
   )
   default int outerLimit() {
      return 0;
   }

   @ConfigItem(
      keyName = "relaxCameraPitch",
      name = "Expand pitch limit",
      description = "Relax the camera's upper and lower pitch limits. Allows vertical and horizontal camera movement.",
      position = 3
   )
   default boolean relaxCameraPitch() {
      return false;
   }

   @ConfigItem(
      keyName = "controlFunction",
      name = "Control function",
      description = "Configures the zoom function when control is pressed.",
      position = 4
   )
   default ControlFunction controlFunction() {
      return ControlFunction.NONE;
   }

   @ConfigItem(
      keyName = "ctrlZoomValue",
      name = "Reset zoom position",
      description = "Position of zoom when it is reset.",
      position = 5
   )
   @Range(
      min = -400,
      max = 1400
   )
   default int ctrlZoomValue() {
      return 512;
   }

   @ConfigItem(
      keyName = "zoomIncrement",
      name = "Zoom speed",
      description = "Speed of zoom.",
      position = 6
   )
   default int zoomIncrement() {
      return 25;
   }

   @ConfigItem(
      keyName = "cameraSpeed",
      name = "Camera speed",
      description = "Speed which the camera moves from input.",
      position = 7
   )
   default double cameraSpeed() {
      return 1.0;
   }

   @ConfigItem(
      keyName = "disableCameraShake",
      name = "Disable camera shake",
      description = "Disables camera shake.",
      position = 8
   )
   default boolean disableCameraShake() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickMovesCamera",
      name = "Right-click moves camera",
      description = "Remaps right-click to middle mouse click.",
      position = 7,
      section = "mouseSettings"
   )
   default boolean rightClickMovesCamera() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickObjects",
      name = "Right-click objects",
      description = "Right-clicking objects opens the menu when 'Right-click moves camera' is on.",
      position = 8,
      section = "mouseSettings"
   )
   default boolean rightClickObjects() {
      return true;
   }

   @ConfigItem(
      keyName = "rightClickExamine",
      name = "Right-click examine",
      description = "Right-clicking examinable objects opens the menu when 'Right-click moves camera' is on.",
      position = 9,
      section = "mouseSettings"
   )
   default boolean rightClickExamine() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickMenuBlocksCamera",
      name = "Right-click menu blocks camera",
      description = "Prevents camera movement when 'Right-click moves camera' is on and the right-click menu<br>is opened due to either 'Right-click objects' or 'Right-click examine' being on.",
      position = 10,
      section = "mouseSettings"
   )
   default boolean rightClickMenuBlocksCamera() {
      return true;
   }

   @ConfigItem(
      keyName = "middleClickMenu",
      name = "Middle-button opens menu",
      description = "Remaps middle mouse click to right-click.",
      position = 11,
      section = "mouseSettings"
   )
   default boolean middleClickMenu() {
      return false;
   }

   @ConfigItem(
      keyName = "invertYaw",
      name = "Invert yaw",
      description = "Makes moving the camera horizontally with the mouse backwards.",
      position = 12,
      section = "mouseSettings"
   )
   default boolean invertYaw() {
      return false;
   }

   @ConfigItem(
      keyName = "invertPitch",
      name = "Invert pitch",
      description = "Makes moving the camera vertically with the mouse backwards.",
      position = 13,
      section = "mouseSettings"
   )
   default boolean invertPitch() {
      return false;
   }
}
