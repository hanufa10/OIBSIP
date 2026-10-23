package GUI;
import java.util.HashMap;

public class Bank {
    private HashMap<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
        // Seed data for testing
        accounts.put("user1", new Account("user1", "1234", 1000.00));
        accounts.put("user2", new Account("user2", "5678", 500.00));
        accounts.put("user3", new Account("user3", "0000", 2500.00));
    }

    public Account getAccount(String userId) {
        return accounts.get(userId);
    }
}
