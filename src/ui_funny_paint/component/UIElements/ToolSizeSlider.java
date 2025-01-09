package ui_funny_paint.component.UIElements;

import controller.canvas.CanvasController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ToolSizeSlider extends JToolBar {
    private CanvasController controller;
    private final JSlider sizeSlider;
    private final JLabel sizeLabel;
    private int size;

    public ToolSizeSlider() {
        this.size = 1;

        sizeSlider = new JSlider(1, 10, this.size);
        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setPreferredSize(new Dimension(200, 50));

        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ToolSizeSlider.this.size = sizeSlider.getValue();
                ToolSizeSlider.this.controller.setSize(ToolSizeSlider.this.size);
                sizeLabel.setText(ToolSizeSlider.this.size + " px");
            }
        });

        sizeLabel = new JLabel(this.size + " px");
        sizeLabel.setHorizontalAlignment(SwingConstants.CENTER);

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
