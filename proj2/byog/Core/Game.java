package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.TileHumanSet;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

import java.awt.*;
import java.io.*;
/**
 * @author Long
 * Add a maze game, use a integer to decide the mode(play with shadow or not,
 * 0 is the normal mode, others correspond shadow mode). 
 *
 */


public class Game{
    /* Feel free to change the width and height. */
    public static final int WIDTH = 30;
    public static final int HEIGHT = 15;
    public TERenderer ter = new TERenderer();
    private static int MouseX;
    private static int MouseY;
    public Position player;
    public Position door;
    public boolean win = false;
    public Life life;
    public static TETile[][] finalWorldFrame;
    public static TETile[][] shadowWorldFrame;
    public static boolean shadow = false;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    
    public Game(int mode) {
    	if(mode != 0) {
    		shadow = true;
    	}
    }
    
    public void playWithKeyboard() {
        GUI_Initial();
        drawGUI();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                break;
            }
        }
        char c = StdDraw.nextKeyTyped();
        if (c == 'N' || c == 'n'){
            newGame();
            System.exit(0);
        } else if (c == 'L' || c == 'l'){
        	String filename = "save.file";
        	try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
	        	finalWorldFrame = (TETile[][]) in.readObject();
	        	this.player = (Position) in.readObject();
	        	this.door  = (Position) in.readObject();
	        	this.life = (Life) in.readObject();
	        	this.shadow = (Boolean) in.readObject();
	        	in.close();
                ter.renderFrame(finalWorldFrame);
                System.out.println("Object has been deserialized ");
                shadowWorldFrame = new TETile[WIDTH][HEIGHT];
                refreshShadow();
//                readToStart();
                operation();
                System.exit(0);
            } catch(IOException ex) {
                System.out.println("IOException is caught");
            } catch(ClassNotFoundException ex)
            {
                System.out.println("ClassNotFoundException is caught");
            }

        }else if (c == 'Q' || c == 'q'){
            System.exit(0);
        }

    }

    public void GUI_Initial(){
//        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
    	StdDraw.setCanvasSize(this.WIDTH * 48, this.HEIGHT * 48);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public void drawGUI() {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(((double)WIDTH)/2, ((double)HEIGHT)*3/4, "CS 61B: THE GAME");
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(((double)WIDTH)/2, ((double)HEIGHT)*3/4 * 0.5, "New Game(N)");
        StdDraw.text(((double)WIDTH)/2, ((double)HEIGHT)*3/4 * 0.4, "Load Game(L)");
        StdDraw.text(((double)WIDTH)/2, ((double)HEIGHT)*3/4 * 0.3, "Quit Game(Q)");
        StdDraw.show();
    }

    public void drawSeed(String s){
    	StdDraw.clear();
        StdDraw.clear(Color.black);
    	
        Font smallFont = new Font("Monaco", Font.CENTER_BASELINE, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(((double)WIDTH)/2, ((double)HEIGHT)*3/4 * 0.2, "SEED: "+s);
        StdDraw.text(((double)WIDTH)/2, ((double)HEIGHT)*3/4 * 0.1, "Press 'S' to end up your input!");
        StdDraw.show();
    }

    public void newGame(){
        ter.initialize(WIDTH, HEIGHT);
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        shadowWorldFrame = new TETile[WIDTH][HEIGHT];
        String input = "";
        drawSeed(input);
        while (true){
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = StdDraw.nextKeyTyped();
            if (c == 'S'){
                break;
            }
            input += String.valueOf(c);
//            drawGUI();
            drawSeed(input);
        }
        Long seed = toDigit(input);
//        PlaceRooms PLACEROOMS = new PlaceRooms(seed);
//        PLACEROOMS.initial(finalWorldFrame, Tileset.NOTHING);
//        PLACEROOMS.addMaze(finalWorldFrame,Tileset.FLOOR);
//        PLACEROOMS.addWall(finalWorldFrame,TileHumanSet.BW_TV);
//        player = PLACEROOMS.player;
//        door = PLACEROOMS.door;
//        life = new Life(5);

        RandomWorldGenerator wo = new RandomWorldGenerator(seed);
        wo.initialWorld();
        wo.GenerateClosedWall();
        wo.addWallsToWorld();
        TETile[][] getWorld = wo.getRandomWorld();
        for(int i = 0; i < getWorld.length; i++) {
        	for(int j = 0; j < getWorld[0].length; j++) {
        		finalWorldFrame[i][j] = getWorld[i][j];
        		shadowWorldFrame[i][j] = finalWorldFrame[i][j];
        	}
        }
        this.player = wo.getPlayer();
        this.door = wo.getDoor();
        life = new Life(5);
//        readToStart();
//        drawHelpQuit();
        drawlife(life.life);
        operation();
    }
    
    public void readToStart() {
		drawHelpGo();
    	ter.renderFrame(finalWorldFrame);
    	while (true){
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = StdDraw.nextKeyTyped();
            if (c == 'G'){
                break;
            }
        }
    	System.out.println("123");
    }
    
    public void operation(){
    	drawHelpQuit();
        ter.renderFrame(finalWorldFrame);
    	while (true){
            if(StdDraw.hasNextKeyTyped()){
                char c = StdDraw.nextKeyTyped();
                stepWithChar(c);
                if(c == 'Q' || c == 'q')
                	break;
            }
            if(StdDraw.isMousePressed()){
                MouseX = (int)StdDraw.mouseX();
                MouseY = (int)StdDraw.mouseY();
                String des = finalWorldFrame[MouseX][MouseY].description();
                drawHUD(des);
                if(shadow) {
                	ter.renderFrame(shadowWorldFrame);
                }else {
                	ter.renderFrame(finalWorldFrame);
                }
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void stepWithChar(char c) {
    	if (c == 'Q' || c == 'q'){
            String filename = "save.file";
            // Serialization
            try
            {
                //Saving of object in a file
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
				out.writeObject(finalWorldFrame);
                out.writeObject(this.player);
                out.writeObject(this.door);
                out.writeObject(this.life);
                out.writeObject(this.shadow);
                out.close();	
                System.out.println("Saved!");
            } 
            catch(IOException ex)
            {
                System.out.println("IOException is caught");
            }
            return;
        }
        playerMove(c);
        drawlife(life.life);
        if(shadow) {
        	ter.renderFrame(shadowWorldFrame);
        }else {
        	ter.renderFrame(finalWorldFrame);
        }
        if (win){
            System.exit(0);
        }else if (life.life == 0){
            System.exit(0);
        }
        try
        {
            Thread.sleep(10);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ter.initialize(WIDTH, HEIGHT);

        Long seed = toDigit(input);

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        ter.renderFrame(finalWorldFrame);
        PlaceRooms PLACEROOMS = new PlaceRooms(seed);
        TETile t = Tileset.FLOOR;
        PLACEROOMS.addMaze(finalWorldFrame,t);
        t = TileHumanSet.BW_TV;
        PLACEROOMS.addWall(finalWorldFrame,t);
        player = PLACEROOMS.player;
        door = PLACEROOMS.door;
        life = new Life(5);
        drawlife(life.life);
        ter.renderFrame(finalWorldFrame);
        operation();
        return finalWorldFrame;
    }

    public Long toDigit(String input){
        char[] c = input.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++){
            if (Character.isDigit(c[i])){
            	builder.append(c[i]);
            }
        }
        return Long.parseLong(builder.toString());
    }

    public void  drawHUD(String des) {
        for (int i = 0; i < 15; i++){
            finalWorldFrame[i][HEIGHT-1] = Tileset.NOTHING;
            refreshShadow();
        }
        for (int i = 0; i < des.length(); i++){
            finalWorldFrame[i][HEIGHT-1] = new TETile (des.toCharArray()[i], Color.white, Color.black, "nothing");
            refreshShadow();
        }
    }

    public void drawlife(int life){
        for (int i = WIDTH - 10; i < WIDTH-1; i++) {
            finalWorldFrame[i][HEIGHT - 1] = Tileset.NOTHING;
            refreshShadow();
        }
        for (int i = WIDTH - 10; i < WIDTH - 10 + life; i++){
            finalWorldFrame[i][HEIGHT-1] = Tileset.HEART;
            refreshShadow();
        }
    }

    public void drawHelpGo() {
    	for (int i = 0; i < WIDTH; i++){
            finalWorldFrame[i][HEIGHT-1] = Tileset.NOTHING;
            refreshShadow();
        }
//    	String d = "Press G to start!";
//    	char[] des = d.toCharArray();
//        for (int i = 30; i < 30 + des.length; i++){
//            finalWorldFrame[i][HEIGHT-1] = new TETile (des[i - 30], Color.white, Color.black, "nothing");
//            refreshShadow();
//        }
    }
    public void drawHelpQuit() {
    	for (int i = 0; i < WIDTH; i++){
            finalWorldFrame[i][HEIGHT-1] = Tileset.NOTHING;
            refreshShadow();
        }
    	String d = "Press Q to quit!";
    	char[] des = d.toCharArray();
        for (int i = 10; i < 10 + des.length; i++){
            finalWorldFrame[i][HEIGHT-1] = new TETile (des[i - 10], Color.white, Color.black, "nothing");
            refreshShadow();
        }
    }
    public void playerMove(char c){
        TETile t = TileHumanSet.BW_TV;
        TETile destination = Tileset.LOCKED_DOOR;
        if (c == 'w' || c == 'W'){
        	if(!((player.y+1) < HEIGHT - 1))
        		return;
            if (!finalWorldFrame[player.x][player.y+1].equals(t)){
                finalWorldFrame[player.x][player.y] = Tileset.FLOOR;
                finalWorldFrame[player.x][player.y+1] = TileHumanSet.Kaws;
                refreshShadow();
                player = new Position(player.x, player.y+1);
                if (player.equals(door)){
                    win = true;
                }
            }
            else{
                life.life--;
            }
        }else if (c == 'a'|| c == 'A'){
        	if(!((player.x-1) >= 0))
        		return;
            if (!finalWorldFrame[player.x-1][player.y].equals(t)){
                finalWorldFrame[player.x][player.y] = Tileset.FLOOR;
                finalWorldFrame[player.x-1][player.y] = TileHumanSet.Kaws;
                refreshShadow();
                player = new Position(player.x-1, player.y);
                if (player.equals(door)){
                    win = true;
                }
            } else{
                life.life--;
            }
        }else if (c == 's'|| c == 'S'){
        	if(!((player.y-1) >= 0))
        		return;
            if (!finalWorldFrame[player.x][player.y-1].equals(t)){
                finalWorldFrame[player.x][player.y] = Tileset.FLOOR;
                finalWorldFrame[player.x][player.y-1] = TileHumanSet.Kaws;
                refreshShadow();
                player = new Position(player.x, player.y-1);
                if (player.equals(door)){
                    win = true;
                }
            }else{
                life.life--;
            }
        }else if (c == 'd'|| c == 'D'){
        	if(!((player.x+1) < WIDTH))
        		return;
            if (!finalWorldFrame[player.x+1][player.y].equals(t)){
                finalWorldFrame[player.x][player.y] = Tileset.FLOOR;
                finalWorldFrame[player.x+1][player.y] = TileHumanSet.Kaws;
                refreshShadow();
                player = new Position(player.x+1, player.y);
                if (player.equals(door)){
                    win = true;
                }
            }else{
                life.life--;
            }
        }else {
            return ;
        }
    }
    
    private void refreshShadow() {
    	int x = this.player.x;
    	int y = this.player.y;
    	for(int i = 0; i < WIDTH; i++) {
    		for(int j = 0; j < HEIGHT; j++) {
    			if(((x - i) * (x - i) + (y - j) * (y - j) <= 75) || j == HEIGHT - 1) {
    				shadowWorldFrame[i][j] = finalWorldFrame[i][j];
    			}
    			else {
    				shadowWorldFrame[i][j] = Tileset.NOTHING;
    			}
    		}
    	}
    }
    
    public static void main(String[] args) {
    	Game game = new Game(1);
        game.playWithKeyboard();
    }
    
}