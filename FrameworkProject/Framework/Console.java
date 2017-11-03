package Framework;

import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Console view is a view in th terminal.
 * 
 * @author Stephen Kobata
 * @version 11/8/15
 */
public class Console implements Observer
{
    private GameModel game;
    private GameController controller;
    private PrintWriter writer;
    private Scanner scan;
    
    private int[] selectedPrefs;
    private boolean prefState;
    private boolean running = true;
    private boolean hofDisplayed;
    
    private final char kRightClick = '.';
    
    /**
     * Create Console.
     * @param model the model to use.
     * @param control the controller to use.
     * @param scanner the scanner to use.
     * @param printWriter the printWriter to use.
     */
    public Console(GameModel model, GameController control,
        Scanner scanner, PrintWriter printWriter)
    {
        game = model;
        controller = control;
        
        scan = scanner;
        writer = printWriter;
        
        int size = model.getPreferences().getSubMenuKeys("Preferences").size();
        selectedPrefs = new int[size];
        
        printView();
    }
    
    /**
     * Gives control to console.
     * Scannes using the given scanner.
     */
    public void run()
    {   
        String command = "";

        // scan from scanner.
        while (scan.hasNext() && running)
        {
            command = scan.next();
            // IF user had clicked preference before.
            if (prefState)
            {
                String lowerCmd = command.toLowerCase();
                // For each preference command the user gave.
                for (int idx = 0; idx < lowerCmd.length(); idx++)
                {
                    executePrefCommand("" + lowerCmd.charAt(idx));
                }
            }
            // IF in regular state.
            else
            {
                executeCommand(command.toUpperCase());
            }
            
            // IF quit was called.
            if (!running)
            {
                break;
            }
        }
    }
    
    /**
     * Excecutes the command.
     * @param command the command to be excecuted.
     */
    private void executeCommand(String command)
    {
        boolean commandFound = false;
        List<MenuListener> menuActions = controller.getMenuListeners();
        
        int menuIdx = 1;
        // Cheak to see if the command was a menu command.
        try
        {
            for (MenuListener action : menuActions)
            {
                // IF menu command using int, execute it.
                if (Integer.parseInt(command) == menuIdx++)
                {
                    action.actionPerformed(
                        new ActionEvent(this, menuIdx - 1, command));
                    commandFound = true;
                }
    
            }
            
            for (MenuListener action : game.getDefaultMenus())
            {
                if (Integer.parseInt(command) == menuIdx++)
                {
                    action.actionPerformed(new ActionEvent(
                        this, menuIdx - 1, command));
                    commandFound = true;
                }
            }
            
            // Hidden menu 0 that clears hall of fame.
            if (Integer.parseInt(command) == 0)
            {
                game.getHallOfFame().clear();
                writer.write("Hall Of Fame cleared.\n");
                writer.flush();
                commandFound = true;
            }
        }
        catch (NumberFormatException e)
        {
            if (e.toString().charAt(0) != '.')
            {
            }
        }
        
        // IF the command wasnt a menu command.
        if (!commandFound)
        {
            boolean isRightClick = false;
            // Cheak for right click.
            if (command.charAt(0) == kRightClick)
            {
                isRightClick = true;
                command = command.substring(1);
            }
            
            try
            {
                sendClickFromCommand(command, isRightClick);
            }
            catch (CommandNotFoundException e)
            {
                writer.println("Command not found - " + command);
                writer.flush();
            }
            
        }
        
    }
    
    /**
     * Changes preferences given the string of commands.
     * @param command the string of commands, each char is 1 command.
     */
    private void executePrefCommand(String command)
    {
        int totalCount = 0;
        // For each count in prefCounts
        for (int count : getPrefCounts())
        {
            totalCount += count;
        }
        
        // Invalid command.
        if (command.length() > 1 || command.length() <= 0 ||
            command.charAt(0) < 'a' ||
            command.charAt(0) > 'a' + (totalCount - 1))
        {
            writer.write("Command not found - " + command);
        }
        else
        {
        
            char cmd = command.toLowerCase().charAt(0);
                
            Map<String, ActionListener> listeners = 
                controller.getPreferenceListeners();
            
            int idx = 0;
            int curSetting = getPrefCounts().get(idx);
            // within bounds of how many [ ] exist. Less than the number.
            while (idx <= getPrefCounts().size() - 1 &&
                cmd - 'a' > curSetting - 1)
            {
                curSetting += getPrefCounts().get(++idx);
            }
            
            // Call the right action listener.
            Object[] sectionNames = listeners.keySet().toArray();
            Object[] itemNames = game.getPreferences().getMenuItemKeys(
                "Preferences", (String)sectionNames[idx]).toArray();
            int prefNumInSection = (cmd - 'a') - 
                (curSetting - getPrefCounts().get(idx));
            AbstractButton button =
                new JMenuItem((String)itemNames[prefNumInSection]);
            
            listeners.get((String)sectionNames[idx]).actionPerformed(
                new ActionEvent(button, idx, "prefs"));
                
            prefState = false;
            selectedPrefs[idx] = prefNumInSection;
        
        }
    }
    
    /**
     * Counts how many preferences are in each section.
     * @return a list of how many preferences are in each [ ] section.
     */
    private List<Integer> getPrefCounts()
    {
        List<Integer> counts = new ArrayList<Integer>();
        Preferences prefs = game.getPreferences();
        
        int idx = 0;
        for (String menuName : prefs.getSubMenuKeys("Preferences"))
        {
            counts.add(0);
            for (String menuItemName : prefs.getMenuItemKeys(
                "Preferences", menuName))
            {
                counts.set(idx, counts.get(idx) + 1);
            }
            idx++;
        }
        
        return counts;
    }
    
