package Collapse;

import Framework.*;
import java.util.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import org.ini4j.*;

/**
 * Board class that manages the board data.
 * @author Stephen Kobata
 * @version 10/18/15
 */
public class Board extends AbstractTableModel implements Serializable
{
    // Size of the board.
    private int boardSize;
    // Array containing the Tiles on the board.
    private TileStack[] map;
    // number of moves made.
    private int moveCount;
    // Stack of tiles that can be reinstantiated.
    private Stack<String> undoStack;
    // max number of tiles so random can generate a random value.
    private final int kNumTiles = 3;

    /**
     * Creates a new Board object with a map of size size.
     * @param size the size of the map for the board.
     */
    public Board(int size)
    {
        newGame(size);

        fillRandom();

        saveBoard();
    }

    /**
     * Creates a new Board object from a Scanner using a boardString.
     * @param scan Scanner that is reading from a boardToString.
     */
    public Board(Scanner scan)
    {       
        moveCount = 0;
        undoStack = new Stack<String>();
        Scanner scanWithSpace = new Scanner(boardScannerToString(scan));
        layoutBoardFromScanner(scanWithSpace);
        saveBoard();
    }

    /**
     * Sets the board layout to a board read from a Scanner 
     * using boardToString. 
     * (Creates a new map)
     * @param scan the Scanner that is reading from a boardToString.
     */
    private void layoutBoardFromScanner(Scanner scan)
    {
        ArrayList<ArrayList<Tile>> tilesArr = 
            new ArrayList<ArrayList<Tile>>();
        // WHILE there is another row.
        while (scan.hasNextLine())
        {
            ArrayList<Tile> rowArr = new ArrayList<Tile>();
            String line = scan.nextLine();
            Scanner lineScan = new Scanner(line);
            // WHILE there is another column
            while (lineScan.hasNext())
            {
                rowArr.add(new Tile(lineScan.next().trim().toLowerCase()));
                //rowArr.add(new Tile(lineScan.next().trim().toLowerCase()));
            }
            tilesArr.add(0, rowArr);
        }

        boardSize = tilesArr.size();
        map = new TileStack[boardSize];
        initTileStacks();

        // FOR each item in rowArray
        for (int yIdx = 0; yIdx < boardSize; yIdx++)
        {
            // FOR each item in tilesArr.
            for (int xIdx = 0; xIdx < boardSize; xIdx++)
            {
                // IF valid string.
                if (tilesArr.get(yIdx).get(xIdx).getTileEnum().getSymbol().equals("x") ||
                    tilesArr.get(yIdx).get(xIdx).getTileEnum().getSymbol().equals("o") ||
                    tilesArr.get(yIdx).get(xIdx).getTileEnum().getSymbol().equals("+")) 
                {
                    map[xIdx].addTileAtIndex(
                        tilesArr.get(yIdx).get(xIdx), yIdx);
                }

            }
        }

    }

    /**
     * Initializes a new game.
     * @param size the size of the new board to make.
     */
    public void newGame(int size)
    {
        boardSize = size;
        map = new TileStack[boardSize];
        initTileStacks();
        moveCount = 0;
        undoStack = new Stack<String>();

        //readPrefFile();
    }

    /**
     * Initializes all the TileStacks in map.
     */
    private void initTileStacks()
    {
        // FOR each stack in the map.
        for (int idx = 0; idx < boardSize; idx++)
        {
            map[idx] = new TileStack(boardSize);
        }
    }

    /**
     * fill the board with random Tiles.
     */
    private void fillRandom()
    {
        String[] nameArr = {"x", "o", "+"};
        Random rand = new Random();
        // For each TileStack in map.
        for (TileStack stack : map)
        {
            // Add tiles to the TileStack until its full.
            for (int idx = 0; idx < boardSize; idx++)
            {
                //assert stack != null;
                stack.addTileAtIndex(
                    new Tile(nameArr[rand.nextInt(kNumTiles)]), idx);
            }
        }
    }

