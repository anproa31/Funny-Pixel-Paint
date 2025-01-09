package controller.tools;

import java.awt.*;
import java.awt.event.MouseAdapter;


public abstract class Tool extends MouseAdapter {
    private Cursor cursor;

    public Tool() {
    }

    public Tool(Cursor cursor) {
        this();
        setCursor(cursor);
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

}
