package entity;

import animation.Animator;
import main.GamePanel;
import tile.Map;
import utilities.AStar;
import utilities.Node;

import java.awt.*;

import java.util.*;
import java.util.List;


import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Ghost extends DynamicEntity
{
    enum States
    {
        chase,
        scatter,
        eaten,
        frightened
    }
    Player pl;
    AStar ast;
    final int ghSize = 21;
    boolean arrived;
    public boolean alive;
    private States state;
    int scTrX,scTrY;
    int dScTrX,dScTrY;
    int spX, spY;
    private boolean alreadyEaten;
    private String name;
    private Timer timer;
    private TimerTask scatterChaseTask;

    public Ghost(GamePanel gp, Player pl, Map map, AStar aStar,int spX,int spY,String name)
    {
        this.gp  = gp;
        this.map = map;
        this.pl  = pl;
        this.ast = aStar;
        this.spX = spX;
        this.spY = spY;
        this.name = name;
        setDefaultValues();
        setGhostImages("/ghost/"+name+"/idle/");
    }
    public void setDefaultValues()
    {
        x=gp.tileSize*spX + (gp.tileSize/2);
        y=gp.tileSize*spY + (gp.tileSize/2);
        timer = new Timer();
        speed = 2;
        direction = "up";
        arrived = false;
        alive = true;
        chaseState();
        dScTrX = 1;
        dScTrY = 1;
        scTrX = dScTrX;
        scTrY = dScTrY;
        alreadyEaten = false;

    }
    public void update()
    {
        if(alive)
        {
            checkState();
            checkCollision();
            move();
            //region scelta animazione
            if(state == States.eaten)
                anim.run("eatenDown");
            else if(state == States.frightened)
                anim.run("scared");
            else
                anim.run("idle");
            //endregion
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
            if(map.isFree(xTile,yTile-1) || y>yTarget)
            {
                y -= speed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "down"))
        {
            if(map.isFree(xTile,yTile+1) || y<yTarget)
            {
                y += speed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "left"))
        {
            if(xTile>0)
            {
                if (map.isFree(xTile-1,yTile) || x > xTarget)
                {
                    x -= speed;
                } else arrived = true;
            }
            else {
                x -= speed;
                if(x<0)
                    x=gp.maxScreenCol*gp.tileSize;
            }
        }
        if(Objects.equals(direction, "right"))
        {
            if(xTile<gp.maxScreenCol-1)
            {
                if (map.isFree(xTile+1,yTile) || x < xTarget)
                {
                    x += speed;
                }
                else arrived = true;
            }
            else
            {
                x += speed;
                if(x> gp.maxScreenCol*gp.tileSize)
                {
                    x = 0;
                }

            }

        }
        //endregion

        //region inutilizato crea nuovo scared target
        /*
        if(Math.abs(x-(scTrX*gp.tileSize +(gp.tileSize)/2))<=1 &&Math.abs(y-(scTrY*gp.tileSize +(gp.tileSize)/2))<=1)
        {
            newScTr();
        }*/
        //endregion

        //region checkBivio

        if(speed == 3||speed ==2)
        {
            if(abs(x-xTarget)<=1)
                x = xTarget;
            if(abs(y-yTarget)<=1)
                y = yTarget;
        }

        if(x==xTarget && y==yTarget && xTile<gp.maxScreenCol-1 && xTile>0)
        {
            if((map.isFree(xTile,yTile+1)||map.isFree(xTile,yTile-1)) && (Objects.equals(direction, "left") || Objects.equals(direction, "right")) )
                arrived=true;
            if((map.isFree(xTile+1,yTile)||map.isFree(xTile-1,yTile)) && (Objects.equals(direction, "up") || Objects.equals(direction, "down")))
                arrived=true;
        }
        //endregion

        //region check nuova direzione
        if (arrived )
        {
            //System.out.println("arrivato");
            arrived = false;
            goToTarget();
        }
        //endregion


    }
    public void goToTarget()
    {
        //region Sceglie il target e lo segue
        if(state == States.chase)
            goTo(pl.xTile,pl.yTile);
        else if (state == States.frightened)
        {
            randomDir();
        } else
            goTo(scTrX, scTrY);
        //endregion
    }
    private void goTo(int x,int y)
    {
        //region inutilizato cerca strada vecchio path finding
        /*
        ast.reset();
        ast.setInitialNode(new Node(xTile,yTile));
        ast.setFinalNode(new Node(x,y));
        ast.setDirection(direction);
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

        }*/
        //endregion
        getNewDir(x,y);
    }
    private void newScTr()
    {
        //region scegli nuovo punto casuale
        do
        {
            scTrX = (int)(Math.random()*(gp.maxScreenCol));
            scTrY = (int)(Math.random()*(gp.maxScreenRow));
            System.out.println("vado "+scTrX+" "+scTrY);
        }while (!map.isFree(scTrX,scTrY));
        //endregion
    }
    public void setGhostImages(String directory)
    {
        //region settaggio animazioni
        anim = new Animator();
        anim.newAnimation("idle",directory,5);
        anim.newAnimation("scared","/ghost/scared/",5);
        anim.newAnimation("eatenDown","/ghost/"+name+"/eaten/down",1);
        anim.run("idle");
        //endregion
    }
    public void draw(Graphics2D g2)
    {
        //region passa al frame successivo e lo disegna
        if (alive)
        {
            anim.next();
            g2.drawImage(anim.getSprite(), x - (ghSize / 2), y - (ghSize / 2), ghSize, ghSize, null);
        }
        //endregion
    }
    private void checkCollision()
    {
        //region Controlla la distanza dal player
        double dis = (double)abs(pl.x-x)+abs(pl.y-y) ;
        double minDis = (double)(pl.plSize+ghSize)/2;
        if(dis<=minDis)
        {
            collision();
        }
        //endregion
    }
    protected void collision()
    {
        if(state == States.frightened)
        {
            eatenState();
        }
        else if (state == States.chase||state==States.scatter)
        {
            pl.kill();
            alive = false;
        }
    }
    private void getNewDir(int tX, int tY)
    {
        //region Direzione più veloce
        double minDis =-2;
        String minDir = "up";
        double app = -1;

        //region check up
        if(map.isFree(xTile,yTile-1)&& !Objects.equals(direction, "down"))
        {
            app = distance(tX,tY,xTile,yTile-1);
            if(minDis<0)
            {
                minDis=app;
                minDir="up";
            }
        }
        //endregion

        //region check left
        if(map.isFree(xTile-1,yTile)&& !Objects.equals(direction, "right"))
        {
            app = distance(tX,tY,xTile-1,yTile);
            if(minDis<0)
            {
                minDis=app;
                minDir="left";
            }
            else if(app<minDis)
            {
                minDis=app;
                minDir="left";
            }

        }
        //endregion

        //region check down
        if(map.isFree(xTile,yTile+1)&& !Objects.equals(direction, "up"))
        {
            app = distance(tX,tY,xTile,yTile+1);
            if(minDis<0)
            {
                minDis=app;
                minDir="down";
            }
            else if(app<minDis)
            {
                minDis=app;
                minDir="down";
            }

        }
        //endregion

        //region check right
        if(map.isFree(xTile+1,yTile)&& !Objects.equals(direction, "left"))
        {
            app = distance(tX,tY,xTile+1,yTile);
            if(minDis<0)
            {
                minDis=app;
                minDir="right";
            }
            else if(app<minDis)
            {
                minDis=app;
                minDir="right";
            }

        }
        //endregion

        direction = minDir;
        //endregion
    }
    private double distance(int x1,int y1, int x2, int y2)
    {return pow(y1-y2,2)+pow(x1-x2,2);}
    private void frightenedState()
    {
        state = States.frightened;
        speed = 1;
        oppositeDirection();
    }
    private void chaseState()
    {
        state = States.chase;
        speed = 2;
        switchChaseScatter(20000);
        System.out.println("chase");
    }
    private void scatterState()
    {
        state = States.scatter;
        scTrX = dScTrX;
        scTrY = dScTrY;
        speed = 2;
        switchChaseScatter(7000);
        oppositeDirection();
        System.out.println("scatter");
    }
    private void eatenState()
    {
        scTrX = spX;
        scTrY = spY;
        state = States.eaten;
        alreadyEaten = true;
        speed = 3;
    }
    private void oppositeDirection()
    {
        if(Objects.equals(direction, "right")) direction="left";
        else if(Objects.equals(direction, "left")) direction="right";
        else if(Objects.equals(direction, "down")) direction="up";
        else if(Objects.equals(direction, "up")) direction="down";
    }
    private void checkState()
    {
        if(state==States.chase||state==States.scatter)
        {
            if(pl.isChasing&&!alreadyEaten)
            {
                frightenedState();
            }
            if(!pl.isChasing)
            {
                alreadyEaten = false;
            }
        } else if (state==States.frightened)
        {
            if(!pl.isChasing)
            {
                chaseState();
            }
        } else if (state==States.eaten)
        {
            if(xTile == spX && yTile == spY)
            {
                chaseState();
            }
        }
    }
    public void randomDir()
    {
        int nX=xTile ,nY =yTile;
        String nDir = "right";
        int f=0;
        int bC=0;
        if(!map.isFree(xTile+1,yTile)) bC++;
        if(!map.isFree(xTile-1,yTile)) bC++;
        if(!map.isFree(xTile,yTile-1)) bC++;
        if(!map.isFree(xTile,yTile+1)) bC++;
        if(bC<3) {
            do {
                nX = xTile;
                nY = yTile;
                int f1 = 0;
                int s = (int) (Math.random() * 4);
                if (s == 0) {
                    if (!Objects.equals(direction, "left")) {
                        nX = xTile + 1;
                        nDir = "right";
                        f1 = 1;
                    }
                } else if (s == 1) {
                    if (!Objects.equals(direction, "right")) {
                        nX = xTile - 1;
                        nDir = "left";
                        f1 = 1;
                    }
                } else if (s == 2) {
                    if (!Objects.equals(direction, "down")) {
                        nY = yTile - 1;
                        nDir = "up";
                        f1 = 1;
                    }
                } else if (s == 3) {
                    if (!Objects.equals(direction, "up")) {
                        nY = yTile + 1;
                        nDir = "down";
                        f1 = 1;
                    }
                }
                if (f1 == 1) {
                    if (map.isFree(nX, nY))
                        f = 1;
                }

            } while (f == 0);
            direction = nDir;
        }
        else {
            oppositeDirection();
        }
    }

    private void switchChaseScatter(int delay)
    {
        if(scatterChaseTask!=null)
            scatterChaseTask.cancel();
        scatterChaseTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if(state == States.chase)
                {
                    scatterState();
                }
                else
                {
                    chaseState();
                }
            }
        };
        timer.schedule(scatterChaseTask,delay);
    }
}
