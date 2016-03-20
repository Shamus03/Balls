import java.awt.Color;
import java.awt.Graphics;

public class Spring extends Entity
{
    private double springConstant;
    private double springLength;
    private Entity b1;
    private Entity b2;

    public Spring(Entity b1, Entity b2, double constant, double length)
    {
        this.b1 = b1;
        this.b2 = b2;
        springConstant = constant;
        springLength = length;
    }

    public Spring(Entity b1, Entity b2, double constant)
    {
        this.b1 = b1;
        this.b2 = b2;
        springConstant = constant;
        setSpringLength();
    }

    
    public Spring(Entity b1, Entity b2)
    {
        this.b1 = b1;
        this.b2 = b2;
        springConstant = 0;
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

    public Vector getPos()
    {
        return new Vector(0, 0);
    }

    public Vector getVel()
    {
        return new Vector(0, 0);
    }
    
    public double getEnergy()
    {
        double springStretch = springLength
            - b1.getPos().subtract(b2.getPos()).getMagnitude();
        return .5 * springConstant * springStretch * springStretch;
    }

    public double getMass()
    {
        // Ideal springs are massless
        return 0;
    }

    public void tick()
    {
        if (b1 == null || b2 == null)
            return;

        Vector difference = b1.getPos().subtract(b2.getPos());

        Vector springAccel = difference.getUnitVector().scale(
            springConstant * (springLength - difference.getMagnitude()));
        
        b1.setVel(b1.getVel().add(springAccel.scale(1 / b1.getMass())));
        b2.setVel(b2.getVel().subtract(springAccel.scale(1 / b2.getMass())));
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
