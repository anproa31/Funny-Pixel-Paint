package ui_funny_paint.component.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorChooserButton extends JButton {
    private Color current;

    public ColorChooserButton(Color c, String prompt) {
        setSelectedColor(c);
        addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, prompt, current);
            setSelectedColor(newColor);
        });
    }

    public interface ColorChangedListener {
        void colorChanged(Color newColor);
    }

    private final List<ColorChangedListener> listeners = new ArrayList<>();

    public void addColorChangedListener(ColorChangedListener toAdd) {
        listeners.add(toAdd);
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

    public Color getSelectedColor() {
        return current;
    }

    public void setSelectedColor(Color newColor) {
        setSelectedColor(newColor, true);
    }

    public void setSelectedColor(Color newColor, boolean notify) {

        if (newColor == null) return;

        current = newColor;
        setIcon(createIcon(current, 16, 16));
        repaint();

        if (notify) {
            for (ColorChangedListener l : listeners) {
                l.colorChanged(newColor);
            }
        }
    }
}