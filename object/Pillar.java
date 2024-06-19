package object;

import group.User;


/**
 * This class represents a pillar in the bike sharing system.
 * The pillar can have a bike, maintenance report, and station.
 * The pillar can borrow a bike, return a bike, remove a bike, and add a bike.
 */
public class Pillar {
    private Station station; // Station class should have information about the station
    private String pillarId;
    private Bike bike;
    private String maintenanceReport;

    public Pillar(Station station, Bike bike, String pillarId) {
        this.station = station;
        this.pillarId = pillarId;
        this.bike = bike;
        this.maintenanceReport = null;
    }

    // Getters and Setters
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getPillarId() {
        return pillarId;
    }

    public void setPillarId(String pillarId) {
        this.pillarId = pillarId;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public void emptyBike() {
        this.bike = null;
    }

    public String getMaintenanceReport() {
        return maintenanceReport;
    }

    public void setMaintenanceReport(String maintenanceReport) {
        this.maintenanceReport = maintenanceReport;
    }

    public boolean borrowBike(User user) {
        if (this.bike == null) {
            System.out.println("No bike available at this pillar");
        }
        if (this.bike != null) {
            this.bike.updateStatus(BikeStatus.RENTED);
            this.bike.leavePillar();
            user.borrowBike();
            this.bike = null;
            return true;
        } else {
            return false;
        }
    }

    public boolean returnBike(User user, Bike bike) {
        if (this.bike == null) {
            try {
                this.setBike(bike);
                this.bike.updateStatus(BikeStatus.OPERATIONAL);
                this.bike.setCurrentLocation(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean removeBike() {
        if (this.bike != null && !this.bike.isRented()) {
            this.bike.updateStatus(BikeStatus.NON_OPERATIONAL);
            this.bike.leavePillar();
            this.bike = null;
            return true;
        } else {
            return false;
        }
    }

    public boolean addBike(Bike bike) {
        if (this.bike == null) {
            try {
                this.setBike(bike);
                this.bike.updateStatus(BikeStatus.OPERATIONAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Pillar{" +
                "station=" + station +
                ", pillarId='" + pillarId + '\'' +
                ", maintenanceReport=" + maintenanceReport +
                '}';
    }
}
