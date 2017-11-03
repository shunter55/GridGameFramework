package Framework;

import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.GrayFilter;
import javax.swing.table.*;
import java.awt.event.*;
import org.ini4j.*;
import java.io.*;

/**
 * GameModel represents a new game (plugin).
 * 
 * @author Stephen Kobata
 * @version 11/14/15
 */
public abstract class GameModel extends Observable
{
    private java.util.Timer timer;
    private int elapsedTime;
    
    // Preferences
    Preferences preferences;
    private TileEnum tileEnum;
    private Map<String, ImageIcon> curSkin;
    private String curSkinName;
    
    private String pluginName;
    
    private HallOfFame hallOfFame;
    
    /**
     * Constructs a new GameModel.
     * @param tiles the TileEnum that contains the list of all possible Tiles
     * that will be in a cell.
     * @param pName the name of the plugin. Must be the same as the Package
     * name.
     */
    public GameModel(TileEnum tiles, String pName)
    {
        // Create preferences.
        preferences = new Preferences();
        preferences.addMenu(pName + "/preferences.ini", "Preferences");
        pluginName = pName;
        
        // Get all skins in pref file.
        List<String> itemKeys = preferences.getMenuItemKeys(
            "Preferences", "Skin");
        // Use the first skin as the default.
        String sName = preferences.getMenuItemVal(
            "Preferences", "Skin", itemKeys.get(0));
        setSkin(sName, tiles);
        
    }
    
    /**
     * Send a click to a location in the board.
     * @param loc the location to click.
     */
    abstract public void clickLocation(Location loc);
    
    /**
     * Send a right click to a location in the board.
     * @param loc the location to click.
     */
    abstract public void rightClickLocation(Location loc);
    
    /**
     * get the size of the board.
     * @return the board size.
     */
    abstract public int getSize();
    
    /**
     * Sends the status to a string.
     * @return the status as a string.
     */
    abstract public String statusToString();
    
    /**
     * Sets the title of the Game.
     * @return the title of the game.
     */
    abstract public String getTitle();
    
    /**
     * Returns the board as a string.
     * How you want your board to be layed out in console view.
     * @return the board as a string.
     */
    abstract public String boardToString();
    
    /**
     * Returns the table model that represents the 2D board.
     * @return the table model.
     */
    abstract public AbstractTableModel getModel();
    
    /**
     * Loads a predefined board from a Scanner that is reading from a
     * file. The file should not have spaces between symbols.
     */
    abstract public void loadModelFromScanner(Scanner scan);
    
    /**
     * Returns true if the game is over. Must be changed before notifyObservers.
     * @return if the game is over or not.
     */
    abstract public boolean gameIsOver();
    
    /** 
     * Returns the players score for the Hall Of Fame.
     * @return the player's score.
     */
    abstract public Serializable getScore();
    
    /**
     * Returns true if the background should be faded.
     * @return if the background should be faded.
     */
    abstract public boolean backgroundIsFaded();
    
    /**
     * Image files must be located in a folder Skins/[skinName]/
     * Image names must be the [Tile's Name].jpg.
     * @param skinName the name of the skin you want to use.
     * @param tEnum enumeration of all the tiles.
     */
    public void setSkin(String skinName, TileEnum tEnum)
    {
        Map<String, ImageIcon> imageMap =
            new LinkedHashMap<String, ImageIcon>();
        
        ArrayList<String> tileNames = new ArrayList<String>();
        
        String path = pluginName + "/Skins/" + skinName + "/";
        
        // Add tile images.
        for (TileEnum tile : tEnum.getNames())
        {
            if (tile.isImage())
            {
                imageMap.put(tile.getSymbol(), new ImageIcon(
                    path + tile.toString() + ".jpg"));
            }
        }
        
        // Add background image.
        imageMap.put("background", new ImageIcon(path + "background.jpg"));
        // Add disabled background image.
        imageMap.put("backgroundDisabled", new ImageIcon(GrayFilter
            .createDisabledImage(imageMap.get("background").getImage())));
        
        curSkin = imageMap;
        
        setChanged();
        notifyObservers("skin");
    }
    
    /**
     * Returns the current skin.
     * @return map with the current skin.
     */
    public Map<String, ImageIcon> getSkin()
    {
        return curSkin;
    }
    
    /**
     * gets the skin Name.
     * @return the skin name.
     */
    public String getSkinName()
    {
        return curSkinName;
    }
    
