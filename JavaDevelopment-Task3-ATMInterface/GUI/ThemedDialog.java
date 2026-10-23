package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Reusable themed dialogs — matches the ATM's navy + gold look. */
public class ThemedDialog extends JDialog {

    private Object result;          // set by subclasses of usage
    private JComponent inputComp;   // optional input
    private final JLabel errorLabel = new JLabel(" ");

    private ThemedDialog(Window owner, String title, String subtitle) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        RoundedCardPanel root = new RoundedCardPanel(16);
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(0, 0, 4, 4));  // room for shadow

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(UITheme.FONT_HEADING);
        header.add(titleLbl, BorderLayout.WEST);

        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(new Color(0xB9C5D8));
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> { result = null; dispose(); });
        header.add(closeBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UITheme.CARD);
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel sub = new JLabel("<html><div style='width:280px;'>" + subtitle + "</div></html>");
            sub.setFont(UITheme.FONT_BODY);
            sub.setForeground(UITheme.TEXT_DARK);
            sub.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(sub);
            body.add(Box.createVerticalStrut(14));
        }

        this.bodyPanel = body;
        root.add(body, BorderLayout.CENTER);

        setContentPane(root);
        getRootPane().putClientProperty("Window.shadow", Boolean.TRUE);
    }

    private JPanel bodyPanel;

    // ================= Factory: message =================
    public static void showMessage(Component parent, String title, String message, Color accent) {
        ThemedDialog d = new ThemedDialog(SwingUtilities.getWindowAncestor(parent), title, message);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        JButton ok = UITheme.primaryButton("  OK  ");
        ok.addActionListener(e -> d.dispose());
        actions.add(ok);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        d.bodyPanel.add(Box.createVerticalStrut(6));
        d.bodyPanel.add(actions);

        d.pack();
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }

    // ================= Factory: input =================
    public static String showInput(Component parent, String title, String message,
                                   String fieldLabel, boolean isPassword) {
        ThemedDialog d = new ThemedDialog(SwingUtilities.getWindowAncestor(parent), title, message);

        JLabel lbl = UITheme.label(fieldLabel);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        d.bodyPanel.add(lbl);
        d.bodyPanel.add(Box.createVerticalStrut(4));

        JTextField field = isPassword ? UITheme.passwordField() : UITheme.field();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        d.bodyPanel.add(field);
        d.bodyPanel.add(Box.createVerticalStrut(16));

        // Error label (hidden until needed)
        d.errorLabel.setFont(UITheme.FONT_BODY);
        d.errorLabel.setForeground(UITheme.DANGER);
        d.errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        d.bodyPanel.add(d.errorLabel);
        d.bodyPanel.add(Box.createVerticalStrut(8));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton cancel = UITheme.secondaryButton("  Cancel  ");
        JButton ok     = UITheme.primaryButton("  Confirm  ");
        actions.add(cancel);
        actions.add(ok);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        d.bodyPanel.add(actions);

        cancel.addActionListener(e -> { d.result = null; d.dispose(); });
        Runnable submit = () -> {
            String val = isPassword
                    ? new String(((JPasswordField) field).getPassword()).trim()
                    : field.getText().trim();
            if (val.isEmpty()) {
                d.errorLabel.setText("This field cannot be empty.");
                return;
            }
            d.result = val;
            d.dispose();
        };
        ok.addActionListener(e -> submit.run());
        field.addActionListener(e -> submit.run());

        d.pack();
        d.setLocationRelativeTo(parent);
        SwingUtilities.invokeLater(field::requestFocusInWindow);
        d.setVisible(true);
        return (String) d.result;
    }

    // ================= Factory: transaction history =================
    public static void showHistory(Component parent, Account account) {
        ThemedDialog d = new ThemedDialog(SwingUtilities.getWindowAncestor(parent),
                "Transaction History",
                "All transactions for account <b>" + account.getUserId() + "</b> this session.");

        JTextArea area = new JTextArea(14, 44);
        area.setEditable(false);
        area.setFont(UITheme.FONT_MONO);
        area.setBackground(UITheme.BG);
        area.setForeground(UITheme.TEXT_DARK);
        area.setBorder(new EmptyBorder(12, 14, 12, 14));
        area.setText(account.getHistoryText());
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scroll, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(400, 280));
        wrapper.setPreferredSize(new Dimension(400, 280));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        d.bodyPanel.add(wrapper);
        d.bodyPanel.add(Box.createVerticalStrut(14));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        JButton close = UITheme.primaryButton("  Close  ");
        close.addActionListener(e -> d.dispose());
        actions.add(close);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        d.bodyPanel.add(actions);

        d.pack();
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }
}