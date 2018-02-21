package GameLogic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class implementation for the Logic Class.
 */
public class Logic {
    private final int ASCII_A_LOWER = 97;
    private final int ASCII_A_UPPER = 65;
    private final int ASCII_J_LOWER = 106;
    private final int ASCII_0 = 48;
    private final int ASCII_1 = 49;
    private final int ASCII_9 = 57;
    private final int SIDE_LENGTH = 10;

    private List<Tank> tankList;
    private Fortress fortress;
    private int[][] board;//-1 miss, 0 untouched, 1 hit
    private char[][] tankBoard;

    /**
     *  Constructor for the Logic Class. Constructed class will have an empty tankList and a field without tanks if randomization did not succeed.
     * @param tankNum Must not be null. Used to initialize how many tanks will be on the field.
     * @param maxTries Must not be null. Integer used to try this amount of times to initialize the tanks.
     */
    public Logic(int tankNum, int maxTries){
        assert tankNum > 0;//TODO: take this out later, not sure if we have to error check for this
        int fortressHealth = 1500;

        this.fortress = new Fortress(fortressHealth);
        this.board = new int[SIDE_LENGTH][SIDE_LENGTH];
        this.tankBoard = new char[SIDE_LENGTH][SIDE_LENGTH];
        this.tankList = new ArrayList<>();

        for(int i = 0; i < SIDE_LENGTH; i++){
            for(int j = 0; j < SIDE_LENGTH; j++){
                this.tankBoard[i][j] = '.';
            }
        }

        int currentTries = 0;
        while(!randomize(tankNum) && currentTries < maxTries){
            for(int i = 0; i < SIDE_LENGTH; i++){
                for(int j = 0; j < SIDE_LENGTH; j++){
                    this.tankBoard[i][j] = '.';
                }
            }
            tankList.clear();
            currentTries++;
        }
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
    private int[] getTanksDamage(){
        int[] tanksDamageArray = new int[tankList.size()];
        for(int i = 0; i < tankList.size(); i++){
            tanksDamageArray[i] = tankList.get(i).getPower();
        }
        return tanksDamageArray;
    }

    /**
     * Returns the damage of the active tanks on the field.
     * @return Integer list for the damage of all active tanks.
     */
    public List<Integer> getTanksDamageForUI(){
        List<Integer> tanksDamage = new ArrayList<>();
        for(Tank currentTank : tankList){
            int currentDamage = currentTank.getPower();

            if(currentDamage > 0){
                tanksDamage.add(currentDamage);
            }
        }

        return tanksDamage;
    }

    /**
     *  Returns where there is and isnt a tank in the board.
     * @return 2D Boolean Array showing where the tanks are.
     */
    public char[][] getTankBoardState(){
        return tankBoard;
    }

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
     */
    public void shootFortress(){
        int [] tanksDamage = getTanksDamage();
        for(int currentDamage : tanksDamage){
            this.fortress.decreaseHealth(currentDamage);
        }
    }

    /**
     *  Checks to see if it is possible to randomize the tanks. If it is possible, it goes ahead and updates the tankList and the tankBoard.
     * @param tankNum Must not be null. Integer showing how many tanks are supposed to be placed on the field.
     */
    private boolean randomize(int tankNum){
        int tetraminoSize = 4;
        Random rand = new Random();

        //checks if it is at all possible to put all tanks on the board
        if(tankNum > (int)((double)(SIDE_LENGTH*SIDE_LENGTH)/(double)(tetraminoSize))){//TODO: Error check
            return false;
        }

        //create all the tanks so long as there are no errors
        int tanksPlaced = 0;
        int asciiValue = ASCII_A_UPPER;
        while(tanksPlaced < tankNum){
            int[] rows = new int[tetraminoSize];
            int[] columns = new int[tetraminoSize];
            int tetraminosPlaced = 0;

            //check if there is at least one open spot in the board
            boolean canPlaceOne = false;
            for(int i = 0; i < SIDE_LENGTH; i++){
                for(int j = 0; j < SIDE_LENGTH; j++){
                    if(tankBoard[i][j] == '.'){
                        canPlaceOne = true;
                    }
                }
            }
            if(!canPlaceOne){
                return false;
            }

            //find a random coordinate that is currently free
            List<Point> pointList = new ArrayList<>();
            getAllFreeCoordinates(pointList);
            int pointListSize = pointList.size();
            int randomIndex = rand.nextInt(pointListSize);// 0 is min, pointListSize - 1 is max//todo: error check

            //update rows, columns, and the current tankboard
            rows[tetraminosPlaced] = pointList.get(randomIndex).y;
            columns[tetraminosPlaced] = pointList.get(randomIndex).x;
            tankBoard[rows[tetraminosPlaced]][columns[tetraminosPlaced]] = (char)(asciiValue); // todo: error check
            tetraminosPlaced++;

            while(tetraminosPlaced < tetraminoSize){
                if(!atLeastOneFreeAdjacent(tankBoard,rows,columns,tetraminosPlaced)){
                    return false;
                }

                //randomly choose a coordinate from all the free adjacent points of the current tetramino size
                List<Point> adjacentPointList = new ArrayList<>();
                getAllAdjacent(adjacentPointList, tankBoard, tetraminosPlaced, rows, columns);
                int adjacentListSize = adjacentPointList.size();
                randomIndex = rand.nextInt(adjacentListSize);//from 0 to adjacentListSize - 1//todo: error check

                //update rows, columns, tankboard, and how many have been occupied so far
                rows[tetraminosPlaced] = adjacentPointList.get(randomIndex).y;
                columns[tetraminosPlaced] = adjacentPointList.get(randomIndex).x;
                tankBoard[rows[tetraminosPlaced]][columns[tetraminosPlaced]] = (char)(asciiValue); // todo: error check
                tetraminosPlaced++;
            }

            //initialize a tank when the proper coordinates have been occupied, update how many tanks have been placed and update the letter
            tankList.add(new Tank(rows, columns));
            asciiValue++;
            tanksPlaced++;
        }

        return true;
    }

    /**
     * Checks if there is one free adjacent coordinate in the occupied spots so far.
     * @param tankBoard Must not be null. 2D character array showing the positions of all the tanks
     * @param rows Must not be null. Integer array that shows all the occupied row spots of the tank so far.
     * @param columns Must not be null. Integer array that shows all the occupied column spots of the tank so far.
     * @param tetraminosPlaced Must not be null. Integer that shows how many tetraminos have claimed a spot so far.
     * @return Boolean showing if there is at least one free adjacent coordinate in the occupied spots so far.
     */
    private boolean atLeastOneFreeAdjacent(char[][] tankBoard, int[] rows, int[] columns, int tetraminosPlaced){
        //boolean canPlaceAdjacent = false;
        for(int i = 0; i < tetraminosPlaced; i++){
            if(columns[i] == 0){
                if(rows[i] == 0 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], true, false, false, true)){
                    //upper left corner
                    return true;
                }
                else if(rows[i] == SIDE_LENGTH - 1 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, false, true, true)){
                    //lower left corner
                    return true;
                }
                else if(canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, false, false, true)){
                    //left side
                    return true;
                }
            }
            else if(columns[i] == SIDE_LENGTH - 1){
                if(rows[i] == 0 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], true, true, false, false)){
                    //upper right corner
                    return true;
                }
                else if(rows[i] == SIDE_LENGTH - 1 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, true, true, false)){
                    //lower right corner
                    return true;
                }
                else if(canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, true, false, false)){
                    //right side
                    return true;
                }
            }
            else if(rows[i] == 0 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], true, false, false, false)){
                //top side
                return true;
            }
            else if(rows[i] == SIDE_LENGTH - 1 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, false, true, false)){
                //bottom side
                return true;
            }
            else if(canPlaceOnAdjacent(tankBoard, rows[i], columns[i], true, true, true, true)){
                //somewhere in the middle
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes the point list to get all free adjacent spots of so far occupied spots.
     * @param adjacentPointList Must not be null. List of Points to later have all the free adjacent spots of so far occupied spots.
     * @param tankBoard Must not be null. 2D char array that contains the locations of all occupied spots.
     * @param tetraminosPlaced Must not be null. Integer that shows how many tetraminos have been placed so far.
     * @param rows Must not be null. Integer array that shows all the row coordinates of the occupied spots.
     * @param columns Must not be null. Integer array that shows all the column coordinates of the occupied spots.
     */
    private void getAllAdjacent(List<Point> adjacentPointList, char[][] tankBoard, int tetraminosPlaced, int[] rows, int[] columns){
        for(int i = 0; i < tetraminosPlaced; i++){
            if(columns[i] < SIDE_LENGTH - 1 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, false, false, true)){
                adjacentPointList.add(new Point(columns[i] + 1, rows[i]));
            }

            if(columns[i]  > 0 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, true, false, false)){
                adjacentPointList.add(new Point(columns[i] - 1, rows[i]));
            }

            if(rows[i]  < SIDE_LENGTH - 1 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], true, false, false, false)){
                adjacentPointList.add(new Point(columns[i], rows[i] + 1));
            }

            if(rows[i] > 0 && canPlaceOnAdjacent(tankBoard, rows[i], columns[i], false, false, true, false)){
                adjacentPointList.add(new Point(columns[i], rows[i] - 1));
            }
        }
    }

    /**
     * Initializes the point list to get all free spots on the board.
     * @param pointList Must not be null. List of Points to later have all the free spots on the board.
     */
    private void getAllFreeCoordinates(List<Point> pointList){
        for(int row = 0; row < SIDE_LENGTH; row++){
            for(int column = 0; column < SIDE_LENGTH; column++){
                if(tankBoard[row][column] == '.'){
                    pointList.add(new Point(column, row));
                }
            }
        }
    }

    /**
     * Checks if an adjacent spot is free
     * @param tankBoard Must not be null. 2D Character array to see where the tanks are in the field.
     * @param row Must not be null. Integer that contains the row to check around.
     * @param column Must not be null. Integer that contains the column to check around.
     * @param isTop Must not be null. Boolean that contains if the point to look around could be on top of the adjacent to check.
     * @param isRight Must not be null. Boolean that contains if the point to look around could be on the right of the adjacent to check.
     * @param isBottom Must not be null. Boolean that contains if the point to look around could be on the bottom of the adjacent to check.
     * @param isLeft Must not be null. Boolean that contains if the point to look around could be on the left of the adjacent to check.
     * @return Returns a boolean stating if at least one of the edges to check has a free spot.
     */
    private boolean canPlaceOnAdjacent(char[][] tankBoard, int row, int column, boolean isTop, boolean isRight, boolean isBottom, boolean isLeft){
        if(isTop && tankBoard[row + 1][column] == '.'){
            return true;
        }

        if(isRight && tankBoard[row][column - 1] == '.'){
            return true;
        }

        if(isBottom && tankBoard[row - 1][column] == '.'){
            return true;
        }

        if(isLeft && tankBoard[row][column + 1] == '.'){
            return true;
        }
        return false;
    }

    /**
     *  Checks if the coordinate given is valid.
     * @param coordinate Must not be null. Must not have any spaces. String that shows the coordinate of where to fire.
     * @return Boolean. Shows if the coordinate given was valid.
     */
    private boolean isValidCoordinate(String coordinate){
        assert !(coordinate.contains(" "));//TODO: take this out later, not sure if we have to handle this
        boolean isValidRow = false;
        char rowValue = Character.toLowerCase(coordinate.charAt(0));
        int asciiRowValue = (int)rowValue;

        if(asciiRowValue >= ASCII_A_LOWER && asciiRowValue <= ASCII_J_LOWER){
            isValidRow = true;
        }

        boolean isValidColumn = false;
        if(coordinate.length() > 1 && coordinate.length() < 4){
            if(coordinate.length() == 2){
                int asciiDigitValue = (int)(coordinate.charAt(1));
                if(asciiDigitValue >= ASCII_1 && asciiDigitValue <= ASCII_9){
                    isValidColumn = true;
                }
            }
            else{
                int asciiDigit1Value = (int)(coordinate.charAt(1));
                int asciiDigit2Value = (int)(coordinate.charAt(2));

                if(asciiDigit1Value == ASCII_1 && asciiDigit2Value == ASCII_0){
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
        int coordinateSize = 2;
        int[] intCoordinate = new int[coordinateSize];

        int rowValue = (int)(Character.toLowerCase(coordinate.charAt(0))) - ASCII_A_LOWER;
        int columnValue;
        if(coordinate.length() == coordinateSize){
            columnValue = (int)(coordinate.charAt(1)) - ASCII_1;
        }
        else{
            columnValue = SIDE_LENGTH - 1;
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
        board[row][column] = status;

        char charStatus = (!isHit) ? ' ' : Character.toLowerCase(tankBoard[row][column]);
        tankBoard[row][column] = charStatus;
    }
}
