package ubikesystem;

import group.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class provides the functionality for the user.
 * The user can also find a station, search for rent records, add credit to a card, borrow a bike, return a bike, and call maintenance.
 */

public class UserService {
    public AccountService accountService;
    public BikeSharingSystem system;
    public StationService stationService;
    public RentalService rentalService;
    public RentBorrowService rentBorrowService;
    public CallMaintenance callMaintenance;
    public CardService cardService;

    public User user;

    public UserService() {
        this.user = null;
        this.system = null;
        this.accountService = new AccountService();
        this.stationService = new StationService();
        this.rentalService = new RentalService();
        this.rentBorrowService = new RentBorrowService();
        this.callMaintenance = new CallMaintenance();
        this.cardService = new CardService();
    }

    public void showUserPanel(JFrame frame, User user, BikeSharingSystem system) {
        this.user = user;
        this.system = system;
        
        UserService userService = this;


        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(8, 1));

        if (this.user == null) {
            JButton loginRegisterButton = new JButton("Login/Register");
            userPanel.add(loginRegisterButton);
            loginRegisterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accountService.showLoginPanel(frame, userService);
                }
            });
    
        }
        else {
            JButton changePasswordButton = new JButton("Change My password");
            userPanel.add(changePasswordButton);

            User changedUser = this.user;

            changePasswordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accountService.showChangePasswordPanel(frame, changedUser, userService);
                }
            });
        }
        JButton findStationButton = new JButton("Find Station");
        JButton rentRecordSearchButton = new JButton("Rent Record Search");
        JButton addCardCreditButton = new JButton("Add Card's Credit");
        JButton borrowBikeButton = new JButton("Borrow Bike");
        JButton returnBikeButton = new JButton("Return Bike");
        JButton callMaintenanceButton = new JButton("Call Maintenance");
        JButton returnHomeButton = new JButton("Return to Homepage");

        userPanel.add(findStationButton);
        userPanel.add(rentRecordSearchButton);
        userPanel.add(addCardCreditButton);
        userPanel.add(borrowBikeButton);
        userPanel.add(returnBikeButton);
        userPanel.add(callMaintenanceButton);
        userPanel.add(returnHomeButton);

        frame.getContentPane().removeAll();
        frame.add(userPanel);
        frame.revalidate();
        frame.repaint();

        // Add ActionListeners for each button and implement the required functionalities

        
        User userOpearion = this.user;
        findStationButton.addActionListener(new ActionListener() {
            @Override
            
            public void actionPerformed(ActionEvent e) {
                if (userOpearion == null) {
                    JOptionPane.showMessageDialog(frame, "Please login first.");
                    return;
                }
                stationService.showFindStationPanel(frame, userOpearion, userService);
            }
        });

        rentRecordSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOpearion == null) {
                    JOptionPane.showMessageDialog(frame, "Please login first.");
                    return;
                }
                rentalService.showRecords(frame, userOpearion, userService);
            }
        });

        addCardCreditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOpearion == null) {
                    JOptionPane.showMessageDialog(frame, "Please login first.");
                    return;
                }
                cardService.showAddMoneyPanel(frame, userOpearion, userService);
            }
        });

        borrowBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOpearion == null) {
                    JOptionPane.showMessageDialog(frame, "Please login first.");
                    return;
                }
                if (new RentalService().notYetReturned(user)) {
                    JOptionPane.showMessageDialog(frame, "Please return the bike first.");
                    return;
                }
                rentBorrowService.showBorrowBikePanel(frame, userOpearion, userService);
            }
        });

        returnBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOpearion == null) {
                    JOptionPane.showMessageDialog(frame, "Please login first.");
                    return;
                }
                if (!new RentalService().notYetReturned(user)) {
                    JOptionPane.showMessageDialog(frame, "Please borrow a bike first.");
                    return;
                }
                rentBorrowService.showReturnBikePanel(frame, userOpearion, userService);
            }
        });

        callMaintenanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userOpearion == null) {
                    JOptionPane.showMessageDialog(frame, "Please login first.");
                    return;
                }
                callMaintenance.showCallPanel(frame, userService);
            }
        });

        BikeSharingSystem returnsystem = this.system;

        returnHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Return to homepage
                returnsystem.returnBack(frame);
            }
        });
    }

}
