package ui_funny_paint.panel;

import ui_funny_paint.component.UIElements.HueSlider;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CustomHSVChooserPanel extends AbstractColorChooserPanel {
    private JPanel colorBoxPanel;
    private BufferedImage colorBoxImage;
    private float hue = 0;
    private int selectedX = -1;
    private int selectedY = -1;
    private JSlider hueSlider;
    private JPanel colorPreviewPanel;

    private boolean isUpdatingFromColorBox = false;

    @Override
    protected void buildChooser() {
        setLayout(new BorderLayout());
        setupColorBoxPanel();
        setupHueSlider();
        setupColorPreviewPanel();
        setupLayout();


        updateColorBox();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(colorBoxPanel, BorderLayout.CENTER);
        mainPanel.add(hueSlider, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(colorPreviewPanel, BorderLayout.WEST);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupColorPreviewPanel() {
        colorPreviewPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Color bgColor = getBackground();
                String hexColor = String.format("#%02X%02X%02X",
                        bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());

                // Calculate brightness to determine text color
                double brightness = (bgColor.getRed() * 0.299 +
                        bgColor.getGreen() * 0.587 + bgColor.getBlue() * 0.114) / 255;
                g2d.setColor(brightness < 0.5 ? Color.WHITE : Color.BLACK);

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(hexColor)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(hexColor, x, y);
            }
        };

        colorPreviewPanel.setPreferredSize(new Dimension(150, 30));
        colorPreviewPanel.setBackground(Color.WHITE);
        colorPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        colorPreviewPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color initialColor = colorPreviewPanel.getBackground();
                Color selectedColor = JColorChooser.showDialog(
                        SwingUtilities.getWindowAncestor(colorPreviewPanel),
                        "Choose Color",
                        initialColor
                );

                if (selectedColor != null) {
                    getColorSelectionModel().setSelectedColor(selectedColor);
                    updateColorPreview(selectedColor);
                }
            }
        });
    }

    private void setupHueSlider() {
        hueSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        hueSlider.setUI(new HueSlider(hueSlider)); // Use custom UI
        hueSlider.setPreferredSize(new Dimension(150, 50));
        hueSlider.setFocusable(false);
        // Add change listener to the hue slider
        hueSlider.addChangeListener(e -> {
            hue = hueSlider.getValue() / 360f;
            updateColorBox();
        });

    }

    private void setupColorBoxPanel() {
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
                    int size = 20;
                    int x = selectedX - size / 2;
                    int y = selectedY - size / 2;

                    g.drawLine(x, y + size / 2, x + size, y + size / 2);
                    g.drawLine(x + size / 2, y, x + size / 2, y + size);
                }
            }
        };
        colorBoxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateColorFromMouse(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                updateColorFromMouse(e);
            }
        });

        colorBoxPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateColorFromMouse(e);
            }
        });

        colorBoxPanel.setPreferredSize(new Dimension(150, 150));
        colorBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

    }

    private void updateColorBox() {
        int width = 150;
        int height = 150;

        colorBoxImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = colorBoxImage.createGraphics();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float saturation = (float) x / width;
                float value = 1 - (float) y / height;
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
