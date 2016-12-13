package com.company;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Random;

public class ContentPanel extends JFrame
{
    public ContentPanel()
    {
        this.setLayout(new MigLayout());
        this.setSize(new Dimension(750, 500));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Sensor Sim");

        simPanel = new JPanel(null);
        listOfSensors = new ArrayList<>();

        JPanel cpanel = new JPanel(new MigLayout());
        this.add(cpanel, "width 100%, height 100%");

        JLabel diameterLabel = new JLabel("Diameter", SwingConstants.RIGHT);
        diameterLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(diameterLabel, "height 10%, width 160px");

        JSpinner diameterSpinner = new JSpinner(new SpinnerNumberModel(50.0, 50.0, 500.0, 50.0));
        diameterSpinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                // Similar conversion used in the listener for sensorSpinner.
                int diameter = Double.valueOf(((JSpinner) (e.getSource())).getValue().toString()).intValue();

                for (SensorPanel sensor : listOfSensors)
                {
                    int yAdjust = simPanel.getHeight()/2-sensor.getHeight()/2;
                    sensor.setBounds(sensor.getX(), yAdjust, diameter, diameter);
                    sensor.changeDiameter(diameter);
                }

                simPanel.repaint();
            }
        });
        cpanel.add(diameterSpinner, "height 10%, width 160px");

        JLabel sensorLabel = new JLabel("Sensors", SwingConstants.RIGHT);
        sensorLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(sensorLabel, "height 10%, width 160px");

        // Initialized here for use in sensorSpinner's listener
        JTextArea logOutput = new JTextArea();
        logOutput.setEditable(false);

        JSpinner sensorSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        sensorSpinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                // I apologize in advance for how ugly this conversion is.
                // It converts the source to a JSpinner, converts the value of the JSpinner to a string,
                // then to a double then to an integer.
                int numOfSensors = Double.valueOf(((JSpinner) (e.getSource())).getValue().toString()).intValue();
                if (numOfSensors < listOfSensors.size())
                {
                    int actualNumOfSensors = listOfSensors.size();
                    for (int i = 0; i < actualNumOfSensors-numOfSensors; i++)
                    {
                        simPanel.remove(listOfSensors.remove(listOfSensors.size()-1));
                    }
                }
                else if (numOfSensors > listOfSensors.size())
                {
                    int actualNumOfSensors = listOfSensors.size();
                    Random rand = new Random();
                    for (int i = 0; i < numOfSensors-actualNumOfSensors; i++)
                    {
                        addSensor(Double.valueOf(diameterSpinner.getValue().toString()).intValue());
                    }
                }

                simPanel.repaint();
            }
        });
        cpanel.add(sensorSpinner, "height 10%, width 160px, wrap");

        JLabel heuristicLabel = new JLabel("Heuristic", SwingConstants.RIGHT);
        heuristicLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(heuristicLabel, "height 10%, grow, span 2");

        JComboBox heuristicCombo = new JComboBox(new Object[]{"Simple Coverage", "Rigid Routing"});
        cpanel.add(heuristicCombo, "height 10%, grow, span 2, wrap");

        JButton startButton = new JButton("Simulate");
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                completeHeuristic(logOutput, (Double) diameterSpinner.getValue(), heuristicCombo.getSelectedIndex());
            }
        });
        cpanel.add(startButton, "height 10%, grow, span 4, wrap");

        simPanel.addComponentListener(new ComponentListener()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                int vertCenter = 0;
                for (SensorPanel sensor : listOfSensors)
                {
                    vertCenter = simPanel.getHeight()/2-sensor.getHeight()/2;
                    sensor.setBounds(sensor.getX(), vertCenter, sensor.getWidth(), sensor.getHeight());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
        cpanel.add(simPanel, "height 80%, grow, span 4, wrap");

        JScrollPane logScroll = new JScrollPane(logOutput);
        cpanel.add(logScroll, "dock east, width 250px, height 100%");

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        // Sensors must be added after all sizes are calculated.
        // Add one sensor since the JSpinner for sensors will default at 1.
        addSensor(Double.valueOf(diameterSpinner.getValue().toString()).intValue());
    }

    // heuristicID is based on the order of the comboBox
    // 0: Simple Coverage
    // 1: Rigid Routing
    private void completeHeuristic(JTextArea logOutput, double rawDiameter, int heuristicID)
    {
        // Check if heuristicID is valid
        if (heuristicID < 0 || heuristicID > 2)
        {
            return;
        }

        // Used to create a usable scaled value from the UI value
        double UIModifier = 500;
        double diameter = rawDiameter/UIModifier;

        ArrayList<Double> vertices = new ArrayList();
        for (SensorPanel sensor : listOfSensors)
        {
            // Gets XLoc inside of actual domain [0, 1]
            vertices.add(sensor.getXLoc());
            addLogText(logOutput, "Sensor at: " + sensor.getXLoc());
        }

        addLogText(logOutput, "Diameter: " + diameter);
        addLogText(logOutput, "Sensors:");
        for (int i = 0; i < vertices.size(); i++)
        {
            addLogText(logOutput, "[" + i + "]: " + vertices.get(i));
        }
        addLogText(logOutput, "-----------");

        ArrayList<Double> newVertices = new ArrayList();
        double result = 0;

        switch (heuristicID)
        {
            // Simple Coverage
            case 0:
                SimpleCoverage sc = new SimpleCoverage(vertices, diameter);
                sc.rGreaterL();

                newVertices.addAll(sc.getDoubleVertices());
                result = sc.getSumDoubleDistance();

                break;

            // Rigid Routing
            case 1:
                RigidRouting rr = new RigidRouting(vertices, diameter);

                newVertices.addAll(rr.rigidRouting());
                result = rr.getTotalDistanceMoved();

                break;

            case 2:

        }

        if (newVertices.size() != listOfSensors.size())
        {
            System.out.println("Lists are different sizes!");
        }

        for (int i = 0; i < listOfSensors.size(); i++)
        {
            moveSensor(listOfSensors.get(i), newVertices.get(i));
        }

        addLogText(logOutput, "After movement:");
        for (int i = 0; i < newVertices.size(); i++)
        {
            addLogText(logOutput, "[" + i + "]: " + newVertices.get(i));
        }
        addLogText(logOutput, "-----------");

        addLogText(logOutput, "Result: " + result);
    }

    private void addSensor(int diameter)
    {
        Random rand = new Random();

        SensorPanel sensor = new SensorPanel(diameter);
        listOfSensors.add(sensor);
        simPanel.add(sensor);

        Dimension size = sensor.getPreferredSize();

        int vertCenter = simPanel.getHeight()/2-size.height/2;
        // Only 10 options in the real domain [0, 1]
        // Visual domain is [0, ~500)
        int x = rand.nextInt(10) + 1; // [1, 10]
        // Sets the actual x location based on the actual domain
        sensor.setXLoc(x/10.0d);
        // Multiply the x option so that it can cover entire visual domain
        sensor.setBounds(x*50, vertCenter, size.width, size.height);
    }

    private void moveSensor(SensorPanel sensor, double xLoc)
    {
        int vertCenter = simPanel.getHeight()/2-sensor.getHeight()/2;
        System.out.println("Old x: " + sensor.getXLoc());
        System.out.println("New x: " + xLoc);
        System.out.println("New Visual X: " + (((Double) (xLoc * 500.0d)).intValue()));
        System.out.println("------------------");
        sensor.setXLoc(xLoc);
        sensor.setBounds(((Double) (xLoc * 500.0d)).intValue(), vertCenter, sensor.getWidth(), sensor.getHeight());
    }

    private void addLogText(JTextArea log, String str)
    {
        String newLine = (log.getText().isEmpty()) ? "" : "\n";
        log.setText(log.getText() + newLine + str);
    }

    private ArrayList<SensorPanel> listOfSensors;

    private JPanel simPanel;
}
