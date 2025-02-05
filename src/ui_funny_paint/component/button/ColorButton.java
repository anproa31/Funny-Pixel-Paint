package ui_funny_paint.component.button;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorButton extends JButton {
    private Color current;

    public ColorButton(Color c) {
        setSelectedColor(c);
    }

    public Color getSelectedColor() {
        return current;
    }

    public void setSelectedColor(Color newColor) {

        if (newColor == null) return;

        this.current = newColor;
        setIcon(createIcon(current, 24, 24));
        repaint();
    }

    public static ImageIcon createIcon(Color main, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(main);
        graphics.setComposite(AlphaComposite.Src);
        graphics.fillRect(0, 0, width, height);
        graphics.setXORMode(Color.DARK_GRAY);
        graphics.drawRect(0, 0, width - 1, height - 1);
        image.flush();
        return new ImageIcon(image);
    }
}