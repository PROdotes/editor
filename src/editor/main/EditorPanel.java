package editor.main;


import editor.UI.*;

import javax.swing.*;
import java.awt.*;


public class EditorPanel extends JPanel implements Runnable {

    int screenWidth = 800, screenHeight = 800;
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();
    HueSlider hue = new HueSlider(10, 360, 240);
    SaturationSlider saturation = new SaturationSlider(50, 100, 100);
    BrightnessSlider brightness = new BrightnessSlider(90, 100, 100);
    PickedColor pickedColor = new PickedColor(new Color(Color.HSBtoRGB(hue.getValue(), saturation.getValue(), brightness.getValue())));
    RedSlider red = new RedSlider(150, 255, 0);
    GreenSlider green = new GreenSlider(190, 255, 0);
    BlueSlider blue = new BlueSlider(230, 255, 255);
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
                repaint();
                mouseHandler.setMouseDown(false);
                mouseHandler.setWheelAmount(0);
            }
        }
    }

    private void checkSliders(int mouseX, int mouseY, int wheelAmmount) {

        boolean checkSliders = hue.checkClicked(mouseX, mouseY, wheelAmmount);
        checkSliders = saturation.checkClicked(mouseX, mouseY, wheelAmmount) || checkSliders;
        checkSliders = brightness.checkClicked(mouseX, mouseY, wheelAmmount) || checkSliders;
        checkSliders = red.checkClicked(mouseX, mouseY, wheelAmmount) || checkSliders;
        if (checkSliders) {
            drawSliders();
        }
    }

    private void drawSliders() {

        hue.setSlider(SliderImageGenerator.generateHue(saturation.getValue(), brightness.getValue()));
        saturation.setSlider(SliderImageGenerator.generateSaturation(hue.getValue(), brightness.getValue()));
        brightness.setSlider(SliderImageGenerator.generateBrightness(hue.getValue(), saturation.getValue()));
        pickedColor.setPickedColor(hue.getValue(), saturation.getValue(), brightness.getValue());
        red.setSlider(SliderImageGenerator.generateRed(0,255));
        green.setSlider(SliderImageGenerator.generateGreen(0,255));
        blue.setSlider(SliderImageGenerator.generateBlue(0,0));
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
    }

}
