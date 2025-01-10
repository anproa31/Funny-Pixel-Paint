import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;
import controller.tools.BrushController;
import model.DatabaseManager;
import ui_funny_paint.screen.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static utils.CursorManager.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> showSplashScreen(Main::initializeApplication));
    }

    private static void showSplashScreen(Runnable postSplashAction) {
        JWindow splashWindow = new JWindow();
        Image splashImage = new ImageIcon(Objects.requireNonNull(Main.class.getResource("res/splash.gif"))).getImage();
        JLabel splashImageLabel = new JLabel(new ImageIcon(splashImage));

        splashWindow.getContentPane().add(splashImageLabel);
        splashWindow.pack();
        splashWindow.setLocationRelativeTo(null);
        splashWindow.setVisible(true);

        Timer splashTimer = new Timer(3000, e -> {
            splashWindow.dispose();
            postSplashAction.run();
        });

        splashTimer.setRepeats(false);
        splashTimer.start();
    }

    private static void initializeApplication() {
        DatabaseManager.connect("recent_files.db");

        setupTheme();
        setupFont();

        MainFrame frame = new MainFrame();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getController().setCanvasTool(new BrushController(brushCursor));

        setCustomCursor(frame, defaultCursor);
    }

    private static void setupTheme() {
        try {
            UIManager.setLookAndFeel(new FlatMaterialDeepOceanIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setupFont() {
        InputStream fontStream = Main.class.getResourceAsStream("fonts/aseprite.ttf");
        Font pixelFont;

        try {
            assert fontStream != null;
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f);
        } catch (FontFormatException | IOException e) {
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
    }
}
