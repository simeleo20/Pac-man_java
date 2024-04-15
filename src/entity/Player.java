package entity;



import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;


import animation.Animation;
import animation.Animator;
import main.GamePanel;
import main.KeyHandler;
import tile.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends DynamicEntity
{
    private KeyHandler keyH;
    final int plSize = 30;
    String tryDirection;
    int oldX;
    int oldY;
    private int points=0;
    public boolean isChasing;
    Timer timer = new Timer();
    TimerTask stopChase;
    boolean dead;
    private int framesStop;



    public Player(GamePanel gp, KeyHandler keyH, Map map)
    {
        this.gp = gp;
        this.keyH =keyH;
        this.map = map;
        seDefaultValues();
        setPlayerImages();
    }
    public void seDefaultValues()
    {
        x=gp.tileSize + (gp.tileSize/2);
        y=gp.tileSize + (gp.tileSize/2);
        oldX=x;
        oldY=y;
        speed = 2;
        direction = "right";
        isChasing = false;
        dead = false;
        framesStop =0;
    }
    public void update()
    {
        anim.next();
        if(framesStop<=0)
        {
            if (Objects.equals(anim.getAnimation("death").getMessage(), "finito"))
                gp.restart();
            if (!dead)
            {
                move();
                anim.run(direction);
            }
        }
        else framesStop--;

    }
    private void move()
    {

        updTilePos();
        if(keyH.upPressed)
        {

            tryDirection="up";

        }
        if(keyH.downPressed)
        {


            tryDirection="down";
        }
        if(keyH.leftPressed)
        {

            tryDirection="left";
        }
        if(keyH.rightPressed)
        {

            tryDirection="right";
        }

        if(Math.abs(x-(xTile* gp.tileSize+(gp.tileSize/2)))<=1) {
            if (Objects.equals(tryDirection, "up") && map.isFree(xTile,yTile-1) ) {
                direction = "up";
            }
            if (Objects.equals(tryDirection, "down") && map.isFree(xTile,yTile+1)) {
                direction = "down";
            }
        }

        if(Math.abs(y-(yTile* gp.tileSize+(gp.tileSize/2)))<=1)
        {
            if(xTile>0&&Objects.equals(tryDirection, "left")&&map.isFree(xTile-1,yTile) )
            {
                direction ="left";
            }

            if(xTile<(gp.maxScreenCol-1))
            {

                if (Objects.equals(tryDirection, "right") && map.isFree(xTile+1,yTile))
                {
                    direction = "right";
                }

            }

        }
        oldX=x;
        oldY=y;
        if(Objects.equals(direction, "up"))
        {
            if(map.isFree(xTile,yTile-1) || y>yTile* gp.tileSize+(gp.tileSize/2))
            {
                y -= speed;
            }
        }
        if(Objects.equals(direction, "down"))
        {
            if(map.isFree(xTile,yTile+1) || y<yTile* gp.tileSize+(gp.tileSize/2))
            {
                y += speed;
            }
        }
        if(Objects.equals(direction, "left"))
        {
            if(xTile>0) {
                if (map.isFree(xTile-1,yTile) || x > xTile * gp.tileSize + (gp.tileSize / 2)) {
                    x -= speed;
                }
            }
            else {
                x -= speed;
                if(x<0)
                    x=gp.maxScreenCol*gp.tileSize;
            }
        }

        if(Objects.equals(direction, "right"))
        {

            if(xTile<gp.maxScreenCol-1)
            {
                if (map.isFree(xTile+1,yTile) || x < xTile * gp.tileSize + (gp.tileSize / 2)) {
                    x += speed;
                }
            }
            else
            {
                x += speed;
                if(x> gp.maxScreenCol*gp.tileSize)
                    x=0;
            }
        }

        /*
        if(oldX==x)
        {
            x = xTile* gp.tileSize+(gp.tileSize/2);
        }
        if(oldY==y)
        {
            y = yTile* gp.tileSize+(gp.tileSize/2);
        }*/


    }
    public void setPlayerImages()
    {
        anim = new Animator();
        anim.newAnimation("right","/player/idle/",5);
        anim.newAnimation("down","/player/idle/",5,90);
        anim.newAnimation("left","/player/idle/",5,180);
        anim.newAnimation("up","/player/idle/",5,270);
        anim.newAnimation("death","/player/death/",5,false,"finito");
        anim.run("right");

    }
    public void draw(Graphics2D g2)
    {
        g2.drawImage(anim.getSprite(),x-(plSize/2),y-(plSize/2),plSize,plSize,null);
        g2.setColor(Color.white);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, gp.tileSize/2 -1));

        g2.drawString("HIGH SCORE",gp.screenWidth/2 -(gp.tileSize*3/2),gp.tileSize/2);
        g2.drawString(""+points,gp.screenWidth/2,gp.tileSize);
    }

    public int getPoints()
    {
        return points;
    }
    public void addPoints(int points)
    {
        this.points += points;
    }

    public void chase()
    {
        isChasing=true;
        stopChase = new TimerTask()
        {
            @Override
            public void run()
            {
                isChasing = false;
            }
        };
        timer.schedule(stopChase,6000);
    }
    public void kill()
    {
        anim.run("death");
        dead = true;
    }

    public void addFramesStop(int frame)
    {
        framesStop += frame;
    }

}
