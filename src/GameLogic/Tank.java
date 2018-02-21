package GameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implementation for enemy tanks.
 */
public class Tank {
    private static final int FOUR_CELL = 4;
    private static final int THREE_CELL = 3;
    private static final int TWO_CELL = 2;
    private static final int ONE_CELL = 1;
    private static final int ZERO_CELL = 0;

    private List<Tetramino> tetraminoList;
    private int power;
    private boolean isAlive;

    /**
     * Set up Tetraminos
     * @param rows Must not be null. Array of row coordinates for Tetramino blocks.
     * @param columns Must not be null. Array of column coordinates for Tetramino blocks.
     */
    public Tank(int[] rows, int[] columns) {
        this.isAlive = true;
        tetraminoList = new ArrayList<>();
        setTetraminos(rows, columns);
        setPower(getLiveTetraminoNum());
    }

    /**
     * Sets position values and creates all Tetraminos.
     * Requires rows and columns inputs to be correct.
     * @param rows  Must not be null. Contains array for each row placement of each Tetramino
     * @param columns Must not be null. Contains array for each column placement of each Tetramino
     */
    private void setTetraminos(int[] rows, int[] columns)
    {
        for(int i = 0; i < rows.length; i++)
        {
            Tetramino tetramino = new Tetramino(rows[i], columns[i]);
            tetraminoList.add(tetramino);
        }
    }

    /**
     * Returns true if Tetraminio is hit.
     * Set off algorithm to kill Tetramino.
     * @param row Must not be null. Coordinate for row of Tetramino
     * @param column Must not be null. Coordinate for column of Tetramino
     * @return Boolean containing whether hit was successful or not.
     */
    public boolean hit(int row, int column)
    {
        boolean hit = false;
        for(Tetramino tetramino : tetraminoList)
        {
            if(tetramino.isHit(row, column))
            {
                int currentTetNum = getLiveTetraminoNum();
                setPower(currentTetNum);
                hit = true;
                return hit;
            }
        }

        return hit;
    }

    /**
     * Returns live status of Tank
     * @return Boolean containing status of tank.
     */
    public boolean isAlive()
    {
        return this.isAlive;
    }

    /**
     * Sets tank status to dead.
     */
    private void setDead()
    {
        if(getLiveTetraminoNum() == ZERO_CELL)
        {
            this.isAlive = false;
        }
    }

    /**
     * Returns power level of the tank.
     * @return Int containing value of power of tank.
     */
    public int getPower()
    {
        return this.power;
    }

    /**
     * Sets the power of the tank.
     * @param tetraminoNum Number of tetramino blocks that have not been hit.
     */
    private void setPower(int tetraminoNum)
    {
        if(tetraminoNum == FOUR_CELL)
            this.power = 20;
        else if(tetraminoNum == THREE_CELL)
            this.power = 5;
        else if(tetraminoNum == TWO_CELL)
            this.power = 2;
        else if(tetraminoNum == ONE_CELL)
            this.power = 1;
        else if(tetraminoNum == ZERO_CELL) {
            this.power = 0;
            setDead();
        }
    }

    /**
     * Parameter used to calculate the power of the Tank.
     * Iterate through list of Tetraminos, and count how many are still alive.
     * @return Int containing number of live Tetraminos
     */
    private int getLiveTetraminoNum()
    {
        int tetraminoCount = 0;
        for(Tetramino tetramino: tetraminoList)
        {
            if(tetramino.isAlive())
            {
                tetraminoCount++;
            }
        }
        return tetraminoCount;
    }
}
