import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SceneSelector
{
    private static final char COMMENT_CHARACTER = '#';

    private BallPanel panel;
    private Random generator;

    public SceneSelector(BallPanel panel)
    {
        this.panel = panel;
        generator = new Random();
    }

    public void chooseSceneFile()
    {
        JFileChooser chooser =
            new JFileChooser(new File(System.getProperty("user.dir")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Ball Scene Files", "ballscene");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(panel);
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
        panel.clearEntities();
        try (Scanner scan = new Scanner(inFile))
        {
            Map<String, Entity> namedEntities = new Map<String, Entity>();

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
                            addBall(words, namedEntities);
                        }
                        else if (words[0].equals("spring") && words.length > 3)
                        {
                            addSpring(words, namedEntities);
                        }
                        else if (words[0].equals("system"))
                        {
                            addSystem(words, namedEntities);
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
                        else if (words[0].equals("drawSystem")
                                && words.length > 1)
                        {
                            panel.setDrawSystem(Boolean.parseBoolean(words[1]));
                        }
                        else
                        {
                            System.out.println(words[0]
                                + " is not a valid scene parameter.");
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
                generator.nextInt(panel.getPhysicsBounds().getWidth()
                - radius * 2) + panel.getPhysicsBounds().getMinX() + radius;

            int yPos =
                generator.nextInt(panel.getPhysicsBounds().getHeight()
                - radius * 2) + panel.getPhysicsBounds().getMinY() + radius;

            Vector position = new Vector(xPos, yPos);

            double vel = generator.nextDouble() * maxVel;
            double angle = generator.nextDouble() * Math.PI * 2;

            Vector velocity = new Vector(
                vel * Math.cos(angle),
                vel * Math.sin(angle));

            Ball b = new Ball(position, velocity, radius);
            panel.addEntity(b);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    private void addBall(String[] words, Map namedEntities)
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
            panel.addEntity(b);

            if (words.length > 6)
            {
                namedEntities.put(words[6], b);
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    private void addSpring(String[] words, Map namedEntities)
    {
        if (namedEntities.containsKey(words[1])
            && namedEntities.containsKey(words[2]))
        {
            Entity e1 = (Entity) namedEntities.get(words[1]);
            Entity e2 = (Entity) namedEntities.get(words[2]);
            Spring spring;

            try
            {
                double springConstant =
                    Double.parseDouble(words[3]);

                if (words.length > 4)
                {
                    double springLength =
                        Double.parseDouble(words[4]);
                    spring = new Spring(e1, e2,
                        springConstant, springLength);
                }
                else
                {
                    spring = new Spring(e1, e2,
                        springConstant);
                }

                panel.addEntity(spring);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Named entities " + words[1]
                + " and " + words[2] + " do not exist");
        }
    }

    private void addSystem(String[] words, Map namedEntities)
    {
        MassSystem system = new MassSystem();
        for (int i = 1; i < words.length; i++)
        {
            if (namedEntities.containsKey(words[i]))
            {
                system.add((Entity) namedEntities.get(words[i]));
            }
            else
            {
                System.out.println("No entity with name " + words[i]);
            }
        }
        panel.addEntity(system);
    }
}
