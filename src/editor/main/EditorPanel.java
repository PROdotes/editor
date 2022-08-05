package editor.main;


import editor.UI.Button;
import editor.UI.Canvas;
import editor.UI.*;

import javax.swing.*;
import java.awt.*;


public class EditorPanel extends JPanel {

    int pixelNumber = 20;
    int leftPadding = 450;
    int pixelSize = 785 / 20 / 3;
    int screenWidth = leftPadding + pixelNumber * pixelSize * 3, screenHeight = pixelNumber * pixelSize * 3;
    KeyHandler keyHandler = new KeyHandler(this);
    MouseHandler mouseHandler = new MouseHandler(this);
    ComponentAdapter componentAdapter = new ComponentAdapter(this);
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
    long lastZoom = System.currentTimeMillis();

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

        drawSliders();
    }

    private boolean checkButtons(int mouseX, int mouseY, boolean click) {

        if (openFile.checkMouseOver(mouseX, mouseY)) {
            if (click) {
                openFile.click();
                canvas.openFile(this);
                pixelNumber = canvas.getPixelNumber();
            }
            return true;
        }
        if (saveFile.checkMouseOver(mouseX, mouseY)) {
            if (click) {
                saveFile.click();
                canvas.saveFile(this);
            }
            return true;
        }
        if (resizeCanvas.checkMouseOver(mouseX, mouseY)) {
            if (click) {
                resizeCanvas.click();
                int resizeValue = (brush.getValue() == 1) ? 2 : brush.getValue();
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to resize the canvas to the brush size of " + resizeValue + " pixels?", "Resize Canvas", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    int minPixelSize = Math.min(this.getWidth() - leftPadding, this.getHeight());
                    canvas.changePixels(resizeValue, minPixelSize);
                    pixelNumber = canvas.getPixelNumber();
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkCanvas(int mouseX, int mouseY, boolean leftClick, boolean rightClick, boolean controlDown) {

        if (rightClick) {
            Color color = canvas.getColor(mouseX, mouseY);
            if (color != null) {
                pickedColor.setPickedColorRGB(color.getRed(), color.getGreen(), color.getBlue());
                return true;
            }
        } else if (leftClick) {
            return canvas.setPixelColor(mouseX, mouseY, pickedColor.getColor(), controlDown);
        } else {
            return canvas.preview(mouseX, mouseY, controlDown);
        }
        return false;
    }

    private boolean checkSliders(int mouseX, int mouseY, int wheelAmount) {

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
            if (canvas.getBrushSize() > pixelNumber) {
                canvas.setBrushSize(pixelNumber);
                //brush.setValue(pixelNumber);
            }
        }
        if (updatedHSB) {
            pickedColor.setPickedColorHSB(hue.getValue(), saturation.getValue(), brightness.getValue());
            updateRGBValues();
        } else if (updatedRGB) {
            pickedColor.setPickedColorRGB(red.getValue(), green.getValue(), blue.getValue());
            updateHSBValues();
        }
        if (updatedHSB || updatedRGB) {
            drawSliders();
        }
        return updatedHSB || updatedRGB || brushCheck;
    }

    private void updateHSBValues() {

        hue.setValue(pickedColor.getHue());
        saturation.setValue(pickedColor.getSaturation());
        brightness.setValue(pickedColor.getBrightness());
    }

    private void updateRGBValues() {

        red.setValue(pickedColor.getRed());
        green.setValue(pickedColor.getGreen());
        blue.setValue(pickedColor.getBlue());
    }

    private void drawSliders() {

        hue.setSlider(SliderImageGenerator.generateHue(pickedColor.getSaturation(), pickedColor.getBrightness()));
        saturation.setSlider(SliderImageGenerator.generateSaturation(pickedColor.getHue(), pickedColor.getBrightness()));
        brightness.setSlider(SliderImageGenerator.generateBrightness(pickedColor.getHue(), pickedColor.getSaturation()));
        red.setSlider(SliderImageGenerator.generateRed(pickedColor.getGreen(), pickedColor.getBlue()));
        green.setSlider(SliderImageGenerator.generateGreen(pickedColor.getRed(), pickedColor.getBlue()));
        blue.setSlider(SliderImageGenerator.generateBlue(pickedColor.getRed(), pickedColor.getGreen()));
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
        canvas.draw(g2d, keyHandler.isControlDown());
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
        g2d.drawString("Right click the picked color to change grid color", 30, top + 180);
    }

    public void toggleGrid() {

        canvas.toggleGrid();
        repaint();
    }

    public void undo() {

        canvas.undo();
        repaint();
    }

    public void redo() {

        canvas.redo();
        repaint();
    }


    public void doubleClick(int x, int y) {

        if (pickedColor.checkIfClicked(x, y)) {
            canvas.flood(pickedColor.getColor(), this);
            repaint();
        }
    }

    public void handleLeftClick(int x, int y) {

        if (checkButtons(x, y, true))
            repaint();
        if (checkSliders(x, y, 0))
            repaint();
        if (checkCanvas(x, y, true, false, keyHandler.isControlDown()))
            repaint();
    }

    public void handleRightClick(int x, int y) {

        if (checkCanvas(x, y, false, true, keyHandler.isControlDown())) {
            updateRGBValues();
            updateHSBValues();
            repaint();
        }
        if (pickedColor.checkIfClicked(x, y)) {
            System.out.println("Right click");
            canvas.setGridColor(pickedColor.getColor());
            repaint();
        }
    }

    public void handleMouseWheel(int wheelRotation, int x, int y) {

        if (checkSliders(x, y, wheelRotation))
            repaint();

    }

    public void toggleZoom() {

        if (System.currentTimeMillis() - lastZoom > 200) {
            lastZoom = System.currentTimeMillis();
            canvas.toggleZoom();
            repaint();
        }
    }

    public void handleMouseMoved(int x, int y) {

        if (checkButtons(x, y, false))
            repaint();
        if (checkCanvas(x, y, false, false, keyHandler.isControlDown()))
            repaint();
    }

    public void handleResize(int width, int height) {

        int rightLen = width - leftPadding;
        int min = Math.min(rightLen, height) / (3 * pixelNumber);
        canvas.resize(min);
        repaint();
    }

    public void controlToggle() {

        if (checkCanvas(mouseHandler.getMouseX(), mouseHandler.getMouseY(), false, false, keyHandler.isControlDown()))
            repaint();
    }

}
