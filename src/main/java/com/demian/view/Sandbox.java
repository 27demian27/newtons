package com.demian.view;

import com.demian.physics.Body;
import com.demian.physics.World;
import com.demian.physics.shapes.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class Sandbox extends JPanel {

    private final World world;

    private double scale;
    private double translateX;
    private double translateY;

    private Point lastDragPoint;

    public Sandbox(World world) {
        this.world = world;

        scale = 1.0;
        translateX = 0.0;
        translateY = 0.0;

    }

    public void initializeWorld() {
        Square s1 = new Square(10, 10, 0, 0);
        s1.setImmovable(true);
        world.addBody(s1);
        world.addBody(new Square(20, 10, 0, 40));
        world.addBody(new Square(50, 50, 30, 0));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.translate(translateX, translateY);
        g2.scale(scale, -scale);

        g2.setColor(Color.BLACK);
        g2.drawLine(-1_000_000, 0, 1_000_000, 0);
        g2.drawLine(0, -1_000_000, 0, 1_000_000);



        drawShapes(g2);

    }

    private void drawShapes(Graphics2D g2) {
        for (Body body : world.getBodies()) {
            if (body instanceof Square square) {
                g2.drawRect((int) square.getSx(),  (int) square.getSy(), (int) square.getSize(), (int) square.getSize());
            }
        }
    }

    private void configureControls() {

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private final Cursor defaultCursor =  new Cursor(Cursor.DEFAULT_CURSOR);
            private final Cursor panningCursor =  new Cursor(Cursor.MOVE_CURSOR);
            private final Cursor brushCursor   =  new Cursor(Cursor.CROSSHAIR_CURSOR);


            @Override
            public void mousePressed(MouseEvent e) {
                lastDragPoint = e.getPoint();
                if (SwingUtilities.isLeftMouseButton(e)) {

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastDragPoint = null;
                setCursor(defaultCursor);
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    setCursor(panningCursor);
                    int dx = e.getX() - lastDragPoint.x;
                    int dy = e.getY() - lastDragPoint.y;
                    translateX += dx;
                    translateY += dy;
                    lastDragPoint = e.getPoint();
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);


        addMouseWheelListener(e -> {
            double oldScale = scale;
            double factor = 1.1;
            int notches = e.getWheelRotation();
            if (notches < 0)
                scale *= Math.pow(factor, -notches);
            else
                scale /= Math.pow(factor, notches);

            scale = Math.clamp(scale, 0.005, 10.0);

            Point p = e.getPoint();
            translateX = (int) (p.x - (p.x - translateX) * (scale / oldScale));
            translateY = (int) (p.y - (p.y - translateY) * (scale / oldScale));

            repaint();
        });
    }

    public void configure() {
        setBackground(Color.WHITE);
        setLayout(new OverlayLayout(this));
        configureControls();
    }
}
