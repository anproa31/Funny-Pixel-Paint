package controller.tools;

import ui_funny_paint.panel.PixelCanvas;

import java.awt.*;
import java.awt.event.MouseEvent;

public class EraserController extends Tool {
    private boolean pressed;
    private Point lastPoint;

    public EraserController(Cursor cursor) {
        super(cursor);
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
        this.lastPoint = coords;
        canvas.erase(coords.x, coords.y);
        canvas.repaint();
    }

    public void mouseDragged(MouseEvent e) {
        PixelCanvas canvas = (PixelCanvas) e.getSource();
        Point coords = e.getPoint();
        this.pressed = true;

        if (lastPoint != null) {
            canvas.eraseLine(lastPoint.x, lastPoint.y, coords.x, coords.y);
        }

        this.lastPoint = coords;
        canvas.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        PixelCanvas canvas = (PixelCanvas) e.getSource();
        this.pressed = false;
        this.lastPoint = null;
        canvas.repaint();
    }
}
