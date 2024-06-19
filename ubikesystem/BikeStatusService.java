package ubikesystem;

import javax.swing.*;

import object.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class provides the functionality to check the status of a bike and change the status of a bike.
 * The staff can check the status of a bike and change the status of a bike.
 */

public class BikeStatusService{
    
    public String showBikeStatus(String bikeID) {
        // Assume stations is a List<Station> containing all stations
        StationService stationService = new StationService();
        Bike bike = stationService.getBikeForPillar(bikeID);
        if (bike != null) {
            return bike.getStatus().toString();
        }
        else {
            return null;
        }
    }

    public Bike setBikeStatus(String bikeID, int status) {
        // Assume stations is a List<Station> containing all stations
        StationService stationService = new StationService();
        Bike bike = stationService.getBikeForPillar(bikeID);
        if (bike != null) {
            bike.setStatus(BikeStatus.fromStatusCode(status));
            return bike;
        }
        else {
            return null;
        }
    }

    public void showStatusPanel(JFrame frame, MaintenanceService maintenanceService) {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout());

        JTextField bikeIdField = new JTextField(10);
        JTextField bikeStatusField = new JTextField(10);
        JButton showStatusButton = new JButton("Show Status");
        JButton changeStatusButton = new JButton("Change Status");
        JButton returnButton = new JButton("Return");

        statusPanel.add(new JLabel("Bike ID:"));
        statusPanel.add(bikeIdField);
        statusPanel.add(new JLabel("Bike Status:"));
        statusPanel.add(bikeStatusField);
        statusPanel.add(showStatusButton);
        statusPanel.add(changeStatusButton);
        statusPanel.add(returnButton);

        frame.getContentPane().removeAll();
        frame.add(statusPanel);
        frame.revalidate();
        frame.repaint();

        showStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to show status
                String bikeId = bikeIdField.getText();
                if (bikeId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bike ID cannot be empty.");
                    return;
                }
                String status = showBikeStatus(bikeId);
                if (status != null) {
                    bikeStatusField.setText(status);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong Bike ID.");
                }
            }
        });

        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bikeId = bikeIdField.getText();
                if (bikeId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bike ID cannot be empty.");
                    return;
                }
                String status = bikeStatusField.getText();
                StationService stationService = new StationService();
                try {
                    int statusInt = Integer.parseInt(status);
                    if (statusInt < 0 || statusInt > 3) {
                        throw new NumberFormatException();
                    }
                    // If the status is a valid integer between 0 and 3, you can set the bike status
                    Bike bike = setBikeStatus(bikeId, statusInt);
                    if (bike != null) {
                        stationService.updateBikeInDatabase(bike);
                        JOptionPane.showMessageDialog(null, "Bike status updated successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Bike not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid status. Please enter an integer value corresponding to the status:\n" +
                            "NON_OPERATIONAL(0), OPERATIONAL(1), RENTED(2), LOST(3)");
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to return
                maintenanceService.showMaintenancePanel(frame, maintenanceService.system);
            }
        });
    }
}