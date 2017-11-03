package Framework;

import java.util.*;

/**
 * The complete list of cell types that can be in the AbstractTableModel.
 * 
 * @author Stephen Kobata
 * @version 11/14/15
 */
public interface TileEnum
{
    /**
     * returns the htmlText of the enum.
     * @return the htmlText.
     */
    public String htmlText();
    
    /**
     * Returns if the enum has an image or HtmlText.
     * @return if the enum has an image.
     */
    public boolean isImage();
    
    /**
     * List of all the names of the tiles in the enum.
     * @return list of all the enums the plugin author created.
     */
    public List<TileEnum> getNames();
    
    /**
     * Returns the enum's symbol. Must be a single char.
     * @return the symbol.
     */
    public String getSymbol();
}
