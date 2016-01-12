/**********************
 Class Risk - Driver file for Risk project
**********************/

//Heads up for readers, the //{ and //} sprinkled throughout are for Notepad++, which I (Joel) am using.
//They allow the enclosed content to be collapsed for my own readability.

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
 
public class Risk{
	
    //Player IDs will go from 0 to 5
    
    public int numPlayers = 6;

    private static User p1 = new Player(0);
    private static User p2 = new Player(1);
    private static User p3 = new Player(2);
    private static User p4 = new Player(3);
    private static User p5 = new Player(4);
    private static User p6 = new Player(5);
		
    private static User[] users = {p1,p2,p3,p4,p5,p6};


    /****************
     phase - int describing game state.
        -1 - exit (to break out of while loop).
         0 - menu (loaded on startup).
         menu desc:
             Start New Game (know your instructions!)
             Load Game
             Settings
             Exit
         1 - playing game
         2 - in-game paused (settings)
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

    /****************
cardBonus - this is incremented. we'll check that later
    *****************/
    private static int cardBonus = 0;

    //Wait function, for that authentic feel.
    public static void wait(int ms){try {Thread.sleep(ms);} catch (InterruptedException ie) {}}
	
    //@William - it'd be nice to make this generic, but I'm not sure how
    private static void swap( int i, int j , Country[] arr) {
	Country temp = arr[i];
	arr[i] = arr[j];
	arr[j] = temp;
    }
	
    public static void shuffle(Country[] arr) {
	int len = arr.length;
	//len - i. Elements to choose from.
	for (int i = 0; i < len; i++){
	    swap(i, i + (int)(Math.random()*(len-i)), arr);
	}
    }
	

    public static char[][] readMap(String fileName){
	char[][] map = new char[125][302];
	
	String line = null;

	try {
	    FileReader fileReader = new FileReader(fileName);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);

	    int linectr = 0; //couldn't use for loop with readLine()
	    
	    while((line = bufferedReader.readLine()) != null) {
                char[] charArr = line.toCharArray(); //convert to array of chars
		for (int i = 0; i < charArr.length; i++) {
		    map[linectr][i] = charArr[i]; //copy over to map
		}		
		linectr++; //increment line counter
	    }
	    bufferedReader.close();
	}

	catch(FileNotFoundException ex) {
	    System.out.println("Unable to open file '" + fileName + "'");
	}
	catch(IOException ex) {
	    System.out.println("Error reading file '" + fileName + "'");
	}

	return map;
    }

    //printMap - presume map is loaded, print top left (loX,loY) to bottom right (hiX,hiY). 
    public static String printMap(int loX, int loY, int hiX, int hiY){
	String retStr = "";
	
	for (int lineNum = 0; lineNum < map.length; lineNum++) {
	    for (int chNum = 0; chNum < map[lineNum].length; chNum++) {
		if ((lineNum < 30) && (chNum < 80)) {
		    retStr += map[lineNum][chNum];
		}
	    }
	    retStr += "\n";
	}
	
	return retStr;
    } 

	//assume no wrapping problems (b/c we're good with this stuff right :))
    public static void update(char[][] world, Country c){
		int[] coords = c.getMapLoc();
		int y = coords[0]; //x,y are inverted, but let's not get bogged down...;
		int x = coords[1];
		String stat = c.status();
		//REMEMBER TO UPDATE w/ NICKNAME!!!
		//User id here:
		world[y][x++] = users[Integer.parseInt(stat.substring(0,1))].getNick();
		x++;
		for (int i = 1; i < stat.length(); i++)
		    world[y][x++] = stat.charAt(i);
		//first char is userId, convert to user info. Track owner or ownerId????
	}
	
	
    public static void main(String[] args){	
	
	//LOADING COUNTRIES
	//Loading of Continents and Countries show 2 different methods of mass-object creation, I guess...
	//{
	Continent nAm = new Continent("North America", 5, 1, 10);
	Continent sAm = new Continent ("South America", 2, 10, 14);
	Continent africa = new Continent("Africa", 3, 14, 20);
	Continent europe = new Continent("Europe", 5, 20, 27);
	Continent australia = new Continent("Australia", 2, 27, 31);
	Continent asia = new Continent("Asia", 7, 31, 43);

	Continent[] continents = {nAm, sAm, africa, europe, australia, asia};
		
	//Example of file parsing
	int numCountries = 42;
	String fileName = "countries.txt";
        String[] countriesIn = new String[numCountries];
	
	int[][] allMapLoc = new int[numCountries][2];
	int[][] allBorders = new int[numCountries][6];//max border count is 6
	/*
	  It's also valid to just use allBorders and the indices to
	  indicate which country's borders we're referring to, but here's
	  an excuse to use a 2d array.
	*/
		
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
		for (int i = 3; i < input.length; i++)    
			allBorders[ctr][i-1] = Integer.parseInt(input[i]) - 1; //-1 because file uses line numbers, not indices... 
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
		
	Country[] countries = new Country[numCountries];
		
	for (int i = 0; i < numCountries; i++)
	    countries[i] = new Country(i, countriesIn[i], allBorders[i], allMapLoc[i]);	
	//}
	
	//{
	System.out.println("Now loading..." );
	
	//System.out.println("Do you use Windows?");




	System.out.println("\n    _______      _          __\n   |_   __ \\    (_)        [  |  _\n    | |__) |   __   .--.   | | / ]\n    |  __ /   [  | ( (`\\]  | '' < \n   _| |  \\ \\_  | |  `'.'.  | |`\\ \\\n  |____| |___|[___][\\__) )[__|  \\_]\n ");
	for (int i = 0; i < 3; i++) {wait(1000); System.out.print(".");}
	System.out.println("\n\n");
	System.out.println("Welcome to Risk");
	//}
	
	//At this point, presume the map is loaded.
	System.out.println("Rules documentation is located in 'Rules.txt'.");
	System.out.println("Detailed controls are in 'Controls.txt'.");
	System.out.println("Type 'quit' at any time to exit the game.");
		
	//Random distribution of countries. Assume 6 human players. Shuffle, and divide into groups of 7.
		
	//Load map
	char[][] map = readMap("map.txt");

	//assign countries
	shuffle(countries);

	for (int i = 0; i < 6; i++)
	    for (int j = 0; j < 7; j++){
		countries[i * 7 + j].setOwnerId(i);
		countries[i * 7 + j].addTroops(1);
		update(map, countries[i*7+j]);
		users[i].add(countries[i*7+j]);
	    }	
			
	//Check for anyone owns continent.
	//for (int i = 0; i < 42; i++){
	//    System.out.println(countries[i].getName() + " owned by player " + users[countries[i].getOwnerId()].getName()); //offset by 1 b/c of array.				
	//}

	
	
	turn = (int)(Math.random()*6)+1;
		
	while (turn != 0){


	}
		
		
		
    }
}
