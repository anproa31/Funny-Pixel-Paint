import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;
import ui_funny_paint.screen.MainFrame;
import model.DatabaseManager;

// entry point class
public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            DatabaseManager.connect("recent_files.db");

            try {
                UIManager.setLookAndFeel(new FlatMaterialDeepOceanIJTheme());
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}

