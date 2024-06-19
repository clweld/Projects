import java.util.*;
import java.io.*;

/** 
 * ConnectGame is the controlling module for the Connect Game
 * CSC-116 (601) 18-Apr-2024
 *
 * @author Christine Weld
 */
public class ConnectGame {
    
   
    /** Minimum number of connected pieces to win game  */
    public static final int MIN_CONNECT_LENGTH = 4;
    
    /** Maximum number of connected pieces to win game */
    public static final int MAX_CONNECT_LENGTH = 6;
    
    /** Symbols displayed on board for players */
    public static final char[] SYMBOL = { 'X', 'O' };

    /** Player information to track players names and symbols for the board pieces    */
    private static Player[] player;
    
    /** Player that goes first, alternate for each game.  For the first game, the
     * player listed first on the command line will go first.   
     */
    private static int firstPlayer = 0;

    /** next player is toggled to point to the player for the current play    */
    private static int nextPlayer;
    
    /** number of connected cells to win for Connect Four, Connect Five, and Connect Six   */
    private static int gameVersion;

    /** number of rows and columns on the board  */
    private static int boardSize;
    
    /** The number of players can be one or more.  Two players are used for this project.
     * For a different number of players, change this value and add symbols to SYMBOL array.
     */
    private static int numPlayers = 2;
    
    /** True if playing another game, false if no more games */
    private static boolean keepPlaying = true;

    /**
     * This is the main method for playing the Connect Game
     *
     * @param args Command line arguments: number of connected cells to win, 
     *        followed by the names of the players.
     */
        
    public static void main ( String[] args ) 
    {        
        Scanner scnr = new Scanner(System.in);
        String game;
        int i;
        int num;
               
        // Check command line arguments
        getCLA ( args, scnr );
      
        //  Print welcome and player information and display board 
        game = "Connect " + ( ( gameVersion == MIN_CONNECT_LENGTH) ? "Four" 
            : (( gameVersion == (MIN_CONNECT_LENGTH + 1) ) ? "Five" : "Six") );
        System.out.println ("Welcome to " + game );
        System.out.println ( player[ 0 ].getName() + " plays first with symbol " 
            + player[ 0 ].getPiece());
        for ( i = 1; i < numPlayers; i++ ) {
            System.out.println ( player[ i ].getName() + " plays next with symbol " 
                + player[ i ].getPiece() );
        }
        System.out.println();
        
        // play games 
        int col;
        int connected;
        String ans;
        boolean valid;
        boolean gameOver;
        while ( keepPlaying ) {
            gameOver = false;
            nextPlayer = firstPlayer;
            // set up board
            Board board = new Board ( gameVersion ); 
            // display the empty board so player can see it before having to select column
            displayBoard( board, scnr );

            // rotate through players
            while ( !gameOver ) {
                // display user info and select column to play
                do {
                    col = inputColumn ( nextPlayer, scnr, board );
                    valid = board.dropPiece ( col, player[ nextPlayer ].getPiece() );
                    // message and repeat if column is full
                    if (! valid ) {
                        System.out.println(" Sorry, that column is full, please try again:");
                    }
                } while (! valid);
                
                // Display board
                displayBoard( board, scnr );
                
                // game over 
                if (board.getLongestConnect(player [ nextPlayer ].getPiece()) >= gameVersion) {
                    // print out message for winner and update stats
                    gameOver = true;
                    for (i = 0; i < numPlayers; i++ ) {
                        if ( i == nextPlayer ) {
                            System.out.println("\n" + player [ nextPlayer ].getName() + " wins!\n");
                            player [ i ].addWin();
                        } else {
                            player [ i ].addLoss();
                        }
                    }
                                        
                } else if ( board.isBoardFull() ) {
                    //print out message for draw, update stats
                    gameOver = true;
                    System.out.println("\nIt's a draw!\n");
                    for (i = 0; i < numPlayers; i++ ) {
                        player [ i ].addDraw();
                    }                   
                }     
               
                nextPlayer = ++nextPlayer % numPlayers;            

            }  // end gameOver while loop
                           
           // Display player stats
            String header = "Wins    Losses    Draws";
            System.out.printf("%37s\n", header);
            for (i = 0; i < numPlayers; i++ ) {
                player[ i ].printStats();
            }
            
            // prompt to continue game
            boolean ok = false;
            while ( !ok ) {
                System.out.print ( "\nWould you like to play again (y/n)? ");
                ans = scnr.next();
                if ( (ans.charAt( 0 ) == 'n') || (ans.charAt( 0 ) == 'N') ) {
                    keepPlaying = false;
                    ok = true;
                    System.out.println ("\n\nThank you for playing today.  Good bye!");
                } else {
                    if ( (ans.charAt( 0 ) == 'y') || (ans.charAt( 0 ) == 'Y') ) {
                        keepPlaying = true;
                        ok = true;
                    } else {
                        System.out.println ("Unrecognized response " + ans 
                            + ", please enter y or n.");
                    }
                }
            }

            // rotate starting player for next game
            firstPlayer = ++firstPlayer % numPlayers; 

        }  // end of keepPlaying loop

    }   // end of main
    
