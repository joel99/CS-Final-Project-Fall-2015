/*********************
 class Game - stores all game data. Can be saved to txt
*********************/

import java.io.*;

public class Game{
    
    public static String[] turnStateNames = {"Reinforce","Attack", "Fortify"};

    private String saveFileName = "saves/game1.rdat";
    
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
    private boolean conqueredAny;

    int numCountries = 42;
    String fileName = "countries.txt";
    String[] countriesIn = new String[numCountries];
    Country[] countries = new Country[numCountries];
	
    int[][] allMapLoc = new int[numCountries][2];
    int[][] allBorders = new int[numCountries][6];//max border count is 6
    /*
      It's also valid to just use allBorders and the indices to
      indicate which country's borders we're referring to, but here's
      an excuse to use a 2d array.
    */

    
    public Game(int numPlayers, String filename){
	users = new User[numPlayers]; 
	for (int i = 0; i < numPlayers; i++) 
	    users[i] = new Player(i);
	map = new Map(filename);
	phase = 0;
	turn = -1;
	turnState = 0;
    }


    public void loadMap() {
	//Loading of Continents and Countries show 2 different methods of mass-object creation, I guess...
		
	//Example of file parsing	
	int ctr = 0;
        try {
	    String line = null;
	    String[] input = null;
            FileReader fileReader = new FileReader(fileName);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null){
		input = line.split("_");
				
		countriesIn[ctr] = input[0];
		allMapLoc[ctr][0] = Integer.parseInt(input[1]);
		allMapLoc[ctr][1] = Integer.parseInt(input[2]);
		for (int i = 3; i < input.length; i++){    
		    //System.out.println(input[i]);
		    allBorders[ctr][i-3] = Integer.parseInt(input[i]) - 1; //-1 because file uses line numbers, not indices... 
		}
		ctr++;
	    }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
			       "Unable to open file '" + 
			       fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
			       "Error reading file '" 
			       + fileName + "'");                  
        }
	        
	for (int i = 0; i < numCountries; i++)
	    countries[i] = new Country(i, countriesIn[i], allBorders[i], allMapLoc[i]);	
	
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
	
    public Map getMap(){
	return map;
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

    public void useReinforcements(int num) {
	reinforcements -= num;
    }
    public void useReinforcements(){
	reinforcements--;
    }

    public void writeSave() {
	try {
	    FileWriter fileWriter = new FileWriter(saveFileName);
	    BufferedWriter saveFile = new BufferedWriter(fileWriter);

	    saveFile.write("test");

	    saveFile.close();
	}
	catch(IOException ex) {
            System.out.println("Error writing to file '" + saveFileName + "'");
        }
    }

    public int calcNumSaves() {
	return 0; //to be changed
    }
	
    
    public Country countryIdentify(String str){
	for (Country c : countries) { //for every country in country array
	    if (c.getName().equals(str)) { //linear search
		return c; 
	    }
	}
	return null; //if no country exists with str as name
	
    }
    
    public boolean conqueredAny(){
	return conqueredAny;
    }
	
    public void setConquered(boolean b){
	conqueredAny = b;
    }

    //checks for generic commands
    public String parse(String str){//takes actions, but returns String for explicit commands
	String ret = "";
	switch(str){
	case "exit": case "e":
	    ret = "QUIT";
	    break;
	case "j":
	    map.pan(-1);
	    ret = "Panned.";
	    break;
	case "k":
	    map.pan(-2);
	    ret = "Panned.";
	    break;
	case "l":
	    map.pan(1);
	    ret = "Panned.";
	    break;
	case "i":
	    map.pan(2);
	    ret = "Panned.";
	    break;
	default:
	    ret = str;
	    break;
	}	
	return ret;
    }	

}
