package com.demian.view;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.World;
import com.demian.physics.rigidbody.shapes.Circle;
import com.demian.physics.rigidbody.shapes.Rect;
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

    private final static Stroke axisLinesStroke = new BasicStroke(0.20f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{1.0f}, 0.0f);
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
        Rect ground = new Rect(Double.POSITIVE_INFINITY, -1_000_000, -1_000_000, 2_000_000, 1_000_000);
        world.addBody(ground);
        Rect wallLeft = new Rect(Double.POSITIVE_INFINITY, -1_000_000, -1_000_000, 999_900, 2_000_000);
        world.addBody(wallLeft);
        Rect wallRight = new Rect(Double.POSITIVE_INFINITY, 400, -1_000_000, 999_600, 2_000_000);
        world.addBody(wallRight);
        Rect ceiling = new Rect(Double.POSITIVE_INFINITY, -1_000_000, 400, 2_000_000, 999_600);
        world.addBody(ceiling);

        Circle c1 = new Circle(10, 360, 360, 20);
        c1.setVelocity_vec(new Vector2D(-50, -50));
        Circle c2 = new Circle(10, 0, 0, 20);
        c2.setVelocity_vec(new Vector2D(50, 50));
        Circle c3 = new Circle(10, -50, 100, 10);
        Circle c4 = new Circle(10, -50, 50, 10);
        Rect r1 = new Rect(10, 300, 300, 100, 20);
        world.addBodies(c3, c4, r1);
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



        drawShapes(g2);
        drawOverlay(g2);
        drawDirectionVecs(g2);

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawOverlay(Graphics2D g2) {
        g2.setStroke(axisLinesStroke);
        g2.setColor(Color.BLACK);
        g2.drawLine(-1_000_000, 0, 1_000_000, 0);
        g2.drawLine(0, -1_000_000, 0, 1_000_000);
    }

    private void drawShapes(Graphics2D g2) {
        g2.setStroke(bodyStroke);
        for (Body body : world.getBodies()) {
            if (body instanceof Rect rect) {
                if (body.isImmovable()) {
                    g2.setColor(Color.GRAY);
                    g2.fillRect((int) rect.getX(),  (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                }
                else {
                    g2.setColor(Color.BLACK);
                    g2.drawRect((int) rect.getX(),  (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
                    g2.setColor(Color.RED);
                    g2.drawLine((int) rect.getX(), (int) rect.getY(), (int) rect.getX(), (int) rect.getY());
                }
            } else if (body instanceof Circle circle) {
                g2.setColor(Color.BLACK);
                g2.drawOval(
                        (int) circle.getX(),
                        (int) circle.getY(),
                        (int) (circle.radius * 2),
                        (int) (circle.radius * 2)
                );
                g2.setColor(Color.RED);
                g2.drawLine((int) circle.getX(), (int) (circle.getY() + circle.radius), (int) circle.getX(), (int) (circle.getY() + circle.radius));
            }
        }
    }

    private void drawDirectionVecs(Graphics2D g2) {
        g2.setStroke(bodyStroke);
        g2.setColor(Color.BLACK);
        for (Body body : world.getBodies()) {
            if (Double.isInfinite(body.getMass()))
                continue;

            Vector2D center = body.getCenterOfMass();
            Vector2D direction = body.getVelocity_vec()
                    .normalized()
                    .scale(Math.clamp(body.getVelocity_vec().getLength(), 0, 20));

            Vector2D normal = direction.rotate(Math.PI / 2.0).normalized();

            int endX = toInt(center.x + direction.x);
            int endY = toInt(center.y + direction.y);
            int crossLength = toInt(direction.getLength() / 10);

            Vector2D crossOrigin = new Vector2D(endX, endY).subtract(direction.normalized().scale(crossLength));
            Vector2D normalScaled = normal.scale(crossLength);
            Vector2D arrowVec1 = crossOrigin.subtract(normalScaled);
            Vector2D arrowVec2 = crossOrigin.add(normalScaled);

            g2.drawLine(
                    toInt(center.x),
                    toInt(center.y),
                    toInt(crossOrigin.x),
                    toInt(crossOrigin.y)
            );

            g2.fillPolygon(
                    new int[]{endX, toInt(arrowVec1.x), toInt(arrowVec2.x)},
                    new int[]{endY, toInt(arrowVec1.y), toInt(arrowVec2.y)},
                    3
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
