package com.company;

import net.miginfocom.swing.MigLayout;
import sun.management.Sensor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Random;

public class ContentPanel extends JFrame
{
    public ContentPanel(int width, int height)
    {
        this.setLayout(new MigLayout());
        this.setSize(new Dimension(width, height));
        this.setLocationRelativeTo(null);
        this.setTitle("Sensor Sim");

        JPanel simPanel = new JPanel(null);
        listOfSensors = new ArrayList<>();

        JPanel cpanel = new JPanel(new MigLayout("fillx"));
        this.add(cpanel, "width 100%, height 100%");

        JLabel radiusLabel = new JLabel("Radius", SwingConstants.RIGHT);
        radiusLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(radiusLabel, "height 10%, grow, width 18%");

        JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(10.0, 0.1, 200.0, 5.0));
        radiusSpinner.addChangeListener(new ChangeListener()
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
        cpanel.add(radiusSpinner, "height 10%, grow, width 18%");

        JLabel sensorLabel = new JLabel("Sensors", SwingConstants.RIGHT);
        sensorLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(sensorLabel, "height 10%, grow, width 18%");

        // Initialized here for use in sensorSpinner's listener
        JTextArea logOutput = new JTextArea();
        logOutput.setEditable(false);

        JSpinner sensorSpinner = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 100.0, 1.0));
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
                        addLogText(logOutput, "Removed sensor: [" + listOfSensors.size() + "]");
                    }
                }
                else if (numOfSensors > listOfSensors.size())
                {
                    int actualNumOfSensors = listOfSensors.size();
                    Random rand = new Random();
                    for (int i = 0; i < numOfSensors-actualNumOfSensors; i++)
                    {
                        addSensor(simPanel, Double.valueOf(radiusSpinner.getValue().toString()).intValue());
                        addLogText(logOutput, "Added sensor: [" + listOfSensors.size() + "]");
                    }
                }

                simPanel.repaint();
            }
        });
        cpanel.add(sensorSpinner, "height 10%, grow, width 18%, wrap");

        JLabel heuristicLabel = new JLabel("Heuristic", SwingConstants.RIGHT);
        heuristicLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        cpanel.add(heuristicLabel, "height 10%, grow, width 18%");

        JComboBox heuristicCombo = new JComboBox();
        cpanel.add(heuristicCombo, "height 10%, span 3, grow, wrap");

        JButton startButton = new JButton("Simulate");
        cpanel.add(startButton, "height 10%, spanx, grow, wrap");

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
        cpanel.add(simPanel, "spanx, height 80%, grow, wrap");

        JScrollPane logScroll = new JScrollPane(logOutput);
        cpanel.add(logScroll, "dock east, width 25%, height 100%");

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        // Sensors must be added after all sizes are calculated.
        // Add one sensor since the JSpinner for sensors will default at 1.
        addSensor(simPanel, Double.valueOf(radiusSpinner.getValue().toString()).intValue());
    }

    private void addSensor(JPanel simPanel, int diameter)
    {
        Random rand = new Random();

        SensorPanel sensor = new SensorPanel(diameter);
        listOfSensors.add(sensor);
        simPanel.add(sensor);

        Dimension size = sensor.getPreferredSize();

        int vertCenter = simPanel.getHeight()/2-size.height/2;
        int x = rand.nextInt(simPanel.getWidth()-(sensor.getDiameter()+1));
        sensor.setBounds(x, vertCenter, size.width, size.height);
    }

    private void addLogText(JTextArea log, String str)
    {
        String newLine = (log.getText().isEmpty()) ? "" : "\n";
        log.setText(log.getText() + newLine + str);
    }

    private ArrayList<SensorPanel> listOfSensors;
}
