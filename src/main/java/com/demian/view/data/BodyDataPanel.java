package nl.demiannieuwenhuis.view.data;

import nl.demiannieuwenhuis.physics.Rope;
import nl.demiannieuwenhuis.physics.rigidbody.Body;
import nl.demiannieuwenhuis.physics.rigidbody.shapes.Circle;
import nl.demiannieuwenhuis.physics.rigidbody.shapes.Rect;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BodyDataPanel extends JPanel {

    private final Body body;

    private final List<RopeDataPanel> ropeDataPanels = new ArrayList<>();

    private final JLabel nameLabel = new JLabel();
    private final JLabel massLabel = new JLabel();
    private final JLabel vxLabel = new JLabel();
    private final JLabel vyLabel = new JLabel();
    private final JLabel positionLabel = new JLabel();
    private final JLabel axLabel = new JLabel();
    private final JLabel ayLabel = new JLabel();


    private final DecimalFormat df = new DecimalFormat("#.00");

    public BodyDataPanel(Body body, List<Rope> ropes) {
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

        for (Rope rope : ropes) {
            RopeDataPanel ropeDataPanel = new RopeDataPanel(rope);
            add(ropeDataPanel);
            ropeDataPanels.add(ropeDataPanel);
        }

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

        for (RopeDataPanel ropeDataPanel : ropeDataPanels)
            ropeDataPanel.update();
    }

    private String getBodyName() {
        if (body instanceof Circle) return "Circle";
        if (body instanceof Rect r) return r.getWidth() == r.getHeight() ? "Square" : "Rect";
        return "Unknown";
    }
}
