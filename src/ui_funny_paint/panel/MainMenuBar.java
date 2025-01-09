package ui_funny_paint.panel;

import controller.canvas.CanvasController;
import model.CanvasDatabaseObject;
import model.DatabaseManager;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static utils.LoadIcon.loadIcon;

public class MainMenuBar extends JMenuBar {
    private CanvasController controller;
    private JMenu fileMenu;
    private JMenu editMenu;

    public MainMenuBar() {
        super();
        setupFileMenu();
        setupEditMenu();

        this.add(fileMenu);
        this.add(editMenu);
        this.revalidate();
    }

    private void setupEditMenu() {
        editMenu = new JMenu("Edit");

        JMenuItem undoMenuItem = new JMenuItem("Undo", KeyEvent.VK_U);
        undoMenuItem.setIcon(loadIcon("undo_s.png"));
        undoMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.getCanvas() == null)
                    return;
                controller.getCanvas().undo();

            }
        });

        JMenuItem redoMenuItem = new JMenuItem("Redo", KeyEvent.VK_R);
        redoMenuItem.setIcon(loadIcon("redo_s.png"));
        redoMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.getCanvas() == null)
                    return;
                controller.getCanvas().redo();

            }
        });

        editMenu.add(undoMenuItem);
        editMenu.addSeparator();
        editMenu.add(redoMenuItem);

        editMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                PixelCanvas canvas = controller.getCanvas();
                if (canvas == null) {
                    undoMenuItem.setEnabled(false);
                    redoMenuItem.setEnabled(false);
                    return;
                }

                boolean canUndo = canvas.canUndo();
                boolean canRedo = canvas.canRedo();

                undoMenuItem.setEnabled(canUndo);
                redoMenuItem.setEnabled(canRedo);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

    }

    private void setupFileMenu() {
        fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setIcon(loadIcon("open_s.png"));
        openMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openCanvasFromFileSystem();
            }
        });

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.setIcon(loadIcon("new_s.png"));
        newMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.createNewCanvas();

            }
        });

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.setIcon(loadIcon("save_s.png"));
        saveAsMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveCanvasAs();
            }
        });

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.closeCanvas();
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean closeWindow = controller.closeCanvas();
                if (!closeWindow)
                    return;
                controller.getMainFrame().setVisible(false);
                controller.getMainFrame().dispose();
            }
        });

        JMenu recentlyOpenedMenu = getRecentlyOpenedMenu();

        fileMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                PixelCanvas canvas = controller.getCanvas();
                boolean doesCanvasExist = canvas != null;
                saveAsMenuItem.setEnabled(doesCanvasExist);
                closeMenuItem.setEnabled(doesCanvasExist);

                ArrayList<CanvasDatabaseObject> l = DatabaseManager.list();
                if (l == null || l.isEmpty()) {
                    recentlyOpenedMenu.setEnabled(false);
                    return;
                }
                recentlyOpenedMenu.setEnabled(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        fileMenu.add(openMenuItem);
        fileMenu.add(newMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(recentlyOpenedMenu);
        fileMenu.addSeparator();
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(closeMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
    }

    private JMenu getRecentlyOpenedMenu() {
        JMenu recentlyOpenedMenu = new JMenu("Recent files");
        recentlyOpenedMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                ArrayList<CanvasDatabaseObject> l = DatabaseManager.list();
                if (l == null || l.isEmpty()) // sanity check
                    return;

                recentlyOpenedMenu.removeAll();
                for (CanvasDatabaseObject o : l) {
                    JMenuItem item = new JMenuItem(String.format("%s\t%s", o.getName(), o.getDate()));
                    item.addActionListener(new AbstractAction() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            byte[] canvasData = DatabaseManager.get(o.getId());
                            PixelCanvas newCanvas = PixelCanvas.fromBytes(canvasData);
                            controller.createCanvas(newCanvas);
                        }
                    });
                    recentlyOpenedMenu.add(item);
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
        return recentlyOpenedMenu;
    }

    public MainMenuBar(CanvasController controller) {
        this();
        this.controller = controller;
    }


    public void setController(CanvasController controller) {
        this.controller = controller;
    }
}
