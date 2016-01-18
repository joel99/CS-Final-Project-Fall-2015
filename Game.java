/*********************
 class Game - stores all game data. Can be saved to txt
*********************/
public class Game{


    public static String[] turnStateNames = {"Reinforce","Attack", "Fortify"};

    /****************
     phase - int describing game state.
        -1 - exit (to break out of while loop).
         0 - menu (loaded on startup).
         menu desc:
             Start New Game (know your instructions!)
             Load Game
             Settings
             Exit
         1 - game start
	 2 - game midgame
         3 - in-game paused (settings)
         settings desc:
             Save Game
             Exit
    ****************/
    private static int phase; 
    
    /****************
     turn - tracks player turn if phase is 1
    *****************/
    private static int turn;
    
    /****************
     turnState - more fine status on turnState.
         0 - reinforcements (card trade in HERE)
         1 - battle (if conquered an enemy, and cards max out
                     set turnState to 0.
         2 - fortify
    *****************/
    private static int turnState;

	    
    private User[] users;
    private Map map;
    private int reinforcements;
	
    
    public Game(int numPlayers, String filename){
	users = new User[numPlayers]; 
	for (int i = 0; i < numPlayers; i++) 
	    users[i] = new Player(i);
	map = new Map(filename);
	phase = 0;
	turn = -1;
	turnState = 0;
    }
	
    public int getPhase(){
	return phase;
    }
	
    public int getTurn(){
	return turn;
    }
	
    public int getTurnState(){
	return turnState;
    }
	
    public User[] getUsers(){
	return users;
    }
	
    public void setTurn(int newTurn){
	turn = newTurn;
    }
	
    public int nextTurn(){
	if (turn == users.length - 1)
	    turn = 0;
	else
	    turn++;
	return turn;
    }
	
    public int setPhase(int newPhase){
	phase = newPhase;
	return phase;
    }
	
    public int nextTurnState(){
	if (turnState == 2)
	    turnState = 0;
	else
	    turnState++;
	return turnState;
    }
	
    //assume no wrapping problems (b/c we're good with this stuff right :))
    public void update(Country c){
	int[] coords = c.getMapLoc();
	int y = coords[0]; //x,y are inverted, but let's not get bogged down...;
	int x = coords[1];
	String stat = c.status();
	//REMEMBER TO UPDATE w/ NICKNAME!!!
	//User id here:
	map.set(x++,y, users[Integer.parseInt(stat.substring(0,1))].getNick());
	x++; //for the space
	for (int i = 1; i < stat.length(); i++)
	    map.set(x++,y, stat.charAt(i));
	//first char is userId, convert to user info. Track owner or ownerId????
    }
	
    public void printMap(){
	System.out.println(map);
    }

    public User getCurrentUser() {
	return users[turn];
    }
    
    public int getReinforcements(){
	return reinforcements;
    }
	
    public void calcReinforce(){
	reinforcements = getCurrentUser().calcReinforcements();
    }

    public void useReinforcement() {
	reinforcements--;
    }

    
	

}
