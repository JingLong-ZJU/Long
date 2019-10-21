
public class HuffmanDecoder {

	public static void main(String[] args) {
		
		ObjectReader or = new ObjectReader(args[0]);
		String newFileName = args[1];
		
		BinaryTrie binaryTrie = (BinaryTrie) or.readObject();
		int num = (Integer) or.readObject();
		BitSequence bs = (BitSequence) or.readObject();
		
		char[] outputChar = new char[num];
		for(int i = 0; i < num; i++) {
			Match match = binaryTrie.longestPrefixMatch(bs);
			outputChar[i] = match.getSymbol();
			bs = bs.allButFirstNBits(match.getSequence().length());
		}
		
		FileUtils.writeCharArray(newFileName, outputChar);
	}

}
