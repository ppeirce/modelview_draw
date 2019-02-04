package edu.nyu.cs.pqs.ps5;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The model of a drawing application. Listeners to this model must
 * implement the {@link CanvasListener} interface. The width and height of the canvas, as
 * well as the brush size and color, are all user-definable. This model sends an updated
 * {@link BufferedImage} to its listeners every time one is ready to be send.
 *
 * @author Peter Peirce
 */
public class CanvasModel {
    private final Color defaultBrushColor = Color.black;
    private final int defaultBrushRadius = 2;
    private int brushRadius;
    private int width;
    private int height;
    private Point prevStrokePoint;
    private Color brushColor;
    private List<CanvasListener> listeners;
    private BufferedImage canvas;
    private Graphics2D graphics;

    /**
     * Constructs a blank canvas model. Sets the brush size and color
     * to the default settings.
     * @param width the width of the canvas
     * @param height the height of the canvas
     */
    public CanvasModel(int width, int height) {
        this.brushRadius = defaultBrushRadius;
        this.brushColor = defaultBrushColor;
        this.width = width;
        this.height = height;
        this.listeners = new ArrayList<>();
        newBlankCanvas();
    }

    /**
     * Adds the specified canvas listener to receive data from this model.
     * @param listener the canvas listener
     */
    public void addCanvasListener(CanvasListener listener) {
        listeners.add(listener);
    }

    /**
     * Sets the brushes color
     * @param color the {@link Color} of the brush
     */
    public void setColor(Color color) {
        brushColor = color;
        graphics.setColor(brushColor);
    }

    /**
     * Sets the color of every pixel on the drawing canvas to white and tells
     * the listeners to update with the new canvas.
     */
    public void clearCanvas() {
        newBlankCanvas();
        fireUpdateImageEvent();
    }

    /**
     * Sets the position from where new lines will be drawn.
     * @param startStrokePoint the point new lines start at
     */
    public void updatePrevStrokePoint(Point startStrokePoint) {
        prevStrokePoint = startStrokePoint;
    }

    /**
     * Draws a line of the set color from the position of the last time
     * {@link CanvasModel#updatePrevStrokePoint} was called to the new point.
     * @param newPoint the end point of the line being drawn
     */
    public void drawLine(Point newPoint) {
        graphics.drawLine(prevStrokePoint.x, prevStrokePoint.y, newPoint.x, newPoint.y);
        prevStrokePoint = newPoint;
        fireUpdateImageEvent();
    }

    private void newBlankCanvas() {
        this.canvas = createNewImage();
        this.graphics = setUpGraphics();
    }

    private BufferedImage createNewImage() {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    private Graphics2D setUpGraphics() {
        Graphics2D g = (Graphics2D) canvas.getGraphics();
        g.setColor(brushColor);
        g.setStroke(new BasicStroke(brushRadius));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }

    private void fireUpdateImageEvent() {
        for (CanvasListener listener : listeners) {
            listener.updateImage(canvas);
        }
    }
    
    public List<CanvasListener> getListeners() {
        return new CopyOnWriteArrayList<CanvasListener>(this.listeners);
    }
    
    public Color getBrushColor() {
        return brushColor;
    }
    
    public Point getPreviousStrokePoint() {
        return prevStrokePoint;
    }

}
