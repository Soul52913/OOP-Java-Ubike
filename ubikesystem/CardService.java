package ubikesystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import group.User;

/**
 * This class provides the functionality to add money to a card, check the card balance, and view recent transactions.
 * The user can add money to their card, check their card balance, and view recent transactions.
 * The user can also go back to the user panel.
 */

public class CardService {
    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ubike";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public Boolean addCard(String cardId) {
        String query = "INSERT INTO cards(card_id, amount) VALUES (?, 0)";
    
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cardId);
            int rowsInserted = preparedStatement.executeUpdate(); // Execute the SQL statement
    
            return rowsInserted > 0; // Return true if insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, assume insert failed
        }
    }

    public boolean addAmount(String cardId, double amount) {
        String updateQuery = "UPDATE cards SET amount = amount + ? WHERE card_id = ?";
        String insertQuery = "INSERT INTO transactions(card_id, transaction_type, transaction_amount, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, cardId);

            insertStmt.setString(1, cardId);
            insertStmt.setString(2, "increment");
            insertStmt.setDouble(3, amount);
            insertStmt.setTimestamp(4, new Timestamp(new Date().getTime()));

            int rowsAffected = updateStmt.executeUpdate();
            insertStmt.executeUpdate();

            return rowsAffected == 1; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, assume update failed
        }
    }

    public boolean decreaseAmount(String cardId, double amount) {
        String updateQuery = "UPDATE cards SET amount = amount - ? WHERE card_id = ?";
        String insertQuery = "INSERT INTO transactions(card_id, transaction_type, transaction_amount, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, cardId);

            insertStmt.setString(1, cardId);
            insertStmt.setString(2, "decrement");
            insertStmt.setDouble(3, amount);
            insertStmt.setTimestamp(4, new Timestamp(new Date().getTime()));

            int rowsAffected = updateStmt.executeUpdate();
            insertStmt.executeUpdate();

            return rowsAffected == 1; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, assume update failed
        }
    }

    public double getCardAmount(String cardId) {
        String query = "SELECT amount FROM cards WHERE card_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("amount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if card not found or error occurred
    }

    public List<String> getRecentTransactions(String cardId) {
        String query = "SELECT transaction_type, transaction_amount, transaction_date FROM transactions WHERE card_id = ? ORDER BY transaction_date DESC LIMIT 10";
        List<String> transactions = new ArrayList<>();

        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String transactionType = resultSet.getString("transaction_type");
                    double transactionAmount = resultSet.getDouble("transaction_amount");
                    Timestamp transactionDate = resultSet.getTimestamp("transaction_date");

                    String transaction = "Type: " + transactionType +
                                        ", Amount: " + transactionAmount +
                                        ", Date: " + transactionDate.toString();
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public void showAddMoneyPanel(JFrame frame, User user, UserService userService) {
        JPanel addMoneyPanel = new JPanel();
        addMoneyPanel.setLayout(new GridLayout(4, 2));

        JLabel moneyLabel = new JLabel("Amount to Add:");
        JTextField moneyField = new JTextField();

        JLabel currentBalanceLabel = new JLabel("Current Balance:");
        JLabel balanceLabel = new JLabel(String.valueOf(getCardAmount(user.getCard())));

        JButton addMoneyButton = new JButton("Add Money");
        JButton backButton = new JButton("Back");

        addMoneyPanel.add(moneyLabel);
        addMoneyPanel.add(moneyField);
        addMoneyPanel.add(currentBalanceLabel);
        addMoneyPanel.add(balanceLabel);
        addMoneyPanel.add(addMoneyButton);
        addMoneyPanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(addMoneyPanel);
        frame.revalidate();
        frame.repaint();

        addMoneyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String moneyString = moneyField.getText();
                try {
                    double moneyToAdd = Double.parseDouble(moneyString);
                    if (addAmount(user.getCard(), moneyToAdd)) {
                        JOptionPane.showMessageDialog(frame, "Money added successfully");
                        userService.showUserPanel(frame, user, userService.system);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error adding money");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, user, userService.system);
            }
        });
    }
}