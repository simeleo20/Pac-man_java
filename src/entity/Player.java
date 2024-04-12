package entity;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
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


public class Player extends Entity
{
    GamePanel gp;
    KeyHandler keyH;
    Map map;
    final int plSize = 21;
    int xTile;
    int yTile;
    String tryDirection;
    int oldX;
    int oldY;

    public Player(GamePanel gp,KeyHandler keyH, Map map)
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

    }
    public void update()
    {
        anim.next();
        move();
        anim.run(direction);
    }
    private void move()
    {

        yTile = y/ gp.tileSize;
        xTile = x/ gp.tileSize;
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
            if (Objects.equals(tryDirection, "up") && map.intMap[yTile - 1][xTile] == 0) {
                direction = "up";
            }
            if (Objects.equals(tryDirection, "down") && map.intMap[yTile + 1][xTile] == 0) {
                direction = "down";
            }
        }
        if(Math.abs(y-(yTile* gp.tileSize+(gp.tileSize/2)))<=1)
        {
            if(Objects.equals(tryDirection, "left")&&map.intMap[ yTile][xTile-1] == 0)
            {
                direction ="left";
            }
            if(Objects.equals(tryDirection, "right")&&map.intMap[ yTile][xTile+1] == 0)
            {
                direction ="right";
            }
        }
        oldX=x;
        oldY=y;
        if(Objects.equals(direction, "up"))
        {
            if(map.intMap[ yTile-1][xTile] == 0 || y>yTile* gp.tileSize+(gp.tileSize/2))
            {
                y -= speed;
            }

        }
        if(Objects.equals(direction, "down"))
        {
            if(map.intMap[ yTile+1][xTile] == 0 || y<yTile* gp.tileSize+(gp.tileSize/2))
            {
                y += speed;
            }
        }
        if(Objects.equals(direction, "left"))
        {
            if(map.intMap[ yTile][xTile-1] == 0 || x>xTile* gp.tileSize+(gp.tileSize/2))
            {
                x -= speed;
            }
        }
        if(Objects.equals(direction, "right"))
        {
            if(map.intMap[ yTile][xTile+1] == 0 || x<xTile* gp.tileSize+(gp.tileSize/2))
            {
                x += speed;
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

        System.out.println("x:"+x+" xt:"+ (xTile* gp.tileSize+(gp.tileSize/2)));
        System.out.println("y:"+y+" yt:"+ (yTile* gp.tileSize+(gp.tileSize/2)));
        System.out.println("xtile:"+xTile+" ytile:"+yTile);
    }
    public void setPlayerImages()
    {
        anim = new Animator();
        anim.newAnimation("right","/player/",5);
        anim.newAnimation("down","/player/",5,90);
        anim.newAnimation("left","/player/",5,180);
        anim.newAnimation("up","/player/",5,270);
        anim.run("right");


    }
    public void draw(Graphics2D g2)
    {


        g2.drawImage(anim.getSprite(),x-(plSize/2),y-(plSize/2),plSize,plSize,null);

    }
}
