package ui_funny_paint.component.field;

import controller.canvas.CanvasController;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ToolSizeField extends JToolBar {
	private CanvasController controller;
	private JSlider sizeSlider; // Slider for size input
	private JLabel sizeLabel; // Label to display the current size
	private int size;

	public ToolSizeField() {
		this.size = 1;

		sizeSlider = new JSlider(1, 10, this.size);
		sizeSlider.setMajorTickSpacing(1);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);
		sizeSlider.setPreferredSize(new Dimension(200, 50)); // Set size of the slider

		// Add a change listener to update the brush size
		sizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ToolSizeField.this.size = sizeSlider.getValue(); // Get the current value of the slider
				ToolSizeField.this.controller.setSize(ToolSizeField.this.size); // Update the brush size
				sizeLabel.setText(ToolSizeField.this.size + " px"); // Update the size label
			}
		});

		// Create a label to display the current size
		sizeLabel = new JLabel(this.size + " px");
		sizeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align text

		JPanel sliderContainer = new JPanel(new BorderLayout());
		sliderContainer.add(sizeLabel, BorderLayout.NORTH);
		sliderContainer.add(sizeSlider, BorderLayout.CENTER);
		sliderContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		addSeparator();
		add(sliderContainer);

		this.setVisible(true);
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
		this.size = controller.getSize();
		this.sizeSlider.setValue(this.size);
		this.sizeLabel.setText(this.size + " px");
	}
}
