package controller.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import ui_funny_paint.panel.PixelCanvas;

public class EraserController extends Tool{
	private boolean pressed;
	
	public EraserController() {
		super();
		this.pressed = false;
	}
	
	public EraserController(ImageIcon icon) {
		super(icon);
		this.pressed = false;
	}
	
    public void mousePressed(MouseEvent e)
    {
    	PixelCanvas canvas = (PixelCanvas) e.getSource();
    	Point coords = e.getPoint();
    	if(!this.pressed)
    		canvas.changeHappened();
    	this.pressed = true;
    	canvas.erase(coords.x, coords.y);
    	canvas.repaint();
    }

    public void mouseDragged(MouseEvent e)
    {
    	PixelCanvas canvas = (PixelCanvas) e.getSource();
    	Point coords = e.getPoint();
    	this.pressed = true;
    	
    	canvas.erase(coords.x, coords.y);
    	canvas.repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
    	PixelCanvas canvas = (PixelCanvas) e.getSource();
    	this.pressed = false;
    	canvas.repaint();

    }

}
