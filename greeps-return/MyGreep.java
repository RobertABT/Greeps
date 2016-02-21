import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class 'MyGreep'. No other classes may be modified or created. 
 *
 * Rule 2 
 * You cannot extend the Greeps' memory. That is: you are not allowed to add 
 * fields (other than final fields) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method defined in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-defined classes, such as MyGreep). Greeps have no magic 
 * powers - they cannot create things out of nothing. 
 * 
 * Rule 6 
 * You are not allowed to call any methods (other than those listed in Rule 4)
 * of any other class in this scenario (including Actor and World). 
 *  
 * If you change the name of this class you should also change it in
 * Ship.createGreep().
 * 
 * Please do not publish your solution anywhere. We might want to run this
 * competition again, or it might be used by teachers to run in a class, and
 * that would be ruined if solutions were available.
 * 
 * 
 * @author (RobertABT)
 * @version 0.1
 */
public class MyGreep extends Greep
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    private static final int FOUND_TOMATO = 1;
    
    /**
     * Default constructor. Do not remove.
     */
    public MyGreep(Ship ship)
    {
        super(ship);
    }
    
    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        
        checkFood(); /*no point moving about if we arent actually looking for food 
        where we are
        */
        if (carryingTomato()) {
            if(atShip()) {
                dropTomato();
            }
            else {
                if (!getFlag(1)){
					turnHome();
					move();
            	}
            }
        }
        //a case for when we arrive at tomatoes
        else if (getTomatoes() != null) { //checks if there are tomatoes at the current location
            TomatoPile tomatoes = getTomatoes(); //checks if pile is here
            //lets use that block on the enemies
            if(!blockingPile(tomatoes)) {
                //no block, we'll make one by calling the boolean function
                turnTowards(getMemory(1), getMemory(2));
                move();
                setFlag(1,true); //set flag to say you are shield Greep
            }
        }//i figured the formatting
        
        else if (getMemory(0) == FOUND_TOMATO){
            //go get those delicious (fruits?)
            turnTowards(getMemory(1),getMemory(2));
            move();
        }
        //time to go on the offensive
        else if (numberOfOpponents(false) >= 3) {
            //makes tactical sense to take out opponents when outnumbered
            kablam();
        }
        
    
        else {
            randomWalk();
            //checkFood(); //no point repeating
        }
        /*the following code should wipe memory bit 0 
         * if there are no tomatoes at the point in memory
         * because otherwise greeps pile up at one location
         */
        TomatoPile tomatoes = getTomatoes();
        if (tomatoes == null && getX() == getMemory(1) && getY() == getMemory(2)){
            wipeMemory();
        }
        //Greeps are getting stuck a bit
        if (atWater()){
            int bearing = getRotation();
            setRotation (bearing + Greenfoot.getRandomNumber(180));
            move();
        }
    }
    
    /** 
     * Move forward, with a slight chance of turning randomly
     */
    public void randomWalk()
    {
        // there's a 3% chance that we randomly turn a little off course
        if (randomChance(3)) {
            turn((Greenfoot.getRandomNumber(3) - 1) * 100);
        }
        
        move();
    }

    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null) { // executes if pile is here
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
            //i think this keeps getting changed to other (x,y) coords, confusing greeps
            if (getMemory(0) == 0){
                setMemory(0, FOUND_TOMATO);
                setMemory(1, tomatoes.getX());
                setMemory(2, tomatoes.getY());
            }//by adding this if statement the greeps should only go to one pile
            //now to try and get the xy to change only after first pile is empty
            //else if (
        }
    }
    
    /**
     * block a pile if there is no friendly greep in range and no shield in place
     */
    private boolean blockingPile(TomatoPile tomatoes){
        boolean onPile = tomatoes != null && sqrtDistance(tomatoes.getX(), tomatoes.getY()) < 5;
        if(onPile && getFriend() == null ){
            // no friends here :(
            block();
            return true; //if block is up
        }
        else {
            return false; // if block isnt up
        }
    }
    
    private int sqrtDistance(int x, int y)
    {
        int cX = getX() - x; //c as change
        int cY = getY() - y;
        return (int) Math.sqrt(cX * cX + cY * cY);
    }
    
    private void wipeMemory(){
        setMemory(0,0); //wipes Found tomato bit
        //bytes at 1 and 2 will be reset when next pile is found
    }
    
    /**
     * This method specifies the name of the greeps (for display on the result board).
     * Try to keep the name short so that it displays nicely on the result board.
     */
    public String getName()
    {
        return "Group 1's Greeps";  // write your name here!
    }
}