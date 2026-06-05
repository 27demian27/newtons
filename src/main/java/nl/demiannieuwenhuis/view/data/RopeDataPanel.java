package nl.demiannieuwenhuis.view.data;

import nl.demiannieuwenhuis.physics.Rope;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class RopeDataPanel extends JPanel {

    private final Rope rope;

    private final JLabel nameLabel = new JLabel();
    private final JLabel baseLengthLabel = new JLabel();
    private final JLabel stiffnessLabel = new JLabel();
    private final JLabel position1Label = new JLabel();
    private final JLabel position2Label = new JLabel();
    private final JLabel lengthLabel = new JLabel();
    private final JLabel stretchLabel = new JLabel();

    private final DecimalFormat df = new DecimalFormat("#.00");

    public RopeDataPanel(Rope rope) {
        this.rope = rope;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 12f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(nameLabel);
        add(baseLengthLabel);
        add(stiffnessLabel);
        add(position1Label);
        add(position2Label);
        add(lengthLabel);
        add(stretchLabel);

        nameLabel.setText("Rope");
        baseLengthLabel.setText("base length: " + df.format(rope.getBase_length()));
        baseLengthLabel.setText("stiffness factor: " + df.format(rope.getStiffness()));

        update();
    }

    public void update() {
        position1Label.setText("position1: [" + df.format(rope.getPos1().x) + ", " + df.format(rope.getPos1().y) + "]");
        position2Label.setText("position2: [" + df.format(rope.getPos2().x) + ", " + df.format(rope.getPos2().y) + "]");
        lengthLabel.setText("length: " + df.format(rope.getBase_length() + rope.getStretch()));
        stretchLabel.setText("stretch: " + df.format(rope.getStretch()));
    }

}
