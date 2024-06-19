package object;

import group.User;
import java.util.Date;
import java.util.UUID;


/**
 * This class represents a rental record in the bike sharing system.
 * It represents a record of a user renting a bike.
 * It has the following attributes:
 * - recordID: a unique identifier for the record
 * - name: the name of the record
 * - description: the description
 * - user: the user who rented the bike
 * - date: the date of the record
 * - status: the status of the record
 * - amount: the amount of the record
 * - cardID: the card ID of the record
 */
public class RentalRecord {
    private String recordID;
    private User user;
    private String bikeID;
    private Date rentalStartTime;
    private Place start;
    private Date rentalEndTime;
    private Place end;
    private int amount;
    private String cardID;


    public RentalRecord() {
        this.recordID = UUID.randomUUID().toString();
    }

    public RentalRecord(String recordID, User user, String bikeID, Date rentalStartTime, Place StartPlace, Date rentalEndTime, Place EndPlace, int amount, String cardID) {
        this.recordID = recordID;
        this.user = user;
        this.bikeID = bikeID;
        this.rentalStartTime = rentalStartTime;
        this.start = StartPlace;
        this.rentalEndTime = rentalEndTime;
        this.end = EndPlace;
        this.amount = amount;
        this.cardID = cardID;
    }

    public RentalRecord(User user, String bikeID, Date rentalStartTime, Place StartPlace, Date rentalEndTime, Place EndPlace, int amount, String cardID) {
        this.recordID = UUID.randomUUID().toString();
        this.user = user;
        this.bikeID = bikeID;
        this.rentalStartTime = rentalStartTime;
        this.start = StartPlace;
        this.rentalEndTime = rentalEndTime;
        this.end = EndPlace;
        this.amount = amount;
        this.cardID = cardID;
    }

    public RentalRecord(User user, String bikeID, Date rentalStartTime, Place StartPlace, int amount, String cardID) {
        this.recordID = UUID.randomUUID().toString();
        this.user = user;
        this.bikeID = bikeID;
        this.rentalStartTime = rentalStartTime;
        this.start = StartPlace;
        this.rentalEndTime = null;
        this.end = null;
        this.amount = amount;
        this.cardID = cardID;
    }

    // Getters and Setters

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public User getUserId() {
        return user;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public String getBikeId() {
        return bikeID;
    }

    public void setBikeId(String bike) {
        this.bikeID = bike;
    }

    public Date getRentalStartTime() {
        return rentalStartTime;
    }

    public void setRentalStartTime(Date rentalStartTime) {
        this.rentalStartTime = rentalStartTime;
    }

    public Place getStartPlace() {
        return start;
    }

    public void setStartPlace(Place start) {
        this.start = start;
    }

    public Date getRentalEndTime() {
        return rentalEndTime;
    }

    public void setRentalEndTime(Date rentalEndTime) {
        this.rentalEndTime = rentalEndTime;
    }

    public Place getEndPlace() {
        return end;
    }

    public void setEndPlace(Place end) {
        this.end = end;
    }

    public int getRental() {
        return amount;
    }

    public void setRental(int amount) {
        this.amount = amount;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }
}