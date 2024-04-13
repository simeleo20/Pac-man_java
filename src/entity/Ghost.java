package entity;

import animation.Animator;
import main.GamePanel;
import tile.Map;
import utilities.AStar;
import utilities.Node;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import java.util.*;

import static java.lang.Math.abs;

public class Ghost extends Entity
{

    Player pl;
    AStar ast;
    final int ghSize = 21;
    boolean arrived;
    public boolean alive;

    public Ghost(GamePanel gp, Player pl, Map map, AStar aStar)
    {
        this.gp = gp;
        this.map = map;
        this.pl =pl;
        this.ast=aStar;
        setDefaultValues();
        setGhostImages("/ghost/blinky/");

    }
    public void setDefaultValues()
    {
        x=gp.tileSize*3 + (gp.tileSize/2);
        y=gp.tileSize*3 + (gp.tileSize/2);
        speed = 1;
        direction = "right";
        arrived = false;
        alive = true;
    }
    public void update()
    {
        if(alive)
        {
            checkCollision();
            anim.next();
            move();
            anim.run("right");
        }
    }

    private void move()
    {
        updTilePos();

        int yTarget =yTile* gp.tileSize+(gp.tileSize/2);
        int xTarget =xTile* gp.tileSize+(gp.tileSize/2);

        //region muovi in direzione
        if(Objects.equals(direction, "up"))
        {
            if(map.intMap[ yTile-1][xTile] == 0 || y>yTarget)
            {
                y -= speed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "down"))
        {
            if(map.intMap[ yTile+1][xTile] == 0 || y<yTarget)
            {
                y += speed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "left"))
        {
            if(map.intMap[ yTile][xTile-1] == 0 || x>xTarget)
            {
                x -= speed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "right"))
        {
            if(map.intMap[ yTile][xTile+1] == 0 || x<xTarget)
            {
                x += speed;
            }
            else arrived = true;
        }
        //endregion

        //region checkBivio
        if(abs(x-xTarget)<=1 && abs(y-yTarget)<=1)
        {
            if((map.intMap[yTile+1][xTile]==0||map.intMap[yTile-1][xTile]==0) && (Objects.equals(direction, "left") || Objects.equals(direction, "right")) )
                arrived=true;
            if((map.intMap[yTile][xTile+1]==0||map.intMap[yTile][xTile-1]==0) && (Objects.equals(direction, "up") || Objects.equals(direction, "down")))
                arrived=true;
        }
        //endregion

        //region check nuova direzione
        if (arrived)
        {
            //System.out.println("arrivato");
            arrived = false;
            goToTarget();
        }
        //endregion

    }
    public void goToTarget()
    {
        goTo(pl.xTile,pl.yTile);
    }
    private void goTo(int x,int y)
    {
        //region cerca strada
        ast.reset();
        ast.setInitialNode(new Node(xTile,yTile));
        ast.setFinalNode(new Node(pl.xTile,pl.yTile));
        List<Node> path = ast.findPath();
        //endregion
        //region choose direction
        if(path.size()>1) {
            if (xTile < path.get(1).getX()) {
                direction = "right";
            }
            if (xTile > path.get(1).getX()) {
                direction = "left";
            }
            if (yTile < path.get(1).getY()) {
                direction = "down";
            }
            if (yTile > path.get(1).getY()) {
                direction = "up";
            }

        }
        //endregion
    }
    public void setGhostImages(String directory)
    {
        anim = new Animator();
        anim.newAnimation("right",directory,5);
        anim.run("right");
    }
    public void draw(Graphics2D g2)
    {
        if (alive)
            g2.drawImage(anim.getSprite(),x-(ghSize /2),y-(ghSize /2), ghSize, ghSize,null);
    }
    private void checkCollision()
    {
        double dis = (double)abs(pl.x-x)+abs(pl.y-y) ;
        double minDis = (double)(pl.plSize+ghSize)/2;
        if(dis<=minDis)
        {
            alive = false;
        }
    }
}
