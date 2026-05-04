package com.demian.view;

import com.demian.physics.rigidbody.Body;
import com.demian.physics.rigidbody.shapes.Circle;
import com.demian.physics.rigidbody.shapes.Rect;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class BodyDataPanel extends JPanel {

    private final Body body;

    private final JLabel nameLabel = new JLabel();
    private final JLabel massLabel = new JLabel();
    private final JLabel vxLabel = new JLabel();
    private final JLabel vyLabel = new JLabel();
    private final JLabel positionLabel = new JLabel();
    private final JLabel axLabel = new JLabel();
    private final JLabel ayLabel = new JLabel();


    private final DecimalFormat df = new DecimalFormat("#.00");

    public BodyDataPanel(Body body) {
        this.body = body;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(nameLabel);
        add(massLabel);
        add(vxLabel);
        add(vyLabel);
        add(axLabel);
        add(ayLabel);

        add(positionLabel);

        update();
    }

    public void update() {
        nameLabel.setText(getBodyName());
        massLabel.setText("mass: " + body.getMass());
        vxLabel.setText("velocity x: " + df.format(body.getVelocity_vec().x));
        vyLabel.setText("velocity y: " + df.format(body.getVelocity_vec().y));
        positionLabel.setText("position: [" + df.format(body.getX()) + ", " + df.format(body.getY()) + "]");
        axLabel.setText("acceleration x: " + df.format((body.getAccel_vec().x)));
        ayLabel.setText("acceleration y: " + df.format((body.getAccel_vec().y)));
    }

    private String getBodyName() {
        if (body instanceof Circle) return "Circle";
        if (body instanceof Rect r) return r.getWidth() == r.getHeight() ? "Square" : "Rect";
        return "Unknown";
    }
}
