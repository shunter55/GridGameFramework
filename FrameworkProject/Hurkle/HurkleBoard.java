package Hurkle;

import javax.swing.table.*;
/** The board in a game of find the hurkle. */
public class HurkleBoard extends AbstractTableModel
{
    private final int boardsize;
    private int hideRow; 
    private int hideCol;
    private boolean found = false;
    private String hint;
    private HurklePiece[][] grid;

    /** The count of wrong guesses */
    private int guessCount = 0;

    /** The limit on number of incorrect guesses allowed. */
    private final int guessLimit = 5;

    // Build a table model for the game number specified
    public HurkleBoard()
    {
        boardsize = 9;
        grid = new HurklePiece[boardsize][boardsize];
        reset();
        hideHurkle(false);
    }

    @Override
    public boolean isCellEditable(int row, int col) {return false;}

    @Override
    public int getRowCount() { return grid.length; }

    @Override
    public int getColumnCount() 
    {
        return grid[0].length;  // assuming all rows have same number of columns
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        return HurklePiece.copy(grid[row][col]);
    }

    /** Make a move at the given location */
    public void makeMove(int row, int col)
    {
        // Does guess match hiding spot?
        if (row == hideRow && col == hideCol)
        {
            found = true; // found hurkle
        }
        else            
        {
            // increment count of wrong guesses
            guessCount = guessCount + 1;
            // Record the piece as guessed
            grid[row][col].setGuess(guessCount);
            // Generate a hint message
            hint = "";
            if(row < hideRow)
            {
                hint = "SOUTH";
            } else if (row > hideRow) 
            {
                hint = "NORTH";
            }

            if(col < hideCol )
            {
                hint += "EAST";
            } else if (col > hideCol) 
            {
                hint += "WEST";
            }
        }
    }

    /** 
     *  Determine if the player won 
     *  @return true if hurkle was found
     */
    public boolean isWin()
    {
        return found; 
    }

    /** 
     *  Determine if the player lost the game (ran out of turns)
     *  @return true if the number of turns equals the limit
     */
    public boolean isLoser()
    {
        return guessCount == guessLimit;
    }

    /** Accessor to turn count */
    public int getTurns()
    {
        return guessCount;
    }

    /** Return the hiding spot as a string message */
    public String solution()
    {
        return "" + (char)('A'+hideRow) + " " + (hideCol+1); 
    }

    /** 
     * return the hint to display
     * @return the hint as a formatted, displayable string.
     */
    public String getHint()
    {
        return hint;
    }

    /** Reset the board (clear all the pieces) */
    public void reset()
    {
        guessCount = 0;
        found = false;
        // create the grid
        for (int Row=0; Row < grid.length; Row++)
        {
            for (int Column=0; Column < grid.length; Column++)
            {
                grid[Row][Column] = new HurklePiece();
            }
        }
    }
    /** hide the hurkle at a random spot. */
    public void hideHurkle(boolean cheat)
    {
        // Cheat mode: put hurkle at known position
        if (cheat)
        {
            hideRow = 4;
            hideCol = 4;
        }
        else
        {
            hideRow = (int)(Math.random()*9);
            hideCol = (int)(Math.random()*9);
        }
        grid[hideRow][hideCol] = new HurklePiece(true);
    } // end reset

    /** Uncover a spot on the board */
    public void reveal(int row, int col)
    {
        grid[row][col].reveal();
    }
} // end class
