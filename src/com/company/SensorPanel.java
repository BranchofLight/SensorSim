package com.company;

import javax.swing.*;
import java.awt.*;

// Contains the visual representation of the sensor
public class SensorPanel extends JPanel
{
    public SensorPanel(int diameter)
    {
        this.setPreferredSize(new Dimension(diameter+1, diameter+1)); // Added buffer so circle fits
        this.setBackground(new Color(0, 0, 0, 0));
        this.diameter = diameter;
    }

    /*
     * Overrides rules on drawing the panel adding the circle used for representing a sensor
     * @param {Graphics} g - the Graphics object used for drawing
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.RED);
        g.drawOval(0, 0, diameter, diameter);
    }

    /*
     * Changes the diameter variable as well as resizes the visual representation
     * @param {int} newDiameter - the replacement diameter
     */
    public void changeDiameter(int newDiameter)
    {
        this.setSize(new Dimension(newDiameter+1, newDiameter+1));
        this.diameter = newDiameter;
    }

    public void setXLoc(double xLoc) { this.xLoc = xLoc; }

    public double getXLoc() { return xLoc; }

    public int getDiameter() { return diameter; }

    private int diameter;
    private double xLoc;
}
