package ubikesystem;

import javax.swing.*;

import object.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // Add this import statement

/**
 * This class provides the functionality to check bikes in different areas.
 * It is used by the maintenance staff to check if there are bikes in the wrong area.
 */

public class BikeAreaCheckService {

    public void showDifferenetAreaPanel(JFrame frame, MaintenanceService maintenanceService) {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout());

        StationService stationService = new StationService();
        List<Bike> bikes = stationService.getBikesWithDifferentArea();

        String[] columnNames = {"Bike ID", "Bike Type", "areaName", "Status"};
        Object[][] data = new Object[bikes.size()][4];
        for (int i = 0; i < bikes.size(); i++) {
            Bike bike = bikes.get(i);
            data[i][0] = bike.getBikeId();
            data[i][1] = bike.getBikeType();
            data[i][2] = bike.getArea().getChineseName();
            data[i][3] = bike.getStatus().toString();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton backButton = new JButton("Back");
        statusPanel.add(scrollPane, BorderLayout.CENTER);
        statusPanel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(statusPanel);
        frame.revalidate();
        frame.repaint();


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to return
                maintenanceService.showMaintenancePanel(frame, maintenanceService.system);
            }
        });
    }
}