package entity;

import main.GamePanel;
import main.KeyHandler;
import tile.Map;

import java.awt.*;

public class PacDot extends Entity
{
    public Player pl;
    public boolean alive;
    int dotSize;
    int points;
    public PacDot(GamePanel gp,Player pl,int xTile,int yTile)
    {
        this.gp = gp;
        this.xTile = xTile;
        this.yTile = yTile;
        this.pl = pl;
        setDefaultValues();
        updRealPos();
    }
    public void setDefaultValues()
    {
        dotSize = (int)((double)2 * gp.scale);
        alive = true;
        points = 10;

    }
    public void update()
    {
        if(alive)
            checkCollision();
    }
    public void checkCollision()
    {
        if(Math.abs(pl.x - x)<=1 && Math.abs(pl.y - y)<=1)
        {
            collision();
        }
    }
    protected void collision()
    {
        gp.addPoints(points);
        pl.addPoints(points);
        pl.addFramesStop(1);
        alive = false;
    }
    public void draw(Graphics2D g2)
    {
        if(alive)
        {
            g2.setColor(Color.decode("#FFFF76"));
            g2.fillRect(x-(dotSize/2), y-(dotSize/2), dotSize, dotSize);
        }
    }
}
