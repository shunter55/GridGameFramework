package Collapse;

import Framework.*;
import java.util.*;
import java.lang.*;

/**
 * TileStack class that manages one column in the map.
 * @author Stephen Kobata
 * @version 10/18/15
 */
public class TileStack
{
    // a stack of tiles that represents one column of the Board map.
    private Renderable[] tileStack;
    // number of Tiles in the tileStack.
    private int tileCount;
    // keeps track of change between fall calls.
    private boolean didChange;

    /**
     * Creates a new random TileStack of size size.
     * @param size of the tileStack.
     */
    public TileStack(int size)
    {
        tileCount = 0;
        tileStack = new Renderable[size];
        didChange = false;
    }

    /**
     * Returns the Tile at the given index.
     * @param idx the index at which to get the Tile from.
     * @return the tile at the given index, or a blank tile if nothing is there.
     */
    public Renderable getTileAtIndex(int idx)
    {
        return tileStack[idx];
    }

    /**
     * adds a Tile at the given index. Shifts blocks up.
     * @param tile the Tile to add.
     * @param idx the index at which to add the tile.
     * @assert Tile count is less than tileStack.length 
     * (there is room for the tile)
     */
    public void addTileAtIndex(Renderable tile, int idx)
    {
        //System.out.println("tileCount: " + tileCount + " idx: " + idx);
        assert tileCount < tileStack.length;

        tilesFall();
        // While moveIdx greater than idx.
        for (int moveIdx = tileCount; moveIdx < tileStack.length &&
            moveIdx > idx; moveIdx--)
        {    
            // copy to spot above.
            tileStack[moveIdx] = tileStack[moveIdx - 1];
        }

        tileStack[idx] = tile;
        tileCount++;
        didChange = true;
    }

    /**
     * Removes the Tile at the given index. Blocks arn't shifted 
     * until tilesFall() is called.
     * @param idx the index from which to remove the Tile.
     */
    public void removeTileAtIndex(int idx)
    {
        // set the tile at the given index to null
        tileStack[idx] = null;
        tileCount--;
        didChange = true;
    }

    /**
     * Makes the tiles fall to their lowest position.
     */
    public void tilesFall()
    {
        // IF the stack changed.
        if (didChange)
        {
            boolean tilesRemain = true;

            // For idx < tileStack length
            for (int curSpot = 0; tilesRemain &&
                curSpot < tileStack.length; curSpot++)
            {
                // If current spot does not have a tile.
                if (tileStack[curSpot] == null)
                {
                    int searchIdx = curSpot + 1;
                    // Find the index of first tile above
                    while (searchIdx < tileStack.length &&
                        tileStack[searchIdx] == null)
                    {
                        searchIdx++;
                    }
                    // If there is a tile above.
                    if (searchIdx < tileStack.length)
                    {
                        // Move the tile to curSpot.
                        tileStack[curSpot] = tileStack[searchIdx];
                        tileStack[searchIdx] = null;
                    }
                    // There is no tile above.
                    else
                    {
                        tilesRemain = false;
                    }
                }

            }
        }

        didChange = false;
    }

    /**
     * Returns if the stack is empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty()
    {
        return tileCount == 0;
    }

    /**
     * Returns the tile count.
     * @return number of tiles.
     */
    public int getTileCount()
    {
        return tileCount;
    }


}