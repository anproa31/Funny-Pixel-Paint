package utils;

import javax.swing.*;

public class LoadIcon {
    public static ImageIcon loadIcon(String filename) {
        try {
            // Use ClassLoader to load the image from the classpath
            java.net.URL imageUrl = LoadIcon.class.getClassLoader().getResource("res/" + filename);
            if (imageUrl == null) {
                System.err.println("Image not found: res/" + filename);
                return null;
            }
            return new ImageIcon(imageUrl);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + filename);
            e.printStackTrace();
            return null;
        }
    }
}
