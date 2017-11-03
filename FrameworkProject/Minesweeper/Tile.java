package Minesweeper;

import Framework.*;

/**
 * Tile represents a renderable cell in the AbstractTableModel.
 * 
 * @author Stephen Kobata
 * @version 11/27/15
 */
public class Tile implements Renderable
{
    private final String symbol;
    
    /**
     * Creates a new tile object.
     * @param name the symbol of the TileEnum.
     */
    public Tile(String name)
    {
        symbol = name;
    }
    
    /**
     * Overrides the equals method.
     * @return true if equal, false otherwise.
     * @param other object to compare to.
     */
    @Override
    public boolean equals(Object other)
    {
        // IF not instance of Tile.
        if (!(other instanceof Tile))
        {
            return false;
        }
        // IF names aren't equal.
        if (!symbol.equals(((Tile)other).symbol))
        {
            return false;
        }
        return true;
    }

    // Renderable interface methods.
    /**
     * Returns the TileEnum associated with the symbol.
     */
    public TileEnum getTileEnum()
    {
        // For every TileEnum.
        for (TileEnum tEnum : Tiles.values())
        {
            if (tEnum.getSymbol().equals(symbol))
            {
                return tEnum;
            }
        }
        
        System.err.println("Tile ERROR - TILEENUM NOT FOUND: |" + symbol + "|");
        return null;
    }
}