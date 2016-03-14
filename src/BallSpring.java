import java.awt.Graphics;
import java.awt.Color;

public class BallSpring extends Entity
{
    private double springConstant;
    private double springLength;
    private Ball b1;
    private Ball b2;

    public BallSpring(double constant, double length, Ball b1, Ball b2)
    {
        springConstant = constant;
        springLength = length;
        this.b1 = b1;
        this.b2 = b2;
    }

    public BallSpring(double constant, Ball b1, Ball b2)
    {
        springConstant = constant;
        this.b1 = b1;
        this.b2 = b2;
        setSpringLength();
    }

    public void setSpringConstant(double s)
    {
        springConstant = s;
    }

    public void setSpringLength(double l)
    {
        springLength = l;
    }

    public void setSpringLength()
    {
        if (b1 == null || b2 == null)
            return;

        springLength = b1.getPos().subtract(b2.getPos()).getMagnitude();
    }

    public double getSpringConstant()
    {
        return springConstant;
    }

    public double getSpringLength()
    {
        return springLength;
    }
    
    public double getEnergy()
    {
        double springStretch = springLength
            - b1.position.subtract(b2.position).getMagnitude();
        return .5 * springConstant * springStretch * springStretch;
    }

    public void tick(int delta)
    {
        if (b1 == null || b2 == null)
            return;

        Vector difference = b1.position.subtract(b2.position);

        Vector springAccel = difference.getUnitVector().scale(
            springConstant * (springLength - difference.getMagnitude()));
        
        b1.velocity = b1.velocity.add(
            springAccel.scale(delta / b1.getMass()));
        b2.velocity = b2.velocity.subtract(
            springAccel.scale(delta / b2.getMass()));
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.setXORMode(Color.WHITE);
        g.drawLine((int) b1.getPos().getX(), (int) b1.getPos().getY(),
            (int) b2.getPos().getX(), (int) b2.getPos().getY());
        g.setPaintMode();
    }
}
