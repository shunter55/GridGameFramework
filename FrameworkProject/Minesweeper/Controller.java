package Minesweeper;

import Framework.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The controller for Minesweeper.
 * 
 * @author Stephen Kobata 
 * @version 11/27/15
 */
public class Controller implements GameController
{
    private Minesweeper game;
    
    /**
     * Creates a new instance of Controller.
     */
    public Controller(GameModel model)
    {
        game = (Minesweeper) model;
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
        listeners.add(new PeekListener());
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
            
        listeners.put("Board Size", new BoardSizeListener());
        listeners.put("Difficulty", new DifficultyListener());
        
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
            game.newGame();
        }
    }
    
    /**
     * Peek listener.
     */
    private class PeekListener implements MenuListener
    {
        /**
         * Returns the name of the listener.
         * @return the name.
         */
        public String getName()
        {
            return "Peek";
        }
        
        /**
         * Returns the shortcut.
         * @return the shorcut char.
         */
        public char getShortcutKey()
        {
            return 'P';
        }
        
        /**
         * Returns the shorcut key event.
         * @return the shortcut key event.
         */
        public int getShortcutKeyEvent()
        {
            return KeyEvent.VK_P;
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
            game.peek();
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
            game.cheat();
        }
    }
    
    /**
     * Board Size listener.
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
    
    /**
     * Difficulty listener.
     */
    private class DifficultyListener implements ActionListener
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
                "Preferences", "Difficulty"))
            {
                // IF menu label equals size label.
                if (buttonName.equals(size))
                {
                    int bombCount = Integer.parseInt(prefs.getMenuItemVal(
                        "Preferences", "Difficulty", size));
                    game.changeDifficulty(bombCount);
                }
            }
            
        }
    }
}
