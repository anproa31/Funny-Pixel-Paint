package ui_funny_paint.panel;

import controller.canvas.CanvasController;
import controller.canvas.RedoUndoController;
import controller.tools.Tool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class PixelCanvas extends JComponent implements Serializable {
    transient private BufferedImage pixels;

    private String savePath;
    private int saveWidth;
    private int saveHeight;
    private boolean changedAfterSave = true;

    private double scaleFactor;
    private int width, height;
    private Color primaryColor;
    private Color secondaryColor;

    transient private Tool selectedTool;
    transient private RedoUndoController undoManager;
    transient private CanvasController controller;
    final transient private MouseAdapter mouseAdapter;

    private PixelCanvas() {
        this.mouseAdapter = new MouseAdapter() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                double scale = e.getPreciseWheelRotation();
                PixelCanvas.this.zoom(scale);
            }
        };

        this.undoManager = new RedoUndoController();

    }

    public PixelCanvas(int width, int height) {
        this();
        this.pixels = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.pixels.setAccelerationPriority(1);
        this.scaleFactor = 1.0F;
        this.width = width + 2;
        this.height = height + 2;
        this.primaryColor = new Color(0, 0, 0, 255);
        this.secondaryColor = new Color(255, 255, 255, 0);
        this.setPreferredSize(new Dimension(width + 2, height + 2));


        addMouseWheelListener(mouseAdapter);
    }

    public PixelCanvas(BufferedImage image) {
        this();
        this.pixels = image;
        this.scaleFactor = 1.0F;
        this.width = image.getWidth() + 2;
        this.height = image.getHeight() + 2;

        this.primaryColor = new Color(0, 0, 0, 255);
        this.secondaryColor = new Color(255, 255, 255, 255);

        addMouseWheelListener(mouseAdapter);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) Math.round(pixels.getWidth() * scaleFactor + 2),
                (int) Math.round(pixels.getHeight() * scaleFactor + 2));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Recalculate the Image dimensions
        this.width = (int) Math.round(pixels.getWidth() * scaleFactor + 2);
        this.height = (int) Math.round(pixels.getHeight() * scaleFactor + 2);
        this.revalidate();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        g2d.setComposite(comp);

        drawChessboard(g2d);

        // Draw the canvas image
        g2d.drawImage(this.pixels, 1, 1, width - 2, height - 2, null);

        // Draw one pixel wide border around the canvas
        g2d.setColor(Color.decode("#101010"));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(0, 0, width - 1, height - 1);


        g2d.dispose();
    }


    private void drawChessboard(Graphics2D g2d) {
        int scaledWidth = (int) (pixels.getWidth() * scaleFactor);
        int scaledHeight = (int) (pixels.getHeight() * scaleFactor);
        int baseSquareSize = 16;

        int scaledSquareSize = (int) Math.max(1, Math.round(baseSquareSize * scaleFactor));

        for (int y = 1; y <= scaledHeight; y += scaledSquareSize) {
            for (int x = 1; x <= scaledWidth; x += scaledSquareSize) {
                int gridX = (x - 1) / scaledSquareSize;
                int gridY = (y - 1) / scaledSquareSize;

                if ((gridX + gridY) % 2 == 0) {
                    g2d.setColor(new Color(192, 192, 192));
                } else {
                    g2d.setColor(new Color(128, 128, 128));
                }

                int width = Math.min(scaledSquareSize, scaledWidth - x + 1);
                int height = Math.min(scaledSquareSize, scaledHeight - y + 1);
                g2d.fillRect(x, y, width, height);
            }
        }
    }

    public void fill(Color c) {
        Graphics2D g2d = this.pixels.createGraphics();
        g2d.setColor(c);
        g2d.setComposite(AlphaComposite.Src);
        g2d.fillRect(0, 0, this.pixels.getWidth(), this.pixels.getHeight());
        repaint();
    }

    public void zoom(double scale) {
        JPanel canvasPanel = this.controller.getCanvasPanel();
        double oldScale = this.scaleFactor;


        double zoomFactor = scale < 0 ? 1 : -1;
        double newScale = this.scaleFactor + zoomFactor;

        // Ensure the scale factor is at least 1
        newScale = Math.max(1, newScale);

        // Snap to the nearest integer
        newScale = Math.round(newScale);

        this.scaleFactor = newScale;

		Rectangle visibleRect = getVisibleRect(oldScale, canvasPanel);
		canvasPanel.scrollRectToVisible(visibleRect);

        repaint();
    }

	private Rectangle getVisibleRect(double oldScale, JPanel canvasPanel) {
		double scaleChange = this.scaleFactor / oldScale;

		Rectangle visibleRect = canvasPanel.getVisibleRect();
		double centerX = visibleRect.getX() + visibleRect.getWidth() / 2;
		double centerY = visibleRect.getY() + visibleRect.getHeight() / 2;

		double newCenterX = centerX * scaleChange;
		double newCenterY = centerY * scaleChange;

		double scrollX = newCenterX - visibleRect.getWidth() / 2;
		double scrollY = newCenterY - visibleRect.getHeight() / 2;

		visibleRect.setRect(scrollX, scrollY, visibleRect.getWidth(), visibleRect.getHeight());
		return visibleRect;
	}

	public void drawBrush(int x, int y, Color c) {
        int brushSize = controller.getSize();
        int realX = this.getScaledCoord(x, brushSize);
        int realY = this.getScaledCoord(y, brushSize);

        Graphics2D g2d = this.pixels.createGraphics();
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) c.getAlpha() / 255f);

        g2d.setColor(c);
        g2d.setComposite(comp);
        g2d.fillRect(realX, realY, brushSize, brushSize);

        g2d.dispose();
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color c) {
		int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            // Draw a brush at the current position
            drawBrush(x1, y1, c);

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    public void erase(int x, int y) {
        int brushSize = controller.getSize();
        int realX = this.getScaledCoord(x, brushSize);
        int realY = this.getScaledCoord(y, brushSize);

        Graphics2D g2d = this.pixels.createGraphics();
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.setComposite(AlphaComposite.Src);
        g2d.fillRect(realX, realY, brushSize, brushSize);

        g2d.dispose();
    }

    public void eraseLine(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            // Erase at the current position
            erase(x1, y1);

            // Stop if we've reached the end point
            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    public void floodFill(int x, int y, Color c) {
        int realX = getScaledCoord(x, 1);
        int realY = getScaledCoord(y, 1);

        int targetColor = this.pixels.getRGB(realX, realY);
        int replaceColor = c.getRGB();

        // Sanity checks
        if (targetColor == replaceColor)
            return;
        Point node = new Point(realX, realY);
        Queue<Point> q = new LinkedList<>();
        q.add(node);

        while (!q.isEmpty()) {
            Point n = q.poll();
            Point west = (Point) n.clone();
            Point east = (Point) n.clone();

            while (west.x > -1 && this.pixels.getRGB(west.x, west.y) == targetColor)
                west.x--;

            while (east.x < this.pixels.getWidth() && this.pixels.getRGB(east.x, east.y) == targetColor)
                east.x++;

            for (int i = west.x + 1; i < east.x; i++) {
                int yLevel = west.y;
                this.pixels.setRGB(i, yLevel, replaceColor);

                if (west.y < this.pixels.getHeight() - 1 && this.pixels.getRGB(i, yLevel + 1) == targetColor)
                    q.add(new Point(i, yLevel + 1));
                if (west.y > 0 && this.pixels.getRGB(i, yLevel - 1) == targetColor)
                    q.add(new Point(i, yLevel - 1));
            }
        }
    }

    public void changeHappened() {
        this.changedAfterSave = true;
        this.undoManager.changeHappened(this.pixels.getData());
    }

    public void undo() {
        Raster image = this.undoManager.undo(this.pixels.getData());
        if (image == null)
            return;

        this.pixels.setData(image);
        repaint();
    }

    public void redo() {
        Raster image = this.undoManager.redo(this.pixels.getData());
        if (image == null)
            return;

        this.pixels.setData(image);
        repaint();
    }


	public byte[] toBytes() {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ObjectOutputStream out;
			out = new ObjectOutputStream(bos);
			// Serialize the whole object
			out.writeObject(this);
			out.flush();

			// Serialize the pixels
			ImageIO.setUseCache(false);
			ImageIO.write(this.pixels, "png", out);

            return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Ignore close exception

		return null;
	}

	public static PixelCanvas fromBytes(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try (ObjectInputStream in = new ObjectInputStream(bis)) {

			PixelCanvas canvas = (PixelCanvas) in.readObject();

            canvas.pixels = ImageIO.read(in);
			canvas.addMouseWheelListener(new MouseAdapter() {

				public void mouseWheelMoved(MouseWheelEvent e) {
					double scale = e.getPreciseWheelRotation();
                    canvas.zoom(scale);
				}
			});

			canvas.undoManager = new RedoUndoController();
			return canvas;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Ignore close exception

		return null;
	}

    public boolean canUndo() {
        return this.undoManager.canUndo();
    }

    public boolean canRedo() {
        return this.undoManager.canRedo();
    }

    public void eyeDrop(int x, int y) {
        int rgb = this.pixels.getRGB(getScaledCoord(x, 1), getScaledCoord(y, 1));
        Color c = new Color(rgb);

        this.controller.getColorPicker().setColor(c);
        this.controller.getColorToggler().setColor(c);
    }

    public BufferedImage getImage() {
        return this.pixels;
    }



    public int getScaledCoord(int coord, int size) {
        return (int) Math.round((coord - 1) / scaleFactor - (size / 2.0f));
    }




    public void setSelectedTool(Tool selectedTool) {
        if (this.selectedTool != null) {
            removeMouseListener(this.selectedTool);
            removeMouseMotionListener(this.selectedTool);
        }
        this.selectedTool = selectedTool;
        addMouseMotionListener(selectedTool);
        addMouseListener(selectedTool);
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }


    public void setScale(double scale) {
        this.scaleFactor = scale;
        this.repaint();
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.changedAfterSave = false;
        this.savePath = savePath;
    }

    public int getSaveWidth() {
        return saveWidth;
    }

    public void setSaveWidth(int saveWidth) {
        this.saveWidth = saveWidth;
    }

    public int getSaveHeight() {
        return saveHeight;
    }

    public void setSaveHeight(int saveHeight) {
        this.saveHeight = saveHeight;
    }

    public boolean isChangedAfterSave() {
        return this.changedAfterSave;
    }

    public void setController(CanvasController controller) {
        this.controller = controller;
    }
}