package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import entity.Ghost;
import entity.PacDot;
import entity.Player;
import tile.Map;
import utilities.AStar;
import utilities.Node;


public class GamePanel extends JPanel implements Runnable
{
    //SCREEN SETTINGS
    final int originalTileSize =16;
    final public double scale = 1.5;

    final public int tileSize = (int)((double)originalTileSize*scale);
    final public int maxScreenCol = 23;
    final public int maxScreenRow = 25;
    final public int screenWidth = tileSize*maxScreenCol;
    final public int screenHeight = tileSize * maxScreenRow;

    //FPS
    int FPS = 60;


    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Map map = new Map(this);
    AStar aStar = new AStar(maxScreenRow, maxScreenCol, new Node(1, 1), new Node(2, 1),map.intMap);
    EntityHandler eh = new EntityHandler(this,keyH,map,aStar);



    public GamePanel()
    {

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
//        aStar.setInitialNode(new Node(7,3));
//        aStar.setFinalNode(new Node(10,1));
//        List<Node> path = aStar.findPath();
//        System.out.println(path);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run()
    {
        double drawIntervall = 1000000000/FPS;
        double delta=0;
        long  lastTime = System.nanoTime();
        long currentTime;
        long timer=0;
        long drawCount=0;

        while(gameThread!=null)
        {
            currentTime = System.nanoTime();

            delta += (currentTime-lastTime) /drawIntervall;
            timer += (currentTime-lastTime);
            lastTime = currentTime;

            if(delta >= 1)
            {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000)
            {
                System.out.println("FPS:"+drawCount);
                drawCount=0;
                timer=0;
            }
            
        }
    }
    public void update()
    {
        eh.update();

    }
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        map.draw(g2);
        eh.draw(g2);

        g2.dispose();
    }
    public void restart()
    {
        eh = new EntityHandler(this,keyH,map,aStar);
    }
}
