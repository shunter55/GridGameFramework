package Framework;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import org.ini4j.*;

/**
 * Board class that manages the board data.
 * @author Stephen Kobata
 * @version 10/18/15
 */
public class GUI extends JFrame implements Observer
{
    private TileJTable jTable;
    private JLabel statusBar;
    private GameModel game;
    private GameController controller;
    private boolean hofDisplayed;

    private final int kImageWidth = 65;
    private final int kImageHeight = 44;

    /** Prepare the GUI before starting the game.
     *  Initialize the board to a random configuration.
     *  @param model the model to use.
     *  @param control the controller to use.
     */
    public GUI(GameModel model, GameController control)
    {
        super();
        //initPreferences();
        game = model;
        controller = control;
        //game.newGame(); // Generates a random board.
        layoutGUI();
    }

    /** Place all the Swing widgets in the frame of this GUI.
     * Load the default skin.
     * Prepare a grid of pieces with a background image.
     * Setup the Game and Preference menus.
     * @post the GUI is ready to be made visible.  
     */
    public void layoutGUI()
    {
        // set frame title.
        setTitle(game.getTitle());

        // Create a new TileJTable.
        jTable = new TileJTable(new ImageIcon());
        //game.giveModelToTable(jTable);
        jTable.setModel(game.getModel());
        
        // Define layout manager.
        getContentPane().setLayout(
            new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create menu bar.
        JMenuBar menuBar = createMenuBar();
        getRootPane().setJMenuBar(menuBar);
        //new MenuBar(this, game);

        // Create status panel.
        JPanel statusPane = new JPanel();
        statusBar = new JLabel("#####");
        statusBar.setName("status");
        statusPane.add(statusBar);
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);
        updateStatusBar();

        // Set dimensions for each column in board to match the image.
        setColumnDimensions();

        // remove editor makes table not editable
        jTable.setDefaultEditor(Object.class, null);

        // define how tile selection works.
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setCellSelectionEnabled(false);

        // set table name.
        jTable.setName("table");

        // Read "How to use Tables" in Java Swing Tutorial
        loadImages();      
        jTable.setRowHeight(kImageHeight);
        jTable.setOpaque(false);
        jTable.setShowGrid(false);
        jTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add a custom mouse listener that will handle player's clicks.
        jTable.addMouseListener(myMouseListener);

        // add the table to the content pane
        getContentPane().add(jTable);

        pack();
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Used to reset the column dimensions for the jTable 
     * when resizing the board.
     */
    private void setColumnDimensions()
    {
        // Set dimensions for each column in board to match the image.
        for (int col = 0; col < game.getSize(); col++)
        {
            TableColumn column = jTable.getColumnModel()
                .getColumn(col);
            column.setMaxWidth(kImageWidth);
            column.setMinWidth(kImageWidth);
        }   
    }

    /**
     * Loads the images from the current curSkin path and passes 
     * them to the jTable.
     */
    private void loadImages()
    {  
        Map<String, ImageIcon> imageMap = game.getSkin();
        
        // If imageMap doesnt exist.
        if (imageMap == null)
        {
            imageMap = game.getSkin();
        }
        
        // IF baground is faded.
        if (game.backgroundIsFaded())
        {
            jTable.setBackgroundImage(game.getSkin().get("backgroundDisabled"));
        }
        else
        {
            jTable.setBackgroundImage(game.getSkin().get("background"));
        }
        
        jTable.setDefaultRenderer(Renderable.class, new TileRenderer(imageMap));
    }

    /**
     * Listener to respond to mouse clicks on jTable.
     */
    private MouseAdapter myMouseListener = new MouseAdapter()
        {
            public void mouseReleased(MouseEvent ev)
            {
                String pressedKey = InputEvent
                    .getModifiersExText(ev.getModifiersEx());
                
                // obtain the selected cell coordinates.
                int col = jTable.columnAtPoint(ev.getPoint());
                // flip y - Must be flipped on input and when board is read.
                int row = game.getSize() - 1 - jTable.rowAtPoint(ev.getPoint());

                // Create a location.
                Location loc = new Location(col, row); 
                
                // IF left click.
                if (ev.getButton() == MouseEvent.BUTTON1)
                {
                    game.clickLocation(loc);
                }
                // ELSE IF right click.
                else if (ev.getButton() == MouseEvent.BUTTON3)
                {
                    game.rightClickLocation(loc);
                }
            }
        };  // end mouse listener

    /**
     * updates the text in the status bar.
     */
    private void updateStatusBar()
    {
        statusBar.setText(game.statusToString());
    }

    /**
     * Creates "Game" from getMenuListeners
     * Creates "Preferences" from game Preferences and getPreferenceListeners.
     */
    private JMenuBar createMenuBar()
    {
        Preferences pref = game.getPreferences();
        JMenuBar menuBar = new JMenuBar();
        
        menuBar.add(getGameMenu(menuBar));
        
        // FOR every menu.
        for (String menuKey : pref.getMenuKeys())
        {
            JMenu menu = new JMenu(menuKey);
            // FOR every submenu.
            for (String subMenuKey : pref.getSubMenuKeys(menuKey))
            {
                ButtonGroup buttonGroup = new ButtonGroup();
                JMenu subMenu = new JMenu(subMenuKey);
                // FOR every menuItem.
                for (String menuItem : 
                    pref.getMenuItemKeys(menuKey, subMenuKey))
                {
                    JRadioButtonMenuItem subTab = 
                        new JRadioButtonMenuItem(menuItem);
                    
                    subTab.addActionListener(
                        controller.getPreferenceListeners().get(subMenuKey));
                    
                    buttonGroup.add(subTab);
                    subMenu.add(subTab);
                    
                    // IF default preference
                    if (menuItem.equals(
                        pref.getMenuItemKeys(menuKey, subMenuKey).get(0)))
                    {
                        // Set active.
                        subTab.setSelected(true);
                    }
                }
                menu.add(subMenu);
            }
            menuBar.add(menu);
        }
        
        return menuBar;
    }
    
    /**
     * Generates the gameMenu from the controller.
     * @param menuBar the menu bar to add the menuItems to.
     */
    private JMenu getGameMenu(JMenuBar menuBar)
    {
        JMenu gameMenu = new JMenu("Game");
        
        // MAKE game menu.
        for (MenuListener action : controller.getMenuListeners())
        {
            JMenuItem item = new JMenuItem(action.getName());
            
            KeyStroke keyStroke;
            // Add shortcut key.
            if (action.getKeyModifier() < 0)
            {
                keyStroke = KeyStroke.getKeyStroke(action.getShortcutKey());
            }
            else 
            {
                keyStroke = KeyStroke.getKeyStroke(action.getShortcutKeyEvent(),
                    action.getKeyModifier());
            }
            //menuBar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            //    keyStroke, action.getName());
            //menuBar.getActionMap().put(action.getName(), action);
            item.addActionListener(action);
            item.setAccelerator(keyStroke);
            
            if (action.isVisible())
            {
                gameMenu.add(item);
            }
            
        }
        
        gameMenu.addSeparator();
        
        // For all the menus privided by the framework.
        for (MenuListener action : game.getDefaultMenus())
        {
            // Dont add the Prefs tab.
            if (!action.getName().equals("Prefs"))
            {
                JMenuItem item = new JMenuItem(action.getName());
                
                KeyStroke keyStroke;
                // Add shortcut key.
                if (action.getKeyModifier() < 0)
                {
                    keyStroke = KeyStroke.getKeyStroke(action.getShortcutKey());
                }
                else 
                {
                    keyStroke = KeyStroke.getKeyStroke(
                        action.getShortcutKeyEvent(), action.getKeyModifier());
                }
                //menuBar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                //    keyStroke, action.getName());
                //menuBar.getActionMap().put(action.getName(), action);
                item.addActionListener(action);
                item.setAccelerator(keyStroke);
                // If action is visible.
                if (action.isVisible())
                {
                    gameMenu.add(item);
                }
            }
        }
        
        return gameMenu;
    }
    
    /**
     * Updates the view.
     * @param obs the observable.
     * @param obj the command.
     */
    @Override
    public void update(Observable obs, Object obj)
    {   
        if (obj != null && obj.equals("Quit"))
        {
            dispose();
            System.exit(0);
        }
        else if (obj != null && obj.equals("Prefs"))
        {
            
        }
        else if (obj != null && obj.equals("Hall"))
        {
            displayHOFRanking();
        }
        else
        {
            jTable.setModel(game.getModel());
            
            loadImages();
            
            // Set dimensions for each column in 
            // board to match the image.
            setColumnDimensions();
            pack();
        
            updateStatusBar();
        
            repaint();
            
            // Reset gameIsOver when gameIsOver() is reset.
            if (!game.gameIsOver())
            {
                hofDisplayed = false;
            }
            // Display Hall of Fame.
            if (game.getHallOfFame() != null && !hofDisplayed &&
                game.gameIsOver() &&
                game.getHallOfFame().isEligable(game.getScore()))
            {
                displayHOF();
            }
        }
    }
    
    /**
     * Display the Hall Of Fame dialog.
     */
    private void displayHOF()
    {
        String msg = "Your score of " + game.getScore() + 
            " will be entered into the Hall Of Fame.\nEnter your name:";
        
        hofDisplayed = true;
        String name = JOptionPane.showInputDialog(
            this, msg, "Name Entry", JOptionPane.QUESTION_MESSAGE);

        if (name != null)
        {
            game.getHallOfFame().add(name, game.getScore());
        }
    }
    
    /**
     * Display the Hall Of Fame rankings dialog.
     */
    private void displayHOFRanking()
    {
        String list = "";
        
        java.util.List<HallItem> hallItems = 
            game.getHallOfFame().getDispList();
        for (int idx = 0; idx < hallItems.size(); idx++)
        {
            list += (idx + 1) + ") " + hallItems.get(idx).toString() + "\n";
        }
        
        JOptionPane.showMessageDialog(
            this, list, "Hall Of Fame", JOptionPane.PLAIN_MESSAGE);
    }

}