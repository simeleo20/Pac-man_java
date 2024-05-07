package entity;

import animation.Animator;
import main.GamePanel;
import tile.Map;
import utilities.AStar;

import java.awt.*;

import java.util.*;


import static java.lang.Math.abs;
import static java.lang.Math.pow;

/*
 *  c'è un bug si incastra nello spawn
 *
 */
public class Ghost extends DynamicEntity
{
    enum States
    {
        chase,
        scatter,
        eaten,
        frightened,
        jailed

    }
    Player pl;
    AStar ast;
    final int ghSize = 21;
    boolean arrived;
    public boolean alive;
    private States state;
    int scatterX, scatterY;
    int spawnX, spawnY;
    double jailTimer;
    double lastTime;
    private boolean alreadyEaten;
    private String name;
    private Timer timer;
    private TimerTask scatterChaseTask;

    private boolean tunneled;
    private boolean tunnelOut;

    public Ghost(GamePanel gp, Player pl, Map map,int spawnX,int spawnY,String name)
    {
        this.gp  = gp;
        this.map = map;
        this.pl  = pl;
        //this.ast = aStar;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.name = name;
        setDefaultValues();
        setGhostImages("/ghost/"+name+"/idle/");
    }
    public void setDefaultValues()
    {
        x=gp.tileSize* spawnX + (gp.tileSize/2);
        y=gp.tileSize* spawnY + (gp.tileSize/2);
        updTilePos();
        timer = new Timer();
        speed = 2;
        direction = "up";
        arrived = false;
        alive = true;
        chaseState();
        defaultScatter();
        tunneled = false;
        tunnelOut = false;
        alreadyEaten = false;
        jailTimer =10;
        lastTime=System.nanoTime();;
    }
    public void defaultScatter()
    {
        scatterX = 0;
        scatterY = 0;
    }
    public void update()
    {
        System.out.println(jailTimer);
        jailTimer += lastTime - System.nanoTime();
        lastTime =System.nanoTime();
        if(alive&&jailTimer<=0)
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
                anim.run(direction);
            //endregion
        }

    }

    private void move()
    {
        updTilePos();

        int yTarget =yTile* gp.tileSize+(gp.tileSize/2);
        int xTarget =xTile* gp.tileSize+(gp.tileSize/2);

        int rSpeed = speed;

        if(tunneled) rSpeed = 1;

        //region muovi in direzione
        if(Objects.equals(direction, "up"))
        {
            if(map.isFree(xTile,yTile-1) || y>yTarget)
            {
                y -= rSpeed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "down"))
        {
            if(map.isFree(xTile,yTile+1) || y<yTarget)
            {
                y += rSpeed;
            }
            else arrived = true;
        }
        if(Objects.equals(direction, "left"))
        {
            if(xTile>0)
            {
                if (map.isFree(xTile-1,yTile) || x > xTarget)
                {
                    x -= rSpeed;
                } else arrived = true;
            }
            else {
                x -= rSpeed;
                if(x<0)
                {
                    x = gp.maxScreenCol * gp.tileSize;
                    tunnelOut = true;
                }
            }
        }
        if(Objects.equals(direction, "right"))
        {
            if(xTile<gp.maxScreenCol-1)
            {
                if (map.isFree(xTile+1,yTile) || x < xTarget)
                {
                    x += rSpeed;
                }
                else arrived = true;
            }
            else
            {
                x += rSpeed;
                if(x> gp.maxScreenCol*gp.tileSize)
                {
                    x = 0;
                    tunnelOut=true;
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

        if(rSpeed == 3||rSpeed ==2)
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

        //region check tunneled
        if(xTile>0 && xTile<gp.maxScreenCol)
        {
            if (tunnelOut==false && map.intMap[yTile][xTile] == 5)
            {
                tunneled = true;
            }
            if(tunnelOut==true&& map.intMap[yTile][xTile] == 5)
            {
                tunneled = false;

            }
            if(tunnelOut==true && tunneled ==false && map.intMap[yTile][xTile] != 5)
            {
                tunnelOut=false;
            }
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
        if(state == States.chase) {
            goTo(getChaseX(), getChaseY());
            //System.out.println(getChaseX()+" "+pl.xTile+" "+getChaseY()+" "+pl.yTile);
        }
        else if (state == States.frightened)
        {
            randomDir();
        } else if(state == States.scatter)
            goTo(scatterX, scatterY);
        else
        {
            goTo(spawnX,spawnY);
        }
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

    public void setGhostImages(String directory)
    {
        //region settaggio animazioni
        anim = new Animator();
        anim.newAnimation("right",directory+"right/",5);
        anim.newAnimation("left",directory+"left/",5);
        anim.newAnimation("up",directory+"up/",5);
        anim.newAnimation("down",directory+"down/",5);
        anim.newAnimation("scared","/ghost/scared/",5);
        anim.newAnimation("eatenDown","/ghost/"+name+"/eaten/down",1);
        anim.run("right");
        //endregion
    }
    public void draw(Graphics2D g2)
    {
        //region passa al frame successivo e lo disegna
        if (alive)
        {
            anim.next();
            g2.drawImage(anim.getSprite(), x - (ghSize / 2), y - (ghSize / 2), ghSize, ghSize, null);
            drawTarget(g2);
        }
        //endregion
    }
    public void drawTarget(Graphics2D g2){}
    private void checkCollision()
    {
        //region Controlla la distanza dal player
        double dis = (double)abs(pl.x-x)+abs(pl.y-y) ;
        double minDis = (double)(pl.plSize+ghSize)/3;
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
            pl.eatGhost();
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
                if(state == States.eaten || map.intMap[yTile][xTile-1]!=4)
                {
                    minDis = app;
                    minDir = "left";
                }
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
                if(state == States.eaten || map.intMap[yTile][xTile+1]!=4)
                {
                    minDis = app;
                    minDir = "right";
                }
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
        speed = 2;
        switchChaseScatter(7000);
        oppositeDirection();
        System.out.println("scatter");
    }
    private void eatenState()
    {
        state = States.eaten;
        alreadyEaten = true;
        speed = 2;
    }
    private void jailedState()
    {

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
            if(xTile == spawnX && yTile == spawnY)
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


    public int getChaseX()
    {
        return pl.xTile;
    }
    public int getChaseY()
    {
        return pl.yTile;
    }

}
