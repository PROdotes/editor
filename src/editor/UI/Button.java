package editor.UI;


import java.awt.*;


public class Button {
    int x, y, width, height;
    String name;
    Color backgroundColor = Color.WHITE;

    public Button(int xIN, int yIN, int widthIN, int heightIN, String nameIN) {

        x = xIN;
        y = yIN;
        width = widthIN;
        height = heightIN;
        name = nameIN;
    }

    public void draw(Graphics2D g2d) {

        g2d.setColor(backgroundColor);
        g2d.drawRect(x, y, width, height);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (height * 0.7)));
        g2d.drawString(name, x + (width / 2) - (g2d.getFontMetrics().stringWidth(name) / 2), y + (height / 2) + (g2d.getFontMetrics().getHeight() / 4));
    }

    public boolean checkMouseOver(int mouseX, int mouseY) {

        if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            backgroundColor = Color.gray;
            return true;
        }
        backgroundColor = Color.WHITE;
        return false;
    }

    public void click() {

        backgroundColor = Color.white;
    }

}
