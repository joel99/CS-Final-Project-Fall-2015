import java.io.*;

public class Map{
    
    public static void main(String[] args){
	String fileName = "map.txt";
	//Note: each row is 302 char long.

	//this 2D array contains the entire map
	//125 lines, 302 chars per line
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

	String retStr = "";
	
	for (int lineNum = 0; lineNum < map.length; lineNum++) {
	    for (int chNum = 0; chNum < map[lineNum].length; chNum++) {
		if ((lineNum < 30) && (chNum < 80)) {
		    retStr += map[lineNum][chNum];
		}
	    }
	    retStr += "\n";
	}
	
	System.out.println(retStr);
	
    }
}
