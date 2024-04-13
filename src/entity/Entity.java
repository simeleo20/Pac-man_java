package entity;

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
    public int speed;
    public int xTile;
    public int yTile;

    public Animator anim;
    public String direction;

    protected GamePanel gp;
    protected KeyHandler keyH;
    protected Map map;

    protected void updTilePos()
    {
        yTile = y/ gp.tileSize;
        xTile = x/ gp.tileSize;
    }
}
