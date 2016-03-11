public class Ball implements Collidable, Tickable
{
    protected static final double DEFAULT_RADIUS = 30;
    protected static final Vector GRAVITY = new Vector(0, .0005);
    protected static final double RESTITUTION = .9;

    protected Vector position;
    protected Vector velocity;
    protected double radius;

    public Ball()
    {
        this(new Vector(), new Vector(), DEFAULT_RADIUS);
    }

    public Ball(double x, double y, double radius)
    {
        position = new Vector(x, y);
        this.radius = radius;
        velocity = new Vector(0, 0);
    }

    public Ball(Vector position, Vector velocity, double radius)
    {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
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

    public double getRadius()
    {
        return radius;
    }

    public double getMass()
    {
        return Math.PI * radius * radius;
    }

    public void tick(int delta)
    {
        velocity = velocity.add(GRAVITY.scale(delta));
        position = position.add(velocity.scale(delta));
    }

    public void collide(Collidable otherCollidable)
    {
        if (isColliding(otherCollidable))
        {
            if (otherCollidable instanceof Ball)
            {
                Ball other = (Ball) otherCollidable;

                Vector v1 = this.getVel();
                Vector v2 = other.getVel();
                Vector x1 = this.getPos();
                Vector x2 = other.getPos();

                if (v2.subtract(v1).dotProduct(x2.subtract(x1)) < 0)
                {
                    double m1 = this.getMass();
                    double m2 = other.getMass();

                    double s1 = 2 * m2 / (m1 + m2) * v1.subtract(v2)
                        .scale(RESTITUTION).dotProduct(x1.subtract(x2))
                        / Math.pow(x1.subtract(x2).getMagnitude(), 2);
                    Vector u1 = v1.subtract(x1.subtract(x2).scale(s1));

                    double s2 = 2 * m1 / (m1 + m2) * v2.subtract(v1)
                        .scale(RESTITUTION).dotProduct(x2.subtract(x1))
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
                return this.getPos().subtract(b.getPos()).getMagnitude() < this.radius + b.radius;
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
        if ((velocity.getX() < 0 && position.getX() - radius < bounds.getMinX()) ||
            (velocity.getX() > 0 && position.getX() + radius > bounds.getMaxX()))
        {
            velocity = new Vector(-velocity.getX(), velocity.getY());
        }

        if ((velocity.getY() < 0 && position.getY() - radius < bounds.getMinY()) ||
            (velocity.getY() > 0 && position.getY() + radius > bounds.getMaxY()))
        {
            velocity = new Vector(velocity.getX(), -velocity.getY());
        }
    }

    public String toString()
    {
        String result = String.format("Ball:  Radius %.2f", radius);
        result += "\nPosition: " + position;
        result += "\nVelocity: " + velocity;

        return result;
    }
}
