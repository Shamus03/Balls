import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BallPanelControlPanel extends JPanel
{
    private BallPanel ballPanel;
    private JButton startButton;
    private JButton stopButton;

    public BallPanelControlPanel(BallPanel ballPanel)
    {
        super();

        this.ballPanel = ballPanel;

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new QuitButtonListener());

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

    private void updateButtons()
    {
        if (ballPanel != null)
        {
            boolean running = ballPanel.isRunning();
            startButton.setEnabled(!running);
            stopButton.setEnabled(running);
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