    /**
     * Removes a single tile at the specified location. Decrements tilesLeft.
     * @param location a location object containing the 
     * x and y coordinates of the Tile to remove.
     */
    public void removeTileAtLocation(Location location)
    {   
        // ShiftToCenter us handled in removeAdjacent.
        map[location.getX()].removeTileAtIndex(location.getY()); 

        //shiftColumnsToCenter();
    }

    /**
     * Adds a single tile at the location specified.
     * @param tile the Tile to be added. location - location to add the tile.
     * @param location the location to add the tile.
     */
    public void addTileAtLocation(Renderable tile, Location location)
    {
        map[location.getX()].addTileAtIndex(tile, location.getY());
    }

    /**
     * Returns the center bounds.
     * @return the center bounds depending on odd or even number board size.
     */
    private int[] getCenterBounds()
    {
        int centerUpperBound;
        int centerLowerBound;
        // if board size is even.
        if (boardSize % 2 == 0)
        {   
            centerLowerBound = boardSize / 2 - 1;
            centerUpperBound = centerLowerBound + 1;
        }
        // if board size is odd.
        else 
        {
            centerLowerBound = boardSize / 2 + 1 - 1;
            centerUpperBound = centerLowerBound;
        }
        
        int[] arr = new int[2];
        arr[0] = centerLowerBound;
        arr[1] = centerUpperBound;
        
        return arr;
    }
    
    /**
     * Shifts all the TileStacks in map to the center.
     */
    public void shiftColumnsToCenter()
    {
        int[] arr = getCenterBounds();
        
        int centerUpperBound = arr[1];
        int centerLowerBound = arr[0];
        // if board size is even.
        //if (boardSize % 2 == 0)
        //{   
        //    centerLowerBound = boardSize / 2 - 1;
        //    centerUpperBound = centerLowerBound + 1;
        //}
        // if board size is odd.
        //else 
        //{
        //    centerLowerBound = boardSize / 2 + 1 - 1;
        //    centerUpperBound = centerLowerBound;
        //}

        // for each column in the map.
        for (int idx = 0; idx < boardSize; idx++)
        {
            // IF the stack is empty.
            if (map[idx].isEmpty())
            {
                // IF idx is above centerUpperBound
                if (idx >= centerUpperBound)
                {
                    // put the tile on the undoStack representing the shift.
                    //undoStack.push(new Tile("<", new Location(idx, 0)));

                    int moveDist = 1;
                    // FOR every index above idx copy the stack down.
                    for (int copyIdx = idx; copyIdx + moveDist < boardSize;
                        copyIdx++)
                    {
                        // find the next non-empty stack.
                        while (copyIdx + moveDist < boardSize - 1 &&
                            map[copyIdx + moveDist].isEmpty())
                        {
                            moveDist++;
                        }
                        map[copyIdx] = map[copyIdx + moveDist];
                    }
                    map[boardSize - 1] = new TileStack(boardSize);
                }
                // IF idx is below centerLowerBound
                else if (idx <= centerLowerBound)
                {
                    // put the tile on the undoStack representing the shift.
                    //undoStack.push(new Tile(">", new Location(idx, 0)));

                    int moveDist = -1;
                    // FOR every index above idx copy the stack down.
                    for (int copyIdx = idx; copyIdx + moveDist >= 0; copyIdx--)
                    {
                        // find the next non-empty stack.
                        while (copyIdx + moveDist > 0 &&
                            map[copyIdx + moveDist].isEmpty())
                        {
                            moveDist--;
                        }
                        map[copyIdx] = map[copyIdx + moveDist];
                    }
                    map[0] = new TileStack(boardSize);
                }

            }
        }

    }

    /**
     * Updates moveCount, saves the board, and removes all touching tiles. 
     * Next makes the tiles fall and shifts them to the center.
     * @return true if a tile was clicked.
     * @param location the location of the tile to start removing.
     */
    public boolean removeAdjacentTiles(Location location)
    {
        // if there is a tile at location
        if (tileAtLocation(location))
        {
            moveCount++;
            saveBoard();
        }
        else 
        {
            return false;
        }

        // If there are two of the same kind of tile next to eachother.
        if (isAdjacent(location))
        {   
            // remove the tiles.
            removeAdjacentTilesHelper(location);
            // have changed stacks fall.
            fallAll();

            shiftColumnsToCenter();
        }
        
        return true;
    }

