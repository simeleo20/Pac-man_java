package main;

import animation.Animation;
import entity.*;
import tile.Map;
import utilities.AStar;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class EntityHandler
{

    private Animation animation;
    private GamePanel gp;
    private KeyHandler keyH;
    private Map map;
    private AStar ast;
    public Player player;
    Blinky blinky;
    Pinky pinky;
    Clyde clyde;
    Inky inky;
    List<PacDot> pacDots;
    public int life;
    private boolean restartCalled;
    public EntityHandler(GamePanel gp, KeyHandler keyH, Map map, AStar ast)
    {
        this.gp = gp;
        this.keyH = keyH;
        this.map = map;
        this.ast = ast;
        life=3;
        defaultValue();
        pacDots = new LinkedList<>();
        fillPacDots();
    }
    void defaultValue()
    {
        player = new Player(gp, keyH,map,this);
        blinky = new Blinky(gp,player,map,9,11);
        pinky = new Pinky(gp,player,map,10,11);
        clyde = new Clyde(gp,player,map,12,11);
        inky = new Inky(gp,player,map,13,11,blinky);
        animation= new Animation("/player/idle/",5,true);
    }
    public void restart()
    {
        restartCalled=true;

        //restartPacDots();
    }
    private void checkRestart()
    {
        if(restartCalled==true)
        {
            player.seDefaultValues();
            blinky.setDefaultValues();
            pinky.setDefaultValues();
            clyde.setDefaultValues();
            inky.setDefaultValues();
            restartCalled=false;
        }
    }
    public void update()
    {
        checkRestart();
        for(PacDot pacdot:pacDots)
        {
            pacdot.update();
        }
        blinky.update();
        pinky.update();
        clyde.update();
        inky.update();
        player.update();
    }

    public void draw(Graphics2D g2)
    {
        for(PacDot pacdot:pacDots)
        {
            pacdot.draw(g2);
        }
        blinky.draw(g2);
        pinky.draw(g2);
        clyde.draw(g2);
        inky.draw(g2);
        player.draw(g2);
        animation.next();
        for (int i = 0; i < life-1; i++)
        {
            g2.drawImage(animation.getSprite(),6*gp.tileSize+(i*gp.tileSize),0,gp.tileSize,gp.tileSize,null);
        }

    }
    void fillPacDots()
    {
        for (int y = 0; y < gp.maxScreenRow; y++)
            for (int x = 0; x < gp.maxScreenCol; x++)
            {
                if(map.intMap[y][x]==3)
                    pacDots.add(new PacDot(gp,player,x,y));
                else if(map.intMap[y][x]==2)
                    pacDots.add(new PowerPellet(gp,player,x,y));
            }
    }
    void restartPacDots()
    {
        for(PacDot pacdot:pacDots)
        {
            pacdot.pl = player;
        }
    }
}
