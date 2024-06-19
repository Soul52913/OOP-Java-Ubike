package object;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of a card in the system.
 */

public class Card {
    private String cardNumber;
    private double balance;
    private List<String> transactions;

    public Card(String cardNumber, double balance) {
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    // Method to add money to the card
    public void addMoney(double amount) {
        if (amount > 0) {
            this.balance += amount;
            transactions.add("Add Money, " + amount);
        }
    }

    // Method to deduct money from the card
    public boolean deductMoney(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            transactions.add("Deduct Money, " + -amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}