package ui_funny_paint.panel;

import controller.canvas.CanvasController;
import ui_funny_paint.component.button.ColorToggler;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static java.lang.Integer.min;

public class ColorPicker extends JPanel {
    private CanvasController controller;
    private JColorChooser colorChooser;
    private JPanel swatchesPanel;
    private JPanel colorBoxPanel;
    private final ArrayList<JButton> colorPaletteButton = new ArrayList<>();

    private class Listener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            ColorToggler colorToggler = ColorPicker.this.controller.getColorToggler();
            if (colorToggler == null) return;
            colorToggler.setColor(ColorPicker.this.colorChooser.getColor());
        }
    }

    public ColorPicker(Color initialColor) {
        createColorWheelPanel(initialColor);
        createSwatches();
        setupLayout();

        colorChooser.getSelectionModel().addChangeListener(new Listener());
    }

    private void setupLayout() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Add the swatches panel to the top of the GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1;
        this.add(swatchesPanel, gbc);

        // Add the colorbox to the bottom of the GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(colorBoxPanel, gbc);

    }

    private void createColorWheelPanel(Color initialColor) {
        colorChooser = new JColorChooser(initialColor);
        colorBoxPanel = new JPanel();

        colorChooser.setPreviewPanel(new JPanel());

        AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
        for (AbstractColorChooserPanel panel : panels) {
            colorChooser.removeChooserPanel(panel);
        }

        colorChooser.addChooserPanel(new CustomHSVChooserPanel());
        colorBoxPanel.add(colorChooser);
    }

    private void createSwatches() {
        JPanel customSwatches = new JPanel();
        customSwatches.setLayout(new GridBagLayout());
        GridBagConstraints swatchesGbc = new GridBagConstraints();
        swatchesGbc.insets = new Insets(1, 1, 1, 1);

        ArrayList<Color> swatchesColors = readColorPalette();
        int columns = 5; // Number of columns in the grid
        for (int i = 0; i < swatchesColors.size(); i++) {
            JButton colorButton = getjButton(swatchesColors, i);

            // Add the button to the grid
            swatchesGbc.gridx = i % columns; // Column index
            swatchesGbc.gridy = i / columns; // Row index
            colorPaletteButton.add(colorButton);
            customSwatches.add(colorButton, swatchesGbc);
        }
        updateColorButtonSize();

        swatchesPanel = new JPanel();
        swatchesPanel.setLayout(new BorderLayout());
        swatchesPanel.add(customSwatches, BorderLayout.CENTER);
        swatchesPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    }

    private JButton getjButton(ArrayList<Color> swatchesColors, int i) {
        JButton colorButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        colorButton.setBackground(swatchesColors.get(i));
        colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        colorButton.setOpaque(true);
        colorButton.setFocusPainted(false);
        colorButton.setContentAreaFilled(false);
        colorButton.addActionListener(e -> colorChooser.setColor(swatchesColors.get(i))); // Set selected color
        return colorButton;
    }

    public void updateColorButtonSize() {
        int panelHeight = getHeight() - 300 - 25;
        int panelWidth = getWidth();
        if (panelWidth <= 0 && panelHeight <= 0) return;

        int size = min(panelWidth / 5 - 5, panelHeight / 13 - 1);

        Dimension iconSize = new Dimension(size, size);

        for (JButton b : colorPaletteButton) {
            b.setPreferredSize(new Dimension(iconSize));
        }

        swatchesPanel.revalidate();
        swatchesPanel.repaint();
    }

    private ArrayList<Color> readColorPalette() {
        ArrayList<Color> colorsList = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("res/ColorPalette.txt")) {
            if (inputStream == null) {
                throw new RuntimeException("ColorPalette.txt not found in the classpath!");
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        colorsList.add(Color.decode(line));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read color palette", e);
        }
        return colorsList;
    }

    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    public void setColor(Color c) {
        colorChooser.setColor(c);
    }
}

