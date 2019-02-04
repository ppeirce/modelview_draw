package edu.nyu.cs.pqs.ps5;

/**
 * The driver class for the Canvas Application. Launches a single
 * canvas with dimensions of 640x480. 
 * @author ppeirce
 *
 */
public class CanvasApp {
    private final int width = 640;
    private final int height = 480;

    private void run() {
        CanvasModel model = new CanvasModel(width, height);
        new CanvasView(model, width, height);
        new CanvasView(model, width, height);
    }
    
    public static void main(String[] args) {
        new CanvasApp().run();
    }

}
