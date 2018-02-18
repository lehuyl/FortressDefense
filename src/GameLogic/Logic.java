package GameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implementation for the Logic Class.
 */
public class Logic {
    private List<Tank> tankList;
    private Fortress fortress;
    private int[][] board;//-1 miss, 0 untouched, 1 hit
    //private boolean[][] tankBoard; // not sure if we need this to be honest

    /**
     *  Constructor for the Logic Class.
     * @param tankNum Must not be null. Used to initialize how many tanks will be on the field.
     */
    public Logic(int tankNum){
        assert tankNum > 0;//TODO: take this out later
        int fortressHealth = 1500;
        int sideLength = 10;

        this.fortress = new Fortress(fortressHealth);
        this.board = new int[sideLength][sideLength];
        //this.tankBoard = new boolean[sideLength][sideLength];
        this.tankList = new ArrayList<>();

        randomize(tankNum);
        //i want to initialize the tanklist
        //i want to initialize the board
    }

    /**
     *  Finds out if the game has ended.
     * @return Boolean stating whether or not the game is over.
     */
    public boolean isGameOver(){
        boolean liveTanks = false;
        for(Tank currentTank : tankList){
            if(currentTank.isAlive()){
                liveTanks = true;
            }
        }
        return !(liveTanks && fortress.isAlive());
    }

    /**
     *  Finds out if the user has won the game.
     * @return Boolean stating whether or not the user has won the game.
     */
    public boolean isVictory(){
        return isGameOver() && getFortressHealth() > 0;
    }

    /**
     *  Returns the current board state showing hits, miss, and untouched.
     * @return 2D integer array showing the current state of the board.
     */
    public int[][] getBoardState(){
        return board;
    }

    /**
     *  Returns the health of the fortress.
     * @return Integer showing the health of the fortress.
     */
    public int getFortressHealth(){
        return this.fortress.getHealth();
    }

    /**
     *  Returns the damage each tank can do.
     * @return Integer array showing the damage of each tank.
     */
    public int[] getTanksDamage(){
        int[] tanksDamageArray = new int[tankList.size()];
        for(int i = 0; i < tankList.size(); i++){
            tanksDamageArray[i] = tankList.get(i).getPower();
        }
        return tanksDamageArray;
    }

//    /**
//     *  Returns where there is and isnt a tank in the board.
//     * @return 2D Boolean Array showing where the tanks are.
//     */
//    public boolean[][] getTankBoardState(){
//        return tankBoard;
//    }

    /**
     *  Returns the list of Tanks found on the board.
     * @return Tank List with the information of all the tanks.
     */
    public List<Tank> getTankList(){
        return tankList;
    }

    /**
     *  Shoots the coordinate and tells if something was hit or not, or if the coordinate was invalid.
     * @param coordinate Must not be null. String that the user has input as the coordinate.
     * @return Integer. -1 if invalid coordinate, 0 if miss, and 1 if hit.
     */
    public int shootCoordinate(String coordinate){
        if(!isValidCoordinate(coordinate)){
            return -1;
        }

        int[] numericalCoordinate = getActualCoordinate(coordinate);
        int row = numericalCoordinate[0];
        int column = numericalCoordinate[1];

        for(Tank currentTank : tankList){
            if(currentTank.hit(row, column)){
                updateBoard(row, column, true);
                return 1;
            }
        }

        updateBoard(row, column, false);
        return 0;
    }

    /**
     * Shoots the Fortress with the damage each tank is capable of.
     * @param tanksDamage Must not be null. Integer array with the damage each tank is capable of.
     */
    public void shootFortress(int[] tanksDamage){
        for(int currentDamage : tanksDamage){
            this.fortress.decreaseHealth(currentDamage);
        }
    }

    /**
     *
     * @param tankNum
     */
    private void randomize(int tankNum){

    }

    /**
     *  Checks if the coordinate given is valid.
     * @param coordinate Must not be null. Must not have any spaces. String that shows the coordinate of where to fire.
     * @return Boolean. Shows if the coordinate given was valid.
     */
    private boolean isValidCoordinate(String coordinate){
        assert !(coordinate.contains(" "));//TODO: take this out later, not sure if we have to handle this
        int asciiAlower = 97;
        int asciiJlower = 106;
        int ascii0 = 48;
        int ascii1 = 49;
        int ascii9 = 57;
        boolean isValidRow = false;
        char rowValue = Character.toLowerCase(coordinate.charAt(0));
        int asciiRowValue = (int)rowValue;

        if(asciiRowValue >= asciiAlower && asciiRowValue <= asciiJlower){
            isValidRow = true;
        }

        boolean isValidColumn = false;
        if(coordinate.length() > 1 && coordinate.length() < 4){
            if(coordinate.length() == 2){
                int asciiDigitValue = (int)(coordinate.charAt(1));
                if(asciiDigitValue >= ascii1 && asciiDigitValue <= ascii9){
                    isValidColumn = true;
                }
            }
            else{
                int asciiDigit1Value = (int)(coordinate.charAt(1));
                int asciiDigit2Value = (int)(coordinate.charAt(2));

                if(asciiDigit1Value == ascii1 && asciiDigit2Value == ascii0){
                    isValidColumn = true;
                }
            }
        }

        return isValidRow && isValidColumn;
    }

    /**
     * Returns the actual coordinate of the shot given as (row, column) aka. (vertical, horizontal).
     * @param coordinate Must not be null. String that contains a valid coordinate.
     * @return Integer Array of 2 elements containing values 0 to 9;
     */
    private int[] getActualCoordinate(String coordinate){
        //TODO: this assumes that the coordinate being passed is already valid
        int[] intCoordinate = new int[2];
        int asciiAlower = 97;
        int ascii1 = 49;

        int rowValue = (int)(Character.toLowerCase(coordinate.charAt(0))) - asciiAlower;
        int columnValue;
        if(coordinate.length() == 2){
            columnValue = (int)(coordinate.charAt(1)) - ascii1;
        }
        else{
            columnValue = 9;
        }

        intCoordinate[0] = rowValue;
        intCoordinate[1] = columnValue;
        return intCoordinate;
    }

    /**
     * Updates the board state.
     * @param row Must not be null. Integer that has the row (vertical) location of the shot.
     * @param column Must not be null. Integer that has the column (horizontal) location of the shot.
     * @param isHit Must not be null. Boolean that shows if there was something hit in the location.
     */
    private void updateBoard(int row, int column, boolean isHit){
        int status = isHit ? 1 : -1; // check if this is actually correct
        this.board[row][column] = status;
    }
}
