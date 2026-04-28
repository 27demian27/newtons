package com.demian.view;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.World;
import com.demian.physics.rigidbody.shapes.Square;
import com.demian.physics.util.Vector2D;
import com.demian.simulation.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sandbox extends JPanel {

    private final World world;
    private final Simulation simulation;

    private double scale;
    private double translateX;
    private double translateY;

    private Point lastDragPoint;
    private boolean initialized;

    private final static Stroke axisLinesStroke = new BasicStroke(0.20f);
    private final static Stroke bodyStroke = new BasicStroke(0.50f);

    public Sandbox(World world, Simulation simulation) {
        this.world = world;
        this.simulation = simulation;

        scale = 1.0;
        translateX = 0.0;
        translateY = 0.0;
        initialized = false;
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

        if (!initialized) {
            scale = scale * 2.0;
            translateX = getWidth() / 2.0;
            translateY = getHeight() / 2.0;
            initialized = true;
        }

        g2.translate(translateX, translateY);
        g2.scale(scale, -scale);

        g2.setColor(Color.BLACK);
        g2.setStroke(axisLinesStroke);
        g2.drawLine(-1_000_000, 0, 1_000_000, 0);
        g2.drawLine(0, -1_000_000, 0, 1_000_000);



        drawShapes(g2);
        drawDirectionVecs(g2);

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawShapes(Graphics2D g2) {
        g2.setStroke(bodyStroke);
        for (Body body : world.getBodies()) {
            if (body instanceof Square square) {
                g2.drawRect((int) square.getX(),  (int) square.getY(), (int) square.getSize(), (int) square.getSize());
            }
        }
    }

    private void drawDirectionVecs(Graphics2D g2) {
        g2.setStroke(bodyStroke);
        for (Body body : world.getBodies()) {
            Vector2D center = body.getCenterOfMass();
            Vector2D direction = body.getDirection_vec().scale(body.getMass());
            Vector2D normal = direction.rotate(Math.PI / 2.0).normalized();

            int centerX = toInt(center.x);
            int centerY = toInt(center.y);
            int endX = toInt(center.x + direction.x);
            int endY = toInt(center.y + direction.y);
            int crossLength = toInt(direction.getLength() / 10);

            Vector2D crossOrigin = new Vector2D(endX, endY).subtract(direction.normalized().scale(crossLength));
            Vector2D normalScaled = normal.scale(crossLength);
            Vector2D arrowVec1 = crossOrigin.subtract(normalScaled);
            Vector2D arrowVec2 = crossOrigin.add(normalScaled);

            g2.drawLine(
                    centerX,
                    centerY,
                    toInt(crossOrigin.x),
                    toInt(crossOrigin.y)
            );

            g2.drawLine(
                    endX,
                    endY,
                    toInt(arrowVec1.x),
                    toInt(arrowVec1.y)
            );
            g2.drawLine(
                    endX,
                    endY,
                    toInt(arrowVec2.x),
                    toInt(arrowVec2.y)
            );

            g2.drawLine(
                    toInt(crossOrigin.x - normalScaled.x),
                    toInt(crossOrigin.y - normalScaled.y),
                    toInt(crossOrigin.x + normalScaled.x),
                    toInt(crossOrigin.y + normalScaled.y)
            );

        }
    }

    private int toInt(double val) {
        return Math.toIntExact(Math.round(val));
    }

    private void configureControls() {

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private final Cursor defaultCursor =  new Cursor(Cursor.DEFAULT_CURSOR);
            private final Cursor panningCursor =  new Cursor(Cursor.MOVE_CURSOR);


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

            scale = Math.clamp(scale, 0.005, 100.0);

            Point p = e.getPoint();
            translateX = (int) (p.x - (p.x - translateX) * (scale / oldScale));
            translateY = (int) (p.y - (p.y - translateY) * (scale / oldScale));

        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "pauseSim");
        getActionMap().put("pauseSim", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (simulation.isPaused())
                    simulation.unpause();
                else
                    simulation.pause();
            }
        });
    }

    public void configure() {
        setBackground(Color.WHITE);
        setLayout(new OverlayLayout(this));
        configureControls();
    }
}
