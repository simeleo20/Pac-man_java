package entity;

import main.GamePanel;

public class PowerPellet extends PacDot
{

    public PowerPellet(GamePanel gp, Player pl, int xTile, int yTile)
    {
        super(gp, pl, xTile, yTile);
    }
    public void setDefaultValues()
    {
        dotSize = 4 * gp.scale;
        alive = true;
        points = 100;
    }
    protected void collision()
    {
        pl.addPoints(points);
        pl.addFramesStop(3);
        pl.chase();
        alive = false;
    }
}