package Framework;

import org.ini4j.*;
import java.util.*;
import java.io.*;

/**
 * Preferences that are read in from a .ini file.
 * 
 * @author Stephen Kobata
 * @version 11/14/15
 */
public class Preferences
{
    /**
     * First map represents items in the menu bar. Ex: Game, Preferences.
     * Second map represents items in each of the tabs.
     * Third map represents menu items and their corresponding values.
     */
    private Map<String, Map<String, Map<String, Object>>> prefs;
    
    /**
     * Creates a new preferences object.
     */
    public Preferences()
    {
        prefs = new LinkedHashMap<String, Map<String, Map<String, Object>>>();
    }
    
    /**
     * Copies a preference object.
     * @param pref the Preference to copy.
     */
    public Preferences(Preferences pref)
    {
        prefs = new LinkedHashMap<String, Map<String, Map<String, Object>>>();
        
        // For every menu key in pref.
        for (String menuKey : pref.getMenuKeys())
        {
            LinkedHashMap<String, Map<String, Object>> subMenus = 
                new LinkedHashMap<String, Map<String, Object>>();
            // For every subMenu key.
            for (String subMenuKey : pref.getSubMenuKeys(menuKey))
            {
                LinkedHashMap<String, Object> menuItems =
                    new LinkedHashMap<String, Object>();
                // For every menuItem.
                for (String menuItem : pref.getMenuItemKeys(
                    menuKey, subMenuKey))
                {
                    menuItems.put(menuItem, pref.getMenuItemVal(
                        menuKey, subMenuKey, menuItem));
                }
                subMenus.put(subMenuKey, menuItems);
            }
            prefs.put(menuKey, subMenus);
        }
    }
    
    /**
     * Adds a menu from a file.
     * @param fileName the file to read from.
     * @param menuName the name for the menu.
     */
    public void addMenu(String fileName, String menuName)
    {       
        prefs.put(menuName, getPreferences(fileName));
    }
    
    /**
     * Gets preferences from a file.
     * @return a map containing the preferences in the file.
     */
    private Map<String, Map<String, Object>> getPreferences(String fileName)
    {
        Map<String, Map<String, Object>> prefMap = 
            new LinkedHashMap<String, Map<String, Object>>();
        
        try
        {
            Ini ini = new Ini(new File(fileName));
            
            // FOR every preference in preference key set.
            for (Object prefOption : ini.keySet())
            {
                // Name of the word in [ ].
                String prefOptionName = (String) prefOption;
                // Map for each menuItem corresponding to its value.
                Map<String, Object> itemMap =
                    new LinkedHashMap<String, Object>();
                // section to read.
                Profile.Section section = 
                    (Profile.Section) ini.get(prefOptionName);
                    
                // keys of words under [ ].
                Object[] keys = section.keySet().toArray();
                
                // For every key.
                for (Object obj : keys)
                {
                    String name = (String) obj;
                    
                    // Generic int type as Object?
                    itemMap.put(name, section.fetch(name));
                }
                prefMap.put(prefOptionName, itemMap);
            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        
        return prefMap;
    }
    
    /**
     * Gets the menu Keys.
     * @return a list of all the key names.
     */
    public List<String> getMenuKeys()
    {
        ArrayList<String> keys = new ArrayList<String>();
        
        // For every key in prefs.
        for (String key : prefs.keySet())
        {
            keys.add(key);
        }
        
        return keys;
    }
    
    /**
     * Returns the list of names in [ ] for the menu given.
     * @param menuName the name of the menu you want the keys for.
     * @return the set of Strings for the menu given.
     */
    public List<String> getSubMenuKeys(String menuName)
    {
        ArrayList<String> keys = new ArrayList<String>();
        
        // For every key in pref menuName.
        for (String key : prefs.get(menuName).keySet())
        {
            keys.add(key);
        }
        
        return keys;
    }
    
    /**
     * Returns the list of names under [ ] for the menu given.
     * @param menuName the name of the menu you want the keys for.
     * @param subMenuName the name of the thing in [ ].
     * @return the set of Strings for the menu given.
     */
    public List<String> getMenuItemKeys(String menuName, String subMenuName)
    {
        ArrayList<String> keys = new ArrayList<String>();
        
        // For every menuItem key.
        for (String key : prefs.get(menuName).get(subMenuName).keySet())
        {
            keys.add(key);
        }
        
        return keys;
    }
    
    /**
     * Returns the object corresponding to the preference item.
     * @return the item associated with the preference.
     */
    public String getMenuItemVal(
        String menuName, String subMenuName, String itemName)
    {
        return (String) prefs.get(menuName).get(subMenuName).get(itemName);
    }
    
}














