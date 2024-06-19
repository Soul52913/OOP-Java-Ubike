package ubikesystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class provides the functionality to add and remove bikes from a station.
 * The staff can add a bike to a station and remove a bike from a station.
 */

public class BikeSharingSystem {
    public UserService userService;
    public MaintenanceService maintenanceService;

    public BikeSharingSystem() {
        this.userService = new UserService();
        this.maintenanceService = new MaintenanceService();
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Bike Sharing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton userButton = new JButton("User");
        JButton maintenanceButton = new JButton("Maintenance Staff");

        mainPanel.add(userButton);
        mainPanel.add(maintenanceButton);

        frame.add(mainPanel);

        BikeSharingSystem returnBack = this;

        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, null, returnBack);
            }
        });
        
        maintenanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                maintenanceService.showMaintenancePanel(frame, returnBack);
            }
        });

        frame.setVisible(true);
    }

    public void returnBack(JFrame frame) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton userButton = new JButton("User");
        JButton maintenanceButton = new JButton("Maintenance Staff");

        mainPanel.add(userButton);
        mainPanel.add(maintenanceButton);

        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();

        BikeSharingSystem returnBack = this;

        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, null, returnBack);
            }
        });
        
        maintenanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                maintenanceService.showMaintenancePanel(frame, returnBack);
            }
        });

    }

}
