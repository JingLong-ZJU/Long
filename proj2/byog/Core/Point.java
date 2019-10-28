package byog.Core;

/*
 * @Cite:https://github.com/GAKKI100/cs61b-sp18-Data-Structure/blob/master/
 * 
 */

public class Point implements java.io.Serializable{
    public int x;
    public int y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object p){
        if (this == p) {
            return true;
        }
        if (p == null) {
            return false;
        }
        if (this.getClass() != p.getClass()) {
            return false;
        }
        Point that = (Point) p;
        return ((this.x == that.x) && (this.y == that.y));
    }
}