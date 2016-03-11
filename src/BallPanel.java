import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class BallPanel extends JPanel
{
    private static final int FRAME_SPEED = 10;
    public static final int BORDER_WIDTH = 5;

    private ArrayList<Ball> balls;
    private Rectangle bounds;
    private boolean running;

    public BallPanel(int width, int height)
    {
        super();
        balls = new ArrayList<Ball>();
        bounds = new Rectangle(BORDER_WIDTH, BORDER_WIDTH,
            width - BORDER_WIDTH * 2, height - BORDER_WIDTH * 2);
    }

    public void addBall(Ball b)
    {
        balls.add(b);
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
        g.setColor(Color.BLACK);

        Ball b;
        for (int i = 0; i < balls.size(); i++)
        {
            b = balls.get(i);
            if (b instanceof DrawableBall)
            {
                ((DrawableBall) b).draw(g);
            }
            else
            {
                new DrawableBall(b).draw(g);
            }
        }
    }

    public void tick(int delta)
    {
        Ball b;
        for (int i = balls.size() - 1; i >= 0; i--)
        {
            b = balls.get(i);
            b.tick(delta);
            b.checkBounds(bounds);
        }
    }

    public void checkCollisions()
    {
        int i;
        int j;
        for (i = balls.size() - 1; i > 0; i--)
        {
            for (j = i - 1; j >= 0; j--)
            {
                balls.get(i).collide(balls.get(j));
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
            while (running)
            {
                currentTime = System.currentTimeMillis();
                tick((int) (currentTime - lastTick));
                checkCollisions();
                lastTick = currentTime;
            }
        }
    }

    private class BallPanelDrawer implements Runnable
    {
        public void run()
        {
            long lastRedraw = System.currentTimeMillis();
            long currentTime;
            while (running)
            {
                currentTime = System.currentTimeMillis();

                if (currentTime - lastRedraw > FRAME_SPEED)
                {
                    repaint();
                    lastRedraw = currentTime;
                }
            }
        }
    }
}