    /**
     * Handles a click command.
     * @param command the click command to be handled.
     */
    private void sendClickFromCommand(
        String command, boolean isRightClick) throws CommandNotFoundException
    {
        int xCoord, yCoord;
        
        try
        {
            xCoord = Integer.parseInt("" +
                command.substring(1, command.length())) - 1;
            yCoord = (game.getSize() - 1) - (command.charAt(0) - 'A');
        }
        catch (NumberFormatException e)
        {
            throw new CommandNotFoundException();
        }
        
        // If not a valid command.
        if (yCoord > game.getSize() - 1 || yCoord < 0)
        {
            throw new CommandNotFoundException();
        } 
        // IF not a valid command.
        else if (xCoord > game.getSize() - 1 || xCoord < 0)
        {
            throw new CommandNotFoundException();
        }
        
        // IF it is a left click.
        if (!isRightClick)
        {
            game.clickLocation(new Location(xCoord, yCoord));
        }
        else 
        {
            game.rightClickLocation(new Location(xCoord, yCoord));
        }
    }
    
    /**
     * Prints the title.
     */
    private void printTitle()
    {
        //System.out.println("Collapse by Stephen Kobata");
        writer.print(game.getTitle() + "\n");
    }
    
    /**
     * Prints the status.
     */
    private void printStatus()
    {
        //System.out.println(game.statusToString());
        writer.print(game.statusToString() + "\n");
    }
    
    private final int kNumDidgets = 10;
    /**
     * Prints the board.
     */
    private void printBoard(String board)
    {
        String boardFinal = "   ";
        char curChar = 'A';
        
        // Make the numbers.
        for (int col = 1; col <= game.getSize(); col++)
        {
            boardFinal += "  " + col % kNumDidgets;
        }
        boardFinal += "\n ";
        
        Scanner scanBoard = new Scanner(board);
        // Make the letters.
        while (scanBoard.hasNextLine())
        {
            boardFinal += "" + curChar++ + ":" + scanBoard.nextLine() + "\n ";
        }
        
        // Add the dashes.
        for (int num = 0; num < game.getSize(); num++)
        {
            boardFinal += "---";
        }
        boardFinal += "--\n";
        
        //System.out.println(boardFinal);
        writer.print(boardFinal);
    }
    
    /**
     * Prints the options.
     */
    private void printOptions()
    {
        String output = "";
        
        int num = 1;
        
        // Get plugin MenuListeners.
        for (MenuListener action : controller.getMenuListeners())
        {
            output += num++ + ") " + action.getName() + " ";
        }
        
        // Get predefined framework MenuListeners.
        for (MenuListener action : game.getDefaultMenus())
        {
            output += num++ + ") " + action.getName() + " ";
        }
        
        writer.println(output);
    }
    
    /**
     * Prints the view.
     */
    private void printView()
    {
        printTitle();
        printStatus();
        printBoard(game.boardToString());
        printOptions();
        writer.flush();
    }
    
    private void printPrefs()
    {
        Preferences prefs = game.getPreferences();
        
        String output = "";
        char curLetter = 'a';
        
        int menuIdx = 0;
        for (String menuName : prefs.getSubMenuKeys("Preferences"))
        {
            output += "[" + menuName + "]\n";
            int position = 0;
            for (String menuItemName : prefs.getMenuItemKeys(
                "Preferences", menuName))
            {
                String outputName;
                if (selectedPrefs[menuIdx] == position)
                {
                    outputName = menuItemName.toUpperCase();
                }
                else
                {
                    outputName = menuItemName.toLowerCase();
                }
                
                output += "(" + curLetter++ + ") " + outputName + " = " +
                    prefs.getMenuItemVal(
                        "Preferences", menuName, menuItemName) + "\n";
                    
                position++;
            }
            menuIdx++;
        }
        
        output += "Your choice?\n";
        
        writer.print(output);
    }
    
    private void printHallRanking()
    {
        String output = "Hall Of Fame\n";
        
        java.util.List<HallItem> hallItems = 
            game.getHallOfFame().getDispList();
        for (int idx = 0; idx < hallItems.size(); idx++)
        {
            output += (idx + 1) + ") " + hallItems.get(idx).toString() + "\n";
        }
        
        writer.print(output);
    }
    
    private void displayHOF()
    {
        String msg = "Your score of " + game.getScore() + 
            " will be entered into the Hall Of Fame.\nEnter your name: ";
            
        writer.write(msg);
        writer.flush();
        String name = scan.next();
        
        game.getHallOfFame().add(name, game.getScore());
    }
    
    /**
     * Updates the console.
     * @param obs the observable that updated.
     * @param obj the obj the object passed in.
     */
    public void update(Observable obs, Object obj)
    {
        // IF prefs display prefs.
        if (obj != null && obj.equals("Prefs"))
        {
            printPrefs();
            writer.flush();
            prefState = true;
        }
        // IF quit quit the game.
        else if (obj != null && obj.equals("Quit"))
        {
            running = false;
        }
        else if (obj != null && obj.equals("skin"))
        {            
        }
        else if (obj != null && obj.equals("timer"))
        {            
        }
        else if (obj != null && obj.equals("Hall"))
        {
            printHallRanking();
            writer.flush();
        }
        // if its not a timer or skin update.
        else
        {
            printView();
            
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
     * Exception for when a command is invalid.
     */
    private class CommandNotFoundException extends Exception
    {
    }
}
