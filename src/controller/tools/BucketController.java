package controller.tools;

import java.awt.*;
import java.awt.event.*;

import javax.swing.ImageIcon;

import ui_funny_paint.panel.PixelCanvas;


public class BucketController extends Tool {
	
	public BucketController() {
		super();
	}
	
    public BucketController(Cursor cursor) {
		super(cursor);
	}
    

	public void mousePressed(MouseEvent e)
    {
    	PixelCanvas canvas = (PixelCanvas) e.getSource();
    	Point coords = e.getPoint();
    	Color c = (e.getButton() == MouseEvent.BUTTON1) ? canvas.getPrimaryColor(): canvas.getSecondaryColor();
    	canvas.changeHappened();
    	canvas.floodFill(coords.x, coords.y, c);
    	canvas.repaint();
    }
    
}
