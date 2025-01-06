import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;
import ui_funny_paint.screen.MainFrame;
import model.DatabaseManager;

// entry point class
public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            DatabaseManager.connect("recent_files.db");
            InputStream fontStream = Main.class.getResourceAsStream("fonts/Minecraftia-Regular.ttf");
            Font pixelFont = null; // Set size to 12pt
            try {
                assert fontStream != null;
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }

            try {
                UIManager.setLookAndFeel(new FlatMaterialDeepOceanIJTheme());
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            UIManager.put("Label.font", pixelFont);
            UIManager.put("Button.font", pixelFont);
            UIManager.put("defaultFont", pixelFont);
            UIManager.put("TextField.font", pixelFont);

            System.setProperty("awt.useSystemAAFontSettings", "off");
            System.setProperty("swing.aatext", "false");

            // Register the font
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);


            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}

