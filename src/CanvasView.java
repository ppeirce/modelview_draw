package edu.nyu.cs.pqs.ps5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * GUI implementation of the {@link CanvasListener} interface.
 * It displays a window composed of two parts: a canvas that can be drawn on,
 * and a panel of buttons that allow the user to change the behavior of the canvas.
 *
 * @author Peter Peirce
 */
public class CanvasView implements CanvasListener {
    private final int toolbarHeight = 45;
    private final Color toolbarBackgroundColor = Color.darkGray;
    private int width;
    private int height;
    private CanvasModel model;
    private JLabel imageLabel;
    private BufferedImage canvas;
    
    /**
     * Constructs a new window of the specified size and that communicates with the given model.
     * @param model the {@link CanvasModel CanvasModel} this view is associated with
     * @param width the width of the drawing area
     * @param height the height of the drawing area
     */
    public CanvasView(CanvasModel model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;
        model.addCanvasListener(this);
        openWindow();
    }

    /**
     * <p>Shows the image passed into this method on the primary drawing panel of the window.
     * The image cannot be null.</p>
     * @param newImage {@link BufferedImage} to be displayed in the window
     * @throws IllegalArgumentException if the BufferedImage is null
     */
    @Override
    public void updateImage(BufferedImage newImage) {
        if (newImage == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }
        canvas = newImage;
        imageLabel.setIcon(new ImageIcon(canvas));
    }
    
    private void openWindow() {
        JFrame frame = createFrame();
        JPanel drawingPanel = createPanel();
        JToolBar toolbar = createToolBar();
        drawingPanel.setBackground(Color.white);
        frame.getContentPane().add(drawingPanel, BorderLayout.NORTH);
        frame.getContentPane().add(toolbar, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private JFrame createFrame() {
        JFrame newFrame = new JFrame("Drawing Application");
        newFrame.setLayout(new BorderLayout());
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(width, height+toolbarHeight);
        newFrame.setResizable(false);
        return newFrame;
    }
    
    private JPanel createPanel() {
        JPanel newPanel = new JPanel(new BorderLayout());
        imageLabel = createNewLabelFromCanvas();
        newPanel.add(imageLabel);
        return newPanel;
    }
    
    private JLabel createNewLabelFromCanvas() {
        canvas = createEmptyBufferedImage();
        JLabel imageLabel = new JLabel(new ImageIcon(canvas));
        imageLabel.addMouseListener(mouseDown());
        imageLabel.addMouseMotionListener(mouseDrag());
        return imageLabel;
    }
    
    private BufferedImage createEmptyBufferedImage() {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    private JToolBar createToolBar() {
        JToolBar toolbar = new JToolBar("Buttons");
        toolbar.setBackground(toolbarBackgroundColor);
        toolbar.setFloatable(false);
        addButtons(toolbar);
        return toolbar;
    }
    
    private void addButtons(JToolBar toolbar) {
        List<JButton> toolbarButtons = createButtons();
        for (JButton button : toolbarButtons) {
            toolbar.add(button);
        }
    }
    
    private List<JButton> createButtons() {
        List<JButton> buttons = new ArrayList<>();
        buttons.add(newColorButton(Color.black, "black"));
        buttons.add(newColorButton(Color.red, "red"));
        buttons.add(newColorButton(Color.blue, "blue"));
        buttons.add(newColorButton(Color.white, "eraser"));
        buttons.add(newClearButton());
        return buttons;
    }
    
    private JButton newColorButton(Color color, String buttonName) {
        JButton button = new JButton(buttonName);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setColor(color);
            }
        });
        return button;
    }
    
    private JButton newClearButton() {
        JButton button = new JButton("new canvas");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clearCanvas();
            }
        });
        return button;
    }
    
    /**
     * Creates a new {@link MouseAdapter} which activates when
     * the mouse button is pressed down. It tells the model the 
     * position of the mouse and to draw a point there.
     * @return the new MouseAdapter
     */
    private MouseAdapter mouseDown() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                model.updatePrevStrokePoint(e.getPoint());
                model.drawLine(e.getPoint());
            }
        };
    }
    
    /**
     * Creates a new {@link MouseAdapter} which activates when
     * the mouse is pressed down and dragged. It tells the model
     * to draw a line from the previous position of the mouse to
     * its new position.
     * @return the new MouseAdapter
     */
    private MouseAdapter mouseDrag() {
        return new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                model.drawLine(e.getPoint());
            }
        };
    }

}
