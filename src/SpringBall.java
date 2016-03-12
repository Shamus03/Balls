public SpringBall extends DrawableBall
{
    private double springConstant;
    private double springLength;
    private Ball target;

    public void setSpringConstant(double s)
    {
        springLength = s;
    }

    public void setSpringLength(double l)
    {
        springLength = l;
    }

    public void setSpringTarget(Ball b)
    {
        this.target = b;
    }

    public void getSpringConstant()
    {
        return springConstant;
    }

    public void getSpringLength()
    {
        return springLength;
    }

    public void tick(int delta)
    {
        Vector springAccel = new Vector();
        this.velocity = thisvelocity.add(
            springAccel.scale(other.getMass());
        other.velocity = other.velocity.subtract(
            springAccel.scale(this.getMass());
        super.tick(delta);
    }
}
