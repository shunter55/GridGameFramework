package Framework;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.sql.*;
import java.io.*;

/**
 * The test class HallOfFameTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class HallOfFameTest extends TestCase
{
    /**
     * Default constructor for test class HallOfFameTest
     */
    public HallOfFameTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }
    
    public void testIntegers()
    {
        HallOfFame<Integer> hall = new HallOfFame<Integer>(new Comparator<HallItem>()
            {
                public int compare(HallItem o1, HallItem o2)
                {
                    return ((Integer)o1.getScore()).compareTo((Integer)o2.getScore());
                }
                
                public boolean equals(Object obj)
                {
                    if (obj instanceof Comparator)
                        return true;
                    return false;
                }
            }, 10);
        
        hall.add("Bob", new Integer(5));
        hall.add("Joe", new Integer(2));
        hall.add("James", new Integer(7));
        
        List<HallItem> times = hall.getDispList();
        
        assertEquals(times.get(0).toString(), "Joe 2");
        assertEquals(times.get(1).toString(), "Bob 5");
        assertEquals(times.get(2).toString(), "James 7");
    }
    
    public void testTimes()
    {
        HallOfFame<Time> hall = new HallOfFame<Time>(new Comparator<HallItem>()
            {
                public int compare(HallItem o1, HallItem o2)
                {
                    return ((Time)o1.getScore()).compareTo((Time)o2.getScore());
                }
                
                public boolean equals(Object obj)
                {
                    if (obj instanceof Comparator)
                        return true;
                    return false;
                }
            }, 10);
        
        hall.add("Bob", new Time(0, 5, 2));
        hall.add("Joe", new Time(0, 5, 0));
        hall.add("James", new Time(0, 6, 17));
        
        List<HallItem> times = hall.getDispList();
        
        assertEquals(times.get(0).toString(), "Joe 00:05:00");
        assertEquals(times.get(1).toString(), "Bob 00:05:02");
        assertEquals(times.get(2).toString(), "James 00:06:17");
    }
    
    public void testSave()
    {
        HallOfFame<Time> hall = new HallOfFame<Time>(new Comparator<HallItem>()
            {
                public int compare(HallItem o1, HallItem o2)
                {
                    return ((Time)o1.getScore()).compareTo((Time)o2.getScore());
                }
                
                public boolean equals(Object obj)
                {
                    if (obj instanceof Comparator)
                        return true;
                    return false;
                }
            }, 10);
        
        hall.add("Bob", new Time(0, 5, 2));
        hall.add("Joe", new Time(0, 5, 0));
        hall.add("James", new Time(0, 6, 17));
        
        hall.save("HOFTestFile.txt");
        
        HallOfFame<Time> hall2 = new HallOfFame<Time>(null, 0);
        try
        {
            hall2.read("HOFTestFile.txt");
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
            assertTrue(false);
        }
        catch (IOException e)
        {
            System.out.println(e);
            assertTrue(false);
        } 
        
        List<HallItem> times = hall.getDispList();        
        assertEquals(times.get(0).toString(), "Joe 00:05:00");
        assertEquals(times.get(1).toString(), "Bob 00:05:02");
        assertEquals(times.get(2).toString(), "James 00:06:17");
        
        List<HallItem> times2 = hall2.getDispList();  
        System.out.println("--> " + times2.get(0).toString());
        assertTrue(times.get(0) == times.get(0));
        assertFalse(times == times2);
        assertFalse(times.get(0) == times2.get(0));
        assertEquals(times2.get(0).toString(), "Joe 00:05:00");
        assertEquals(times2.get(1).toString(), "Bob 00:05:02");
        assertEquals(times2.get(2).toString(), "James 00:06:17");
    }
    
    /*
    public void testSave2()
    {
        HallOfFame<Time> hall = new HallOfFame<Time>(new Comparator<HallItem>()
            {
                public int compare(HallItem o1, HallItem o2)
                {
                    return ((Time)o1.getScore()).compareTo((Time)o2.getScore());
                }
                
                public boolean equals(Object obj)
                {
                    if (obj instanceof Comparator)
                        return true;
                    return false;
                }
            }, 10);
        
        hall.add("Bob", new Time(0, 5, 2));
        hall.add("Joe", new Time(0, 5, 0));
        hall.add("James", new Time(0, 6, 17));
        
        hall.save("HOF.save");
        
        HallOfFame<Time> hall2 = null;
        try
        {
            hall2 = new HallOfFame<Time>(null, 10);
            hall2 = hall2.read("HOF.save");
        }
        catch (FileNotFoundException e)
        {
            
        }
        catch (IOException e)
        {}
        
        // verify the object state
        List<HallItem> times2 = hall2.getDispList();  
        System.out.println("--> " + times2.get(0).toString());
        assertEquals(times2.get(0).toString(), "Joe 00:05:00");
        assertEquals(times2.get(1).toString(), "Bob 00:05:02");
        assertEquals(times2.get(2).toString(), "James 00:06:17");
    }
    */

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
}
