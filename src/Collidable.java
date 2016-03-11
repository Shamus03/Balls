public interface Collidable
{
    public abstract void collide(Collidable other);

    public abstract boolean isColliding(Collidable other);

    public abstract void checkBounds(Rectangle bounds);

    public abstract Rectangle getBounds();
}
