import java.util.*;

/**
 * This class will create the player for the connect game and handle their stats
 * 
 * @author Christine Weld
 */
public class Player {
    
    /** the player's name */
    private String name;
    
    /** the player's piece */
    private char piece;
    
    /** player's win count */
    private int win;
    
    /** player's loss count */
    private int loss;
    
    /** player's draw count */
    private int draw;
    
    /**
     * The constructor class for the player
     * @param name will set the player's name
     * @param piece will set the player's piece character
     */
    public Player(String name, char piece){
        this.name = name;
        this.piece = piece;
        this.win = 0;
        this.loss = 0;
        this.draw = 0;
    }
    
    /**
     * this is a getter method for the player name
     * @return the player's name
     */
    public String getName(){
        return name;
    }
    
    /**
     * this is a getter method for the player piece
     * @return the player's piece
     */
    public char getPiece(){
        return piece;
    }
    
    /**
     * this is a getter method for the player wins
     * @return the player's win count
     */
    public int getWin(){
        return win;
    }
    
    /**
     * this is a getter method for the player losses
     * @return the player's loss count
     */
    public int getLoss(){
        return loss;
    }
    
    /**
     * this is a getter method for the player draws
     * @return the player's draw count
     */
    public int getDraw(){
        return draw;
    }
    
    /**
     * this is a setter method for the player wins
     */
    public void addWin(){
        this.win++;
    }
    
    /**
     * this is a setter method for the player losses
     */
    public void addLoss(){
        this.loss++;
    }
    
    /**
     * this is a setter method for the player losses
     */
    public void addDraw(){
        this.draw++;
    }
    
    /**
     * This method will print the player's name, and win, loss, and draw stats
     */
    public void printStats(){
        System.out.printf("%-12s ", this.name + ": ");
        System.out.printf("%4d%8d%8d\n", this.win, this.loss, this.draw);  
    }
}