package net.runelite.client.plugins.kourendlibrary;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class KourendLibraryOverlay extends Overlay {
   private static final int MAXIMUM_DISTANCE = 24;
   private final Library library;
   private final Client client;
   private final KourendLibraryConfig config;
   private final KourendLibraryPlugin plugin;

   @Inject
   private KourendLibraryOverlay(Library library, Client client, KourendLibraryConfig config, KourendLibraryPlugin plugin) {
      this.library = library;
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D g) {
      Player player = this.client.getLocalPlayer();
      if (player == null) {
         return null;
      } else {
         WorldPoint playerLoc = player.getWorldLocation();
         if (playerLoc.getRegionID() != 6459) {
            return null;
         } else {
            List<Bookcase> allBookcases = this.library.getBookcasesOnLevel(this.client.getPlane());
            Iterator var5 = allBookcases.iterator();

            while(true) {
               while(true) {
                  LocalPoint localBookcase;
                  Point screenBookcase;
                  boolean bookIsKnown;
                  Book book;
                  Set possible;
                  do {
                     Bookcase bookcase;
                     do {
                        WorldPoint caseLoc;
                        do {
                           do {
                              do {
                                 if (!var5.hasNext()) {
                                    int customerId = this.library.getCustomerId();
                                    if (customerId != -1) {
                                       Iterator var27 = this.plugin.getNpcsToMark().iterator();

                                       while(var27.hasNext()) {
                                          NPC n = (NPC)var27.next();
                                          if (n.getId() == customerId) {
                                             Book b = this.library.getCustomerBook();
                                             boolean doesPlayerContainBook = this.plugin.doesPlayerContainBook(b);
                                             LocalPoint local = n.getLocalLocation();
                                             Polygon poly = Perspective.getCanvasTilePoly(this.client, local);
                                             OverlayUtil.renderPolygon(g, poly, doesPlayerContainBook ? Color.GREEN : Color.WHITE);
                                             Point screen = Perspective.localToCanvas(this.client, local, this.client.getPlane(), n.getLogicalHeight());
                                             if (screen != null) {
                                                g.drawImage(b.getIcon(), screen.getX() - b.getIcon().getWidth() / 2, screen.getY() - b.getIcon().getHeight(), (ImageObserver)null);
                                             }
                                          }
                                       }
                                    }

                                    return null;
                                 }

                                 bookcase = (Bookcase)var5.next();
                                 caseLoc = bookcase.getLocation();
                              } while(Math.abs(playerLoc.getX() - caseLoc.getX()) > 24);
                           } while(Math.abs(playerLoc.getY() - caseLoc.getY()) > 24);

                           localBookcase = LocalPoint.fromWorld(this.client, caseLoc);
                        } while(localBookcase == null);

                        screenBookcase = Perspective.localToCanvas(this.client, localBookcase, caseLoc.getPlane(), 25);
                     } while(screenBookcase == null);

                     bookIsKnown = bookcase.isBookSet();
                     book = bookcase.getBook();
                     possible = bookcase.getPossibleBooks();
                     if (!bookIsKnown && possible.size() == 1) {
                        book = (Book)possible.iterator().next();
                        bookIsKnown = true;
                     }
                  } while(book == Book.VARLAMORE_ENVOY && !this.plugin.showVarlamoreEnvoy());

                  Color color = bookIsKnown ? (book == this.library.getCustomerBook() ? Color.GREEN : Color.ORANGE) : Color.WHITE;
                  if ((!bookIsKnown || book != null) && (this.library.getState() == SolvedState.NO_DATA || book != null || !possible.isEmpty()) && !this.shouldHideOverlayIfDuplicateBook(book)) {
                     Polygon poly = Perspective.getCanvasTilePoly(this.client, localBookcase);
                     if (poly != null) {
                        OverlayUtil.renderPolygon(g, poly, color);
                     }
                  }

                  int height = false;
                  int height;
                  if (bookIsKnown) {
                     if (book != null && !this.shouldHideOverlayIfDuplicateBook(book)) {
                        FontMetrics fm = g.getFontMetrics();
                        Rectangle2D bounds = fm.getStringBounds(book.getShortName(), g);
                        height = (int)bounds.getHeight() + book.getIcon().getHeight() + 6;
                        Point textLoc = new Point((int)((double)screenBookcase.getX() - bounds.getWidth() / 2.0), screenBookcase.getY() - height / 2 + (int)bounds.getHeight());
                        OverlayUtil.renderTextLocation(g, textLoc, book.getShortName(), color);
                        g.drawImage(book.getIcon(), screenBookcase.getX() - book.getIcon().getWidth() / 2, screenBookcase.getY() + height / 2 - book.getIcon().getHeight(), (ImageObserver)null);
                     }
                  } else {
                     int BOOK_ICON_SIZE = true;
                     Book[] books = (Book[])possible.stream().filter(Objects::nonNull).limit(9L).toArray((x$0) -> {
                        return new Book[x$0];
                     });
                     if (books.length > 1 && books.length <= 9) {
                        int cols = (int)Math.ceil(Math.sqrt((double)books.length));
                        int rows = (int)Math.ceil((double)books.length / (double)cols);
                        height = rows * 32;
                        int xbase = screenBookcase.getX() - cols * 32 / 2;
                        int ybase = screenBookcase.getY() - rows * 32 / 2;

                        for(int i = 0; i < books.length; ++i) {
                           int col = i % cols;
                           int row = i / cols;
                           int x = col * 32;
                           int y = row * 32;
                           if (row == rows - 1) {
                              x += 32 * (books.length % rows) / 2;
                           }

                           g.drawImage(books[i].getIcon(), xbase + x, ybase + y, (ImageObserver)null);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean shouldHideOverlayIfDuplicateBook(@Nullable Book book) {
      return this.config.hideDuplicateBook() && book != null && this.plugin.doesPlayerContainBook(book);
   }
}
