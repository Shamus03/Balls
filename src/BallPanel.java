import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BallPanel extends JPanel
{
    private static final int FRAME_SPEED = 10;
    private static final int TICK_DELAY = 1;
    private static final int BORDER_WIDTH = 3;

    private ArrayList<Entity> entities;
    private MassSystem system;
    private Rectangle bounds;
    private boolean running;
    private boolean drawSystem;
    private SceneSelector sceneSelector;
    private Vector gravity;
    private BallPanelControlPanel controlPanel;

    public BallPanel(int width, int height)
    {
        super();
        clearEntities();
        bounds = new Rectangle(BORDER_WIDTH, BORDER_WIDTH,
            width - BORDER_WIDTH * 2, height - BORDER_WIDTH * 2);
        drawSystem = false;
        sceneSelector = new SceneSelector(this);
        gravity = new Vector(0, 0);

        setPreferredSize(new Dimension(width, height));

        new Thread(new BallPanelDrawer()).start();
    }

    public void addEntity(Entity e)
    {
        entities.add(e);
        system.add(e);
    }

    public void clearEntities()
    {
        entities = new ArrayList<Entity>();
        system = new MassSystem();
    }

    public Rectangle getPhysicsBounds()
    {
        return bounds;
    }

    public void setDrawSystem(boolean b)
    {
        drawSystem = b;
    }

    public void setGravity(Vector g)
    {
        gravity = g;
    }

    public double getTotalEnergy()
    {
        double sum = 0;

        for (int i = entities.size() - 1; i >= 0; i--)
        {
            sum += entities.get(i).getEnergy();
        }

        return sum;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect( bounds.getMinX() - BORDER_WIDTH,
                    bounds.getMinY() - BORDER_WIDTH,
                    bounds.getWidth() + BORDER_WIDTH * 2,
                    bounds.getHeight() + BORDER_WIDTH * 2);
        g.setColor(Color.WHITE);
        g.fillRect(bounds.getMinX(), bounds.getMinY(),
            bounds.getWidth(), bounds.getHeight());

        for (int i = 0; i < entities.size(); i++)
        {
            entities.get(i).draw(g);
        }
        if (drawSystem)
        {
            system.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setXORMode(Color.WHITE);
        g.drawString("Total Energy: " + getTotalEnergy(),
            BORDER_WIDTH + 2, BORDER_WIDTH + 12);
        g.setPaintMode();
    }

    public void setControlPanel(BallPanelControlPanel p)
    {
        controlPanel = p;
    }

    public void tick()
    {
        Entity e;
        for (int i = entities.size() - 1; i >= 0; i--)
        {
            e = entities.get(i);
            e.addVel(gravity);
            e.tick();
            if (e instanceof Collidable)
            {
                ((Collidable) e).checkBounds(bounds);
            }
        }
        if (drawSystem)
        {
            system.tick();
        }

        checkCollisions();
    }

    public void checkCollisions()
    {
        int i;
        int j;
        Entity e1;
        Entity e2;
        Collidable c1;
        Collidable c2;
        for (i = entities.size() - 1; i > 0; i--)
        {
            e1 = entities.get(i);
            if (e1 instanceof Collidable)
            {
                c1 = (Collidable) e1;
                for (j = i - 1; j >= 0; j--)
                {
                    e2 = entities.get(j);
                    if (e2 instanceof Collidable)
                    {
                        c2 = (Collidable) e2;
                        c1.collide(c2);
                    }
                }
            }
        }
    }

    public void start()
    {
        if (!running)
        {
            running = true;
            new Thread(new BallPanelTicker()).start();
        }
        controlPanel.updateButtons();
    }

    public void stop()
    {
        running = false;
        controlPanel.updateButtons();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void chooseSceneFile()
    {
        sceneSelector.chooseSceneFile();
    }

    public void chooseSceneFile(String fileName)
    {
        sceneSelector.chooseSceneFile(fileName);
    }

    private class BallPanelTicker implements Runnable
    {
        public void run()
        {
            try
            {
                while (running)
                {
                    tick();
                    Thread.sleep(TICK_DELAY);
                }
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    private class BallPanelDrawer implements Runnable
    {
        public void run()
        {
            try
            {
                while (true)
                {
                    repaint();
                    Thread.sleep(FRAME_SPEED);
                }
            }
            catch (InterruptedException e)
            {
            }
        }
    }
}
