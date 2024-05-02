package entity;

import main.GamePanel;
import tile.Map;

import java.awt.*;
import java.util.Objects;

public class Blinky extends Ghost
{
    public Blinky(GamePanel gp, Player pl, Map map, int spawnX, int spawnY)
    {
        super(gp, pl, map,spawnX,spawnY,"blinky");
    }
    public void drawTarget(Graphics2D g2)
    {
        int xT = getChaseX();
        int yT = getChaseY();
        g2.setColor(Color.decode("#FF0000"));
        g2.drawRect(xT*gp.tileSize,yT* gp.tileSize,gp.tileSize,gp.tileSize);
    }
    @Override
    public void defaultScatter() {
        scatterX =gp.maxScreenCol-1;
        scatterY =0;
    }
}