    private final long kMillisecondsInSec = 1000;
    /**
     * Resets the timer.
     * @param time the starting time for the timer.
     * @param increment whether the timer should increment or decrement.
     * @param interval the interval in milliseconds.
     */
    public void resetTimer(int time, final boolean increment, int interval)
    {
        stopTimer();
        
        timer = new java.util.Timer(true);
        timer.schedule(new TimerTask()
            {
                /**
                 * The timer action.
                 */
                public void run()
                {
                    // IF it should increment.
                    if (increment)
                    {
                        elapsedTime++;
                    }
                    // Otherwise decrement.
                    else
                    {
                        elapsedTime--;
                    }
                    setChanged();
                    notifyObservers("timer");
                }
            }, interval, interval);
        
        elapsedTime = 0;
        
        setChanged();
        notifyObservers("timer");
    }
    
    /**
     * Stops the active timer.
     */
    public void stopTimer()
    {
        // If timer exists.
        if (timer != null)
        {
            timer.cancel();
            timer.purge();  
        }
    }
    
    /**
     * Number of times the timer has run since its start/reset.
     * @return number of times the timer has run.
     */
    public int getElapsedTime()
    {
        return elapsedTime;
    }
    
    /**
     * Converts the string to the right format. (adds spaces)
     * @return a string in the right format. (spaces added)
     * @param scan a scanner to a file that needs spaces added.
     */
    private String boardScannerToString(Scanner scan)
    {
        String boardStr = "";

        // While there is another line in the file.
        while (scan.hasNextLine())
        {
            String line = scan.nextLine();
            // For each character in the line.
            for (int idx = 0; idx < line.length(); idx++)
            {
                boardStr += "" + line.charAt(idx) + " ";
            }
            boardStr += "\n";
        }

        return boardStr;
    }
    
    /**
     * Returns the preferences.
     * @return the preferences.
     */
    public Preferences getPreferences()
    {
        return new Preferences(preferences);
    }
    
    /**
     * Returns the default menuActions.
     * @return list of the default menusActionListeners.
     */
    public List<MenuListener> getDefaultMenus()
    {
        List<MenuListener> menuListeners = new ArrayList<MenuListener>();
        
        menuListeners.add(new MenuListener()
        {
            public String getName()
            {
                return "Hall";
            }
            
            public char getShortcutKey()
            {
                return 'H';
            }
            
            public int getShortcutKeyEvent()
            {
                return KeyEvent.VK_H;
            }
            
            public int getKeyModifier()
            {
                return InputEvent.ALT_DOWN_MASK;
            }
            
            public boolean isVisible()
            {
                return true;
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                notifyObservers("Hall");
            }
        });
        
        menuListeners.add(new MenuListener()
        {
            public String getName()
            {
                return "Quit";
            }
            
            public char getShortcutKey()
            {
                return 'Q';
            }
            
            public int getShortcutKeyEvent()
            {
                return KeyEvent.VK_Q;
            }
            
            public int getKeyModifier()
            {
                return InputEvent.ALT_DOWN_MASK;
            }
            
            public boolean isVisible()
            {
                return true;
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                notifyObservers("Quit");
            }
        });
        
        menuListeners.add(new MenuListener()
        {
            public String getName()
            {
                return "Prefs";
            }
            
            public char getShortcutKey()
            {
                return 'P';
            }
            
            public int getShortcutKeyEvent()
            {
                return KeyEvent.VK_P;
            }
            
            public int getKeyModifier()
            {
                return InputEvent.ALT_DOWN_MASK;
            }
            
            public boolean isVisible()
            {
                return true;
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                notifyObservers("Prefs");
            }
        });
        
        return menuListeners;
    }
    
    /**
     * Sets the hall of fame.
     * @param hall the hall of fame to use.
     */
    public void setHallOfFame(HallOfFame hall)
    {
        hallOfFame = hall;
    }
    
    /**
     * gets the hall of fame.
     * @return the hall of fame.
     */
    public HallOfFame getHallOfFame()
    {
        return hallOfFame;
    }
    
    /*
    private final String prefFile = "preferences.ini";
     /**
     * Gets the initial preferences from the preferences.ini file.
     */
    /*
    private void initPreferences()
    {
        try
        {
            Ini ini = new Ini(new File(prefFile));

            // FOR every preference in preference key set.
            for (Object prefOption : ini.keySet())
            {
                Profile.Section section = 
                    (Profile.Section) ini.get((String)prefOption);
                Object[] keys = section.keySet().toArray();
                // Set default board size.
                if (((String)prefOption).equals("Board Size"))
                {
                    boardSize = Integer.parseInt(
                        section.fetch((String) keys[0]));
                }
                // Set default skin.
                else if (((String)prefOption).equals("Skin"))
                {
                    setSkin(section.fetch((String)keys[0]));
                    //curSkin = section.fetch((String)keys[0]);
                }
            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }
    */
    
}











