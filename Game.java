/*********************
 class Game - stores all game data. Can be saved to txt
*********************/

import java.io.*;
import java.util.*;

public class Game{

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
	    
	private ArrayList<User> users = new ArrayList<>();
    private Map map;
    private int reinforcements;
    private boolean conqueredAny;

    int numCountries = 42;
    private String fileName = "countries.txt";
    private String[] countriesIn = new String[numCountries];
    public Country[] countries = new Country[numCountries];
    public ArrayList<Card> cards = new ArrayList<>(); //need two wildcards
    
    int[][] allMapLoc = new int[numCountries][2];
    int[][] allBorders = new int[numCountries][6];//max border count is 6
    /*
      It's also valid to just use allBorders and the indices to
      indicate which country's borders we're referring to, but here's
      an excuse to use a 2d array.
    */

    
    public Game(int numPlayers, String filename){ 
	for (int i = 0; i < numPlayers; i++) 
	    users.add(new Player(i));
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
	        
	for (int i = 0; i < numCountries; i++){
	    countries[i] = new Country(i, countriesIn[i], allBorders[i], allMapLoc[i]);	
	    cards.add(new Card(countries[i],i % 3));
	}
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
	
	public void setTurnState(int num){
		turnState = num;
	}
	
    public ArrayList<User> getUsers(){
	return users;
    }
	
    public void setTurn(int newTurn){
	turn = newTurn;
    }
	
    public int nextTurn(){
	if (turn == users.size() - 1)
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
	
    public void update(int cId){
	update(countries[cId]);
    }
	
    //assume no wrapping problems (b/c we're good with this stuff right :))
    public void update(Country c){
	int[] coords = c.getMapLoc();
	int y = coords[0]; //x,y are inverted, but let's not get bogged down...;
	int x = coords[1];
	String stat = c.status();
	//REMEMBER TO UPDATE w/ NICKNAME!!!
	//User id here:
	map.set(x++,y, users.get(Integer.parseInt(stat.substring(0,1))).getNick());
	map.set(x++,y, '|'); 
	for (int i = 1; i < stat.length(); i++)
	    map.set(x++,y, stat.charAt(i));
	//gotta overwrite old info
	while (Util.userChars.indexOf(map.get(x,y)) != -1)
		map.set(x,y, ' ');
	}
    public void printMap(){
	System.out.println(map);
    }
	
    public Map getMap(){
	return map;
    }
    
    public User getCurrentUser() {
	return users.get(turn);
    }
    
    public int getReinforcements(){
	return reinforcements;
    }
    public void setReinforcements(int n){
	reinforcements = n;
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

	    saveFile.write(saveFileName); //1
	    saveFile.newLine();
	    saveFile.write(Integer.toString(phase)); //2
	    saveFile.newLine();
	    saveFile.write(Integer.toString(turnState)); //3
	    saveFile.newLine();
	    saveFile.write(Integer.toString(turn)); //4
	    saveFile.newLine();
	    saveFile.write(users.toString()); //5
	    saveFile.newLine();
	    saveFile.write(map.toString()); //6
	    saveFile.newLine();
	    saveFile.write(Integer.toString(reinforcements)); //7
	    saveFile.newLine();
	    saveFile.write(Boolean.toString(conqueredAny)); //8
	    saveFile.newLine();
	    saveFile.write(Integer.toString(numCountries)); //9
	    saveFile.newLine();
	    saveFile.write(countries.toString()); //10
	    saveFile.newLine();
		

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
	str = str.toLowerCase();
	for (Country c : countries) { //for every country in country array
	    if (c.getName().toLowerCase().equals(str)) { //linear search
		return c; 
	    }
	}
	return null; //if no country exists with str as name
    }
	public Card cardIdentify(Country c){
		for (Card card: getCurrentUser().getCards())
			if (card.toString().equals(c.toString()))
				return card;
		return null;
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
	boolean success = false;
	switch(str){
	case "exit": case "e":
	    ret = "QUIT";
	    break;
	case "status":
		ret = "to be implemented";
		break;
	case "map":
		printMap();
		ret = "Map printed.";
		break;
	case "j":
	    map.pan(-1);
	    System.out.println(map);
	    ret = "Panned Left.\n";
	    break;
	case "k":
	    map.pan(-2);
	    System.out.println(map);
	    ret = "Panned Down.\n";
	    break;
	case "l":
	    map.pan(1);
	    System.out.println(map);
	    ret = "Panned Right.\n";
	    break;
	case "i":
	    map.pan(2);
	    System.out.println(map);
	    ret = "Panned Up.\n";
	    break;
	case "cards":
		System.out.println("");
		for (int i = 0; i < getCurrentUser().getCards().size(); i++)
			System.out.println(cards.get(i));
		ret = "Printed cards. \n";
		break;
	case "trade": case "t":
		parse("cards");	//just to print them.
		System.out.println("Please enter the three cards (seperated by a comma ',') to trade in for " + Util.getCardReinforcePredict() + " troops.");
		ret = "Trading...";
		break;
	case "help":
	    ret = "\n=== COMMANDS ===\n" +
		"'help' -- display commands\n" +
		"'i' -- pan map up\n" +
		"'j' -- pan map left\n" +
		"'k' -- pan map down\n" +
		"'l' -- pan map right\n" +
		"'zoom in' -- zoom in once\n" +
		"'zoom out' -- zoom out once\n" +
		"'zoom <country>' -- zoom into a country\n" + 
		"'zoom <continent>' -- zoom into a continent\n" +
		"'trade' -- trade in cards\n" +
		"'cards' -- display cards\n";
	    break;
	default:
	    if ((str.length() >= 5) && (str.substring(0,5).equals("zoom "))) {
		String zoomArg = str.substring(5,str.length());
		System.out.println("zoomarg is " + zoomArg);
		//test if arg is recognized
		if (zoomArg.equals("in")) {
		    map.zoom(1);
		    System.out.println(map);
		    ret = "Zoomed In.\n";
		    success = true;
		} else if (zoomArg.equals("out")) {
			System.out.println("out dtected");
		    map.zoom(-1);
		    System.out.println(map);
		    ret = "Zoomed Out.\n";
		    success = true;
		}

		if (!success) {
		    for (Country c : countries) {
			if (c.getName().equals(zoomArg)) {
			    map.zoom(c,1);
			    System.out.println(map);
			    ret = "Zoomed into " + c.getName() + ".\n";
			    success = true;
			    break;
			}
		    }
		}

		if (!success) {
		    for (Continent co : Util.continents) {
			if (co.toString().equals(zoomArg)) {
			    //map.zoom(co,2);
			    ret = "Continent zoom to be implemented...";
			    success = true;
			    break;
			}
		    }
		}

		if (!success) {
		    System.out.println("'" + zoomArg + "' is not a valid zoom argument");
		}

		
	    } else {	    
		ret = str;
		break;
	    }
	}
	if (!str.equals(ret))
		System.out.println(ret);
	//System.out.println("Player " + getCurrentUser() + "'s turn (TEXT ORDER WILL BE FIXED!):");
	return ret;
    }	

	
    //ADDS RANDOM CARD OF DECK TO CURRENT USER. IMPLEMENT.
    //cards stores bank cards. Assume only need to add card to given user.
    public void addCard(){
	int index = (int) (Math.random() * cards.size());
	getCurrentUser().add(cards.get(index));
	cards.remove(index);
    }
	
	public boolean ownsCard(Country c, User u){
		for (int i = 0; i < u.getCards().size(); i++)
			if (u.getCards().get(i).toString().equals(c.toString()))
				return true;
		return false;
	}
	
	public void trade(Card[] gameCards){
		User user = getCurrentUser();
		for (int i = 0; i < gameCards.length; i++){
			user.getCards().remove(gameCards[i]);
			cards.add(gameCards[i]);
		}
		reinforcements += Util.getCardReinforce();
		System.out.println("Cards traded.");
	}
	
	public Country[] getCountries(){
		return countries;
	}
	
}
