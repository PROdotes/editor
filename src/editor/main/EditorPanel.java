package editor.main;


import editor.UI.*;
import editor.UI.Canvas;

import javax.swing.*;
import java.awt.*;


public class EditorPanel extends JPanel implements Runnable {

    int screenWidth = 672+400, screenHeight = 672;
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();
    HueSlider hue = new HueSlider(10, 360, 240);
    SaturationSlider saturation = new SaturationSlider(50, 100, 100);
    BrightnessSlider brightness = new BrightnessSlider(90, 100, 100);
    RedSlider red = new RedSlider(150, 255, 0);
    GreenSlider green = new GreenSlider(190, 255, 0);
    BlueSlider blue = new BlueSlider(230, 255, 255);
    PickedColor pickedColor = new PickedColor(new Color(0, 0, 255));
    Canvas canvas = new Canvas();
    Thread thread;

    public EditorPanel() {

        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        setFocusable(true);
    }

    public void startThread() {

        thread = new Thread(this);
        thread.start();
        drawSliders();
    }

    @Override
    public void run() {

        while (thread != null) {
            if (mouseHandler.isMouseDown() || mouseHandler.getWheelAmount() != 0) {
                checkSliders(mouseHandler.getMouseX(), mouseHandler.getMouseY(), mouseHandler.getWheelAmount());
                checkCanvas(mouseHandler.getMouseX(), mouseHandler.getMouseY());
                repaint();
                mouseHandler.setMouseDown(false);
                mouseHandler.setWheelAmount(0);
            }
        }
    }

    private void checkCanvas(int mouseX, int mouseY) {

            canvas.checkClick(mouseX, mouseY, pickedColor.getColor());
    }

    private void checkSliders(int mouseX, int mouseY, int wheelAmmount) {

        boolean updatedHSB = hue.checkClicked(mouseX, mouseY, wheelAmmount);
        updatedHSB = saturation.checkClicked(mouseX, mouseY, wheelAmmount) || updatedHSB;
        updatedHSB = brightness.checkClicked(mouseX, mouseY, wheelAmmount) || updatedHSB;
        boolean updatedRGB = red.checkClicked(mouseX, mouseY, wheelAmmount);
        updatedRGB = green.checkClicked(mouseX, mouseY, wheelAmmount) || updatedRGB;
        updatedRGB = blue.checkClicked(mouseX, mouseY, wheelAmmount) || updatedRGB;
        if (updatedHSB) {
            System.out.println("updatedHSB");
            pickedColor.setPickedColorHSB(hue.getValue(), saturation.getValue(), brightness.getValue());
            red.setValue(pickedColor.getRed());
            green.setValue(pickedColor.getGreen());
            blue.setValue(pickedColor.getBlue());
        } else if (updatedRGB) {
            System.out.println("updatedRGB");
            pickedColor.setPickedColorRGB(red.getValue(), green.getValue(), blue.getValue());
            hue.setValue(pickedColor.getHue());
            saturation.setValue(pickedColor.getSaturation());
            brightness.setValue(pickedColor.getBrightness());
        }
        if (updatedHSB || updatedRGB) {
            drawSliders();
        }
    }

    private void drawSliders() {

        hue.setSlider(SliderImageGenerator.generateHue(pickedColor.getSaturation(), pickedColor.getBrightness()));
        saturation.setSlider(SliderImageGenerator.generateSaturation(pickedColor.getHue(), pickedColor.getBrightness()));
        brightness.setSlider(SliderImageGenerator.generateBrightness(pickedColor.getHue(), pickedColor.getSaturation()));
        red.setSlider(SliderImageGenerator.generateRed(pickedColor.getGreen(), pickedColor.getBlue()));
        green.setSlider(SliderImageGenerator.generateGreen( pickedColor.getRed(), pickedColor.getBlue()));
        blue.setSlider(SliderImageGenerator.generateBlue(pickedColor.getRed(), pickedColor.getGreen()));
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        hue.draw(g2d);
        saturation.draw(g2d);
        brightness.draw(g2d);
        pickedColor.draw(g2d);
        red.draw(g2d);
        green.draw(g2d);
        blue.draw(g2d);
        canvas.draw(g2d);
        g.dispose();
    }

}
