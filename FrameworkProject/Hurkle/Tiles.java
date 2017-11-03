package Hurkle;

import Framework.*;
import java.util.*;

/**
 * Enumeration class Tiles - all the possible tiles that can be in a cell.
 * 
 * @author Stephen Kobata
 * @version 11/27/15
 */
enum Tiles implements TileEnum
{
    empty("x"), hurkle("H"), hidden("."),
    one("1"), two("2"), three("3"), four("4"), five("5"), six("6"),
    seven("7"), eight("8");
    
    public static final String startTags = "<HTML><B>";
    public static final String endTags = "</B></HTML>";
    
    private String symbol;
    
    /**
     * Creates a new TileEnum with given symbol.
     * @param sym the symbol.
     */
    private Tiles(String sym)
    {
        symbol = sym;
    }
    
    /**
     * returns the htmlText of the enum.
     * @return the htmlText.
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
        return this == hidden || this == hurkle;
        //this == empty || this == hidden ||
            //this == hidden || this == flag;
    }
    
    /**
     * List of all the names of the tiles in the enum.
     * @return list of all the enums the plugin author created.
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
        // For ever tileEnum.
        for (Tiles tile : Tiles.values())
        {
            names.add(tile);
        }
        return names;
    }
}
