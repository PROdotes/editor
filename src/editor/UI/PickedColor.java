package editor.UI;


import java.awt.*;


public class PickedColor {

    private static Color pickedColor;
    int x = 180, y = 50, width = 150, height = 65;

    public PickedColor(Color color) {

        pickedColor = color;
    }

    public void setPickedColor(int hue, int saturation, int brightness) {

        pickedColor = new Color(Color.HSBtoRGB((float) hue / 360, (float) saturation / 100, (float) brightness / 100));
    }


    public void draw(Graphics2D g) {

        g.setColor(pickedColor);
        g.fillRect(x, y, width, height);
    }

}
