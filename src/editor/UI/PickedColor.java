package editor.UI;


import java.awt.*;


public class PickedColor {

    private static Color pickedColor;
    int x = 180, y = 50, width = 150, height = 65;

    public PickedColor(Color color) {

        pickedColor = color;
    }

    public void setPickedColorHSB(int hue, int saturation, int brightness) {

        pickedColor = new Color(Color.HSBtoRGB((float) hue / 360, (float) saturation / 100, (float) brightness / 100));
    }


    public void draw(Graphics2D g) {

        g.setColor(pickedColor);
        g.fillRect(x, y, width, height);
    }

    public void setPickedColorRGB(int red, int green, int blue) {

            pickedColor = new Color(red, green, blue);

    }

    public int getHue() {

            return (int) (Color.RGBtoHSB(pickedColor.getRed(), pickedColor.getGreen(), pickedColor.getBlue(), null)[0] * 360);
    }

    public int getSaturation() {

            return (int) (Color.RGBtoHSB(pickedColor.getRed(), pickedColor.getGreen(), pickedColor.getBlue(), null)[1] * 100);
    }

    public int getBrightness() {

            return (int) (Color.RGBtoHSB(pickedColor.getRed(), pickedColor.getGreen(), pickedColor.getBlue(), null)[2] * 100);
    }

    public int getRed() {

            return pickedColor.getRed();
    }

    public int getGreen() {

            return pickedColor.getGreen();
    }

    public int getBlue() {

            return pickedColor.getBlue();
    }

    public Color getColor() {

                return pickedColor;
    }

}
