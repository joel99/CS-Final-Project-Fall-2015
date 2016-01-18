/**********************
 Class Risk - Driver file for Risk project
**********************/

//Heads up for readers, the //{ and //} sprinkled throughout are for Notepad++, which I (Joel) am using.
//They allow the enclosed content to be collapsed for my own readability.

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
 
public class Risk{
	
    public static void main(String[] args){	
	
	//LOADING COUNTRIES
	//Loading of Continents and Countries show 2 different methods of mass-object creation, I guess...
	//{
		
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
		for (int i = 3; i < input.length; i++){    
		    System.out.println(input[i]);
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
		
	Country[] countries = new Country[numCountries];
		
	for (int i = 0; i < numCountries; i++)
	    countries[i] = new Country(i, countriesIn[i], allBorders[i], allMapLoc[i]);	
	//}
	
	//{
	System.out.println("Now loading..." );
	
	//System.out.println("Do you use Windows?");

	Game game = new Game(6, "map.txt");


	System.out.println("\n    _______      _          __\n   |_   __ \\    (_)        [  |  _\n    | |__) |   __   .--.   | | / ]\n    |  __ /   [  | ( (`\\]  | '' < \n   _| |  \\ \\_  | |  `'.'.  | |`\\ \\\n  |____| |___|[___][\\__) )[__|  \\_]\n ");
	for (int i = 0; i < 3; i++) {Util.wait(1000); System.out.print(".");}
	System.out.println("\n\n");
	System.out.println("Welcome to Risk");
	//}
	
	//At this point, presume the map is loaded.
	System.out.println("Rules documentation is located in 'Rules.txt'.");
	System.out.println("Detailed controls are in 'Controls.txt'.");
	System.out.println("Type 'quit' at any time to exit the game.");
		
	//Random distribution of countries. Assume 6 human players. Shuffle, and divide into groups of 7.

	//assign countries
	Util.shuffle(countries);
	
	for (int i = 0; i < 6; i++)
	    for (int j = 0; j < 7; j++){
		countries[i * 7 + j].setOwnerId(i);
		countries[i * 7 + j].addTroops(1);
		//update(countries[i*7+j]);
		game.getUsers()[i].add(countries[i*7+j]);
	    }	
			
	game.printMap();

	//TEST
	game.writeSave();

	
	
	//Check for anyone owns continent.
	//for (int i = 0; i < 42; i++){
	//    System.out.println(countries[i].getName() + " owned by player " + users[countries[i].getOwnerId()].getName()); //offset by 1 b/c of array.				
	//}

	Scanner in = new Scanner(System.in);
	while(game.getPhase() != -1){
	    switch(in.nextLine().substring(0,1)){
			
		//"e": exit
	    case "e": System.out.println("Now exiting Risk.");
		break;
			
		//"o": options
	    case "o":
		break;
			
		//"s": start game
	    case "s":
		game.setTurn((int)(Math.random()*6)+1);
		
		//Initial reinforcement phase
		//blahblah something similar to case 0, skipping others.
		
		game.setPhase(1);
		
		while(game.getReinforcements() > 0) {
			
				//snake pick...
					
					System.out.println("Please select a country.");
					
				    game.parse(in.nextLine()); //user-entered country
				    
					boolean countryAdded = false; //used to tell user country is invalid

				    for (Country c : countries) { //for every country in country array
					if (c.getName().equals(countryStr)) { //linear search

					    game.getCurrentUser().add(c);
						
					    countryAdded = true;
						System.out.println(");
						
					    game.useReinforcement();
					    break;
					}
			}
		}
		
		
		//Game looping
		
		game.setPhase(2);
		
		while (game.getTurn() != -1){
		    switch(game.getTurnState()){
		    case 0: //REINFORCE
			while(game.getTurn() <= game.getUsers().length){ //do for every player
			    //don't want to refill troops if process is cut off
			    if (game.getReinforcements() == 0)
					game.calcReinforce();
			    
			    while(game.getReinforcements() > 0) {
				//distribute given reinforcements among user owned countries.
					
					System.out.println("Please select a country to reinforce.");
					
				    game.parse(in.nextLine()); //user-entered country
				    
					boolean countrySelected = false; //used to tell user country is invalid

				    for (Country c : countries) { //for every country in country array
					if (c.equals(countryStr)) { //linear search
						game.logBoundaries();
						game.zoom(c);
					    countrySelected = true;
						System.out.println("How many troops do you want to add?");
						if ()
						
					    game.useReinforcement();
					    break;
					}
				    }
				    
				    if (!countryAdded) { //if the linear search failed
					System.out.println("Error: Invalid country '" + countryStr + "'");
				    }
			    }
			    game.nextTurn();
			}
			      
			break;
		    case 1: //ATTACK
			while(in.nextLine().substring(0,1) != "e"){
						
			}
			break;
		    case 2: //FORTIFY
			while(in.nextLine().substring(0,1) != "e"){
						
			}
			break;
		    }
		    //game.nextTurnState();
		    switch(in.nextLine().substring(0,1)){
		    case "e":
			game.setTurn(-1);
			break;
				
				
				
				
				
		    }
		    game.nextTurn();
		}
		break;
			
			
	    }
	}
		
    }	
		
}
