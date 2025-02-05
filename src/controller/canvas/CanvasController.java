package controller.canvas;

import controller.tools.Tool;
import model.DatabaseManager;
import model.ImageFileManager;
import ui_funny_paint.component.button.ColorToggler;
import ui_funny_paint.component.dialogs.NewCanvasDialog;
import ui_funny_paint.component.dialogs.SaveCanvasDialog;
import ui_funny_paint.panel.ColorPicker;
import ui_funny_paint.panel.ControlPanel;
import ui_funny_paint.panel.PixelCanvas;
import ui_funny_paint.panel.ToolPanel;
import ui_funny_paint.screen.MainFrame;
import utils.CursorManager;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class CanvasController {
    private final MainFrame mainFrame;
    private final ToolPanel toolPanel;
    private final ColorPicker colorPicker;
    private final ColorToggler colorToggler;
    private final JPanel canvasPanel;
    private PixelCanvas canvas;
    private int size;
    private Tool activeTool;


    public CanvasController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.toolPanel = mainFrame.getTools();

        this.colorPicker = mainFrame.getColorPicker();
        this.canvasPanel = mainFrame.getCanvasPanel();
        ControlPanel controlPanel = mainFrame.getControlPanel();
        this.colorToggler = mainFrame.getColorToggler();

        this.size = 1;
        this.colorToggler.setController(this);
        controlPanel.setController(this);
        this.toolPanel.setController(this);
        this.colorPicker.setController(this);
    }

    public void createNewCanvas() {
        NewCanvasDialog d = new NewCanvasDialog(this.mainFrame);
        int closeOption = d.showOpenDialog();
        if (closeOption == NewCanvasDialog.APPROVE_OPTION) {
            boolean created = createCanvas(d.getChosenWidth(), d.getChosenHeight());
            if (created)
                this.canvas.fill(d.getChosenFillColor());
        }
    }

    public void createCanvas(PixelCanvas newCanvas) {
        boolean closedCanvas = this.closeCanvas();
        if (!closedCanvas)
            return;

        this.canvas = newCanvas;
        this.canvas.setSelectedTool(this.activeTool);
        this.canvas.setScale(this.calculateScale());
        this.canvas.setController(this);

        this.canvasPanel.add(this.canvas);
        this.colorToggler.updateCanvas();
        this.mainFrame.getCanvasContainer().repaint();
        this.mainFrame.revalidate();
    }

    public boolean createCanvas(int width, int height) {
        boolean closedCanvas = this.closeCanvas();
        if (!closedCanvas)
            return false;

        this.canvas = new PixelCanvas(width, height);
        this.canvas.setSelectedTool(this.activeTool);

        this.canvas.setScale(this.calculateScale());
        this.canvas.setController(this);

        this.canvasPanel.add(this.canvas);
        this.colorToggler.updateCanvas();
        this.mainFrame.getCanvasContainer().repaint();
        this.mainFrame.revalidate();
        return true;
    }


    public boolean createCanvas(BufferedImage image) {
        boolean closedCanvas = this.closeCanvas();
        if (!closedCanvas)
            return false;


        this.canvas = new PixelCanvas(image);
        this.canvas.setSelectedTool(this.activeTool);
        this.canvas.setScale(this.calculateScale());
        this.canvas.setController(this);
        this.canvasPanel.add(this.canvas);
        this.colorToggler.updateCanvas();
        this.mainFrame.getCanvasContainer().repaint();
        this.mainFrame.revalidate();
        return true;
    }

    public void setCanvasTool(Tool t) {
        this.activeTool = t;
        CursorManager.setCustomCursor(MainFrame.getInstance().getCanvasContainer(), t.getCursor());
        if (this.canvas != null) this.canvas.setSelectedTool(t);
    }


    public void openCanvasFromFileSystem() {
        BufferedImage image = ImageFileManager.open();
        if (image == null)
            return;
        boolean created = this.createCanvas(image);
        if (!created)
            return;

        this.canvas.setSavePath(ImageFileManager.latestPath);
        this.canvas.setSaveHeight(this.canvas.getImage().getHeight());
        this.canvas.setSaveWidth(this.canvas.getImage().getWidth());
    }


    public void saveCanvas() {
        if (this.canvas == null) return;

        String path = this.canvas.getSavePath();
        if (path == null) {
            saveCanvasAs();
        } else {
            BufferedImage original = this.canvas.getImage();
            BufferedImage scaled = ImageFileManager.resize(original, this.canvas.getSaveWidth(), this.canvas.getSaveHeight());
            ImageFileManager.save(scaled, path);
        }
    }

    public boolean closeCanvas() {
        if (this.canvas == null)
            return true;

        if (!this.canvas.isChangedAfterSave()) {
            this.removeCanvas();
            return true;
        }

        int choice = JOptionPane.showConfirmDialog(this.mainFrame, "Do you want to save?", "Closing Canvas", JOptionPane.YES_NO_CANCEL_OPTION);

        switch (choice) {
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.OK_OPTION:
                this.saveCanvas();
                break;
            default:
                return false;
        }

        this.removeCanvas();
        return true;
    }


    public void removeCanvas() {
        if (this.canvas == null)
            return;

        this.canvasPanel.remove(this.canvas);
        this.canvasPanel.repaint();

        byte[] canvasData = this.canvas.toBytes();

        // insert canvas to database
        String name = "Untitled";
        if (this.canvas.getSavePath() != null)
            name = this.canvas.getSavePath().split("/")[this.canvas.getSavePath().split("/").length - 1];

        DatabaseManager.insert(name, canvasData);

        this.canvas = null;
        this.mainFrame.revalidate();
    }

    public void saveCanvasAs() {
        if (this.canvas == null)
            return;

        SaveCanvasDialog d = new SaveCanvasDialog(this.mainFrame, this.canvas);

        int closeOption = d.showOpenDialog();

        if (closeOption != SaveCanvasDialog.APPROVE_OPTION)
            return;

        BufferedImage scaled = ImageFileManager.resize(this.canvas.getImage(), d.getChosenWidth(), d.getChosenHeight());
        ImageFileManager.save(scaled, d.getChosenAbsolutePath());
        this.canvas.setSavePath(d.getChosenAbsolutePath());
        this.canvas.setSaveWidth(d.getChosenWidth());
        this.canvas.setSaveHeight(d.getChosenHeight());
    }

    public double calculateScale() {
        if (this.canvas == null)
            return 0;
        double widthScale = (double) (this.mainFrame.getWidth() / 2) / this.canvas.getImage().getWidth();
        double heightScale = (double) (this.mainFrame.getHeight() / 2) / this.canvas.getImage().getHeight();
        return Math.min(widthScale, heightScale);
    }

    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

    public PixelCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(PixelCanvas canvas) {
        this.canvas = canvas;
    }

    public ToolPanel getTools() {
        return toolPanel;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public JPanel getCanvasPanel() {
        return this.canvasPanel;
    }

    public ColorToggler getColorToggler() {
        return colorToggler;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
