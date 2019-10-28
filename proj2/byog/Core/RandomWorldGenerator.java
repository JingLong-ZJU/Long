package byog.Core;

import java.lang.reflect.Field;
import java.util.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


public class RandomWorldGenerator {
	private static final int WIDTH = 40;
    private static final int HEIGHT = 15;
    
    private static final Position START_POSITION = new Position(1, 1);
    private static final Direction initDirection = Direction.RIGHT;
    
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    
    private static TETile[][] randomWorld;
    private static worldNode[][] worldDescription;
    
    private static Stack<worldNode> worldNodeStack;
    
    
    private RandomWorldGenerator() {
    }
    
    private static void initialWorld() {
    	randomWorld = new TETile[WIDTH][HEIGHT];
    	worldDescription = new worldNode[WIDTH][HEIGHT];
    	worldNodeStack = new Stack<worldNode>();
    	for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
            	randomWorld[x][y] = Tileset.NOTHING;
            	worldDescription[x][y] = new worldNode(new Position(x, y));
            }
        }
    }
    
    private static void addWallsToWorld(TETile[][] world) {
    	int width = world.length;
    	int height = world[0].length;
    	for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
            	if(worldDescription[i][j].walls)
            		world[i][j] = Tileset.WALL;
            }
        }
    }
    
    private static void GenerateClosedWall() {
    	worldDescription[START_POSITION.x][START_POSITION.y].walls = true;
    	worldDescription[START_POSITION.x][START_POSITION.y].direction = initDirection;
    	worldDescription[START_POSITION.x][START_POSITION.y].validDirection = new ArrayList<Direction>() {{
    		add(initDirection);
    	}};
    	Direction direction = initDirection;
    	Direction preDirection = initDirection;
    	Position curPosition = START_POSITION;
    	Position nextPosition = START_POSITION;
    	do {
    		nextPosition = getNextPosition(curPosition, preDirection);
    		if(!worldDescription[nextPosition.x][nextPosition.y].isInit) {
    			getValidDirection(nextPosition, preDirection);
    			worldDescription[nextPosition.x][nextPosition.y].isInit = true;
    		}
    		direction = getNextDirection(nextPosition,preDirection);
    		if(direction == null) {
    			worldNode node;
    			do {
    				node = worldNodeStack.pop();
    				worldDescription[node.position.x][node.position.y].walls = false;
    				worldDescription[node.position.x][node.position.y].validDirection.
    					remove(worldDescription[node.position.x][node.position.y].direction);
    			}while(node.validDirection.size() < 1);
    			curPosition = node.position;
    			preDirection = node.validDirection.get(RANDOM.nextInt(node.validDirection.size()));
    		}
    		else {
    			preDirection = direction;
    			curPosition = nextPosition;
        		worldDescription[nextPosition.x][nextPosition.y].walls = true;
    			worldDescription[nextPosition.x][nextPosition.y].direction = direction;
    			worldNodeStack.push(worldDescription[nextPosition.x][nextPosition.y]);
    		}
    	}while(!START_POSITION.equals(nextPosition));
    }
    private static Direction getNextDirection(Position position, Direction direction) {
    	Iterator<Direction> iterator = worldDescription[position.x][position.y].validDirection.listIterator();
    	while(iterator.hasNext()) {
    		Direction i = iterator.next();
    		if(!validNextDirection(position, i)) {
    			iterator.remove();
    		}
    	}
    	if(worldDescription[position.x][position.y].validDirection.isEmpty())
    		return null;
    	else return worldDescription[position.x][position.y].validDirection.
    			get(RANDOM.nextInt(worldDescription[position.x][position.y].validDirection.size()));
    }
    
    private static boolean validNextDirection(Position currentPosition, Direction direction) {
    	Position position1 = getNextPosition(currentPosition, direction);
    	Position position2 = getNextPosition(position1, direction);
    	if(!validPosition(position1) || worldDescription[position1.x][position1.y].walls)
    		return false;
    	else if(validPosition(position2) && !worldDescription[position2.x][position2.y].walls)
			return true;
    	else return false;
    }
    
    private static void getValidDirection(Position currentPosition, Direction direction) {
    	switch(direction) {
	    	case TOP:{
	    		worldDescription[currentPosition.x][currentPosition.y].validDirection
	    		 = new ArrayList<Direction>() {{
	    			add(Direction.TOP);
	    			add(Direction.RIGHT);
	    			add(Direction.LEFT);
	    		 }};
	    	}
	    	case BOTTOM:{
	    		worldDescription[currentPosition.x][currentPosition.y].validDirection
	    		 = new ArrayList<Direction>() {{
	    			add(Direction.BOTTOM);
	    			add(Direction.RIGHT);
	    			add(Direction.LEFT);
	    		 }};
	    	}
	    	case LEFT:{
	    		worldDescription[currentPosition.x][currentPosition.y].validDirection
	    		 = new ArrayList<Direction>() {{
	    			add(Direction.BOTTOM);
	    			add(Direction.TOP);
	    			add(Direction.LEFT);
	    		 }};
	    	}
	    	case RIGHT:{
	    		worldDescription[currentPosition.x][currentPosition.y].validDirection
	    		 = new ArrayList<Direction>() {{
	    			add(Direction.BOTTOM);
	    			add(Direction.TOP);
	    			add(Direction.RIGHT);
	    		 }};
	    	}
	    	default:return;
    	}
    }
    
    private static boolean validPosition(Position position) {
    	return !(position.x >= WIDTH || position.x < 0 || position.y >= HEIGHT || position.y < 0);
    }
    
    private static Position getNextPosition(Position currentPosition, Direction direction) {
    	switch(direction){
	    	case LEFT:
	    		return new Position(currentPosition.x - 1, currentPosition.y);
			case RIGHT:
				return new Position(currentPosition.x + 1, currentPosition.y);
    		case BOTTOM:
    			return new Position(currentPosition.x, currentPosition.y - 1);
    		case TOP:
    			return new Position(currentPosition.x, currentPosition.y + 1);
    	}
    	return new Position(currentPosition.x, currentPosition.y);
    }
    
    
    public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        initialWorld();
        GenerateClosedWall();
        addWallsToWorld(randomWorld);
        ter.renderFrame(randomWorld);
    }
    
}
class worldNode{
	Position position;
	boolean walls;
	boolean isInit;
	Direction direction;
	List<Direction> validDirection = null;
	public worldNode(Position inputPosition) {
		walls = false;
		direction = Direction.RIGHT;
		position = inputPosition;
		isInit = false;
	}
}
