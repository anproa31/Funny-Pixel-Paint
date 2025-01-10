package controller.tools;

import ui_funny_paint.panel.PixelCanvas;

import java.awt.*;
import java.awt.event.MouseEvent;


public class EyeDropperController extends Tool {

    public EyeDropperController(Cursor cursor) {
        super(cursor);
    }

    public void mousePressed(MouseEvent e) {
        PixelCanvas canvas = (PixelCanvas) e.getSource();
        Point coords = e.getPoint();
        canvas.eyeDrop(coords.x, coords.y);
    }

}
