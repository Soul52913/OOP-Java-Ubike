package group;


/**
 * This class represents a user of the bike sharing system.
 * The user can borrow a bike, return a bike, and view their profile.
 */
public class User {
    private String phoneNumber;
    private String password;
    private String idNumber;
    private String email;
    private String cardNumber;
    private boolean borrowed;

    public User(String phoneNumber, String password, String idNumber, String email, String cardNumber) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.idNumber = idNumber;
        this.email = email;
        this.cardNumber = cardNumber;
        this.borrowed = false;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCard() {
        return cardNumber;
    }

    public void setCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void borrowBike() {
        this.borrowed = true;
    }

    public void returnBike() {
        this.borrowed = false;
    }

}
