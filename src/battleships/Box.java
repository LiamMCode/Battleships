package battleships;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Box extends Rectangle 
{
    public int x, y;
    public Ship ship = null;
    public boolean wasShot = false;
    public static Color colourOfHit = Color.RED;
    public static Color colourOfMiss = Color.WHITE;
    public static Color colourOfBackground = Color.AQUA;

    private Board board;

    public Box(int x, int y, Board board) //contains all the info about each square
    {
        super(30, 30);//changes the size of the squares
        this.x = x; 
        this.y = y; 
        this.board = board; 
        setFill(colourOfBackground);
        setStroke(Color.BLACK);
    }

    public boolean shoot() //boolean for if the ship has been shot 
    {
        wasShot = true;
        setFill(colourOfMiss);
        if (ship != null) 
        {
            ship.hit();
            setFill(colourOfHit);
            if (!ship.isAlive()) 
            {
                board.ships--;
            }
            return true;
        }
        return false;
    }
}