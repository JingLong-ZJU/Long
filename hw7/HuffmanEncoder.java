import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {

	public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols){
		Map<Character, Integer> buildMap = new HashMap<Character, Integer>();
		for(char i : inputSymbols) {
			if(buildMap.containsKey(i)) {
				buildMap.replace(i, buildMap.get(i) + 1);
			}
			else {
				buildMap.put(i, 1);
			}
		}
		return buildMap;
	}
	public static void main(String[] args) {
		
		char[] inputChar = FileUtils.readFile(args[0]);
		Map<Character, Integer> buildFrequencyMap = buildFrequencyTable(inputChar);
		BinaryTrie binaryTrie = new BinaryTrie(buildFrequencyMap);
		
		String newFileName = args[0] + ".huf";
		ObjectWriter ow = new ObjectWriter(newFileName);
		ow.writeObject(binaryTrie);
		ow.writeObject(inputChar.length);
		
		Map<Character, BitSequence> lookupTable = binaryTrie.buildLookupTable();
		List<BitSequence> bsList = new ArrayList<BitSequence>();
		for(char i : inputChar) {
			bsList.add(lookupTable.get(i));
		}
		BitSequence bs = BitSequence.assemble(bsList);
	    ow.writeObject(bs);
	}

}
