package tile;

import java.awt.image.BufferedImage;

public class Tile
{
    public BufferedImage image;
    public boolean collision = false;
    public boolean oDown;
    public boolean oLeft;
    public boolean oUp;
    public boolean oRight;
    public String code;

    public Tile(BufferedImage image)
    {
        this.image = image;
    }

    public void setStructure(String structure)
    {
        oDown =false;
        oLeft=false;
        oUp=false;
        oRight=false;
        if(structure.substring(3, 4).equals("1"))
            oDown =true;
        if(structure.substring(2, 3).equals("1"))
            oLeft=true;
        if(structure.substring(1, 2).equals("1"))
            oUp=true;
        if(structure.substring(0, 1).equals("1"))
            oRight=true;
        code = structure;
        System.out.println(oDown+" "+oLeft+" "+oUp+" "+oRight);
    }
}