    /** Check command line arguments to ensure they are valid, read them in,
     * and set the appropriate ConnectGame instance variables.  All arguments are required.
     * For the two-player game:  
     * arg[ 0 ] is the game version 4, 5, or 6.  
     * arg[ 1 ] is name of the first player, can be any text string.  
     * arg[ 2 ] is name of the second player, can be any text string.  
     *
     * @param args Array of command line arguments
     * @param scnr Scanner for printing to console
     *
     */
    public static void getCLA ( String[] args, Scanner scnr ) {
        // check that all three arguments are present and the first one is an integer
        if (args.length < ( 1 + numPlayers ))  {
            System.out.println("Usage: java ConnectGame (4 - 6) player1 player2");
            System.exit(0);
        }
        Scanner cla = new Scanner(args[0]);
        if ( (!cla.hasNextInt()) ) {
            System.out.println("Usage: java ConnectGame (4 - 6) player1 player2");
            System.exit(0);
        }        
        
        // gameVersion is 4, 5, or 6 for Connect Four, Connect Five, or Connect Six
        gameVersion = cla.nextInt();
        cla.close();
        
        // check arg[ 0 ] value - must be integer 4, 5, or 6
        if ( (gameVersion < MIN_CONNECT_LENGTH ) || ( gameVersion > MAX_CONNECT_LENGTH) ) {
            System.out.println ("Game version must be " + MIN_CONNECT_LENGTH + " to "
                + MAX_CONNECT_LENGTH + " inclusive ");
            System.exit(0);
        }
        // set up players.       
        player = new Player[ numPlayers ];
        int i;
        for ( i = 0; i < numPlayers; i++ ) { 
            player [ i ] = new Player ( args[ i + 1 ], SYMBOL [ i ] );
        }

        boardSize = 2 * gameVersion;    
    }  // end of getCLA
    
    /**
     * Prompt player to enter column number to play and reprompt as necessary until a
     * a valid value is entered.  If the player "q" or "quit" (not case sensitive) then exit
     * the game
     * 
     * @param playerID the index for the current player[], must greater than 0 and 
     * less than gameVersion.
     * @param scnr Scanner used for console input.
     * @param board to pass board methods to this method
     * @return index of column selected by user.
     * @throws IllegalArgumentException if playerID is invalid.
     *
     */
    public static int inputColumn( int playerID , Scanner scnr, Board board ) {
        //  the playerID should never be invalid, but check just in case
        if ( (playerID < 0) || (playerID >= numPlayers) ) {
            throw new IllegalArgumentException("Invalid player index = " + playerID 
                + " must be 0 to " + (numPlayers - 1)  );
        }   
        
        int col = -1;
        
        // Print out name, game piece symbol, and max number of connected game
        //pieces the user has at each play
        System.out.println ("Player " + player[ playerID ].getName() + " (" 
            + player[ playerID ].getPiece() + ") has " 
                + board.getLongestConnect(player[ playerID ].getPiece())
                + " connected pieces.");
        while ( col < 0 ) {
            System.out.print ( "Enter the column to place your " + player[ playerID ].getPiece()
                + " piece (1 to " + boardSize + ") or q to quit: ");
            if ( scnr.hasNextInt() ) {
                col = scnr.nextInt();
                if ( (col < 1 ) || (col > boardSize) ) {
                    System.out.println ("Invalid column number, please try again.");
                    col = -1;
                }
            } else {  // quit if player enters 'q' or 'Q'
                String str = scnr.next();
                if (( str.charAt( 0 ) == 'q' ) || ( str.charAt( 0 ) == 'Q' ) ) {
                    System.out.println ("\n\nThank you for playing today.  Good bye!");
                    System.exit( 0 );
                } else {
                    System.out.println ("\nNot a number, please try again.");
                }
            }
        }
        
        return col - 1;
    }
    
    /** method to print out ConnectGame board to avoid duplicating code
     *
     * @param board Board class instance to be displayed
     * @param scnr Scanner used for output to consolew    
    */
    private static void displayBoard( Board board, Scanner scnr ) {
        // print column header and board grid
        System.out.println();
        for ( int i = 1; i <= boardSize; i++) {
            System.out.printf ( "%2d", i );
        } 
        System.out.println();
        System.out.println ( board.toString() );  
    }
    
}       // end of class