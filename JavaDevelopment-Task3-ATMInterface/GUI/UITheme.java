package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UITheme {
    // Palette
    public static final Color NAVY        = new Color(0x1B2A41);
    public static final Color NAVY_LIGHT  = new Color(0x2A3F5F);
    public static final Color ACCENT      = new Color(0xF4B41A); // gold
    public static final Color ACCENT_DARK = new Color(0xD99A0B);
    public static final Color BG          = new Color(0xF4F6FA);
    public static final Color CARD        = Color.WHITE;
    public static final Color TEXT_DARK   = new Color(0x1B2A41);
    public static final Color TEXT_MUTED  = new Color(0x6B7A90);
    public static final Color SUCCESS     = new Color(0x2E7D32);
    public static final Color DANGER      = new Color(0xC62828);
    public static final Color BORDER      = new Color(0xE0E4EC);

    // Fonts
    public static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 17);
    public static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_LABEL   = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_MONO    = new Font("Monospaced", Font.PLAIN, 12);

    /** Card panel with rounded corners and a subtle border. */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)));
        return p;
    }

    /** Styled input field. */
    public static JTextField field() {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        f.setBackground(Color.WHITE);
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        f.setBackground(Color.WHITE);
        return f;
    }

    /** Round, colored button with hover effect. */
    public static JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = getModel().isRollover() ? bg.brighter() : bg;
                if (getModel().isPressed()) fill = bg.darker();
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_BODY);
        b.setForeground(fg);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
        return b;
    }

    public static JButton primaryButton(String text)   { return button(text, ACCENT, NAVY); }
    public static JButton secondaryButton(String text) { return button(text, NAVY_LIGHT, Color.WHITE); }
    public static JButton dangerButton(String text)    { return button(text, DANGER, Color.WHITE); }

    public static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_TITLE);
        l.setForeground(TEXT_DARK);
        return l;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_DARK);
        return l;
    }

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    public static JLabel body(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_DARK);
        return l;
    }
}