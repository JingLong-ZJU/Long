package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
	private int s;
    private int t;
    private boolean targetFound = false;
	private Maze maze;
    private Stack<Integer> stack;
    private int start;
    private int end;
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        s = maze.xyTo1D(1, 1);
        t = maze.xyTo1D(maze.N(), maze.N());
        distTo[s] = 0;
        edgeTo[s] = s;
        stack = new Stack<Integer>();
    }

    private void dfs(int v){
        marked[v] = true;
        stack.push(v);
        if (v == t) {
            targetFound = true;
        }
        if (targetFound) {
            return;
        }

        for (int w : maze.adj(v)){
            if (!marked[w]){
                distTo[w] = distTo[v] + 1;
                edgeTo[w] = v;
                dfs(w);
                if (targetFound) {
                    return;
                }

            }else{
                if(distTo[w] != distTo[v] - 1){
                    start = w;
                    end = v;
                    targetFound = true;
                    return;
                }
            }
        }

    }
    @Override
    public void solve() {
        dfs(s);
        announce();
        clearEdge();
    }
    
    private void clearEdge() {
    	Integer pre,now;
    	pre = end;
    	boolean finish = false;
    	stack.pop();   //throw the end node
    	while(!stack.isEmpty()) {
    		if(stack.peek() == start) {
    			finish = true;	//find the cycle
    		}
    		if(!finish) {
    			now = stack.pop();
        		if(distTo[now] != distTo[pre]-1) {
        			edgeTo[now] = now;	//clear the edge which is not in the cycle
        		}
        		else {
        			pre = now;
        		}
    		}
    		else {
    			now = stack.pop();
    			edgeTo[now] = now;
    		}
    	}
        announce();
    	edgeTo[start] = end;
        announce();
    }
}

