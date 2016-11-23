package com.company;

import javax.swing.*;

public class DisplayManager
{
    public DisplayManager(int width, int height)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
            {
                System.out.println(ex.toString());
            }

            System.out.println(e.toString());
        }

        ContentPanel cpanel = new ContentPanel(width, height);
    }
}
