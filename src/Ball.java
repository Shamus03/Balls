import java.awt.Color;
import java.awt.Graphics;

public class Ball extends Entity implements Collidable
{
    private static double defaultRestitution = 1;
    private static double defaultFrictionK = 0;
    private static double defaultFrictionS = 0;

    private Vector position;
    private Vector velocity;
    private double radius;
    private double restitution;
    private double frictionK;
    private double frictionS;

    public Ball(double x, double y, double radius)
    {
        position = new Vector(x, y);
        velocity = new Vector(0, 0);
        this.radius = radius;
        restitution = defaultRestitution;
        frictionK = defaultFrictionK;
        frictionS = defaultFrictionS;
    }

    public Ball(Vector position, Vector velocity, double radius)
    {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        restitution = defaultRestitution;
        frictionK = defaultFrictionK;
        frictionS = defaultFrictionS;
    }

    public Vector getPos()
    {
        return position;
    }
    
    public Vector getVel()
    {
        return velocity;
    }

    public void setPos(Vector pos)
    {
        position = pos;
    }

    public void setVel(Vector vel)
    {
        velocity = vel;
    }

    public void addVel(Vector vel)
    {
        velocity = velocity.add(vel);
    }

    public void addPos(Vector pos)
    {
        position = position.add(pos);
    }

    public double getRestitution()
    {
        return restitution;
    }

    public double getKineticFriction()
    {
        return frictionK;
    }

    public double getStaticFriction()
    {
        return frictionS;
    }

    public void setRestitution(double r)
    {
        restitution = r;
    }

    public void setKineticFriction(double f)
    {
        frictionK = f;
    }

    public void setStaticFriction(double f)
    {
        frictionS = f;
    }

    public double getRadius()
    {
        return radius;
    }

    public double getMass()
    {
        return Math.PI * radius * radius;
    }

    public double getEnergy()
    {
        return .5 * getMass() * Math.pow(velocity.getMagnitude(), 2);
    }

    public void tick()
    {
        double speed = velocity.getMagnitude();
        if (speed != 0)
        {
            if (speed < frictionS)
            {
                velocity = new Vector(0, 0);
            }
            else
            {
                double kineticFrictionScalar =
                    (speed - frictionK) / speed;
                velocity = velocity.scale(kineticFrictionScalar);
            }
            position = position.add(velocity);
        }
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillOval( (int) (getPos().getX() - getRadius()),
                    (int) (getPos().getY() - getRadius()),
                    (int) (getRadius() + getRadius()),
                    (int) (getRadius() + getRadius()));
    }

    public void collide(Collidable otherCollidable)
    {
        if (isColliding(otherCollidable))
        {
            if (otherCollidable instanceof Ball)
            {
                Ball other = (Ball) otherCollidable;
                double m1 = this.getMass();
                double m2 = other.getMass();

                Vector diff = this.getPos().subtract(other.getPos());
                Vector unitDiff = diff.getUnitVector();
                double thisChange = m2 / (m1 + m2);
                double otherChange = m1 / (m1 + m2);
                double distance = diff.getMagnitude();
                double combinedRadius = this.getRadius() + other.getRadius();
                double overlapDistance = combinedRadius - distance;
                this.addPos(unitDiff.scale(thisChange * overlapDistance));
                other.addPos(unitDiff.scale(-otherChange * overlapDistance));

                Vector v1 = this.getVel();
                Vector v2 = other.getVel();
                Vector x1 = this.getPos();
                Vector x2 = other.getPos();

                if (v2.subtract(v1).dotProduct(x2.subtract(x1)) < 0)
                {
                    double s1 = 2 * m2 / (m1 + m2) * v1.subtract(v2)
                        .scale(restitution).dotProduct(x1.subtract(x2))
                        / Math.pow(x1.subtract(x2).getMagnitude(), 2);
                    Vector u1 = v1.subtract(x1.subtract(x2).scale(s1));

                    double s2 = 2 * m1 / (m1 + m2) * v2.subtract(v1)
                        .scale(restitution).dotProduct(x2.subtract(x1))
                        / Math.pow(x2.subtract(x1).getMagnitude(), 2);
                    Vector u2 = v2.subtract(x2.subtract(x1).scale(s2));

                    this.velocity = u1;
                    other.velocity = u2;
                }
            }
        }
    }

    public boolean isColliding(Collidable other)
    {
        if (other.getBounds().isIntersecting(this.getBounds()))
        {
            if (other instanceof Ball)
            {
                Ball b = (Ball) other;
                return this.getPos().subtract(b.getPos()).getMagnitude()
                    < this.radius + b.radius;
            }
        }
        return false;
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) (position.getX() - radius),
                             (int) (position.getY() - radius),
                             (int) radius * 2, (int) radius * 2);
    }

    public void checkBounds(Rectangle bounds)
    {
        if (position.getX() < bounds.getMinX() + radius)
        {
            position = new Vector(bounds.getMinX() + radius, position.getY());
            if (velocity.getX() < 0)
            {
                velocity = new Vector(-velocity.getX() * restitution,
                    velocity.getY());
            }
        }
        else if (position.getX() > bounds.getMaxX() - radius)
        {
            position = new Vector(bounds.getMaxX() - radius, position.getY());
            if (velocity.getX() > 0)
            {
                velocity = new Vector(-velocity.getX() * restitution,
                    velocity.getY());
            }
        }

        if (position.getY() < bounds.getMinY() + radius)
        {
            position = new Vector(position.getX(), bounds.getMinY() + radius);
            if (velocity.getY() < 0)
            {
                velocity = new Vector(velocity.getX(),
                    -velocity.getY() * restitution);
            }
        }
        else if (position.getY() > bounds.getMaxY() - radius)
        {
            position = new Vector(position.getX(), bounds.getMaxY() - radius);
            if (velocity.getY() > 0)
            {
                velocity = new Vector(velocity.getX(),
                    -velocity.getY() * restitution);
            }
        }
    }

    public String toString()
    {
        String result = String.format("Ball:  Radius %.2f", radius);
        result += "\nPosition: " + position;
        result += "\nVelocity: " + velocity;

        return result;
    }

    public static void setDefaultRestitution(double r)
    {
        defaultRestitution = r;
    }

    public static void setDefaultKineticFriction(double f)
    {
        defaultFrictionK = f;
    }

    public static void setDefaultStaticFriction(double f)
    {
        defaultFrictionS = f;
    }
}
