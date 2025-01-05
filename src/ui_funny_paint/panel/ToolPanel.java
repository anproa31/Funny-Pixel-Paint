package ui_funny_paint.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.canvas.CanvasController;
import controller.tools.BrushController;
import controller.tools.BucketController;
import controller.tools.EraserController;
import controller.tools.EyeDropperController;

@SuppressWarnings("serial")
public class ToolPanel extends JToolBar {
	private CanvasController controller;
	
	private BrushController brush;
	private BucketController bucket;
	private EyeDropperController eyeDropper;
	private EraserController eraser;

	
	
	
	public ToolPanel() {
		super("Tools", JToolBar.VERTICAL);
		
		ButtonListener listener = new ButtonListener();
		
		this.brush = new BrushController();
		this.bucket = new BucketController();
		this.eyeDropper = new EyeDropperController();
		this.eraser = new EraserController();
		
		
		String[] buttonLabels = { "Brush", "Eraser", "EyeDropper", "Bucket"};
		ImageIcon[] buttonIcons = {
				new ImageIcon("resources/paintbrush.png"),
				new ImageIcon("resources/eraser.png"),
				new ImageIcon("resources/eyedropper.png"),
				new ImageIcon("resources/bucket.png")
				};
		
		JButton[] buttons = new JButton[buttonLabels.length];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(buttonIcons[i]);
			buttons[i].setSize(200, 200);
			buttons[i].setActionCommand(buttonLabels[i]);
			buttons[i].setToolTipText(buttonLabels[i]);
			buttons[i].addActionListener(listener);
			this.add(buttons[i]);
			this.addSeparator();
		}
		
		setFloatable(false);
	}

	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
				
			case "Bucket":
				controller.setCanvasTool(bucket);
				break;
				
			case "Brush":
				controller.setCanvasTool(brush);
				break;
				
			case "EyeDropper":
				controller.setCanvasTool(eyeDropper);
				break;
				
			case "Eraser":
				controller.setCanvasTool(eraser);
				break;
			}
		}
	}
	

	public void setController(CanvasController controller)
	{
		this.controller = controller;
	}
	
}
