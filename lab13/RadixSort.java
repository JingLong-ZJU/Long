import java.util.ArrayList;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
    	if(asciis == null) {
    		throw new IllegalArgumentException();
    	}
    	String[] sortedString = new String[asciis.length]; 
    	int maxLength = findMaxLength(asciis);
    	int[] asciisInt = new int[asciis.length];
    	for(int i = 0; i < asciis.length; i++) {
    		asciisInt[i] = Integer.parseInt(asciis[i]);
    	}
    	for(int i = 0; i < maxLength; i++) {
    		sortHelperLSD(asciisInt,i);
    	}
    	for(int i = 0; i < asciis.length; i++) {
    		sortedString[i] = String.valueOf(asciisInt[i]);
    	}
        return sortedString;
    }
    
    private static int findMaxLength(String[] asciis) {
    	int maxLength = -1;
    	for(String s : asciis) {
    		maxLength = maxLength > s.length()? maxLength : s.length();
    	}
    	return maxLength;
    }
    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
//    private static void sortHelperLSD(String[] asciis, int index) {
    private static void sortHelperLSD(int[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
    	ArrayList<ArrayList<Integer>> sorted = new ArrayList<ArrayList<Integer>>(10);
    	for(int i = 0; i < 10; i++) {
    		sorted.add(new ArrayList<Integer>());
    	}
    	int divisor = 10;
    	while(index > 0) {
    		index--;
    		divisor*=10;
    	}
    	int divisor2 = divisor/10;	
    	for(int i = 0; i < asciis.length; i++) {
    		sorted.get((asciis[i]%divisor)/divisor2).add(asciis[i]);
    	}
    	int count = 0;
    	for(int i = 0; i < sorted.size(); i++) {
    		for(int j = 0; j < sorted.get(i).size(); count++,j++) {
    			asciis[count] = sorted.get(i).get(j);
    		}
    	}
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
    	
        return;
    }
    
    public static void main(String[] args) {
    	String[] arr = {"10","255","0","100","111"};
    	String[] sorted;
    	sorted = sort(arr);
        for(String s : sorted){
            System.out.println(s);
        }
    }
    
}
