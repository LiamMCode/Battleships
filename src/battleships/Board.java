package battleships;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Board extends Parent 
{
    private VBox rows = new VBox();
    private boolean enemy = false;
    public int ships = 8;
    public static Color colourOfShips = Color.GREEN;

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) //click event handler   
    {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) //creates the boards 
        {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) 
            {
                Box s = new Box(x, y, this);
                s.setOnMouseClicked(handler);
                row.getChildren().add(s);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    public boolean ShipPos(Ship ship, int x, int y) //places the ship on the board 
    {
        if (validShipPlace(ship, x, y)) 
        {
            int length = ship.type;
            if (ship.vertical) 
            {
                for (int i = y; i < y + length; i++) 
                {
                    Box Box = getBox(x, i);
                    Box.ship = ship;
                    if (!enemy)
                    {
                        Box.setFill(colourOfShips);
                    }
                }
            }
            else 
            {
                for (int i = x; i < x + length; i++) 
                {
                    Box Box = getBox(i, y);
                    Box.ship = ship;
                    if (!enemy) 
                    {
                        Box.setFill(colourOfShips);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Box getBox(int x, int y) //gets the 'Co-ordinates of the Box' 
    {
        return (Box)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public boolean validShipPlace(Ship ship, int x, int y) //checks if the ship can be placed
    {
        int length = ship.type;
        if (ship.vertical) //if ship is vertical
        {
            for (int i = y; i < y + length; i++) 
            {
                if (!valid(x, i))
                {
                    return false;
                }
                Box Box = getBox(x, i);
                if (Box.ship != null)
                {
                    return false;
                }
            }
        }
        else //if ship is horizontal
        {
            for (int i = x; i < x + length; i++) 
            {
                if (!valid(i, y))
                {
                    return false;
                }
                Box Box = getBox(i, y);
                if (Box.ship != null)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean valid(double x, double y) //sets the valid size of x and y
    {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
}