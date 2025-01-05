package controller.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import ui_funny_paint.panel.PixelCanvas;

public class EraserController extends Tool {
	private boolean pressed;
	private Point lastPoint; // Store the last mouse position

	public EraserController() {
		super();
		this.pressed = false;
		this.lastPoint = null;
	}

	public EraserController(ImageIcon icon) {
		super(icon);
		this.pressed = false;
		this.lastPoint = null;
	}

	public void mousePressed(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		Point coords = e.getPoint();
		if (!this.pressed) {
			canvas.changeHappened();
		}
		this.pressed = true;
		this.lastPoint = coords; // Store the initial point
		canvas.erase(coords.x, coords.y);
		canvas.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		Point coords = e.getPoint();
		this.pressed = true;

		if (lastPoint != null) {
			// Erase along the line between the last point and the current point
			canvas.eraseLine(lastPoint.x, lastPoint.y, coords.x, coords.y);
		}

		this.lastPoint = coords; // Update the last point to the current position
		canvas.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		this.pressed = false;
		this.lastPoint = null; // Reset the last point
		canvas.repaint();
	}
}
