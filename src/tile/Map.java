package tile;


import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map
{
    GamePanel gp;
    TileSet ts;

    //region Mappa
    public int[][] intMap= {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };
    //endregion

    Tile[][] tileMap;

    public Map(GamePanel gp)
    {
        ts = new TileSet("/tiles/");
        this.gp = gp;
        tileMap = new Tile[gp.maxScreenRow][gp.maxScreenCol];
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
                if(intMap[y][x]==0) {
                    if (y > 0) {

                        if (intMap[y - 1][x] == 0) {
                            oUp = true;
                        }
                    }
                    else oUp = true;
                    if (x > 0) {
                        if (intMap[y][x - 1] == 0) {
                            oLeft = true;
                        }
                    }
                    else oLeft = true;
                    if (y < gp.maxScreenRow - 1) {
                        if (intMap[y + 1][x] == 0) {
                            oDown = true;
                        }
                    }
                    else oDown = true;
                    if (x < gp.maxScreenCol - 1) {
                        if (intMap[y][x + 1] == 0) {
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
        return intMap[y][x] == 0;
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

}
