package utils;

import java.awt.*;
import java.util.Objects;

import static utils.LoadIcon.loadIcon;

public class CursorManager {
    private final static Image defaultCursorImage = Objects.requireNonNull(loadIcon("cursor.png")).getImage();
    private final static Image brushCursorImage = Objects.requireNonNull(loadIcon("brushCursor.png")).getImage();
    private final static Image eraserCursorImage = Objects.requireNonNull(loadIcon("eraserCursor.png")).getImage();
    private final static Image eyedropperCursorImage = Objects.requireNonNull(loadIcon("eyedropperCursor.png")).getImage();
    private final static Image bucketCursorImage = Objects.requireNonNull(loadIcon("bucketCursor.png")).getImage();

    public final static Cursor defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(defaultCursorImage, new Point(0,0), "defaultCursor");
    public final static Cursor brushCursor = Toolkit.getDefaultToolkit().createCustomCursor(brushCursorImage, new Point(14,14), "brushCursor");
    public final static Cursor eraserCursor = Toolkit.getDefaultToolkit().createCustomCursor(eraserCursorImage, new Point(0,0), "eraserCursor");
    public final static Cursor eyedropperCursor = Toolkit.getDefaultToolkit().createCustomCursor(eyedropperCursorImage, new Point(0,31), "eyedroppeerCursor");
    public final static Cursor bucketCursor = Toolkit.getDefaultToolkit().createCustomCursor(bucketCursorImage, new Point(0,0), "bucketCursor");


    public static void setCustomCursor(Component component, Cursor cursor) {
        if (component == null || cursor == null) {
            return;
        }
        component.setCursor(cursor);
    }
}
