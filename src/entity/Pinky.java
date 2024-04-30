package entity;
import main.GamePanel;
import tile.Map;

import java.util.Objects;

public class Pinky extends Ghost
{
    public Pinky(GamePanel gp, Player pl, Map map,int spawnX,int spawnY)
    {
        super(gp, pl, map,spawnX,spawnY,"blinky");
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
}
