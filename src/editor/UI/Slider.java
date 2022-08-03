package editor.UI;


import java.awt.*;
import java.awt.image.BufferedImage;


public class Slider {
    private BufferedImage slider;
    private int value;
    private final int x = 10, y, height = 20, width, triangle = 14;
    private Font font = new Font("Arial", Font.ITALIC, triangle);

    public Slider(int yIN, int widthIN, int valueIN) {
        y = yIN;
        width = widthIN;
        value = valueIN;
        slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setSlider(BufferedImage sliderIN) {

        slider = sliderIN;
    }

    public void draw(Graphics2D g) {
        g.drawImage(slider, x, y, null);
        g.setColor(Color.WHITE);
        g.fillPolygon(new int[]{x+ value-4, x+ value, x + value+4}, new int[]{y+height+triangle, y+height+3, y+height+triangle}, 3);
        g.setFont(font);
        g.drawString(String.valueOf(value), x+ value +10, y+height+triangle);
    }

    public boolean checkClicked(int mouseX, int mouseY, int wheelAmmount) {
        if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            if (wheelAmmount == 0)
                value = mouseX - x;
            else
                value = Math.min(Math.max(value+wheelAmmount, 0),width);
            return true;
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int valueIN) {
        value = valueIN;
    }

}
