package Minesweeper;

import Framework.*;
import javax.swing.table.*;
import java.util.*;

/**
 * Represents a 2D board with renderable tiles.
 * 
 * @author Stephen Kobata
 * @version 11/27/15
 */
public class Board extends AbstractTableModel
{
    // Array containing the locations of the bombs.
    private Tile[][] bombMap;
    // Array containing the 2D board of Renderable Tiles.
    private Tile[][] map;
    // How many moves were made.
    private int moveCount;
    // How many flags were placed.
    private int flagCount;
    // if the game is lost or not.
    private boolean gameIsLost;
    
    /**
     * initialize a board with size and bomb count.
     * @param size the board size.
     * @param bombCount the number of bombs.
     */
    public Board(int size, int bombCount)
    {
        newGame(size, bombCount);        
    }
    
    /**
     * initialize a board from a scanner. File must not have
     * spaces.
     * @param scan the scanner that is scanning a file.
     */
    public Board(Scanner scan)
    {
        bombMap = null;
        //bombCount = 0;
        
        // Make bombMap from the scanner.
        int yIdx = 0;
        // WHILE there is another row.
        while (scan.hasNextLine())
        {
            int xIdx = 0;
            String line = scan.nextLine();
            
            if (bombMap == null)
            {
                bombMap = new Tile[line.length()][line.length()];
                map = new Tile[line.length()][line.length()];
            }
            
            //Scanner lineScan = new Scanner(line);
            // WHILE there is another column
            for (int idx = 0; idx < line.length(); idx++)
            {
            //while (lineScan.hasNext())
            //{
                //String symbol = lineScan.next().trim().toLowerCase();
                String symbol = "" + line.charAt(idx);
                // If symbol is a bomb.
                if (symbol.equals(Tiles.bomb.getSymbol()))
                {
                    bombMap[yIdx][xIdx] = new Tile(symbol);
                    //bombCount++;
                }
                // Else put a clear tile in the spot.
                else
                {
                    bombMap[yIdx][xIdx] = new Tile(Tiles.clear.getSymbol());
                }
                
                xIdx++;
            }
            yIdx++;
        }
        
        //bombCount = bombCount * getSize() * getSize();
        
        // initialize the map.
        initMap();

    }
    
    /**
     * Creates a new game.
     * @param size the size of the board.
     * @param bombCount the number of bombs on the board.
     */
    public void newGame(int size, int bombCount)
    {
        bombMap = new Tile[size][size];
        map = new Tile[size][size];
        
        moveCount = 0;
        flagCount = 0;
        gameIsLost = false;
        
        initBombMap(bombCount);
        initMap();
    }
    
