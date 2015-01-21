import java.awt.event.*;
/**
 * Write a description of class GameTimerAction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameTimerAction implements ActionListener
{
    public GameTimerAction()
    {
        
    }
    public void actionPerformed(ActionEvent e)
    {
        MineSweeper.timePass();
    }
}
