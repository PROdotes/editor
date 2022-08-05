package editor.Object;


import java.awt.*;


public class Action {
    Point pixel;
    Color oldColor;
    Color newColor;

    public Action(Point pixelIN, Color oldColorIN, Color newColorIN) {

        pixel = pixelIN;
        oldColor = oldColorIN;
        newColor = newColorIN;
    }

    public String getString() {

        return "Action: " + pixel.x + " " + pixel.y + " " + oldColor.getRGB() + " " + newColor.getRGB();
    }

    public int getX() {

        return pixel.x;
    }

    public int getY() {

        return pixel.y;
    }

    public Color getOldColor() {

        return oldColor;
    }

    public Color getNewColor() {

        return newColor;
    }

}
