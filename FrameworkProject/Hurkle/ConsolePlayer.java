package Hurkle;

import java.awt.event.ActionEvent;
import java.util.Scanner;
import javax.swing.table.*;

/**
 * A console-based user interface to the Find the Hurkle game.
 */
public class ConsolePlayer implements java.util.Observer
{
    /** reference to the controller */
    private HurkleGame  model;
    /** input reader */
    private Scanner console;

    /** Set the controller to be used by the console to control the game. */
    public void setControl(HurkleGame aController)
    {
        this.model = aController;
    }

    /** The main event loop that drives the application.
     * @param b (Ignored)
     */
    public void setVisible(boolean b)
    {
        ActionEvent event;
        try
        {
            // establish a keyboard reader
            console = new Scanner(System.in);

            // Main event loop: loop forever
            while (true)
            {
                // get the player's move
                String response = getMove();
                // Create an event for this move
                event = new ActionEvent(this, 0, response);
                // Call controller to process the move
                model.actionPerformed(event);
            }
            // END LOOP
        }
        // if exception occurs display the exception message
        catch (java.io.IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Asks the user for a move until a valid move is entered.
     */
    private String getMove() throws java.io.IOException
    {
        boolean isValid = false;
        String input = "";

        // WHILE move is invalid LOOP
        while (!isValid)
        {
            // Prompt for a move
            System.out.print("New game, Cheat, Quit, or move: ");

            // Read the move (a line from the console)
            input = console.nextLine();

            // IF input is one-character THEN
            if (input.length() == 1)
            {
                // IF input is "new game" THEN
                if (input.equals("n"))
                {
                    // Handle cheat here
                    input = "newgame";
                    isValid = true;
                }
                // IF input is "cheat" THEN
                if (input.equals("c"))
                {
                    // Handle cheat here
                    input = "cheatgame";
                    isValid = true;
                }
                // IF input is "quit" THEN
                if (input.equals("q"))
                {
                    input = "quit";
                    isValid = true;
                    // Println
                    System.out.println();
                    // Exit
                    System.exit(0);
                }
            }
            // END IF
            // IF input is 2-char command (indicates a move ) THEN
            if (input.length() == 2)
            {
                // Validity check the move here 
                isValid = true;
                char upper = Character.toUpperCase(input.charAt(0));
                input = "" + upper + input.charAt(1);
            }
            // END IF
            // IF move not valid THEN
            if (!isValid)
            {
                // Display "Invalid move"
                System.out.println("Invalid move");
            }
        }
        // END LOOP

        // Return the move
        return input;
    }

    /** {@inheritDoc} */
    public void update(java.util.Observable obs, Object obj)
    {
        // Call display computer hand
        displayBoard();
        displayStatus();
        showGameOver();
    }

    /** Display board  */
    private void displayBoard()
    {
        AbstractTableModel grid = model.getBoard();
        System.out.println("    1   2   3   4   5   6   7   8   9");
        for (int Row=0; Row < grid.getRowCount(); Row++)
        {
            System.out.print((char)('A'+Row));
            for (int Column=0; Column < grid.getColumnCount(); Column++)
            {
                System.out.print(grid.getValueAt(Row,Column));
            }
            System.out.println();
        } 
        System.out.println();
    }

    /** Display the status message */
    private void displayStatus()
    {
        System.out.println("   " + model.getStatus() + "   ");
    }

    /** If the game is over, show the end game message */
    public void showGameOver()
    {
        if (model.gameOver())
        {
            // Display the game over message
            System.out.println("-------------- game over");
            // Did the player win the game? 
            if (model.isWinner())
            {
                System.out.println("You found the hurkle!");
            }
            else 
            {
                System.out.println("Game Over - You Lose.");
                System.out.println("Hurkle was hiding at " + model.getSolution());
            }           
        }
        // END IF
    }

    // Local main to launch the UI
    public static void main(String[] args)
    {
        // Create the UI 
        ConsolePlayer ui = new ConsolePlayer();
        HurkleGame game = new HurkleGame();
        ui.setControl(game);
        game.addObserver(ui); 
        game.newGame(false);

        ui.setVisible(true);
    }


}