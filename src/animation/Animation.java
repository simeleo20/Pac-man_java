package animation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import utilities.Im;


public class Animation
{
    public HashMap<Integer,BufferedImage> sprites = new HashMap<Integer,BufferedImage>();
    public int size;
    public int speed;
    public boolean loop;

    private int frameCounter;
    public int frame;
    public boolean finished;
    public String endMessage;
    private String endOutMessage;

    public Animation(String directory, int speed)
    {
        setDefaultValues(directory,speed,true);
    }
    public Animation(String directory, int speed,boolean loop)
    {
        setDefaultValues(directory,speed,loop);
    }
    public void setDefaultValues(String path,int speed, boolean loop)
    {
        this.loop = loop;
        frame = 0;
        frameCounter=0;
        this.speed = speed;
        finished = false;
        try
        {
            File[] directory = new File(getClass().getResource(path).getPath()).listFiles();
            int i = 0;
            for(File file : directory )
            {
                BufferedImage app = ImageIO.read(file);
                String fileName = file.getName().replace(".png","");
                //sprites.add(Integer.parseInt(fileName),app);
                sprites.put(Integer.parseInt(fileName),app);
                i++;
            }
            size = directory.length;
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    public void reset()
    {
        frame = 0;
        frameCounter=0;
        finished= false;
    }
    public BufferedImage getSprite()
    {
        if(!finished)
            return sprites.get(frame);
        else return null;
    }

    public void next()
    {

        frameCounter++;
        if(frameCounter >= speed)
        {
            frameCounter=0;
            frame++;
            if(frame >= size)
            {
                if(loop)
                {
                    frame=0;
                }
                else
                {
                    finished = true;
                    frame =0;
                    frameCounter=0;
                    endOutMessage = endMessage;
                }
            }
        }

    }
    public void rotate(int degree)
    {
        for (int i = 0; i < size; i++)
        {
            //sprites.set(i, Im.rotate(sprites.get(i),degree));
            sprites.replace(i, Im.rotate(sprites.get(i),degree));
        }

    }
    public String getMessage()
    {
        return endOutMessage;
    }

}
