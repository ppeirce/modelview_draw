package edu.nyu.cs.pqs.ps5;

import java.awt.image.BufferedImage;

/**
 * The listener interface for receiving actions from a {@link CanvasModel}.
 * A class that wishes to process a CanvasModel event implements this interface,
 * and the associated object must register with the CanvasModel using the
 * {@link CanvasModel#addCanvasListener} method.
 *
 * @author Peter Peirce
 */
public interface CanvasListener {
    /**
     * Invoked when the CanvasModel has prepared a new image.
     */
    void updateImage(BufferedImage newImage);
}
