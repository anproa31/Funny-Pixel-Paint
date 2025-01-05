package controller.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import ui_funny_paint.panel.PixelCanvas;

public class BrushController extends Tool {
	private Point previousPoint; // Store the previous mouse position

	public BrushController() {
		super();
	}

	public BrushController(ImageIcon icon) {
		super(icon);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		Point coords = e.getPoint();

		// Store the initial point
		previousPoint = coords;

		// Indicate that a change has happened
		canvas.changeHappened();

		// Determine the color based on the mouse button
		Color c = ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
				? canvas.getPrimaryColor()
				: canvas.getSecondaryColor();

		// Draw a brush at the initial point
		canvas.drawBrush(coords.x, coords.y, c);
		canvas.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		Point currentPoint = e.getPoint();

		// Determine the color based on the mouse button
		Color c = ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
				? canvas.getPrimaryColor()
				: canvas.getSecondaryColor();

		// Draw a line between the previous and current points
		canvas.drawLine(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y, c);

		// Update the previous point
		previousPoint = currentPoint;

		canvas.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Reset the previous point when the mouse is released
		previousPoint = null;
	}
}
