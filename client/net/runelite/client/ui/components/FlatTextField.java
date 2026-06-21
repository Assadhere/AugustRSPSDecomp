package net.runelite.client.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import net.runelite.client.ui.ColorScheme;

public class FlatTextField extends JPanel {
   private final JTextField textField;
   private Color backgroundColor;
   private Color hoverBackgroundColor;
   private boolean blocked;

   public FlatTextField() {
      this.backgroundColor = ColorScheme.DARKER_GRAY_COLOR;
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(0, 10, 0, 0));
      this.textField = new JTextField();
      this.textField.setBorder((Border)null);
      this.textField.setOpaque(false);
      this.textField.setSelectedTextColor(Color.WHITE);
      this.textField.setSelectionColor(ColorScheme.BRAND_ORANGE_TRANSPARENT);
      this.add(this.textField, "Center");
      this.textField.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent mouseEvent) {
            if (!FlatTextField.this.blocked) {
               if (FlatTextField.this.hoverBackgroundColor != null) {
                  FlatTextField.this.setBackground(FlatTextField.this.hoverBackgroundColor, false);
               }

            }
         }

         public void mouseExited(MouseEvent mouseEvent) {
            FlatTextField.this.setBackground(FlatTextField.this.backgroundColor);
         }
      });
   }

   public void addActionListener(ActionListener actionListener) {
      this.textField.addActionListener(actionListener);
   }

   public String getText() {
      return this.textField.getText();
   }

   public void setText(String text) {
      this.textField.setText(text);
   }

   public void addKeyListener(KeyListener keyListener) {
      this.textField.addKeyListener(keyListener);
   }

   public void removeKeyListener(KeyListener keyListener) {
      this.textField.removeKeyListener(keyListener);
   }

   public void setBackground(Color color) {
      this.setBackground(color, true);
   }

   public boolean requestFocusInWindow() {
      return this.textField.requestFocusInWindow();
   }

   public void setBackground(Color color, boolean saveColor) {
      if (color != null) {
         super.setBackground(color);
         if (saveColor) {
            this.backgroundColor = color;
         }

      }
   }

   public void setHoverBackgroundColor(Color color) {
      if (color != null) {
         this.hoverBackgroundColor = color;
      }
   }

   public void setEditable(boolean editable) {
      this.blocked = !editable;
      this.textField.setEditable(editable);
      this.textField.setFocusable(editable);
      if (!editable) {
         super.setBackground(this.backgroundColor);
      }

   }

   public Document getDocument() {
      return this.textField.getDocument();
   }

   public JTextField getTextField() {
      return this.textField;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public Color getHoverBackgroundColor() {
      return this.hoverBackgroundColor;
   }

   public boolean isBlocked() {
      return this.blocked;
   }
}
