package entity;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

import animation.Animation;
import animation.Animator;
import main.GamePanel;
import main.KeyHandler;
import tile.Map;

public class Entity 
{
    public int x,y;

    public int xTile;
    public int yTile;


    protected GamePanel gp;



    protected void updTilePos()
    {
        yTile = y/ gp.tileSize;
        xTile = x/ gp.tileSize;
    }
    protected void updRealPos()
    {
        y = yTile*gp.tileSize+ (gp.tileSize/2);
        x = xTile*gp.tileSize+ (gp.tileSize/2);
    }

    public void update()
    {

    }
    public void draw(Graphics2D g2)
    {

    }

}
