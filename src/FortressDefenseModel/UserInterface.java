package FortressDefenseModel;

import java.util.List;
import java.util.Scanner;

/**
 * Class implementation of the User Interface.
 */
public class UserInterface {
    private static Scanner in = new Scanner(System.in);
    private final int BOARD_LENGTH = 10;

    /**
     *  Gets the input that the user just made.
     * @return String which contains the coordinate of the next shot.
     */
    public String getUserInput(){
        return in.nextLine();
    }

    /**
     *  Prints if the player has won or lost.
     * @param isVictory Must not be null. Boolean showing if the player has won or not.
     */
    public void printEndGameResult(boolean isVictory){
        if(isVictory){
            System.out.println("Congratulations! You won!\n");
        }
        else{
            System.out.println("I'm sorry, your fortress has been smashed!\n");
        }
    }

    /**
     * Prints the status of the fortress.
     * @param fortressHealth Must not be null. Integer that contains the health of the fortress.
     */
    private void printFortressHealth(int fortressHealth){
        String fortressStatusString = "Fortress Structure Left: " + fortressHealth + ".";
        System.out.println(fortressStatusString);
    }

    /**
     * Prints the current game state and fortress health with the fog.
     * @param stateBoard Must not be null. 2D Integer array that contains the integer state of the board.
     * @param fortressHealth Must not be null. Integer for the health of the fortress.
     */
    public void printGameState(int[][] stateBoard, int fortressHealth){
        //this also shows Gameboard:
        System.out.println("Game Board:");
        System.out.println("       1  2  3  4  5  6  7  8  9  10");
        for(int i = 0; i < BOARD_LENGTH; i++){
            StringBuilder boardString = new StringBuilder();
            boardString.append("    ");

            switch(i){
                case 0: boardString.append("A");
                    break;
                case 1: boardString.append("B");
                    break;
                case 2: boardString.append("C");
                    break;
                case 3: boardString.append("D");
                    break;
                case 4: boardString.append("E");
                    break;
                case 5: boardString.append("F");
                    break;
                case 6: boardString.append("G");
                    break;
                case 7: boardString.append("H");
                    break;
                case 8: boardString.append("I");
                    break;
                case 9: boardString.append("J");
                    break;
                default:
                    assert false;
            }
            for(int j = 0; j < BOARD_LENGTH; j++){
                switch (stateBoard[i][j]){
                    case -1: boardString.append(" ");
                        break;
                    case 0: boardString.append("~");
                        break;
                    case 1: boardString.append("X");
                    default:
                        assert false;
                }
            }
            System.out.println(boardString);
        }
        printFortressHealth(fortressHealth);
    }

    /**
     * Prints the intro to the game.
     * @param tankNum Must not be null. Integer for the amount of tanks on the field.
     * @param isCheat Must not be null. Boolean for the cheating option.
     * @param tankBoard Must not be null. 2D character array for the revealed state of the board.
     * @param fortressHealth Must not be null. Integer for the health of the fortress.
     */
    public void printIntro(int tankNum, boolean isCheat, char[][] tankBoard, int fortressHealth){
        if(isCheat){
            printRevealed(tankBoard, fortressHealth);
        }

        System.out.println("");
        System.out.println("");
        System.out.println("Starting game with " + tankNum + " tanks.");
        System.out.println("----------------------------");
        System.out.println("Welcome to Fortress Defense!");
        System.out.println("by Steven Lee and Miguel Taningco");
        System.out.println("----------------------------\n\n");
        System.out.println("");
        System.out.println("");
    }

    /**
     *  Prints the prompt to enter your move.
     */
    public void printMovePrompt(){
        System.out.print("Enter your move: ");
    }

    /**
     *  Prints the result after shooting into the field.
     * @param result Must not be null. Integer containing the number corresponding to the result of shooting.
     * @param damageArray Must not be null. Integer array containing the damage of all active tanks.
     */
    public void printMoveResult(int result, List<Integer> damageArray){
        if(result == -1){
            System.out.println("Invalid target. Please enter a coordinate such as D10.");
        }
        else if(result == 0){
            System.out.println("Miss.");
        }
        else{
            System.out.println("HIT!");
        }

        for(int i = 1; i <= damageArray.size(); i++){
            System.out.println("Alive tank #" + i + " of " + damageArray.size() + " shot you for " + damageArray.get(i) + "!");
        }
        System.out.println("");
    }

    /**
     * Prints the state of the board without the fog as well as the health of the fortress.
     * @param tankBoard Must not be null. 2D character array containing the state of the board.
     * @param fortressHealth Must not be null. Integer that contains the health of the fortress.
     */
    public void printRevealed(char[][] tankBoard, int fortressHealth){
        //this also shows Gameboard:

        System.out.println("Game Board:");
        System.out.println("       1  2  3  4  5  6  7  8  9  10");
        for(int i = 0; i < BOARD_LENGTH; i++){
            StringBuilder boardString = new StringBuilder();
            boardString.append("    ");

            switch(i){
                case 0: boardString.append("A");
                    break;
                case 1: boardString.append("B");
                    break;
                case 2: boardString.append("C");
                    break;
                case 3: boardString.append("D");
                    break;
                case 4: boardString.append("E");
                    break;
                case 5: boardString.append("F");
                    break;
                case 6: boardString.append("G");
                    break;
                case 7: boardString.append("H");
                    break;
                case 8: boardString.append("I");
                    break;
                case 9: boardString.append("J");
                    break;
                default:
                    assert false;
            }
            for(int j = 0; j < BOARD_LENGTH; j++){
                boardString.append("  ");
                boardString.append(tankBoard[i][j]);
            }
            System.out.println(boardString);
        }
        printFortressHealth(fortressHealth);
        System.out.println("(Lower case tank letters are where you shot.)");
    }
}