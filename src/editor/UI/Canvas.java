package editor.UI;


import java.awt.*;
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
        if (point != null) {
            if (!controlDown) {
                pixels[point.x][point.y] = color;
            } else {
                Color replaceColor = pixels[point.x][point.y];
                for (int i = 0; i < pixelNumber; i++) {
                    for (int j = 0; j < pixelNumber; j++) {
                        if (pixels[i][j] == replaceColor) {
                            pixels[i][j] = color;
                        }
                    }
                }
            }
            previewPixel = null;
            points.clear();
        }
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
            return pixels[point.x][point.y];
        }
        return null;
    }

    public boolean preview(int mouseX, int mouseY, boolean controlDown, Color selectedColor) {
        Point point = checkBounds(mouseX, mouseY);
        boolean notMoved = comparePoints(point, previewPixel);
        if (point != null && notMoved) {
            long now = System.currentTimeMillis();
            if (now - lastTime > 200) {
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
                return true;
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
            return true;
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
            return true;
        }
        return false;
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
}


