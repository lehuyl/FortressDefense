package GameLogic;

/**
 * Class for the Tetramino block structure that makes up a Tank.
 */
public class Tetramino {
    private boolean isAlive;
    private int row;
    private int column;

    public Tetramino(int row, int column) {
        this.isAlive = true;
        this.row = row;
        this.column = column;
    }

    /**
     * Alogrithm used to register a hit to Tetramino.
     * @param row Must not be null. Coordinate for row
     * @param col Must not be null. Coordinate for column
     * @return Boolean containing whether or not this tetramino has been hit.
     */
    public boolean isHit(int row, int col)
    {
        boolean isHit = false;
        if(this.row == row && this.column == col)
        {
            isHit = true;
            setDead();
        }

        return isHit;
    }

    /**
     * Returns live status of Tetramino.
     * @return  Boolean containing live status of Tetramino.
     */
    public boolean isAlive()
    {
        return this.isAlive;
    }

    /**
     * Set Tetramino to dead.
     * Call when hit returns true
     */
    private void setDead() {
        this.isAlive = false;
    }
}
