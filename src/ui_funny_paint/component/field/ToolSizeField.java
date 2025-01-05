////package ui_funny_paint.component.field;
////
////import controller.canvas.CanvasController;
////
////import java.awt.BorderLayout;
////import java.awt.Dimension;
////import java.awt.Font;
////import java.awt.event.KeyAdapter;
////import java.awt.event.KeyEvent;
////
////import javax.swing.BorderFactory;
////import javax.swing.JLabel;
////import javax.swing.JPanel;
////import javax.swing.JTextField;
////import javax.swing.JToolBar;
////import javax.swing.SwingConstants;
////
////@SuppressWarnings("serial")
////public class ToolSizeField extends JToolBar {
////	private CanvasController controller;
////	private JTextField sizeInput; // Text field for size input
////	private JLabel sizeLabel; // Label to display "Size"
////	private int size;
////
////	public ToolSizeField() {
////		this.size = 1; // Default brush size
////
////		// Create a label for "Size"
////		sizeLabel = new JLabel("Size:");
////		sizeLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Set font for the label
////		sizeLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Align text to the right
////
////		// Create a text field for size input
////		sizeInput = new JTextField(2); // Set the width of the text field (smaller)
////		sizeInput.setText(String.valueOf(this.size)); // Set initial value
////		sizeInput.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the text field
////		sizeInput.setHorizontalAlignment(SwingConstants.CENTER); // Center-align text
////		sizeInput.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); // Minimal padding
////		sizeInput.setPreferredSize(new Dimension(25, 18)); // Set a smaller size for the text field
////
////		// Add a key listener to handle input
////		sizeInput.addKeyListener(new KeyAdapter() {
////			@Override
////			public void keyReleased(KeyEvent e) {
////				String text = sizeInput.getText().trim(); // Get the trimmed text from the text field
////
////				// If the text field is empty, do nothing (allow deletion)
////				if (text.isEmpty()) {
////					return;
////				}
////
////				try {
////					// Parse the input value as an integer
////					int newSize = Integer.parseInt(text);
////
////					// Validate the input (ensure it's within a reasonable range)
////					if (newSize >= 1 && newSize <= 50) {
////						ToolSizeField.this.size = newSize;
////						ToolSizeField.this.controller.setSize(ToolSizeField.this.size);
////					} else {
////						// If the input is invalid, reset the text field to the last valid size
////						sizeInput.setText(String.valueOf(ToolSizeField.this.size));
////					}
////				} catch (NumberFormatException ex) {
////					// If the input is not a valid number, reset the text field to the last valid size
////					sizeInput.setText(String.valueOf(ToolSizeField.this.size));
////				}
////			}
////		});
////
////		// Create a panel to hold the label and text field
////		JPanel inputContainer = new JPanel(new BorderLayout());
////		inputContainer.add(sizeLabel, BorderLayout.WEST); // Add "Size" label to the left
////		inputContainer.add(sizeInput, BorderLayout.CENTER); // Add text field to the center
////		inputContainer.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Minimal padding
////
////		// Add components to the toolbar
////		addSeparator();
////		add(inputContainer);
////
////		setFloatable(false); // Prevent the toolbar from being moved
////		this.setVisible(true);
////	}
////
////	public void setController(CanvasController controller) {
////		this.controller = controller;
////		this.size = controller.getSize();
////		this.sizeInput.setText(String.valueOf(this.size)); // Update the text field with the current size
////	}
////}
//
//package ui_funny_paint.component.field;
//
//import controller.canvas.CanvasController;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//
//import javax.swing.BorderFactory;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.JToolBar;
//import javax.swing.SwingConstants;
//import javax.swing.OverlayLayout;
//
//@SuppressWarnings("serial")
//public class ToolSizeField extends JToolBar {
//	private CanvasController controller;
//	private JTextField sizeInput; // Text field for size input
//	private JLabel sizeLabel; // Label to display "Size"
//	private JLabel pxLabel; // Label to display "px"
//	private int size;
//
//	public ToolSizeField() {
//		this.size = 1; // Default brush size
//
//		// Create a label for "Size"
//		sizeLabel = new JLabel("Size:");
//		sizeLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Set font for the label
//		sizeLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Align text to the right
//
//		// Create a text field for size input
//		sizeInput = new JTextField(2); // Set the width of the text field (smaller)
//		sizeInput.setText(String.valueOf(this.size)); // Set initial value
//		sizeInput.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the text field
//		sizeInput.setHorizontalAlignment(SwingConstants.CENTER); // Center-align text
//		sizeInput.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); // Minimal padding
//		sizeInput.setPreferredSize(new Dimension(25, 18)); // Set a smaller size for the text field
//
//		// Create a label for "px"
//		pxLabel = new JLabel("px");
//		pxLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the label
//		pxLabel.setForeground(sizeInput.getBackground()); // Match the background color of the text field
//		pxLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left
//
//		// Create a panel to hold the text field and the "px" label
//		JPanel inputContainer = new JPanel();
//		inputContainer.setLayout(new OverlayLayout(inputContainer)); // Use OverlayLayout to stack components
//		inputContainer.add(sizeInput); // Add text field first
//		inputContainer.add(pxLabel); // Add "px" label on top
//
//		// Add a key listener to handle input
//		sizeInput.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				String text = sizeInput.getText().trim(); // Get the trimmed text from the text field
//
//				// If the text field is empty, do nothing (allow deletion)
//				if (text.isEmpty()) {
//					return;
//				}
//
//				try {
//					// Parse the input value as an integer
//					int newSize = Integer.parseInt(text);
//
//					// Validate the input (ensure it's within a reasonable range)
//					if (newSize >= 1 && newSize <= 50) {
//						ToolSizeField.this.size = newSize;
//						ToolSizeField.this.controller.setSize(ToolSizeField.this.size);
//					} else {
//						// If the input is invalid, reset the text field to the last valid size
//						sizeInput.setText(String.valueOf(ToolSizeField.this.size));
//					}
//				} catch (NumberFormatException ex) {
//					// If the input is not a valid number, reset the text field to the last valid size
//					sizeInput.setText(String.valueOf(ToolSizeField.this.size));
//				}
//			}
//		});
//
//		// Create a panel to hold the label and input container
//		JPanel container = new JPanel(new BorderLayout());
//		container.add(sizeLabel, BorderLayout.WEST); // Add "Size" label to the left
//		container.add(inputContainer, BorderLayout.CENTER); // Add input container to the center
//		container.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Minimal padding
//
//		// Add components to the toolbar
//		addSeparator();
//		add(container);
//
//		setFloatable(false); // Prevent the toolbar from being moved
//		this.setVisible(true);
//	}
//
//	public void setController(CanvasController controller) {
//		this.controller = controller;
//		this.size = controller.getSize();
//		this.sizeInput.setText(String.valueOf(this.size)); // Update the text field with the current size
//	}
//}

