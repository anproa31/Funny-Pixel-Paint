package ui_funny_paint.component.button;

import controller.canvas.CanvasController;
import ui_funny_paint.panel.PixelCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorToggler extends JToolBar implements ActionListener {
    private CanvasController controller;
    private final ColorButton primaryColorButton;
    private final ColorButton secondaryColorButton;
    private boolean primaryColorPicked;

    private static final Color selectedBackgroundColor = new Color(37, 122, 253);
    private static Color defaultBackgroundColor;


    public ColorToggler(Color primaryColor, Color secondaryColor) {
        this.primaryColorButton = new ColorButton(primaryColor);
        this.primaryColorButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        this.primaryColorButton.addActionListener(this);

        this.secondaryColorButton = new ColorButton(secondaryColor);
        this.secondaryColorButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        this.secondaryColorButton.addActionListener(this);

        this.primaryColorPicked = true;


        JPanel primaryButtonContainer = new JPanel();
        primaryButtonContainer.setLayout(new BoxLayout(primaryButtonContainer, BoxLayout.Y_AXIS));
        primaryButtonContainer.add(primaryColorButton);

        JPanel secondaryButtonContainer = new JPanel();
        secondaryButtonContainer.setLayout(new BoxLayout(secondaryButtonContainer, BoxLayout.Y_AXIS));
        secondaryButtonContainer.add(secondaryColorButton);

        add(primaryButtonContainer);
        addSeparator();
        add(secondaryButtonContainer);
        setFloatable(false);
        defaultBackgroundColor = primaryButtonContainer.getBackground();
        revalidate();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }


    public void setColor(Color c) {
        if (this.primaryColorPicked)
            this.primaryColorButton.setSelectedColor(c);
        else
            this.secondaryColorButton.setSelectedColor(c);
        updateCanvas();
    }

    public void swapColors() {
        Color primary = this.primaryColorButton.getSelectedColor();
        Color secondary = this.secondaryColorButton.getSelectedColor();

        this.primaryColorButton.setSelectedColor(secondary);
        this.secondaryColorButton.setSelectedColor(primary);
        updateCanvas();
        repaint();
    }

    public void updateCanvas() {
        PixelCanvas canvas = controller.getCanvas();
        if (canvas == null)
            return;
        canvas.setPrimaryColor(this.primaryColorButton.getSelectedColor());
        canvas.setSecondaryColor(this.secondaryColorButton.getSelectedColor());
    }

    public void setController(CanvasController controller) {
        this.controller = controller;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        this.primaryColorPicked = e.getSource() == this.primaryColorButton;

        if (this.primaryColorPicked) {
            this.primaryColorButton.setBackground(selectedBackgroundColor);
            this.secondaryColorButton.setBackground(defaultBackgroundColor);
        } else {
            this.primaryColorButton.setBackground(defaultBackgroundColor);
            this.secondaryColorButton.setBackground(selectedBackgroundColor);
        }
        repaint();
    }
}
