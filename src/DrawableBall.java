import java.awt.Graphics;

public class DrawableBall extends Ball implements Drawable
{
    public DrawableBall()
    {
        super(new Vector(), new Vector(), DEFAULT_RADIUS);
    }

    public DrawableBall(Ball b)
    {
        super(b.getPos(), b.getVel(), b.getRadius());
    }

    public DrawableBall(double x, double y, double radius)
    {
        super(x, y, radius);
    }

    public DrawableBall(Vector position, Vector velocity, double radius)
    {
        super(position, velocity, radius);
    }

    public void draw(Graphics g)
    {
        g.fillOval( (int) (getPos().getX() - getRadius()),
                    (int) (getPos().getY() - getRadius()),
                    (int) (getRadius() + getRadius()),
                    (int) (getRadius() + getRadius()));
    }
}
