package Collapse;

import Framework.*;
import java.util.*;

/**
 * Enum of all the tiles that can appear on the board.
 * 
 * @author Stephen Kobata
 * @version 11/15/15
 */
enum Tiles implements TileEnum
{    
    purple("x"), cyan("o"), green("+");
    public static final String startTags = "<HTML><B>";
    public static final String endTags = "</B></HTML>";
    
    private String symbol;
    
    /**
     * Creates a new TileEnum with symbol.
     * @param sym the symbol.
     */
    private Tiles(String sym)
    {
        symbol = sym;
    }
    
    /**
     * Returns the htmlText.
     * @return the html Text.
     */
    public String htmlText()
    {
        return startTags + symbol + endTags;
    }
    
    /**
     * Returns if the enum has an image or HtmlText.
     * @return if the enum has an image.
     */
    public boolean isImage()
    {
        return true;
    }
    
    /**
     * Returns the enum's symbol. Must be a single char.
     * @return the symbol.
     */
    public String getSymbol()
    {
        return symbol;
    }
    
    /**
     * List of all the names of the tiles in the enum.
     * @return list of all the enums the plugin author created.
     */
    public List<TileEnum> getNames()
    {
        ArrayList<TileEnum> names = new ArrayList<TileEnum>();
        // For every tileEnum.
        for (Tiles tile : Tiles.values())
        {
            names.add(tile);
        }
        return names;
    }
}
