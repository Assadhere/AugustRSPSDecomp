package net.runelite.api.widgets;

import java.awt.Rectangle;
import javax.annotation.Nullable;
import net.runelite.api.FontTypeFace;
import net.runelite.api.Point;
import org.jetbrains.annotations.Range;

public interface Widget {
   int getId();

   int getType();

   void setType(int var1);

   int getContentType();

   Widget setContentType(int var1);

   int getClickMask();

   Widget setClickMask(int var1);

   Widget getParent();

   int getParentId();

   @Nullable
   Widget getChild(int var1);

   @Nullable
   Widget[] getChildren();

   void setChildren(Widget[] var1);

   Widget[] getDynamicChildren();

   Widget[] getStaticChildren();

   Widget[] getNestedChildren();

   int getRelativeX();

   /** @deprecated */
   @Deprecated
   void setRelativeX(int var1);

   int getRelativeY();

   /** @deprecated */
   @Deprecated
   void setRelativeY(int var1);

   void setForcedPosition(int var1, int var2);

   String getText();

   Widget setText(String var1);

   int getTextColor();

   Widget setTextColor(int var1);

   int getOpacity();

   Widget setOpacity(int var1);

   String getName();

   Widget setName(String var1);

   int getModelId();

   Widget setModelId(int var1);

   int getModelType();

   Widget setModelType(int var1);

   int getAnimationId();

   Widget setAnimationId(int var1);

   @Range(
   from = 0L,
   to = 2047L
) int getRotationX();

   Widget setRotationX(@Range(
   from = 0L,
   to = 2047L
) int var1);

   @Range(
   from = 0L,
   to = 2047L
) int getRotationY();

   Widget setRotationY(@Range(
   from = 0L,
   to = 2047L
) int var1);

   @Range(
   from = 0L,
   to = 2047L
) int getRotationZ();

   Widget setRotationZ(@Range(
   from = 0L,
   to = 2047L
) int var1);

   int getModelZoom();

   Widget setModelZoom(int var1);

   int getSpriteId();

   boolean getSpriteTiling();

   Widget setSpriteTiling(boolean var1);

   Widget setSpriteId(int var1);

   boolean isHidden();

   boolean isSelfHidden();

   Widget setHidden(boolean var1);

   int getIndex();

   Point getCanvasLocation();

   int getWidth();

   /** @deprecated */
   @Deprecated
   void setWidth(int var1);

   int getHeight();

   /** @deprecated */
   @Deprecated
   void setHeight(int var1);

   Rectangle getBounds();

   int getItemId();

   Widget setItemId(int var1);

   int getItemQuantity();

   Widget setItemQuantity(int var1);

   boolean contains(Point var1);

   int getScrollX();

   Widget setScrollX(int var1);

   int getScrollY();

   Widget setScrollY(int var1);

   int getScrollWidth();

   Widget setScrollWidth(int var1);

   int getScrollHeight();

   Widget setScrollHeight(int var1);

   int getOriginalX();

   Widget setOriginalX(int var1);

   int getOriginalY();

   Widget setOriginalY(int var1);

   Widget setPos(int var1, int var2);

   Widget setPos(int var1, int var2, int var3, int var4);

   int getOriginalHeight();

   Widget setOriginalHeight(int var1);

   int getOriginalWidth();

   Widget setOriginalWidth(int var1);

   Widget setSize(int var1, int var2);

   Widget setSize(int var1, int var2, int var3, int var4);

   @Nullable
   String[] getActions();

   Widget createChild(int var1, int var2);

   Widget createChild(int var1);

   void deleteAllChildren();

   void setAction(int var1, String var2);

   void clearActions();

   void setOnOpListener(Object... var1);

   void setOnDialogAbortListener(Object... var1);

   void setOnKeyListener(Object... var1);

   void setOnMouseOverListener(Object... var1);

   void setOnMouseRepeatListener(Object... var1);

   void setOnMouseLeaveListener(Object... var1);

   void setOnTimerListener(Object... var1);

   void setOnTargetEnterListener(Object... var1);

   void setOnTargetLeaveListener(Object... var1);

   boolean hasListener();

   Widget setHasListener(boolean var1);

   boolean isIf3();

   void revalidate();

   void revalidateScroll();

   Object[] getOnOpListener();

   Object[] getOnKeyListener();

   Object[] getOnLoadListener();

   Object[] getOnInvTransmitListener();

   int getFontId();

   Widget setFontId(int var1);

   int getBorderType();

   void setBorderType(int var1);

   boolean isFlippedVertically();

   void setFlippedVertically(boolean var1);

   boolean isFlippedHorizontally();

   void setFlippedHorizontally(boolean var1);

   boolean getTextShadowed();

   Widget setTextShadowed(boolean var1);

   int getDragDeadZone();

   void setDragDeadZone(int var1);

   int getDragDeadTime();

   void setDragDeadTime(int var1);

   int getItemQuantityMode();

   Widget setItemQuantityMode(int var1);

   int getXPositionMode();

   Widget setXPositionMode(int var1);

   int getYPositionMode();

   Widget setYPositionMode(int var1);

   int getLineHeight();

   Widget setLineHeight(int var1);

   int getXTextAlignment();

   Widget setXTextAlignment(int var1);

   int getYTextAlignment();

   Widget setYTextAlignment(int var1);

   int getWidthMode();

   Widget setWidthMode(int var1);

   int getHeightMode();

   Widget setHeightMode(int var1);

   FontTypeFace getFont();

   boolean isFilled();

   Widget setFilled(boolean var1);

   String getTargetVerb();

   void setTargetVerb(String var1);

   int getTargetPriority();

   void setTargetPriority(int var1);

   boolean getNoClickThrough();

   void setNoClickThrough(boolean var1);

   boolean getNoScrollThrough();

   void setNoScrollThrough(boolean var1);

   int[] getVarTransmitTrigger();

   void setVarTransmitTrigger(int... var1);

   void setOnClickListener(Object... var1);

   void setOnHoldListener(Object... var1);

   void setOnReleaseListener(Object... var1);

   void setOnDragCompleteListener(Object... var1);

   void setOnDragListener(Object... var1);

   void setOnScrollWheelListener(Object... var1);

   Widget getDragParent();

   Widget setDragParent(Widget var1);

   Object[] getOnVarTransmitListener();

   void setOnVarTransmitListener(Object... var1);
}
