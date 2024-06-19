package ubikesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import group.*;

/**
 * This class provides the functionality to rent and borrow bikes.
 * The user can borrow a bike from a station and return a bike to a station.
 * The user can also go back to the user panel.
 */

public class RentBorrowService{
    // Method to show borrow bike panel
    
    public void showBorrowBikePanel(JFrame frame, User user, UserService userService) {
        MaskFormatter maskFormatter = null;
        try {
            maskFormatter = new MaskFormatter("####-##-## ##:##:##");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JPanel borrowBikePanel = new JPanel();
        borrowBikePanel.setLayout(new GridLayout(4, 2));
        
        JLabel stationNameLabel = new JLabel("Station Name:");
        JTextField stationNameField = new JTextField();
        JLabel startTimeLabel = new JLabel("Start Time:");
        JFormattedTextField startTimeField = new JFormattedTextField(maskFormatter);
        JButton borrowBikeButton = new JButton("Borrow Bike");
        JButton backButton = new JButton("Back");

        borrowBikePanel.add(stationNameLabel);
        borrowBikePanel.add(stationNameField);
        borrowBikePanel.add(startTimeLabel);
        borrowBikePanel.add(startTimeField);
        borrowBikePanel.add(borrowBikeButton);
        borrowBikePanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(borrowBikePanel);
        frame.revalidate();
        frame.repaint();
        StationService service = new StationService();
        
        borrowBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new RentalService().notYetReturned(user)) {
                    JOptionPane.showMessageDialog(frame, "Please return the bike first.");
                    userService.showUserPanel(frame, user, userService.system);
                    return;
                }
                String stationName = stationNameField.getText().trim();
                String startTimeString = startTimeField.getText().trim();
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date startTime = null;
                try {
                    startTime = formatter.parse(startTimeString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Invalid date-time format. Please enter in yyyy/MM/dd HH:mm:ss format.");
                    return; // Exit the method if parsing fails
                }
                int result = service.borrowBike(stationName, user, startTime);

                if (result == 1) {
                    JOptionPane.showMessageDialog(frame, "Bike borrowed successfully.");
                    userService.showUserPanel(frame, user, userService.system);
                } else if (result == 0) {
                    JOptionPane.showMessageDialog(frame, "No avaliable bike.");
                } else if (result == 2) {
                    JOptionPane.showMessageDialog(frame, "Station not found.");
                } else {
                    JOptionPane.showMessageDialog(frame, "No Card balance.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, user, userService.system);
            }
        });
    }

    // Method to show return bike panel
    public void showReturnBikePanel(JFrame frame, User user, UserService userService) {
        MaskFormatter maskFormatter = null;
        try {
            maskFormatter = new MaskFormatter("####-##-## ##:##:##");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JPanel returnBikePanel = new JPanel();
        returnBikePanel.setLayout(new GridLayout(4, 2));

        JLabel stationIDLabel = new JLabel("Station ID:");
        JTextField stationIDField = new JTextField();
        JLabel endTimeLabel = new JLabel("End Time:");
        JFormattedTextField endTimeField = new JFormattedTextField(maskFormatter);
        JButton returnBikeButton = new JButton("Return Bike");
        JButton backButton = new JButton("Back");

        returnBikePanel.add(stationIDLabel);
        returnBikePanel.add(stationIDField);
        returnBikePanel.add(endTimeLabel);
        returnBikePanel.add(endTimeField);
        returnBikePanel.add(returnBikeButton);
        returnBikePanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(returnBikePanel);
        frame.revalidate();
        frame.repaint();
        StationService service = new StationService();

        returnBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!new RentalService().notYetReturned(user)) {
                    JOptionPane.showMessageDialog(frame, "Please borrow a bike first.");
                    userService.showUserPanel(frame, user, userService.system);
                    return;
                }
                String stationID = stationIDField.getText().trim();
                String endTimeString = endTimeField.getText().trim();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date endTime = null;
                try {
                    endTime = formatter.parse(endTimeString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Invalid date-time format. Please enter in yyyy/MM/dd HH:mm:ss format.");
                    return;
                }

                int result = service.returnBike(stationID, user, endTime);
                if (result == 1) {
                    JOptionPane.showMessageDialog(frame, "Bike returned successfully.");
                    userService.showUserPanel(frame, user, userService.system);
                } else if (result == 2) {
                    JOptionPane.showMessageDialog(frame, "Station not found.");
                } else if (result == 3) {
                    JOptionPane.showMessageDialog(frame, "Not avaliable return Time.");
                    userService.showUserPanel(frame, user, userService.system);
                } else if (result == 4) {
                    JOptionPane.showMessageDialog(frame, "Not yet borrow a bike.");
                    userService.showUserPanel(frame, user, userService.system);
                } else {
                    JOptionPane.showMessageDialog(frame, "No available slots or other error.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, user, userService.system);
            }
        });
    }
}