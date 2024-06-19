package ubikesystem;

import javax.swing.*;

import object.*;

import java.sql.*;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import group.User;

/**
 * This class provides the functionality to add a rental record, get a rental record, and update a rental record.
 * The user can add a rental record, get a rental record, and update a rental record.
 */

public class RentalService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ubike";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    private List<RentalRecord> getRecordRecently(User user) {
        List<RentalRecord> records = new ArrayList<>();
        String recordQuery = "SELECT recordID, id_number, start, startTime, end, endTime, bikeID, amount, cardID FROM records WHERE id_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement recordStmt = connection.prepareStatement(recordQuery)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            recordStmt.setString(1, user.getIdNumber());
            try (ResultSet recordRs = recordStmt.executeQuery()) {
                while (recordRs.next() && records.size() < 10) {
                    Place start = Place.fromChineseName(recordRs.getString("start"));
                    Date startTime = new Date();
                    try {
                        startTime = formatter.parse(recordRs.getString("startTime"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String endName = recordRs.getString("end");
                    Place end = endName.isEmpty() ? null : Place.fromChineseName(endName);
                    Date endTime = null;
                    String endTimeStr = recordRs.getString("endTime");
                    if (endTimeStr != null) {
                        try {
                            endTime = formatter.parse(endTimeStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    String bikeID = recordRs.getString("bikeID");
                    int amount = recordRs.getInt("amount");
                    String recordID = recordRs.getString("recordID");
                    String cardID = recordRs.getString("cardID");
                    
                    RentalRecord record = new RentalRecord(recordID, user, bikeID, startTime, start, endTime, end, amount, cardID);
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public void addRecord(User user, RentalRecord record) {
        String insertQuery = "INSERT INTO records (recordID, id_number, start, startTime, end, endTime, bikeID, amount, cardID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            insertStmt.setString(1, record.getRecordID());
            insertStmt.setString(2, user.getIdNumber());
            insertStmt.setString(3, record.getStartPlace().getChineseName());
            insertStmt.setString(4, formatter.format(record.getRentalStartTime()));
            insertStmt.setString(5, "");
            insertStmt.setString(6, null);
            insertStmt.setString(7, record.getBikeId());
            insertStmt.setInt(8, record.getRental());
            insertStmt.setString(9, record.getCardID());

            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RentalRecord getBorrowRecord(User user) {
        String recordQuery = "SELECT recordID, start, startTime, bikeID, amount, cardID FROM records WHERE id_number = ? AND endtime IS NULL AND end = ''";

        try (Connection connection = getConnection();
             PreparedStatement recordStmt = connection.prepareStatement(recordQuery)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            recordStmt.setString(1, user.getIdNumber());
            try (ResultSet recordRs = recordStmt.executeQuery()) {
                if (recordRs.next()) {
                    Place start = Place.fromChineseName(recordRs.getString("start"));
                    Date startTime = new Date();
                    try {
                        startTime = formatter.parse(recordRs.getString("startTime"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String bikeID = recordRs.getString("bikeID");
                    int amount = recordRs.getInt("amount");
                    String recordID = recordRs.getString("recordID");
                    String cardID = recordRs.getString("cardID");
                    
                    RentalRecord record = new RentalRecord(recordID, user, bikeID, startTime, start, null, null, amount, cardID);
                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean notYetReturned(User user) {
        if (getBorrowRecord(user) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public RentalRecord findRecord(User user) {
        String recordQuery = "SELECT recordID, id_number, start, startTime, end, endTime, bikeID, amount, cardID FROM records WHERE id_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement recordStmt = connection.prepareStatement(recordQuery)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
            recordStmt.setString(1, user.getIdNumber());
            try (ResultSet recordRs = recordStmt.executeQuery()) {
                if (recordRs.next()) {
                    Place start = Place.fromChineseName(recordRs.getString("start"));
                    Date startTime = new Date();
                    try {
                        startTime = formatter.parse(recordRs.getString("startTime"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String endName = recordRs.getString("end");
                    Place end = endName.isEmpty() ? null : Place.fromChineseName(endName);
                    Date endTime = null;
                    String endTimeStr = recordRs.getString("endTime");
                    if (endTimeStr != null) {
                        try {
                            endTime = formatter.parse(endTimeStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    String bikeID = recordRs.getString("bikeID");
                    int amount = recordRs.getInt("amount");
                    String recordID = recordRs.getString("recordID");
                    String cardID = recordRs.getString("cardID");
                    
                    return new RentalRecord(recordID, user, bikeID, startTime, start, endTime, end, amount, cardID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
        // Add a return statement here
        return null;
    }

    public void updateRecord(User user, RentalRecord record) {
        String recordQuery = "SELECT recordID, start, startTime, bikeID, amount, cardID FROM records WHERE id_number = ? AND endtime IS NULL AND end = ''";
        String updateQuery = "UPDATE records SET end = ?, endTime = ?, amount = ? WHERE recordID = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement recordStmt = connection.prepareStatement(recordQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            
            recordStmt.setString(1, user.getIdNumber());
    
            try (ResultSet recordRs = recordStmt.executeQuery()) {
                if (recordRs.next()) {
                    // Update the record with end time and end place
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    updateStmt.setString(1, record.getEndPlace().getChineseName());
                    updateStmt.setString(2, formatter.format(record.getRentalEndTime()));
                    updateStmt.setInt(3, record.getRental());
                    updateStmt.setString(4, recordRs.getString("recordID"));
                    updateStmt.executeUpdate();
    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showRecords(JFrame frame, User user, UserService userService) {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());

        List<RentalRecord> records = getRecordRecently(user);

        String[] columnNames = {"Record ID", "Start", "StartTime", "End", "EndTime", "Bike ID", "Amount", "Card ID"};
        Object[][] data = new Object[records.size()][8];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (int i = 0; i < records.size(); i++) {
            RentalRecord record = records.get(i);
            data[i][0] = record.getRecordID();
            data[i][1] = record.getStartPlace().getChineseName();
            data[i][2] = formatter.format(record.getRentalStartTime());
            if (record.getEndPlace() != null && record.getRentalEndTime() != null) {
                data[i][3] = record.getEndPlace().getChineseName();
                data[i][4] = formatter.format(record.getRentalEndTime());
            } else {
                data[i][3] = "Not yet returned";
                data[i][4] = "Not yet returned";
            }
            data[i][5] = record.getBikeId();
            data[i][6] = record.getRental();
            data[i][7] = record.getCardID();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = new JButton("Back");

        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        resultsPanel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(resultsPanel);
        frame.revalidate();
        frame.repaint();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userService.showUserPanel(frame, user, userService.system);
            }
        });
    }
}
