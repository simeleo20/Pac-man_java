package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.Map;


public class GamePanel extends JPanel implements Runnable
{
    //SCREEN SETTINGS
    final int originalTileSize =13;
    final int scale = 3;

    final public int tileSize = originalTileSize*scale; 
    final public int maxScreenCol = 14;
    final public int maxScreenRow = 18;
    final int screenWidth = tileSize*maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    //FPS
    int FPS = 60;


    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Map map = new Map(this);
    Player player = new Player(this, keyH,map);


    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
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
        player.update();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        map.draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}
