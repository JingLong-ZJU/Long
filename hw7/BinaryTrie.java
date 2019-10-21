import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
	
	private StringBuilder bitSequeneceOfChar = new StringBuilder();
	private Map<Character, BitSequence> buildLookupMap;
	private Node root;
	
	private class Node implements Serializable {
        private char ch;
        private double freq;
        private Node left, right;
        public Node(char ch, double freq, Node left, Node right){
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
    }
	
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
    	PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				if(o1.freq > o2.freq) {
					return 1;
				}
				else if(o1.freq == o2.freq) {
					return 0;
				}
				else {
					return -1;
				}
			}
		});
    	
    	for(Character c : frequencyTable.keySet()) {
    		pq.add(new Node(c, frequencyTable.get(c), null, null));
    	}
    	while(pq.size() > 1) {
    		Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('#',left.freq + right.freq, left, right);
            pq.add(parent);
    	}
    	root = pq.poll();
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
    	 Node p = root;
    	 StringBuilder sb = new StringBuilder();
         for(int i = 0; i < querySequence.length(); i++){
             int num = querySequence.bitAt(i);
             if(num == 0){
                 p = p.left;
             }
             else if(num == 1){
                 p = p.right;
             }
             sb.append(num);
             if(isLeaf(p)){
                 break;
             }
         }
         BitSequence bs = new BitSequence(sb.toString());
         return new Match(bs, p.ch);
    }
    public Map<Character, BitSequence> buildLookupTable(){
    	buildLookupMap = new HashMap<Character, BitSequence>();
        bitSequeneceOfChar = new StringBuilder();
        traverse(root);
    	return buildLookupMap;
    }
    
    private boolean isLeaf(Node x){
        return (x.left == null && x.right == null);
    }
    
    private void traverse(Node x){
        if(isLeaf(x)){
            BitSequence bs = new BitSequence(bitSequeneceOfChar.toString());
            buildLookupMap.put(x.ch,bs);
            return;
        }
        bitSequeneceOfChar.append(0);
        traverse(x.left);
        bitSequeneceOfChar.deleteCharAt(bitSequeneceOfChar.length() - 1);
        bitSequeneceOfChar.append(1);
        traverse(x.right);
        bitSequeneceOfChar.deleteCharAt(bitSequeneceOfChar.length() - 1);
    }
    
}
