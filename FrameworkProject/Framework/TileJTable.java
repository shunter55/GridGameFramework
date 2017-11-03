package Framework;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/** 
 * Our custom JTable has special features for displaying images and 
 * a background.
 * @author Stephen Kobata
 * @version 10/26/15
 */
public class TileJTable extends JTable
{
    private ImageIcon background;

    /**
     * Custom JTable that displayes the tiles modeled in board.
     * @param model the TableModel to use.
     * @param backgroundImg the background to use.
     */
    public TileJTable(TableModel model, ImageIcon backgroundImg)
    {
        super(model);
        this.background = backgroundImg;
    }
    
     /**
     * Custom JTable that displayes the tiles modeled in board.
     * @param backgroundImg the background to use.
     */
    public TileJTable(ImageIcon backgroundImg)
    {
        super();
        this.background = backgroundImg;
    }

    /**
     * Tell JTable it should expect each column to contain Renderable things,
     * and should select the corresponding renderer for displaying it.
     * @param column the column to get.
     * @return Class the class.
     */
    public Class getColumnClass(int column)
    {
        return Renderable.class;
    }

    /**
     * Allow the background to be displayed.
     * @return the component.
     * @param renderer the TableCellRenderer to use.
     * @param row the row.
     * @param column the column.
     */
    public Component prepareRenderer(TableCellRenderer renderer, int row,
    int column)
    {
        Component component = super.prepareRenderer(renderer, row, column);
        // We want renderer component to be
        // transparent so background image is visible
        if (component instanceof JComponent)
        {
            ((JComponent) component).setOpaque(false);
        }
        return component;
    }

    /**
     * Sets the background Image.
     * @param backgroundImg the background image to use.
     */
    public void setBackgroundImage(ImageIcon backgroundImg)
    {
        this.background = backgroundImg;
    }

    /**
     * Override paint so as to show the table background.
     * @param gfx the Graphic to use.
     */
    public void paint(Graphics gfx)
    {
        // paint an image in the table background
        if (background != null)
        {
            //Image backgroundImage = GrayFilter
                //.createDisabledImage(background.getImage());
            Image backgroundImage = background.getImage();
            // If game is over.
            //if (((Board)getModel()).getTileCount() == 0)
            //{
            //    backgroundImage = background.getImage();
            //}
            //gfx.drawImage(background.getImage(), 0, 0, null, null);
            gfx.drawImage(backgroundImage, 0, 0, null, null);
        }
        // Now let the paint do its usual work
        super.paint(gfx);
    }

}