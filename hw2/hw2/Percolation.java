package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int[][] chess;
	private int sideLength;
	private int countOpen=0;
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF uf_top;
	private int top;
	private int bottom;	
	private int[][] neighborDirection = {{0,1},{1,0},{-1,0},{0,-1}};
	public Percolation(int N) {
		// create N-by-N grid, with all sites initially blocked
		if(N<=0) {
			throw new IllegalArgumentException(); 
		}
		sideLength=N;
		uf = new WeightedQuickUnionUF(sideLength*sideLength+2);//the biggest two is for top and bottom
		uf_top= new WeightedQuickUnionUF(sideLength*sideLength+1);//only for isFull method
		top=sideLength*sideLength;     //top relation
		bottom=sideLength*sideLength+1;
		chess = new int[N][N];	
		for(int i=0;i<N;i++) {
			for(int j=0;i<N;i++) {
				chess[i][j]=0;
			}
		}		
	}
	
	private int getLoc(int row,int col) {
		validateLoc(row,col);
		return row*sideLength+col;
	}
	
	private void validateLoc(int row,int col) {
		if(row>=sideLength||row<0||col>=sideLength||col<0) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public void open(int row, int col) {
		// open the site (row, col) if it is not open already
		validateLoc(row,col);
		if(!isOpen(row,col)) {
			chess[row][col] = 1;
			countOpen++;	
			if(row == 0) {
				uf.union(top, getLoc(row, col));
				uf_top.union(top,getLoc(row, col));
			}
			if (row == sideLength-1) {
				uf.union(bottom, getLoc(row, col));
			}
			for(int[]d : neighborDirection) {
				int neighborRow = row+d[0];
				int neighborCol = col+d[1];
				if(neighborRow>=0&&neighborRow<sideLength&&neighborCol>=0&&neighborCol<sideLength&&isOpen(neighborRow, neighborCol)) {
					uf.union(getLoc(row, col), getLoc(neighborRow, neighborCol));
					uf_top.union(getLoc(row, col), getLoc(neighborRow, neighborCol));
				}
			}
		}
	}
	public boolean isOpen(int row, int col) {
		// is the site (row, col) open?
		validateLoc(row,col);
		return chess[row][col] == 1;
	}
	public boolean isFull(int row, int col) {
		// is the site (row, col) full?
		validateLoc(row,col);
		return uf_top.connected(top, getLoc(row,col));
	}
	public int numberOfOpenSites() {
		// number of open sites
		return countOpen;
	}
	public boolean percolates() {
		// does the system percolate?
		return countOpen>0&&uf.connected(top, bottom);
	}
	public static void main(String[] args) {
		
	}
}
