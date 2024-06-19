import java.util.*;

/**
 * This class will create the connect board and handle piece placement within the board
 * 
 * @author Christine Weld
 */
public class Board {
    
    /** Smallest Connect Size */
    public static final int MIN_CONNECT_SIZE = 4;
    
    /** Largest Connect Size */
    public static final int MAX_CONNECT_SIZE = 6;
    
    /** current board setup */
    private char[][] board;
    
    /** determine the connect and board size */
    private int connectSize;
    
    /** max pieces allowed to be played */
    private int totalBoardSize;
    
    /** the column that a piece will be dropped into */
    private int column;
    
    /** array of the next location in column to drop piece */
    private int[] nextSpot;
    
    /** return the longest connect for a player */
    private int longestConnect;
    
    /** counter for total pieces played */
    private int totalPieceCount;    
    
    /** 
     * Constructor class for the board
     * @param connectSize is used to determine how large to make the board
     * @throws IllegalArgumentException with the message "Invalid connect size." if the connect 
     *         parameter is smaller than MIN_CONNECT_SIZE or larger than MAX_CONNECT_SIZE
     */
    public Board(int connectSize){
        
        if(connectSize < MIN_CONNECT_SIZE || connectSize > MAX_CONNECT_SIZE){
            throw new IllegalArgumentException("Invalid connect size");
        }
        
        this.connectSize = connectSize;
        board = new char[connectSize * 2][connectSize * 2];
        for(int i = 0; i < connectSize * 2; ++i){
            for(int j = 0; j < connectSize * 2; ++j){
                board[i][j] = '_';
            }
        }
        totalBoardSize = (connectSize * 2) * (connectSize * 2);
        
        nextSpot = new int[connectSize * 2];
        
        for (int i = 0; i < nextSpot.length; i++){
            nextSpot[i] = nextSpot.length - 1;
        }
        
        totalPieceCount = 0;
    }
    
    /**
     * method to add a new piece to the board
     * @param column where the piece will be placed
     * @param piece is the piece type that will be placed in the specified column
     * @return a boolean on if the piece could be dropped or not
     */
    public boolean dropPiece(int column, char piece){        
        boolean validColumn = false;
        
        if(column < 0 || column > nextSpot.length - 1){
            return validColumn;
        }
        
        int row = nextSpot[column];
        
        if(row < 0){
            return validColumn;
        } else {
            board [row][column] = piece;
            nextSpot[column] -= 1;
            totalPieceCount += 1;
            validColumn = true;
        }
        
        return validColumn;
    }
    
    /** 
     * This method will go through the board to determine a player's longest connect length
     * @param piece char to match the player's dropped pieces on the board
     * @return the longest connect length on the board for the player
     */
    public int getLongestConnect(char piece){
        //initialize each directional variable
        int longestHorizontal = 0;
        int longestVertical = 0;
        int longestForwardSlash = 0;
        int longestBackSlash = 0;
        int internalIncrement = 0;
        longestConnect = 0;
        
        //Loop through the board in all horizontals
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if(board[i][j] == piece){
                    internalIncrement++;
                    if(internalIncrement >= longestHorizontal){
                        longestHorizontal = internalIncrement;
                    }
                } else {
                    internalIncrement = 0;
                }
            }
        }
        
        internalIncrement = 0;
        
        //Loop through the board in all verticals
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if(board[j][i] == piece){
                    internalIncrement++;
                    if(internalIncrement >= longestVertical){
                        longestVertical = internalIncrement;
                    }
                } else {
                    internalIncrement = 0;
                }
                
            }
        }
        
        internalIncrement = 0;
        
        //Loop through in a forward slash
        //This requires two looping sections since the board will be split in half
        //This is for the bottom half of the board
        for (int t = 0; t < board.length; t++){
            for (int i = board.length - 1 ; i >= t; i--){
                for (int j = board.length - i - 1; j < board.length - i; j++){
                    if(board[i][j + t] == piece){
                        internalIncrement++;
                        if(internalIncrement >= longestForwardSlash){
                            longestForwardSlash = internalIncrement;
                        }
                    } else {
                        internalIncrement = 0;
                    }
                }
            }
        }
        
        internalIncrement = 0;
        
        //This is the forward slash search for the top of the board
        for (int t = 0; t < board.length; t++){
            for (int i = board.length - 1; i >= t; i--){
                for (int j = board.length - i - 1; j < board.length - i; j++){
                    if(board[i - t][j] == piece){
                        internalIncrement++;
                        if(internalIncrement >= longestForwardSlash){
                            longestForwardSlash = internalIncrement;
                        }
                    } else {
                        internalIncrement = 0;
                    }
                }
            }
        }
        
        internalIncrement = 0;
        
        //Loop through in a backslash
        //This requires two sets of loops since the board will be split in half
        //This is for the bottom half of the board
        for (int t = 0; t < board.length; t++){
            for (int i = board.length - 1; i >= t; i--){
                for (int j = i; j <= i; j++){
                    if(board[i][j - t] == piece){
                        internalIncrement++;
                        if(internalIncrement >= longestBackSlash){
                            longestBackSlash = internalIncrement;
                        }
                    } else {
                        internalIncrement = 0;
                    }
                }
            }
        }
        
        internalIncrement = 0;
       
        //This is for the top half of the board
        for (int t = 0; t < board.length; t++){
            for (int i = board.length - 1; i >= t; i--){
                for (int j = i; j <= i; j++){
                    if(board[j - t][i] == piece){
                        internalIncrement++;
                        if(internalIncrement >= longestBackSlash){
                            longestBackSlash = internalIncrement;
                        }
                    } else {
                        internalIncrement = 0;
                    }
                }
            }
        }
        
        if(longestHorizontal > longestConnect){
            longestConnect = longestHorizontal;
        }
        if(longestVertical > longestConnect){
            longestConnect = longestVertical;
        }
        if(longestForwardSlash > longestConnect){
            longestConnect = longestForwardSlash;
        }
        if(longestBackSlash > longestConnect){
            longestConnect = longestBackSlash;
        }
        
        return longestConnect;
        
    }
    
    /** 
     * getter method for the total pieces played
     * @return the total pieces played
     */
    public int getTotalPieceCount(){
        return totalPieceCount;
    }
    
    /**
     * method to determine if the board is full or not
     * @return if the board is full or not
     */
    public boolean isBoardFull(){
        if (totalPieceCount < totalBoardSize){
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * this method will return a string representation of the board
     * @return a string representation of the board
     */
    public String toString(){
        String boardString = "";
        connectSize = this.connectSize;
        for(int i = 0; i < connectSize * 2; ++i){
            boardString += "|";
            for(int j = 0; j < connectSize * 2; ++j){
                boardString += board[i][j] + "|";
            }
            boardString += "\n";
        }
        return boardString;
    }
    
}