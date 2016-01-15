/*********************
 class Game - stores all game data. Can be saved to txt
*********************/
public class Game{
    private int numPlayers = 6;
    
    private User[] users = new User[6];
    for (int i = 0; i < users.length; i++) {
	users[i] = new Player(i);
    }

    /****************
     boundaries - holds default map bounds in form loX, loY, hiX, hiY
    *****************/
    private int[] boundaries = {0, 0, 149, 99}; //even amount of spots pls tyvm 

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
    private static int phase = 0; 
    
    /****************
     turn - tracks player turn if phase is 1
    *****************/
    private static int turn = -1;
    
    /****************
     turnState - more fine status on turnState.
         0 - reinforcements (card trade in HERE)
         1 - battle (if conquered an enemy, and cards max out
                     set turnState to 0.
         2 - fortify
    *****************/
    private static int turnState = 0;



}
