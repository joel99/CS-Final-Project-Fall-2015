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
	
	boolean validInput; //Used for all sorts of control flow.

	System.out.println("Now loading..." );
	
	//System.out.println("Do you use Windows?");

	Game game = new Game(6, "map.txt");


	System.out.println("\n    _______      _          __\n   |_   __ \\    (_)        [  |  _\n    | |__) |   __   .--.   | | / ]\n    |  __ /   [  | ( (`\\]  | '' < \n   _| |  \\ \\_  | |  `'.'.  | |`\\ \\\n  |____| |___|[___][\\__) )[__|  \\_]\n ");
	for (int i = 0; i < 3; i++) {Util.wait(1000); System.out.print(".");}
	System.out.println("\n\n");
	System.out.println("Welcome to Risk");

	//Load map
	game.loadMap();
	
	//At this point, presume the map is loaded.
	System.out.println("Rules documentation is located in 'Rules.txt'.");
	System.out.println("Detailed controls are in 'Controls.txt'.");
	System.out.println("Type 'quit' at any time to exit the game.");


	
	//Random distribution of countries. Assume 6 human players. Shuffle, and divide into groups of 7.
	//Code coould be simpler if in Game.java, but we thought assigning countries to players made more sense in driver
	
	//assign countries
	Util.shuffle(game.countries);
	
	for (int i = 0; i < 6; i++)
	    for (int j = 0; j < 7; j++){
		game.countries[i * 7 + j].setOwnerId(i);
		game.countries[i * 7 + j].addTroops(1);
		//update(countries[i*7+j]);
		game.getUsers()[i].add(game.countries[i*7+j]);
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
		/*
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
		*/
		
		//Game looping
		
		game.setPhase(2);
		
		while (game.getTurn() != -1){
			
		    game.setConquered(false); //Don't set in reinforce in case of card bonus.
		    //CAREFUL TO OVERWRITE THIS IN CASE OF SAVING!
			
		    switch(game.getTurnState()){
				
		    case 0: //REINFORCE
			
			if (game.getReinforcements() == 0)
			    game.calcReinforce();
				
			while(game.getReinforcements() > 0) {
			    //distribute given reinforcements among user owned countries.
				
			    //lmao no panning while zoomed.
			    game.printMap();
			    System.out.println("Please select a country to reinforce.");
			    Country c = game.countries[0]; //for initialization fears...
			    validInput = false;
					
			    while (!validInput){	//validInput condition: input country is valid.
				String input = in.nextLine();
				if (game.parse(input).equals(input)){ //this if checks if command is nongeneric.
				    try{
					c = game.countryIdentify(input);
					if (game.getCurrentUser().owns(c.getId())){
					    validInput = true;
					}
					else
					    System.out.println("You do not own " + c + ".");
				    }
				    catch(Exception e){
					System.out.println("Error: Invalid country '" + input + "'.");
				    }
				}
			    }
						
			    game.getMap().logBoundaries();
			    game.getMap().zoom(c, 1);
			    game.printMap();
					
			    System.out.println("How many troops do you want to add?");
			    validInput = false;
					
			    while (!validInput){ //validInput condition: input is a number <= reinforcements.
				try{//consider breaking in a do/while loop??? I unno how to use that.
				    int num = Integer.parseInt(in.nextLine());
				    if (num > game.getReinforcements())
					System.out.println("The amount specified is more than you have.");
				    else if (num <= 0)
					System.out.println("Please specify a positive amount.");
				    else{
					c.addTroops(num);
					game.update(c);
					game.useReinforcements(num);
					game.getMap().resetZoom();
				    }
				}
				catch(Exception e){
				    System.out.println("Invalid number.");
				}
			    }
				
			} 
				
				
			game.nextTurnState();
			      
			break;
		    case 1: //ATTACK
			while(in.nextLine().substring(0,1) != "e"){
							
			}
			break;
		    case 2: //FORTIFY
			boolean end = false;
				
			while(!end){
					
			    System.out.println("Select a country to fortify from");
			    validInput = false;
				
				
			}
				
			game.nextTurnState();
			game.nextTurn();
			break;
		    }
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
