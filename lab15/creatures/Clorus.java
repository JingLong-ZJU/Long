package creatures;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

public class Clorus extends Creature{
	/** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;
    /** fraction of energy to retain when replicating. */
    private double repEnergyRetained = 0.5;
    /** fraction of energy to bestow upon offspring. */
    private double repEnergyGiven = 0.5;
    
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    public Clorus() {
        this(1);
    }
    /**
     * The color() method for Cloruses should 
     * always return the color red = 34, green = 0, blue = 231.
     */
    public Color color() {
        r = 34;
        b = 231;
        g = 0;
        return color(r, g, b);
    }

    /** Do nothing with C, Plips are pacifists. */
    public void attack(Creature c) {
    	this.energy += c.energy();
    }

    public void move() {
    	this.energy -= 0.03 ;
    }
    
    public String name() {
    	return "plip";
    }

    public void stay() {
    	this.energy -= 0.01;
    }

    public Plip replicate() {
		energy = energy * repEnergyRetained;
		double babyEnergy = energy * repEnergyGiven;
		return new Plip(babyEnergy);
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> foods = getNeighborsOfType(neighbors, "plip");

        if(empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }
        else if(foods.size() > 1) {
        	Direction attDir = HugLifeUtils.randomEntry(foods);
        	Creature food = (Creature) neighbors.get(attDir);
        	attack(food);
        	return new Action(Action.ActionType.ATTACK, attDir);
        }
        else if(this.energy > 1){
        	Direction repDir = HugLifeUtils.randomEntry(empties);
        	return new Action(Action.ActionType.REPLICATE, repDir);
        }
        else {
        	Direction movDir = HugLifeUtils.randomEntry(empties);
        	return new Action(Action.ActionType.REPLICATE, movDir);
        }
    }
}
