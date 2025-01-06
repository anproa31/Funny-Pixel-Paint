package utils;

import java.awt.*;

public class CursorManager {
    private Image defaultCursor;
    private Image brushCursor;
    private Image eraserCursor;
    private Image eyedropperCursor;
    private Image bucketCursor;

    public CursorManager()
    {
        SetupCursorImage();
    }

    private void SetupCursorImage()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.defaultCursor = toolkit.getImage("resources/cursor.png");
        this.brushCursor = toolkit.getImage("resources/paintbrush.png");
        this.eraserCursor = toolkit.getImage("resources/eraser.png");
        this.eyedropperCursor = toolkit.getImage("resources/eyedropper.png");
        this.bucketCursor = toolkit.getImage("resources/bucket.png");
    }


}
