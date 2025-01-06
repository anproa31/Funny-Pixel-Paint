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
            InputStream fontStream = Main.class.getResourceAsStream("fonts/aseprite.ttf");
            Font pixelFont = null;

            try {
                assert fontStream != null;
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }

            try {
                UIManager.setLookAndFeel(new FlatMaterialDeepOceanIJTheme());
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            System.setProperty("awt.useSystemAAFontSettings", "off");
            System.setProperty("swing.aatext", "false");

            UIManager.put("Label.font", pixelFont);
            UIManager.put("Button.font", pixelFont);
            UIManager.put("defaultFont", pixelFont);
            UIManager.put("TextField.font", pixelFont);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);


            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}

