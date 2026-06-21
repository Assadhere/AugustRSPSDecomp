package net.runelite.client.ui;

import com.apple.eawt.FullScreenAdapter;
import com.apple.eawt.FullScreenUtilities;
import com.apple.eawt.event.FullScreenEvent;
import java.awt.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OSXFullScreenAdapter extends FullScreenAdapter {
   private static final Logger log = LoggerFactory.getLogger(OSXFullScreenAdapter.class);
   private final Frame frame;

   public void windowEnteredFullScreen(FullScreenEvent e) {
      log.debug("Window entered fullscreen mode--setting extended state to {}", 6);
      this.frame.setExtendedState(6);
   }

   public void windowExitedFullScreen(FullScreenEvent e) {
      log.debug("Window exited fullscreen mode--setting extended state to {}", 0);
      this.frame.setExtendedState(0);
   }

   static void install(Frame frame) {
      FullScreenUtilities.addFullScreenListenerTo(frame, new OSXFullScreenAdapter(frame));
   }

   public OSXFullScreenAdapter(Frame frame) {
      this.frame = frame;
   }
}
