import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.util.Random;

public class BallGui extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static final int NUM_RANDOM_BALLS = 5;

    private static final int RADIUS_MIN = 10;
    private static final int RADIUS_MAX = 40;
    private static final double VEL_MAX = .1;

    private BallPanel ballPanel;

    public static void main(String[] args)
    {
        BallGui window = new BallGui();

        Random r = new Random();

        Ball b;
        double randR;
        double randX;
        double randY;
        double randVX;
        double randVY;
        for (int i = 0; i < NUM_RANDOM_BALLS; i++)
        {
            randR = r.nextInt(RADIUS_MAX - RADIUS_MIN) + RADIUS_MIN;
            randX = r.nextInt((int)
                (WIDTH - (randR + BallPanel.BORDER_WIDTH) * 2))
                + randR + BallPanel.BORDER_WIDTH ;
            randY = r.nextInt((int)
                (HEIGHT - (randR + BallPanel.BORDER_WIDTH) * 2))
                + randR + BallPanel.BORDER_WIDTH;
            randVX = VEL_MAX * (r.nextDouble() * 2 - 1);
            randVY = VEL_MAX * (r.nextDouble() * 2 - 1);

            b = new Ball(randX, randY, randR);
            b.setVel(new Vector(randVX, randVY));

            window.addBall(b);
        }

        SpringBall b1 = new SpringBall(300, 200, 50);
        SpringBall b2 = new SpringBall(400, 200, 50);
        SpringBall b3 = new SpringBall(450, 250, 50);

        double springConstant = .00000002;
        double springLength = 200;

        b1.setSpringTarget(b2);
        b1.setSpringConstant(springConstant);
        b1.setSpringLength(springLength);

        b2.setSpringTarget(b3);
        b2.setSpringConstant(springConstant);
        b2.setSpringLength(springLength);

        b3.setSpringTarget(b1);
        b3.setSpringConstant(springConstant);
        b3.setSpringLength(springLength);

        window.addBall(b1);
        window.addBall(b2);
        window.addBall(b3);

        window.setVisible(true);
    }

    public BallGui()
    {
        super("Ball GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        ballPanel = new BallPanel(WIDTH, HEIGHT);
        JPanel controlPanel = new BallPanelControlPanel(ballPanel);

        add(ballPanel);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        ballPanel.start();
    }

    public void addBall(Ball b)
    {
        if (b instanceof DrawableBall)
        {
            ballPanel.addBall((DrawableBall) b);
        }
        else
        {
            ballPanel.addBall(new DrawableBall(b));
        }
    }
}
