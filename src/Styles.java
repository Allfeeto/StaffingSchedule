

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Styles {
	
    public static void setButtonStyle1(JButton button, Color backgroundColor, Color foregroundColor, Font font) {
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(font);
    }

    public static void setButtonStyle(JButton button) {
        setButtonStyle1(button, Color.GRAY, Color.WHITE, new Font("Arial", Font.BOLD, 14));
    }
    
    public static class ButtonHoverListener extends MouseAdapter {
        private Color hoverColor;

        public ButtonHoverListener(Color hoverColor) {
            this.hoverColor = hoverColor;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            // Изменяем цвет текста при наведении мыши
            button.setForeground(hoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            // Возвращаем исходный цвет текста при уходе мыши
            button.setForeground(Color.WHITE);
        }
    }
    
    
}
