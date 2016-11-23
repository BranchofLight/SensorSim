package com.company;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JFrame
{
    public ContentPanel(int width, int height)
    {
        this.setLayout(new MigLayout());
        this.setSize(new Dimension(width, height));
        this.setLocationRelativeTo(null);

        JPanel cpanel = new JPanel(new MigLayout("fillx"));
        this.add(cpanel, "width 100%, height 100%");

        JLabel sensorLabel = new JLabel("Sensors", SwingConstants.RIGHT);
        sensorLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(sensorLabel, "height 10%, grow, width 18%");

        JSpinner sensorSpinner = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 100.0, 1.0));
        cpanel.add(sensorSpinner, "height 10%, grow, width 18%");

        JLabel radiusLabel = new JLabel("Radius", SwingConstants.RIGHT);
        radiusLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(radiusLabel, "height 10%, grow, width 18%");

        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.1, 100.0, 0.1));
        cpanel.add(radiusSpinner, "height 10%, grow, width 18%, wrap");

        JLabel heuristicLabel = new JLabel("Heuristic", SwingConstants.RIGHT);
        heuristicLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(heuristicLabel, "height 10%, grow, width 18%");

        JComboBox heuristicCombo = new JComboBox();
        cpanel.add(heuristicCombo, "height 10%, span 3, grow, wrap");

        JButton startButton = new JButton("Simulate");
        cpanel.add(startButton, "height 10%, spanx, grow, wrap");

        JPanel simPanel = new JPanel(new MigLayout());
        cpanel.add(simPanel, "spanx, height 80%, grow, wrap");

        JTextArea logOutput = new JTextArea();
        logOutput.setEditable(false);
        cpanel.add(logOutput, "dock east, width 25%, height 100%");

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
