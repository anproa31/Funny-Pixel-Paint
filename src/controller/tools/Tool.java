package controller.tools;

import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;



public abstract class Tool extends MouseAdapter {
	private Cursor cursor;

	public Tool() {
	}

	public Tool(Cursor cursor)
	{
		this();
		setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return this.cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

}
