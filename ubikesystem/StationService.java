package ubikesystem;

import javax.swing.*;
import group.*;
import object.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;


/**
 * This class provides the functionality to manage stations. The station service can get pillars for a station, and then get a pillar or a bike from a pillar.
 * The station service can also write a pillar or a bike to the database, update a pillar or a bike in the database, search for stations, borrow a bike, return a bike, add a bike to a station, and remove a bike from a station.
 * The station service can also get bikes with different areas, and show the find station panel.
 * The station service can also show the station results, show the station results map, and get a pillar.
 * The station service can also get a bike for a pillar, get a bike from a record, and get pillars for a station.
 * The station service can also get a bike, write a station to the database, and update a station in the database.
 * The station service can also write a pillar to the database, write a bike to the database, and update a pillar in the database.
 * The station service can also update a bike in the database, and search stations.
 * The station service can also show the find station panel, and show the station results.
 * The station service can also return a bike, get bikes with different areas, and add a bike to a station.
 * The station service can also borrow a bike, and get a pillar.
 */

public class StationService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ubike";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public List<Pillar> getPillarsForStation(Station station) {
        List<Pillar> pillars = new ArrayList<>();
        String pillarQuery = "SELECT stationID, pillarID, bikeID, maintenanceReport FROM pillars WHERE stationID = ?";
        try (Connection connection = getConnection();
             PreparedStatement pillarStmt = connection.prepareStatement(pillarQuery)) {

            pillarStmt.setString(1, station.getStationId());
            try (ResultSet pillarRs = pillarStmt.executeQuery()) {
                while (pillarRs.next()) {
                    String pillarID = pillarRs.getString("pillarID");
                    String bikeID = pillarRs.getString("bikeID");
                    String maintenanceReport = pillarRs.getString("maintenanceReport");
                    
                    Pillar pillar = null;
                    Bike bike = null;

                    if (bikeID != "") {
                        bike = getBikeForPillar(bikeID);
                    }

                    pillar = new Pillar(station, bike, pillarID);
                    
                    if (bike != null){
                        bike.setCurrentLocation(pillar);
                    }

                    if (maintenanceReport != null) {
                        pillar.setMaintenanceReport(maintenanceReport);
                    }
                    
                    pillars.add(pillar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pillars;
    }

    public Pillar getPillar(String pillarID) {
        String pillarQuery = "SELECT p.pillarID, p.stationID, p.bikeID, p.maintenanceReport, s.stationName, s.areaName, s.posX, s.posY " +
                             "FROM pillars p " +
                             "JOIN stations s ON p.stationID = s.stationID " +
                             "WHERE p.pillarID = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(pillarQuery)) {
    
            stmt.setString(1, pillarID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String stationID = rs.getString("stationID");
                    String stationName = rs.getString("stationName");
                    String areaName = rs.getString("areaName");
                    Place area = Place.fromChineseName(areaName);
                    Float posX = rs.getFloat("posX");
                    Float posY = rs.getFloat("posY");
                    List<Float> position = Arrays.asList(posX, posY);
    
                    Station station = new Station(stationName, stationID, area, position, new ArrayList<>());
                    String bikeID = rs.getString("bikeID");
                    String maintenanceReport = rs.getString("maintenanceReport");
    
                    Bike bike = getBikeForPillar(bikeID);
                    Pillar pillar = new Pillar(station, bike, pillarID);
                    bike.setCurrentLocation(pillar);
    
                    if (maintenanceReport != null) {
                        pillar.setMaintenanceReport(maintenanceReport);
                    }
    
                    return pillar;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if no pillar found
    }
    

    public Bike getBikeForPillar(String bikeID) {
        String bikeQuery = "SELECT bikeID, bikeType, status, areaName, maintenanceReport FROM bicycles WHERE bikeID = ?";
        try (Connection connection = getConnection();
             PreparedStatement bikeStmt = connection.prepareStatement(bikeQuery)) {

            bikeStmt.setString(1, bikeID);
            try (ResultSet bikeRs = bikeStmt.executeQuery()) {
                if (bikeRs.next()) {
                    int bikeType = bikeRs.getInt("bikeType");
                    String areaName = bikeRs.getString("areaName");
                    Place area = Place.fromChineseName(areaName);
                    int status = bikeRs.getInt("status");
                    BikeStatus bikeStatus = BikeStatus.fromStatusCode(status);
                    String maintenanceReport = bikeRs.getString("maintenanceReport");
                    
                    Bike bike = new Bike(bikeID, bikeType, area, bikeStatus);

                    if (maintenanceReport != null) {
                        bike.setMaintenanceReport(maintenanceReport);
                    }
                    return bike;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no bike found
    }

    public Bike getBikeFromRecord(User user) {
        RentalService service = new RentalService();
        RentalRecord record = service.findRecord(user);
        return getBikeForPillar(record.getBikeId());
    }

    // public void writeStationToDatabase(Station station) {
    //     String query = "INSERT INTO stations (stationID, stationName, areaName, posX, posY, pillarList) VALUES (?, ?, ?, ?, ?, ?, ?)";
    //     try (Connection connection = getConnection();
    //          PreparedStatement stmt = connection.prepareStatement(query)) {
    //         stmt.setString(1, station.getStationId());
    //         stmt.setString(2, station.getStationName());
    //         stmt.setString(3, station.getArea().toString());
    //         stmt.setFloat(4, station.getPos().get(0));
    //         stmt.setFloat(5, station.getPos().get(1));
    //         stmt.setString(6, station.getPillarList().toString()); // Assuming pillarList is a string
    //         stmt.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    public void writePillarToDatabase(Pillar pillar) {
        String query = "INSERT INTO pillars (stationID, pillarID, bikeID, maintenanceReport) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, pillar.getStation().getStationId());
            stmt.setString(2, pillar.getPillarId());
            stmt.setString(3, pillar.getBike().getBikeId());
            stmt.setString(4, pillar.getMaintenanceReport());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void writeBikeToDatabase(Bike bike) {
        String query = "INSERT INTO bicycles (bikeID, bikeType, status, areaName, maintenanceReport) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, bike.getBikeId());
            stmt.setInt(2, bike.getBikeType());
            stmt.setInt(3, bike.getStatus().getStatusCode());
            stmt.setString(4, bike.getArea().toString());
            stmt.setString(5, bike.getMaintenanceReport());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // public void updateStationInDatabase(Station station) {
    //     String query = "UPDATE stations SET stationName = ?, areaName = ?, posX = ?, posY = ?, pillarList = ? WHERE stationID = ?";
    //     try (Connection connection = getConnection();
    //          PreparedStatement stmt = connection.prepareStatement(query)) {
    //         stmt.setString(1, station.getStationName());
    //         stmt.setString(2, station.getArea().toString());
    //         stmt.setFloat(3, station.getPos().get(0));
    //         stmt.setFloat(4, station.getPos().get(1));
    //         stmt.setString(6, station.getPillarList().toString()); // Assuming pillarList is a string
    //         stmt.setString(7, station.getStationId());
    //         stmt.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    public void updatePillarInDatabase(Pillar pillar) {
        String query = "UPDATE pillars SET stationID = ?, bikeID = ?, maintenanceReport = ? WHERE pillarID = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, pillar.getStation().getStationId());
            if (pillar.getBike() != null) {
                stmt.setString(2, pillar.getBike().getBikeId());
            }
            else {
                stmt.setString(2, "");
            }
            stmt.setString(3, pillar.getMaintenanceReport());
            stmt.setString(4, pillar.getPillarId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateBikeInDatabase(Bike bike) {
        String query = "UPDATE bicycles SET bikeType = ?, status = ?, areaName = ?, maintenanceReport = ? WHERE bikeID = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setInt(1, bike.getBikeType());
            stmt.setInt(2, bike.getStatus().getStatusCode());
            stmt.setString(3, bike.getArea().toString());
            stmt.setString(4, bike.getMaintenanceReport());
            stmt.setString(5, bike.getBikeId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Station> searchStations(String name, List<Float> location, boolean hasBike, boolean hasSlot) {
        List<Station> stations = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT s.stationID, s.stationName, s.areaName, s.posX, s.posY, ")
                .append("(SELECT COUNT(*) FROM pillars p JOIN bicycles b ON p.bikeID = b.bikeID WHERE b.bikeType = 0 AND p.stationID = s.stationID AND b.status = 1) AS bikeType0Count, ")
                .append("(SELECT COUNT(*) FROM pillars p JOIN bicycles b ON p.bikeID = b.bikeID WHERE b.bikeType = 1 AND p.stationID = s.stationID AND b.status = 1) AS bikeType1Count, ")
                .append("(SELECT COUNT(*) FROM pillars p WHERE p.bikeID = '' AND p.stationID = s.stationID) AS emptySlotCount ")
                .append("FROM stations s");
    
        List<String> conditions = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            conditions.add("LOWER(s.stationName) LIKE ?");
        }
        if (location != null && location.size() == 3) {
            conditions.add("s.posX BETWEEN ? AND ? AND s.posY BETWEEN ? AND ?");
        }
        if (hasBike) {
            conditions.add("(SELECT COUNT(*) FROM pillars p JOIN bicycles b ON p.bikeID = b.bikeID WHERE b.status = 0 AND p.stationID = s.stationID) > 0");
        }
        if (hasSlot) {
            conditions.add("(SELECT COUNT(*) FROM pillars p WHERE p.bikeID = '' AND p.stationID = s.stationID) > 0");
        }
    
        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(String.join(" AND ", conditions));
        }
    
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString())) {
    
            int paramIndex = 1;
            if (name != null && !name.isEmpty()) {
                stmt.setString(paramIndex++, "%" + name.toLowerCase() + "%");
            }
            if (location != null && location.size() == 3) {
                float posX = location.get(0);
                float posY = location.get(1);
                float range = location.get(2);
                stmt.setFloat(paramIndex++, posX - range);
                stmt.setFloat(paramIndex++, posX + range);
                stmt.setFloat(paramIndex++, posY - range);
                stmt.setFloat(paramIndex++, posY + range);
            }
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String stationID = rs.getString("stationID");
                    String stationName = rs.getString("stationName");
                    String areaName = rs.getString("areaName");
                    Place area = Place.fromChineseName(areaName);
                    Float posX = rs.getFloat("posX");
                    Float posY = rs.getFloat("posY");
                    List<Float> position = Arrays.asList(posX, posY);
    
                    int bikeType0Count = rs.getInt("bikeType0Count");
                    int bikeType1Count = rs.getInt("bikeType1Count");
                    int emptySlotCount = rs.getInt("emptySlotCount");
    
                    Station station = new Station(stationName, stationID, area, position, new ArrayList<>());
                    station.setBikeType0Count(bikeType0Count);
                    station.setBikeType1Count(bikeType1Count);
                    station.setEmptySlotCount(emptySlotCount);
                    stations.add(station);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return stations;
    }

    public void showFindStationPanel(JFrame frame, User user, UserService userService) {
        JPanel findStationPanel = new JPanel();
        findStationPanel.setLayout(new GridLayout(8, 2));

        JLabel nameLabel = new JLabel("Station Name:");
        JTextField nameField = new JTextField();
        JLabel locationLabelX = new JLabel("Location X:");
        JTextField locationFieldX = new JTextField();
        JLabel locationLabelY = new JLabel("Location Y:");
        JTextField locationFieldY = new JTextField();
        JLabel locationLabelRange = new JLabel("Range:");
        JTextField locationFieldRange = new JTextField();
        JCheckBox hasBikeCheckBox = new JCheckBox("Has Bike");
        JCheckBox hasSlotCheckBox = new JCheckBox("Has Slot");
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        findStationPanel.add(nameLabel);
        findStationPanel.add(nameField);
        findStationPanel.add(locationLabelX);
        findStationPanel.add(locationFieldX);
        findStationPanel.add(locationLabelY);
        findStationPanel.add(locationFieldY);
        findStationPanel.add(locationLabelRange);
        findStationPanel.add(locationFieldRange);
        findStationPanel.add(new JLabel("Filters:"));
        findStationPanel.add(new JLabel());
        findStationPanel.add(hasBikeCheckBox);
        findStationPanel.add(hasSlotCheckBox);
        findStationPanel.add(new JLabel());
        findStationPanel.add(searchButton);
        findStationPanel.add(new JLabel());
        findStationPanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(findStationPanel);
        frame.revalidate();
        frame.repaint();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                List<Float> location = new ArrayList<>();
                String locationX = locationFieldX.getText();
                String locationY = locationFieldY.getText();
                String locationRange = locationFieldRange.getText();

                if(locationX.isEmpty() && locationY.isEmpty()) {
                    location = null;
                } else {
                    location.add(Float.parseFloat(locationX));
                    location.add(Float.parseFloat(locationY));
                    location.add(Float.parseFloat(locationRange));
                }
                boolean hasBike = hasBikeCheckBox.isSelected();
                boolean hasSlot = hasSlotCheckBox.isSelected();

                List<Station> stations = searchStations(name, location, hasBike, hasSlot);
                showStationResults(frame, stations, user, userService);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the back button functionality to go back to the previous panel
                userService.showUserPanel(frame, user, userService.system);
            }
        });
    }


    // private void showStationResults(JFrame frame, List<Station> stations, User user, UserService userService) {
    //     JPanel resultsPanel = new JPanel();
    //     resultsPanel.setLayout(new BorderLayout());

    //     // Create a map viewer
    //     JXMapViewer mapViewer = new JXMapViewer();
    //     OSMTileFactoryInfo info = new OSMTileFactoryInfo();
    //     DefaultTileFactory tileFactory = new DefaultTileFactory(info);
    //     mapViewer.setTileFactory(tileFactory);

    //     // Set the focus
    //     if (!stations.isEmpty()) {
    //         Station firstStation = stations.get(0);
    //         GeoPosition initialPosition = new GeoPosition(firstStation.getLatitude(), firstStation.getLongitude());
    //         mapViewer.setZoom(7);
    //         mapViewer.setAddressLocation(initialPosition);
    //     }

    //     // Create waypoints from the station positions
    //     Set<Waypoint> waypoints = new HashSet<>();
    //     for (Station station : stations) {
    //         GeoPosition position = new GeoPosition(station.getLatitude(), station.getLongitude());
    //         waypoints.add(new DefaultWaypoint(position));
    //     }

    //     // Create a waypoint painter to draw the waypoints
    //     WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
    //     waypointPainter.setWaypoints(waypoints);

    //     // Set the overlay painter
    //     mapViewer.setOverlayPainter(waypointPainter);

    //     // Add map to panel
    //     resultsPanel.add(new JScrollPane(mapViewer), BorderLayout.CENTER);

    //     // Back button
    //     JButton backButton = new JButton("Back");
    //     resultsPanel.add(backButton, BorderLayout.SOUTH);

    //     // Remove existing content and add new content
    //     frame.getContentPane().removeAll();
    //     frame.add(resultsPanel);
    //     frame.revalidate();
    //     frame.repaint();

    //     backButton.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             showFindStationPanel(frame, user, userService);
    //         }
    //     });
    // }

    private void showStationResultsMap(JFrame frame, List<Station> stations, User user, UserService userService) {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());

        // Load the image
        ImageIcon imageIcon = new ImageIcon("data\\map.png");

        // Create a label and add the image to it
        JLabel imageLabel = new JLabel(imageIcon);

        // Add the label to the panel
        resultsPanel.add(imageLabel, BorderLayout.CENTER);
        JButton backButton = new JButton("Back");
        resultsPanel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(resultsPanel);
        frame.revalidate();
        frame.repaint();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFindStationPanel(frame, user, userService);
            }
        });
    }
    
    private void showStationResults(JFrame frame, List<Station> stations, User user, UserService userService) {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "Location", "Bike Count/(2.0)", "Bike Count/(2.0E)", "Slot Count"};
        Object[][] data = new Object[stations.size()][5];
        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            data[i][0] = station.getStationName();
            data[i][1] = station.getPos();
            data[i][2] = station.getBikeType0Count();
            data[i][3] = station.getBikeType1Count();
            data[i][4] = station.getEmptySlotCount();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton mapButton = new JButton("Show Map");
        JButton backButton = new JButton("Back");

        JPanel southPanel = new JPanel();
        southPanel.add(mapButton);
        southPanel.add(backButton);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        resultsPanel.add(southPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(resultsPanel);
        frame.revalidate();
        frame.repaint();

        mapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStationResultsMap(frame, stations, user, userService);
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFindStationPanel(frame, user, userService);
            }
        });
    }

    
    public int borrowBike(String stationName, User user, Date rentalStartTime) {
        // Assume stations is a List<Station> containing all stations
        List<Station> stations = searchStations(stationName, null, false, false);
        Station targetStation = stations.get(0);
        if (targetStation != null) {
            CardService cardService = new CardService();
            if (targetStation.getBikeType0Count() + targetStation.getBikeType1Count() > 0 && cardService.getCardAmount(user.getCard()) >= 0){
                targetStation.setPillarList(getPillarsForStation(targetStation));
                Pillar picked = targetStation.getRandomPillarWithBike();
                if (picked == null) {
                    System.out.println("Pillard is null");
                    return 0;
                }
                Bike bike = picked.getBike();
                if (!picked.borrowBike(user)){
                    System.out.println("Borrow failed");
                    return 0;
                }
                RentalRecord record = new RentalRecord(user, bike.getBikeId(), rentalStartTime, targetStation.getArea(), 0, user.getCard());
                RentalService recordService = new RentalService();
                recordService.addRecord(user, record);
                updateBikeInDatabase(bike);
                updatePillarInDatabase(picked);
                return 1;
            } else {
                return 3;
            }
        } else {
            return 2;
        }
    }

    public int returnBike(String stationName, User user, Date rentalEndTime) {
        // Assume stations is a List<Station> containing all stations
        List<Station> stations = searchStations(stationName, null, false, false);
        Station targetStation = stations.get(0);
        Bike bike = getBikeFromRecord(user);
        if (bike == null) {
            return 4;
        }

        if (targetStation != null) {
            if (targetStation.getEmptySlotCount() != 0){
                targetStation.setPillarList(getPillarsForStation(targetStation));
                Pillar picked = targetStation.getRandomEmptyPillar();
                if (!picked.returnBike(user, bike)){
                    return 5;
                }
                
                FeeCalculator feeCalculator = new FeeCalculator();
                RentalRecord record = new RentalService().getBorrowRecord(user);
                record.setRentalEndTime(rentalEndTime);
                record.setEndPlace(targetStation.getArea());
                
                if (record.getRentalStartTime() != null && rentalEndTime != null && record.getRentalStartTime().after(rentalEndTime)) {
                    return 3;
                }

                int fee = 0;
                if (bike.getBikeType() == 0){
                    fee = feeCalculator.calculateYouBikeFee(record.getRentalStartTime(), rentalEndTime, targetStation.getArea());
                } else {
                    fee = feeCalculator.calculateYouBikeEFee(record.getRentalStartTime(), rentalEndTime, targetStation.getArea());
                }
                fee += feeCalculator.calculateCrossAreaFee(record.getRentalStartTime(), rentalEndTime, record.getStartPlace(), targetStation.getArea());

                
                record.setRental(fee);

                RentalService recordService = new RentalService();
                recordService.updateRecord(user, record);
                updateBikeInDatabase(picked.getBike());
                updatePillarInDatabase(picked);
                
                CardService cardService = new CardService();
                cardService.decreaseAmount(user.getCard(), fee);
                return 1;
            } else {
                return 3;
            }
        } else {
            return 2;
        }
    }

    public List<Bike> getBikesWithDifferentArea() {
        List<Bike> allBikes = new ArrayList<>();
        
        String bikeQuery = "SELECT b.bikeID, b.bikeType, b.status, b.areaName, b.maintenanceReport " +
                "FROM bicycles b " +
                "WHERE EXISTS ( " +
                "    SELECT 1 " +
                "    FROM pillars p " +
                "    JOIN stations s ON p.stationID = s.stationID " +
                "    WHERE p.bikeID = b.bikeID " +
                "    AND s.areaName != b.areaName " +
                ")";
    
        try (Connection connection = getConnection();
             PreparedStatement bikeStmt = connection.prepareStatement(bikeQuery)) {

            try (ResultSet bikeRs = bikeStmt.executeQuery()) {
                while (bikeRs.next()) {
                    String bikeID = bikeRs.getString("bikeID");
                    int bikeType = bikeRs.getInt("bikeType");
                    String areaName = bikeRs.getString("areaName");
                    Place area = Place.fromChineseName(areaName);
                    int status = bikeRs.getInt("status");
                    BikeStatus bikeStatus = BikeStatus.fromStatusCode(status);
                    String maintenanceReport = bikeRs.getString("maintenanceReport");
                    
                    Bike bike = new Bike(bikeID, bikeType, area, bikeStatus);

                    if (maintenanceReport != null) {
                        bike.setMaintenanceReport(maintenanceReport);
                    }

                    allBikes.add(bike);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBikes;
    }

    public int addBikeToStation(String bikeID, String stationName) {
        List<Station> stations = searchStations(stationName, null, false, false);
        Station station = stations.get(0);
        if (station != null && station.getEmptySlotCount() > 0) {
            Bike bike = getBikeForPillar(bikeID); 
            if(bike == null) {
                return 2;
            }
            station.setPillarList(getPillarsForStation(station));
            String query = "SELECT * FROM pillars WHERE bikeID = ?";
            try (Connection connection = getConnection();
                     PreparedStatement bikeStmt = connection.prepareStatement(query)) {

                bikeStmt.setString(1, bikeID);
                try (ResultSet bikeRs = bikeStmt.executeQuery()) {
                    if (bikeRs.next()) {
                        return 1;
                    }
                    else {
                        Pillar picked = station.getRandomEmptyPillar();
                        

                        if (!picked.addBike(bike)){
                            System.out.println("Add bike failed");
                            return 4;
                        }
                        updateBikeInDatabase(picked.getBike());
                        updatePillarInDatabase(picked);
                        return 0;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return 4;
            }
        }
        else {
            return 3;
        }
    }

    public int removeBikeFromStation(String bikeID, String stationName) {
        
        List<Station> stations = searchStations(stationName, null, false, false);
        Station station = stations.get(0);
        if (station != null) {
            Bike bike = getBikeForPillar(bikeID);
            if (bike != null) {
                station.setPillarList(getPillarsForStation(station));

                int flag = 0;
                for (Pillar pillar : station.getPillarList()) {
                    if (pillar.getBike() != null && pillar.getBike().getBikeId().equals(bikeID)) {
                        flag = 1;
                    }
                }
                if (flag != 1) {
                    return 1;
                }
                Pillar picked = station.getRandomPillarWithBike();
                if (!picked.removeBike()){
                    return 4;
                }
                updateBikeInDatabase(bike);
                updatePillarInDatabase(picked);
                return 0;
            } else {
                return 2;
            }
        } else {
            return 3;   
        }
    }
}
