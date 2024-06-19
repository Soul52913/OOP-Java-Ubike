package ubikesystem;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class provides the functionality to add and remove bikes from a station.
 * The staff can add a bike to a station and remove a bike from a station.
 * The staff can also go back to the maintenance panel.
 */

public class AddRemoveService {

    public void showStaffPanel(JFrame frame, MaintenanceService maintenanceService) {
        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(new GridLayout(5, 2));

        JLabel bikeIdLabel = new JLabel("Bike ID:");
        JTextField bikeIdField = new JTextField();
        JLabel stationNameLabel = new JLabel("Station ID:");
        JTextField stationNameField = new JTextField();
        JButton addBikeButton = new JButton("Add Bike to Pillar");
        JButton removeBikeButton = new JButton("Remove Bike from Pillar");
        JButton backButton = new JButton("Back");

        staffPanel.add(bikeIdLabel);
        staffPanel.add(bikeIdField);
        staffPanel.add(stationNameLabel);
        staffPanel.add(stationNameField);
        staffPanel.add(addBikeButton);
        staffPanel.add(removeBikeButton);
        staffPanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(staffPanel);
        frame.revalidate();
        frame.repaint();

        StationService service = new StationService();
        addBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bikeId = bikeIdField.getText();
                String stationName = stationNameField.getText();
                if (bikeId.isEmpty() || stationName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both bike ID and station ID.");
                    return;
                }
                int result = service.addBikeToStation(bikeId, stationName);
                if (result == 0) {
                    JOptionPane.showMessageDialog(frame, "Bike added successfully.");
                } else if (result == 1) {
                    JOptionPane.showMessageDialog(frame, "Bike is already in a station.");
                } else if (result == 2) {
                    JOptionPane.showMessageDialog(frame, "Unknown bike ID.");
                } else if (result == 3) {
                    JOptionPane.showMessageDialog(frame, "Station not found or no empty slots.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Unknown error.");
                }
            }
        });


        removeBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bikeId = bikeIdField.getText();
                String stationName = stationNameField.getText();
                if (bikeId.isEmpty() || stationName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both bike ID and station ID.");
                    return;
                }
                int result = service.removeBikeFromStation(bikeId, stationName);
                if (result == 0) {
                    JOptionPane.showMessageDialog(frame, "Bike removed successfully.");
                } else if (result == 1) {
                    JOptionPane.showMessageDialog(frame, "Bike is not in a station.");
                } else if (result == 2) {
                    JOptionPane.showMessageDialog(frame, "Unknown bike ID.");
                } else if (result == 3) {
                    JOptionPane.showMessageDialog(frame, "Station not found or no bikes.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Unknown error.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to go back to the previous panel
                maintenanceService.showMaintenancePanel(frame, maintenanceService.system);
            }
        });
    }
}