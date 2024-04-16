package tile;


import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map
{
    GamePanel gp;
    TileSet ts;

    private final String[] strMap=
    {
            "wwwwwwwwwwwwwww",
            "w      w      w",
            "w ww w w w ww w",
            "w+           +w",
            "w ww w w w ww w",
            "w    w   w    w",
            "wwww ww ww wwww",
            "wwww       wwww",
            "wwww ww ww wwww",
            "     w   w     ",
            "wwww wwwww wwww",
            "wwww       wwww",
            "wwww wwwww wwww",
            "ww     w     ww",
            "ww www w www ww",
            "w             w",
            "w ww wwwww ww w",
            "w             w",
            "wwwwwwwwwwwwwww",
    };

    //region Mappa
    public int[][] intMap;
    //endregion

    Tile[][] tileMap;

    public Map(GamePanel gp)
    {
        ts = new TileSet("/tiles/");
        this.gp = gp;
        tileMap = new Tile[gp.maxScreenRow][gp.maxScreenCol];
        intMap = stringsToIntMap();
        setTileMap();
    }
    private void setTileMap()
    {
        for (int y = 0; y < gp.maxScreenRow; y++)
            for (int x = 0; x < gp.maxScreenCol; x++) {
                boolean oDown=false;
                boolean oLeft=false;
                boolean oUp=false;
                boolean oRight=false;
                if(isFree(x,y)) {
                    if (y > 0) {

                        if (isFree(x,y-1)) {
                            oUp = true;
                        }
                    }
                    else oUp = true;
                    if (x > 0) {
                        if (isFree(x-1,y)) {
                            oLeft = true;
                        }
                    }
                    else oLeft = true;
                    if (y < gp.maxScreenRow - 1) {
                        if (isFree(x,y+1)) {
                            oDown = true;
                        }
                    }
                    else oDown = true;
                    if (x < gp.maxScreenCol - 1) {
                        if (isFree(x+1,y)) {
                            oRight = true;
                        }
                    }
                    else oRight = true;
                    tileMap[y][x]=ts.getTile(oDown,oLeft,oUp,oRight);
                }
                else
                    tileMap[y][x]=new Tile(new BufferedImage(13,13,BufferedImage.TYPE_BYTE_GRAY));


            }


    }

    public boolean isFree(int x,int y)
    {
        if(x>=0 && x< gp.maxScreenCol && y>=0 && y< gp.maxScreenRow)
            return intMap[y][x] == 0 ||intMap[y][x] == 2;
        else
            return false;
    }

    public void draw(Graphics2D g2)
    {

        for (int y = 0; y < gp.maxScreenRow; y++)
            for (int x = 0; x < gp.maxScreenCol; x++)
            {
                g2.drawImage(tileMap[y][x].image,x* gp.tileSize,y* gp.tileSize,gp.tileSize,gp.tileSize,null);
            }
        //g2.drawImage(tileMap[0][0].image,0,0,gp.tileSize,gp.tileSize,null);

        //g2.drawImage(ts.getTile(true,false,true,true).image,0,0,gp.tileSize,gp.tileSize,null);


    }

    private int[][] stringsToIntMap()
    {
        int[][] out;
        out = new int[gp.maxScreenRow][gp.maxScreenCol];
        for (int y = 0; y < gp.maxScreenRow; y++)
        {
            String str = strMap[y];
            for (int x = 0; x < gp.maxScreenCol; x++)
            {
                char ch = str.charAt(x);
                if(ch == 'w')
                    out[y][x] = 1;
                else if(ch == ' ')
                    out[y][x] = 0;
                else if(ch == '+')
                    out[y][x] = 2;
            }
        }
        return out;
    }
}