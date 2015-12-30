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
		wait(3000);
		System.out.println("\n\n");
		System.out.println("Welcome to Risk");
		//}
	
		//At this point, presume the map is loaded.
		System.out.println("Rules documentation is located in 'Rules.txt'.");
		System.out.println("Detailed controls are in 'Controls.txt'.");
		System.out.println("Type 'quit' at any time to exit the game.");
		
		
	}
}