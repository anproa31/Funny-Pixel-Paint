package ui_funny_paint.component.dialogs;


import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class NewCanvasDialog extends JDialog {
    public final static int APPROVE_OPTION = 1;
    public final static int CANCEL_OPTION = 0;

    private static int defaultWidth = 32;
    private static int defaultHeight = 32;
    private static Color defaultFillColor = new Color(0, 0, 0, 0); // transparent empty canvas

    private int width;
    private int height;
    private Color fillColor;
    private int closeOperationOption = CANCEL_OPTION;


    public NewCanvasDialog(JFrame parent) {
        super(parent, "Create a new Canvas", true);
        width = defaultWidth;
        height = defaultHeight;
        fillColor = defaultFillColor;

        JFormattedTextField widthInput = new JFormattedTextField(NumberFormat.getIntegerInstance());
        widthInput.setValue(width);
        widthInput.setColumns(5);
        widthInput.addPropertyChangeListener("value", e -> NewCanvasDialog.this.width = ((Number) ((JFormattedTextField) e.getSource()).getValue()).intValue());

        JFormattedTextField heightInput = new JFormattedTextField(NumberFormat.getIntegerInstance());
        heightInput.setColumns(5);
        heightInput.setValue(height);
        heightInput.addPropertyChangeListener("value", e -> NewCanvasDialog.this.height = ((Number) ((JFormattedTextField) e.getSource()).getValue()).intValue());

        ColorChooserButton colorChooser = new ColorChooserButton(defaultFillColor, "Choose a fill color");
        colorChooser.addColorChangedListener(newColor -> NewCanvasDialog.this.fillColor = newColor);


        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            defaultWidth = width;
            defaultHeight = height;
            defaultFillColor = fillColor;
            NewCanvasDialog.this.closeOperationOption = APPROVE_OPTION;
            NewCanvasDialog.this.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> NewCanvasDialog.this.dispose());

        JPanel widthPanel = new JPanel();
        widthPanel.add(new JLabel("Width:"));
        widthPanel.add(widthInput);

        JPanel heightPanel = new JPanel();
        heightPanel.add(new JLabel("Height:"));
        heightPanel.add(heightInput);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JLabel("Fill Color:"));
        buttonPanel.add(colorChooser);

        JPanel controls = new JPanel();
        controls.add(okButton);
        controls.add(cancelButton);

        JPanel settings = new JPanel();
        settings.add(widthPanel);
        settings.add(heightPanel);
        settings.add(buttonPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(settings);
        mainPanel.add(controls);


        this.getContentPane().add(mainPanel);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    public int showOpenDialog() {
        return this.closeOperationOption;
    }


    public int getChosenWidth() {
        return this.width;
    }

    public int getChosenHeight() {
        return this.height;
    }

    public Color getChosenFillColor() {
        return this.fillColor;
    }

}
