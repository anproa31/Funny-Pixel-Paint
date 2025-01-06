package utils;

import controller.canvas.CanvasController;

import java.awt.Event;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;


@SuppressWarnings("serial")
public class GlobalKeyBinder {
	private CanvasController controller;

	public GlobalKeyBinder(JRootPane root) {
		KeyStroke undoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		KeyStroke saveKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
		KeyStroke newKey = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
		KeyStroke openKey = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK);
		KeyStroke swapColorsKey = KeyStroke.getKeyStroke('x');
		KeyStroke brushKey = KeyStroke.getKeyStroke('b');
		KeyStroke eraserKey = KeyStroke.getKeyStroke('e');
		KeyStroke eyedropperKey = KeyStroke.getKeyStroke('i');
		KeyStroke fillKey = KeyStroke.getKeyStroke('g');

		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKey, "undo");
		root.getActionMap().put("undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.getCanvas().undo();
			}
		});
		
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKey, "redo");
		root.getActionMap().put("redo", new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.getCanvas().redo();
			}
		});
		
		
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKey, "save");
		root.getActionMap().put("save", new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.saveCanvas();
			}
		});
		
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(openKey, "open");
		root.getActionMap().put("open", new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.openCanvasFromFileSystem();
			}
		});
		
		
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(newKey, "new");
		root.getActionMap().put("new", new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.createNewCanvas();
			}
		});
		
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(swapColorsKey, "swapColors");
		root.getActionMap().put("swapColors", new AbstractAction() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.getColorToggler().swapColors();
			}
		});

		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(brushKey, "brushKey");
		root.getActionMap().put("brushKey", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.setCanvasTool(controller.getTools().getBrush());
			}
		});

		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(eyedropperKey, "setEyeDropper");
		root.getActionMap().put("setEyeDropper", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.setCanvasTool(controller.getTools().getEyeDropper());
			}
		});

		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(eraserKey, "setEraser");
		root.getActionMap().put("setEraser", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.setCanvasTool(controller.getTools().getEraser());
			}
		});

		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(fillKey, "setFill");
		root.getActionMap().put("setFill", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GlobalKeyBinder.this.controller.setCanvasTool(controller.getTools().getBucket());
			}
		});
	}

	public void setController(CanvasController controller)
	{
		this.controller = controller;
	}
	
}
