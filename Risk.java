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

	public static void wait(int ms){try {Thread.sleep(ms);} catch (InterruptedException ie) {}}
	
	public static void main(String[] args){
		//{
		System.out.println("Now loading..." );
		System.out.println("\n    _______      _          __\n   |_   __ \\    (_)        [  |  _\n    | |__) |   __   .--.   | | / ]\n    |  __ /   [  | ( (`\\]  | '' < \n   _| |  \\ \\_  | |  `'.'.  | |`\\ \\\n  |____| |___|[___][\\__) )[__|  \\_]\n ");
		wait(3000);
		System.out.println("\n\n");
		System.out.println("Welcome to Risk");
		//}
		
		//Loading of Continents and Countries show 2 different methods of mass-object creation, I guess...
		
		Continent nAm = new Continent("North America", 5, 0, 9);
		Continent sAm = new Continent ("South America", 2, 9, 13);
		Continent africa...
		Continent europe...
		Continent australia...
		Continent asia...

		public Continent[] continents = {nAm, sAm, africa, europe, australia, asia};
		
		//Example of file parsing
		int numCountries = 42;
		String fileName = "countries.txt";
        String[] countries = new String[numCountries];
		
		//I don't think any country has more than 6 neighbors
		int[][] allBorders = new int[numCountries][6];
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
				input = line.split();
				countries[ctr] = input[0];
				for (int i = 1; i < input.length; i++)
					allBorders[ctr][i-1] = Integer.parseInt(input[i]); 
				ctr ++;
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
		
		public Country[] countries = new Country[numCountries];
		
		for (int i = 0; i < numCountries; i++)
			Country[i] = new Country(idCount, countries[i], allBorders[i]);	

		
	}
}