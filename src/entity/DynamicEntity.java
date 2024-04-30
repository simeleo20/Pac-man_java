package entity;

import animation.Animator;
import main.GamePanel;
import main.KeyHandler;
import tile.Map;

import java.util.Objects;

public class DynamicEntity extends Entity
{
    protected Map map;
    public Animator anim;
    public String direction;
    public int speed;

    public int dirX;
    public int dirY;

    public void updateDir()
    {
        if(Objects.equals(direction, "up"))
        {
            dirX=0;
            dirY=-1;
        }
        else if(Objects.equals(direction, "down"))
        {
            dirX=0;
            dirY=1;
        }
        else if(Objects.equals(direction, "left"))
        {
            dirX=-1;
            dirY=0;
        }
        else if(Objects.equals(direction, "right"))
        {
            dirX=1;
            dirY=0;
        }
    }

}
