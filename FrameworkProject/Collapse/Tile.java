package Collapse;

import Framework.*;
import java.util.*;
import java.awt.*;

/**
 * Tile class that holds name and location.
 * @author Stephen Kobata
 * @version 10/26/15
 */
public final class Tile implements Renderable
{
    private final String name;
    private final Location location;

    /**
     * Makes a new Tile.
     * @param nameVal the name of the tile.
     */
    public Tile(String nameVal)
    {
        this.name = nameVal;
        this.location = null;
    }  

    /**
     * Constructs a new tile with a location.
     * @param nameVal the name of the tile.
     * @param locationVal the location of the tile.
     */
    public Tile(String nameVal, Location locationVal)
    {
        this.name = nameVal;
        this.location = locationVal;
    }  

    /**
     * Gets the name of the tile.
     * @return the tile's name.
     */
    public String getName()
    {
        return name.toLowerCase();
    }

    /**
     * Get location of tile.
     * @return the location of the tile.
     */
    public Location getLocation()
    {
        return location;
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
        if (!name.equals(((Tile)other).name))
        {
            return false;
        }
        return true;
    }

    // Renderable interface methods.
    /**
     * Returns the tileEnum associated with the symbol.
     */
    public TileEnum getTileEnum()
    {
        // For every tile enum.
        for (TileEnum tEnum : Tiles.values())
        {
            if (tEnum.getSymbol().equals(name))
            {
                return tEnum;
            }
        }
        
        System.err.println("ERROR - TILEENUM NOT FOUND!!!");
        return null;
    }
    
}