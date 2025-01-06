//package ui_funny_paint.panel;
//
//import controller.canvas.CanvasController;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.*;
//
//@SuppressWarnings("serial")
//public class ControlPanel extends JToolBar {
//	private CanvasController controller;
//
//
//	public ControlPanel() {
//		super("Controls", JToolBar.HORIZONTAL);
//
//		ButtonListener listener = new ButtonListener();
//
//		String[] buttonLabels = {"New", "Open", "Save As", "Undo", "Redo"};
//		ImageIcon[] buttonIcons = {
//				new ImageIcon("res/new.png"),
//				new ImageIcon("res/open.png"),
//				new ImageIcon("res/save.png"),
//				new ImageIcon("res/undo.png"),
//				new ImageIcon("res/redo.png"),
//				};
//
//		JButton[] buttons = new JButton[buttonLabels.length];
//		for (int i = 0; i < buttons.length; i++) {
//			buttons[i] = new JButton(buttonIcons[i]);
//			buttons[i].setActionCommand(buttonLabels[i]);
//			buttons[i].setToolTipText(buttonLabels[i]);
//			buttons[i].addActionListener(listener);
//			buttons[i].setMargin(new Insets(5,15,5,15));
//			this.add(buttons[i]);
//		}
//
//		setFloatable(false);
//
//
//	}
//
//	private class ButtonListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			switch(e.getActionCommand()) {
//			case "New":
//				controller.createNewCanvas();
//				break;
//			case "Open":
//				controller.openCanvasFromFileSystem();
//				break;
//
//			case "Save As":
//				if(controller.getCanvas() == null) return;
//				controller.saveCanvasAs();
//				break;
//
//			case "Undo":
//				if(controller.getCanvas() == null) return;
//				controller.getCanvas().undo();
//				break;
//
//			case "Redo":
//				if(controller.getCanvas() == null) return;
//				controller.getCanvas().redo();
//				break;
//
//			}
//		}
//	}
//
//
//	public void setController(CanvasController controller)
//	{
//		this.controller = controller;
//	}
//
//}


package ui_funny_paint.panel;

import controller.canvas.CanvasController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static utils.LoadIcon.loadIcon;

@SuppressWarnings("serial")
public class ControlPanel extends JToolBar {
	private CanvasController controller;

	public ControlPanel() {
		super("Controls", JToolBar.HORIZONTAL);

		ButtonListener listener = new ButtonListener();

		String[] buttonLabels = {"New", "Open", "Save As", "Undo", "Redo"};
		ImageIcon[] buttonIcons = {
				loadIcon("new.png"),
				loadIcon("open.png"),
				loadIcon("save.png"),
				loadIcon("undo.png"),
				loadIcon("redo.png"),
		};

		JButton[] buttons = new JButton[buttonLabels.length];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(buttonIcons[i]);
			buttons[i].setActionCommand(buttonLabels[i]);
			buttons[i].setToolTipText(buttonLabels[i]);
			buttons[i].addActionListener(listener);
			buttons[i].setMargin(new Insets(5, 15, 5, 15));
			this.add(buttons[i]);
		}

		setFloatable(false);
	}

	/**
	 * Loads an image icon from the classpath.
	 *
	 * @param filename The name of the image file (e.g., "new.png").
	 * @return The loaded ImageIcon, or null if the image could not be loaded.
	 */


	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
				case "New":
					controller.createNewCanvas();
					break;
				case "Open":
					controller.openCanvasFromFileSystem();
					break;
				case "Save As":
					if (controller.getCanvas() == null) return;
					controller.saveCanvasAs();
					break;
				case "Undo":
					if (controller.getCanvas() == null) return;
					controller.getCanvas().undo();
					break;
				case "Redo":
					if (controller.getCanvas() == null) return;
					controller.getCanvas().redo();
					break;
			}
		}
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
	}
}
