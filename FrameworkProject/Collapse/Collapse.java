package Collapse;

import Framework.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;

/**
 * The GameModel for the Collapse plugin.
 * 
 * @author Stephen Kobata
 * @version 11/21/15
 */
public class Collapse extends GameModel
{
    private Board board;
    private int moveCount;
    
    private final int kTimerInterval = 1000;
    private final int kSecPerMin = 60;
    private final String kSaveFile = "Collapse/HallOfFame.save";
    
    /**
     * Creates a new instance of Collapse.
     */
    public Collapse()
    {
        super(Tiles.purple, "Collapse");
        
        Preferences prefs = super.getPreferences();
        
        newGame(Integer.parseInt(prefs.getMenuItemVal(
            "Preferences", "Board Size", prefs.getMenuItemKeys(
                "Preferences", "Board Size").get(0))));
        
        HallOfFame hof = new HallOfFame<Integer>(new Comparator<HallItem>()
            {
                public int compare(
                    HallItem o1, HallItem o2)
                {
                    return ((Integer)o1.getScore()).compareTo(
                        (Integer)o2.getScore());
                }
                
                public boolean equals(Object obj)
                {
                    if (obj instanceof Comparator)
                    {
                        return false;
                    }
                    return false;
                }
            }, 10);
        try
        {
            hof.read(kSaveFile);
        }
        catch (IOException e)
        {
            if (!(e instanceof FileNotFoundException))
            {
                System.err.println(e);
            }
        }
        
        super.setHallOfFame(hof);
    }
    
    /**
     * Called when you left click a location.
     * @param loc the location that was clicked.
     */
    public void clickLocation(Location loc)
    {
        boolean tileClicked;
        
        tileClicked = board.removeAdjacentTiles(loc);
        
        // IF game is won.
        if (board.getTileCount() == 0)
        {
            // Stop timer.
            super.stopTimer();
        }
        
        // if a tile was clicked.
        if (tileClicked)
        {
            setChanged();
            notifyObservers();
        }        
    }
    
    /**
     * Called when you right click a location.
     */
    public void rightClickLocation(Location loc)
    {
    }
    
    /**
     * Returns the board size.
     * @return the board size.
     */
    public int getSize()
    {
        return board.getSize();
    }
    
    /**
     * Returns the status label as a string.
     * @return the status label as a string.
     */
    public String statusToString()
    {
        String str = "";
        str += String.format("Tiles left: %d    Moves: %d  %d:%02d",
            board.getTileCount(), board.getMoveCount(),
                super.getElapsedTime() /
                    kSecPerMin, super.getElapsedTime() % kSecPerMin);
                
        return str;
    }
    
    /**
     * Returns the board as a string.
     * @return the board as a string.
     */
    public String boardToString()
    {
        return board.boardToStringSpace();
    }
    
    /**
     * Returns the title for the views.
     * @return String for the view title.
     */
    public String getTitle()
    {
        return "Collapse";
    }
    
    /**
     * Returns the AbstractTableModel representing the board.
     * @return the board.
     */
    public AbstractTableModel getModel()
    {
        return board;
    }
    
    /**
     * Creates a predefined board from the scanner.
     * @param scan the scanner used to make a board.
     */
    public void loadModelFromScanner(Scanner scan)
    {
        board = new Board(scan);
    }
    
    /**
     * Returns if the game is over or not.
     * @return if the game is over.
     */
    public boolean gameIsOver()
    {
        if (board.getTileCount() == 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Returns the score for the hall of fame.
     * @return player's score.
     */
    public Serializable getScore()
    {
        return (Integer) board.getMoveCount();
    }
    
    /**
     * Returns if the background is faded or not.
     * used for fading or unfading the background.
     * @return if the background should be faded.
     */
    public boolean backgroundIsFaded()
    {
        return gameIsOver();
    }
    
    // Plugin methods.
    /**
     * Makes a new game with the given board size.
     * @param size the board size.
     */
    public void newGame(int size)
    {
        board = new Board(size);
        moveCount = 0;
        
        super.resetTimer(0, true, kTimerInterval);
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Makes a new game with the previous board size.
     */
    public void newGame()
    {
        newGame(board.getSize());
    }
    
    /**
     * Resets the board.
     */
    public void reset()
    {
        board.reset();
        moveCount = 0;
        super.resetTimer(0, true, kTimerInterval);
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Undoes the player's last move.
     */
    public void undo()
    {
        board.undo();
        
        setChanged();
        notifyObservers();
    }
}
