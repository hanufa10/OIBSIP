package GUI;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon;

/** Simple vector icons drawn in code — no image files needed. */
public class Icons {

    public static Icon card(Color color)   { return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawRoundRect(2, 5, 16, 11, 3, 3);
        g.drawLine(2, 9, 18, 9);
    }};}

    public static Icon money(Color color)  { return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f));
        g.drawOval(3, 3, 14, 14);
        g.drawString("$", 8, 14);
    }};}

    public static Icon history(Color color){ return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawOval(3, 3, 14, 14);
        g.drawLine(10, 6, 10, 10);
        g.drawLine(10, 10, 14, 12);
    }};}

    public static Icon withdraw(Color color){ return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(4, 4, 4, 16);   // wallet left
        g.drawLine(4, 16, 16, 16); // bottom
        g.drawLine(16, 16, 16, 4); // right
        g.drawLine(4, 4, 16, 4);   // top
        g.drawLine(10, 7, 10, 13); // arrow shaft
        g.drawLine(7, 10, 10, 13); // arrow head left
        g.drawLine(13, 10, 10, 13);// arrow head right
    }};}

    public static Icon deposit(Color color) { return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawRoundRect(3, 3, 14, 14, 3, 3);
        g.drawLine(10, 6, 10, 14); // shaft
        g.drawLine(6, 10, 10, 6);  // arrow up-left
        g.drawLine(14, 10, 10, 6); // arrow up-right
    }};}

    public static Icon transfer(Color color){ return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(3, 7, 15, 7);
        g.drawLine(12, 4, 15, 7);
        g.drawLine(12, 10, 15, 7);
        g.drawLine(17, 13, 5, 13);
        g.drawLine(8, 10, 5, 13);
        g.drawLine(8, 16, 5, 13);
    }};}

    public static Icon exit(Color color)   { return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawRoundRect(3, 3, 10, 14, 2, 2);
        g.drawLine(12, 10, 18, 10);
        g.drawLine(15, 7, 18, 10);
        g.drawLine(15, 13, 18, 10);
    }};}

    public static Icon user(Color color)   { return new BaseIcon(20, 20) { void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2f));
        g.drawOval(7, 3, 6, 6);
        g.drawArc(4, 11, 12, 10, 0, 180);
    }};}

    private abstract static class BaseIcon implements Icon {
        private final int w, h;
        BaseIcon(int w, int h) { this.w = w; this.h = h; }
        @Override public int getIconWidth()  { return w; }
        @Override public int getIconHeight() { return h; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            draw(g2);
            g2.dispose();
        }
        abstract void draw(Graphics2D g);
    }
}