package com.company;

import javax.swing.*;

public class SensorSim
{
    private SensorSim()
    {
        DisplayManager dm = new DisplayManager(1100, 600);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                SensorSim app = new SensorSim();
            }
        });
    }
}
