package GameLogic;

/**
 *
 */
public class Fortress {

    private int health;
    private boolean alive;

    /**
     * Constructor for the Fortress Class.
     * @param health Must not be null. Default health is 1500 if the input is invalid.
     */
    public Fortress(int health){
        int defaultHealth = 1500;

        if(health <= 0){
            this.health = defaultHealth;
        }
        else{
            this.health = health;
        }
        alive = true;
    }

    /**
     * Returns the health of the Fortress Class.
     * @return Int. Health of the Fortress Class.
     */
    public int getHealth(){
        return this.health;
    }

    /**
     * Returns whether or not the Fortress is still alive.
     * @return Boolean. Status of the Fortress class whether it is dead or alive.
     */
    public boolean isAlive(){
        return this.alive;
    }

    /**
     * Decreases the health of the Fortress Class by the amount given.
     * @param amount Must not be null. Decreases the health by this amount.
     */
    public void decreaseHealth(int amount){
        assert amount >= 0;//TODO: delete this on release.
        int dead = 0;
        this.health -= amount;

        if(this.health <= dead){
            this.health = dead;
            setDead();
        }
    }

    private void setDead(){
        this.alive = false;
    }
}
