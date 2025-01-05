import java.awt.*;
import javax.swing.*;

import ui_funny_paint.screen.MainFrame;
import model.DatabaseManager;

// entry point class
public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            DatabaseManager.connect("recent_files.db");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {

            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}

