package Hurkle;


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