package net.runelite.client.plugins.grandexchange;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.ThinProgressBar;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;

public class GrandExchangeOfferSlot extends JPanel {
   private static final int PANEL_HEIGHT = 45;
   private static final String FACE_CARD = "FACE_CARD";
   private static final String DETAILS_CARD = "DETAILS_CARD";
   private static final ImageIcon RIGHT_ARROW_ICON;
   private static final ImageIcon LEFT_ARROW_ICON;
   private final GrandExchangePlugin grandExchangePlugin;
   private final JPanel container = new JPanel();
   private final CardLayout cardLayout = new CardLayout();
   private final JLabel itemIcon = new JLabel();
   private final JLabel itemName = new JLabel();
   private final JLabel offerInfo = new JLabel();
   private final JLabel itemPrice = new JLabel();
   private final JLabel offerSpent = new JLabel();
   private final ThinProgressBar progressBar = new ThinProgressBar();
   private boolean showingFace = true;

   GrandExchangeOfferSlot(GrandExchangePlugin grandExchangePlugin) {
      this.grandExchangePlugin = grandExchangePlugin;
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.setBorder(new EmptyBorder(7, 0, 0, 0));
      MouseListener ml = new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
               GrandExchangeOfferSlot.this.switchPanel();
            }

         }

         public void mouseEntered(MouseEvent mouseEvent) {
            GrandExchangeOfferSlot.this.container.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
         }

         public void mouseExited(MouseEvent mouseEvent) {
            GrandExchangeOfferSlot.this.container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         }
      };
      this.container.setLayout(this.cardLayout);
      this.container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      JPanel faceCard = new JPanel();
      faceCard.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      faceCard.setLayout(new BorderLayout());
      faceCard.addMouseListener(ml);
      this.itemIcon.setVerticalAlignment(0);
      this.itemIcon.setHorizontalAlignment(0);
      this.itemIcon.setPreferredSize(new Dimension(45, 45));
      this.itemName.setForeground(Color.WHITE);
      this.itemName.setVerticalAlignment(3);
      this.itemName.setFont(FontManager.getRunescapeSmallFont());
      this.offerInfo.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.offerInfo.setVerticalAlignment(1);
      this.offerInfo.setFont(FontManager.getRunescapeSmallFont());
      JLabel switchFaceViewIcon = new JLabel();
      switchFaceViewIcon.setIcon(RIGHT_ARROW_ICON);
      switchFaceViewIcon.setVerticalAlignment(0);
      switchFaceViewIcon.setHorizontalAlignment(0);
      switchFaceViewIcon.setPreferredSize(new Dimension(30, 45));
      JPanel offerFaceDetails = new JPanel();
      offerFaceDetails.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      offerFaceDetails.setLayout(new GridLayout(2, 1, 0, 2));
      offerFaceDetails.add(this.itemName);
      offerFaceDetails.add(this.offerInfo);
      faceCard.add(offerFaceDetails, "Center");
      faceCard.add(this.itemIcon, "West");
      faceCard.add(switchFaceViewIcon, "East");
      JPanel detailsCard = new JPanel();
      detailsCard.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      detailsCard.setLayout(new BorderLayout());
      detailsCard.setBorder(new EmptyBorder(0, 15, 0, 0));
      detailsCard.addMouseListener(ml);
      this.itemPrice.setForeground(Color.WHITE);
      this.itemPrice.setVerticalAlignment(3);
      this.itemPrice.setFont(FontManager.getRunescapeSmallFont());
      this.offerSpent.setForeground(Color.WHITE);
      this.offerSpent.setVerticalAlignment(1);
      this.offerSpent.setFont(FontManager.getRunescapeSmallFont());
      JLabel switchDetailsViewIcon = new JLabel();
      switchDetailsViewIcon.setIcon(LEFT_ARROW_ICON);
      switchDetailsViewIcon.setVerticalAlignment(0);
      switchDetailsViewIcon.setHorizontalAlignment(0);
      switchDetailsViewIcon.setPreferredSize(new Dimension(30, 45));
      JPanel offerDetails = new JPanel();
      offerDetails.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      offerDetails.setLayout(new BoxLayout(offerDetails, 3));
      offerDetails.setPreferredSize(new Dimension(0, 45));
      JPanel offerDetailsWrapper = new JPanel();
      offerDetailsWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      offerDetailsWrapper.setLayout(new BoxLayout(offerDetailsWrapper, 3));
      offerDetailsWrapper.add(this.itemPrice);
      offerDetailsWrapper.add(this.offerSpent);
      offerDetails.add(Box.createVerticalGlue());
      offerDetails.add(offerDetailsWrapper);
      offerDetails.add(Box.createVerticalGlue());
      detailsCard.add(offerDetails, "Center");
      detailsCard.add(switchDetailsViewIcon, "East");
      this.container.add(faceCard, "FACE_CARD");
      this.container.add(detailsCard, "DETAILS_CARD");
      this.cardLayout.show(this.container, "FACE_CARD");
      this.add(this.container, "Center");
      this.add(this.progressBar, "South");
   }

   void updateOffer(ItemComposition offerItem, BufferedImage itemImage, @Nullable GrandExchangeOffer newOffer) {
      if (newOffer != null && newOffer.getState() != GrandExchangeOfferState.EMPTY) {
         this.cardLayout.show(this.container, "FACE_CARD");
         this.itemName.setText(offerItem.getMembersName());
         this.itemIcon.setIcon(new ImageIcon(itemImage));
         boolean buying = newOffer.getState() == GrandExchangeOfferState.BOUGHT || newOffer.getState() == GrandExchangeOfferState.BUYING || newOffer.getState() == GrandExchangeOfferState.CANCELLED_BUY;
         String offerState = (buying ? "Bought " : "Sold ") + QuantityFormatter.quantityToRSDecimalStack(newOffer.getQuantitySold()) + " / " + QuantityFormatter.quantityToRSDecimalStack(newOffer.getTotalQuantity());
         this.offerInfo.setText(offerState);
         this.itemPrice.setText(this.htmlLabel("Price each: ", QuantityFormatter.formatNumber((long)newOffer.getPrice())));
         String action = buying ? "Spent: " : "Received: ";
         JLabel var10000 = this.offerSpent;
         String var10003 = QuantityFormatter.formatNumber((long)newOffer.getSpent());
         var10000.setText(this.htmlLabel(action, var10003 + " / " + QuantityFormatter.formatNumber((long)(newOffer.getPrice() * newOffer.getTotalQuantity()))));
         this.progressBar.setForeground(this.getProgressColor(newOffer));
         this.progressBar.setMaximumValue(newOffer.getTotalQuantity());
         this.progressBar.setValue(newOffer.getQuantitySold());
         JPopupMenu popupMenu = new JPopupMenu();
         popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
         JMenuItem openGeLink = new JMenuItem("Open Grand Exchange website");
         openGeLink.addActionListener((e) -> {
            this.grandExchangePlugin.openGeLink(offerItem.getMembersName(), offerItem.getId());
         });
         popupMenu.add(openGeLink);
         Component[] var9 = this.container.getComponents();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Component c = var9[var11];
            if (c instanceof JPanel) {
               JPanel panel = (JPanel)c;
               panel.setToolTipText(this.htmlTooltip((int)this.progressBar.getPercentage() + "%"));
               panel.setComponentPopupMenu(popupMenu);
            }
         }

         this.revalidate();
      }
   }

   private String htmlTooltip(String value) {
      String var10000 = ColorUtil.toHexColor(ColorScheme.LIGHT_GRAY_COLOR);
      return "<html><body style = 'color:" + var10000 + "'>Progress: <span style = 'color:white'>" + value + "</span></body></html>";
   }

   private String htmlLabel(String key, String value) {
      return "<html><body style = 'color:white'>" + key + "<span style = 'color:" + ColorUtil.toHexColor(ColorScheme.LIGHT_GRAY_COLOR) + "'>" + value + "</span></body></html>";
   }

   private void switchPanel() {
      this.showingFace = !this.showingFace;
      this.cardLayout.show(this.container, this.showingFace ? "FACE_CARD" : "DETAILS_CARD");
   }

   private Color getProgressColor(GrandExchangeOffer offer) {
      if (offer.getState() != GrandExchangeOfferState.CANCELLED_BUY && offer.getState() != GrandExchangeOfferState.CANCELLED_SELL) {
         return offer.getQuantitySold() == offer.getTotalQuantity() ? ColorScheme.PROGRESS_COMPLETE_COLOR : ColorScheme.PROGRESS_INPROGRESS_COLOR;
      } else {
         return ColorScheme.PROGRESS_ERROR_COLOR;
      }
   }

   static {
      BufferedImage rightArrow = ImageUtil.alphaOffset(ImageUtil.loadImageResource(GrandExchangeOfferSlot.class, "/util/arrow_right.png"), 0.25F);
      RIGHT_ARROW_ICON = new ImageIcon(rightArrow);
      LEFT_ARROW_ICON = new ImageIcon(ImageUtil.flipImage(rightArrow, true, false));
   }
}
