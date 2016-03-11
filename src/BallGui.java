import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.util.Random;

public class BallGui extends JFrame
{
    protected static final int WIDTH = 800;
    protected static final int HEIGHT = 600;
    protected static final int BOTTOM_PANEL_PADDING = 36;

    private static final int NUM_RANDOM_BALLS = 30;

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
            randX = r.nextInt((int) (WIDTH - (randR + BallPanel.BORDER_WIDTH) * 2)) + randR + BallPanel.BORDER_WIDTH ;
            randY = r.nextInt((int) (HEIGHT - (randR + BallPanel.BORDER_WIDTH) * 2)) + randR + BallPanel.BORDER_WIDTH;
            randVX = VEL_MAX * (r.nextDouble() * 2 - 1);
            randVY = VEL_MAX * (r.nextDouble() * 2 - 1);

            b = new Ball(randX, randY, randR);
            b.setVel(new Vector(randVX, randVY));

            window.addBall(b);
        }

        window.addBall(new TextBall(100, 0, 50, "Shamus"));

        window.setVisible(true);
    }

    public BallGui()
    {
        super("Ball GUI");

        setSize(WIDTH, HEIGHT + BOTTOM_PANEL_PADDING);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ballPanel = new BallPanel(WIDTH, HEIGHT);
        JPanel controlPanel = new BallPanelControlPanel(ballPanel);

        add(ballPanel);
        add(controlPanel, BorderLayout.SOUTH);

        ballPanel.start();
    }

    public void addBall(Ball b)
    {
        ballPanel.addBall(b);
    }
}
