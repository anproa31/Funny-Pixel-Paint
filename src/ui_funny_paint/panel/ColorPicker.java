package ui_funny_paint.panel;

import controller.canvas.CanvasController;
import ui_funny_paint.component.button.ColorToggler;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ColorPicker extends JPanel {
	private CanvasController controller;
	private JColorChooser colorChooser;
	private JPanel swatchesPanel;
	private JPanel colorBoxPanel;
	private ArrayList<JButton> colorPaletteButton = new ArrayList<>();
	private ToolPanel toolPanel;

	private class Listener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			ColorToggler colorToggler = ColorPicker.this.controller.getColorToggler();
			if (colorToggler == null) return;
			colorToggler.setColor(ColorPicker.this.colorChooser.getColor());
		}
	}

	private ArrayList<Color> ReadColorPalette() {
		ArrayList<Color> colorsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/ColorPalette.txt"))) {
            String line = br.readLine();
            while (line != null) {
				if (line != null && !line.trim().isEmpty())
                	colorsList.add(Color.decode(line));
				line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		return colorsList;
	}

	public ColorPicker(Color initialColor) {
		// Set the layout to GridBagLayout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH; // Make components fill their space

		CreateColorWheelPanel(initialColor);
		CreateSwatches();

		// Add the swatches panel to the top of the GridBagLayout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1;
		this.add(swatchesPanel, gbc);

		// Add the tabbed pane to the bottom of the GridBagLayout
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(colorBoxPanel, gbc);

		// Add a change listener to the color chooser
		colorChooser.getSelectionModel().addChangeListener(new Listener());

	}

	private void CreateColorWheelPanel(Color initialColor)
	{
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

	private void CreateSwatches()
	{
		JPanel customSwatches = new JPanel();
		customSwatches.setLayout(new GridBagLayout()); // Use GridBagLayout for more control
		GridBagConstraints swatchesGbc = new GridBagConstraints();
		swatchesGbc.insets = new Insets(1, 1, 1, 1); // Add some padding between buttons

		ArrayList<Color> swatchesColors = ReadColorPalette();
		int columns = 5; // Number of columns in the grid
		for (int i = 0; i < swatchesColors.size(); i++) {
			JButton colorButton = new JButton() {
				@Override
				protected void paintComponent(Graphics g) {
					// Fill the entire button with the background color
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			colorButton.setBackground(swatchesColors.get(i));
			colorButton.setPreferredSize(new Dimension(21, 21)); // Fixed size for buttons
			colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			colorButton.setOpaque(true);
			colorButton.setFocusPainted(false);
			colorButton.setContentAreaFilled(false);
			int finalI = i;
			colorButton.addActionListener(e -> colorChooser.setColor(swatchesColors.get(finalI))); // Set selected color

			// Add the button to the grid
			swatchesGbc.gridx = i % columns; // Column index
			swatchesGbc.gridy = i / columns; // Row index
			colorPaletteButton.add(colorButton);
			customSwatches.add(colorButton, swatchesGbc);
		}

		// Create a panel to hold the custom swatches at the top
		swatchesPanel = new JPanel();
		swatchesPanel.setLayout(new BorderLayout());
		swatchesPanel.add(customSwatches);
	}

	public void UpdateColorButtonSize()
	{
		int panelHeight = getHeight() - 300;
		int panelWidth = getWidth();
		if (panelWidth <= 0 && panelHeight <=0) return;
		Dimension iconSize = new Dimension(panelHeight/14, panelHeight/14);
		System.out.println(iconSize);
		for (JButton b : colorPaletteButton)
		{
			b.setPreferredSize(new Dimension(iconSize));
		}
		for (JButton b : colorPaletteButton)
		{
			b.setPreferredSize(new Dimension(iconSize));
		}
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
	}

	public void setColor(Color c) {
		colorChooser.setColor(c);
	}
}

