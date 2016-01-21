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
	//more control flow variables
	Country cFrom = null;
	Country cTo = null;
	boolean from = false;
	boolean to = false;
	boolean end = false;

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
	    case "e": System.out.println("Now exiting Risk.\n\n");
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
			//====================================================================================				
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


			//====================================================================================
		    case 1: //ATTACK
			end = false;
			from = false;
			to = false;
			boolean attack = false;
		    attack:
			while(game.getCurrentUser().numTroops() != game.getCurrentUser().getCountries().size()	//implying must be out of attacking troops
			      || !end){
					
			    System.out.println("Select a country to attack from. 'end' to end phase.");
			    validInput = false;
			    if (!from)
				cFrom = game.countries[0];
			    while (!from || !validInput){	//validInput condition: country owned. While I can't understand where to go from
				String input = in.nextLine();
				if (game.parse(input).equals(input)){ //this if checks if command is nongeneric.
				    try{
					if (input.equals("end") || input.equals("e"))
					    break attack;
						
					cFrom = game.countryIdentify(input);
					if (!game.getCurrentUser().owns(cFrom.getId()))
					    System.out.println("You do not own " + cFrom + ".");
					else if(cFrom.getTroops() == 1)
					    System.out.println("Not enough troops to attack from " + cFrom + ".");
					else{
					    validInput = true;
					    from = true;
					}	
				    }
				    catch(Exception e){
					System.out.println("Error: Invalid country '" + input + "'.");
				    }
				}
			    }
				
			    game.getMap().logBoundaries();
			    game.getMap().zoom(cFrom, 2);	//zoom to allow adequate viewing of neighboring countries
			    game.printMap();
				
			    if (!to)
				cTo = game.countries[0];
				
			    System.out.println("Country selected. 'cancel' to cancel. Select neighboring country to attack.");
			    validInput = false;
			    while (!to || !validInput){	//validInput condition: input country is neighbor to og, is enemy's
				String input = in.nextLine();
				if (game.parse(input).equals(input)){ //this if checks if command is nongeneric.
				    try{
					if (input.equals("c") || input.equals("cancel")){
					    System.out.println("Selection canceled.");
					    game.getMap().resetZoom();
					    from = false;
					    break;
					}
							
					cTo = game.countryIdentify(input);
								
					if (game.getCurrentUser().owns(cTo.getId()))
					    System.out.println("You cannot attack " + cTo + ". (You own it)");
					else if(!Util.contains(cFrom.getBorders(),cTo.getId()))
					    System.out.println(cTo + " is not connected to " + cFrom + ".");
					else{
					    System.out.println("Target country selected.");
					    validInput = true;
					    to = true;
					    game.getMap().zoom(cFrom, cTo);
					    game.printMap();
					}
							
				    }
				    catch(Exception e){
					System.out.println("Error: Invalid country '" + input + "'.");
				    }
				}
			    }


			    //BATTLE!!!
			    if (from && to){
				validInput = false;
				System.out.println("Roll? (Y/N)");
				while (!validInput){
				    String input = in.nextLine();
				    try{
					if (input.equals("Y") || input.equals("yes")){
					    int attackDiceNum = 0; int defendDiceNum = 0;
					    System.out.println("Battle beginning between countries " + cTo + " and " + cFrom + ".");
					    //there's no breaking now!!!
					    while(!attack){//a mini validInput for this stuff specifically
						System.out.println(game.getCurrentUser() + ", please specify number of dice to roll." );
						try{
						    attackDiceNum = Integer.parseInt(in.nextLine());
						    if (attackDiceNum > cFrom.getTroops() - 1)
							System.out.println(cFrom + " does not have sufficient troops (must be number of troops - 1 or less).");
						    else if (attackDiceNum > 3 || attackDiceNum <= 0)
							System.out.println("You can only roll 1, 2, or 3 dice.");
						    else
							attack = true;
						}
						catch(Exception e){
						    System.out.println("Not a valid number.");
						}
					    }
					    attack = false;
					    while (!attack){
						System.out.println(game.getUsers()[cTo.getOwnerId()] + ", please specify number of dice to roll." );
						try{
						    defendDiceNum = Integer.parseInt(in.nextLine());
						    if (defendDiceNum > cTo.getTroops())
							System.out.println(cTo + " does not have sufficiently many troops (must be number of troops or less.");
						    if (defendDiceNum > 2 || defendDiceNum <= 0)
							System.out.println("You can only roll 1 or 2 dice.");
						    else
							attack = true;
						}
						catch(Exception e){
						    System.out.println("Not a valid number.");
						}
					    }
						int roll;
					    //Roll dice (INSERT INTO ARRAY BY HIGHEST FIRST)
					    ArrayList<Integer> attackDice = new ArrayList<>();
					    ArrayList<Integer> defendDice = new ArrayList<>();
					    System.out.println("Attacker roll(s): \t");
					    for (int i = 0; i < attackDiceNum; i++){
						roll = Util.rollDie();
						System.out.println(roll + " \t");
						for (int j = 0; j < attackDiceNum; j++)
						    if (roll > attackDice.get(j)){
							attackDice.add(j, roll);
							break;
						    }
					    }
					    System.out.println("Defender roll(s) : \t");
					    for (int i = 0; i < defendDiceNum; i++){
						roll = Util.rollDie();
						System.out.println(roll + " \t");
						for (int j = 0; j < defendDiceNum; j++)
						    if (roll > defendDice.get(j)){
							defendDice.add(j, roll);
						    }
					    }
					    
					    //Compare
					    int attCas = 0;
					    int defCas = 0;
					    for (int i = 0; i < Math.min(attackDiceNum, defendDiceNum); i++)
						if (attackDice.get(i) > defendDice.get(i))
						    defCas++;
						else
						    attCas++;
					    System.out.println("Attacker loses " + attCas + " troop(s).");
					    cFrom.addTroops(-1 * attCas);
					    System.out.println(cFrom + " has " + cFrom.getTroops() + " troops left.");
					    System.out.println("Defender loses " + defCas + " troop(s).");
					    cTo.addTroops(-1 * defCas);
					    System.out.println(cTo + " has " + cTo.getTroops() + " troops left.");
					    game.update(cFrom); game.update(cTo);

					    //Check for battle report.
					    if (cFrom.getTroops() == 1 || cTo.getTroops() == 0){
						if (cFrom.getTroops() == 1)
						    System.out.println(cFrom + " no longer able to attack. Retreating...");
						else{
						    game.setConquered(true);
						    System.out.println(cTo + " has been conquered.");
						    cTo.setOwnerId(game.getCurrentUser().getId());
						    System.out.println("How many troops do you want to transfer to " + cTo + "?");
						    validInput = false;
						    while (!validInput){
							try{//consider breaking in a do/while loop??? I unno how to use that.
							    int num = Integer.parseInt(in.nextLine());
							    if (num > cFrom.getTroops() - 1)
								System.out.println(cFrom + " does not have enough troops.");
							    else if (num < attackDiceNum)
								System.out.println("You must move at least as many troops as dice you have rolled (" + attackDiceNum + ")."); //always possible
							    else if (num <= 0)
								System.out.println("Please specify a positive amount.");
							    else{
								cTo.addTroops(num);
								cFrom.addTroops(-num);
								game.update(cTo);
								game.update(cFrom);
								System.out.println("Troops transfered.");
							    }
							}
							catch(Exception e){
							    System.out.println("Invalid number.");
							}    
						    }
						}
						    
						from = false;
						to = false;
						game.getMap().resetZoom();
						game.printMap();
						break;
					    }
					    //note, game rules don't allow for some sort of troop count paradox, so don't worry about that (if's are mutually exclusive)
					}

					else if (input.equals("N") || input.equals("no")){
					    System.out.println("Retreating.");
					    from = false;
					    to = false;
					    game.getMap().resetZoom();
					    break; //should break up to y/n while loop (asking for new countries)
					}
					
					else
					    System.out.println("I don't understand...");
				    }
				    catch(Exception e){
					System.out.println("Yes or no? Not hard...");
				    }
				}
				
			    }

				
			}
			break;

			//====================================================================================
			//Country selection mechanism of fortify is essentially identical to the one in attack. No new code here, really.
		    case 2: //FORTIFY
			end = false;
			from = false;
			to = false;

		    fortify:
			while(!end){
					
			    System.out.println("Select a country to fortify from. 'end' to skip phase.");
			    validInput = false;
			    if (!from)
				cFrom = game.countries[0];
				
			    while (!from || !validInput){	//validInput condition: country owned. While I can't understand where to go from
				String input = in.nextLine();
				if (game.parse(input).equals(input)){ //this if checks if command is nongeneric.
				    try{
					if (input.equals("end") || input.equals("e"))
					    break fortify;
						
					cFrom = game.countryIdentify(input);
					if (!game.getCurrentUser().owns(cFrom.getId()))
					    System.out.println("You do not own " + cFrom + ".");
					else if(cFrom.getTroops() == 1)
					    System.out.println("Not enough troops to attack from " + cFrom + ".");
					else{
					    validInput = true;
					    from = true;
					}
				    }
				    catch(Exception e){
					System.out.println("Error: Invalid country '" + input + "'.");
				    }
				}
			    }
				
			    game.getMap().logBoundaries();
			    game.getMap().zoom(cFrom, 2);	//zoom to allow adequate viewing of neighboring countries
			    game.printMap();
				
			    if (!to)
				cTo = game.countries[0];
				
			    System.out.println("Country selected. 'cancel' to cancel. Select neighboring country to fortify.");
			    validInput = false;
			    while (!to || !validInput){	//validInput condition: input country is neighbor to og, is also yours
				String input = in.nextLine();
				if (game.parse(input).equals(input)){ //this if checks if command is nongeneric.
				    try{
					if (input.equals("c") || input.equals("cancel")){
					    System.out.println("Selection canceled.");
					    game.getMap().resetZoom();
					    from = false;
					    break;
					}
							
					cTo = game.countryIdentify(input);
								
					if (!game.getCurrentUser().owns(cTo.getId()))
					    System.out.println("You do not own " + cTo + ".");
					else if(!Util.contains(cFrom.getBorders(),cTo.getId()))
					    System.out.println(cTo + " is not connected to " + cFrom + ".");
					else{
					    System.out.println("Target country selected.");
					    validInput = true;
					    to = true;
					    game.getMap().zoom(cFrom, cTo);
					    game.printMap();
					}
							
				    }
				    catch(Exception e){
					System.out.println("Error: Invalid country '" + input + "'.");
				    }
				}
			    }
				
			    //if this part of the code wasn't reached by a break line
			    if (validInput){	//therefore, if cTo and cFrom are valid.
				System.out.println("Enter number of troops to transfer.");
				validInput = false;
				while (!validInput){
				    String input = in.nextLine();
						
				    if (input.equals("c") || input.equals("cancel")){
					System.out.println("Selection canceled.");
					to = false;
					break;
				    }	
							
				    try{//consider breaking in a do/while loop??? I unno how to use that.
					int num = Integer.parseInt(input);
					if (num > cFrom.getTroops() - 1)
					    System.out.println(cFrom + " does not have enough troops.");
					else if (num <= 0)
					    System.out.println("Please specify a positive amount.");
					else{
					    cTo.addTroops(num);
					    cFrom.addTroops(-num);
					    game.update(cTo);
					    game.update(cFrom);
					    game.getMap().resetZoom();
					    System.out.println("Troops transfered.");				
					    validInput = true;
					    end = true;
					}
				    }
				    catch(Exception e){
					System.out.println("Invalid number.");
				    }    
				}
			    }
				
				
			}
			
			if (game.conqueredAny())
			    game.addCard();
			game.nextTurnState();
			game.nextTurn();
			break;
		    }
		    //switch(in.nextLine().substring(0,1)){
		    //case "e": unreachable???
		    //game.setTurn(-1);
		    //break;	
		    //}
		}
		break;
						
	    }
	}
		
    }	
		
}
