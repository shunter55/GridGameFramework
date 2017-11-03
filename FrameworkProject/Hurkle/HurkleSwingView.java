package Hurkle;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observer;
import java.util.Enumeration;

/** 
 *  Graphical UI for Find the Hurkle. 
 */
public class HurkleSwingView extends JFrame implements Observer
{

    /* Main components of the GUI */
    private JLabel myStatus = new JLabel();
    private JTable table;
    private HurkleGame model;

    /* Square dimensions */
    private int TileWidth = 40;
    private int TileHeight = 30;
    

    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public HurkleSwingView()
    {
        super("Find the Hurkle");

        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }

    public void setModel(HurkleGame game)
    {
        this.model = game;
    }
    /** Place all the Swing widgets in the frame of this GUI.
     * @post the GUI is visible.  
     */
    public void layoutGUI()
    {
       
        table = new JTable(model.getBoard());
        
        // Set the dimensions for each column in the board to match the image */        
        for (Enumeration em = table.getColumnModel().getColumns(); em.hasMoreElements();)
        {
            TableColumn column = (TableColumn) em.nextElement();
            column.setMaxWidth(TileWidth);
            column.setMinWidth(TileWidth);
        }

        // Define the layout manager that will control order of components
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        // Create a panel to hold the buttons
        JPanel buttonPane = new JPanel();
        JButton btnNew = new JButton("New Game");
        btnNew.setActionCommand("newgame");
        btnNew.setMnemonic('N');
        btnNew.addActionListener(model);
        buttonPane.add(btnNew);
        JButton btnCheat = new JButton("");
        btnCheat.setActionCommand("cheatgame");
        btnCheat.setMnemonic('C');
        btnCheat.addActionListener(model);
        btnCheat.setBorderPainted(false);
        btnCheat.setFocusPainted(false);
        btnCheat.setMargin(new java.awt.Insets(0,0,0,0));        
        buttonPane.add(btnCheat);
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(buttonPane);

        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        myStatus.setText(model.getStatus());
        statusPane.add(myStatus);
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);

        // Define the characteristics of the table that shows the game board        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setRowHeight(TileHeight);

        table.setOpaque(false);
        table.setShowGrid(false);

        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(table);

        // Define the mouse listener that will handle player's clicks.
        table.addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent ev)
            {
                int col = table.getSelectedColumn() + 1;
                char row = (char) ( (int)'A' + table.getSelectedRow());
                String response = "" + row + col;
                ActionEvent event = new ActionEvent(this, 0, response);
                // Call controller to process the move
                model.actionPerformed(event);
            }
        }
        );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    } // end layout

    /** Update the view */
    public void update(java.util.Observable obs, Object obj)
    {
        myStatus.setText(model.getStatus());
        repaint();
        table.clearSelection();  // remove highlighting from cell
        
        if (model.gameOver())
        {
            // Did the player win the game? 
            if (model.isWinner())
            {
                JOptionPane.showMessageDialog(this,
                "You found the hurkle!","Win Dialog", 
                JOptionPane.INFORMATION_MESSAGE);                        
            }
            else 
            {
                JOptionPane.showMessageDialog(this, "You lost.\nThe hurkle was hiding at "
                    +model.getSolution(), "Lose Msg", JOptionPane.ERROR_MESSAGE);                            
            }           
        }
    }
    // Local main to launch the GUI
    public static void main(String[] args)
    {
        // Create the GUI 
        HurkleSwingView frame = new HurkleSwingView();
        HurkleGame game = new HurkleGame();
        frame.setModel(game);
        game.addObserver(frame); 
        frame.layoutGUI();   // do the layout of widgets
               
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
}  // end class

