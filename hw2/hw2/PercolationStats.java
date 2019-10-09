package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;

public class PercolationStats {
	private double[] ratio;	//threshold
	private Percolation exp;
	private int times; 
	public PercolationStats(int N, int T, PercolationFactory pf) {
		// perform T independent experiments on an N-by-N grid
		if(N<=0||T<=0) {
			throw new IllegalArgumentException();
		}
		ratio = new double[T];
		this.times=T;
		for(int i = 0;i<T;i++) {
			exp = pf.make(N);
			int row,col;
			while(!exp.percolates()) {
				row = StdRandom.uniform(N);
				col = StdRandom.uniform(N);
				exp.open(row, col);				
			}
			if(exp.percolates()) {
				ratio[i]=(double) exp.numberOfOpenSites()/(N*N);
			}
		}
	}
	public double mean() {
		// sample mean of percolation threshold
		return StdStats.mean(ratio);
	}
	public double stddev() {
		// sample standard deviation of percolation threshold
		if(this.times == 1)
			return Double.NaN;
		return StdStats.stddev(ratio);	
	}
	public double confidenceLow() {
		// low endpoint of 95% confidence interval
		return StdStats.mean(ratio)-1.96*StdStats.stddev(ratio)/Math.sqrt(times);
	}
	public double confidenceHigh() {
		// high endpoint of 95% confidence interval
		return StdStats.mean(ratio)+1.96*StdStats.stddev(ratio)/Math.sqrt(times);	
	}
	//for test
//	public static void main (String[] args) {
//		Stopwatch testWatch = new Stopwatch();
//		PercolationStats test = new PercolationStats(60,100,new PercolationFactory());
//		System.out.println(test.mean());
//		System.out.println(testWatch.elapsedTime());
//		
//	}
}
