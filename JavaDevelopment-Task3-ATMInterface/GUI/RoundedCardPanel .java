package GUI;
import javax.swing.*;
import java.awt.*;

/** A JPanel that paints a rounded white card with a subtle shadow. */
class RoundedCardPanel extends JPanel {
    private final int radius;
    public RoundedCardPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, radius, radius);
        // Card
        g2.setColor(UITheme.CARD);
        g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}