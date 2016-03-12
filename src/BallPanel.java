import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class BallPanel extends JPanel
{
    private static final int FRAME_SPEED = 10;
    private static final int MIN_DELTA = 3;

    protected static final int BORDER_WIDTH = 3;

    private ArrayList<Entity> entities;
    private Rectangle bounds;
    private boolean running;

    public BallPanel(int width, int height)
    {
        super();
        entities = new ArrayList<Entity>();
        bounds = new Rectangle(BORDER_WIDTH, BORDER_WIDTH,
            width - BORDER_WIDTH * 2, height - BORDER_WIDTH * 2);

        setPreferredSize(new Dimension(width, height));
    }

    public void addEntity(Entity e)
    {
        entities.add(e);
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

        g.setColor(Color.BLACK);
        g.setXORMode(Color.WHITE);
        g.drawString("Total Energy: " + getTotalEnergy(),
            BORDER_WIDTH + 2, BORDER_WIDTH + 12);
        g.setPaintMode();
    }

    public void tick(int delta)
    {
        Entity e;
        for (int i = entities.size() - 1; i >= 0; i--)
        {
            e = entities.get(i);
            e.tick(delta);
            if (e instanceof Collidable)
            {
                ((Collidable) e).checkBounds(bounds);
            }
        }
    }

    public void start()
    {
        if (!running)
        {
            running = true;
            new Thread(new BallPanelTicker()).start();
            new Thread(new BallPanelDrawer()).start();
        }
    }

    public void stop()
    {
        running = false;
    }

    public boolean isRunning()
    {
        return running;
    }

    private class BallPanelTicker implements Runnable
    {
        public void run()
        {
            long lastTick = System.currentTimeMillis();
            long currentTime;
            int delta;
            try
            {
                while (running)
                {
                    currentTime = System.currentTimeMillis();
                    delta = (int) (currentTime - lastTick);
                    if (delta >= MIN_DELTA)
                    {
                        tick(delta);
                        checkCollisions();
                        lastTick = currentTime;
                    }
                    Thread.sleep(1);
                }
            }
            catch (InterruptedException e)
            {
            }
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

    }

    private class BallPanelDrawer implements Runnable
    {
        public void run()
        {
            try
            {
                while (running)
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
