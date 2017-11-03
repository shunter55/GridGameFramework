This project has dependencies on ApacheCommons and ini4j.
   This means you need to include class paths to these frameworks.
   I included these in the project and the Makefile will compile them for you.
   Just remember when running you have to include the class path:
      -cp ".:./Dependencies/apache/commons-cli-1.3.1.jar:./Dependencies/ini4j/ini4j.jar"

Should work using java 1.7 on mac or Linux. May not work with java 1.8.

MAKING AND RUNNING:
   A makefile is included to make and clean the project.
   A sample run is also included in the makefile.
      make: makes the project.
      make clean: cleans the project.
      make run: runs the project with a GUI version of the Collapse plugin.

RUN COMMANDS:
   This program uses command line commands:
      -p : (required) specifies the name of the game plugin to run.
           The name of the plugin in the name of the plugin package.
              “Collapse” - A Collapse game in which you click a group of tiles
                           and they fall down. To win you must remove all tiles.
              “Minesweeper” - A Minesweeper game where you try to flag all the
                              mines. To win uncover all tiles that aren’t mines.
              “Hurkle” - This is not a fully implemented game. This is how much
                         of a simple find hurkle game that I was able to 
                         implement in exactly 50 minutes using the framework
                         I built.
      -b : (optional) uses a predefined board given a file name. Two sample
           files are included for the Collapse and Minesweeper plugins.
              “colBoard.txt” - collapse board
              “mineBoard.txt” - minesweeper board
      -g : (optional) use GUI view. Does not take additional parameters.
           This is the default if -c is not specified.
      -c : (optional) use a Console view. Play in Terminal.
      -i : (optional) For testing. Name of a file to get input from.
      -o : (optional) For testing. Name of a file to write output to.

   -p [Plugin Name]
   -b [File Name]
   -g (no parameter)
   -c (no parameter)
   -i [File Name]
   -o [File Name]

   Ex: java -cp … App -p Collapse -c -b colBoard.txt

PROJECT NOTES:
   In this project I used the Model/View/Controller pattern to create a framework
   that could handle multiple views (GUI and Console) for any board game plugin
   (Collapse, Minesweeper, and Hurkle).

   The framework provides: (some key features)
      Hall of Fame - Leaderboard to keep track of Highscores.
      Skins - Allows GUI to display different skins that the plugin author makes.
      GUI and Console views - Two views one for GUI and one for Terminal.
      Custom Preferences - Plugin author can provide logic for any menu items
         they want to include.
      Custom Status Bar - Plugin author can choose what data to show such as score
         and time.

CLASS STRUCTURE:
   Each game plugin must include:

      GameModel (Abstract Class): Frameworks abstract class that has the abstract   
                 methods that must be written. This handles the high level game
                 logic.
         clickLocation() - Logic for when a tile was left clicked.
         rightClickLocation() - Logic for when a tile was right clicked.
         getSize() - returns the current size of the board.
         statusToString() - returns a string with what should appear in the
            top status bar. (example score and time)
         getTitle() - returns a string representing the title of the game.
         boardToString() - returns a string representing the board for the console.
         getModel() - returns the AbstractTableModel you are using to represent
            your game.
         loadModelFromScanner() - given a scanner reads in the board. For creating
            predefined boards.
         gameIsOver() - returns true or false when your endgame conditions are met.
         getScore() - returns the players current score for the hall of fame.
         backgroundIsFaded() - gives you control over the color of the background.
      
      GameController (Interface): Handles logic for the Menu Items that the plugin
                                    wants to provide. This allows the plugin author
                                    to provide any menu items they want.
         
      AbstractTableModel (Interface - Java): Model that represents the actual tiles
                                               and their locations.

      RenderableTileEnum (Interface): An enum list that represents symbols for each
                                        tile.

      Skins folder: Holds all the skins for tiles. May have different themes.

      “preferences.ini”: Holds static info for menu items such as board size
         and skin names.



