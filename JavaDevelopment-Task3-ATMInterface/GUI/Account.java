package GUI;
import java.util.ArrayList;

public class Account {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> history;

    public Account(String userId, String pin, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
        this.history = new ArrayList<>();
    }

    // Encapsulation: Getters and setters
    public String getUserId() { return userId; }
    public boolean validatePin(String inputPin) { return this.pin.equals(inputPin); }
    public double getBalance() { return balance; }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            history.add(new Transaction("DEPOSIT", amount, ""));
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            history.add(new Transaction("WITHDRAW", amount, ""));
            return true;
        }
        return false;
    }

    public boolean transferOut(double amount, String recipientId) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            history.add(new Transaction("TRANSFER", amount, "to " + recipientId));
            return true;
        }
        return false;
    }

    public void transferIn(double amount, String senderId) {
        balance += amount;
        history.add(new Transaction("TRANSFER", amount, "from " + senderId));
    }

    public void printHistory() {
        System.out.println("--- Transaction History ---");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
        System.out.println("---------------------------");
    }
    // 在 Account.java 中添加
    public String getHistoryText() {
        if (history.isEmpty()) {
            return "No Transaction History Found ...";
        }
        StringBuilder sb = new StringBuilder();
        for (Transaction t : history) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }
}