package Collapse;

import Framework.*;
import java.util.*;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;

/**
 * Controller for the Collapse plugin.
 * 
 * @author Stephen Kobata
 * @version 11/21/15
 */
public class Controller implements GameController
{
    private Collapse game;
    
    /**
     * Creates a new instance of Controller.
     */
    public Controller(GameModel model)
    {
        game = (Collapse)model;
    }
    
    /**
     * Returns the menuListeners.
     * @return list of menuListeners representing the menus.
     */
    public List<MenuListener> getMenuListeners()
    {
        ArrayList<MenuListener> listeners = new ArrayList<MenuListener>();
        
        listeners.add(new RestartListener());
        listeners.add(new NewGameListener());
        listeners.add(new UndoListener());
        
        return listeners;
    }
    
    /**
     * Returns preference action listeners.
     * @return a map containing the preference listeners for 
     * each section [ ].
     */
    public Map<String, ActionListener> getPreferenceListeners()
    {
        HashMap<String, ActionListener> listeners = 
            new HashMap<String, ActionListener>();
            
        listeners.put("Board Size", new BoardSizeListener());
        listeners.put("Skin", new SkinListener());
        
        return listeners;
    }
    
    /**
     * Restart listener.
     */
    private class RestartListener implements MenuListener
    {
        /**
         * Returns the name of the listener.
         * @return the name.
         */
        public String getName()
        {
            return "Restart";
        }
        
        /**
         * Returns the shortcut.
         * @return the shorcut char.
         */
        public char getShortcutKey()
        {
            return 'R';
        }
        
        /**
         * Returns the shorcut key event.
         * @return the shortcut key event.
         */
        public int getShortcutKeyEvent()
        {
            return KeyEvent.VK_R;
        }
        
        /**
         * Returns the key modifier.
         * @return the key modifier.
         */
        public int getKeyModifier()
        {
            return InputEvent.ALT_DOWN_MASK;
        }
        
        /**
         * Returns if its visible (Not functioning).
         * @returns true of false if the item is visible.
         */
        public boolean isVisible()
        {
            return true;
        }
        
        /**
         * The action that should happen.
         * @param e the actionEvent.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            game.reset();
        }
    }
    
    /**
     * New Game listener.
     */
    private class NewGameListener implements MenuListener
    {
        /**
         * Returns the name of the listener.
         * @return the name.
         */
        public String getName()
        {
            return "New Game";
        }
        
        /**
         * Returns the shortcut.
         * @return the shorcut char.
         */
        public char getShortcutKey()
        {
            return 'N';
        }
        
        /**
         * Returns the shorcut key event.
         * @return the shortcut key event.
         */
        public int getShortcutKeyEvent()
        {
            return KeyEvent.VK_N;
        }
        
        /**
         * Returns the key modifier.
         * @return the key modifier.
         */
        public int getKeyModifier()
        {
            return InputEvent.ALT_DOWN_MASK;
        }
        
        /**
         * Returns if its visible (Not functioning).
         * @returns true of false if the item is visible.
         */
        public boolean isVisible()
        {
            return true;
        }
        
        /**
         * The action that should happen.
         * @param e the actionEvent.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            game.newGame();
        }
    }
    
    /**
     * Undo listener.
     */
    private class UndoListener implements MenuListener
    {
        /**
         * Returns the name of the listener.
         * @return the name.
         */
        public String getName()
        {
            return "Undo";
        }
        
        /**
         * Returns the shortcut.
         * @return the shorcut char.
         */
        public char getShortcutKey()
        {
            return 'U';
        }
        
        /**
         * Returns the shorcut key event.
         * @return the shortcut key event.
         */
        public int getShortcutKeyEvent()
        {
            return KeyEvent.VK_U;
        }
        
        /**
         * Returns the key modifier.
         * @return the key modifier.
         */
        public int getKeyModifier()
        {
            return InputEvent.ALT_DOWN_MASK;
        }
        
        /**
         * Returns if its visible (Not functioning).
         * @returns true of false if the item is visible.
         */
        public boolean isVisible()
        {
            return true;
        }
        
        /**
         * The action that should happen.
         * @param e the actionEvent.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            game.undo();
        }
    }
    
    /**
     * BoardSize listener.
     */
    private class BoardSizeListener implements ActionListener
    {
        /**
         * The action that should happen.
         * @param e the actionEvent.
         */
        public void actionPerformed(ActionEvent e)
        {
            Preferences prefs = game.getPreferences();
            
            String buttonName = ((AbstractButton)e.getSource()).getText();
            //System.out.println("BoardSize buttonName: " + buttonName);
            // FOR every size in BoardSize.
            for (String size : prefs.getMenuItemKeys(
                "Preferences", "Board Size"))
            {
                // IF menu label equals size label.
                if (buttonName.equals(size))
                {
                    game.newGame(Integer.parseInt(prefs.getMenuItemVal(
                        "Preferences", "Board Size", size)));
                }
            }
            
        }
    }
    
    // A call to setSkin() will change the skin in the GUI.
    /**
     * Skin listener.
     */
    private class SkinListener implements ActionListener
    {
        /**
         * The action that should happen.
         * @param e the actionEvent.
         */
        public void actionPerformed(ActionEvent e)
        {
            Preferences prefs = game.getPreferences();
            
            String buttonName = ((AbstractButton)e.getSource()).getText();
            
            // FOR every size in BoardSize.
            for (String skinName : prefs.getMenuItemKeys("Preferences", "Skin"))
            {
                // IF menu label equals size label.
                if (buttonName.equals(skinName))
                {
                    game.setSkin(prefs.getMenuItemVal(
                        "Preferences", "Skin", skinName), Tiles.cyan);
                    game.newGame();
                }
            }

        }
    }
    
}
