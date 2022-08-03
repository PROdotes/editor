package editor.UI;


import java.awt.*;
import java.awt.image.BufferedImage;


public class SliderImageGenerator {
    private static final int height = 20;

    public static BufferedImage generateHue(int saturation, int brightness) {
        int width = 360;
        BufferedImage slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int drawWidth = 0; drawWidth < width; drawWidth++) {
            int color = Color.HSBtoRGB((float) drawWidth / width, (float) saturation / 100, (float) brightness / 100);
            for (int drawHeight = 0; drawHeight < height; drawHeight++) {
                slider.setRGB(drawWidth, drawHeight, color);
            }
        }
        return slider;
    }

    public static BufferedImage generateSaturation(int hue, int brightness) {
        int width = 100;
        BufferedImage slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int drawWidth = 0; drawWidth < width; drawWidth++) {
            int color = Color.HSBtoRGB((float) hue / 360, (float) drawWidth / width, (float) brightness / 100);
            for (int drawHeight = 0; drawHeight < height; drawHeight++) {
                slider.setRGB(drawWidth, drawHeight, color);
            }
        }
        return slider;
    }

    public static BufferedImage generateBrightness(int hue, int saturation) {
        int width = 100;
        BufferedImage slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int drawWidth = 0; drawWidth < width; drawWidth++) {
            int color = Color.HSBtoRGB((float) hue / 360, (float) saturation / 100, (float) drawWidth / width);
            for (int drawHeight = 0; drawHeight < height; drawHeight++) {
                slider.setRGB(drawWidth, drawHeight, color);
            }
        }
        return slider;
    }

    public static BufferedImage generateRed(int green, int blue) {
        int width = 255;
        BufferedImage slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int drawWidth = 0; drawWidth < width; drawWidth++) {
            for (int drawHeight = 0; drawHeight < height; drawHeight++) {
                slider.setRGB(drawWidth, drawHeight, new Color(drawWidth, green, blue).getRGB());
            }
        }
        return slider;
    }

    public static BufferedImage generateGreen(int red, int blue) {
        int width = 255;
        BufferedImage slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int drawWidth = 0; drawWidth < width; drawWidth++) {
            for (int drawHeight = 0; drawHeight < height; drawHeight++) {
                slider.setRGB(drawWidth, drawHeight, new Color(red, drawWidth, blue).getRGB());
            }
        }
        return slider;
    }

    public static BufferedImage generateBlue(int red, int green) {
        int width = 255;
        BufferedImage slider = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int drawWidth = 0; drawWidth < width; drawWidth++) {
            for (int drawHeight = 0; drawHeight < height; drawHeight++) {
                slider.setRGB(drawWidth, drawHeight, new Color(red, green, drawWidth).getRGB());
            }
        }
        return slider;
    }
}
