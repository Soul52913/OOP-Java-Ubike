package ubikesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class provides the functionality to manage the maintenance of the bikes.
 * The staff can check the status of a bike, check cross-region bikes, and manage bikes at a station.
 * The staff can also go back to the main maintenance panel.
 */

public class MaintenanceService {
    public BikeSharingSystem system;
    public BikeStatusService bikeStatusService;
    public BikeAreaCheckService bikeAreaCheckService;
    public AddRemoveService addRemoveService;

    public MaintenanceService() {
        this.system = null;
        this.bikeStatusService = new BikeStatusService();
        this.bikeAreaCheckService = new BikeAreaCheckService();
        this.addRemoveService = new AddRemoveService();
    }

    public void showMaintenancePanel(JFrame frame, BikeSharingSystem system) {
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridLayout(5, 1));
        this.system = system;
        MaintenanceService maintenanceService = this;


        JButton checkModifyBikeButton = new JButton("Check/Modify Bike Status");
        JButton checkCrossRegionBikesButton = new JButton("Check Cross-Region Bikes");
        JButton manageBikesAtStationButton = new JButton("Manage Bikes at Station");
        JButton returnButton = new JButton("Return");

        maintenancePanel.add(checkModifyBikeButton);
        maintenancePanel.add(checkCrossRegionBikesButton);
        maintenancePanel.add(manageBikesAtStationButton);
        maintenancePanel.add(returnButton);

        frame.getContentPane().removeAll();
        frame.add(maintenancePanel);
        frame.revalidate();
        frame.repaint();

        checkModifyBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bikeStatusService.showStatusPanel(frame, maintenanceService);
            }
        });

        checkCrossRegionBikesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bikeAreaCheckService.showDifferenetAreaPanel(frame, maintenanceService);
            }
        });

        manageBikesAtStationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRemoveService.showStaffPanel(frame, maintenanceService);
            }
        });

        BikeSharingSystem returnsystem = this.system;
        
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnsystem.returnBack(frame);
            }
        });
    }
}