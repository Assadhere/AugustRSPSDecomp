package net.runelite.client.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.Document;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.SwingUtil;
import org.apache.commons.lang3.StringUtils;

public class IconTextField extends JPanel {
   private final JLabel iconWrapperLabel;
   private final FlatTextField textField;
   private final JButton clearButton;
   private final JButton suggestionButton;
   private final DefaultListModel<String> suggestionListModel;
   private final List<Runnable> clearListeners = new ArrayList();

   public IconTextField() {
      this.setLayout(new BorderLayout());
      this.iconWrapperLabel = new JLabel();
      this.iconWrapperLabel.setPreferredSize(new Dimension(30, 0));
      this.iconWrapperLabel.setVerticalAlignment(0);
      this.iconWrapperLabel.setHorizontalAlignment(0);
      this.textField = new FlatTextField();
      this.textField.setBorder((Border)null);
      JTextField innerTxt = this.textField.getTextField();
      innerTxt.removeMouseListener(innerTxt.getMouseListeners()[innerTxt.getMouseListeners().length - 1]);
      MouseListener hoverEffect = new MouseAdapter() {
         public void mouseEntered(MouseEvent mouseEvent) {
            if (!IconTextField.this.textField.isBlocked()) {
               Color hoverColor = IconTextField.this.textField.getHoverBackgroundColor();
               if (hoverColor != null) {
                  IconTextField.super.setBackground(hoverColor);
                  IconTextField.this.textField.setBackground(hoverColor, false);
               }

            }
         }

         public void mouseExited(MouseEvent mouseEvent) {
            IconTextField.this.setBackground(IconTextField.this.textField.getBackgroundColor());
         }
      };
      this.textField.addMouseListener(hoverEffect);
      innerTxt.addMouseListener(hoverEffect);
      this.clearButton = this.createRHSButton(ColorScheme.PROGRESS_ERROR_COLOR, Color.PINK, FontManager.getRunescapeBoldFont());
      this.clearButton.setText("×");
      this.clearButton.addActionListener((evt) -> {
         this.setText((String)null);
         Iterator var2 = this.clearListeners.iterator();

         while(var2.hasNext()) {
            Runnable l = (Runnable)var2.next();
            l.run();
         }

      });
      this.suggestionListModel = new DefaultListModel();
      this.suggestionListModel.addListDataListener(new ListDataListener() {
         public void intervalAdded(ListDataEvent e) {
            IconTextField.this.updateContextButton();
         }

         public void intervalRemoved(ListDataEvent e) {
            IconTextField.this.updateContextButton();
         }

         public void contentsChanged(ListDataEvent e) {
            IconTextField.this.updateContextButton();
         }
      });
      final JList<String> suggestionList = new JList();
      suggestionList.setSelectionMode(0);
      suggestionList.setModel(this.suggestionListModel);
      suggestionList.addListSelectionListener((e) -> {
         String val = (String)suggestionList.getSelectedValue();
         if (val != null) {
            this.textField.setText(val);
            this.textField.getTextField().selectAll();
            this.textField.getTextField().requestFocusInWindow();
         }
      });
      final JPopupMenu popup = new JPopupMenu();
      popup.setLightWeightPopupEnabled(true);
      popup.setLayout(new BorderLayout());
      popup.add(suggestionList, "Center");
      popup.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            popup.setVisible(false);
            suggestionList.clearSelection();
         }
      });
      this.suggestionButton = this.createRHSButton(ColorScheme.LIGHT_GRAY_COLOR, ColorScheme.MEDIUM_GRAY_COLOR, FontManager.getDefaultBoldFont());
      this.suggestionButton.setText("▾");
      this.suggestionButton.addActionListener((e) -> {
         suggestionList.setPreferredSize(new Dimension(this.getWidth(), suggestionList.getPreferredSize().height));
         popup.show(this, 0, this.suggestionButton.getHeight());
         popup.revalidate();
         popup.requestFocusInWindow();
      });
      this.textField.getTextField().getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent e) {
            IconTextField.this.updateContextButton();
         }

         public void removeUpdate(DocumentEvent e) {
            IconTextField.this.updateContextButton();
         }

         public void changedUpdate(DocumentEvent e) {
            IconTextField.this.updateContextButton();
         }
      });
      JPanel rhsButtons = new JPanel();
      rhsButtons.setBackground(new Color(0, 0, 0, 0));
      rhsButtons.setOpaque(false);
      rhsButtons.setLayout(new BorderLayout());
      rhsButtons.add(this.clearButton, "East");
      rhsButtons.add(this.suggestionButton, "West");
      this.updateContextButton();
      this.add(this.iconWrapperLabel, "West");
      this.add(this.textField, "Center");
      this.add(rhsButtons, "East");
   }

   private JButton createRHSButton(final Color fg, final Color rollover, Font font) {
      final JButton b = new JButton();
      b.setPreferredSize(new Dimension(30, 0));
      b.setFont(font);
      b.setBorder((Border)null);
      b.setRolloverEnabled(true);
      SwingUtil.removeButtonDecorations(b);
      b.setForeground(fg);
      b.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent mouseEvent) {
            b.setForeground(rollover);
            IconTextField.this.textField.dispatchEvent(mouseEvent);
         }

         public void mouseExited(MouseEvent mouseEvent) {
            b.setForeground(fg);
            IconTextField.this.textField.dispatchEvent(mouseEvent);
         }
      });
      return b;
   }

   private void updateContextButton() {
      boolean empty = StringUtils.isBlank(this.textField.getText());
      this.clearButton.setVisible(!empty);
      this.suggestionButton.setVisible(!this.suggestionListModel.isEmpty() && empty);
   }

   public void addActionListener(ActionListener actionListener) {
      this.textField.addActionListener(actionListener);
   }

   public void setIcon(Icon icon) {
      ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(icon.getFile()));
      this.iconWrapperLabel.setIcon(imageIcon);
   }

   public void setIcon(ImageIcon imageIcon) {
      this.iconWrapperLabel.setIcon(imageIcon);
   }

   public String getText() {
      return this.textField.getText();
   }

   public void setText(String text) {
      assert SwingUtilities.isEventDispatchThread();

      this.textField.setText(text);
   }

   public void setBackground(Color color) {
      if (color != null) {
         super.setBackground(color);
         if (this.textField != null) {
            this.textField.setBackground(color);
         }

      }
   }

   public void setHoverBackgroundColor(Color hoverBackgroundColor) {
      if (hoverBackgroundColor != null) {
         this.textField.setHoverBackgroundColor(hoverBackgroundColor);
      }
   }

   public void addKeyListener(KeyListener keyListener) {
      this.textField.addKeyListener(keyListener);
   }

   public void addClearListener(Runnable clearListener) {
      this.clearListeners.add(clearListener);
   }

   public void removeKeyListener(KeyListener keyListener) {
      this.textField.removeKeyListener(keyListener);
   }

   public void setEditable(boolean editable) {
      this.textField.setEditable(editable);
      if (!editable) {
         super.setBackground(this.textField.getBackgroundColor());
      }

   }

   public boolean requestFocusInWindow() {
      super.requestFocusInWindow();
      return this.textField.requestFocusInWindow();
   }

   public Document getDocument() {
      return this.textField.getDocument();
   }

   public DefaultListModel<String> getSuggestionListModel() {
      return this.suggestionListModel;
   }

   public static enum Icon {
      SEARCH("search.png"),
      LOADING("loading_spinner.gif"),
      LOADING_DARKER("loading_spinner_darker.gif"),
      ERROR("error.png");

      private final String file;

      public String getFile() {
         return this.file;
      }

      private Icon(String file) {
         this.file = file;
      }
   }
}
