package controller.tools;

import java.awt.*;
import java.awt.event.*;

import javax.swing.ImageIcon;

import ui_funny_paint.panel.PixelCanvas;


public class EyeDropperController extends Tool {
	
	public EyeDropperController() {
		super();
	}
	
    public EyeDropperController(Cursor cursor) {
		super(cursor);
	}

	public void mousePressed(MouseEvent e)
    {
    	PixelCanvas canvas = (PixelCanvas) e.getSource();
    	Point coords = e.getPoint();
    	canvas.eyeDrop(coords.x, coords.y);
    }
    
}
