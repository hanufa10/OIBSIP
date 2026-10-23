package GUI;
import java.util.Scanner;

public class ATM {
    private Bank bank;
    private Account currentAccount;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Welcome to the ATM ===");
        
        // Login Logic (Max 3 attempts)
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter User ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            Account acc = bank.getAccount(id);
            if (acc != null && acc.validatePin(pin)) {
                currentAccount = acc;
                System.out.println("Login successful! Welcome, " + id);
                showMenu();
                return;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Attempts remaining: " + (3 - attempts));
            }
        }
        System.out.println("Card blocked. Too many failed attempts. Goodbye.");
    }

    private void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    currentAccount.printHistory();
                    break;
                case "2":
                    handleWithdraw();
                    break;
                case "3":
                    handleDeposit();
                    break;
                case "4":
                    handleTransfer();
                    break;
                case "5":
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleWithdraw() {
        System.out.print("Enter amount to withdraw: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            if (currentAccount.withdraw(amount)) {
                System.out.printf("Successfully withdrew $%.2f. New Balance: $%.2f%n", amount, currentAccount.getBalance());
            } else {
                System.out.println("Insufficient Funds");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    private void handleDeposit() {
        System.out.print("Enter amount to deposit: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            currentAccount.deposit(amount);
            System.out.printf("Successfully deposited $%.2f. New Balance: $%.2f%n", amount, currentAccount.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    private void handleTransfer() {
        System.out.print("Enter recipient User ID: ");
        String recipientId = scanner.nextLine();
        
        if (recipientId.equals(currentAccount.getUserId())) {
            System.out.println("You cannot transfer to yourself.");
            return;
        }

        Account recipient = bank.getAccount(recipientId);
        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        System.out.print("Enter amount to transfer: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            if (currentAccount.transferOut(amount, recipientId)) {
                recipient.transferIn(amount, currentAccount.getUserId());
                System.out.printf("Successfully transferred $%.2f to %s. New Balance: $%.2f%n", amount, recipientId, currentAccount.getBalance());
            } else {
                System.out.println("Insufficient Funds");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }
}
