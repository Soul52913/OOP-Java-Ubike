package group;


import object.BikeStatus;

/**
 * This class represents a maintenance staff user of the bike sharing system.
    * The maintenance staff can update the status of a bike, get bikes that have been returned in a different district,
 */
public class MaintenanceStaff extends User {
    public MaintenanceStaff(String phoneNumber, String password, String idNumber, String email, String cardNumber) {
        super(phoneNumber, password, idNumber, email, cardNumber);
    }


    public void updateBikeStatus(String bikeId, BikeStatus newStatus) {
        // code to update bike status
    }

    // public List<Bike> getCrossDistrictBikes() {
    //     // code to get bikes that have been returned in a different district
    // }

    // public void addBikeToStation(Station station, Bike bike) {
    //     // code to add a bike to a station
    // }

    // public void removeBikeFromStation(Station station, Bike bike) {
    //     // code to remove a bike from a station
    // }

    // private void showMaintenancePanel(JFrame frame) {
    //     JPanel maintenancePanel = new JPanel();
    //     maintenancePanel.setLayout(new GridLayout(3, 1));

    //     JButton checkBikeStatusButton = new JButton("Check Bike Status");
    //     JButton searchAreaBikeButton = new JButton("Search Different Area Bike");
    //     JButton addDecreaseBikeButton = new JButton("Add/Decrease Bike");

    //     maintenancePanel.add(checkBikeStatusButton);
    //     maintenancePanel.add(searchAreaBikeButton);
    //     maintenancePanel.add(addDecreaseBikeButton);

    //     frame.getContentPane().removeAll();
    //     frame.add(maintenancePanel);
    //     frame.revalidate();
    //     frame.repaint();

    //     // Add ActionListeners for each button and implement the required functionalities
    // }
}