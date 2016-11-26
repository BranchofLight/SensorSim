package com.company;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SensorPanel extends JPanel
{
    public SensorPanel(int diameter)
    {
        this.setPreferredSize(new Dimension(diameter+1, diameter+1)); // Added buffer so circle fits
        this.setBackground(new Color(0, 0, 0, 0));
        this.diameter = diameter;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.RED);
        g.drawOval(0, 0, diameter, diameter);
    }

    public void changeDiameter(int newDiameter)
    {
        this.setSize(new Dimension(newDiameter+1, newDiameter+1));
        this.diameter = newDiameter;
    }

    public int getDiameter() { return diameter; }

    private int diameter;
}
