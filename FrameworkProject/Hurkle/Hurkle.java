package Hurkle;

import Framework.*;
import javax.swing.table.*;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Write a description of class Hurkle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hurkle extends GameModel
{
    private final String kVersionTag = "1.0";
    private final String kTurnStartMsg = "Turns: 0";

    private Board myBoard;
    private String status; 
    
    private final String kSaveFile = "Hurkle/HallOfFame.save";
    
    public Hurkle()
    {
        super(Tiles.hidden, "Hurkle");
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
        
        
        myBoard = new Board();
        status = kTurnStartMsg;
    }
    
    /**
     * Send a click to a location in the board.
     * @param loc the location to click.
     */
    public void clickLocation(Location loc)
    {
        myBoard.makeMove(loc.getY(), loc.getX());
        myBoard.reveal(loc.getY(), loc.getX());
        status = "Turns: " + myBoard.getTurns() + "   Hint: " + myBoard.getHint();
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Send a right click to a location in the board.
     * @param loc the location to click.
     */
    public void rightClickLocation(Location loc)
    {
        
    }
    
    /**
     * get the size of the board.
     * @return the board size.
     */
    public int getSize()
    {
        return myBoard.getRowCount();
    }
    
    /**
     * Sends the status to a string.
     * @return the status as a string.
     */
    public String statusToString()
    {
        return status;
    }
    
    /**
     * Sets the title of the Game.
     * @return the title of the game.
     */
    public String getTitle()
    {
        return "Hurkle";
    }
    
    /**
     * Returns the board as a string.
     * How you want your board to be layed out in console view.
     * @return the board as a string.
     */
    public String boardToString()
    {
        return myBoard.toString();
    }
    
    /**
     * Returns the table model that represents the 2D board.
     * @return the table model.
     */
    public AbstractTableModel getModel()
    {
        return myBoard;
    }
    
    /**
     * Loads a predefined board from a Scanner that is reading from a
     * file. The file should not have spaces between symbols.
     */
    public void loadModelFromScanner(Scanner scan)
    {
        // Dont need.
    }
    
    /**
     * Returns true if the game is over. Must be changed before notifyObservers.
     * @return if the game is over or not.
     */
    public boolean gameIsOver()
    {
        return myBoard.isWin();
    }
    
    private final int kMilliInSec = 1000;
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
        return false;
    }
    
    // Plugin author methods.
    
    /** Start a new game, and optionally cheat. */
    public void newGame(boolean cheatFlag)
    {
        myBoard.reset();
        myBoard.hideHurkle(cheatFlag);
        status = kTurnStartMsg;
        super.resetTimer(0, true, 1000);
        
        setChanged();
        notifyObservers();
    }
    
    public void setGuessLimit(int limit)
    {
        myBoard.setGuessLimit(limit);
    }
  
}
