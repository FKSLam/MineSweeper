import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
/**
 * Write a description of class InvisibleButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InvisibleButton extends JButton implements ActionListener
{
    private static final String[] FILE_NAMES={"Images/flag.png", "Images/q.png"};
    private static final float ICON_SCALING=0.75f;
    private static BufferedImage[] iconImages;
    private static final char ICON_CYCLE=3;
    // instance variables - replace the example below with your own
    private static JPanel parentContainers;
    private static MineSweeper originalGame;
    private JButton currentButton;
    private volatile boolean alreadySelected;
    private int id;
    private int iconId;
    private boolean seen;
    private static class MouseDetection implements MouseListener
    {
        private InvisibleButton parentButton;
        public static final Color BUTTON_DEFAULT= new JButton().getBackground();
        public static volatile boolean rightButtonPressed;
        public static volatile boolean leftButtonPressed;
        private boolean registeredEnter;
        public MouseDetection()
        {
            rightButtonPressed=false;
        }
        public MouseDetection(InvisibleButton a)
        {
            parentButton=a;
            rightButtonPressed=false;
            registeredEnter=false;
        }
        public void mouseClicked(MouseEvent e)
        {
            
        }
        public void mousePressed (MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e))
            {
                leftButtonPressed=true;
              //  System.out.println("LEFT");
                registeredEnter=true;
            }
            else if (SwingUtilities.isRightMouseButton(e))
            {
              //  System.out.println("Right");
                rightButtonPressed=true;
                parentButton.shiftCycle();
            }
        }
       
        public void mouseReleased(MouseEvent e)
        {
            
            
            /*
            System.out.println("SDF");
               parentButton.getButton().setBackground(BUTTON_DEFAULT);
                parentButton.parentRepaint();
              if (SwingUtilities.isLeftMouseButton(e))
            {
                leftButtonPressed=false;
              
                
            }
            else if (SwingUtilities.isRightMouseButton(e))
            {
                rightButtonPressed=false;
            }*/
        }
        public void mouseEntered(MouseEvent e)
        {
            /*
            if (leftButtonPressed&&!parentButton.isSelected())
            {
                if (!registeredEnter)
                    System.out.println("ENTERED");
                registeredEnter=true;
                //parentButton.setInvisible();
                parentButton.getButton().setBackground(InvisibleButton.parentContainers.getBackground());
                parentButton.parentRepaint();
            }*/
        }
        
        public void mouseExited(MouseEvent e)
        {
            /*
            System.out.println("EXIT");
            if (leftButtonPressed&&registeredEnter&&!parentButton.isSelected())
            {

                //parentButton.setVisible();
                parentButton.getButton().setBackground(BUTTON_DEFAULT);
                parentButton.parentRepaint();
                
                registeredEnter=false;
            }*/
            
        }
    }
    public InvisibleButton(int id)
    {
        
        currentButton=new JButton();

        currentButton.addActionListener(this);
        //currentButton.setIcon(new ImageIcon(iconImages[0]));
        iconId=0;
        currentButton.setFocusPainted(false);
       // if (id==0)
            //System.out.println("IMAGE WIDTH: " + (iconImages[0]).getWidth());
        setVisible();
        alreadySelected=false;
        currentButton.addMouseListener( new MouseDetection(this));
        this.id=id;
        seen=true;
    }
    public void shiftCycle()
        {
            iconId=(iconId+1)%ICON_CYCLE;
           // System.out.println("ICON ID: "+ iconId);
            if (iconId-1>=0)
            {
                currentButton.setIcon(new ImageIcon(iconImages[iconId-1]));
                parentRepaint();
            }
            else currentButton.setIcon(null);
        }
    public static void initializePictures()
    {
        iconImages= new BufferedImage[FILE_NAMES.length];
        for (int a=0; a<FILE_NAMES.length; a++)
        {
            try
            {
                iconImages[a]=ImageIO.read(new File(FILE_NAMES[a]));
                // scale it
                
            }
            catch (Exception e)
            {
                System.out.println("InvisibleButton could not load a picture.");
            }
            
        }
    }
    public static void scaleIcons(int rows, int cols, int w, int h)
    {
        for (int a=0; a<FILE_NAMES.length; a++)
        {

            float wwidth=((ICON_SCALING*(w/cols)))/((float)iconImages[a].getWidth());
          //  if (a==0)
               // System.out.println("Scaling Width: " + wwidth);
            float hheight=((ICON_SCALING*(h/rows)))/((float)iconImages[a].getHeight());
            BufferedImage temp= new BufferedImage((int)(iconImages[a].getWidth()*wwidth),(int)(iconImages[a].getHeight()*hheight), iconImages[a].getType());
            Graphics2D gg=temp.createGraphics();
            AffineTransform cc=AffineTransform.getScaleInstance(wwidth,hheight);
            gg.drawRenderedImage(iconImages[a],cc);
           // System.out.println("FInal width "+ temp.getWidth());
            iconImages[a]=temp;
            
        }
        
    }
    public static void setParentContainer(JPanel parent)
    {
       
        parentContainers=parent;
    }
    public JButton getButton()
    {
        return currentButton;
    }
    public void setInvisible()
    {
        currentButton.setVisible(false);
        currentButton.setOpaque(false);
        seen=false;
    }
    public void setVisible()
    {
        currentButton.setVisible(true);
        currentButton.setOpaque(true);
        seen=true;
    }
    public boolean isSelected()
    {
        return alreadySelected;
    }
    public InvisibleButton(int width, int height, boolean opaque)
    {
      currentButton = new JButton();
      currentButton.setPreferredSize( new Dimension (width, height));
      alreadySelected=false;
      setVisible();
      currentButton.addActionListener(this);
    //currentButton.addMouseListener( new MouseDetection(this));
    }
    public boolean isSeen()
    {
        return seen;
    }
    private void parentRepaint()
    {
        if (parentContainers!=null)
            parentContainers.repaint();
    }
  
    /**
     * Constructor for objects of class InvisibleButton
     */
    public void actionPerformed (ActionEvent e)
    {
        if (MineSweeper.finishedGame) return;
        alreadySelected=true;
        //setInvisible();
       // System.out.println("FS");
       parentRepaint();
       MineSweeper.processUserInput(id);
    }
}
