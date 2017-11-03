package Hurkle;

import Framework.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Write a description of class Controller here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Controller implements GameController
{
    private Hurkle game;
    
    /**
     * Creates a new instance of Controller.
     */
    public Controller(GameModel model)
    {
        game = (Hurkle) model;
    }
    
    /**
     * Returns the menuListeners.
     * @return list of menuListeners representing the menus.
     */
    public List<MenuListener> getMenuListeners()
    {
        ArrayList<MenuListener> listeners = new ArrayList<MenuListener>();
        
        listeners.add(new NewGameListener());
        listeners.add(new CheatListener());
        
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
        
        
        return listeners;
    }
    
    /**
     * NewGame listener.
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
            game.newGame(false);
        }
    }
    
    /**
     * Cheat listener.
     */
    private class CheatListener implements MenuListener
    {
        /**
         * Returns the name of the listener.
         * @return the name.
         */
        public String getName()
        {
            return "Cheat";
        }
        
        /**
         * Returns the shortcut.
         * @return the shorcut char.
         */
        public char getShortcutKey()
        {
            return 'C';
        }
        
        /**
         * Returns the shorcut key event.
         * @return the shortcut key event.
         */
        public int getShortcutKeyEvent()
        {
            return KeyEvent.VK_C;
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
            game.newGame(true);
        }
    }
    
    /**
     * Turns listener.
     */
    private class TurnsListener implements ActionListener
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
                    game.setGuessLimit(Integer.parseInt(prefs.getMenuItemVal(
                        "Preferences", "Board Size", size)));
                }
            }
            
        }
    }
}
