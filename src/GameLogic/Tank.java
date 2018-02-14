package GameLogic;

import java.util.List;

/**
 * Class implementation for enemy tanks.
 */
public class Tank {
    public static final int FOUR_CELL = 4;
    public static final int THREE_CELL = 3;
    public static final int TWO_CELL = 2;
    public static final int ONE_CELL = 1;
    public static final int ZERO_CELL = 0;

    private List<Tetramino> tetraminoList;
    private int power;
    private boolean isAlive;

    /**
     * Set up Tetraminos
     * @param rows
     * @param columns
     */
    public Tank(int[] rows, int[] columns) {
        this.isAlive = true;
        setTetraminos(rows, columns);
        setPower(getLiveTetraminoNum());
    }

    /**
     * Sets position values and creates all Tetraminos.
     * Requires rows and columns inputs to be correct.
     * @param rows
     * @param columns
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
     * @param row
     * @param column
     * @return
     */
    public boolean hit(int row, int column)
    {
        boolean hit = false;
        for(int i = 0; i < tetraminoList.size(); i++)
        {
            Tetramino tetramino = tetraminoList.get(i);
            if(tetramino.isHit(row, column));
            {
                int currentTetNum = getLiveTetraminoNum();
                setPower(currentTetNum);

                return true;
            }
        }

        return false;
    }

    /**
     * Returns live status of Tank
     * @return
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
     * @return
     */
    public int getPower()
    {
        return this.power;
    }

    /**
     * Sets the power of the tank.
     * @param tetraminoNum
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
     * @return
     */
    private int getLiveTetraminoNum()
    {
        int tetraminoCount = 0;
        for(int tetraIterator = 0; tetraIterator < tetraminoList.size(); tetraIterator++)
        {
            Tetramino tetramino = tetraminoList.get(tetraIterator);
            if(tetramino.isAlive())
            {
                tetraminoCount++;
            }
        }
        return tetraminoCount;
    }
}
