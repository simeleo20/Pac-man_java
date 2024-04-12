package animation;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Animator
{
    public HashMap<String,Animation> animations = new HashMap<String,Animation>();
    private Animation runningAnimation;
    String runningAnimationName;
    String idleAnimation;
    public Animator()
    {

    }
    public void next()
    {
        runningAnimation.next();
        animHandler();
    }
    private void animHandler()
    {
        if(animations.get(runningAnimationName).finished)
        {
            runningAnimationName = idleAnimation;
            run(idleAnimation);

        }

    }
    public BufferedImage getSprite()
    {
        return runningAnimation.getSprite();
    }
    public void newAnimation(String name,String directory, int speed)
    {
        Animation app = new Animation(directory,speed);
        animations.put(name,app);
        if(runningAnimationName == null)
        {
            runningAnimationName = name;
            runningAnimation = app;
        }
    }
    public void newAnimation(String name,String directory, int speed, int rotation)
    {

        Animation app = new Animation(directory,speed);
        app.rotate(rotation);
        animations.put(name,app);

        if(runningAnimationName == null)
        {
            runningAnimationName = name;
            runningAnimation = app;
        }

    }
    public void newAnimation(String name,Animation animation, int speed)
    {

        animations.put(name,animation);
        if(runningAnimationName == null)
        {
            runningAnimationName = name;
            runningAnimation = animation;
        }
    }
    public Animation getAnimation(String name)
    {
        return animations.get(name);
    }
    public void run(String name)
    {
        runningAnimationName = name;
        runningAnimation = getAnimation(name);
    }
    public void start(String name)
    {
        runningAnimation.reset();
        runningAnimationName = name;
        runningAnimation = getAnimation(name);
    }
    public void idle(String name)
    {
        idleAnimation = name;
    }
}
