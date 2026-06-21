package net.runelite.client.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import lombok.NonNull;

class TransferableBufferedImage implements Transferable {
   private final @NonNull BufferedImage image;

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (flavor.equals(DataFlavor.imageFlavor)) {
         return this.image;
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }

   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{DataFlavor.imageFlavor};
   }

   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(DataFlavor.imageFlavor);
   }

   public TransferableBufferedImage(@NonNull BufferedImage image) {
      if (image == null) {
         throw new NullPointerException("image is marked non-null but is null");
      } else {
         this.image = image;
      }
   }
}
