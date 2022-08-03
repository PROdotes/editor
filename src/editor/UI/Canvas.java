package editor.UI;


import java.awt.*;


public class Canvas {
    int pixelNumber = 32;
    int pixelSize = 7;
    int posX = 400, posY = 0;
    Color[][] pixels = new Color[pixelNumber][pixelNumber];

    public Canvas() {

        for (int i = 0; i < pixelNumber; i++) {
            for (int j = 0; j < pixelNumber; j++) {
                Color random = new Color(i * 5, j * 5, 0);
                pixels[i][j] = random;
            }
        }
    }

    public void draw(Graphics2D g2d) {

        for (int i = 0; i < pixelNumber; i++) {
            for (int j = 0; j < pixelNumber; j++) {
                g2d.setColor(pixels[i][j]);
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        g2d.fillRect(posX + i * pixelSize + k * pixelSize * pixelNumber, posY + j * pixelSize + l * pixelSize * pixelNumber, pixelSize, pixelSize);
                    }
                }
            }
        }
    }

    public void checkClick(int mouseX, int mouseY, Color color) {

        if (mouseX > posX && mouseX < posX + pixelSize * pixelNumber * 3 && mouseY > posY && mouseY < posY + pixelSize * pixelNumber * 3) {
            int i = (mouseX - posX) / pixelSize;
            if (i >= pixelNumber) i -= pixelNumber;
            if (i >= pixelNumber) i -= pixelNumber;
            int j = (mouseY - posY) / pixelSize;
            if (j >= pixelNumber) j -= pixelNumber;
            if (j >= pixelNumber) j -= pixelNumber;
            pixels[i][j] = color;
        }
    }

}


