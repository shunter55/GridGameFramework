package Minesweeper;

import Framework.*;
import javax.swing.table.*;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Represents the Minesweeper game plugin.
 * 
 * @author Stephen Kobata
 * @version 11/26/15
 */
public class Minesweeper extends GameModel
{
    private Board board;
    
    private int bombCount;
    
    private final int kSecPerMin = 60;
    
    private final String kSaveFile = "Minesweeper/HallOfFame.save";
    
    /**
     * Constructs a Minesweeper game.
     */
    public Minesweeper()
    {
        super(Tiles.bomb, "Minesweeper");
        
        Preferences prefs = super.getPreferences();
        
        HallOfFame hof = new HallOfFame<Integer>(new Comparator<HallItem>()
            {
                public int compare(
                    HallItem o1, HallItem o2)
                {
                    return ((Time)o1.getScore()).compareTo(
                        (Time)o2.getScore());
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
            //hof.setSaveFile(kSaveFile);
        }
        super.setHallOfFame(hof);
        
        int boardSize = Integer.parseInt(prefs.getMenuItemVal(
            "Preferences", "Board Size", prefs.getMenuItemKeys(
                "Preferences", "Board Size").get(0)));
        int bCount = Integer.parseInt(prefs.getMenuItemVal(
            "Preferences", "Difficulty", prefs.getMenuItemKeys(
                "Preferences", "Difficulty").get(0)));
            
        newGame(boardSize, bCount);
    }
    
    /**
     * Send a click to a location in the board.
     * @param loc the location to click.
     */
    public void clickLocation(Location loc)
    {
        board.clickLocation(loc);
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Send a right click to a location in the board.
     * @param loc the location to click.
     */
    public void rightClickLocation(Location loc)
    {
        board.addFlag(loc);
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * get the size of the board.
     * @return the board size.
     */
    public int getSize()
    {
        return board.getRowCount();
    }
    
    /**
     * Sends the status to a string.
     * @return the status as a string.
     */
    public String statusToString()
    {
        String status = "";
        
        status += String.format("Moves: %d   Flags: %d/%d  %d:%02d",
            board.getMoveCount(), board.getFlagCount(), getSize() * getSize() / bombCount,
            super.getElapsedTime() / kSecPerMin,
            super.getElapsedTime() % kSecPerMin);
            
        return status;
    }
    
    /**
     * Returns the title of the game. Displayed in gui and console.
     * @return the title of the game.
     */
    public String getTitle()
    {
        return "Minesweeper";
    }
    
    /**
     * Returns the board as a string.
     * How you want your board to be layed out in console view.
     * @return the board as a string.
     */
    public String boardToString()
    {
        return board.toString();
    }
    
    /**
     * Returns the table model that represents the 2D board.
     * @return the table model.
     */
    public AbstractTableModel getModel()
    {
        return board;
    }
    
    /**
     * Loads a predefined board from a Scanner that is reading from a
     * file. The file should not have spaces between symbols.
     * The file only needs to specify bomb locations.
     */
    public void loadModelFromScanner(Scanner scan)
    {
        board = new Board(scan);
    }
    
    /**
     * Returns true if the game is over. Must be changed before notifyObservers.
     * @return if the game is over or not.
     */
    public boolean gameIsOver()
    {
        return board.gameIsWon();
    }
    
    /** 
     * Returns the players score for the Hall Of Fame.
     * @return the player's score.
     */
    public Serializable getScore()
    {
        return new java.sql.Time((long)(
            super.getElapsedTime() * kMilliInSec - 57600000));
    }
    
    /**
     * Returns true if the background should be faded.
     * @return if the background should be faded.
     */
    public boolean backgroundIsFaded()
    {
        if (board.gameIsWon() || board.gameIsLost())
        {
            super.stopTimer();
        }
        return board.gameIsWon() || board.gameIsLost();
    }
    
    // Minesweeper methods.
    
    private final int kMilliInSec = 1000;
    /**
     * Makes a new game.
     * @param boardSize the size of the board.
     * @param bCount the bomb count.
     */
    public void newGame(int boardSize, int bCount)
    {
        bombCount = bCount;
        
        board = new Board(boardSize, bCount);
        
        super.resetTimer(0, true, kMilliInSec);
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Creates a new game with the previous bomb count.
     * @param boardSize the size of the new board.
     */
    public void newGame(int boardSize)
    {        
        newGame(boardSize, bombCount);
    }
    
    /**
     * Creates a new game with the previous board size
     * and bomb count.
     */
    public void newGame()
    {
        newGame(board.getRowCount(), bombCount);
    }
    
    /**
     * Creates a new game of the same size with a 
     * different number of bombs.
     * @param bCount the number of bombs.
     */
    public void changeDifficulty(int bCount)
    {
        bombCount = bCount;
        newGame(board.getRowCount(), bCount);
    }
    
    /**
     * Resets the board.
     */
    public void reset()
    {
        board.reset();
        super.resetTimer(0, true, kMilliInSec);
                
        setChanged();
        notifyObservers();
    }
    
    /**
     * Reveals the board.
     */
    public void peek()
    {
        board.peek();
        
        super.stopTimer();
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Initializes the board so that:
     * Top left of the board is a bomb,
     * the cell to the right of it is a 1.
     */
    public void cheat()
    {
        board.reset();
        super.resetTimer(0, true, kMilliInSec);
        
        board.cheat();
        
        setChanged();
        notifyObservers();
    }
}
