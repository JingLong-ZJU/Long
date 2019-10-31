package byog.Core;

import java.lang.reflect.Field;
import java.util.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.TileHumanSet;
import byog.TileEngine.Tileset;


/*
 * Generate a random maze in the world
 * 
 */

public class RandomWorldGenerator {
	private static final int WIDTH = 30;
    private static final int HEIGHT = 14;
    
    private static final Position START_POSITION = new Position(2, 2);
    private static final Direction initDirection = Direction.RIGHT;
    
    private static long SEED = 2875123;
    private static Random RANDOM = new Random(SEED);
    
    private static TETile[][] randomWorld;
    private static worldNode[][] worldDescription;
    
    private static Stack<worldNode> worldNodeStack;
   
    private static final Position player = new Position(2, 2);
    private static Position door;
    
    public RandomWorldGenerator(Long seed) {
    	this.SEED = seed;
    	RANDOM = new Random(this.SEED);
    }
    
    public static TETile[][] getRandomWorld(){
    	return randomWorld;
    }
    
    public static Position getPlayer(){
    	return player;
    }
    
    public static Position getDoor(){
    	return door;
    }
    
    public static void initialWorld() {
    	randomWorld = new TETile[WIDTH][HEIGHT];
    	worldDescription = new worldNode[WIDTH][HEIGHT];
    	worldNodeStack = new Stack<worldNode>();
    	for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
            	randomWorld[x][y] = TileHumanSet.BW_TV;
            	worldDescription[x][y] = new worldNode(new Position(x, y));
            }
        }
    }
    
    public static void addWallsToWorld() {
    	int width = randomWorld.length;
    	int height = randomWorld[0].length;
    	int flag = 0;
    	for (int i = width - 1; i >= 0; i -= 1) {
            for (int j = height - 1; j >= 0; j -= 1) {
            	if(worldDescription[i][j].walls) {
            		randomWorld[i][j] = Tileset.FLOOR;
            		if(flag == 0) {
            			randomWorld[i][j] = Tileset.LOCKED_DOOR;
            			door = new Position(i,j);
            			flag = 1;
            		}
            	}
            }
        }
        randomWorld[player.x][player.y] = TileHumanSet.Kaws;
    }
    
    public static void GenerateClosedWall() {
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
    			worldDescription[nextPosition.x][nextPosition.y].validDirection = getValidDirection(preDirection);
    			worldDescription[nextPosition.x][nextPosition.y].isInit = true;
    		}
    		direction = getNextDirection(nextPosition,preDirection);
    		if(direction == null) {
    			worldNode node;
    			do {
    				try {
    					node = worldNodeStack.pop();
    				}
    				catch (EmptyStackException e) {
    					System.out.println(worldNodeStack.isEmpty());
    					return;
//    					throw new EmptyStackException();
    				}
    				
//    				worldDescription[node.position.x][node.position.y].walls = false;
    				worldDescription[node.position.x][node.position.y].validDirection.
    					remove(worldDescription[node.position.x][node.position.y].direction);
    			}while((node.validDirection.size() < 1) && !worldNodeStack.isEmpty());
    			
    			if(worldNodeStack.isEmpty())return;
    			
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
    	if(!validPosition(position1) || worldDescription[position1.x][position1.y].walls)
    		return false;
    	
//    	List<Direction> list = getValidDirection(direction);
//    	Position positionA = getNextPosition(position1, list.get(0));
//    	Position positionB = getNextPosition(position1, list.get(1));
//    	Position positionC = getNextPosition(position1, list.get(2));
//    	
//    	if(validPosition(positionA) && worldDescription[positionA.x][positionA.y].walls 
//    			|| validPosition(positionB) && worldDescription[positionB.x][positionB.y].walls
//    			|| validPosition(positionC) && worldDescription[positionC.x][positionC.y].walls)
//    		return false;
    	if(direction.equals(direction.TOP)) {
    		List<Position> fivePo = new ArrayList<Position>() {{
    			add(new Position(position1.x, position1.y + 1));
    			add(new Position(position1.x-1, position1.y));
    			add(new Position(position1.x+1, position1.y));
    			add(new Position(position1.x-1, position1.y + 1));
    			add(new Position(position1.x+1, position1.y + 1));
    		}};
    		Iterator<Position> iterator = fivePo.iterator();
    		while(iterator.hasNext()) {
    			Position i = iterator.next();
        		if(validPosition(i) && worldDescription[i.x][i.y].walls) {
        			return false;
        		}
        	}
    		return true;
    	}
    	else if(direction.equals(direction.BOTTOM)) {
    		List<Position> fivePo = new ArrayList<Position>() {{
    			add(new Position(position1.x, position1.y - 1));
    			add(new Position(position1.x-1, position1.y));
    			add(new Position(position1.x+1, position1.y));
    			add(new Position(position1.x-1, position1.y - 1));
    			add(new Position(position1.x+1, position1.y - 1));
    		}};
    		Iterator<Position> iterator = fivePo.iterator();
    		while(iterator.hasNext()) {
    			Position i = iterator.next();
        		if(validPosition(i) && worldDescription[i.x][i.y].walls) {
        			return false;
        		}
        	}
    		return true;
    	}
    	else if(direction.equals(direction.RIGHT)) {
    		List<Position> fivePo = new ArrayList<Position>() {{
    			add(new Position(position1.x+1, position1.y));
    			add(new Position(position1.x, position1.y+1));
    			add(new Position(position1.x, position1.y-1));
    			add(new Position(position1.x+1, position1.y+1));
    			add(new Position(position1.x+1, position1.y-1));
    		}};
    		Iterator<Position> iterator = fivePo.iterator();
    		while(iterator.hasNext()) {
    			Position i = iterator.next();
        		if(validPosition(i) && worldDescription[i.x][i.y].walls) {
        			return false;
        		}
        	}
    		return true;
    	}
    	else {
    		List<Position> fivePo = new ArrayList<Position>() {{
    			add(new Position(position1.x-1, position1.y));
    			add(new Position(position1.x, position1.y+1));
    			add(new Position(position1.x, position1.y-1));
    			add(new Position(position1.x-1, position1.y+1));
    			add(new Position(position1.x-1, position1.y-1));
    		}};
    		Iterator<Position> iterator = fivePo.iterator();
    		while(iterator.hasNext()) {
    			Position i = iterator.next();
        		if(validPosition(i) && worldDescription[i.x][i.y].walls) {
        			return false;
        		}
        	}
    		return true;
    	}
    	
    }
    
    private static ArrayList<Direction> getValidDirection(Direction direction) {
    	switch(direction) {
	    	case TOP:{
	    		 return new ArrayList<Direction>() {{
	    			add(Direction.TOP);
	    			add(Direction.RIGHT);
	    			add(Direction.LEFT);
	    		 }};
	    	}
	    	case BOTTOM:{
	    		 return new ArrayList<Direction>() {{
	    			add(Direction.BOTTOM);
	    			add(Direction.RIGHT);
	    			add(Direction.LEFT);
	    		 }};
	    	}
	    	case LEFT:{
	    		 return new ArrayList<Direction>() {{
	    			add(Direction.BOTTOM);
	    			add(Direction.TOP);
	    			add(Direction.LEFT);
	    		 }};
	    	}
	    	case RIGHT:{
	    		 return new ArrayList<Direction>() {{
	    			add(Direction.BOTTOM);
	    			add(Direction.TOP);
	    			add(Direction.RIGHT);
	    		 }};
	    	}
	    	default:return null;
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
        addWallsToWorld();
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
