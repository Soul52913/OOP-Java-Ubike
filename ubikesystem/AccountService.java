package ubikesystem;

import group.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * This class represents the account service in the bike sharing system.
 * It is responsible for user registration, login, and password change.
 */
public class AccountService {
    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ubike";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    private UserService userService = null;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    private User login(String phoneNumber, String password) {
        String query = "SELECT * FROM users WHERE phone_number = ? AND password = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getString("phone_number"), resultSet.getString("password"),
                                    resultSet.getString("id_number"), resultSet.getString("email"),
                                    resultSet.getString("card_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User register(String phoneNumber, String password, String idNumber, String email, String cardNumber) {
        if (isUserExist(phoneNumber, idNumber, email, cardNumber)) {
            return null; // User already exists
        }

        String query = "INSERT INTO users (phone_number, password, id_number, email, card_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, idNumber);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, cardNumber);

            preparedStatement.executeUpdate();
            CardService cardService = new CardService();
            if(!cardService.addCard(cardNumber)) {
                return null; // Card creation failed
            } // Add a card for the new user
            // Return the newly registered user
            return new User(phoneNumber, password, idNumber, email, cardNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Registration failed
        }
    }

    private boolean isUserExist(String phoneNumber, String idNumber, String email, String cardNumber) {
        String query = "SELECT * FROM users WHERE phone_number = ? OR id_number = ? OR email = ? OR card_number = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, idNumber);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, cardNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Return true if any user already exists with the given details
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, assume user exists to avoid potential duplicates
        }

    }

    public void showChangePasswordPanel(JFrame frame, User user, UserService userService) {
        JPanel changePasswordPanel = new JPanel();
        changePasswordPanel.setLayout(new GridLayout(5, 2));

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        JPasswordField currentPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton changePasswordButton = new JButton("Change Password");
        JButton backButton = new JButton("Back");

        changePasswordPanel.add(currentPasswordLabel);
        changePasswordPanel.add(currentPasswordField);
        changePasswordPanel.add(newPasswordLabel);
        changePasswordPanel.add(newPasswordField);
        changePasswordPanel.add(confirmPasswordLabel);
        changePasswordPanel.add(confirmPasswordField);
        changePasswordPanel.add(changePasswordButton);
        changePasswordPanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(changePasswordPanel);
        frame.revalidate();
        frame.repaint();

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPassword = new String(currentPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (validateCurrentPassword(user, currentPassword)) {
                    if (newPassword.equals(confirmPassword)) {
                        if (changePasswordInDatabase(user, newPassword)) {
                            JOptionPane.showMessageDialog(frame, "Password changed successfully");
                            user.setPassword(newPassword);
                            userService.showUserPanel(frame, user, userService.system);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Error updating password");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "New passwords do not match");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Current password is incorrect");
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

    private boolean validateCurrentPassword(User user, String currentPassword) {
        if (user.getPassword() != null) {
            if (currentPassword.equals(user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private boolean changePasswordInDatabase(User user, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE phone_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, user.getPhoneNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, assume password update failed
        }
    }

    public void showLoginPanel(JFrame frame, UserService userService) {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));
        this.userService = userService;

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JTextField phoneNumberField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton switchToRegisterButton = new JButton("Register");
        JButton returnButton = new JButton("Return User Menu");

        loginPanel.add(phoneNumberLabel);
        loginPanel.add(phoneNumberField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(switchToRegisterButton);
        loginPanel.add(new JLabel());
        loginPanel.add(returnButton);

        frame.getContentPane().removeAll();
        frame.add(loginPanel);
        frame.revalidate();
        frame.repaint();
        

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                String password = new String(passwordField.getPassword());
                User user = login(phoneNumber, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(frame, "Login successful");
                    userService.showUserPanel(frame, user, userService.system);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid phone number or password");
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, null, userService.system);
            }
        });

        switchToRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterPanel(frame);
            }
        });
    }

    public void showRegisterPanel(JFrame frame) {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(6, 2));

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JTextField phoneNumberField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel idNumberLabel = new JLabel("ID Number:");
        JTextField idNumberField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel cardNumberLabel = new JLabel("Card Number:");
        JTextField cardNumberField = new JTextField();
        JButton registerButton = new JButton("Register");
        JButton returnButton = new JButton("Return Login");

        registerPanel.add(phoneNumberLabel);
        registerPanel.add(phoneNumberField);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordField);
        registerPanel.add(idNumberLabel);
        registerPanel.add(idNumberField);
        registerPanel.add(emailLabel);
        registerPanel.add(emailField);
        registerPanel.add(cardNumberLabel);
        registerPanel.add(cardNumberField);
        registerPanel.add(returnButton);
        registerPanel.add(registerButton);

        frame.getContentPane().removeAll();
        frame.add(registerPanel);
        frame.revalidate();
        frame.repaint();

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                String password = new String(passwordField.getPassword());
                String idNumber = idNumberField.getText();
                String email = emailField.getText();
                String cardNumber = cardNumberField.getText();
                register(phoneNumber, password, idNumber, email, cardNumber);
                JOptionPane.showMessageDialog(frame, "Registration successful");
                showLoginPanel(frame, userService);
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPanel(frame, userService);
            }
        });
    }

}
