/*******************
 class Util - holds constants and driver's static methods 
 ******************/


public class Util{
    
    public final int[] cardBonus = {4, 6, 8, 10, 12, 15};

    public int cardBonusCtr = 0; //num sets traded in so far

    public int getCardReinforce(){
	cardBonusCtr++;
	if (cardBonusCtr > cardBonus.length)
	    return (cardBonusCtr - cardBonus.length) * 5 + cardBonus[cardBonus.length - 1];
	return cardBonus[cardBonusCtr - 1];
    }

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

}