    /**
     * Used by removeAdjacentTiles to see if there are at
     * least 2 tiles touching.
     * @return true if there are 2 tiles touching, false otherwise.
     * @param location the location of the tile you are cheaking.
     */
    private boolean isAdjacent(Location location)
    {
        Tile clickedTile = (Tile) getTileAtLocation(location);

        // Cheak right.
        Location right = new Location(location.getX() + 1, location.getY());
        // IF matching tile to right.
        if (tileAtLocation(right) && 
            getTileAtLocation(right).equals(clickedTile))
        {
            return true;
        }

        // Cheak left.
        Location left = new Location(location.getX() - 1, location.getY());
        // IF matching tile to left.
        if (tileAtLocation(left) &&
            getTileAtLocation(left).equals(clickedTile))
        {
            return true;
        }
        
        // Check up.
        Location up = new Location(location.getX(), location.getY() + 1);
        // IF matching tile up.
        if (tileAtLocation(up) && 
            getTileAtLocation(up).equals(clickedTile))
        {
            return true;
        }
        
        // Cheak down.
        Location down = new Location(location.getX(), location.getY() - 1);
        // IF matching tile down.
        if (tileAtLocation(down) &&
            getTileAtLocation(down).equals(clickedTile))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if there is a tile at the location.
     * @return true if there is a tile at the location.
     * @param loc the location to cheak.
     */
    private boolean tileAtLocation(Location loc)
    {
        // IF your are in the bounds of the map.
        if (loc.getX() < getSize() && loc.getX() >= 0 &&
            loc.getY() < getSize() && loc.getY() >= 0)
        {
            // IF there is a tile at the location.
            if (getTileAtLocation(loc) != null && 
                !getTileAtLocation(loc).getTileEnum().getSymbol().equals(" "))
            {
                return true;
            }
        }
        return false;
    }
    

    /**
     * Used by removeAdjacentTiles to recursively remove tiles touching the 
     * one at the specified location.
     * @param location the location of the tile to start removing from.
     */
    private void removeAdjacentTilesHelper(Location location)
    {
        Renderable clickedTile = getTileAtLocation(location);
        
        // Remove the clicked tile.
        removeTileAtLocation(location);

        // Cheak right.
        Location right = new Location(location.getX() + 1, location.getY());
        // IF matching tile to right.
        if (tileAtLocation(right) && 
            getTileAtLocation(right).equals(clickedTile))
        {
            removeAdjacentTilesHelper(right);
        }

        // Cheak left.
        Location left = new Location(location.getX() - 1, location.getY());
        // IF matching tile to left.
        if (tileAtLocation(left) &&
            getTileAtLocation(left).equals(clickedTile))
        {
            removeAdjacentTilesHelper(left);
        }
        
        // Check up.
        Location up = new Location(location.getX(), location.getY() + 1);
        // IF matching tile up.
        if (tileAtLocation(up) && 
            getTileAtLocation(up).equals(clickedTile))
        {
            removeAdjacentTilesHelper(up);
        }
        
        // Cheak down.
        Location down = new Location(location.getX(), location.getY() - 1);
        // IF matching tile down.
        if (tileAtLocation(down) &&
            getTileAtLocation(down).equals(clickedTile))
        {
            removeAdjacentTilesHelper(down);
        }
    }

    /**
     * Undo's the last move and pops it off the undoStack.
     */
    public void undo()
    {
        // IF the undoStack is not empty. (there is something to undo)
        if (!undoStack.empty())
        {
            String mapString = undoStack.pop();

            // Dont undo the first move.
            if (undoStack.empty())
            {
                undoStack.push(mapString);
            }
            // Normal undo.
            else
            {           
                Scanner scan = new Scanner(mapString);

                layoutBoardFromScanner(scan);

                moveCount++;
            }
        }
    }

    /**
     * returns the number of tiles left on the board.
     * @return the number of tiles on the board.
     */
    public int getTileCount()
    {
        int tilesLeft = 0;
        // FOR ever TileStack in map.
        for (TileStack stack : map)
        {
            tilesLeft += stack.getTileCount();
        }

        return tilesLeft;
    }

    /**
     * Returns the number of moves made so far.
     * @return the number of moves made so far.
     */
    public int getMoveCount()
    {
        return moveCount;
    }

    /**
     * Returns the size of the board.
     * @return the size of the board.
     */
    public int getSize()
    {
        return boardSize;
    }

    /**
     * Calls tilesFall in every TileStack. Makes the tiles in every stack fall.
     */
    public void fallAll()
    {
        // FOR every TileStack in map.
        for (TileStack stack : map)
        {
            stack.tilesFall();
        }
    }

    /**
     * Returns the Tile at the specified location.
     * @return the tile at the specified location in the map.
     * @param location the location at which to get the tile.
     */
    private Renderable getTileAtLocation(Location location)
    {
        return map[location.getX()].getTileAtIndex(location.getY());
    }

    /**
     * Prints the board in text to the console.
     */
    public void printBoard()
    {
        System.out.println("----------------");
        System.out.println(boardToString());
        System.out.println("----------------");
    }

    /**
     * Resets the board to its first state. (original state)
     */
    public void reset()
    {
        String origBoard = "";
        // While undoStack is not empty.
        while (!undoStack.empty())
        {
            origBoard = undoStack.pop();
        }

        Scanner scan = new Scanner(origBoard);
        layoutBoardFromScanner(scan);
        saveBoard();
        moveCount = 0;

    }
    /**
     * Saves the board state so you can undo to it.
     */
    private void saveBoard()
    {
        undoStack.push(boardToString());
    }

    /**
     * Makes a string representing the pieces on the board "_"
     * is an empty space.
     * @return the string that represents the pieces on the board.
     */
    public String boardToString()
    {
        String str = "";
        // FOR every column.
        for (int yIdx = boardSize - 1; yIdx >= 0; yIdx--)
        {
            // FOR every row.
            for (int xIdx = 0; xIdx < boardSize; xIdx++)
            {
                // Cheak if there is a tile at location.
                if (getTileAtLocation(new Location(xIdx, yIdx)) != null &&
                    !getTileAtLocation(new Location(xIdx, yIdx))
                        .getTileEnum().getSymbol().equals(" "))
                {
                    str += getTileAtLocation(new Location(xIdx, yIdx))
                        .getTileEnum().getSymbol() + " ";
                }
                // otherwise use the empty tile character.
                else 
                {
                    // _ is a null tile.
                    str += "_" + " ";
                }
            }
            str += "\n";
        }
        return str;
    }
    
    /**
     * Makes a string representing the pieces on the board "  "
     * is an empty space.
     * @return the string that represents the pieces on the board.
     */
    public String boardToStringSpace()
    {
        String str = "";
        // FOR every column.
        for (int yIdx = boardSize - 1; yIdx >= 0; yIdx--)
        {
            // FOR every row.
            for (int xIdx = 0; xIdx < boardSize; xIdx++)
            {
                // Cheak if there is a tile at location.
                if (getTileAtLocation(new Location(xIdx, yIdx)) != null &&
                    !getTileAtLocation(new Location(xIdx, yIdx))
                        .getTileEnum().getSymbol().equals(" "))
                {
                    str += "  " + getTileAtLocation(new Location(xIdx, yIdx))
                        .getTileEnum().getSymbol();
                }
                // otherwise use the empty tile character.
                else 
                {
                    // " " is a null tile.
                    str += " " + "  ";
                }
            }
            str += "\n";
        }
        return str;
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

    // AbstractTableModel Methods.
    /**
     * Returns the object at a row and column index in the map.
     * @return The object at the spot in the map.
     * @param row The indexes for row 
     * @param col theindex for column in the map.
     */
    @Override
    public Object getValueAt(int row, int col)
    {
        return map[col].getTileAtIndex(boardSize - 1 - row); // flip y
    }

    /**
     * Returns the count of columns in the map.
     * @return the number of columns in the map.
     */
    @Override
    public int getColumnCount()
    {
        return boardSize;
    }

    /**
     * Returns the number of rows in the map.
     * @return the number of rows in the map.
     */
    @Override 
    public int getRowCount()
    {
        return boardSize;
    }

}