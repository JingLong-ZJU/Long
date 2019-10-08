package lab11.graphs;

import java.util.LinkedList;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /*Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
    	marked[v] = true;
        announce();
        if (v == t) {
            targetFound = true;
        }
        if (targetFound) {
            return;
        }
        LinkedList<Integer> arr = new LinkedList<Integer>();
        arr.add(v);
        Integer output = v;
        while(!arr.isEmpty()) {
        	output = arr.pop();
        	if(output == t) {
        		targetFound = true;
        		return;
        	}
        	for (int w : maze.adj(output)) {
        		if(!marked[w]) {
        			edgeTo[w] = output;
                    announce();
                    arr.add(w);
            		distTo[w] = distTo[output] + 1;
                    marked[w] = true;
        		}
        	}
        }
    }


    @Override
    public void solve() {
         bfs(s);
    }
}

