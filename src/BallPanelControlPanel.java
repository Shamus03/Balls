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
    private JButton tickButton;

    public BallPanelControlPanel(BallPanel ballPanel)
    {
        super();

        this.ballPanel = ballPanel;

    
        JButton loadButton = new JButton("Load");
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        tickButton = new JButton("Tick");
        JButton quitButton = new JButton("Quit");

        loadButton.addActionListener(new LoadButtonListener());
        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        tickButton.addActionListener(new TickButtonListener());
        quitButton.addActionListener(new QuitButtonListener());

        add(loadButton);
        add(startButton);
        add(stopButton);
        add(tickButton);
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
            tickButton.setEnabled(!running);
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
        }
    }

    private class StopButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ballPanel.stop();
        }
    }

    private class TickButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ballPanel.tick();
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
