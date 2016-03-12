import java.awt.Graphics;
import java.awt.Color;

public class SpringBall extends DrawableBall
{
    private double springConstant;
    private double springLength;
    private Ball target;

    public SpringBall(int x, int y, int radius)
    {
        super(x, y, radius);

        springConstant = 0;
        springLength = 0;
        target = null;
    }

    public void setSpringConstant(double s)
    {
        springConstant = s;
    }

    public void setSpringLength(double l)
    {
        springLength = l;
    }

    public void setSpringTarget(Ball b)
    {
        this.target = b;
    }

    public double getSpringConstant()
    {
        return springConstant;
    }

    public double getSpringLength()
    {
        return springLength;
    }

    public void tick(int delta)
    {
        if (target == null)
            return;

	Vector difference = this.position.subtract(target.position);

        Vector springAccel = difference.scale(springLength
            - difference.getMagnitude()).scale(springConstant
            / difference.getMagnitude());
        this.velocity = this.velocity.add(
            springAccel.scale(target.getMass() * delta));
        target.velocity = target.velocity.subtract(
            springAccel.scale(this.getMass() * delta));
        super.tick(delta);
    }

    public void draw(Graphics g)
    {
        super.draw(g);
        g.setColor(Color.GRAY);
        g.fillOval((int) position.getX() - 5,
            (int) position.getY() - 5, 10, 10);
    }
}
