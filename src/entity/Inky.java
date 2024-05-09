package entity;

import main.GamePanel;
import tile.Map;

import java.awt.*;
import java.util.Objects;

public class Inky extends Ghost
{
    Blinky blinky;

    public Inky(GamePanel gp, Player pl, Map map, int spawnX, int spawnY,Blinky blinky)
    {
        super(gp, pl, map,spawnX,spawnY,"inky");
        this.blinky=blinky;
        jailPoint=300;
    }

    @Override
    public void defaultScatter() {
        scatterX =gp.maxScreenCol-1;
        scatterY =gp.maxScreenRow-1;
    }

    public int getChaseX()
    {
        int xTarget;
        if(Objects.equals(pl.direction, "up"))
            xTarget = pl.xTile - 2 ;
        else xTarget = pl.xTile + (pl.dirX*2);
        return (xTarget-blinky.xTile)*2 + blinky.xTile;
    }
    public int getChaseY()
    {
        int yTarget;
        yTarget = pl.yTile + (pl.dirY*2);
        return (yTarget-blinky.yTile)*2 + blinky.yTile;
    }

    @Override
    public void drawTarget(Graphics2D g2)
    {
        int xT = getChaseX();
        int yT = getChaseY();
        g2.setColor(Color.decode("#0088FF"));
        g2.drawRect(xT*gp.tileSize,yT* gp.tileSize,gp.tileSize,gp.tileSize);
    }
}