package ui_funny_paint.component.field;

import controller.canvas.CanvasController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ToolSizeField extends JToolBar {
	private CanvasController controller;
	private JTextField sizeInput; // Text field for size input
	private JLabel sizeLabel; // Label to display "Size"
	private JLabel pxLabel; // Label to display "px"
	private int size;

	public ToolSizeField() {
		this.size = 1; // Default brush size

		// Create a label for "Size"
		sizeLabel = new JLabel("Size:     ");
		sizeLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Set font for the label
		sizeLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Align text to the right

		// Create a text field for size input
		sizeInput = new JTextField(2); // Set the width of the text field (smaller)
		sizeInput.setText(String.valueOf(this.size)); // Set initial value
		sizeInput.setFont(new Font("Arial", Font.PLAIN, 20)); // Set font for the text field
		sizeInput.setHorizontalAlignment(SwingConstants.CENTER); // Center-align text
		sizeInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Minimal padding

		// Create a label for "px"
		pxLabel = new JLabel("     px");
		pxLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Set font for the label
		pxLabel.setForeground(sizeInput.getForeground()); // Match the text color of the text field
		pxLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left

		// Create a panel to hold the text field and the "px" label
		JPanel inputContainer = new JPanel(new BorderLayout());
		inputContainer.add(sizeInput, BorderLayout.CENTER); // Add text field to the center
		inputContainer.add(pxLabel, BorderLayout.EAST); // Add "px" label to the right
		inputContainer.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Minimal padding

		// Add a key listener to handle input
		sizeInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = sizeInput.getText().trim(); // Get the trimmed text from the text field

				// If the text field is empty, do nothing (allow deletion)
				if (text.isEmpty()) {
					return;
				}

				try {
					// Parse the input value as an integer
					int newSize = Integer.parseInt(text);

					// Validate the input (ensure it's within a reasonable range)
					if (newSize >= 1 && newSize <= 50) {
						ToolSizeField.this.size = newSize;
						ToolSizeField.this.controller.setSize(ToolSizeField.this.size);
					} else {
						// If the input is invalid, reset the text field to the last valid size
						sizeInput.setText(String.valueOf(ToolSizeField.this.size));
					}
				} catch (NumberFormatException ex) {
					// If the input is not a valid number, reset the text field to the last valid size
					sizeInput.setText(String.valueOf(ToolSizeField.this.size));
				}
			}
		});

		// Create a panel to hold the label and input container
		JPanel container = new JPanel(new BorderLayout());
		container.add(sizeLabel, BorderLayout.WEST); // Add "Size" label to the left
		container.add(inputContainer, BorderLayout.CENTER); // Add input container to the center
		container.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Minimal padding

		// Add components to the toolbar
		addSeparator();
		add(container);

		setFloatable(false); // Prevent the toolbar from being moved
		this.setVisible(true);
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
		this.size = controller.getSize();
		this.sizeInput.setText(String.valueOf(this.size)); // Update the text field with the current size
	}
}