package ui_funny_paint.panel;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CustomHSVChooserPanel extends AbstractColorChooserPanel {
    private JPanel colorBoxPanel;
    private BufferedImage colorBoxImage;
    private float hue = 0; // Default hue
    private int selectedX = -1; // X position of the selected color
    private int selectedY = -1; // Y position of the selected color
    private JSlider hueSlider; // Vertical hue slider
    private JPanel colorPreviewPanel; // Color preview box

    private boolean isUpdatingFromColorBox = false;
    @Override
    protected void buildChooser() {
        setLayout(new BorderLayout());

        colorBoxPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (colorBoxImage != null) {
                    g.drawImage(colorBoxImage, 0, 0, getWidth(), getHeight(), this);
                }

                if (selectedX >= 0 && selectedY >= 0 && colorBoxImage != null &&
                        selectedX < colorBoxImage.getWidth() && selectedY < colorBoxImage.getHeight()) {
                    Color selectedColor = new Color(colorBoxImage.getRGB(selectedX, selectedY));

                    double brightness = (selectedColor.getRed() * 0.299 +
                            selectedColor.getGreen() * 0.587 +
                            selectedColor.getBlue() * 0.114) / 255;

                    Color indicatorColor = (brightness < 0.5) ? Color.WHITE : Color.BLACK;

                    g.setColor(indicatorColor);
                    int size = 20; // Size of the plus icon
                    int x = selectedX - size / 2; // Center the plus icon horizontally
                    int y = selectedY - size / 2; // Center the plus icon vertically

                    g.drawLine(x, y + size / 2, x + size, y + size / 2);
                    g.drawLine(x + size / 2, y, x + size / 2, y + size);
                }
            }
        };

        colorBoxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateColorFromMouse(e); // Update color when mouse is pressed
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                updateColorFromMouse(e); // Update color when mouse is released
            }
        });

        colorBoxPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateColorFromMouse(e); // Update color when mouse is dragged
            }
        });

        colorBoxPanel.setPreferredSize(new Dimension(150, 150));
        colorBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Initialize the vertical hue slider
        hueSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        hueSlider.setUI(new HueSliderUI(hueSlider)); // Use custom UI
        hueSlider.setPreferredSize(new Dimension(150, 50));
        hueSlider.setFocusable(false);
        // Add change listener to the hue slider
        hueSlider.addChangeListener(e -> {
            hue = hueSlider.getValue() / 360f;
            updateColorBox();
        });

        colorPreviewPanel = new JPanel();
        colorPreviewPanel.setPreferredSize(new Dimension(150, 30));
        colorPreviewPanel.setBackground(Color.WHITE);
        colorPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(colorBoxPanel, BorderLayout.CENTER);
        mainPanel.add(hueSlider, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(colorPreviewPanel, BorderLayout.WEST);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateColorBox();
    }

    private void updateColorBox() {
        int width = 150;
        int height = 150;


        // Create a new image for the color box
        colorBoxImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = colorBoxImage.createGraphics();

        // Draw the color box
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float saturation = (float) x / width;
                float value = 1 - (float) y / height; // Invert y-axis for value
                Color color = Color.getHSBColor(hue, saturation, value);
                g2d.setColor(color);
                g2d.fillRect(x, y, 1, 1);
            }
        }

        g2d.dispose();
        colorBoxPanel.repaint();
    }

    // Update the color preview box
    private void updateColorPreview(Color color) {
        colorPreviewPanel.setBackground(color);
        colorPreviewPanel.repaint();
    }

    // Update the color based on mouse position
    private void updateColorFromMouse(MouseEvent e) {
        isUpdatingFromColorBox = true;
        int x = e.getX();
        int y = e.getY();
        int width = colorBoxPanel.getWidth();
        int height = colorBoxPanel.getHeight();

        // Clamp the mouse coordinates within the bounds of the color box
        x = Math.max(0, Math.min(x, width - 1));
        y = Math.max(0, Math.min(y, height - 1));

        // Calculate saturation and value based on mouse position
        float saturation = (float) x / width;
        float value = 1 - (float) y / height; // Invert y-axis for value

        // Create the selected color
        Color color = Color.getHSBColor(hue, saturation, value);

        // Set the selected color in the JColorChooser's color selection model
        getColorSelectionModel().setSelectedColor(color);

        // Update the selected position
        selectedX = x;
        selectedY = y;

        // Update the color preview box
        updateColorPreview(color);

        // Repaint the color box to show the plus icon indicator
        colorBoxPanel.repaint();
    }

    @Override
    public void updateChooser() {
        if (isUpdatingFromColorBox) {
            return; // Skip updating the slider if the change is from the color box
        }

        // Update the color box when the color changes externally
        Color color = getColorFromModel();
        float[] hsv = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);

        hue = hsv[0]; // Update the hue
        hueSlider.setValue((int) (hue * 360)); // Update the hue slider

        // Calculate the selected position based on the current color
        int width = colorBoxPanel.getWidth();
        int height = colorBoxPanel.getHeight();
        selectedX = (int) (hsv[1] * width); // Saturation -> X
        selectedY = (int) ((1 - hsv[2]) * height); // Value -> Y (inverted)

        // Update the color box and preview panel
        updateColorBox();
        updateColorPreview(color);
    }

    @Override
    public String getDisplayName() {
        return "HSV"; // Name of the tab in the JColorChooser
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return null; // No icon
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return null; // No icon
    }
}


class HueSliderUI extends BasicSliderUI {
    public HueSliderUI(JSlider slider) {
        super(slider);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Use the trackRect dimensions to draw the gradient
        int x = trackRect.x;
        int y = trackRect.y;
        int width = trackRect.width;
        int height = trackRect.height;

        // Draw the gradient for the hue slider (horizontal)
        for (int i = 0; i < width; i++) {
            float hue = (float) i / width; // Normalize hue to 0-1
            g2d.setColor(Color.getHSBColor(hue, 1, 1)); // Full saturation and value
            g2d.drawLine(x + i, y, x + i, y + height -1);
        }

        g2d.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Customize the thumb appearance
        g2d.setColor(Color.BLACK); // Thumb color
        g2d.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height); // Fill the thumb

        g2d.dispose();
    }

    @Override
    protected Dimension getThumbSize() {
        // Set the size of the thumb
        return new Dimension(10, 25); // Width x Height
    }

    @Override
    protected void calculateThumbLocation() {
        // Force the slider to repaint when the thumb moves
        super.calculateThumbLocation();
        slider.repaint(); // Ensure the slider is repainted when the thumb moves
    }
}