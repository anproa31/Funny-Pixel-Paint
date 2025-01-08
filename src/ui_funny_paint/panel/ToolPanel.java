package ui_funny_paint.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.canvas.CanvasController;
import controller.tools.*;
import ui_funny_paint.screen.MainFrame;
import utils.CursorManager;

import static utils.CursorManager.*;
import static utils.LoadIcon.loadIcon;

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
		
		this.brush = new BrushController(brushCursor);
		this.bucket = new BucketController(bucketCursor);
		this.eyeDropper = new EyeDropperController(eyedropperCursor);
		this.eraser = new EraserController(eraserCursor);

		String[] buttonLabels = {"Brush", "Eraser", "EyeDropper", "Bucket"};
		ImageIcon[] buttonIcons = {
				loadIcon("paintbrush.png"),
				loadIcon("eraser.png"),
				loadIcon("eyedropper.png"),
				loadIcon("bucket.png"),
		};
		
		JButton[] buttons = new JButton[buttonLabels.length];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(buttonIcons[i]);
			buttons[i].setActionCommand(buttonLabels[i]);
			buttons[i].setToolTipText(buttonLabels[i]);
			buttons[i].addActionListener(listener);
			buttons[i].setMargin(new Insets(5,10,5,10));

			this.add(buttons[i]);
		}
		
		setFloatable(false);
	}

	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
				
			case "Bucket":
				controller.setCanvasTool(bucket);
				setCustomCursor(MainFrame.getInstance(), bucketCursor);
				break;
				
			case "Brush":
				controller.setCanvasTool(brush);
				setCustomCursor(MainFrame.getInstance(), brushCursor);
				break;
				
			case "EyeDropper":
				controller.setCanvasTool(eyeDropper);
				setCustomCursor(MainFrame.getInstance(), eyedropperCursor);
				break;
				
			case "Eraser":
				controller.setCanvasTool(eraser);
				setCustomCursor(MainFrame.getInstance(), eraserCursor);
				break;
			}
		}
	}


	public CanvasController getController() {
		return controller;
	}

	public BrushController getBrush() {
		return brush;
	}

	public void setBrush(BrushController brush) {
		this.brush = brush;
	}

	public BucketController getBucket() {
		return bucket;
	}

	public void setBucket(BucketController bucket) {
		this.bucket = bucket;
	}

	public EyeDropperController getEyeDropper() {
		return eyeDropper;
	}

	public void setEyeDropper(EyeDropperController eyeDropper) {
		this.eyeDropper = eyeDropper;
	}

	public EraserController getEraser() {
		return eraser;
	}

	public void setEraser(EraserController eraser) {
		this.eraser = eraser;
	}

	public void setController(CanvasController controller)
	{
		this.controller = controller;
	}
	
}
