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
	
	//Options menu things...
	//0 - random
	int distributionMethod = 0;
	
	
	
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
	System.out.println("Type 'exit' at any time to exit the game.");	

	//TEST
	//game.writeSave();

	
	
	//Check for anyone owns continent.
	//for (int i = 0; i < 42; i++){
	//    System.out.println(countries[i].getName() + " owned by player " + users[countries[i].getOwnerId()].getName()); //offset by 1 b/c of array.				
	//}

	Scanner in = new Scanner(System.in);
	while(game.getPhase() != -1){
		System.out.println("Please make your selection:");
		System.out.println("e - exit");
		System.out.println("o - options (currently non-functional)");
		System.out.println("l - load game (currently non-functional)");
		System.out.println("s - start game");
		
	    switch(in.nextLine().substring(0,1)){
			
		//"e": exit
	    case "e": System.out.println("Now exiting Risk.");
		break;
			
		//"o": options
	    case "o":
		break;
			
		//"s": start game
	    case "s":
		
		game.setTurn((int)(Math.random()*6));
		
		//Initial reinforcement phase
		
		//Random distribution of countries. Assume 6 human players. Shuffle, and divide into groups of 7.
		//Code could be simpler if in Game.java, but we thought assigning countries to players made more sense in driver
		
		//assign countries
		switch(distributionMethod){
		case 0:
		default:
			Util.shuffle(game.countries);
			for (int i = 0; i < 6; i++)
				for (int j = 0; j < 7; j++){
				game.countries[i * 7 + j].setOwnerId(i);
				game.countries[i * 7 + j].addTroops(1);
				game.update(i*7+j);
				game.getUsers()[i].add(game.countries[i*7+j]);
				}
		break;
		}
		
		System.out.println("Countries have been distributed.");
		
		game.setPhase(1);
		int initCtr = game.getUsers().length; //ticks down to change phase.
		
		//GAME START!!!
		
		while (game.getTurn() != -1){
			
		    game.setConquered(false); //Don't set in reinforce in case of card bonus.
		    //CAREFUL TO OVERWRITE THIS IN CASE OF SAVING!
			
		    switch(game.getTurnState()){
				
		    case 0: //REINFORCE
			while (game.getTurn() <= game.getUsers().length) { 
			    if (game.getReinforcements() == 0){ //if saved in the middle of distr reinforcements
					if (game.getPhase() == 1)
						game.setReinforcements(20 - game.getCurrentUser().numTroops()); //20 may be swapped out
					else if (game.getPhase() == 2)
						game.calcReinforce();
				}
			    while(game.getReinforcements() > 0) {
				//distribute given reinforcements among user owned countries.
				
				//lmao no panning while zoomed.
				game.printMap();
				System.out.println("Player " + game.getCurrentUser() + "'s turn start:");
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

				System.out.println("You have " + game.getReinforcements() + " troop(s) remaining.");
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
							System.out.println("Troops added!");				
							validInput = true;
						}
						}
						catch(Exception e){
						System.out.println("Invalid number.");
						}
					}			
			    }
			    System.out.println("Player " + game.getCurrentUser() + "'s turn ended.");
				if (initCtr == 1){	//since this check is done at end, initCtr should stop at 1, not 0.
					game.setPhase(2);
					System.out.println("Initial reinforcement complete!");
				}
				else
					initCtr--;
			    game.nextTurn();
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
