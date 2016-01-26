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
	Util.wait(1000);
	turnState = num;
    }
	
    public ArrayList<User> getUsers(){
	return users;
    }

    public void removeUser(User user) {
	users.remove(user);
    }
	
    public void setTurn(int newTurn){
	Util.wait(1000);
	turn = newTurn;
    }
	
    public int nextTurn(){
	Util.wait(1000);
	if (turn == users.size() - 1)
	    turn = 0;
	else
	    turn++;
	return turn;
    }
	
    public int setPhase(int newPhase){
	Util.wait(1000);
	phase = newPhase;
	return phase;
    }
	
    public int nextTurnState(){
	Util.wait(1000);
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
	String name = users.get(Integer.parseInt(stat.substring(0,1))).getName();
	for (int i = 0; i < (Math.min(3,name.length())); i++)
		map.set(x++, y, name.charAt(i));
	map.set(x++,y, '-'); 
	for (int i = 1; i < stat.length(); i++)
	    map.set(x++,y, stat.charAt(i));
	//gotta overwrite old info
	while (Util.userChars.indexOf(map.get(x,y)) != -1){
		map.set(x,y, ' ');
		x++;	//or the while loop is pointless...
	}
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
	str = str.toLowerCase().trim();
	for (Country c : countries) { //for every country in country array
	    if (c.getName().toLowerCase().equals(str)) { //linear search
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
	str = str.trim().toLowerCase();
	String ret = "";
	boolean success = false;
	switch(str){
	case "exit":
	    ret = "QUIT";
	    System.exit(0);
	    break;
	case "status":
		ret = "to be implemented";
		break;
	case "map":
		printMap();
		ret = "Map printed.";
		break;
	case "j":
	    if (map.pan(-1)) { //if the pan is valid
		System.out.println(map);
		ret = "Panned Left.\n";
	    } else {
		ret = "Could not pan any more in this direction.";
	    }
	    break;
	case "k":
	    if (map.pan(-2)) {
		System.out.println(map);
		ret = "Panned Down.\n";
	    } else {
		ret = "Could not pan any more in this direction.";
	    }
	    break;
	case "l":
	    if (map.pan(1)) {
		System.out.println(map);
		ret = "Panned Right.\n";
	    } else {
		ret = "Could not pan any more in this direction.";
	    }
	    break;
	case "i":
	    if (map.pan(2)) {
		System.out.println(map);
		ret = "Panned Up.\n";
	    } else {
		ret = "Could not pan any more in this direction.";
	    }
	    break;
	case "cards":
		System.out.println("");
		for (int i = 0; i < getCurrentUser().getCards().size(); i++)
			System.out.println(getCurrentUser().getCards().get(i));
		ret = "Printed cards. \n";
		break;
	case "trade": case "t":
		parse("cards");	//just to print them.
		System.out.println("Please enter the three cards (seperated by a comma ',') to trade in for " + Util.getCardReinforcePredict() + " troops.");
		ret = "Trading...";
		break;
	case "help":
	    ret = "\n=== GENERIC COMMANDS ===\n" +
		"'help' -- display generic commands and turn-based instructions\n" +
		"'exit' -- save and quit the game\n" +
		"'save' -- save and continue playing\n" +
		"'map' -- print current map\n" +
		"'i' -- pan map up\n" +
		"'j' -- pan map left\n" +
		"'k' -- pan map down\n" +
		"'l' -- pan map right\n" +
		"'zoom in' -- zoom in once\n" +
		"'zoom out' -- zoom out once\n" +
		"'zoom <country>' -- zoom into a country\n" + 
		"'zoom <continent>' -- zoom into a continent\n\n\n";
		//'trade' -- trade in cards\n" +
		//"'cards' -- display cards\n\n\n";

	    switch(phase) {
	    case 1:
		ret += "You are currently in INITIAL REINFORCEMENT PHASE:\n" +
		    "In Intial Reinforcement, all players are given an initial number of troops to reinforce their countries.\n\n" +
		    "To select a country, first make sure you own the country by seeing if the first label under the country name is your name. If it is, you can type the country name, hit Return, and enter how many troops you want to reinforce the country with.\n\n" +
		    "Player " + users.get(turn) + ", please select a country to reinforce.";
		break;
	    case 2:
		switch(turnState) {
		case 0:
		    ret += "You are currently in Player " + users.get(turn) + "'s REINFORCEMENT PHASE:\n" +
			"in Reinforcement Phase, you are given a certain amount of reinforcements, determined by many factors (listed in Rules.txt).\n\n " + 
			"To select a country, first make sure the country is yours by seeing if the first label under the country name is your name. If it is, you can type the country name, hit Return, and enter how many troops you want to reinforce the country with.\n\n" +
		    "Player " + users.get(turn) + ", please select a country to reinforce.";
		    break;
		    
		case 1:
		    ret += "You are currently in Player " + users.get(turn) + "'s BATTLE PHASE:\n" +
			"in Battle Phase, you must first choose a country you own to attack from, hit Return, and then enter an opposing player's country to attack. You can attack as many times as you want in Battle Phase, aslong as you have a valid country to attack from.\n\n" + 
			"Make sure the you own the attacking country, and it has more than one troop on it.\n Also make sure you DON'T own the country you are attacking and it's adjacent to your attacking country (aerial assault is cheating :c).\n\n" +
			"You will then enter battle. First, you choose a number of dice to roll, with a min of 1 and max of 3. However, you can't rolle more dice than the number of troops in your attacking country - 1.\n Then ask the owner of the defending country (the country being attacked) to choose a number of die to roll, with a min of 1 and max of 2. Similarly, they can't roll more than the number of troops in the defending country - 1.\n\n" +
			"This will keep going until either:\n\t> The defending country runs out of troops, in which case that country is conquered by the attacking player.\n\t> Your attacking country is down to 1 troop, in which case you must retreat and the attack is unsuccessful.\n\t> You (the attacker) choose to retreat, in which case the battle stops.\n\n" +
			"As a general rule for both sides, the more dice one rolls, the higher chance of winning the battle, but the more troops you may lose. \n\n" +
			"Player " + users.get(turn) + ", please select one of your countries to attack from, or if you've done that already, select a country to attack, and if you've done that already, select a number of dice to roll, and if you've done that already, ask the owner of the country being attacked to select a number of dice to roll, and idk I'm probably making this more confusing than it already was :P";
		    break;
		    
		case 2:
		    ret += "You are currently in Player " + users.get(turn) + "'s FORTIFY PHASE:\n" +
			"in Fortify Phase, you're given the opportunity to transfer troops from one of your countries to an adjacent country that you also own. You can only use this once per turn, so use it wisely!\n\n" + 
			"Check that you own both countries and they're adjacent to one another. Also make sure you have at least one troop left in the country you send troops from so you still occupy it.\n\n" +
		    "Player " + users.get(turn) + ", please select a country to supply troops and then select a country to fortify.";
		    break;
		}
		break;
	    }
	    
	    break;
	default:
	    if ((str.length() >= 5) && (str.substring(0,5).equals("zoom "))) {
		String zoomArg = str.substring(5,str.length());
		if (zoomArg.equals("in")) {
		    map.zoom(1);
		    System.out.println(map);
		    ret = "Zoomed In.\n";
		    success = true;
		} else if (zoomArg.equals("out")) {
		    map.zoom(-1);
		    System.out.println(map);
		    ret = "Zoomed Out.\n";
		    success = true;
		}

		if (!success) {
		    for (Country c : countries) {
			if (c.getName().toLowerCase().equals(zoomArg.trim())) {
			    map.zoom(c,1);
			    System.out.println(map);
			    ret = "Zoomed into " + c.getName() + ".\n";
			    success = true;
			    break;
			}
		    }
		}

		if (!success) {
		    System.out.println("'" + zoomArg + "' is not a valid zoom argument\n");
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

	//seems to always get alberta and alaska... idk
    //ADDS RANDOM CARD OF DECK TO CURRENT USER. IMPLEMENT.
    //cards stores bank cards. Assume only need to add card to given user.
    public void addCard(){
	Card c = cards.get((int) (Math.random() * cards.size()));
	//System.out.println("index is " + index);
	getCurrentUser().add(c);
	c.setOwnerId(getCurrentUser().getId());
	cards.remove(c);
    }

	public ArrayList<Card> getCards(){
		return cards;
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
