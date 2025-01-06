import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ui_funny_paint.screen.MainFrame;
import model.DatabaseManager;

// entry point class
public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            DatabaseManager.connect("recent_files.db");
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ignored) {

            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}

