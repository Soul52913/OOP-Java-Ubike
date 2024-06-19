package ubikesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import object.Bike;
import object.Pillar;

/**
 * This class provides the functionality to call maintenance for a pillar or a bike.
 * The staff can create a maintenance report for a pillar or a bike.
 */

public class CallMaintenance{
    private UserService userService;

    public CallMaintenance() {
        this.userService = null;
    }

    public void showCallPanel(JFrame frame, UserService userService) {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));
        this.userService = userService;
    
        JButton pillarButton = new JButton("Pillar");
        JButton bikeButton = new JButton("Bike");
        JButton returnButton = new JButton("Return");
    
        loginPanel.add(pillarButton);
        loginPanel.add(bikeButton);
        loginPanel.add(new JLabel());
        loginPanel.add(returnButton);
    
        frame.getContentPane().removeAll();
        frame.add(loginPanel);
        frame.revalidate();
        frame.repaint();
    
        pillarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action for Pillar button here
                showMaintenancePanel(frame, "Pillar");
            }
        });
    
        bikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action for Bike button here
                showMaintenancePanel(frame, "Bike");
            }
        });
    
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, null, userService.system);
            }
        });
    }

    public void showMaintenancePanel(JFrame frame, String type) {
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridLayout(6, 2));
    
        JLabel idLabel = new JLabel(type + " ID:");
        JTextField idField = new JTextField();
        JLabel maintenanceLabel = new JLabel("Maintenance Info:");
        JTextArea maintenanceArea = new JTextArea();
    
        JButton createButton = new JButton("Create Maintenance");
        JButton showButton = new JButton("Show Maintenance");
        JButton returnButton = new JButton("Return Last Page");
    
        maintenancePanel.add(idLabel);
        maintenancePanel.add(idField);
        maintenancePanel.add(maintenanceLabel);
        maintenancePanel.add(maintenanceArea);
        maintenancePanel.add(createButton);
        maintenancePanel.add(showButton);
        maintenancePanel.add(new JLabel());
        maintenancePanel.add(returnButton);
    
        frame.getContentPane().removeAll();
        frame.add(maintenancePanel);
        frame.revalidate();
        frame.repaint();
    
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action for Create Maintenance button here
                StationService stationService = new StationService();
                if (type == "Pillar"){
                    Pillar pillar = stationService.getPillar(idField.getText());
                    if (pillar != null){
                        pillar.setMaintenanceReport(maintenanceArea.getText());
                        JOptionPane.showMessageDialog(frame, "Maintenance report created successfully.");
                        stationService.updatePillarInDatabase(pillar);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Pillar not found.");
                    }
                } else {
                    Bike bike = stationService.getBikeForPillar(idField.getText());
                    if (bike != null){
                        bike.setMaintenanceReport(maintenanceArea.getText());
                        JOptionPane.showMessageDialog(frame, "Maintenance report created successfully.");
                        stationService.updateBikeInDatabase(bike);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Bike not found.");
                    }
                }
            }
        });
    
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action for Show Maintenance button here
                StationService stationService = new StationService();
                if (type == "Pillar"){
                    Pillar pillar = stationService.getPillar(idField.getText());
                    if (pillar != null){
                        maintenanceArea.setText(pillar.getMaintenanceReport());
                        JOptionPane.showMessageDialog(frame, "Maintenance report shown successfully.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Pillar not found.");
                    }
                } else {
                    Bike bike = stationService.getBikeForPillar(idField.getText());
                    if (bike != null){
                        maintenanceArea.setText(bike.getMaintenanceReport());
                        JOptionPane.showMessageDialog(frame, "Maintenance report shown successfully.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Bike not found.");
                    }
                }
            }
        });
    
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your action for Return Last Page button here
                showCallPanel(frame, userService);
            }
        });
    }
}
