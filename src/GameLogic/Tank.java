package GameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implementation for enemy Tank.
 */
public class Tank {
    private static final int FOUR_CELL = 4;
    private static final int THREE_CELL = 3;
    private static final int TWO_CELL = 2;
    private static final int ONE_CELL = 1;
    private static final int ZERO_CELL = 0;

    private List<Block> blockList;
    private int power;
    private boolean isAlive;

    /**
     * Set up all Block for Tank
     * @param rows Must not be null. Array of row coordinates for Block.
     * @param columns Must not be null. Array of column coordinates for Block.
     */
    public Tank(int[] rows, int[] columns) {
        this.isAlive = true;
        blockList = new ArrayList<>();
        setBlock(rows, columns);
        setPower(getLiveBlockNum());
    }

    /**
     * Sets position values and creates all Block.
     * Requires rows and columns inputs to be correct.
     * @param rows  Must not be null. Contains array for each row placement of each Block
     * @param columns Must not be null. Contains array for each column placement of each Block
     */
    private void setBlock(int[] rows, int[] columns)
    {
        for(int i = 0; i < rows.length; i++)
        {
            Block block = new Block(rows[i], columns[i]);
            blockList.add(block);
        }
    }

    /**
     * Returns true if Block is hit.
     * Set off algorithm to kill Block.
     * @param row Must not be null. Coordinate for row of Block
     * @param column Must not be null. Coordinate for column of Block
     * @return Boolean containing whether hit was successful or not.
     */
    public boolean hit(int row, int column)
    {
        for(Block block : blockList)
        {
            if(block.isHit(row, column))
            {
                int currentBlockNum = getLiveBlockNum();
                setPower(currentBlockNum);

                return true;
            }
        }
        return false;
    }

    /**
     * Returns live status of Tank
     * @return Boolean containing status of Tank.
     */
    public boolean isAlive()
    {
        return this.isAlive;
    }

    /**
     * Sets Tank status to dead.
     */
    private void setDead()
    {
        if(getLiveBlockNum() == ZERO_CELL)
        {
            this.isAlive = false;
        }
    }

    /**
     * Returns power level of the Tank.
     * @return Int containing value of power of Tank.
     */
    public int getPower()
    {
        return this.power;
    }

    /**
     * Sets the power of the tank.
     * @param blockNum Number of Block that have not been hit.
     */
    private void setPower(int blockNum)
    {
        if(blockNum == FOUR_CELL)
            this.power = 20;
        else if(blockNum == THREE_CELL)
            this.power = 5;
        else if(blockNum == TWO_CELL)
            this.power = 2;
        else if(blockNum == ONE_CELL)
            this.power = 1;
        else if(blockNum == ZERO_CELL) {
            this.power = 0;
            setDead();
        }
    }

    /**
     * Parameter used to calculate the power of the Tank.
     * Iterate through list of Block, and count how many are still alive.
     * @return Int containing number of live Block
     */
    private int getLiveBlockNum()
    {
        int blockCount = 0;
        for(Block block : blockList)
        {
            if(block.isAlive())
            {
                blockCount++;
            }
        }
        return blockCount;
    }
}
