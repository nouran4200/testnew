package com.iti.chat.model;

import java.io.Serializable;

public class MessageStyle implements Serializable {
    String fontFamily;
    int size;
    String color;
    String fontWeight;
    String fontPosture;

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
