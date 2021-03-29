package battleships;

import javafx.scene.Parent;

public class Ship extends Parent 
{
    public int type;
    public boolean vertical = true;
    public int health;

    public Ship(int type, boolean vertical) //contains all info on each ship 
    {
        this.type = type;
        this.vertical = vertical;
        health = type;
    }
    public void hit() //takes off health when hit
    {
        health--;
    }
    public boolean isAlive() //boolean if ship is sunk
    {
        return health > 0;
    }
}