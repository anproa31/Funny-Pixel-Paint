package ui_funny_paint.screen;


import controller.canvas.CanvasController;
import model.DatabaseManager;
import ui_funny_paint.component.button.ColorToggler;
import ui_funny_paint.component.UIElements.ToolSizeSlider;
import ui_funny_paint.panel.ColorPicker;
import ui_funny_paint.panel.ControlPanel;
import ui_funny_paint.panel.MainMenuBar;
import ui_funny_paint.panel.ToolPanel;
import utils.GlobalKeyBinder;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import static utils.LoadIcon.loadIcon;

public class MainFrame extends JFrame {
    private ColorPicker colorPicker;
    private ToolPanel toolPanel;
    private JPanel canvasPanel;
    private ControlPanel controlPanel;
    private ColorToggler colorToggler;
    private JScrollPane canvasContainer;
    private CanvasController controller;
    public static MainFrame instance;

    public MainFrame() {
        super("Funny Paint");
        instance = this;

        setupFrame(new Dimension(1280, 720));
        setupEventHandlers();
        initializeComponents();
        setupLayout();
        pack();
    }

    private void setupFrame(Dimension resolution) {
        setPreferredSize(resolution);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(Objects.requireNonNull(loadIcon("logo.png")).getImage());
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                colorPicker.updateColorButtonSize();
            }
        });
    }

    private void handleWindowClosing() {
        boolean closeWindow = controller.closeCanvas();
        if (!closeWindow) return;

        DatabaseManager.disconnect();
        setVisible(false);
        dispose();
    }

    private void initializeComponents() {
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setPreferredSize(new Dimension(200, 0));

        toolPanel = new ToolPanel();
        toolPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        controlPanel = new ControlPanel();

        canvasPanel = new JPanel(new GridBagLayout());
        canvasPanel.setBackground(new Color(34, 32, 52));

        canvasContainer = new JScrollPane(canvasPanel);
        canvasContainer.setBorder(new CompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 25, 0),
                BorderFactory.createLineBorder(Color.BLACK, 5)
        ));

        colorToggler = new ColorToggler(Color.BLACK, Color.WHITE);
        controlPanel.add(colorToggler);

        controller = new CanvasController(this);

        GlobalKeyBinder globalKeyBinder = new GlobalKeyBinder(this.getRootPane());
        globalKeyBinder.setController(controller);

        ToolSizeSlider toolSizeSlider = new ToolSizeSlider();
        toolSizeSlider.setController(controller);
        controlPanel.add(toolSizeSlider);
    }

    private void setupLayout() {
        Container mainPane = getContentPane();
        mainPane.setLayout(new BorderLayout());

        JPanel controlPanelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanelWrapper.add(controlPanel);

        mainPane.add(canvasContainer, BorderLayout.CENTER);
        mainPane.add(colorPicker, BorderLayout.WEST);
        mainPane.add(toolPanel, BorderLayout.EAST);
        mainPane.add(controlPanelWrapper, BorderLayout.NORTH);

        setJMenuBar(new MainMenuBar(controller));
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public ToolPanel getTools() {
        return toolPanel;
    }

    public JPanel getCanvasPanel() {
        return canvasPanel;
    }

    public JScrollPane getCanvasContainer() {
        return canvasContainer;
    }

    public ControlPanel getControlPanel() {
        return this.controlPanel;
    }

    public ColorToggler getColorToggler() {

        return this.colorToggler;
    }

    public CanvasController getController() {
        return controller;
    }

}
