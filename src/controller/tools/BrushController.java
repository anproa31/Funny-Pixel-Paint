package controller.tools;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import ui_funny_paint.panel.PixelCanvas;

public class BrushController extends Tool {
	private Point previousPoint;

	public BrushController() {
		super();
	}

	public BrushController(Cursor cursor) {
		super(cursor);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		Point coords = e.getPoint();

		previousPoint = coords;

		canvas.changeHappened();

		Color c = ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
				? canvas.getPrimaryColor()
				: canvas.getSecondaryColor();

		canvas.drawBrush(coords.x, coords.y, c);
		canvas.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		PixelCanvas canvas = (PixelCanvas) e.getSource();
		Point currentPoint = e.getPoint();

		Color c = ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK)
				? canvas.getPrimaryColor()
				: canvas.getSecondaryColor();

		canvas.drawLine(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y, c);

		previousPoint = currentPoint;

		canvas.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		previousPoint = null;
	}
}
