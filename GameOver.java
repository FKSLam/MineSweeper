import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Write a description of class GameOver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameOver implements ActionListener
{
    private static MineSweeper originalGame;
    public static final int DEFAULT_WIDTH=350;
    public static final int DEFAULT_HEIGHT=125;
    // instance variables - replace the example below with your own
    public static final String VICTORY_MESSAGE="Congratulations, you've won with a time of ";
    public static final String LOSS_MESSAGE="Sorry, you lost!";
    private static final String PLAY_AGAIN="Would you like to play again?";
    private JFrame windows;
    private JPanel firstMessage;
    private JPanel secondMessage;
    private JPanel thirdPanel;
    private JLabel firstt;
    private JLabel secondd;
    private JButton restartGame;
    private JButton exitGame;

    /**
     * Constructor for objects of class GameOver
     */
    public GameOver()
    {
        // initialise instance variables
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        windows.setVisible(false);
        if (e.getSource()==restartGame)
        {
            
            MineSweeper.playAgain();
        }
        else
        {
            MineSweeper.endEverything();
            windows.dispose();
            System.exit(0);
        }
        return;
    }
    public void setString (boolean wonGame, int seconds)
    {
         String total=LOSS_MESSAGE;
        if (wonGame)
        {
            total=VICTORY_MESSAGE+String.valueOf(seconds)+" seconds!";
        }
        firstt.setText(total);
        windows.setVisible(true);
    }
    public GameOver(boolean wonGame, int seconds)
    {
        windows=new JFrame("Game Over");
        windows.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        Container contentPane = windows.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
        firstMessage= new JPanel();
        secondMessage= new JPanel();
        //secondMessage.setLayout(new BorderLayout());
       // firstMessage.setLayout(new BorderLayout());
        JLabel temp= new JLabel();
        String total=LOSS_MESSAGE;
        if (wonGame)
        {
            total=VICTORY_MESSAGE+String.valueOf(seconds)+" seconds!";
        }
        firstt=new JLabel(total);
        secondd=new JLabel(PLAY_AGAIN);
       
        firstMessage.add(firstt);
        secondMessage.add(secondd);
         firstt.setAlignmentX(JComponent.CENTER_ALIGNMENT);
         firstt.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        secondd.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        thirdPanel= new JPanel();
        restartGame= new JButton("Yes");
        exitGame= new JButton("No");
        restartGame.addActionListener(this);
        exitGame.addActionListener(this);
        thirdPanel.add(restartGame);
        thirdPanel.add(Box.createHorizontalGlue());
        thirdPanel.add(exitGame);
        
        //secondd.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        contentPane.add(firstMessage);
        contentPane.add(secondMessage);
        contentPane.add(thirdPanel);
        windows.pack();
        windows.setVisible(true);

    }
}
