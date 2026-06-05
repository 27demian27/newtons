package nl.demiannieuwenhuis.view;

import nl.demiannieuwenhuis.physics.Rope;
import nl.demiannieuwenhuis.physics.rigidbody.Body;
import nl.demiannieuwenhuis.physics.World;
import nl.demiannieuwenhuis.physics.rigidbody.shapes.Circle;
import nl.demiannieuwenhuis.physics.rigidbody.shapes.Rect;
import nl.demiannieuwenhuis.physics.util.Vector2D;
import nl.demiannieuwenhuis.simulation.Simulation;
import nl.demiannieuwenhuis.view.data.BodyDataPanel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

@Getter
public class Sandbox extends JLayeredPane {

    private final World world;
    final Simulation simulation;

    @Setter
    private double scale;
    private double translateX;
    private double translateY;

    private boolean initialized;

    public final Cursor defaultCursor =  new Cursor(Cursor.DEFAULT_CURSOR);
    public final Cursor panningCursor =  new Cursor(Cursor.MOVE_CURSOR);
    public final Stroke axisLinesStroke = new BasicStroke(0.20f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{1.0f}, 0.0f);
    public final Stroke bodyStroke = new BasicStroke(0.50f);

    @Setter
    private Body selectedBody;

    private boolean showBodyDataPanel;
    private BodyDataPanel bodyDataPanel;

    @Setter
    private boolean inInsertionMode;
    @Setter
    private Body insertionBody;

    public Sandbox(World world, Simulation simulation) {
        this.world = world;
        this.simulation = simulation;
        scale = 1.0;
        translateX = 0.0;
        translateY = 0.0;
        initialized = false;
        showBodyDataPanel = false;
        selectedBody = null;
        setBackground(Color.WHITE);
        setLayout(null);
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
        Rect r2 = new Rect(Double.POSITIVE_INFINITY, 200, 300, 5, 5);
        Circle c5 = new Circle(10, 200, 200, 10);

        world.addBodies(c3, c4, r1, r2, c5);
        world.addRope(new Rope(r2.getCenterOfMass(), c5.getCenterOfMass()));
        world.addRope(new Rope(r1.getCenterOfMass(), c5.getCenterOfMass()));
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



        drawRopes(g2);
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

    private void drawRopes(Graphics2D g2) {
        g2.setStroke(bodyStroke);

        for (Rope rope : world.getRopes()) {
            if (rope.getStretch() > 0.0) {
                g2.draw(new Line2D.Double(
                                rope.getPos1().x,
                                rope.getPos1().y,
                                rope.getPos2().x,
                                rope.getPos2().y
                        )
                );
            } else {
                double compress = rope.getPos1().subtract(rope.getPos2()).length() - rope.getBase_length();
                g2.draw(new QuadCurve2D.Double(
                        rope.getPos1().x,
                        rope.getPos1().y,
                        (rope.getPos2().x + rope.getPos1().x) / 2.0 + compress,
                        (rope.getPos2().y + rope.getPos1().y) / 2.0 + compress,
                        rope.getPos2().x,
                        rope.getPos2().y
                ));
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

            if (velocity.length() == 0)
                continue;

            Vector2D direction = velocity
                    .normalized()
                    .scale(Math.clamp(velocity.length(), 0, 20));

            Vector2D normal = direction.rotate(Math.PI / 2.0).normalized();

            double endX = center.x + direction.x;
            double endY = center.y + direction.y;

            double crossLength = direction.length() / 10.0;

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

    public void addBodyDataPanel(Body b) {
        showBodyDataPanel = true;
        selectedBody = b;

        if (bodyDataPanel != null) {
            this.remove(bodyDataPanel);
        }

        bodyDataPanel = new BodyDataPanel(b, world.findRopesAttachedToBody(b));

        bodyDataPanel.setSize(bodyDataPanel.getPreferredSize());

        int padding = 10;
        int x = this.getWidth() - bodyDataPanel.getWidth() - padding;
        int y = padding;

        bodyDataPanel.setLocation(x, y);

        this.add(bodyDataPanel, JLayeredPane.PALETTE_LAYER);
        this.revalidate();
        this.repaint();
    }

    public void removeBodyDataPanel() {
        showBodyDataPanel = false;
        selectedBody = null;

        if (bodyDataPanel != null) {
            this.remove(bodyDataPanel);
            bodyDataPanel = null;
            this.revalidate();
            this.repaint();
        }
    }

    public void setTranslation(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }

    public void translate(double x, double y) {
        translateX += x;
        translateY += y;
    }

    public Vector2D toWorldPoint(Point viewPoint) {
        return new Vector2D((viewPoint.getX() - translateX) / scale, -(viewPoint.getY() - translateY) / scale);
    }
}
