package Framework;

import java.util.*;
import java.io.*;

/**
 * Hall Item represents an Player, Score pair.
 * 
 * @author Stephen Kobata
 * @version 11/25/15
 */
public class HallItem<T> implements Serializable
{
    private String name;
    private T score;
    
    /**
     * Creates a new Hall Item.
     * @param pName the player's name.
     * @param pScore the player's score.
     */
    public HallItem(String pName, T pScore)
    {
        name = pName;
        score = pScore;
    }

    /**
     * Returns the player's name.
     * @return the player's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the player's score.
     * @return the player's score.
     */
    public T getScore()
    {
        return score;
    }
    
    /**
     * Returns the player, score pair as a string.
     * @return a string representing the HallItem.
     */
    public String toString()
    {
        return name + " " + score;
    }
    
    /**
     * writes the object to a file.
     * @param out the ObjectOutputStream.
     */
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        //out.writeObject(name);
        //out.writeObject(score);
        
        out.defaultWriteObject();
    }
    
    /**
     * reads the object from a file.
     * @param in the ObjectInputStream.
     */
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        //name = (String) in.readObject();
        //score = (T) in.readObject();
        
        in.defaultReadObject();
    }
    
    /**
     * reads an object with no data.
     */
    private void readObjectNoData() throws ObjectStreamException
    {
    }
}