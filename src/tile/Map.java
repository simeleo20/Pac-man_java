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
            "wwwwwwwwwwwwwwwwwwwwwww",
            "w,,,,,,,,,,w,,,,,,,,,,w",
            "w+www,wwww,w,wwww,www+w",
            "w,www,wwww,w,wwww,www,w",
            "w,,,,,,,,,,,,,,,,,,,,,w",
            "w,www,w,wwwwwww,w,www,w",
            "w,,,,,w,wwwwwww,w,,,,,w",
            "wwwww,w,,,,w,,,,w,wwwww",
            "wwwww,wwww w wwww,wwwww",
            "wwwww,w         w,wwwww",
            "wwwww,w www www w,wwwww",
            "    t,  wMM MMw  ,t    ",
            "wwwww,w wwwwwww w,wwwww",
            "wwwww,w         w,wwwww",
            "wwwww,w wwwwwww w,wwwww",
            "wwwww,w wwwwwww w,wwwww",
            "w,,,,,,,,,,w,,,,,,,,,,w",
            "w,www,wwww,w,wwww,www,w",
            "w+,,w,,,,,,,,,,,,,w,,+w",
            "www,w,w,wwwwwww,w,w,www",
            "w,,,,,w,,,,w,,,,w,,,,,w",
            "w,wwwwwwww,w,wwwwwwww,w",
            "w,wwwwwwww,w,wwwwwwww,w",
            "w,,,,,,,,,,,,,,,,,,,,,w",
            "wwwwwwwwwwwwwwwwwwwwwww",
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
            return intMap[y][x] == 0 ||intMap[y][x] == 2 ||intMap[y][x] == 3||intMap[y][x] == 4||intMap[y][x] == 5;
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


        g2.setColor(Color.white);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, gp.tileSize/2 -1));

        g2.drawString("HIGH SCORE",gp.screenWidth/2 -(gp.tileSize*3/2),gp.tileSize/2);
        g2.drawString(""+gp.getPoints(),gp.screenWidth/2,gp.tileSize);

        g2.drawString("CURRENT SCORE",gp.screenWidth/2 +(gp.tileSize*3/2),gp.tileSize/2);
        g2.drawString(""+gp.eh.player.getPoints(),gp.screenWidth/2+(gp.tileSize*3),gp.tileSize);

        for (int i = 0; i < gp.eh.life; i++) {

        }
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
                else if (ch == ',')
                {
                    out[y][x] = 3;
                }
                else if (ch == 'M')
                {
                    out[y][x] = 4;
                }
                else if (ch == 't')
                {
                    out[y][x] = 5;
                }
            }
        }
        return out;
    }
}
