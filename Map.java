import java.io.*;
import java.util.Scanner;


public class Map{
    
    public static char[][] readMap(String fileName) {
	map = new char[102][302];

	try {
	    FileReader fileReader = new FileReader(fileName);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);

	    int linectr = 0; //couldn't use for loop with readLine()
	    
	    while((line = bufferedReader.readLine()) != null) {
		char[] charArr = line.toCharArray(); //convert to array of chars
		for (int i = 0; i < charArr.length; i++) 
		    map[linectr][i] = charArr[i]; //copy over to map		
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

    public static String printMap(){
	String retStr = "";
	int max1 = Math.max(boundaries[3], map.length);
	int max2 = Math.max(boundaries[2], map.length);
	for (int lineNum = Math.min(boundaries[1], 0); lineNum < max1; lineNum++) {
	    for (int chNum = Math.min(boundaries[0], 0); chNum < max2; chNum++) 
		retStr += map[lineNum][chNum];
	    retStr += "\n";
	}
	System.out.println(retStr);
	return retStr;
    }
 
}
