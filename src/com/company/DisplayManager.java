package com.company;

import javax.swing.*;

// Manages any metadata for the display such as the UI style to use
public class DisplayManager
{
    public DisplayManager()
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

        ContentPanel cpanel = new ContentPanel();
    }
}
