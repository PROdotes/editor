package editor.main;


import editor.UI.Button;
import editor.UI.Canvas;
import editor.UI.*;

import javax.swing.*;
import java.awt.*;


public class EditorPanel extends JPanel implements Runnable {

    int pixelNumber = 32;
    int pixelSize = 8;
    int leftPadding = 450;
    int screenWidth = leftPadding + pixelNumber * pixelSize * 3, screenHeight = pixelNumber * pixelSize * 3;
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();
    ComponentAdapter componentAdapter = new ComponentAdapter();
    Slider hue = new Slider(10, 360, 240, "Hue");
    Slider saturation = new Slider(50, 100, 100, "Sat");
    Slider brightness = new Slider(90, 100, 100, "Bri");
    Slider red = new Slider(160, 255, 0, "Red");
    Slider green = new Slider(200, 255, 0, "Gre");
    Slider blue = new Slider(240, 255, 255, "Blu");
    Slider brush = new Slider(300, 256, 1, "Brsh");
    PickedColor pickedColor = new PickedColor(new Color(0, 0, 255));
    Button openFile = new Button(30, 350, 70, 30, "Open");
    Button saveFile = new Button(120, 350, 70, 30, "Save");
    Button resizeCanvas = new Button(210, 350, 70, 30, "Resize");
    Canvas canvas = new Canvas(pixelNumber, pixelSize);
    Thread thread;

