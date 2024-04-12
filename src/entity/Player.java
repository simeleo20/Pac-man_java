package entity;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


import animation.Animation;
import animation.Animator;
import main.GamePanel;
import main.KeyHandler;



public class Player extends Entity
{
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp,KeyHandler keyH)
    {
        this.gp = gp;
        this.keyH =keyH;

        seDefaultValues();
        setPlayerImages();
    }
    public void seDefaultValues()
    {
        x=100;
        y=100;
        speed = 4;
        direction = "right";

    }
    public void update()
    {
        anim.next();
        if(keyH.upPressed)
        {
            y -= speed;
            direction="up";

        }
        if(keyH.downPressed)
        {
            y += speed;
            direction="down";
        }
        if(keyH.leftPressed)
        {
            x -=speed;
            direction="left";
        }
        if(keyH.rightPressed)
        {
            x +=speed;
            direction="right";
        }
        anim.run(direction);

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


        g2.drawImage(anim.getSprite(),x,y,gp.tileSize,gp.tileSize,null);

    }
}
