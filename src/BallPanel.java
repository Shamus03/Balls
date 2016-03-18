import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;

public class BallPanel extends JPanel
{
    private static final char COMMENT_CHARACTER = '#';

    private static final int FRAME_SPEED = 10;
    private static final int MIN_DELTA = 3;

    protected static final int BORDER_WIDTH = 3;

    private ArrayList<Entity> entities;
    private Rectangle bounds;
    private boolean running;

    private Random generator;

    public BallPanel(int width, int height)
    {
        super();
        entities = new ArrayList<Entity>();
        bounds = new Rectangle(BORDER_WIDTH, BORDER_WIDTH,
            width - BORDER_WIDTH * 2, height - BORDER_WIDTH * 2);
        generator = new Random();

        setPreferredSize(new Dimension(width, height));
    }

    public void addEntity(Entity e)
    {
        entities.add(e);
    }

    public void clearEntities()
    {
        entities = new ArrayList<Entity>();
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

    public void chooseSceneFile()
    {
        JFileChooser chooser =
            new JFileChooser(new File(System.getProperty("user.dir")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Ball Scene Files", "ballscene");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            loadSceneFromFile(chooser.getSelectedFile());
        }
    }

    public void chooseSceneFile(String fileName)
    {
        loadSceneFromFile(new File(fileName));
    }

    public void loadSceneFromFile(File inFile)
    {
        clearEntities();
        try (Scanner scan = new Scanner(inFile))
        {
            String line = "";
            int repeat = 0;
            String[] words;
            while (scan.hasNext() || repeat > 0)
            {
                if (repeat <= 0)
                {
                    line = scan.nextLine().trim();
                }
                else
                {
                    repeat--;
                }

                if (line.length() > 0 && line.charAt(0) != COMMENT_CHARACTER)
                {
                    System.out.println(line);
                    words = line.split(" ");

                    if (words.length > 0)
                    {
                        if (words[0].equals("repeat") && words.length > 1)
                        {
                            if (repeat > 0) // Repeat calls itself
                            {
                                throw new SceneLoadException("Nested repeat");
                            }

                            try 
                            {
                                repeat = Integer.parseInt(words[1]);
                                if (scan.hasNext())
                                {
                                    line = scan.nextLine().trim();
                                }
                                else
                                {
                                    throw new
                                        SceneLoadException("Invalid repeat");
                                }
                            }
                            catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else if (words[0].equals("randomBall")
                            && words.length > 3)
                        {
                            addRandomBall(words);
                        }
                        else if (words[0].equals("ball") && words.length > 5)
                        {
                            addBall(words);
                        }
                        else if (words[0].equals("seed") && words.length > 1)
                        {
                            try
                            {
                                generator =
                                    new Random(Long.parseLong(words[1]));
                            }
                            catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void addRandomBall(String[] words)
    {
        try
        {
            int minRadius = Integer.parseInt(words[1]);
            int maxRadius = Integer.parseInt(words[2]);
            double maxVel = Double.parseDouble(words[3]);

            int radius = generator.nextInt(maxRadius - minRadius)
                + minRadius;

            int xPos =
                generator.nextInt(bounds.getWidth() - radius * 2)
                + bounds.getMinX() + radius;

            int yPos =
                generator.nextInt(bounds.getHeight() - radius * 2)
                + bounds.getMinY() + radius;

            Vector position = new Vector(xPos, yPos);

            double vel = generator.nextDouble() * maxVel;
            double angle = generator.nextDouble() * Math.PI * 2;

            Vector velocity = new Vector(
                vel * Math.cos(angle),
                vel * Math.sin(angle));

            Ball b = new Ball(position, velocity, radius);
            addEntity(b);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    private void addBall(String[] words)
    {
        try
        {
            double xPos = Double.parseDouble(words[1]);
            double yPos = Double.parseDouble(words[2]);
            double xVel = Double.parseDouble(words[3]);
            double yVel = Double.parseDouble(words[4]);
            double radius = Double.parseDouble(words[5]);

            Ball b = new Ball(new Vector(xPos, yPos),
                              new Vector(xVel, yVel),
                              radius);
            addEntity(b);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
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
