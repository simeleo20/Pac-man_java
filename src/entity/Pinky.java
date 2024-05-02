package entity;
import main.GamePanel;
import tile.Map;

import java.awt.*;
import java.util.Objects;

public class Pinky extends Ghost
{
    public Pinky(GamePanel gp, Player pl, Map map,int spawnX,int spawnY)
    {
        super(gp, pl, map,spawnX,spawnY,"pinky");
    }
    public int getChaseX()
    {
        if(Objects.equals(pl.direction, "up"))
            return pl.xTile - 2 ;
        return pl.xTile + (pl.dirX*2);
    }
    public int getChaseY()
    {
        return pl.yTile + (pl.dirY*2);
    }
    @Override
    public void drawTarget(Graphics2D g2)
    {
        int xT = getChaseX();
        int yT = getChaseY();
        g2.setColor(Color.decode("#FFC0CB"));
        g2.drawRect(xT*gp.tileSize,yT* gp.tileSize,gp.tileSize,gp.tileSize);
    }

}
