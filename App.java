import Framework.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import org.apache.commons.cli.*;

/**
 * Entry point for the Framwork.
 * 
 * @author Stephen Kobata
 * @version 11/14/15
 */
public class App
{   
    private static GameModel model;
    private static GameController controller;
    
    /**
     * Main method. Entry point for the Framework.
     * @param args the commandline arguments.
     */
    public static void main(String[] args)
    {
        try
        {   
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args, true);

            parseCommands(cmd);
        }
        catch (ParseException e)
        {
            System.err.println(e);
        }
        
    }
    
    
    
    /**
     * Parses the commands from the command line.
     * @param cmd the command line from which to get
     * the arguments.
     */
    static void parseCommands(CommandLine cmd)
    {
        parsePlugin(cmd);
        
        parseBoardCommands(cmd);

        parseViewCommands(cmd);
    }
    
    /**
     * Creates the model and controller from the plugin argument name.
     */
    static void parsePlugin(CommandLine cmd)
    {
        if (!cmd.hasOption("p"))
        {
            System.err.println("Must provide -p [pluginName]");
        }
        
        String pluginName = cmd.getOptionValue("p").trim();
        
        try
        {
            System.out.println("Running " + pluginName);
            Class pluginModel = Class.forName(pluginName + "." + pluginName);
            model = (GameModel) pluginModel.newInstance();
            
            Class pluginController = Class.forName(
                pluginName + "." + "Controller");
            Class[] arg = new Class[1];
            arg[0] = GameModel.class;
            controller = (GameController) 
                pluginController.getDeclaredConstructor(arg).newInstance(model);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e);
        }
        catch (InstantiationException e)
        {
            System.out.println(e);
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e);
        }
        catch (NoSuchMethodException e)
        {
            System.out.println(e);
        }
        catch (InvocationTargetException e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Parses the board command -b.
     * @param cmd the command line from which to get
     * the arguments.
     */
    
    private static void parseBoardCommands(CommandLine cmd)
    {
        try
        {
            // Cheak for a predefined board.
            if (cmd.hasOption("b"))
            {
                //System.out.println("b " + cmd.getOptionValue("b"));
                String file = cmd.getOptionValue("b").trim();
                Scanner scan = new Scanner(new File(file));
                model.loadModelFromScanner(scan);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found. Starting normally...");
        }
    }
    

    /**
     * Parse the view commands -g -c.
     * @param cmd the command line from which to get
     * the arguments.
     */
    private static void parseViewCommands(CommandLine cmd)
    {
        // Create console according to -o and -i.
        Scanner scan = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);
        try 
        {
            // IF command -i then change Scanner.
            if (cmd.hasOption("i"))
            {
                scan = new Scanner(new File(cmd.getOptionValue("i").trim()));
            }

            // IF command -o then change PrintWriter.
            if (cmd.hasOption("o"))
            {
                writer = new PrintWriter(new File(
                    cmd.getOptionValue("o").trim()));
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Read or write file not found.");
        }

        // IF user wants only console.
        if (cmd.hasOption("c"))
        {
            //System.out.println("c " + cmd.getOptionValue("c"));
            Framework.Console console = 
                new Framework.Console(model, controller, scan, writer);
            model.addObserver(console);
            console.run();
        }
        // IF user wants only gui.
        else //if (cmd.hasOption("g"))
        {
            //System.out.println("g " + cmd.getOptionValue("g"));
            GUI gui = new GUI(model, controller);
            model.addObserver(gui);
            gui.setVisible(true);
        }
        
    }

    /**
     * Creates all the options defined in the spec.
     */
    static Options getOptions()
    {
        Options options = new Options();

        options.addOption("p", "plugin", true, "plugin to use.");
        options.addOption("b", "board", true, "predefined board file.");
        options.addOption("i", "infile", true, 
            "file to use instead of Standard Input.");
        options.addOption("o", "outfile", true, 
            "file to use instead of Standard OutInput.");

        OptionGroup viewGroup = new OptionGroup();
        viewGroup.addOption(new Option("c", "console", false, "use console."));
        viewGroup.addOption(new Option("g", "gui", false, "use gui."));
        options.addOptionGroup(viewGroup);

        return options;
    }
}
