package lab11.graphs;

import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    MinPQ<Integer> pq;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        pq = new MinPQ<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if(h(o1) + distTo[o1] < h(o2) + distTo[o2])
					return -1;
				else if(h(o1) + distTo[o1] > h(o2) + distTo[o2])
					return 1;
				else return 0;
			}
		});
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
    	int sourceX = maze.toX(s);
    	int sourceY = maze.toY(s);
    	int targetX = maze.toX(t);
    	int targetY = maze.toY(t);
    	return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return pq.delMin();
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
    	pq.insert(s);
        marked[s] = true;
        announce();
        while(!pq.isEmpty()){
        	int v = findMinimumUnmarked();
        	if (v == t) {
                targetFound = true;
            }
            if (targetFound) {
                return;
            }
            for(int w : maze.adj(v)) {
            	if(!marked[w]) {
            		pq.insert(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    announce();
            	}
            }
            
            
        }
    }

    @Override
    public void solve() {
        astar(s);
    }
}

