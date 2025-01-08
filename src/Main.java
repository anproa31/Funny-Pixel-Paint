import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.swing.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;
import controller.tools.BrushController;
import ui_funny_paint.screen.MainFrame;
import model.DatabaseManager;

import static utils.CursorManager.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JWindow splash = new JWindow();

            Image logoImage = new ImageIcon(Objects.requireNonNull(Main.class.getResource("res/splash.gif"))).getImage();
            JLabel imageLabel = new JLabel(new ImageIcon(logoImage));

            splash.getContentPane().add(imageLabel);
            splash.pack();
            splash.setLocationRelativeTo(null);
            splash.setVisible(true);

            Timer timer = new Timer(3000, e -> {
                splash.dispose();
                initializeApplication();
            });

            timer.setRepeats(false);
            timer.start();
        });
      }

    private static void initializeApplication() {
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
        frame.getController().setCanvasTool(new BrushController(brushCursor));
    }

}
