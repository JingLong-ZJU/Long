import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private Picture picture;
	private int width;
	private int height;
	private double[][] energyMatrix;
	private double[][] M;
	private double[][] MT;//the M matrix after transpose
	boolean isVertical = true;
    public SeamCarver(Picture picture) {
    	this.picture = picture;
    	regeneration();
    }
    
    private void regeneration() {
    	energyMatrix = new double[picture.width()][picture.height()];
    	this.width = picture.width();
    	this.height = picture.height();
    	for(int i = 0; i < picture.width(); i++) {
    		for(int j = 0; j < picture.height(); j++) {
    			energyMatrix[i][j] = this.energy(i, j);
        	}
    	}
    	this.M = getM();
    	rotate();
    	this.MT = getM();
    	rotate();
    	this.isVertical = true;
    }
    
    private void rotate() {
    	this.energyMatrix = matrixTranspose(energyMatrix);
    	int temp = this.height;
    	this.height = this.width;
    	this.width = temp;
    	this.isVertical = !this.isVertical;
    }
    private double[][] matrixTranspose(double[][] matrix){
    	if(matrix.length == 0) {
    		throw new IndexOutOfBoundsException("can`t transpose an empty matrix");
    	}
    	double arr[][] = new double[matrix[0].length][matrix.length];
    	for(int i = 0; i < arr.length; i++) {
    		for(int j = 0; j < arr[0].length; j++) {
    			arr[i][j] = matrix[j][i];
    		}
    	}
    	return arr;
    }
    private double[][] getM(){
    	double m[][] = new double[width][height];
    	for(int i = 0; i < width; i++) {
    		m[i][0] = energyMatrix[i][0];
    	}
    	for(int j = 1; j < height; j++) {
    		for(int i = 0; i < width; i++) {
    			if(i == 0) {
    				m[i][j] = energyMatrix[i][j] + Math.min(m[i][j-1], m[(i+1)%width][j-1]);
    			}
    			else if(i == width - 1) {
    				m[i][j] = energyMatrix[i][j] + Math.min(m[i][j-1], m[(i-1+width)%width][j-1]);
    			}
    			else {
    				m[i][j] = energyMatrix[i][j] + 
    					Math.min(m[i][j-1], Math.min(m[(i-1+width)%width][j-1], m[(i+1)%width][j-1]));
    			}
    		}
    	}
    	return m;
    }
    
    public Picture picture() {
    	// current picture
    	return this.picture;
    }
    public     int width() {
    	// width of current picture
    	return width;
    }
    public     int height() {
    	// height of current picture
    	return height;
    }
    public  double energy(int x, int y) {        
    	// energy of pixel at column x and row y
    	validateColumnIndex(x);
    	validateRowIndex(y);
    	int Rx,Gx,Bx,Ry,Gy,By,deltaX,deltaY;
    	
    	Rx = this.picture.get((x + 1)%width, y).getRed() - this.picture.get((x + width - 1)%width, y).getRed();
    	Gx = this.picture.get((x + 1)%width, y).getGreen() - this.picture.get((x + width - 1)%width, y).getGreen();
		Bx = this.picture.get((x + 1)%width, y).getBlue() - this.picture.get((x + width - 1)%width, y).getBlue();
		
		Ry = this.picture.get(x, (y + 1)%height).getRed() - this.picture.get(x, (y + height - 1)%height).getRed();
		Gy = this.picture.get(x, (y + 1)%height).getGreen() - this.picture.get(x, (y + height - 1)%height).getGreen();
		By = this.picture.get(x, (y + 1)%height).getBlue() - this.picture.get(x, (y + height - 1)%height).getBlue();
		
    	deltaX = Rx * Rx + Gx * Gx + Bx * Bx;
    	deltaY = Ry * Ry + Gy * Gy + By * By;
    	return deltaX + deltaY;
    }
    public   int[] findHorizontalSeam() {
    	// sequence of indices for horizontal seam
    	rotate();
    	int[] arr = findVerticalSeam();
    	rotate();
    	return arr;
    }
    public   int[] findVerticalSeam() {
    	// sequence of indices for vertical seam
    	double min = this.getMContent(0, height - 1);
    	int index = 0;
    	for(int i = 0; i < width; i++) {
    		if(min > this.getMContent(i, height - 1)) {
    			min = this.getMContent(i, height - 1);
    			index = i;
    		}
    	}
    	if(this.isVertical)return findPath(M,index);
    	else return findPath(MT,index);
    }
    
    private double getMContent(int i, int j) {
    	if(this.isVertical) {
    		return M[i][j];
    	}
    	else {
    		return MT[i][j];
    	}
    }
    
    private int[] findPath(double[][] matrix,int start) {
    	int[] arr = new int[matrix[0].length];
    	arr[arr.length-1] = start;
    	int pre = start;
    	for(int j = arr.length-2; j >= 0; j--) {
    		if(pre == 0) {
    			if(matrix[pre][j] < matrix[(pre + 1)%width][j]) {
    				arr[j] = pre;
    			}
    			else {
    				pre = (pre + 1)%width;
    				arr[j] = pre;
    			}
    		}
    		else if(pre == matrix.length - 1) {
    			if(matrix[(pre + width - 1)%width][j] < matrix[pre][j]) {
    				pre = (pre + width - 1)%width;
    				arr[j] = pre;
    			}
    			else {
    				arr[j] = pre;
    			}
    		}
    		else {
    			double minNow = Math.min(matrix[(pre + width - 1)%width][j], Math.min(matrix[pre][j], matrix[(pre + 1)%width][j]));
    			if(matrix[(pre + width - 1)%width][j] == minNow) {
    				pre = (pre + width - 1)%width;
    				arr[j] = pre;
    			}
    			else if(matrix[pre][j] == minNow) {
    				arr[j] = pre;
    			}
    			else {
    				pre = (pre + 1)%width;
    				arr[j] = pre;
    			}
    		}
    	}
    	return arr;
    }
    public    void removeHorizontalSeam(int[] seam) {
    	// remove horizontal seam from picture
    	this.picture = SeamRemover.removeHorizontalSeam(this.picture, findHorizontalSeam());
    	regeneration();
    }
    public    void removeVerticalSeam(int[] seam) {
    	// remove vertical seam from picture
    	this.picture = SeamRemover.removeVerticalSeam(this.picture, findVerticalSeam());
    	regeneration();
    }
    private void validateColumnIndex(int col) {
        if (col < 0 || col >= width())
            throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
    }
    private void validateRowIndex(int row) {
        if (row < 0 || row >= height())
            throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
    }
}
