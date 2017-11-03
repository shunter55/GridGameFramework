package Framework;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;

/**
 * Interface for a MenuListener. Represents a menuItem in a view.
 * 
 * @author Stephen Kobata
 * @version 11/21/15
 */
public interface MenuListener extends ActionListener
{
    /**
     * The name of the menuItem.
     * @return the name.
     */
    public String getName();
    
    /**
     * The shortcut key such as 'r' for "Restart".
     * @return the shortcut key.
     */
    public char getShortcutKey();
    
    /**
     * The shortcut key event such as KeyEvent.VK_R for "Restart".
     * @return the shortcut key.
     */
    public int getShortcutKeyEvent();
    
    /**
     * The key modifier such as InputEvent.ALT_DOWN_MASK for alt.
     * return negative number for no modifier.
     * @return int which represents a key.
     */
    public int getKeyModifier();
    
    /**
     * If you want the item to be displayed or not. (for secret items)
     * @return true if the menu should be shown.
     */
    public boolean isVisible();
}
