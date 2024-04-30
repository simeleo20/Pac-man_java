package entity;

import main.GamePanel;
import tile.Map;

import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Clyde extends Ghost
{
    public Clyde(GamePanel gp, Player pl, Map map, int spawnX, int spawnY) {
        super(gp, pl, map, spawnX, spawnY, "blinky");
    }
    public int getChaseX()
    {
        if(distance(pl.xTile,pl.yTile,xTile,yTile)<=8)
        {
            System.out.println("innnnnnnn");
            return scatterX;
        }
        return pl.xTile;
    }
    public int getChaseY()
    {
        if(distance(pl.xTile,pl.yTile,xTile,yTile)<=8)
            return scatterY;
        return pl.yTile;
    }
    private double distance(int x1,int y1, int x2, int y2)
    {return sqrt( pow(y1-y2,2)+pow(x1-x2,2));}
}
