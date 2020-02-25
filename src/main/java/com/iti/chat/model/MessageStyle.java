package com.iti.chat.model;

import com.iti.chat.util.ColorUtils;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.Serializable;

public class MessageStyle implements Serializable {
    String fontFamily;
    int size;
    String color;
    String fontWeight;
    String fontPosture;

    {
        size = 15;
        color = ColorUtils.toRGB(Color.BLACK);
        fontFamily = "Arial";
        fontWeight = FontWeight.LIGHT.name();
        fontPosture = FontPosture.REGULAR.name();
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontPosture() {
        return fontPosture;
    }

    public void setFontPosture(String fontPosture) {
        this.fontPosture = fontPosture;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String toString() {
        return "-fx-font-family: \"" + fontFamily + "\"; " + "-fx-text-fill: " + color + ";" + "-fx-font-size: "
                + size + ";" + " -fx-font-weight:" + fontWeight + ";" + " -fx-font-style:" + fontPosture;
    }
}
