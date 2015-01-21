import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;
import java.awt.event.*;
/**
 * Self implemented version of the classic MineSweeper Game
 * 
 * @author Felix Lam
 * @version 1
 */
public class MineSweeper 
{
    // instance variables - replace the example below with your own
    public static  int windowHeight=500;
    public static  int windowWidth=500;
    public static final int MINE_ID=9;
    public static final int INFO_BAR_FACTOR=8;
    public static final int INVISIBLE_MARGIN_FACTOR=16;
    public static final int GRID_HEIGHT=300;
    public static final int GRID_WIDTH=300;
    public static final int DEFAULT_ROWS=9;
    public static final int DEFAULT_COLS=9;
    public static final int DEFAULT_MINE_NUMBER=10;
    public static final int SECOND_COUNTER=1000;
    public static final int TIMER_WIDTH=80;
    public static final int TIMER_HEIGHT=50;
    
    private static int totalMines;
    private static JFrame mainWindow= new JFrame("Minesweeper");
    private static JPanel mineField;
    private static GridPanel grid;
    private static JPanel infoBar;
    private static JMenuBar menuBar;
    private static final JMenu[] MENUS ={new JMenu("Game")};
    private static final JMenuItem[][] MENU_ITEMS={{new JMenuItem("New Game"), new JMenuItem("Options")}};
    private static int rowsOfMines;
    private static int colsOfMines;
    private static JPanel topMargin;
    private static InvisibleButton[] gridButtons;
    private static JButton[] buttons;
    private static char[][] generatedGame;
    private static boolean firstMove;
    private static Random numberGenerator;
    private static boolean[][] visited;
    public static volatile boolean finishedGame;
    private static int blocksRemaining;
    private static boolean firstGame;
    private static Timer gameTimer;
    private static GameOver endFrame;
    private static JLabel timeSpent;
    private static volatile int timeKeeper;
    private static volatile JLabel minesRemaining;
    /**
     * Constructor for objects of class MineSweeper
     */
    public MineSweeper()
    {
        // initialise instance variables
   
       
    }
    /**
     * Initialize the window menu bar
     */
    private static void initializeMenu()
    {
        //final String[] menus= {"Game"};
        //final String[][] menuItems = {{"New Game", "Options"}};
        menuBar = new JMenuBar();
        for (int a=0; a<MENUS.length; a++)
        {
            
            for (int b=0; b<MENU_ITEMS[a].length; b++)
            {
               MENUS[a].add(MENU_ITEMS[a][b]);
            }
            menuBar.add(MENUS[a]);
        }
        
    }
    public static void processUserInput(int buttonSource)
    {
        //System.out.println("PRESSED BUTTON: " + buttonSource);
        int row=buttonSource/(colsOfMines);
            int col=buttonSource%colsOfMines;
            
        if (firstMove)
        {
            firstMove=false;
            gameTimer.start();
            timePass();
            if (generatedGame[row][col]>=9)
            {
                // dont let first action result in hitting a mine
                for (int bb=row-1; bb<rowsOfMines&&bb<row+2; bb++)
                {
                    if (bb<0) continue;
                    for (int cc=col-1; cc<colsOfMines&&cc<col+2; cc++)
                    {
                        if (cc<0) continue;
                        if (bb!=row||cc!=col)
                            generatedGame[bb][cc]--;
                    }
                }
                generateMine(totalMines);
                generatedGame[row][col]-=MINE_ID;
                //initializeMineField(rowsOfMines,colsOfMines);
                grid.initializePanel(rowsOfMines,colsOfMines,generatedGame);
                
                
                //grid.repaint();
                
            }

        }
        gridButtons[buttonSource].setInvisible();
        blocksRemaining--;
        if (generatedGame[row][col]>=9)
        {
            gameTimer.stop();
            for (int aa=0; aa<rowsOfMines; aa++)
            {
                for (int gg=0; gg<colsOfMines; gg++)
                {
                    if ((aa==row)&&(gg==col)) continue;
                    if (generatedGame[aa][gg]>=9)gridButtons[aa*colsOfMines+gg].setInvisible();
                }
            }
            finishedGame=true;
            grid.setGameOver(row,col);
            grid.repaint();
            if (endFrame==null)
                endFrame= new GameOver(false,-1);
            else endFrame.setString(false,-1);
            //initializeGame();
            //System.out.println("RETURNING");
            return;
            
        }
        
        
        for (int a=0;a<rowsOfMines; a++)
        {
            for (int b=0; b<colsOfMines; b++)
            {
                visited[a][b]=false;
            }
            
        }
        dfs(row,col);
        if (blocksRemaining<=totalMines)
        {
            //System.out.println("gameover-victory");
            gameTimer.stop();
            finishedGame=true;
            if (endFrame==null)
                endFrame= new GameOver(true,timeKeeper);
            else endFrame.setString(true,timeKeeper);
            return;
        }
        //System.out.println("Blocks Remaining: " + blocksRemaining);
        grid.repaint();
    }
    public static void playAgain()
        {
            initializeGame();
        }
    public static void endEverything()
    {
        System.exit(0);
    }
    public static void dfs(int row, int col)
    {
        
        if (row<0||row>=rowsOfMines) return;
        if (col<0||col>=colsOfMines) return;
        if (visited[row][col]) return;
        visited[row][col]=true;
        
        if (generatedGame[row][col]!=0) 
        {
            //blocksRemaining--;
            return;
        }
        gridButtons[row*colsOfMines+(col)].setInvisible();

        for (int cc=row-1; cc<rowsOfMines&&cc<row+2;cc++)
        {
            if (cc<0) continue;
            for (int dd=col-1;dd<colsOfMines&&dd<col+2;dd++)
            {
                if (dd<0) continue;
                int tt=cc*colsOfMines+(dd);
                if (gridButtons[tt].isSeen())
                    blocksRemaining--;
                gridButtons[tt].setInvisible();

                if (generatedGame[cc][dd]==0) dfs(cc,dd);
            }
        }
        if (generatedGame[row][col]==0)
        {
            dfs(row+1,col);
            dfs(row-1,col);
            dfs(row,col+1);
            dfs(row,col-1);
        }
    }
    private static void initializeMineField(int rows,int cols)
    {
        mineField= new JPanel();
        mineField.setBackground(Color.GRAY);
        
        grid= new GridPanel(rowsOfMines,colsOfMines,generatedGame);
        /*
        System.out.println("Current Grid Field");
        for (int a=0; a<rowsOfMines; a++)
        {
            for (int b=0; b<colsOfMines; b++)
            {
                System.out.print((int)generatedGame[a][b]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();*/
        //grid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //grid.setBorder(BorderFactory.createLoweredBevelBorder());
        int newWidth=GRID_WIDTH+(colsOfMines-(GRID_WIDTH%colsOfMines));
        int newHeight=GRID_HEIGHT+(rowsOfMines-(GRID_HEIGHT%rowsOfMines));
        grid.setPreferredSize( new Dimension(newWidth,newHeight));
        InvisibleButton.scaleIcons(rowsOfMines,colsOfMines,newWidth,newHeight);
        grid.setBackground(new Color(230,230,230));
        grid.setOpaque(true);
        gridButtons= new InvisibleButton[rows*cols];
        buttons= new JButton[rows*cols];
        GridLayout gl= new GridLayout(rows,cols);
        grid.setLayout(gl);
        //System.out.println(mineField.getSize().width);
        
        
        InvisibleButton.setParentContainer(grid);
            
        for (int a=0; a<rows*cols; a++)
        {
            gridButtons[a]=new InvisibleButton(a);
            //buttons[a] = new JButton();
            //buttons[a].setVisible(false);
            //grid.add(buttons[a]);
            grid.add(gridButtons[a].getButton());
        }

        topMargin= new JPanel();
        topMargin.setBackground(Color.RED);
        topMargin.setPreferredSize(new Dimension(windowWidth,windowHeight/INVISIBLE_MARGIN_FACTOR));
        topMargin.setOpaque(false);
        mineField.add(topMargin,BorderLayout.PAGE_START);
        mineField.add(grid,BorderLayout.CENTER);
        
        
        mineField.setOpaque(true);
        
    }
    private static void initializeInfoPanel()
    {
        infoBar= new JPanel();
        infoBar.setBackground(Color.GRAY);
        infoBar.setOpaque(true);
        infoBar.setPreferredSize( new Dimension (windowWidth,windowHeight/INFO_BAR_FACTOR));
        infoBar.add(timeSpent);
    }
    private static void generateMine(int mineNumber)
    {
         int positionsAvailable=rowsOfMines*colsOfMines;
        
            int temp=numberGenerator.nextInt(positionsAvailable-mineNumber);
            //System.out.println("NUMBER GENERATED: "+temp);
            for (int b=0; b<rowsOfMines; b++)
            {
                for (int c=0; c<colsOfMines; c++)
                {
                    if (generatedGame[b][c]>=9) continue;
                    temp--;
                    if (temp<0)
                    {
                        generatedGame[b][c]+=MINE_ID;
                        
                        for (int bb=b-1; bb<rowsOfMines&&bb<b+2; bb++)
                        {
                            if (bb<0) continue;
                            for (int cc=c-1; cc<colsOfMines&&cc<c+2; cc++)
                            {
                                if (cc<0) continue;
                                if (bb!=b||cc!=c)
                                    generatedGame[bb][cc]++;
                            }
                        }
                        break;
                    }
                    
                }
                if (temp<0) break;
            }
        
    }    
    private static void initializeWindow()
    {
        
        
        initializeMenu();
        initializeMineField(rowsOfMines,colsOfMines);
        initializeInfoPanel();
        mainWindow.setPreferredSize ( new Dimension (windowWidth,windowHeight));
        
        Container content = mainWindow.getContentPane();
       // mainWindow.setJMenuBar(menuBar);
        content.add(mineField,BorderLayout.CENTER);
        content.add(infoBar,BorderLayout.PAGE_END);
        
    }
    private static void initializeGame()
    {
       // System.out.println("Initializing game");
        mainWindow.getContentPane().removeAll();
        finishedGame=false;
        timeKeeper=0;
        if (timeSpent==null)
        {
            timeSpent= new JLabel("0", SwingConstants.CENTER);
            timeSpent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            timeSpent.setFont(new Font("Serif", Font.PLAIN,28));
            timeSpent.setBackground(new Color(230,230,230));
            timeSpent.setOpaque(true);
            timeSpent.setPreferredSize(new Dimension(TIMER_WIDTH,TIMER_HEIGHT));
            timeSpent.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        }
        else timeSpent.setText("0");

        firstMove=true;
        generatedGame= new char[rowsOfMines][colsOfMines];
        blocksRemaining=rowsOfMines*colsOfMines;
        //System.out.println("BLocks remaining: " + blocksRemaining);
        visited=new boolean[rowsOfMines][colsOfMines];
        for (int a=0; a<rowsOfMines; a++)
        {
            for (int b=0; b<colsOfMines; b++)
            {
                generatedGame[a][b]=0;
            }
        }
        // generate mines
        int positionsAvailable=rowsOfMines*colsOfMines;
        for (int a=0;a<totalMines; a++)
        {
            generateMine(a);
            /*
            int temp=numberGenerator(positionsAvailable-a);
            for (int b=0; b<rowsOfMines; b++)
            {
                for (int c=0; c<colsOfMines; c++)
                {
                    if (generatedGame[b][c]>=9) continue;
                    temp--;
                    if (temp<0)
                    {
                        generatedGame[b][c]+=9;
                        for (int bb=b-1; bb<rowsOfMines&&bb<b+2; bb++)
                        {
                            if (bb<0) continue;
                            for (int cc=c-1; cc<colsOfMines&&cc<c+2; cc++)
                            {
                                if (cc<0) continue;
                                if (bb!=b||cc!=c)
                                    generatedGame[bb][cc]++;
                            }
                        }
                    }
                    
                }
            }*/
        }
        initializeWindow();
         mainWindow.pack();
         mainWindow.repaint();
         mainWindow.setVisible(true);
        
    }
    public static void timePass()
    {
        timeKeeper++;
        timeSpent.setText(String.valueOf(timeKeeper));
    }
    public static void main (String args[])
    {
        endFrame=null;
        gameTimer= new Timer (SECOND_COUNTER,new GameTimerAction());
        timeSpent=null;
        //GameOver.setParentGame(this);
        InvisibleButton.initializePictures();
                mainWindow= new JFrame("Minesweeper");
       numberGenerator= new Random();

        totalMines=DEFAULT_MINE_NUMBER;
         rowsOfMines=DEFAULT_ROWS;
        colsOfMines=DEFAULT_COLS;
        initializeGame();
       // initializeWindow();
       
       
        
    }
   
}
