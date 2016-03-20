import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BallPanelControlPanel extends JPanel
{
    private BallPanel ballPanel;
    private JButton startButton;
    private JButton stopButton;

    public BallPanelControlPanel(BallPanel ballPanel)
    {
        super();

        this.ballPanel = ballPanel;

    
        JButton loadButton = new JButton("Load");
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        JButton quitButton = new JButton("Quit");

        loadButton.addActionListener(new LoadButtonListener());
        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        quitButton.addActionListener(new QuitButtonListener());

        add(loadButton);
        add(startButton);
        add(stopButton);
        add(quitButton);
    }

    @Override
    public void repaint()
    {
        super.repaint();
        updateButtons();
    }

    public void updateButtons()
    {
        if (ballPanel != null)
        {
            boolean running = ballPanel.isRunning();
            startButton.setEnabled(!running);
            stopButton.setEnabled(running);
        }
    }

    private class LoadButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ballPanel.chooseSceneFile();
        }
    }

    private class StartButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ballPanel.start();
            updateButtons();
        }
    }

    private class StopButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ballPanel.stop();
            updateButtons();
        }
    }

    private class QuitButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
}
