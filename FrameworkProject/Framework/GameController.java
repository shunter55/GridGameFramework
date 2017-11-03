package Framework;

import java.util.*;
import java.awt.event.*;

/**
 * Abstract class GameController - write a description of the class here.
 * 
 * @author Stephen Kobata
 * @version 11/14/15
 */
public interface GameController
{
    // Reference to GameModel will be needed.
    
    /**
     * Returns a map with all the action listeners for "Game" menuItems.
     * The key for the MenuListener will be the name of the menuItem.
     * 
     * Menu "Game" will be generated from this.
     * 
     * @return a map of action listeners for the menu items you want.
     */
    public List<MenuListener> getMenuListeners();
    
    /**
     * Returns a map with all the action listeners for preference menuItems.
     * The key for the ActionListener must be the name that appears in the
     * preferences.ini file. (The word in [ ]) There should not be duplicate 
     * keys.
     * 
     * GameModel provides Preferences class so you can access your preference
     * file data.
     * 
     * @return a map of action listeners for the menu items you want.
     */
    public Map<String, ActionListener> getPreferenceListeners();
    
    /**
     * Sends a click to the AbstractTableModel at location.
     * @param loc location of the click.
     */
    //public void clickLocation(Location loc);
    
    /**
     * Sends a right click to the AbstractTableModel at location.
     * @param loc location of the right click.
     */
    //public void rightClickLocation(Location loc);
    
}
