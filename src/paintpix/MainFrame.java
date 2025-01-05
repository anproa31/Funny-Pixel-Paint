package paintpix;


import java.awt.event.*;
import javax.swing.*;

import managers.DatabaseManager;

import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame  {
	private ColorPicker colorPicker;
	private ToolPanel toolPanel;
	private JPanel canvasPanel;
	private ControlPanel controlPanel;
	private ColorToggler colorToggler;
	private ToolSizeSlider sizeSlider;
	
	private JScrollPane canvasContainer;
	private MainController controller;
	
	public MainFrame() {
	    super("Funny Paint");


	    Container mainPane = this.getContentPane();
	    mainPane.setLayout(new BorderLayout());
	    
	    setPreferredSize(new Dimension(1500, 1000));
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
	  
	    // creating the components
	    colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setPreferredSize(new Dimension(550, 600));
		toolPanel = new ToolPanel();
		controlPanel = new ControlPanel();
		
		
        canvasPanel = new JPanel(new GridBagLayout());
        canvasContainer = new JScrollPane(canvasPanel);
        colorToggler = new ColorToggler(Color.BLACK, Color.WHITE);
        controlPanel.add(colorToggler);

        // creating Main controller
        controller = new MainController(this);
        
        // creating a global key binder
        GlobalKeyBinder globalKeyBinder = new GlobalKeyBinder(this.getRootPane());
        globalKeyBinder.setController(controller);
        
        sizeSlider = new ToolSizeSlider();
        controlPanel.add(sizeSlider);
        sizeSlider.setController(controller);
        
        

        // creating the menu bar
        MainMenuBar menuBar = new MainMenuBar(controller);
        this.setJMenuBar(menuBar);
        
        
        mainPane.add(canvasContainer, BorderLayout.CENTER);
        mainPane.add(colorPicker, BorderLayout.WEST);
        mainPane.add(toolPanel, BorderLayout.EAST);
        mainPane.add(controlPanel, BorderLayout.NORTH);
        
        // setting the icon
        Toolkit tk = Toolkit.getDefaultToolkit();
        setIconImage(tk.getImage("resources/logo.png"));
        
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

	
}