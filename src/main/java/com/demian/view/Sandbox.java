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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

public class Sandbox extends JLayeredPane {

    private final World world;
    private final Simulation simulation;

    private double scale;
    private double translateX;
    private double translateY;

    private boolean initialized;

    private final Cursor defaultCursor =  new Cursor(Cursor.DEFAULT_CURSOR);
    private final Cursor panningCursor =  new Cursor(Cursor.MOVE_CURSOR);
    private final Stroke axisLinesStroke = new BasicStroke(0.20f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{1.0f}, 0.0f);
    private final Stroke bodyStroke = new BasicStroke(0.50f);

    private boolean showBodyDataPanel;
    private BodyDataPanel bodyDataPanel;
    private Body selectedBody;
    private Point lastDragPoint;
    private float lastDragUpdate;
    private boolean mousePressed;

    public Sandbox(World world, Simulation simulation) {
        this.world = world;
        this.simulation = simulation;

        scale = 1.0;
        translateX = 0.0;
        translateY = 0.0;
        initialized = false;
        showBodyDataPanel = false;
        selectedBody = null;
        lastDragUpdate = Float.POSITIVE_INFINITY;
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

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

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

        if (showBodyDataPanel) bodyDataPanel.update();

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


                Rectangle2D.Double shape = new Rectangle2D.Double(
                        rect.getX(),
                        rect.getY(),
                        rect.getWidth(),
                        rect.getHeight()
                );

                if (body.isImmovable()) {
                    g2.setColor(Color.GRAY);
                    g2.fill(shape);
                } else {
                    if (body == selectedBody) {
                        Stroke oldStroke = g2.getStroke();

                        g2.setColor(new Color(163, 216, 230));
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.draw(shape);

                        g2.setStroke(oldStroke);
                    }
                    g2.setColor(Color.BLACK);
                    g2.draw(shape);
                }
            } else if (body instanceof Circle circle) {

                double x = circle.getX();
                double y = circle.getY();
                double d = circle.radius * 2.0;

                Ellipse2D.Double shape = new Ellipse2D.Double(x, y, d, d);

                if (body == selectedBody) {
                    Stroke oldStroke = g2.getStroke();

                    g2.setColor(new Color(163, 216, 230));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(shape);

                    g2.setStroke(oldStroke);
                }
                g2.setColor(Color.BLACK);
                g2.draw(shape);
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
            Vector2D velocity = body.getVelocity_vec();

            if (velocity.getLength() == 0)
                continue;

            Vector2D direction = velocity
                    .normalized()
                    .scale(Math.clamp(velocity.getLength(), 0, 20));

            Vector2D normal = direction.rotate(Math.PI / 2.0).normalized();

            double endX = center.x + direction.x;
            double endY = center.y + direction.y;

            double crossLength = direction.getLength() / 10.0;

            Vector2D crossOrigin = new Vector2D(endX, endY)
                    .subtract(direction.normalized().scale(crossLength));

            Vector2D normalScaled = normal.scale(crossLength);

            Vector2D arrowVec1 = crossOrigin.subtract(normalScaled);
            Vector2D arrowVec2 = crossOrigin.add(normalScaled);

            g2.draw(new Line2D.Double(
                    center.x, center.y,
                    crossOrigin.x, crossOrigin.y
            ));

            Path2D.Double arrow = new Path2D.Double();
            arrow.moveTo(endX, endY);
            arrow.lineTo(arrowVec1.x, arrowVec1.y);
            arrow.lineTo(arrowVec2.x, arrowVec2.y);
            arrow.closePath();

            g2.fill(arrow);
        }
    }

    private void configureControls() {

        MouseAdapter mouseAdapter = new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    handleMouseClick(e);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    selectedBody = null;
                }
                lastDragPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastDragPoint = null;
                mousePressed = false;
                setCursor(defaultCursor);
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    handlePanning(e);
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selectedBody != null && !selectedBody.isImmovable()) {
                        handleBodyDrag(selectedBody, e);
                    }
                }
                lastDragPoint = e.getPoint();
                lastDragUpdate = 0.0f;
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

            scale = Math.clamp(scale, 0.005, 4.0); //TODO: bug: muren verdwijnen bij scale > 4

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

    public void handleBodyHold() {
        if (mousePressed) {
            if (selectedBody != null && !selectedBody.isImmovable()) {
                Body body = selectedBody;
                if (body instanceof Rect rect) {
                    rect.setX(toWorldPoint(lastDragPoint).x - rect.getWidth() / 2.0);
                    rect.setY(toWorldPoint(lastDragPoint).y - rect.getHeight() / 2.0);
                } else if (body instanceof Circle circle) {
                    circle.setX(toWorldPoint(lastDragPoint).x - circle.radius);
                    circle.setY(toWorldPoint(lastDragPoint).y - circle.radius);
                }

                if (lastDragUpdate > 0.1f) body.setVelocity_vec(new Vector2D(0, 0));
            }
        }
    }

    private void handleBodyDrag(Body body, MouseEvent e) {
        System.out.println(lastDragUpdate);
        Vector2D worldPoint = toWorldPoint(e.getPoint());
        Vector2D worldLastDragPoint = toWorldPoint(lastDragPoint);
        if (lastDragUpdate <= 0.1f && lastDragUpdate >= 0.016f) {
            body.setVelocity_vec(new Vector2D(worldPoint.subtract(worldLastDragPoint)).scale(1/lastDragUpdate));
        }
    }

    private void handlePanning(MouseEvent e) {
        setCursor(panningCursor);
        int dx = e.getX() - lastDragPoint.x;
        int dy = e.getY() - lastDragPoint.y;
        translateX += dx;
        translateY += dy;
        lastDragPoint = e.getPoint();
    }

    private void handleMouseClick(MouseEvent e) {
        Vector2D worldPoint = toWorldPoint(e.getPoint());

        Optional<Body> body = world.findBody(worldPoint.x, worldPoint.y);
        body.ifPresentOrElse(
                (b) -> {
                    showBodyDataPanel = true;
                    selectedBody = b;

                    if (bodyDataPanel != null) {
                        this.remove(bodyDataPanel);
                    }

                    bodyDataPanel = new BodyDataPanel(b);

                    bodyDataPanel.setSize(bodyDataPanel.getPreferredSize());

                    int padding = 10;
                    int x = this.getWidth() - bodyDataPanel.getWidth() - padding;
                    int y = padding;

                    bodyDataPanel.setLocation(x, y);

                    this.add(bodyDataPanel, JLayeredPane.PALETTE_LAYER);
                    this.revalidate();
                    this.repaint();
                },
                () -> {
                    showBodyDataPanel = false;
                    selectedBody = null;

                    if (bodyDataPanel != null) {
                        this.remove(bodyDataPanel);
                        bodyDataPanel = null;
                        this.revalidate();
                        this.repaint();
                    }
                }
        );
    }

    public Vector2D toWorldPoint(Point viewPoint) {
        return new Vector2D((viewPoint.getX() - translateX) / scale, -(viewPoint.getY() - translateY) / scale);
    }

    public void configure() {
        setBackground(Color.WHITE);
        setLayout(null);
        configureControls();
    }

    public void incrementLastDragUpdate(float t) {
        lastDragUpdate += t;
    }
}
