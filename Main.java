import javax.swing.*;

import ubikesystem.BikeSharingSystem;

/**
 * This class provides the main method to run the Bike Sharing System.
 */

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BikeSharingSystem().createAndShowGUI();
            }
        });
    }
}
