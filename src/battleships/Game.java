package battleships;

import java.util.Random;
import battleships.Box;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Game extends Application 
{
    public static Paint selectedColourHit = Color.RED; //setting the default colours 
    public static Paint selectedColourMiss = Color.WHITE;
    public static Paint selectedColourBackground = Color.AQUA;
    public static Paint selectedColourShip = Color.GREEN; 
    
	private boolean running = false;//when running is false ships haven't been placed
    private Board eBoard, pBoard;
    private int [] shipsToPlace = {5,4,4,3,3,2,2,2}; //array for ships
    private int placedShips = 7;
    private boolean enemyTurn = false;
    private Random random = new Random();
    
    private Parent gameWindow() 
    {
        BorderPane side = new BorderPane();
        side.setPrefSize(600, 800);
        side.setRight(new Text("CONTROLS" + System.lineSeparator() + "Right Click = Horizontal Ship" + System.lineSeparator() 
        	+ "Left Click = Vertical Ship" + System.lineSeparator() + "Top Grid = Enemy" + System.lineSeparator() 
        	+ "Bottom Grid = Players"));
        
        eBoard = new Board(true, event -> 
        {
            if (!running)
            {
                return;
            }
            Box cell = (Box) event.getSource();
            if (cell.wasShot)
            {
                return;
            }
            enemyTurn = !cell.shoot();
            if (eBoard.ships == 0) 
            {
            	winner();
            }
            if (enemyTurn)
            {
                enemyMove();
            }
        });

        pBoard = new Board(false, event -> 
        {
            if (running)
            {
                return;
            }
            Box cell = (Box) event.getSource();
            if (pBoard.ShipPos(new Ship(shipsToPlace[placedShips], event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) 
            {
                if (--placedShips == -1) 
                {
                    startGame();
                }
            }
        });
        
        VBox vbox = new VBox(50, eBoard, pBoard);
        vbox.setAlignment(Pos.CENTER);
        side.setCenter(vbox);
        return side;
    }
    
    private void startGame() 
    {
        // place enemy ships
        int enemyShipPlaced = 7;
        while (enemyShipPlaced > -1) 
        {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            if (eBoard.ShipPos(new Ship(shipsToPlace[enemyShipPlaced], Math.random() < 0.5), x, y)) 
            {
            	if (--enemyShipPlaced == -1)
            	{
            		enemyShipPlaced--;
            	}
            }
        }
        running = true;
    }

    private void enemyMove() 
    {
        while (enemyTurn) 
        {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Box cell = pBoard.getBox(x, y);
            if (cell.wasShot)
            {
                continue;
            }
            enemyTurn = cell.shoot();
            if (pBoard.ships == 0) 
            {
            	loser();
        	}
        }
   	}
    public void game(Stage primaryStage) throws Exception //displays the gameWindow
    {
		Scene scene = new Scene(gameWindow());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void start(Stage primaryStage) throws Exception 
    {
		primaryStage.setTitle("Battleships");		
		
		Button btnPlay = new Button("Play Game"); //creating the buttons  
		Button btnCustom = new Button("Customise");
		Button btnExit= new Button("Exit Game");		

		btnExit.setOnAction(e -> System.exit(0)); //event handlers for the button being pressed 
		btnCustom.setOnAction(e -> {
			Customiser();
			primaryStage.close();//closes the stage
		});
		btnPlay.setOnAction(e -> { 
			try 
			{
				game(primaryStage);
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		});
		VBox layout = new VBox();
		layout.getChildren().addAll(btnPlay, btnCustom, btnExit); //adds the buttons to the hbox
		Scene scene1= new Scene(layout, 150, 150); //adds the hbox to the scene
		primaryStage.setScene(scene1);
		primaryStage.show();//shows the scene
    }

    private void Customiser() 
    {
		Stage primaryStage = new Stage();
		Button menu = new Button("Main Menu");
		menu.setOnAction(e -> {
			try 
			{
				start(primaryStage);
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		});
		Button selected = new Button("Confirm Customisation");
		
    	ChoiceBox<String> hitColour = new ChoiceBox<String>(FXCollections.observableArrayList("Red", "Orange", "Yellow"));
    	Label lbHitColour = new Label("Hit Marker Colour");
    	hitColour.setTooltip(new Tooltip("Select the colour of the Hit Marker"));
	    
		ChoiceBox<String> missColour = new ChoiceBox<String>(FXCollections.observableArrayList("White", "Grey", "Brown"));
		Label lbMissColour = new Label("Miss Marker Colour");
		missColour.setTooltip(new Tooltip("Select the colour of the Miss Marker"));
		
		ChoiceBox<String> backgroundColour = new ChoiceBox<String>(FXCollections.observableArrayList("Light Blue", "Aqua", "Dark Blue"));
		Label lbBackgroundColour = new Label("Grid Background Colour");
		backgroundColour.setTooltip(new Tooltip("Select the colour of the Grids Background"));
		
		ChoiceBox<String> shipColour = new ChoiceBox<String>(FXCollections.observableArrayList("Green", "Purple", "Pink"));
		Label lbShipColour = new Label("Player Ship Colour");
		shipColour.setTooltip(new Tooltip("Select the colour of the players Ships"));
		
		selected.setOnAction(e -> {
			String chosenHit = hitColour.getValue();
			String chosenMiss = missColour.getValue();
			String chosenBackground = backgroundColour.getValue();
			String chosenShip = shipColour.getValue();
			
			if (chosenHit == "Red")
            {
            	Box.colourOfHit = Color.RED;
            }
            if (chosenHit == "Orange")
            {
            	Box.colourOfHit = Color.ORANGE;
            }
            if (chosenHit == "Yellow")
            {
            	Box.colourOfHit = Color.YELLOW;
            }
            
			if (chosenMiss == "White")
            {
            	Box.colourOfMiss = Color.WHITE;
            }
            if (chosenMiss == "Grey")
            {
            	Box.colourOfMiss = Color.GREY;
            }
            if (chosenMiss == "Brown")
            {
            	Box.colourOfMiss = Color.BROWN;
            }
            
			if (chosenBackground == "Aqua")
            {
            	Box.colourOfBackground = Color.AQUA;
            }
            if (chosenBackground == "Light Blue")
            {
            	Box.colourOfBackground = Color.LIGHTBLUE;
            }
            if (chosenBackground == "Dark Blue")
            {
            	Box.colourOfBackground = Color.DARKBLUE;
            }
            
			if (chosenShip == "Green")
            {
            	Board.colourOfShips = Color.GREEN;
            }
            if (chosenShip == "Purple")
            {
            	Board.colourOfShips = Color.PURPLE;
            }
            if(chosenShip == "Pink")
            {
            	Board.colourOfShips = Color.PINK;
            }
		});
		
		VBox choiceBoxes = new VBox(10);
		choiceBoxes.getChildren().addAll(lbHitColour, hitColour, lbMissColour, missColour, lbBackgroundColour, 
				backgroundColour,lbShipColour, shipColour, selected, menu);
		Scene customScene = new Scene(choiceBoxes, 200, 400);
		primaryStage.setScene(customScene);
		primaryStage.show();
	}

	public static void main(String[] args) 
    {
        launch(args);
    }
    public void winner()
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
    public void loser()
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
}