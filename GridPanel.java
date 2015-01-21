import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.*;
/**
 * Write a description of class GridPanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GridPanel extends JPanel
{
    // instance variables - replace the example below with your own
    public static final String FILES_TO_LOAD[]={"Images/1.png","Images/2.png","Images/3.png","Images/4.png","Images/5.png","Images/6.png","Images/7.png","Images/8.png","Images/mine.png","Images/rmine.png"};
    
    private boolean gameOver;
    private int rowSelected;
    private int colSelected;
    private int width;
    private int height;
    private int rowsOfMines;
    private int colsOfMines;
    private char [][] minefield;
    private int firstDraw;
    // 0-8 are mine pics, 9 is mine
    private BufferedImage[] fieldImages;
    /**
     * Constructor for objects of class GridPanel
     */
    public GridPanel()
    {
        // initialise instance variables
        super();
        loadImages();
        minefield=null;
        gameOver=false;
        firstDraw=0;
    }
    public GridPanel(int rows,int cols, char[][] newMine)
    {
        super();
        loadImages();
        gameOver=false;
        firstDraw=0;
        initializePanel(rows,cols,newMine);
    }
  
    public void initializePanel(int rows,int cols, char[][] newMine)
    {
        rowsOfMines=rows;
        colsOfMines=cols;
        minefield= new char[rows][cols];
        for (int a=0; a<rows; a++)
        {
            for (int b=0; b<cols; b++)
            {
                minefield[a][b]=newMine[a][b];
            }
        }
        // System.out.println("Current Grid Field");
        /*
        for (int a=0; a<rowsOfMines; a++)
        {
            for (int b=0; b<colsOfMines; b++)
            {
                System.out.print((int)minefield[a][b]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();*/
    }
    public void setGameOver(int r,int c)
    {
        gameOver=true;
        rowSelected=r;
        colSelected=c;
    }
    private void loadImages()
    {
        fieldImages=new BufferedImage[FILES_TO_LOAD.length];
        for (int a=0; a<FILES_TO_LOAD.length; a++)
        {
            try
            {
                fieldImages[a]= ImageIO.read(new File(FILES_TO_LOAD[a]));
            }
            catch (Exception e)
            {
                System.out.println("Could not load an image");
            }
        }

    }
    public void setPreferredSize(Dimension d)
    {
        super.setPreferredSize(d);
        width=d.width;
        height=d.height;
    }
    private BufferedImage scalePicture(BufferedImage originalImage,int types,int width, int height,float scaledWidth,float scaledHeight)
    {
        BufferedImage scaledPicture=null;
        if (originalImage!=null)
        {
            scaledPicture= new BufferedImage(width,height,types);
            Graphics2D drawnPic=scaledPicture.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(scaledWidth,scaledHeight);
            drawnPic.drawRenderedImage(originalImage,at);
        }
        return scaledPicture;
    }
    private void drawMineField(Graphics g)
    {
        int gg=0;
        //for (int a=0; a<fieldImages.length; a++)
        for (int a=0; a<rowsOfMines; a++)
        {
            for (int b=0; b<colsOfMines; b++)
            {
                int id=(int)minefield[a][b];
                if (id>=9) id=9;
                id--;
                if (id<0) continue;
                float wwidth=fieldImages[id].getWidth();
                float hheight=fieldImages[id].getHeight();
                BufferedImage toDraw=scalePicture(fieldImages[id],fieldImages[id].getType(),fieldImages[id].getWidth(),fieldImages[id].getHeight(),((float)(width/colsOfMines))/wwidth,((float)(height/rowsOfMines))/hheight);
                g.drawImage(toDraw,b*(width/colsOfMines)+1,a*(height/rowsOfMines),null);
               
            }
        }
        
            
        
    }
    private void setNewGame()
    {
        gameOver=false;
    }
    private void drawRedMine(Graphics g)
    {
        BufferedImage original=fieldImages[9];
        float wwidth=original.getWidth();
        float hheight=original.getHeight();
        BufferedImage toDraw=scalePicture(original,original.getType(),original.getWidth(),original.getHeight(),((float)(width/colsOfMines))/wwidth,((float)(height/rowsOfMines))/hheight);
      //  System.out.println("Drawing red mine");
        g.drawImage(toDraw,colSelected*(width/colsOfMines)+1,rowSelected*(height/rowsOfMines),null);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
      
      //  System.out.println("GRID PANEL REPAINT called");
        g.setColor(Color.BLACK);
        for (int a=0; a<=width-width/colsOfMines; a+=width/colsOfMines)
        {
            g.drawLine(a,0,a,height);
        }
        g.drawLine(width-1,0,width-1,height);
        for (int a=0; a<=height-height/rowsOfMines; a+=height/rowsOfMines)
        {
            g.drawLine(0,a,width,a);
        }
        g.drawLine(0,height-1,width,height-1);
        firstDraw++;
        
        drawMineField(g);
         if (gameOver)
            drawRedMine(g);
        
        
    }
    
}
