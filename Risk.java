/**********************
 Class Risk - Driver file for Risk project
 **********************/

//Heads up for readers, the //{ and //} sprinkled throughout are for Notepad++, which I (Joel) am using.
//They allow the enclosed content to be collapsed for my own readability.

import java.io.*;
 
public class Risk{
	
	//Player IDs will go from 1 to 6

	private int phase = 0;
	private int turn = 0;
	private int cardBonus = 0;

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
				for (int i = 1; i < input.length; i++)
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
			countries[i] = new Country(i, countriesIn[i], allBorders[i]);	
		//}
	
		//{
		System.out.println("Now loading..." );
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
		User p1 = new Player(0);
		User p2 = new Player(1);
		User p3 = new Player(2);
		User p4 = new Player(3);
		User p5 = new Player(4);
		User p6 = new Player(5);
		
		User[] users = {p1,p2,p3,p4,p5,p6};
		
		shuffle(countries);

		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++){
				countries[i * 7 + j].setOwnerId(i);
				users[i].add(countries[i*7+j]);
			}	
			
		//Check for anyone owns continent.
		for (int i = 0; i < 42; i++){
			System.out.println(countries[i].getName() + " owned by player " + users[countries[i].getOwnerId()].getName()); //offset by 1 b/c of array.				
		}
		
		
		
		
	}
}