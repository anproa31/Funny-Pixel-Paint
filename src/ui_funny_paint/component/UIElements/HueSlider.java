package ui_funny_paint.component.UIElements;


import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class HueSlider extends BasicSliderUI {
    public HueSlider(JSlider slider) {
        super(slider);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = trackRect.x;
        int y = trackRect.y;
        int width = trackRect.width;
        int height = trackRect.height;

        for (int i = 0; i < width; i++) {
            float hue = (float) i / width; // Normalize hue to 0-1
            g2d.setColor(Color.getHSBColor(hue, 1, 1)); // Full saturation and value
            g2d.drawLine(x + i, y, x + i, y + height - 1);
        }

        g2d.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);

        g2d.fillRect(thumbRect.x, thumbRect.y + thumbRect.y / 2 + 1, 10, 10); // Fill the thumb

        g2d.dispose();
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(10, 25); // Width x Height
    }

    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        slider.repaint();
    }
}