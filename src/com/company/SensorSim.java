package com.company;

import javax.swing.*;

public class SensorSim
{
    private SensorSim()
    {
        DisplayManager dm = new DisplayManager();
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
