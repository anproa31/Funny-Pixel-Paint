package ui_funny_paint.screen;


import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import controller.canvas.CanvasController;
import model.DatabaseManager;
import ui_funny_paint.component.button.ColorToggler;
import ui_funny_paint.panel.ColorPicker;
import ui_funny_paint.panel.ControlPanel;
import ui_funny_paint.panel.MainMenuBar;
import ui_funny_paint.panel.ToolPanel;
import ui_funny_paint.component.field.ToolSizeField;
import utils.GlobalKeyBinder;

import java.awt.*;
import java.util.Objects;

import static utils.LoadIcon.loadIcon;

@SuppressWarnings("serial")
public class MainFrame extends JFrame  {
	private ColorPicker colorPicker;
	private ToolPanel toolPanel;
	private JPanel canvasPanel;
	private ControlPanel controlPanel;
	private ColorToggler colorToggler;
	private ToolSizeField toolSizeField;

	private JScrollPane canvasContainer;

	private CanvasController controller;

	public MainFrame() {
	    super("Funny Paint");

	    Container mainPane = this.getContentPane();
	    mainPane.setLayout(new BorderLayout());


	    setPreferredSize(new Dimension(1280, 720));
	    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	    // what happens  when the window is closed
	    addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	boolean closeWindow = MainFrame.this.controller.closeCanvas();
	        	if(!closeWindow)
	        		return;

	        	DatabaseManager.disconnect();
	        	MainFrame.this.setVisible(false);
	        	MainFrame.this.dispose();
	        }
	    });

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				colorPicker.UpdateColorButtonSize();
			}
		});

	    // creating the components
	    colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setPreferredSize(new Dimension(200, 0));
		toolPanel = new ToolPanel();
		toolPanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		controlPanel = new ControlPanel();



        canvasPanel = new JPanel(new GridBagLayout());
		canvasPanel.setBackground(new Color(34, 32, 52));
        canvasContainer = new JScrollPane(canvasPanel);
		canvasContainer.setBorder(new CompoundBorder(
				BorderFactory.createEmptyBorder(0,0,25,0),
				BorderFactory.createLineBorder(Color.BLACK, 5)
		));

        colorToggler = new ColorToggler(Color.BLACK, Color.WHITE);
        controlPanel.add(colorToggler);
        // creating Main controller
        controller = new CanvasController(this);

        // creating a global key binder
        GlobalKeyBinder globalKeyBinder = new GlobalKeyBinder(this.getRootPane());
        globalKeyBinder.setController(controller);

        toolSizeField = new ToolSizeField();
        controlPanel.add(toolSizeField);
        toolSizeField.setController(controller);
		JPanel controlPanelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlPanelWrapper.add(controlPanel);




		// creating the menu bar
        MainMenuBar menuBar = new MainMenuBar(controller);
        this.setJMenuBar(menuBar);


        mainPane.add(canvasContainer, BorderLayout.CENTER);
        mainPane.add(colorPicker, BorderLayout.WEST);
        mainPane.add(toolPanel, BorderLayout.EAST);
        mainPane.add(controlPanelWrapper, BorderLayout.NORTH);

        // setting the icon
//        Toolkit tk = Toolkit.getDefaultToolkit();
        setIconImage(Objects.requireNonNull(loadIcon("logo.png")).getImage());

	    pack();
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

	public ControlPanel getControlPanel()
	{
		return this.controlPanel;
	}

	public ColorToggler getColorToggler() {

		return this.colorToggler;
	}

	public CanvasController getController() {
		return controller;
	}

}
