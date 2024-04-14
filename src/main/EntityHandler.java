package main;

import entity.Ghost;
import entity.PacDot;
import entity.Player;
import tile.Map;
import utilities.AStar;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class EntityHandler
{


    private GamePanel gp;
    private KeyHandler keyH;
    private Map map;
    private AStar ast;
    Player player;
    Ghost blicky;
    List<PacDot> pacDots;

    public EntityHandler(GamePanel gp, KeyHandler keyH, Map map, AStar ast)
    {
        this.gp = gp;
        this.keyH = keyH;
        this.map = map;
        this.ast = ast;
        player = new Player(gp, keyH,map);
        blicky = new Ghost(gp,player,map,ast);
        pacDots = new LinkedList<>();
        fillPacDots();
    }

    public void update()
    {
        for(PacDot pacdot:pacDots)
        {
            pacdot.update();
        }
        blicky.update();
        player.update();
    }

    public void draw(Graphics2D g2)
    {
        for(PacDot pacdot:pacDots)
        {
            pacdot.draw(g2);
        }

        blicky.draw(g2);
        player.draw(g2);
    }
    void fillPacDots()
    {
        for (int y = 0; y < gp.maxScreenRow; y++)
            for (int x = 0; x < gp.maxScreenCol; x++)
            {
                if(map.intMap[y][x]==0)
                    pacDots.add(new PacDot(gp,player,x,y));
            }


    }

}
