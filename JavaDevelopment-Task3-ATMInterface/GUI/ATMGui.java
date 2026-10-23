package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ATMGui extends JFrame {
    private Bank bank;
    private Account currentAccount;
    private int loginAttempts = 0;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Login components
    private JTextField userIdField;
    private JPasswordField pinField;
    private JLabel loginMsgLabel;

    // Menu components
    private JLabel welcomeLabel;
    private JLabel balanceLabel;

    public ATMGui(Bank bank) {
        this.bank = bank;
        setTitle("SecureBank ATM");
        setSize(500, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(UITheme.BG);

        mainPanel.add(buildLoginPanel(), "LOGIN");
        mainPanel.add(buildMenuPanel(),  "MENU");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    // ========================================================
    // HEADER (reused on every screen)
    // ========================================================
    private JPanel buildHeader(String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.NAVY);
        header.setBorder(new EmptyBorder(20, 28, 20, 28));

        JLabel brand = new JLabel("SecureBank");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 20));
        brand.setIcon(Icons.card(UITheme.ACCENT));
        brand.setIconTextGap(10);

        JLabel sub = new JLabel(subtitle);
        sub.setForeground(new Color(0xB9C5D8));
        sub.setFont(UITheme.FONT_BODY);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.add(brand);
        left.add(Box.createVerticalStrut(2));
        left.add(sub);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    // ========================================================
    // LOGIN SCREEN
    // ========================================================
    private JPanel buildLoginPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        root.add(buildHeader("Welcome — please sign in"), BorderLayout.NORTH);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(UITheme.BG);
        wrap.setBorder(new EmptyBorder(24, 40, 40, 40));

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel signIn = UITheme.heading("Sign in to your account");
        signIn.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(signIn);
        card.add(Box.createVerticalStrut(6));

        JLabel hint = UITheme.label("Use your User ID and 4-digit PIN");
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(hint);
        card.add(Box.createVerticalStrut(18));

        JLabel idLbl = UITheme.label("USER ID");
        idLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(idLbl);
        card.add(Box.createVerticalStrut(4));
        userIdField = UITheme.field();
        userIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        userIdField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(userIdField);
        card.add(Box.createVerticalStrut(14));

        JLabel pinLbl = UITheme.label("PIN");
        pinLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(pinLbl);
        card.add(Box.createVerticalStrut(4));
        pinField = UITheme.passwordField();
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pinField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(pinField);
        card.add(Box.createVerticalStrut(20));

        JButton loginBtn = UITheme.primaryButton("  Sign In  ");
        loginBtn.setIcon(Icons.user(UITheme.NAVY));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(14));

        loginMsgLabel = new JLabel(" ");
        loginMsgLabel.setFont(UITheme.FONT_BODY);
        loginMsgLabel.setForeground(UITheme.DANGER);
        loginMsgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginMsgLabel);

        // Enter-key convenience
        pinField.addActionListener(e -> handleLogin());
        userIdField.addActionListener(e -> pinField.requestFocus());

        wrap.add(card);
        root.add(wrap, BorderLayout.CENTER);
        return root;
    }

    private void handleLogin() {
        String userId = userIdField.getText().trim();
        String pin = new String(pinField.getPassword());
        Account acc = bank.getAccount(userId);

        if (acc != null && acc.validatePin(pin)) {
            currentAccount = acc;
            loginAttempts = 0;
            loginMsgLabel.setText(" ");
            userIdField.setText("");
            pinField.setText("");
            refreshMenu();
            cardLayout.show(mainPanel, "MENU");
        } else {
            loginAttempts++;
            int left = 3 - loginAttempts;
            if (left <= 0) {
               ThemedDialog.showMessage(this, "Account Locked",
                        "Too many failed attempts. Your account is locked for security.",
                        UITheme.DANGER);
                System.exit(0);
            } else {
                loginMsgLabel.setText("Incorrect ID or PIN  •  " + left + " attempt(s) left");
            }
        }
    }

    // ========================================================
    // MAIN MENU SCREEN
    // ========================================================
    private JPanel buildMenuPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        root.add(buildHeader("Dashboard"), BorderLayout.NORTH);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(UITheme.BG);
        wrap.setBorder(new EmptyBorder(24, 40, 40, 40));

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Welcome + balance
        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(UITheme.FONT_HEADING);
        welcomeLabel.setForeground(UITheme.TEXT_DARK);
        welcomeLabel.setIcon(Icons.user(UITheme.NAVY));
        welcomeLabel.setIconTextGap(10);

        JLabel balanceCaption = UITheme.label("AVAILABLE BALANCE");
        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        balanceLabel.setForeground(UITheme.SUCCESS);

        JPanel balanceBox = new JPanel();
        balanceBox.setLayout(new BoxLayout(balanceBox, BoxLayout.Y_AXIS));
        balanceBox.setOpaque(false);
        balanceBox.add(balanceCaption);
        balanceBox.add(Box.createVerticalStrut(2));
        balanceBox.add(balanceLabel);

        info.add(welcomeLabel, BorderLayout.NORTH);
        info.add(balanceBox, BorderLayout.SOUTH);

        card.add(info);
        card.add(Box.createVerticalStrut(20));

        // Menu buttons in 2x2 grid
        JPanel grid = new JPanel(new GridLayout(2, 2, 12, 12));
        grid.setOpaque(false);
        grid.add(tile("Transaction History", Icons.history(UITheme.NAVY),     e -> showHistory()));
        grid.add(tile("Withdraw",           Icons.withdraw(UITheme.NAVY),    e -> doWithdraw()));
        grid.add(tile("Deposit",            Icons.deposit(UITheme.NAVY),     e -> doDeposit()));
        grid.add(tile("Transfer",           Icons.transfer(UITheme.NAVY),    e -> doTransfer()));
        card.add(grid);
        card.add(Box.createVerticalStrut(16));

        JButton quitBtn = UITheme.dangerButton("   Quit ATM   ");
        quitBtn.setIcon(Icons.exit(Color.WHITE));
        quitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        quitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        quitBtn.addActionListener(e -> {
            ThemedDialog.showMessage(this, "Goodbye",
                    "Thank you for using SecureBank. Have a great day!", UITheme.SUCCESS);
            System.exit(0);
        });
        card.add(quitBtn);
        card.add(Box.createVerticalStrut(10));

        JButton switchBtn = UITheme.secondaryButton("   Switch Account   ");
        switchBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        switchBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        switchBtn.addActionListener(e -> {
            currentAccount = null;
            loginMsgLabel.setText(" ");
            cardLayout.show(mainPanel, "LOGIN");
        });
        card.add(switchBtn);

        wrap.add(card);
        root.add(wrap, BorderLayout.CENTER);
        return root;
    }

    /** A large clickable tile for the 2x2 menu. */
    private JButton tile(String text, Icon icon, java.awt.event.ActionListener onClick) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xEEF2F9) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setLayout(new BorderLayout());
        b.setIcon(icon);
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setFont(UITheme.FONT_BODY);
        b.setForeground(UITheme.TEXT_DARK);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 90));
        b.setBorder(new EmptyBorder(14, 10, 14, 10));
        b.addActionListener(onClick);
        return b;
    }

    private void refreshMenu() {
        if (currentAccount == null) return;
        welcomeLabel.setText("Hello, " + currentAccount.getUserId());
        balanceLabel.setText(String.format("$%,.2f", currentAccount.getBalance()));
    }

    // ========================================================
    // OPERATIONS
    // ========================================================
        // ========================================================
    // OPERATIONS (using themed dialogs)
    // ========================================================
    private void showHistory() {
        ThemedDialog.showHistory(this, currentAccount);
    }

    private void doWithdraw() {
        String input = ThemedDialog.showInput(this,
                "Withdraw",
                "Enter the amount you would like to withdraw from your account.",
                "AMOUNT ($)", false);
        if (input == null) return;

        Double amount = parseAmount(input);
        if (amount == null) return;

        if (currentAccount.withdraw(amount)) {
            ThemedDialog.showMessage(this, "Withdrawal Successful",
                    String.format("You withdrew $%.2f.<br><br>New balance: <b>$%,.2f</b>",
                            amount, currentAccount.getBalance()),
                    UITheme.SUCCESS);
            refreshMenu();
        } else {
            ThemedDialog.showMessage(this, "Insufficient Funds",
                    "Your balance is not enough to complete this withdrawal.",
                    UITheme.DANGER);
        }
    }

    private void doDeposit() {
        String input = ThemedDialog.showInput(this,
                "Deposit",
                "Enter the amount you would like to deposit.",
                "AMOUNT ($)", false);
        if (input == null) return;

        Double amount = parseAmount(input);
        if (amount == null) return;

        currentAccount.deposit(amount);
        ThemedDialog.showMessage(this, "Deposit Successful",
                String.format("You deposited $%.2f.<br><br>New balance: <b>$%,.2f</b>",
                        amount, currentAccount.getBalance()),
                UITheme.SUCCESS);
        refreshMenu();
    }

    private void doTransfer() {
        String recipientId = ThemedDialog.showInput(this,
                "Transfer — Step 1 of 2",
                "Enter the recipient's User ID.",
                "RECIPIENT USER ID", false);
        if (recipientId == null) return;

        if (recipientId.equals(currentAccount.getUserId())) {
            ThemedDialog.showMessage(this, "Not Allowed",
                    "You cannot transfer to yourself.", UITheme.DANGER);
            return;
        }
        Account recipient = bank.getAccount(recipientId);
        if (recipient == null) {
            ThemedDialog.showMessage(this, "Not Found",
                    "Recipient account <b>" + recipientId + "</b> does not exist.", UITheme.DANGER);
            return;
        }

        String input = ThemedDialog.showInput(this,
                "Transfer — Step 2 of 2",
                "Enter the amount to send to <b>" + recipientId + "</b>.",
                "AMOUNT ($)", false);
        if (input == null) return;

        Double amount = parseAmount(input);
        if (amount == null) return;

        if (currentAccount.transferOut(amount, recipientId)) {
            recipient.transferIn(amount, currentAccount.getUserId());
            ThemedDialog.showMessage(this, "Transfer Successful",
                    String.format("Sent $%.2f to <b>%s</b>.<br><br>New balance: <b>$%,.2f</b>",
                            amount, recipientId, currentAccount.getBalance()),
                    UITheme.SUCCESS);
            refreshMenu();
        } else {
            ThemedDialog.showMessage(this, "Insufficient Funds",
                    "Your balance is not enough to complete this transfer.", UITheme.DANGER);
        }
    }

    /** Parse amount, showing themed error if invalid. Returns null on failure. */
    private Double parseAmount(String raw) {
        try {
            double amount = Double.parseDouble(raw);
            if (amount <= 0) {
                ThemedDialog.showMessage(this, "Invalid Amount",
                        "Amount must be greater than 0.", UITheme.DANGER);
                return null;
            }
            return amount;
        } catch (NumberFormatException ex) {
            ThemedDialog.showMessage(this, "Invalid Input",
                    "Please enter a valid number.", UITheme.DANGER);
            return null;
        }
    }
    /** A styled dialog helper that feels consistent with the theme. */
    private void showDialog(String title, String message, Color accent) {
        JLabel msg = new JLabel("<html><div style='width:280px;'>" + message.replace("\n", "<br>") + "</div></html>");
        msg.setFont(UITheme.FONT_BODY);
        msg.setForeground(UITheme.TEXT_DARK);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(UITheme.FONT_HEADING);
        titleLbl.setForeground(accent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        panel.add(titleLbl);
        panel.add(Box.createVerticalStrut(8));
        panel.add(msg);

        JOptionPane.showMessageDialog(this, panel, "", JOptionPane.PLAIN_MESSAGE);
    }
}