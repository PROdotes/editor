package editor.UI;


import editor.Object.Action;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Canvas {
    int pixelNumber, pixelSize;
    int posX = 450, posY = 0;
    boolean zoomedOutView = true;
    Color[][] pixels;
    ArrayList<Point> drawPixels = new ArrayList<>();
    ArrayList<ArrayList<Action>> undoList = new ArrayList<>();
    ArrayList<Action> tempUndo = new ArrayList<>();
    int undoIndex = -1;
    boolean showGrid = false;
    int brushSize = 1;
    Color gridColor = Color.black;


    public Canvas(int pixelNumberIN, int pixelSizeIN) {

        pixelNumber = pixelNumberIN;
        pixelSize = pixelSizeIN;
        pixels = new Color[pixelNumber][pixelNumber];

        for (int i = 0; i < pixelNumber; i++) {
            for (int j = 0; j < pixelNumber; j++) {
                Color random = new Color(i * pixelSize, j * pixelSize, 0);
                pixels[i][j] = random;
            }
        }
    }

    public void draw(Graphics2D g2d, boolean control) {

        int loop = 3;
        if (!zoomedOutView)
            loop = 1;
        for (int i = 0; i < pixelNumber; i++) {
            for (int j = 0; j < pixelNumber; j++) {
                for (int k = 0; k < loop; k++) {
                    for (int l = 0; l < loop; l++) {
                        g2d.setColor(pixels[i][j]);
                        g2d.fillRect(posX + i * pixelSize * (4 - loop) + k * pixelSize * pixelNumber, posY + j * pixelSize * (4 - loop) + l * pixelSize * pixelNumber, pixelSize * (4 - loop), pixelSize * (4 - loop));
                        if (showGrid) {
                            g2d.setColor(gridColor);
                            g2d.drawRect(posX + i * pixelSize * (4 - loop) + k * pixelSize * pixelNumber, posY + j * pixelSize * (4 - loop) + l * pixelSize * pixelNumber, pixelSize * (4 - loop), pixelSize * (4 - loop));
                        }
                    }
                }
            }
        }
        if (showGrid) {
            g2d.setColor(Color.red);
            g2d.drawRect(posX, posY, pixelNumber * pixelSize * 3, pixelNumber * pixelSize * 3);
            if (zoomedOutView) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        g2d.drawRect(posX + i * pixelSize * pixelNumber, posY + j * pixelSize * pixelNumber, pixelSize * pixelNumber, pixelSize * pixelNumber);
                    }
                }
            }
        }
        g2d.setColor(Color.white);
        if (drawPixels.size() > 0 && !control) {
            ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();
            int half = (brushSize % 2 == 1) ? pixelSize / 2 : pixelSize;
            int centerX = drawPixels.get(0).x * pixelSize + half;
            int centerY = drawPixels.get(0).y * pixelSize + half;
            int width = brushSize * pixelSize;
            if (zoomedOutView) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        rectangleArrayList.add(new Rectangle(centerX - width / 2 + posX + i * pixelSize * pixelNumber, centerY - width / 2 + posY + j * pixelSize * pixelNumber, width, width));
                    }
                }
            } else {
                int startX = (centerX - width / 2) * 3;
                int startY = (centerY - width / 2) * 3 + posY;
                width = width * 3;
                startX += posX;
                startY += posY;
                rectangleArrayList.add(new Rectangle(startX, startY, width, width));
            }
            ArrayList<Rectangle> rectangleArrayList2 = new ArrayList<>();
            for (Rectangle rectangle : rectangleArrayList) {
                checkXRectangle(rectangle, rectangleArrayList2);
            }
            rectangleArrayList.clear();
            for (Rectangle rectangle : rectangleArrayList2) {
                checkYRectangle(rectangle, rectangleArrayList);
            }
            for (Rectangle rect : rectangleArrayList) {
                g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        } else {
            for (Point point : drawPixels) {
                for (int k = 0; k < loop; k++)
                    for (int l = 0; l < loop; l++)
                        g2d.drawRect(posX + point.x * pixelSize * (4 - loop) + k * pixelSize * pixelNumber, posY + point.y * pixelSize * (4 - loop) + l * pixelSize * pixelNumber, pixelSize * (4 - loop), pixelSize * (4 - loop));
            }
        }
    }

    private void checkYRectangle(Rectangle rectangle, ArrayList<Rectangle> rectangleArrayList) {

        if (rectangle.y < posY) {
            rectangleArrayList.add(new Rectangle(rectangle.x, posY, rectangle.width, rectangle.height + rectangle.y - posY));
            rectangleArrayList.add(new Rectangle(rectangle.x, rectangle.y + pixelNumber * pixelSize * 3, rectangle.width, rectangle.height - rectangle.y + posY));
        } else if (rectangle.y + rectangle.height > posY + pixelNumber * pixelSize * 3) {
            rectangleArrayList.add(new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height - rectangle.y + posY + pixelNumber * pixelSize * 3));
            rectangleArrayList.add(new Rectangle(rectangle.x, posY, rectangle.width, rectangle.height + rectangle.y - posY - pixelSize * pixelNumber * 3));
        } else {
            rectangleArrayList.add(rectangle);
        }

    }

    private void checkXRectangle(Rectangle rectangle, ArrayList<Rectangle> rectangleArrayList) {

        if (rectangle.x < posX) {
            rectangleArrayList.add(new Rectangle(posX, rectangle.y, rectangle.width + rectangle.x - posX, rectangle.height));
            rectangleArrayList.add(new Rectangle(rectangle.x + pixelNumber * pixelSize * 3, rectangle.y, rectangle.width - rectangle.x + posX, rectangle.height));
        } else if (rectangle.x + rectangle.width > posX + pixelNumber * pixelSize * 3) {
            rectangleArrayList.add(new Rectangle(rectangle.x, rectangle.y, rectangle.width - rectangle.x + posX + pixelNumber * pixelSize * 3, rectangle.height));
            rectangleArrayList.add(new Rectangle(posX, rectangle.y, rectangle.width + rectangle.x - posX - pixelSize * pixelNumber * 3, rectangle.height));
        } else {
            rectangleArrayList.add(rectangle);
        }
    }

    public boolean preview(int mouseX, int mouseY, boolean controlDown) {

        Point centerPoint = checkBounds(mouseX, mouseY);
        if (centerPoint != null) {
            if (!controlDown) {
                drawPixels.clear();
                drawPixels.add(centerPoint);
            } else {
                drawPixels.clear();
                for (int i = 0; i < pixelNumber; i++) {
                    for (int j = 0; j < pixelNumber; j++) {
                        if (pixels[i][j].equals(pixels[centerPoint.x][centerPoint.y])) {
                            drawPixels.add(new Point(i, j));
                        }
                    }
                }
            }
        } else {
            if (drawPixels.size() < 1)
                return false;
            drawPixels.clear();
        }
        return true;
    }

    public boolean setPixelColor(int mouseX, int mouseY, Color color, boolean controlDown) {

        Point point = checkBounds(mouseX, mouseY);
        if (point != null) {
            preview(mouseX, mouseY, controlDown);
            if (!controlDown) {

                int end = brushSize / 2;
                int start = -end;
                if (brushSize % 2 == 0) {
                    start++;
                }
                for (int i = start; i <= end; i++) {
                    int x = point.x + i;
                    if (x < 0)
                        x += pixelNumber;
                    if (x >= pixelNumber)
                        x -= pixelNumber;
                    for (int j = start; j <= end; j++) {
                        if (i != 0 || j != 0) {
                            int y = point.y + j;
                            if (y < 0)
                                y += pixelNumber;
                            if (y >= pixelNumber)
                                y -= pixelNumber;
                            if (!drawPixels.contains(new Point(x, y))) {
                                drawPixels.add(new Point(x, y));
                            }
                        }
                    }
                }

                if (checkPixels(color)) {
                    for (Point drawPixel : drawPixels) {
                        if (pixels[drawPixel.x][drawPixel.y] != color) {
                            addToUndo(color, drawPixel);
                            pixels[drawPixel.x][drawPixel.y] = color;
                        }                    }
                    pushTempToUndo();
                }
            } else {
                if (checkPixels(color)) {
                    for (Point drawPixel : drawPixels) {
                        addToUndo(color, drawPixel);
                        pixels[drawPixel.x][drawPixel.y] = color;
                    }
                    pushTempToUndo();
                    drawPixels.clear();
                    drawPixels.add(point);
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkPixels(Color color) {
        System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
        for (Point point : drawPixels) {
            System.out.println(pixels[point.x][point.y].getRed() + " " + pixels[point.x][point.y].getGreen() + " " + pixels[point.x][point.y].getBlue());
            if (!pixels[point.x][point.y].equals(color))
                return true;
        }
        return false;
    }

    private void pushTempToUndo() {

        while (undoIndex < undoList.size() - 1) {
            undoList.remove(undoList.size() - 1);
        }

        undoList.add(new ArrayList<>(tempUndo));
        undoIndex++;
        tempUndo.clear();
    }

    private void addToUndo(Color newColor, Point point) {

        tempUndo.add(new Action(point, pixels[point.x][point.y], newColor));
        System.out.println("Added to undo:" + point.x + "," + point.y + " " + pixels[point.x][point.y] + " " + newColor);
    }

    private Point checkBounds(int mouseX, int mouseY) {

        if (mouseX > posX && mouseX < posX + pixelSize * pixelNumber * 3 && mouseY > posY && mouseY < posY + pixelSize * pixelNumber * 3) {
            return new Point(getClickedPixel(mouseX, mouseY));
        }
        return null;
    }

    private Point getClickedPixel(int mouseX, int mouseY) {

        int i, j;
        if (zoomedOutView) {
            i = (mouseX - posX) / pixelSize;
            if (i >= pixelNumber) i -= pixelNumber;
            if (i >= pixelNumber) i -= pixelNumber;
            j = (mouseY - posY) / pixelSize;
            if (j >= pixelNumber) j -= pixelNumber;
            if (j >= pixelNumber) j -= pixelNumber;
        } else {
            i = (mouseX - posX) / pixelSize / 3;
            j = (mouseY - posY) / pixelSize / 3;
        }
        return new Point(i, j);
    }

    public void toggleZoom() {

        zoomedOutView = !zoomedOutView;
    }

    public Color getColor(int mouseX, int mouseY) {

        Point point = checkBounds(mouseX, mouseY);
        if (point != null) {
            return pixels[point.x][point.y];
        }
        return null;
    }

    public void undo() {

        if (undoIndex >= 0) {
            ArrayList<Action> undoStep = undoList.get(undoIndex);
            for (Action undo : undoStep) {
                pixels[undo.getX()][undo.getY()] = undo.getOldColor();
            }
            undoIndex--;
        }
        System.out.println(undoIndex);
    }

    public void redo() {

        if (undoIndex < undoList.size() - 1) {
            undoIndex++;
            ArrayList<Action> redoList = undoList.get(undoIndex);
            for (Action redo : redoList) {
                pixels[redo.getX()][redo.getY()] = redo.getNewColor();
            }
        }
    }

    public void openFile(Component parent) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG files", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG files", "jpg");
        FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("BMP files", "bmp");
        fileChooser.setFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.addChoosableFileFilter(bmpFilter);
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                if (image.getWidth() != image.getHeight()) {
                    JOptionPane.showMessageDialog(parent, "Image size not proportional", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (image.getWidth() > 256 || image.getHeight() > 256) {
                    JOptionPane.showMessageDialog(parent, "Image size too large", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pixelNumber = image.getWidth();
                pixelSize = parent.getWidth() / pixelNumber / 3;
                pixels = new Color[pixelNumber][pixelNumber];
                for (int i = 0; i < pixelNumber; i++) {
                    for (int j = 0; j < pixelNumber; j++) {
                        pixels[i][j] = new Color(image.getRGB(i, j));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile(Component parent) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG files", "png");
        fileChooser.setFileFilter(pngFilter);
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = new BufferedImage(pixelNumber, pixelNumber, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < pixelNumber; i++) {
                    for (int j = 0; j < pixelNumber; j++) {
                        image.setRGB(i, j, pixels[i][j].getRGB());
                    }
                }
                if (!file.getName().endsWith(".png")) {
                    file = new File(file.getPath() + ".png");
                }
                int confirm = -1;
                if (file.exists()) {
                    //confirm overwrite
                    confirm = JOptionPane.showConfirmDialog(parent, "File already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                }
                if (confirm == JFileChooser.APPROVE_OPTION) ImageIO.write(image, "png", file);
                if (confirm == JFileChooser.CANCEL_OPTION) saveFile(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void resize(int min) {

        pixelSize = min;
    }

    public void flood(Color color, Component parent) {

        int confirm;
        confirm = JOptionPane.showConfirmDialog(parent, "Fill entire canvas?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JFileChooser.APPROVE_OPTION) {
            for (int i = 0; i < pixelNumber; i++) {
                for (int j = 0; j < pixelNumber; j++) {
                    addToUndo(color, new Point(i, j));
                    pixels[i][j] = color;
                }
            }
            pushTempToUndo();
        }
    }

    public void toggleGrid() {

        showGrid = !showGrid;
    }

    public int getPixelNumber() {

        return pixelNumber;

    }

    public void changePixels(int value, int width) {

        pixelNumber = value;
        pixelSize = width / pixelNumber / 3;
        if (pixelSize < 1) pixelSize = 1;
        pixels = new Color[pixelNumber][pixelNumber];
        for (int i = 0; i < pixelNumber; i++) {
            for (int j = 0; j < pixelNumber; j++) {
                pixels[i][j] = new Color(i * pixelSize, j * pixelSize, 0);
            }
        }
    }

    public void setBrushSize(int value) {

        brushSize = value;
    }

    public int getBrushSize() {

        return brushSize;

    }

    public void setGridColor(Color color) {

        gridColor = color;
    }

}


