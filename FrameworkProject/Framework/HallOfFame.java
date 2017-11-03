package Framework;

import java.util.*;
import java.io.*;

/**
 * Represents a hall of fame.  Keeps track of top scores.
 * 
 * @author Stephen Kobata
 * @version 11/25/15
 * @param <T> the score type.
 */
public class HallOfFame<T extends Serializable> implements Serializable
{
    

    private ArrayList<HallItem<T>> items;
    private Comparator comparator;
    private Integer listSize;
    private String saveFile;

    /**
     * Creates a hall of fame from a given file.
     * @param comp the comparator to use.
     * @param size the size of the HallOfFame.
     */
    public HallOfFame(Comparator comp, int size)
    {
        comparator = comp;
        items = new ArrayList<HallItem<T>>();
        
        listSize = size;
    }
    
    /**
     * sets the comparator for the Hall Of Fame.
     * @param listComparator the comparator to use.
     */
    public void setComparator(Comparator listComparator)
    {
        comparator = listComparator;
    }
    
    /**
     * Sets the file to save to.
     * @param fileName the file to save to.
     */
    public void setSaveFile(String fileName)
    {
        saveFile = fileName;
    }
    
    /**
     * Clears the Hall of Fame.
     */
    public void clear()
    {
        items = new ArrayList<HallItem<T>>();
        save(saveFile);
    }
    
    /**
     * Adds a Hall Item to the Hall of Fame.
     * @param name the name of the player.
     * @param score the player's score.
     */
    public void add(String name, T score)
    {
        items.add(new HallItem(name, score));
        Collections.sort(items, comparator);
        
        if (items.size() > listSize)
        {
            items.remove(items.size() - 1);
        }
        
        if (saveFile != null)
        {
            save(saveFile);
        }
    }
    
    /**
     * gets a list of HallItems in order.
     * @return the Hall Of Fame list.
     */
    public List<HallItem> getDispList()
    {
        ArrayList<HallItem> itemsCopy = (ArrayList<HallItem>) items.clone();
        
        return itemsCopy;
    }
    
    /**
     * Sets the size of the Hall Of Fame.
     * @param size the size of the Hall.
     */
    public void setListSize(int size)
    {
        listSize = size;
    }
    
    /**
     * Returns if the score is eligable to be in the Hall.
     * @param pScore the score to cheak.
     */
    public boolean isEligable(T pScore)
    {
        if (items.size() < listSize)
        {
            return true;
        }
        if (comparator.compare(pScore, items.get(items.size() - 1)) < 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Saves the hall to a file.
     * @param fileName the name of the file to save to.
     */
    public void save(String fileName)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            
            //writeObject(new ObjectOutputStream(new FileOutputStream(fileName)));
            
            this.writeObject(out);
            
            out.close();
            fileOut.close();
        }
        catch (FileNotFoundException e)
        {            
            System.out.println("Error - File Not Found save");
        } 
        catch (IOException e)
        {
            System.out.println("Error - IOExcepiton save");
            System.out.println(e);
        }
    }
    
    /**
     * Reads the Hall Of Fame from a file.
     * @param fileName the file to read from.
     */
    public void read(String fileName) throws FileNotFoundException, IOException
    {
        saveFile = fileName;
        
        try
        {
            //ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            //in.readObject();
            
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            
            //readObject(new ObjectInputStream(new FileInputStream(fileName)));
            
            this.readObject(in);
            
            in.close();
            fileIn.close();
        }
        //catch (IOException e)
        //{
        //    System.out.println("Error - IOExcepiton read");
        //    System.out.println(e);
        //}
        catch (ClassNotFoundException e)
        {
            System.out.println("Error - ClassNotFoundExcepiton");
        }
    }
    
    /**
     * Writes the Hall Of Fame object to and ObjectOutputStream.
     * @param out the ObjectOutputStream to write to.
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.writeObject(items);
        //out.writeObject(comparator);
        out.writeObject(listSize);
        
        //out.defaultWriteObject();
        
        //out.writeObject(this);
    }

    /**
     * Reads the Hall Of Fame object to and ObjectInputStream.
     * @param in the ObjectInputStream to write to.
     */
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        items = (ArrayList<HallItem<T>>) in.readObject();
        //comparator = (Comparator) in.readObject();
        listSize = (Integer) in.readObject();
        
        //in.defaultReadObject();
        //return (HallOfFame) in.readObject();
    }

    /**
     * Reads the Hall Of Fame object with no data.
     */
    private void readObjectNoData() throws ObjectStreamException
    {
    }

}
