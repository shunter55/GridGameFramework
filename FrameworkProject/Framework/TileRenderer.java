package Framework;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;

/**
 * Tile renderer class.
 * @author Stephen Kobata
 * @version 10/25/15
 */
public class TileRenderer extends DefaultTableCellRenderer
{
    private Map<String, ImageIcon> imageMap;

    /** 
     * Construct a CellRenderer that will use the images specified.
     * @param imageMapVal map to use.
     */
    public TileRenderer(Map<String, ImageIcon> imageMapVal)    
    { 
        super();

        this.imageMap = imageMapVal;
    }

    /**
     * sets the mat to use.
     * @param imageMapVal the map to use.
     */
    public void setMap(Map<String, ImageIcon> imageMapVal)
    {
        this.imageMap = imageMapVal;
    }
    
    /**
     * Returns the imageKeySet.
     * @return the image Key Set.
     */
    public Set<String> getImageKeyset()
    {
        return imageMap.keySet();
    }

    /** Set the value to be displayed in the JTable for the given cell. 
     * @param cell a Piece instance
     * @throws ClassCastException If cell is not an instance of Piece.
     */
    @Override
    public void setValue(Object cell)
    {
        setHorizontalAlignment(CENTER);
        setIcon(null);
        setText(null); // default case
        // Error check for null cells
        if (cell != null)
        {
            Renderable tile = (Renderable) cell;
            
            if (tile.getTileEnum().isImage())
            {
                setIcon(imageMap.get(tile.getTileEnum().getSymbol()));
            }
            else
            {
                setText(tile.getTileEnum().htmlText());
            }
        }
    } // end setValue
}