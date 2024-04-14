package entity;

import animation.Animator;
import main.GamePanel;
import main.KeyHandler;
import tile.Map;

public class DynamicEntity extends Entity
{
    protected Map map;
    public Animator anim;
    public String direction;
    public int speed;

}
