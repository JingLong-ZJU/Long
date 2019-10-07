package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;
public class Board implements WorldState  {
	private int size;
	private int[][] tiles;
	private final int BLANK = 0;
    /** Returns the string representation of the board. 
      * Uncomment this method. */
	  public Board(int[][] tiles) {
		  if(tiles.length == 0 || tiles.length != tiles[0].length) {
			  throw new IllegalArgumentException("input data is invalid");
		  }
		  this.size = tiles.length;
		  this.tiles = new int[this.size][this.size];
		  for(int i = 0; i < this.size; i++) {
			  for(int j = 0; j < this.size; j++) {
				  this.tiles[i][j] = tiles[i][j];
			  }
		  }
	  }
	  
	  private void validateIndex(int i, int j) {
		  if(i<0||i>=size()||j<0||j>=size()) {
			  throw new IndexOutOfBoundsException("invalid index");
		  }
	  }
	  public int tileAt(int i, int j) {
		  validateIndex(i,j);
		  return tiles[i][j];
	  }
	  public int size() {
		  return this.size;
	  }
	  
	  private int getRightValue(int i, int j) {
		  validateIndex(i,j);
		  if(i == this.size-1 && j == this.size-1)
			  return BLANK;
		  else
			  return i*size()+j+1;
	  }
	  
	  public int hamming() {
		  int count = 0;
		  for(int i=0; i<size(); i++) {
			  for(int j=0; j<size(); j++) {
				  if(this.tileAt(i, j) == this.BLANK)
					  continue;
				  if(this.tileAt(i, j) != getRightValue(i,j))
						  count++;
			  }
		  }
		  return count;
	  }
	  
	  
	  public int manhattan() {
		  int count = 0;
		  for(int i=0; i<size(); i++) {
			  for(int j=0; j<size(); j++) {
				  int value = this.tileAt(i, j);
				  if(value == this.BLANK)
					  continue;
				  if(value != getRightValue(i,j)) {
					  int wrongRow = (value-1)/this.size();
					  int wrongCol = (value-1)%this.size();
					  count += Math.abs(wrongRow - i);
					  count += Math.abs(wrongCol - j);
				  }
			  }
		  }
		  return count;
	  }
	  public boolean equals(Object y) {
		  Board yy = (Board) y;
		  if(size() != yy.size())
			  return false;
		  for(int i=0;i<size();i++) {
			  for(int j=0;j<size();j++) {
				  if(this.tileAt(i, j) != yy.tileAt(i, j))
					  return false;
			  }
		  }
		  return true;
	  }
	  
	  
	  @Override
		public int estimatedDistanceToGoal() {
			return this.manhattan();
		}

		@Override
		public Iterable<WorldState> neighbors() {
	        Queue<WorldState> neighbors = new Queue<>();
	        int hug = size();
	        int bug = -1;
	        int zug = -1;
	        for (int rug = 0; rug < hug; rug++) {
	            for (int tug = 0; tug < hug; tug++) {
	                if (tileAt(rug, tug) == BLANK) {
	                    bug = rug;
	                    zug = tug;
	                }
	            }
	        }
	        int[][] ili1li1 = new int[hug][hug];
	        for (int pug = 0; pug < hug; pug++) {
	            for (int yug = 0; yug < hug; yug++) {
	                ili1li1[pug][yug] = tileAt(pug, yug);
	            }
	        }
	        for (int l11il = 0; l11il < hug; l11il++) {
	            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
	                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
	                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
	                    ili1li1[l11il][lil1il1] = BLANK;
	                    Board neighbor = new Board(ili1li1);
	                    neighbors.enqueue(neighbor);
	                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
	                    ili1li1[bug][zug] = BLANK;
	                }
	            }
	        }
	        return neighbors;
		}
	  
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}