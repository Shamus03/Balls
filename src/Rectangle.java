public class Rectangle
{
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getMinX()
    {
        return x;
    }

    public int getMinY()
    {
        return y;
    }

    public int getMaxX()
    {
        return x + width;
    }

    public int getMaxY()
    {
        return y + height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isIntersecting(Rectangle other)
    {
        return (this.getMinX() < other.getMaxX()
            && this.getMaxX() > other.getMinX())
            && (this.getMinY() < other.getMaxY()
            && this.getMaxY() > other.getMinY());
    }

    public String toString()
    {
        return String.format("Rectangle at (%d, %d) with dimensions %dx%d",
            x, y, width, height);
    }
}
