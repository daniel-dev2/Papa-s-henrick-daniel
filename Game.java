/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
import java.util.Stack;
public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Stack<Room> rooms_visited;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        rooms_visited = new Stack<>();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room pizzeria, wall, pub, pyramids, square, destiny, tower, christ;
      
        // create the rooms
        pizzeria = new Room("em frente a pizzaria");
        wall = new Room("no fim da muralha da china");
        pub = new Room("no bar do Ze");
        pyramids = new Room("nas piramides do Egito");
        christ = new Room("em frente ao Cristo Redentor");
        square = new Room("na praca da liberdade");
        tower = new Room("em frente a Torre Eiffel");
        destiny = new Room("no endereço do cliente");
        
        // initialise room exits
        // (norte, leste, sul, oeste)
        pizzeria.setExit("sul", square);
        pizzeria.setExit("leste", pub);

        pub.setExit("oeste", pizzeria);

        square.setExit("norte", pizzeria);
        square.setExit("leste", tower);
        square.setExit("oeste", pyramids);
        square.setExit("sul", christ);

        pyramids.setExit("leste", square);
        pyramids.setExit("norte", wall);

        wall.setExit("sul", pyramids);

        tower.setExit("oeste", square);
        tower.setExit("sul", destiny);

        christ.setExit("norte", square);
        christ.setExit("leste", destiny);

        destiny.setExit("oeste", christ);
        destiny.setExit("norte", tower);


        // create the items
        Item pizza, beer, newspaper, spear, croissant, bible, portao, papyrus;
        pizza = new Item("uma pizza deliciosa", 20);
        newspaper = new Item("um jornal de noticias", 100);
        beer = new Item("uma cerveja gelada", 100);
        spear = new Item("uma lanca afiada", 2000);
        croissant = new Item("um croissant fresquinho", 7000);
        bible = new Item("uma biblia sagrada", 500);
        portao = new Item("o portao da casa do cliente", 80000);
        papyrus = new Item("um papel papiro antigo", 10);

        // setting room items
        pizzeria.addItem(pizza);
        pub.addItem(beer);
        square.addItem(newspaper);
        wall.addItem(spear);
        tower.addItem(croissant);
        christ.addItem(bible);
        destiny.addItem(portao);
        pyramids.addItem(papyrus);

        currentRoom = pizzeria;
        rooms_visited.push(pizzeria);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar. Ate mais.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bem vindo a cidade normal (ou nao) de um entregador de pizza");
        System.out.println("Cidade em que várias coisas interessantes e chatas se encontram normalmente em suas ruas.");
        System.out.println("Digite 'ajuda' se precisar de ajuda");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Eu nao entendo o que quer dizer...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("ajuda")) {
            printHelp();
        }
        else if (commandWord.equals("ir")) {
            goRoom(command);
        }
        else if (commandWord.equals("voltar")) {
            back();
        }
        else if (commandWord.equals("olhar")){
            look();
        }
        else if (commandWord.equals("sair")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Voce esta perdido. Voce esta sozinho. Voce esta vagando");
        System.out.println("ao redor da cidade.");
        System.out.println();
        System.out.println("Suas palavras de comando sao:");
        System.out.println(parser.showCommands());
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Ir pra onde ?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        rooms_visited.push(nextRoom);

        if (nextRoom == null) {
            System.out.println("Nao tem saida!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    public void back() {
        if (rooms_visited.size() == 1) {
            System.out.println("Voce nao pode voltar mais!");
        } else {
            rooms_visited.pop();
            currentRoom = rooms_visited.peek();
            System.out.println(currentRoom.getLongDescription());
        }

    }
    public void look(){
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Sair o que?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
