package byog.lab5;
import static org.junit.Assert.*;

import byog.Core.Direction;
import byog.Core.Position;
import byog.Core.RandomUtils;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final Random RANDOM = new Random();
    private static final int SIZE = 3;
	private static final int WIDTH = 8 * SIZE + 1;
    private static final int HEIGHT = 10 * SIZE;

//    public HexWorld(){
//    	this(50, 40, 3);
//    }
//    
//    public HexWorld(int width, int height, int size){
//    	this.WIDTH = width;
//    	this.HEIGHT = height;
//    	this.SIZE = size;
//    }
    
	public static int hexRowWidth(int size, int index) {
		if(index >= 2 * size) {
			throw new IllegalArgumentException("can`t get the num of the row index "
					+ "out of the height");
		}
        if (index >= size) {
            return 5 * size - 2 * index -2;
        }
        else {
            return size + 2 * index;
        }
    }
	public static int hexRowOffset(int size, int index) {
        if (index >= size) {
        	return index + 1 - 2 * size;
        }
        else {
            return -index;
        }
    }
	
    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param p the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }
    /**
     * Adds a hexagon to the world.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the hexagon
     * @param s the size of the hexagon
     * @param t the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {

        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;
            int xRowStart = p.x + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(s, yi);

            addRow(world, rowStartP, rowWidth, t);
        }
    }
    
    public static void initialWorld(TETile[][] world) {
    	int width = world.length;
    	int height = world[0].length;
    	for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
    
    private static List<Position> generateHexagonWorld(TETile[][] world) {
    	int center = WIDTH/2 - 1;
    	List<Position> position = new ArrayList<Position>();
    	position.add(new Position(center, 0));
    	for(int i = 1; i <= 3; i++) {
    		position.add(new Position(center - 3*SIZE, 2*SIZE*i));
    		position.add(new Position(center + 3*SIZE, 2*SIZE*i));
    		position.add(new Position(center, 2*SIZE*i));
    	}
    	for(int i = 0; i < 4; i++) {
    		position.add(new Position(center - 2*SIZE + 1, (2*i+1)*SIZE));
    		position.add(new Position(center + 2*SIZE - 1, (2*i+1)*SIZE));
    	}
    	position.add(new Position(center, 8 * SIZE));
    	return position;
    }
    
	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] hexagonTiles = new TETile[WIDTH][HEIGHT];
        initialWorld(hexagonTiles);
        List<Position> six = generateHexagonWorld(hexagonTiles);
        
        Class clazz = Class.forName("byog.TileEngine.Tileset");
        Field[] fields = clazz.getFields();
        int j = 0;
        for(Position i : six) {
        	j++;
            HexWorld.addHexagon(hexagonTiles, i, HexWorld.SIZE,
            		(TETile) fields[j%fields.length].get(fields[j%fields.length].getName()));
        }
        ter.renderFrame(hexagonTiles);
//		List<Integer> directionSet = new ArrayList<Integer>();
//		directionSet.add(111);
//		directionSet.add(222);
//		directionSet.add(333);
//		Iterator<Integer> iterator = directionSet.listIterator();
//    	while(iterator.hasNext()) {
//    		Integer i = iterator.next();
//    		if(i.equals(222)) {
//    			iterator.remove();
//    		}
//    	}
//       for(Integer i : directionSet) {
//    	   System.out.println(i);
//       }
//        for(int i = 0; i < fields.length; i++) {
//        	System.out.println(fields[i].get(fields[i].getName()));
//        }
    }
}
