package tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TileSet
{
    public ArrayList<Tile> tiles = new ArrayList<Tile>();
    public int size;
    public TileSet(String path)
    {
        getImages(path);
    }

    public void getImages(String path)
    {

        try
        {
            File[] directory = new File(getClass().getResource(path).getPath()).listFiles();
            for(File file : directory )
            {
                Tile app = new Tile(ImageIO.read(file));
                String name = file.getName().substring(0,4);
                app.setStructure(name);
                tiles.add(app);
            }
            size = directory.length;
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    public Tile getTile( boolean oDown, boolean oLeft, boolean oUp, boolean oRight)
    {
        for(Tile tile : tiles)
        {
            if(tile.oRight == oRight && tile.oUp == oUp && tile.oLeft == oLeft && tile.oDown == oDown)
                return tile;
        }
        return  null;
    }

}


