/*********************
 class Game - stores all game data. Can be saved to txt
 *********************/
public class Game{
    private int numPlayers = 6;
    
    private User[] users = new User[6];
    for (int i = 0; i < users.length; i++) {
	users[i] = new Player(i);
    }




}
