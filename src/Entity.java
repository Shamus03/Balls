public abstract class Entity implements Tickable, Drawable
{
    public abstract Vector getPos();

    public abstract Vector getVel();

    public abstract double getEnergy();

    public abstract double getMass();

    public void addPos(Vector v)
    {
    }

    public void addVel(Vector v)
    {
    }

    public void setPos(Vector position)
    {
    }

    public void setVel(Vector velocity)
    {
    }
}
