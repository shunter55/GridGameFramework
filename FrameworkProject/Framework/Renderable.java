package Framework;

/**
 * The class that fills the AbstractTableModel must implement Renderable.
 * Renderable classes can be "rendered", either by a GUI or a text console.
 * See the Swing Tutorial on JTable for more info about cell rendering.
 * @author Stephen Kobata
 * @version 10/26/15
 */
public interface Renderable
{
    /**
     * Returns the TileEnum that is associated with this object.
     * @return the TileEnum that is associated with this object.
     */
    public TileEnum getTileEnum();
}