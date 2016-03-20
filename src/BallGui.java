import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BallGui extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private BallPanel ballPanel;

    public static void main(String[] args)
    {
        BallGui window = new BallGui();
        
        if (args.length > 0)
        {
            window.chooseSceneFile(args[0]);
        }
        else
        {
            window.chooseSceneFile();
        }
        window.setVisible(true);
    }

    public BallGui()
    {
        super("Ball GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        ballPanel = new BallPanel(WIDTH, HEIGHT);
        BallPanelControlPanel controlPanel =
            new BallPanelControlPanel(ballPanel);

        add(ballPanel);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        ballPanel.start();
        controlPanel.updateButtons();
    }

    public void chooseSceneFile()
    {
        ballPanel.chooseSceneFile();
    }

    public void chooseSceneFile(String fileName)
    {
        ballPanel.chooseSceneFile(fileName);
    }

    public void addEntity(Entity e)
    {
        ballPanel.addEntity(e);
    }
}
