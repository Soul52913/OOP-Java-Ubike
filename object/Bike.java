package object;


/**
 * This class represents a bike in the bike sharing system.
 * The bike can be rented, returned, and maintained.
 * The bike has a unique bike ID, bike type, area, status, current location, maintenance report, and borrow record.
 * The bike can be rented, returned, and maintained.
 
 */
public class Bike {
    private String bikeId;
    private int bikeType; // 0 for 2.0 or 1 for 2.0E
    private Place area;
    private BikeStatus status; // enum for bike status
    private Pillar currentLocation; // Station class should have information about station and pillar
    private String maintenanceReport; // class for maintenance report
    private RentalRecord borrowRecord; // class for maintenance report

    // Constructor
    public Bike(String bikeId, int bikeType, Place area, BikeStatus status) {
        this.bikeId = bikeId;
        this.bikeType = bikeType;
        this.area = area;
        this.status = status;
        this.currentLocation = null;
        this.maintenanceReport = null; // Initialize with no maintenance report
        this.borrowRecord = null;
    }

    // Getters and Setters
    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public int getBikeType() {
        return bikeType;
    }

    public void setBikeType(int bikeType) {
        this.bikeType = bikeType;
    }

    public Place getArea() {
        return area;
    }

    public void setArea(Place area) {
        this.area = area;
    }

    public BikeStatus getStatus() {
        return status;
    }

    public void setStatus(BikeStatus status) {
        this.status = status;
    }

    public Pillar getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Pillar currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void leavePillar() {
        this.currentLocation = null;
    }

    public String getMaintenanceReport() {
        return maintenanceReport;
    }

    public void setMaintenanceReport(String maintenanceReport) {
        this.maintenanceReport = maintenanceReport;
    }

    public void clearMaintenanceReport() {
        this.maintenanceReport = null;
    }

    // Method to update bike status
    public void updateStatus(BikeStatus newStatus) {
        this.status = newStatus;
    }

    // Method to check if the bike is available
    public boolean isAvailable() {
        return status == BikeStatus.OPERATIONAL;
    }

    public boolean isRented() {
        return status == BikeStatus.RENTED;
    }

    public RentalRecord getBorrowRecord() {
        return borrowRecord;
    }

    public void setBorrowRecord(RentalRecord borrowRecord) {
        this.borrowRecord = borrowRecord;
    }

    public void removeBorrowRecord() {
        this.borrowRecord = null;
    }
    
    @Override
    public String toString() {
        return "Bike{" +
                "bikeId='" + bikeId + '\'' +
                ", bikeType='" + bikeType + '\'' +
                ", area='" + area + '\'' +
                ", status=" + status +
                ", currentLocation=" + currentLocation +
                ", maintenanceReport=" + maintenanceReport +
                '}';
    }
}