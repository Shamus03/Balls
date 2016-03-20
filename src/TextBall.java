import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class TextBall extends Ball
{
    private String text;

    public TextBall(double x, double y, double radius, String text)
    {
        super(x, y, radius);
        this.text = text;
    }

    @Override
    public void draw(Graphics g)
    {
        FontMetrics metrics = g.getFontMetrics();
        int x = (int) position.getX() - metrics.stringWidth(text) / 2;
        int y = (int) position.getY() - metrics.getHeight() / 2
            + metrics.getAscent();

        super.draw(g);
        g.setXORMode(Color.WHITE);
        g.drawString(text, x, y);
        g.setPaintMode();
    }
}
