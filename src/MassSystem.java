import java.awt.Color;
import java.awt.Graphics;

public class MassSystem extends Entity
{
    private static Color SYSTEM_COLOR = new Color(255, 0, 0, 50);

    private ArrayList<Entity> system;
    private double totalMass;
    private Vector centerMass;
    private boolean changed;

    public MassSystem()
    {
        system = new ArrayList<Entity>();
        centerMass = new Vector(0, 0);
        totalMass = 0;
        changed = true;
    }

    public void add(Entity e)
    {
        system.add(e);
        changed = true;
        tick();
    }

    public void tick()
    {
        int i;
        double massInfluenceX = 0;
        double massInfluenceY = 0;
        Entity e;
        double mass;
        for (i = 0; i < system.size(); i++)
        {
            e = system.get(i);
            massInfluenceX += e.getMass() * e.getPos().getX();
            massInfluenceY += e.getMass() * e.getPos().getY();
        }

        centerMass = new Vector(massInfluenceX / getMass(),
            massInfluenceY / getMass());
    }

    public void draw(Graphics g)
    {
        int cx = (int) centerMass.getX();
        int cy = (int) centerMass.getY();
        int radius = (int) Math.sqrt(getMass() / Math.PI);
        g.setColor(SYSTEM_COLOR);
        g.fillOval(cx - radius, cy - radius, radius + radius, radius + radius);
    }

    public Vector getPos()
    {
        return centerMass;
    }

    public Vector getVel()
    {
        return new Vector(0, 0);
    }

    public double getEnergy()
    {
        // The system itself has no energy, but the Entities within do
        return 0;
    }

    public double getTotalEnergy()
    {
        double totalEnergy = 0;
        for (int i = 0; i < system.size(); i++)
        {
            totalEnergy += system.get(i).getEnergy();
        }
        return totalEnergy;
    }

    public double getMass()
    {
        if (changed)
        {
            totalMass = 0;
            for (int i = 0; i < system.size(); i++)
            {
                totalMass += system.get(i).getMass();
            }
            changed = false;
        }

        return totalMass;
    }
}
