import java.text.DecimalFormat;

public class Vector
{
    private double x;
    private double y;

    public Vector()
    {
        this(0, 0);
    }

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public Vector add(Vector other)
    {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector subtract(Vector other)
    {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    public Vector scale(double scalar)
    {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    public double getMagnitude()
    {
        return Math.sqrt(x * x + y * y);
    }

    public double getAngle()
    {
        return Math.atan2(y, x);
    }

    public double dotProduct(Vector other)
    {
        return this.x * other.x + this.y * other.y;
    }

    public String toString()
    {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
