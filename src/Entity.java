public abstract class Entity implements Tickable, Drawable
{
    public abstract Vector getPos();

    public abstract Vector getVel();

    public abstract double getEnergy();

    public abstract double getMass();

    public void setPos(Vector position)
    {
    }

    public void setVel(Vector velocity)
    {
    }
}
