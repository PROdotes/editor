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
    boolean displayFullGrid = true;
    Color[][] pixels;
    long lastTime = System.currentTimeMillis();
    Point previewPixel = null;
    Color previewColor = null;
    boolean preview = false;
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Action> undoList = new ArrayList<>();
    int undoIndex = -1;
    boolean lastControlState = false;
    boolean fileOperationActive = false;


    public Canvas(int pixelNumberIN, int pixelSizeIN) {
        pixelNumber = pixelNumberIN;
        pixelSize = pixelSizeIN;
        pixels = new Color[pixelNumber][pixelNumber];

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
                        if (displayFullGrid) {
                            g2d.fillRect(posX + i * pixelSize + k * pixelSize * pixelNumber, posY + j * pixelSize + l * pixelSize * pixelNumber, pixelSize, pixelSize);
                        } else {
                            g2d.fillRect(posX + i * pixelSize * 3 + k * pixelSize * pixelNumber, posY + j * pixelSize * 3 + l * pixelSize * pixelNumber, pixelSize * 3, pixelSize * 3);
                        }
                    }
                }
            }
        }
    }

    public void setColor(int mouseX, int mouseY, Color color, boolean controlDown) {

        Point point = checkBounds(mouseX, mouseY);
        if (preview && point != null) {
            if (comparePoints(point, previewPixel)) {
                preview = false;
                pixels[point.x][point.y] = previewColor;
            }
        }
        if (point != null && pixels[point.x][point.y] != color) {
            if (!controlDown) {
                addToUndo(color, point);
                pixels[point.x][point.y] = color;
            } else {
                Color replaceColor = pixels[point.x][point.y];
                for (int i = 0; i < pixelNumber; i++) {
                    for (int j = 0; j < pixelNumber; j++) {
                        if (pixels[i][j] == replaceColor) {
                            addToUndo(color, point);
                            pixels[i][j] = color;
                        }
                    }
                }
            }
            previewPixel = null;
            points.clear();
        }
    }

    private void addToUndo(Color color, Point point) {
        while (undoIndex < undoList.size() - 1) {
            undoList.remove(undoList.size() - 1);
        }
        undoList.add(new Action(point, pixels[point.x][point.y], color));
        undoIndex++;
        System.out.println("Added to undo list: " + undoList.get(undoIndex).getString());
        System.out.println("Undo list size: " + undoList.size());
    }

    private Point checkBounds(int mouseX, int mouseY) {
        if (mouseX > posX && mouseX < posX + pixelSize * pixelNumber * 3 && mouseY > posY && mouseY < posY + pixelSize * pixelNumber * 3) {
            return new Point(getClickedPixel(mouseX, mouseY));
        }
        return null;
    }

    private Point getClickedPixel(int mouseX, int mouseY) {
        int i, j;
        if (displayFullGrid) {
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

    public void toggleDisplayMode() {
        displayFullGrid = !displayFullGrid;
    }

    public Color getColor(int mouseX, int mouseY) {
        Point point = checkBounds(mouseX, mouseY);
        if (point != null) {
            if (!preview) {
                return pixels[point.x][point.y];
            } else {
                return previewColor;
            }
        }
        return null;
    }

    public boolean preview(int mouseX, int mouseY, boolean controlDown, Color selectedColor) {
        boolean returnBool = false;
        if (!fileOperationActive) {
            Point point = checkBounds(mouseX, mouseY);
            boolean notMoved = comparePoints(point, previewPixel);
            if (point != null && notMoved) {
                if (controlDown != lastControlState) {
                    if (controlDown) {
                        if (preview) pixels[point.x][point.y] = previewColor;
                        preview = false;
                        fetchPoints(point);
                    } else {
                        for (Point p : points) {
                            pixels[p.x][p.y] = previewColor;
                        }
                        preview = false;
                        points.clear();
                    }
                }
                long now = System.currentTimeMillis();
                if (now - lastTime > 250) {
                    preview = !preview;
                    lastTime = now;
                    if (preview) {
                        if (points.size() < 2) {
                            pixels[point.x][point.y] = selectedColor;
                        } else {
                            for (Point p : points) {
                                pixels[p.x][p.y] = selectedColor;
                            }
                        }
                    } else {
                        if (points.size() < 2) {
                            pixels[point.x][point.y] = previewColor;
                        } else {
                            for (Point p : points) {
                                pixels[p.x][p.y] = previewColor;
                            }
                        }
                    }
                    returnBool = true;
                }
            }
            if (!notMoved && point != null) {
                preview = true;
                if (previewPixel != null) {
                    if (points.size() < 2) {
                        pixels[previewPixel.x][previewPixel.y] = previewColor;
                    } else {
                        for (Point p : points) {
                            pixels[p.x][p.y] = previewColor;
                        }
                    }
                }
                previewPixel = point;
                if (controlDown) {
                    fetchPoints(point);
                }
                previewColor = pixels[point.x][point.y];
                if (points.size() < 2) {
                    pixels[point.x][point.y] = selectedColor;
                } else {
                    for (Point p : points) {
                        pixels[p.x][p.y] = selectedColor;
                    }
                }
                returnBool = true;
            }
            if (point == null && preview) {
                preview = false;
                if (previewPixel != null) {
                    if (points.size() < 2) {
                        pixels[previewPixel.x][previewPixel.y] = previewColor;
                    } else {
                        for (Point p : points) {
                            pixels[p.x][p.y] = previewColor;
                        }
                    }
                }
                previewPixel = null;
                previewColor = null;
                points.clear();
                returnBool = true;
            }
            lastControlState = controlDown;
        }
        return returnBool;
    }

    private boolean comparePoints(Point point, Point previewPixel) {
        if (previewPixel == null || point == null) return false;
        return point.x == previewPixel.x && point.y == previewPixel.y;
    }

    public void fetchPoints(Point point) {
        Color controlColor = pixels[point.x][point.y];
        points.clear();
        for (int i = 0; i < pixelNumber; i++) {
            for (int j = 0; j < pixelNumber; j++) {
                if (pixels[i][j] == controlColor) {
                    points.add(new Point(i, j));
                }
            }
        }
    }

    public void undo() {
        if (undoIndex >= 0) {
            Action undo = undoList.get(undoIndex);
            undoIndex--;
            pixels[undo.getX()][undo.getY()] = undo.getOldColor();
            System.out.println("Undid: " + undo.getString());
            points.remove(undo.getPoint());
            if (comparePoints(undo.getPoint(), previewPixel)) {
                preview = false;
                previewPixel = null;
                previewColor = null;
            }
        }
        System.out.println(undoIndex);
    }

    public void redo() {
        if (undoIndex < undoList.size() - 1) {
            undoIndex++;
            Action redo = undoList.get(undoIndex);
            pixels[redo.getX()][redo.getY()] = redo.getNewColor();
        }

        System.out.println("Undo list");
        for (Action action : undoList) {
            System.out.println(action.getString());
            System.out.println(undoIndex);
        }
    }

    public void openFile(Component parent) {
        fileOperationActive = true;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
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
                if (image.getWidth() != pixelNumber || image.getHeight() != pixelNumber) {
                    JOptionPane.showMessageDialog(parent, "Image size does not match grid size", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (int i = 0; i < pixelNumber; i++) {
                    for (int j = 0; j < pixelNumber; j++) {
                        pixels[i][j] = new Color(image.getRGB(i , j ));
                        System.out.println(pixels[i][j]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lastTime = System.currentTimeMillis();
        fileOperationActive = false;
    }

    public void saveFile(Component parent) {
        fileOperationActive = true;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
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
        lastTime = System.currentTimeMillis();
        fileOperationActive = false;
    }
}