    /**
     * Initializes the bomb map with the bomb locations.
     * Bomb locations are randomized.
     * @param bombCount the number of bombs to put on the map.
     */
    private void initBombMap(int bombCount)
    {
        // Create an array with specified number of bombs.
        List<Tile> tileList = new ArrayList<Tile>();
        for (int idx = 0; idx < bombMap.length * bombMap[0].length; idx++)
        {
            // Add number of bombs.
            if (idx < bombMap.length * bombMap.length / bombCount)
            {
                tileList.add(new Tile(Tiles.bomb.getSymbol()));
            }
            // The rest of the tiles are "clear".
            else
            {
                tileList.add(new Tile(Tiles.clear.getSymbol()));
            }
        }
        Collections.shuffle(tileList);
        
        // Fill bombMap with the shuffled elements from tileList.
        int listIdx = 0;
        // FOR every y value.
        for (int yIdx = 0; yIdx < bombMap.length; yIdx++)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < bombMap[0].length; xIdx++)
            {
                bombMap[yIdx][xIdx] = tileList.get(listIdx++);
            }
        }        
    }
    
    /**
     * Initializes the map that is displayed to the player.
     * All cells are initially hidden.
     */
    private void initMap()
    {
        // FOR every y value.
        for (int yIdx = 0; yIdx < bombMap.length; yIdx++)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < bombMap[0].length; xIdx++)
            {
                map[yIdx][xIdx] = new Tile(Tiles.hidden.getSymbol());
            }
        }
    }
    
    /**
     * Executed when a player left clicks a cell.
     * Calls the recursive reveal.
     * @loc the location that was clicked.
     */
    public void clickLocation(Location loc)
    {
        // Cheak if bomb was clicked.
        if (getAdjBombCount(loc) < 0)
        {
            // Game is over.
            peek();
            revealLocation(loc);
            gameIsLost = true;
        }
        else
        {
            recReveal(loc);
        }
        
        // IF not clear.
        if (!getTileAtLocMap(loc).getTileEnum().getSymbol().equals(
            Tiles.clear.getSymbol()))
        {
            moveCount++;
        }
    }
    
    /**
     * Adds a flag to the specified location.
     * @loc the location to add the flag.
     */
    public void addFlag(Location loc)
    {
        // IF hidden.
        if (getTileAtLocMap(loc).getTileEnum().equals(Tiles.hidden))
        {
            setTileAtLoc(new Tile(Tiles.flag.getSymbol()), loc);
            flagCount++;
        }
        // IF flag.
        else if (getTileAtLocMap(loc).getTileEnum().equals(Tiles.flag))
        {
            setTileAtLoc(new Tile(Tiles.hidden.getSymbol()), loc);
            flagCount--;
        }
    }
    
    /**
     * Returns the current flag count.
     * @return the number of flags on the board.
     */
    public int getFlagCount()
    {
        return flagCount;
    }
    
    /**
     * Recursively reveals tiles based off the starting location.
     * @param loc the starting location.
     */
    private void recReveal(Location loc)
    {
        if (loc.getX() >= 0 && loc.getX() < map[0].length &&
            loc.getY() >= 0 && loc.getY() < map.length)
        {
            if (getTileAtLocMap(loc).getTileEnum().equals(Tiles.flag))
            {
                flagCount--;
            }
            revealLocation(loc);
            
            if (getAdjBombCount(loc) == 0)
            {
                Location right = new Location(loc.getX() + 1, loc.getY());
                if (tileAtLocIsHidden(right))
                {
                    recReveal(right);
                }
                Location left = new Location(loc.getX() - 1, loc.getY());
                if (tileAtLocIsHidden(left))
                {
                    recReveal(left);
                }
                Location up = new Location(loc.getX(), loc.getY() + 1);
                if (tileAtLocIsHidden(up))
                {
                    recReveal(up);
                }
                Location down = new Location(loc.getX(), loc.getY() - 1);
                if (tileAtLocIsHidden(down))
                {
                    recReveal(down);
                }
                Location upRight = new Location(loc.getX() + 1, loc.getY() + 1);
                if (tileAtLocIsHidden(upRight))
                {
                    recReveal(upRight);
                }
                Location upLeft = new Location(loc.getX() + 1, loc.getY() - 1);
                if (tileAtLocIsHidden(upLeft))
                {
                    recReveal(upLeft);
                }
                Location downRight = new Location(
                    loc.getX() - 1, loc.getY() + 1);
                if (tileAtLocIsHidden(downRight))
                {
                    recReveal(downRight);
                }
                Location downLeft = new Location(
                    loc.getX() - 1, loc.getY() - 1);
                if (tileAtLocIsHidden(downLeft))
                {
                    recReveal(downLeft);
                }
            }
        }
    }
    
    /**
     * Cheaks if the tile at the location is hidden or not.
     * @param loc the location to cheak.
     * @return true if the tile is hidden.
     */
    private boolean tileAtLocIsHidden(Location loc)
    {
        // If location is within bounds of map.
        if (loc.getX() >= 0 && loc.getX() < map[0].length &&
            loc.getY() >= 0 && loc.getY() < map.length)
        {
            // If the tile is hidden.
            if (getTileAtLocMap(loc).getTileEnum().equals(Tiles.hidden) || 
                getTileAtLocMap(loc).getTileEnum().equals(Tiles.flag))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reveals the cell at the location.
     * @param loc the location to reveal.
     */
    private void revealLocation(Location loc)
    {
        // IF bomb.
        if (getAdjBombCount(loc) < 0)
        {
            setTileAtLoc(new Tile(Tiles.exploded.getSymbol()), loc);
        }
        // IF clear.
        else if (getAdjBombCount(loc) == 0)
        {
            setTileAtLoc(new Tile(Tiles.clear.getSymbol()), loc);
        }
        // Numbered cell.
        else
        {
            int bombCount = getAdjBombCount(loc);
            setTileAtLoc(new Tile("" + bombCount), loc);
        }
    }
    
    /**
     * Reveals the tile in the map.
     * @param loc the location of the cell to reveal.
     * @return the number of bombs nearby, or -1 if a bomb was at the location.
     */
    private int getAdjBombCount(Location loc)
    {
        int bombCount = 0;
        
        // IF bomb was at the location.
        if (getTileAtLoc(loc).getTileEnum().getSymbol().equals(
            Tiles.bomb.getSymbol()))
        {
            //setTileAtLoc(new Tile("X"), loc);
            return -1;
        }
        
        // Cheak every direction for a bomb.
        bombCount += bombAtLocation(new Location(loc.getX() + 1, loc.getY()));
        bombCount += bombAtLocation(new Location(loc.getX() - 1, loc.getY()));
        bombCount += bombAtLocation(new Location(loc.getX(), loc.getY() + 1));
        bombCount += bombAtLocation(new Location(loc.getX(), loc.getY() - 1));
        bombCount += bombAtLocation(
            new Location(loc.getX() + 1, loc.getY() + 1));
        bombCount += bombAtLocation(
            new Location(loc.getX() + 1, loc.getY() - 1));
        bombCount += bombAtLocation(
            new Location(loc.getX() - 1, loc.getY() + 1));
        bombCount += bombAtLocation(
            new Location(loc.getX() - 1, loc.getY() - 1));

        //setTileAtLoc(new Tile("" + bombCount), loc);
        return bombCount;
    }
    
    /**
     * Sets the tile at a location.
     * @param tile the tile to put at the location.
     * @param loc the location to put the tile.
     */
    private void setTileAtLoc(Tile tile, Location loc)
    {
        map[loc.getY()][loc.getX()] = tile;
    }
    
    /**
     * Returns the tile in the bombMap at the given location.
     * @param loc the location to get the Tile of.
     * @return the Tile that was at the given location.
     */
    private Tile getTileAtLoc(Location loc)
    {
        return bombMap[loc.getY()][loc.getX()];
    }
    
    /**
     * Returns the Tile in the map that the player sees.
     * @param loc the location to get the Tile of.
     * @return the Tile at the given location.
     */
    private Tile getTileAtLocMap(Location loc)
    {
        return map[loc.getY()][loc.getX()];
    }
    
    /**
     * Cheaks if there is a bomb at the given location.
     * Returns 1 if there is a bomb at the location,
     * and 0 if there isnt a bomb.
     * @return 1 if there is a bomb, 0 otherwise.
     */
    private int bombAtLocation(Location loc)
    {
        // IF in bounds of map.
        if (loc.getX() >= 0 && loc.getX() < map[0].length &&
            loc.getY() >= 0 && loc.getY() < map.length)
        {
            // IF bomb at location.
            if (getTileAtLoc(loc).getTileEnum().getSymbol().equals(
                Tiles.bomb.getSymbol()))
            {
                return 1;
            }
        }
        return 0;
    }
    
    /**
     * Reveals the entire board.
     */
    public void peek()
    {
        // FOR every y value.
        for (int yIdx = 0; yIdx < bombMap.length; yIdx++)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < bombMap[0].length; xIdx++)
            {
                Location curLoc = new Location(xIdx, yIdx);
                if (bombAtLocation(curLoc) > 0)
                {
                    map[yIdx][xIdx] = new Tile(Tiles.bomb.getSymbol());
                }
                else if (getAdjBombCount(curLoc) == 0)
                {
                    map[yIdx][xIdx] = new Tile(Tiles.clear.getSymbol());
                }
                else
                {
                    map[yIdx][xIdx] = new Tile("" + getAdjBombCount(curLoc));
                }
            }
        }
        
        gameIsLost = true;
    }
    
    /**
     * Returns the board as a string with TileEnum symbols.
     * The board that the player sees.
     * @return a string representing the board.
     */
    public String toString()
    {
        String output = "";
        
        // FOR every y value.
        for (int yIdx = map.length - 1; yIdx >= 0; yIdx--)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < map[0].length; xIdx++)
            {
                Location curLoc = new Location(xIdx, yIdx);
                output += "  " +
                    getTileAtLocMap(curLoc).getTileEnum().getSymbol();
            }
            output += "\n";
        }
        
        return output;
    }
    
    /**
     * Returns how many moves were made so far.
     * @return the number of moves made.
     */
    public int getMoveCount()
    {
        return moveCount;
    }
    
    /**
     * Returns if the game was lost.
     * @return if the game was lost or not.
     */
    public boolean gameIsLost()
    {
        // game is over because a bomb was clicked.
        return gameIsLost;
    }
    
    /**
     * Returns if the game was won.
     * @return if the game was won or not.
     */
    public boolean gameIsWon()
    {
        // IF game is over because you won.
        for (int yIdx = 0; yIdx < bombMap.length; yIdx++)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < bombMap[0].length; xIdx++)
            {
                Location curLoc = new Location(xIdx, yIdx);
                // IF not bomb and map is hidden.
                if (!getTileAtLoc(curLoc).getTileEnum().equals(Tiles.bomb) &&
                    getTileAtLocMap(curLoc).getTileEnum().equals(Tiles.hidden))
                {
                    return false;
                }
            }
        }
        
        if (gameIsLost())
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * Resets the board.
     */
    public void reset()
    {
        initMap();
        moveCount = 0;
        flagCount = 0;
        gameIsLost = false;
    }
    
    /**
     * Initializes the board so that:
     * Top left of the board is a bomb,
     * the cell to the right of it is a 1.
     */
    public void cheat()
    {
        List<Tile> tileList = new ArrayList<Tile>();
        for (int idx = 0; idx < bombMap.length * bombMap[0].length; idx++)
        {
            // Add number of bombs.
            if (idx == bombMap.length * bombMap[0].length - bombMap[0].length)
            {
                tileList.add(new Tile(Tiles.bomb.getSymbol()));
            }
            // The rest of the tiles are "clear".
            else
            {
                tileList.add(new Tile(Tiles.clear.getSymbol()));
            }
        }
        
        int listIdx = 0;
        // FOR every y value.
        for (int yIdx = 0; yIdx < bombMap.length; yIdx++)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < bombMap[0].length; xIdx++)
            {
                bombMap[yIdx][xIdx] = tileList.get(listIdx++);
            }
        }
        
        // FOR every y value.
        for (int yIdx = 0; yIdx < bombMap.length; yIdx++)
        {
            // FOR every x value.
            for (int xIdx = 0; xIdx < bombMap[0].length; xIdx++)
            {
                map[yIdx][xIdx] = new Tile(Tiles.clear.getSymbol());
            }
        }
        
        map[map.length - 1][0] = new Tile(Tiles.hidden.getSymbol());
        map[map.length - 1][1] = new Tile(Tiles.hidden.getSymbol());
    }
    
    // AbstractTableModel methods.
    /**
     * Returns the tile at the row & col.
     * @param row the row of the tile.
     * @param col the column of the tile.
     * @return the Tile that was at the location.
     */
    public Renderable getValueAt(int row, int col)
    {
        return map[getRowCount() - 1 - row][col];
    }
    
    /**
     * Returns how many columns there are.
     * @return the number of columns.
     */
    public int getColumnCount()
    {
        return map[0].length;
    }
    
    /**
     * Returns how many rows there are.
     * @return the number of rows.
     */
    public int getRowCount()
    {
        return map.length;
    }
    
}
