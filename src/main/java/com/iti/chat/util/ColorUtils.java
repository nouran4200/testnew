package com.iti.chat.util;

import javafx.scene.paint.Color;

public class ColorUtils {
    private static final int THRESHOLD = 200;
    public static String toRGB(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static boolean areSimilarColors(Color color1, Color color2) {
        double red = Math.abs(color1.getRed() - color2.getRed()) * 255;
        double green = Math.abs(color1.getGreen() - color2.getGreen()) * 255;
        double blue = Math.abs(color1.getBlue() - color2.getBlue()) * 255;
        return red + green + blue < THRESHOLD;
    }

    public static boolean areSimilarColors(String color1, String color2) {
        return areSimilarColors(Color.web(color1), Color.web(color2));
    }

    public static String invertedColor(Color color) {
        double red = 1 - color.getRed();
        double green = 1 - color.getGreen();
        double blue = 1 - color.getBlue();
        return toRGB(new Color(red, green, blue, 1.0));
    }

    public static String invertedColor(String color) {
        return invertedColor(Color.web(color));
    }
}
