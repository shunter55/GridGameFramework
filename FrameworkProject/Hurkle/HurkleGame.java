package Hurkle;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.*;
import javax.swing.table.*;

/** Game rules for Find the Hurkle. */
public class HurkleGame extends java.util.Observable implements ActionListener
{
    private final String kVersionTag = "1.0";
    private final String kTurnStartMsg = "Turns: 0";

    private HurkleBoard myBoard;
    private String status; 
     
    /** Construct a game with an empty board */
    public HurkleGame()
    {
        this.myBoard = new HurkleBoard();
        status = kTurnStartMsg;
    }
    /** Accessor to the status message */
    public String getStatus() { return status; }
    
    public AbstractTableModel getBoard() 
    { 
        return myBoard; 
    }

    /** Determine if the game is over */
    public boolean gameOver()
    {
        return (myBoard.isWin() || myBoard.isLoser());
    }
    /** Is the board in the winning state? */
    public boolean isWinner()
    {
        return myBoard.isWin();
    }
    /** Accessor to the solution */
    public String getSolution()
    {
        return myBoard.solution();
    }
    /** Start a new game, and optionally cheat. */
    public void newGame(boolean cheatFlag)
    {
        myBoard.reset();
        myBoard.hideHurkle(cheatFlag);
        status = kTurnStartMsg;
        setChanged();
        notifyObservers();
    }
   
    /** Respond to button clicks on the board */
    public void actionPerformed(ActionEvent e) 
    {
        if ("newgame".equals(e.getActionCommand())) 
        {
            newGame(false);
        }
        else if ("cheatgame".equals(e.getActionCommand())) 
        {
            newGame(true);
        }
        else
        {
            // Make a move at the provided location
            int row = (e.getActionCommand().charAt(0) - 'A');
            int col = e.getActionCommand().charAt(1) - '1';
            // Report the results of the move
            if (!gameOver())
            {       
                myBoard.makeMove(row,col);
                myBoard.reveal(row,col);
                status = "Turns: " + myBoard.getTurns() + "   Hint: " + myBoard.getHint();
        
                setChanged();
                notifyObservers();
            }
        }    
    }    
}