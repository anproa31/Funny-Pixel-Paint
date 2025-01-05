package paintpix;

import java.awt.*;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ColorPicker extends JPanel {
	private MainController controller;
	private JColorChooser colorChooser;

	private class Listener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			ColorToggler colorToggler = ColorPicker.this.controller.getColorToggler();
			if (colorToggler == null) return;
			colorToggler.setColor(ColorPicker.this.colorChooser.getColor());
		}
	}

	public ColorPicker(Color initialColor) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Create the main color chooser
		colorChooser = new JColorChooser(initialColor);
		colorChooser.setPreviewPanel(new JPanel()); // Remove the preview panel

		// Remove unwanted panels (e.g., CMYK)
		for (AbstractColorChooserPanel p : colorChooser.getChooserPanels()) {
			String displayName = p.getDisplayName();
			if (displayName.equals("CMYK")) {
				colorChooser.removeChooserPanel(p);
			}
		}

		// Create a split pane to divide the color chooser into two parts
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.8); // Split the space equally

		// Create a custom swatches panel
		JPanel customSwatches = new JPanel();
		customSwatches.setLayout(new GridBagLayout()); // Use GridBagLayout for more control
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2); // Add some padding between buttons

		// Define a larger color palette
		Color[] customColors = {
				// Basic colors
				Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE,
				Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE,
				new Color(128, 0, 128), new Color(255, 105, 180), // Purple, Pink
				new Color(0, 128, 128), new Color(128, 128, 0), // Teal, Olive
				new Color(255, 165, 0), new Color(0, 255, 255), // Orange, Cyan
				new Color(75, 0, 130), new Color(139, 69, 19), // Indigo, Brown
				new Color(240, 230, 140), new Color(255, 20, 147), // Khaki, Deep Pink
				new Color(60, 179, 113), new Color(70, 130, 180), // Medium Sea Green, Steel Blue
				new Color(100, 149, 237), new Color(255, 222, 173), // Cornflower, Navajo White
				new Color(255, 228, 181), new Color(152, 251, 152), // Moccasin, Pale Green
				new Color(219, 112, 147), new Color(255, 240, 245), // Pale Violet Red, Lavender Blush
				new Color(255, 248, 220), new Color(245, 222, 179), // Cornsilk, Wheat

				// Additional colors
				new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255), // Primary RGB
				new Color(255, 255, 0), new Color(0, 255, 255), new Color(255, 0, 255), // Secondary RGB
				new Color(192, 192, 192), new Color(128, 128, 128), new Color(128, 0, 0), // Grays and Maroon
				new Color(128, 128, 0), new Color(0, 128, 0), new Color(128, 0, 128), // Olive, Green, Purple
				new Color(0, 0, 128), new Color(0, 128, 128), new Color(0, 0, 0), // Navy, Teal, Black
				new Color(255, 215, 0), new Color(218, 165, 32), new Color(184, 134, 11), // Gold, Goldenrod, Dark Goldenrod
				new Color(169, 169, 169), new Color(0, 0, 139), new Color(0, 139, 139), // Dark Gray, Dark Blue, Dark Cyan
				new Color(139, 0, 139), new Color(139, 0, 0), new Color(139, 69, 19), // Dark Magenta, Dark Red, Saddle Brown
				new Color(144, 238, 144), new Color(173, 216, 230), new Color(240, 128, 128) // Light Green, Light Blue, Light Coral
		};

		int columns = 19; // Number of columns in the grid
		for (int i = 0; i < customColors.length; i++) {
			JButton colorButton = new JButton() {
				@Override
				protected void paintComponent(Graphics g) {
					// Fill the entire button with the background color
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			colorButton.setBackground(customColors[i]);
			colorButton.setPreferredSize(new Dimension(20, 20)); // Fixed size for buttons
			colorButton.setOpaque(true); // Ensure button is filled with color
			colorButton.setBorderPainted(false); // Remove border
			colorButton.setFocusPainted(false); // Remove focus border
			colorButton.setContentAreaFilled(false); // Disable default content area filling
			int finalI = i;
			colorButton.addActionListener(e -> colorChooser.setColor(customColors[finalI])); // Set selected color

			// Add the button to the grid
			gbc.gridx = i % columns; // Column index
			gbc.gridy = i / columns; // Row index
			customSwatches.add(colorButton, gbc);
		}

		// Create a panel to hold the custom swatches at the top
		JPanel swatchesPanel = new JPanel();
		swatchesPanel.setLayout(new BorderLayout());
		swatchesPanel.add(customSwatches, BorderLayout.NORTH); // Position the palette at the top

		// Replace the swatches panel in the "Swatches" chooser
		for (AbstractColorChooserPanel p : colorChooser.getChooserPanels()) {
			if (p.getDisplayName().equals("Swatches")) {
				p.removeAll();
				p.setLayout(new BorderLayout());
				p.add(swatchesPanel, BorderLayout.CENTER);
				p.revalidate();
				p.repaint();
				break;
			}
		}

		// Create a tabbed pane for the RGB, HSV, and HSL tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		for (AbstractColorChooserPanel p : colorChooser.getChooserPanels()) {
			if (!p.getDisplayName().equals("Swatches")) {
				tabbedPane.addTab(p.getDisplayName(), p);
			}
		}

		// Add the panels to the split pane
		splitPane.setTopComponent(swatchesPanel);
		splitPane.setBottomComponent(tabbedPane);

		// Add the split pane to the main panel
		this.add(splitPane);

		// Add a change listener to the color chooser
		colorChooser.getSelectionModel().addChangeListener(new Listener());

		// Set a border for the color picker
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Empty border with padding
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	public void setColor(Color c) {
		colorChooser.setColor(c);
	}
}

