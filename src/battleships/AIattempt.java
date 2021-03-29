package battleships;


import java.util.ArrayList;
import java.util.Random;
import battleships.Box;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AIattempt extends Application 
{
    private boolean running = false;
    private Board enemy, player;
    private int [] shipsToPlace = {5,4,4,3,3,2,2,2}; //array for ships
    private boolean enemyTurn = false;
    private Random random = new Random();
    private int shipsPlaced = 7;
    
    private Parent gameWindow() 
    {
        BorderPane side = new BorderPane(); //information about the game i.e. Controls & other Useful Info
        side.setPrefSize(600, 800);
        side.setRight(new Text("CONTROLS" + System.lineSeparator() + "Right Click = Horizontal Ship" + System.lineSeparator() 
        	+ "Left Click = Vertical Ship" + System.lineSeparator() + "Top Grid = Enemy" + System.lineSeparator() 
        	+ "Bottom Grid = Players" + System.lineSeparator() + "Hit an enemy ship = extra shot"));
        
        enemy = new Board(true, event -> //event handler for the enemy Board
        {
            if (!running)
            {
                return;
            }
            Box Box = (Box) event.getSource();
            if (Box.wasShot)
            {
                return;
            }
            enemyTurn = !Box.shoot();
            if (enemy.ships == 0) //if enemy has no ships left
            {
            	campione();
            }
            if (enemyTurn)
            {
                enemyMove();
            }
        });

        player = new Board(false, event -> //event handler for the player board
        {
            if (running)
            {
                return;
            }
            Box Box = (Box) event.getSource();
            
            if (player.ShipPos(new Ship(shipsToPlace[shipsPlaced], event.getButton() == MouseButton.PRIMARY), Box.x, Box.y)) //places ship on click onto the player board 
            {
	            if (--shipsPlaced == -1) //once all ships are placed call startGame method to begin the game
	            {
	            	startGame();
	            }
            }
        });
	    VBox vbox = new VBox(50, enemy, player); //adds the boards to a vbox and adds it to the borderPane onto the window
	    vbox.setAlignment(Pos.CENTER);
	    side.setCenter(vbox);
	    return side;
    }

    private void enemyMove() //controls the enemies shots
    {
        Random randDir = new Random();
        int trackShot = 0; //for incrementing when ship is hit to track along the ship till sunk
        
        ArrayList <Integer> dirOfNextShot = new ArrayList<Integer>(4); //4 directions (Up,Down,Left,Right)
        for (int i=1; i<=4; i++) //adds numbers representing each direction to the arraylist
        {
        	dirOfNextShot.add(i);
        }
        int x = random.nextInt(10); //picks random Box for first shot 
        int y = random.nextInt(10);	
        Box Box = player.getBox(x, y);
        enemyTurn = Box.shoot();
        
        if(Box.wasShot)
        {
        	int shipHitX = x; //Setting the hit coordinates to new variables
        	int shipHitY = y;
        	int dir = randDir.nextInt(dirOfNextShot.size()); //picking a random direction to shoot for next shot

        	while (enemyTurn) 
        	{
        		trackShot++;
        		while(Board.valid(shipHitX, shipHitY)) //check if next shot is valid
        		{
        			if (dir == 1) //shooting the Box above the hit Box
        			{
        				//select the next shot being up from previous
        				if (shipHitY + trackShot >= 10)
        				{
        					dir = 3;
        					continue;
        				}
        				else
        				{
        					Box Shot = player.getBox(shipHitX, (shipHitY + trackShot)); //using trackShot to hit the Box above        				
	        				if (Shot.wasShot)
	        				{
	        					continue;
	        				}
	        				else
	        				{
	        					enemyTurn = false;
	        					dirOfNextShot.remove(dir);
	        					break;
	        				}	
        				}
		
        			}
        			if (dir == 2) //shooting the Box to the right of the hit Box
        			{
        				//select the next shot being right from previous
        				if (shipHitX + trackShot >= 10)
        				{
        					dir = 4;
        					continue;
        				}
        				else
        				{
	        				Box Shot = player.getBox((shipHitX + trackShot), shipHitY); //using trackShot to hit the Box to the right
	        				if (Shot.wasShot)
	        				{
	        					continue;
	        				}
	        				else
	        				{
	        					enemyTurn = false;
	        					dirOfNextShot.remove(dir);
	        					break;
	        				}
        				}
        			}
        			if (dir == 3) //shooting the Box below the hit Box
        			{
        				//select the next shot being down from previous
        				if (shipHitY - trackShot <= 0)
        				{
        					dir = 1;
        					continue;
        				}
        				else
        				{
	        				Box Shot = player.getBox(shipHitX, (shipHitY - trackShot)); //using trackShot to hit the Box below
	        				if (Shot.wasShot)
	        				{
	        					continue;
	        				}
	        				else
	        				{
	        					enemyTurn = false;
	        					dirOfNextShot.remove(dir);
	        					break;
	        				}
        				}
        			}
        			if (dir == 4) //shooting the Box left of the hit Box
        			{
        				//select the next shot being left from previous
        				if (shipHitX - trackShot <= 0)
        				{
        					dir = 2;
        					continue;
        				}
        				else
        				{
	        				Box Shot = player.getBox((shipHitX - trackShot), shipHitY); //using trackShot to hit the Box to the left
	        				if (Shot.wasShot)
	        				{
	        					continue;
	        				}
	        				else
	        				{
	        					enemyTurn = false;
	        					dirOfNextShot.remove(dir);
	        					break;
	        				}
	        			}      			
        			}
        		}
        		if (shipHitX < 0 || shipHitY > 10 || shipHitY < 0 || shipHitY > 10)//if out of range of board
        		{
        			enemyMove();
        		}
        	}
	        if (player.ships == 0) //if the player has no ships left 
	        {
	        	failWhale();
	    	}
         }
    }
    private void startGame() 
    {
    	shipsPlaced = 7;
    	while (shipsPlaced > -1) //placing the enemy ships in a random position 
    	{
	        int x = random.nextInt(10);
	        int y = random.nextInt(10);
	
	        if (enemy.ShipPos(new Ship(shipsToPlace[shipsPlaced], Math.random() < 0.5),x,y)) 
	        {
	            shipsPlaced--;
	        }
    	}
        running = true; //running starts the game as when its false ships still need to be placed 
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
		Stage menu = new Stage(); //creating the menu screen
		menu.setTitle("Battleships");		
		
		Button btnPlay = new Button("Play Game"); //creating the buttons  
		Button btnExit= new Button("Exit Game");		

		btnExit.setOnAction(e -> System.exit(0)); //closes the program
		btnPlay.setOnAction(e -> { //event handler for the button being pressed 
			try 
			{
				game(menu);
				primaryStage.close();//closes the stage if the button is pressed
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		});
		HBox layout = new HBox();
		layout.getChildren().addAll(btnPlay, btnExit); //adds the buttons to the hbox
		Scene scene1= new Scene(layout, 150, 150); //adds the hbox to the scene
		primaryStage.setScene(scene1);
		primaryStage.show();//shows the scene
    }
    
    public void campione() //win game pop-up box 
    {
		Stage popupwindow=new Stage();   
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Game Outcome");
		Label label1= new Label("You Win");
		Button button1= new Button("Exit Game");
		button1.setOnAction(e -> System.exit(0));
		VBox layout= new VBox(10);
		layout.getChildren().addAll(label1, button1);
		layout.setAlignment(Pos.CENTER);
		Scene scene1= new Scene(layout, 300, 250);
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();
    }
    public void failWhale() //lose game pop-up box
    {
		Stage popupwindow=new Stage();   
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Game Outcome");
		Label label1= new Label("You Lose");
		Button button1= new Button("Exit Game");
		button1.setOnAction(e -> System.exit(0));
		VBox layout= new VBox(10);
		layout.getChildren().addAll(label1, button1);
		layout.setAlignment(Pos.CENTER);
		Scene scene1= new Scene(layout, 300, 250);
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();
    }
    
    public void game(Stage primaryStage) throws Exception //displays the gameWindow
    {
		Scene scene = new Scene(gameWindow());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) 
    {
    	launch(args);
    }
}