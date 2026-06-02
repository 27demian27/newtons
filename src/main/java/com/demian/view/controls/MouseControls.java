package nl.demiannieuwenhuis.view.controls;

import nl.demiannieuwenhuis.physics.World;
import nl.demiannieuwenhuis.physics.rigidbody.Body;
import nl.demiannieuwenhuis.physics.rigidbody.shapes.Circle;
import nl.demiannieuwenhuis.physics.rigidbody.shapes.Rect;
import nl.demiannieuwenhuis.physics.util.Vector2D;
import nl.demiannieuwenhuis.view.Sandbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Optional;

public class MouseControls extends MouseAdapter {

    private Point lastDragPoint;
    private float lastDragUpdate;
    private boolean mousePressed;

    private final World world;
    private final Sandbox sandbox;

    public MouseControls(World world, Sandbox sandbox) {
        this.world = world;
        this.sandbox = sandbox;
        lastDragUpdate = Float.POSITIVE_INFINITY;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        if (SwingUtilities.isLeftMouseButton(e)) {
            Vector2D worldPoint = sandbox.toWorldPoint(e.getPoint());
            Optional<Body> body = world.findBody(worldPoint.x, worldPoint.y);
            if (body.isEmpty() || body.get() != sandbox.getSelectedBody()) sandbox.setSelectedBody(null);
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            sandbox.setSelectedBody(null);
        }
        lastDragPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastDragPoint = null;
        if (SwingUtilities.isLeftMouseButton(e)) {
            handleMouseClick(e);
        }
        mousePressed = false;
        sandbox.setCursor(sandbox.defaultCursor);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            handlePanning(e);
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (sandbox.getSelectedBody() != null && !sandbox.getSelectedBody().isImmovable()) {
                handleBodyDrag(sandbox.getSelectedBody(), e);
            }
        }
        lastDragPoint = e.getPoint();
        lastDragUpdate = 0.0f;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("mwheel moved");
        double oldScale = sandbox.getScale();
        double factor = 1.1;
        int notches = e.getWheelRotation();
        if (notches < 0)
            sandbox.setScale(sandbox.getScale() * Math.pow(factor, -notches));
        else
            sandbox.setScale(sandbox.getScale() / Math.pow(factor, notches));

        sandbox.setScale(Math.clamp(sandbox.getScale(), 0.005, 4.0)); //TODO: bug: muren verdwijnen bij scale > 4

        Point p = e.getPoint();
        sandbox.setTranslation(
                (int) (p.x - (p.x - sandbox.getTranslateX()) * (sandbox.getScale() / oldScale)),
                (int) (p.y - (p.y - sandbox.getTranslateY()) * (sandbox.getScale() / oldScale))
        );
    }

    public void handleBodyHold() {
        if (mousePressed) {
            if (sandbox.getSelectedBody() != null && !sandbox.getSelectedBody().isImmovable()) {
                Body body = sandbox.getSelectedBody();
                if (body instanceof Rect rect) {
                    rect.setX(sandbox.toWorldPoint(lastDragPoint).x - rect.getWidth() / 2.0);
                    rect.setY(sandbox.toWorldPoint(lastDragPoint).y - rect.getHeight() / 2.0);
                } else if (body instanceof Circle circle) {
                    circle.setX(sandbox.toWorldPoint(lastDragPoint).x - circle.radius);
                    circle.setY(sandbox.toWorldPoint(lastDragPoint).y - circle.radius);
                }

                if (lastDragUpdate > 0.1f) body.setVelocity_vec(new Vector2D(0, 0));
            }
        }
    }

    private void handleBodyDrag(Body body, MouseEvent e) {
        System.out.println(lastDragUpdate);
        Vector2D worldPoint = sandbox.toWorldPoint(e.getPoint());
        Vector2D worldLastDragPoint = sandbox.toWorldPoint(lastDragPoint);
        if (lastDragUpdate <= 0.1f && lastDragUpdate >= 0.016f) {
            body.setVelocity_vec(new Vector2D(worldPoint.subtract(worldLastDragPoint)).scale(1/lastDragUpdate));
        }
    }

    private void handlePanning(MouseEvent e) {
        if (lastDragPoint == null) return;
        sandbox.setCursor(sandbox.panningCursor);
        int dx = e.getX() - lastDragPoint.x;
        int dy = e.getY() - lastDragPoint.y;
        sandbox.translate(dx, dy);
        lastDragPoint = e.getPoint();
    }

    private void handleMouseClick(MouseEvent e) {
        Vector2D worldPoint = sandbox.toWorldPoint(e.getPoint());
        if (sandbox.isInInsertionMode()) {
            sandbox.setInsertionBody(new Circle(10, 0, 0, 5));
            sandbox.getInsertionBody().setX(worldPoint.x);
            sandbox.getInsertionBody().setY(worldPoint.y);
            world.addBody(sandbox.getInsertionBody());
        } else {
            Optional<Body> body = world.findBody(worldPoint.x, worldPoint.y);
            body.ifPresentOrElse(sandbox::addBodyDataPanel, sandbox::removeBodyDataPanel);
        }
    }


    public void incrementLastDragUpdate(float dt) {
        lastDragUpdate += dt;
    }
}
