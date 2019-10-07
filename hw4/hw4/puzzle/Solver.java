package hw4.puzzle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import edu.princeton.cs.algs4.MinPQ;
public class Solver {
	private WorldState initial;
	private String goal;
	private SearchNode result;
	private ArrayList<WorldState> solution = new ArrayList<WorldState>(100);

	private MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new Comparator<SearchNode>() {
		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			if(o1.judge > o2.judge)return 1;
			else if(o1.judge == o2.judge)return 0;
			else return -1;
		}
	});
	
	private class SearchNode{
		private WorldState word;
		private int moves;
		private SearchNode previous;
		private int judge;
		public SearchNode(WorldState initial, int move, SearchNode node) {
			word = initial;
			moves = move;
			previous = node;
			judge = initial.estimatedDistanceToGoal() + move;
		}
	}
	
    public Solver(WorldState initial) {
    	SearchNode initialNode = new SearchNode(initial,0,null);
    	pq.insert(initialNode);
    	SearchNode getNode = pq.delMin();
    	while(!getNode.word.isGoal()) {
    		Iterable<WorldState> i = getNode.word.neighbors();
    		
    		for(WorldState ws : i) {
    			if(getNode.previous != null) {
    				if(getNode.previous.word.equals(ws)) {
    					continue;
    				}
    			}
    			pq.insert(new SearchNode(ws, getNode.moves+1, getNode));
    		}
    		getNode = pq.delMin();
    	}
    	result = getResult(getNode);
    	ArrayList<WorldState> reverseSolution = new ArrayList<WorldState>();
    	for(int i = solution.size()-1; i >= 0; i--) {
    		reverseSolution.add(solution.get(i));
    	}
    	solution = reverseSolution;
    }
    
    private SearchNode getResult(SearchNode result) {
    	if(result.previous == null) {
    		solution.add(result.word);
    		return result;
    	}
    	while(result.previous != null) {
    		solution.add(result.word);
    		result = result.previous;
    	}
		solution.add(result.word);
    	return result;
    }
    
    public int moves() {
    	return solution.size()-1;
    }
    public Iterable<WorldState> solution(){
        return solution;
    }
}