    public EditorPanel() {

        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addComponentListener(componentAdapter);
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
            boolean right = mouseHandler.isRightClick() || mouseHandler.isShortRightClick();
            boolean left = mouseHandler.isShortLeftClick() || mouseHandler.isLeftClick();
            mouseHandler.setShortRightClick(false);
            mouseHandler.setShortLeftClick(false);
            if (keyHandler.isToggleGrid()) {
                canvas.toggleGrid();
                keyHandler.setToggleGrid(false);
                repaint();
            }
            if (mouseHandler.isDoubleClick())
                if (pickedColor.checkDoubleClick(mouseHandler.getMouseX(), mouseHandler.getMouseY()))
                    canvas.flood(pickedColor.getColor(), this);
            mouseHandler.setDoubleClick(false);
            if (componentAdapter.isResized()) {
                int rightLen = componentAdapter.getWidth() - leftPadding;
                int topLen = componentAdapter.getHeight();
                int min = Math.min(rightLen, topLen) / (3 * pixelNumber);
                System.out.println("min: " + min);
                System.out.println("rightLen: " + rightLen);
                System.out.println("topLen: " + topLen);
                canvas.resize(min);
                componentAdapter.setResized(false);
                repaint();
            }
            if (checkCanvas(mouseHandler.getMouseX(), mouseHandler.getMouseY(), left, keyHandler.isControlDown(), right)) {
                repaint();
                if (right) drawSliders();
            }
            if (checkButtons(mouseHandler.getMouseX(), mouseHandler.getMouseY(), left)) {
                repaint();
                mouseHandler.setLeftClick(false);
            }
            if (left || mouseHandler.getWheelAmount() != 0) {
                checkSliders(mouseHandler.getMouseX(), mouseHandler.getMouseY(), mouseHandler.getWheelAmount());
                repaint();
                mouseHandler.setWheelAmount(0);
            }
            if (mouseHandler.isMiddleClick()) {
                canvas.toggleDisplayMode();
                mouseHandler.setMiddleClick(false);
                repaint();
            }
            if (keyHandler.isUndo()) {
                canvas.undo();
                keyHandler.setUndo(false);
                repaint();
            }
            if (keyHandler.isRedo()) {
                canvas.redo();
                keyHandler.setRedo(false);
                repaint();
            }
        }
    }

    private boolean checkButtons(int mouseX, int mouseY, boolean click) {

        if (openFile.checkMouseOver(mouseX, mouseY)) {
            if (click) {
                canvas.openFile(this);
                pixelNumber = canvas.getPixelNumber();
                return true;
            }
            return true;
        }
        if (saveFile.checkMouseOver(mouseX, mouseY)) {
            if (click) {
                canvas.saveFile(this);
                return true;
            }
            return true;
        }
        if (resizeCanvas.checkMouseOver(mouseX, mouseY)) {
            if (click) {
                int clicked;
                int resizeValue = brush.getValue();
                if (resizeValue == 1) resizeValue = 2;
                clicked = JOptionPane.showConfirmDialog(this, "Are you sure you want to resize the canvas to the brush size of " + resizeValue + " pixels?", "Resize Canvas", JOptionPane.YES_NO_OPTION);
                if (clicked == JOptionPane.YES_OPTION) {
                    canvas.changePixels(resizeValue, Math.min(this.getWidth() - leftPadding, this.getHeight()));
                    pixelNumber = canvas.getPixelNumber();
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean checkCanvas(int mouseX, int mouseY, boolean leftClick, boolean controlDown, boolean rightClick) {

        if (rightClick) {
            Color color = canvas.getColor(mouseX, mouseY);
            if (color != null) {
                pickedColor.setPickedColorRGB(color.getRed(), color.getGreen(), color.getBlue());
                return true;
            }
        } else if (leftClick) {
            canvas.setColor(mouseX, mouseY, pickedColor.getColor(), controlDown);
            return true;
        } else {
            return canvas.preview(mouseX, mouseY, controlDown, pickedColor.getColor());
        }
        return false;
    }

    private void checkSliders(int mouseX, int mouseY, int wheelAmount) {

        boolean updatedHSB = hue.checkClicked(mouseX, mouseY, wheelAmount);
        updatedHSB = saturation.checkClicked(mouseX, mouseY, wheelAmount) || updatedHSB;
        updatedHSB = brightness.checkClicked(mouseX, mouseY, wheelAmount) || updatedHSB;
        boolean updatedRGB = red.checkClicked(mouseX, mouseY, wheelAmount);
        updatedRGB = green.checkClicked(mouseX, mouseY, wheelAmount) || updatedRGB;
        updatedRGB = blue.checkClicked(mouseX, mouseY, wheelAmount) || updatedRGB;
        boolean brushCheck = brush.checkClicked(mouseX, mouseY, wheelAmount);
        if (brushCheck) {
            canvas.setBrushSize(brush.getValue());
            if (canvas.getBrushSize() == 0) {
                canvas.setBrushSize(1);
                brush.setValue(1);
            }
        }
        if (updatedHSB) {
            pickedColor.setPickedColorHSB(hue.getValue(), saturation.getValue(), brightness.getValue());
            red.setValue(pickedColor.getRed());
            green.setValue(pickedColor.getGreen());
            blue.setValue(pickedColor.getBlue());
        } else if (updatedRGB) {
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
        green.setSlider(SliderImageGenerator.generateGreen(pickedColor.getRed(), pickedColor.getBlue()));
        blue.setSlider(SliderImageGenerator.generateBlue(pickedColor.getRed(), pickedColor.getGreen()));
        if (mouseHandler.isRightClick() || mouseHandler.isShortRightClick()) {
            hue.setValue(pickedColor.getHue());
            saturation.setValue(pickedColor.getSaturation());
            brightness.setValue(pickedColor.getBrightness());
            red.setValue(pickedColor.getRed());
            green.setValue(pickedColor.getGreen());
            blue.setValue(pickedColor.getBlue());
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        hue.draw(g2d);
        saturation.draw(g2d);
        brightness.draw(g2d);
        red.draw(g2d);
        green.draw(g2d);
        blue.draw(g2d);
        pickedColor.draw(g2d);
        canvas.draw(g2d);
        brush.draw(g2d);
        drawInformation(g2d);
        openFile.draw(g2d);
        saveFile.draw(g2d);
        resizeCanvas.draw(g2d);
        g.dispose();
    }

    private void drawInformation(Graphics2D g2d) {

        int top = 410;
        Font font = new Font("Arial", Font.BOLD, 12);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Click sliders to change value", 30, top);
        g2d.drawString("Scroll mouse wheel over slider to change picked colour", 30, top + 20);
        g2d.drawString("Click canvas to change pixel colour to picked colour", 30, top + 40);
        g2d.drawString("Click right mouse button to set picked colour to canvas pixel colour", 30, top + 60);
        g2d.drawString("Click middle mouse button to zoom in and out", 30, top + 80);
        g2d.drawString("Control click to change all selected colours to picked colour", 30, top + 100);
        g2d.drawString("Ctrl+Z to undo and Ctrl+Shift+Z to redo", 30, top + 120);
        g2d.drawString("Double click the active colour to fill the entire image", 30, top + 140);
        g2d.drawString("G to toggle grid view", 30, top + 160);
    }

}
