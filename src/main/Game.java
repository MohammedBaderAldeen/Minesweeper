package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import models.Cell;
import models.CellStatus;
import models.Player;
import models.Score;
import settings.Action;
import settings.Settings;
import settings.Status;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;


public abstract class Game extends Application   implements Serializable  {
	
    
	private static final long serialVersionUID = -4953500087051076012L;
	
    public boolean over = false;
    
    protected Pane root;
    
    Stage stage;
    
    protected int bombsCount;
   
    private Rectangle[] remainingBombs = new Rectangle[3];
    
    private Rectangle[] timeDigits = new Rectangle[3];
    
    private Rectangle Smile;
    
    public Label[] scoreBoard = new Label[Settings.PLAYERS_COUNT];
    
    public Player currentPlayer;
        
    LinkedList<Player> players = new LinkedList<>();
    
    Cell[][] grid = new Cell[Settings.Y_CELLS][Settings.X_CELLS];
    
    List<Pair<Integer, Integer>> shuffledCoordinates = new ArrayList<>();
    
    List<Cell> Super_Shield = new ArrayList<>();
    
    boolean running = true;
    
    boolean saveReplay =Settings.SAVE_REPLAY;
    
    public ScoreBoard result;
    
    protected boolean re=false;

    public void start(Parent parent) {
        this.stage = new Stage();
        Game g = this;
        this.initGame((Pane) parent);
        if(this.saveReplay) {
        	this.initReplay();
        }
        this.stage.setScene(new Scene(parent));
        this.stage.setResizable(true);
        this.stage.sizeToScene();
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
            	running =false;  
            	
            	
            }

			
            
         });
        new Thread() {

            // runnable for that thread
            public void run() {
                while(running) {
                    try {
                        // imitating work
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    
                    Platform.runLater(new Runnable() {

                        public void run() {
                        	// System.out.println("Second Passed");
                           if(playing) g.SecondPassed();
                        }
                    });
                }
            }
        }.start();
        this.stage.show();
        if(Settings.AutoSave) {
    		AutoSave();
    	}
    }
    
	int TimePassed = 0;
	int OverAllTime =0;
    protected void SecondPassed() {
		if(TimePassed == 0)
		{
			
			this.changeTurn();
		}
		else
		{
			TimePassed--;
		}
		
		this.showTime(TimePassed);
		MoveSuperShield();
		OverAllTime++;
	}

	protected void showTime(int timePassed2) {
		int onesDigit = timePassed2 % 10;
        int tensDigit = ((timePassed2 / 10) % 10);
        int hundredsDigit = timePassed2 / 100;

        this.timeDigits[0].setFill(Settings.ImageService.getImage(Status.getDisplayNumber(onesDigit)));
        this.timeDigits[1].setFill(Settings.ImageService.getImage(Status.getDisplayNumber(tensDigit)));
        this.timeDigits[2].setFill(Settings.ImageService.getImage(Status.getDisplayNumber(hundredsDigit)));
		
	}

	
    private void initGame(Pane root) {
        this.root = root;
        
        this.initGrid(root);
        this.initPlayers();
        this.bombsCount = Settings.BOMBS_COUNT;
        this.initRemainingBombs();
        this.initTime();
        this.initFace();
        initSave();
        this.initScoreBoard();
        this.initResults();
    }
    

	boolean playing=true;
    private synchronized void MoveSuperShield() {
    	try {
    		if(playing)
        	{
        		if(!Super_Shield.isEmpty()) {
            		Iterator<Cell> iterator = this.Super_Shield.iterator();
               	 while (iterator.hasNext()) {
               		 Cell cell = iterator.next();
               		 if(cell.getIsOpened())
               			 Super_Shield.remove(cell);
               		 else
               		 {
               			 List<Cell> Neighbors= cell.getNeighbors();
                   		 this.shuffledCoordinates = new ArrayList<>();
                   		 int NeighborsCount=0;
                   		 Iterator<Cell> cells = Neighbors.iterator();
                   		 while (cells.hasNext()) {
                   			 Cell n = cells.next();
                   			 this.shuffledCoordinates.add(new Pair<>(n.getY(), n.getX()));
                   			 NeighborsCount++;
                   		 }
                   		 Collections.shuffle(this.shuffledCoordinates);
                   		 boolean done = false;
                   		 int index =0;
                   		 Pair<Integer, Integer> coordinate;
                   		 while(!done && index < NeighborsCount)
                   	     {
                   	        	coordinate = this.shuffledCoordinates.get(index);
                   	        	if(this.grid[coordinate.getKey()][coordinate.getValue()].hasBomb||this.grid[coordinate.getKey()][coordinate.getValue()].hasShield||this.grid[coordinate.getKey()][coordinate.getValue()].getIsOpened())
                   	        	{
                   	        		index++;
                   	        	}
                   	        	else
                   	        	{
                   	        		this.grid[cell.getY()][cell.getX()].hasShield=false;
                   	        		this.Super_Shield.remove(cell);
                   	        		this.Super_Shield.add(this.grid[coordinate.getKey()][coordinate.getValue()]);
                   	        		this.grid[coordinate.getKey()][coordinate.getValue()].hasShield=true;
                   	        		System.out.println("Shield Moved  "+coordinate.getKey()+" "+coordinate.getValue());
                   	        		done = true;
                   	        	}
                   	     }
               		 }
               		 
               	 }
            	}
        	}   
    	}
    	catch(Exception e)
    	{
    	}
    	 		
	}

	protected void initScoreBoard() {
    	Iterator<Player> iterator = this.players.iterator();
    	String x="";
    	int i=0;
        while (iterator.hasNext()) {
        	Player player = iterator.next();
             x = player.getName()+" Score:"+player.getScore().getValue()+"  Shields:"+player.getShieldCount();
             StackPane scorePane = new StackPane();
             Label scoreLabel = new Label(x);
             scoreLabel.setFont(new Font("Arial", 14));
             this.scoreBoard[i] = scoreLabel;
             scorePane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
             scorePane.setTranslateX((13+(12*i))*0.75 * Settings.CELL_SIZE);
             scorePane.getChildren().add(scoreLabel);
            this.root.getChildren().add(scorePane);
            i++;
            }      
        
	}
    
    private void updateScoreBoard() {
    	Iterator<Player> iterator = this.players.iterator();
    	Game game= this;
    	String x="";
    	int i=0;
    	for(int j =0;j<game.scoreBoard.length;j++)
    	{
    		game.scoreBoard[j].setText("");
    	}
        while (iterator.hasNext()) {
        	Player player = iterator.next();
                if(!this.re){
        	if (player.getScore().getValue() < Settings.DISPLAY_SCORE_THRESHOLD) {
        		x = player.getName()+" Score:"+player.getScore().getValue()+"  Shields:"+player.getShieldCount()+"\n";
        	}
        	else
        		x = player.getName()+" Score: 00"+"  Shields:"+player.getShieldCount()+"\n";
                }
                else
                    x = player.getName()+" Score:"+player.getScore().getValue()+"  Shields:"+player.getShieldCount()+"\n";
        	game.scoreBoard[i].setText(x);
        	i++;
            }  
       
        
	}

    private void initGrid(Pane root) {
        Cell cell;
        this.shuffledCoordinates = new ArrayList<>();
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
                this.shuffledCoordinates.add(new Pair<>(y, x));
                cell = new Cell(x, y, false,false, this);
                this.grid[y][x] = cell;
                root.getChildren().add(cell);
            }
        }
        // Random
        Collections.shuffle(this.shuffledCoordinates);
        Pair<Integer, Integer> coordinate;
        for (int i = 0; i < Settings.BOMBS_COUNT; i++) {
            coordinate = this.shuffledCoordinates.get(i);
            this.grid[coordinate.getKey()][coordinate.getValue()].hasBomb = true;
            this.grid[coordinate.getKey()][coordinate.getValue()].setValue(Status.GRAY_BOMBED);
        }
        int remeaningShield = 0;
        int allShield = Settings.SHIELDS_COUNT;
        Collections.shuffle(this.shuffledCoordinates);
        while(remeaningShield < allShield)
        {
        	coordinate = this.shuffledCoordinates.get(remeaningShield);
        	if(this.grid[coordinate.getKey()][coordinate.getValue()].hasBomb)
        	{
        		allShield++;
        	}
        	else
        	{
        		this.grid[coordinate.getKey()][coordinate.getValue()].hasShield = true;
        	}
        	remeaningShield++;
        }
        
        remeaningShield = 0;
        allShield = Settings.SUPER_SHIELDS_COUNT;
        Collections.shuffle(this.shuffledCoordinates);
        this.Super_Shield = new ArrayList<>();
        while(remeaningShield < allShield)
        {
        	coordinate = this.shuffledCoordinates.get(remeaningShield);
        	if(this.grid[coordinate.getKey()][coordinate.getValue()].hasBomb||this.grid[coordinate.getKey()][coordinate.getValue()].hasShield)
        	{
        		allShield++;
        	}
        	else
        	{
        		this.grid[coordinate.getKey()][coordinate.getValue()].hasShield=true;
        		this.Super_Shield.add(this.grid[coordinate.getKey()][coordinate.getValue()]);
        	}
        	remeaningShield++;
        }
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
                cell = this.grid[y][x];
                // Calculate number
                cell.init(this.grid,false);
            }
        }
    }

    
    protected void initFace() {
        StackPane stackPane = new StackPane();
        Smile = new Rectangle(1.5 * Settings.CELL_SIZE, 1.5 * Settings.CELL_SIZE);
        Smile.setFill(Settings.ImageService.getImage(Status.SMILEY));
        stackPane.getChildren().add(Smile);
        stackPane.setTranslateX(7*Settings.CELL_SIZE);
        stackPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.reset();
            }
        });
        this.root.getChildren().add(stackPane);
    }
   
   
    private void reset() {
        this.root.getChildren().clear();
        this.players.clear();
        this.currentPlayer = null;
        this.replay = null;
        this.over = false;
        this.stage.setTitle(Settings.WINDOW_TITLE);
        this.initGame(this.root);
        this.playing=true;
        this.OverAllTime=0;
        if(this.saveReplay) {
        	this.initReplay();
        }
    }

    
    public void initRemainingBombs() {
        
        double width = 0.75, height = 1.5;

        StackPane onesDigit = new StackPane();
        StackPane tensDigit = new StackPane();
        StackPane hundredsDigit = new StackPane();

        Rectangle onesRectangle = new Rectangle(width * Settings.CELL_SIZE, height * Settings.CELL_SIZE);
        Rectangle tensRectangle = new Rectangle(width * Settings.CELL_SIZE, height * Settings.CELL_SIZE);
        Rectangle hundredsRectangle = new Rectangle(width * Settings.CELL_SIZE, height * Settings.CELL_SIZE);

        this.remainingBombs[0] = onesRectangle;
        this.remainingBombs[1] = tensRectangle;
        this.remainingBombs[2] = hundredsRectangle;

        hundredsDigit.setTranslateX(width * Settings.CELL_SIZE);
        tensDigit.setTranslateX(2 * width * Settings.CELL_SIZE);
        onesDigit.setTranslateX(3 * width * Settings.CELL_SIZE);

        onesRectangle.setFill(Settings.ImageService.getImage(
                Status.getDisplayNumber(this.bombsCount % 10)));
        tensRectangle.setFill(Settings.ImageService.getImage(
                Status.getDisplayNumber((this.bombsCount / 10) % 10)));
        hundredsRectangle.setFill(Settings.ImageService.getImage(
                Status.getDisplayNumber(this.bombsCount / 100)));

        onesDigit.getChildren().add(onesRectangle);
        tensDigit.getChildren().add(tensRectangle);
        hundredsDigit.getChildren().add(hundredsRectangle);

        this.root.getChildren().add(hundredsDigit);
        this.root.getChildren().add(tensDigit);
        this.root.getChildren().add(onesDigit);
    }
    public void initTime() {
        this.TimePassed = Settings.PLAY_TIME;
        double width = 0.75, height = 1.5;

        StackPane onesDigit = new StackPane();
        StackPane tensDigit = new StackPane();
        StackPane hundredsDigit = new StackPane();

        Rectangle onesRectangle = new Rectangle(width * Settings.CELL_SIZE, height * Settings.CELL_SIZE);
        Rectangle tensRectangle = new Rectangle(width * Settings.CELL_SIZE, height * Settings.CELL_SIZE);
        Rectangle hundredsRectangle = new Rectangle(width * Settings.CELL_SIZE, height * Settings.CELL_SIZE);

        this.timeDigits[0] = onesRectangle;
        this.timeDigits[1] = tensRectangle;
        this.timeDigits[2] = hundredsRectangle;

        hundredsDigit.setTranslateX(6*width * Settings.CELL_SIZE);
        tensDigit.setTranslateX(7 * width * Settings.CELL_SIZE);
        onesDigit.setTranslateX(8 * width * Settings.CELL_SIZE);

        onesRectangle.setFill(Settings.ImageService.getImage(
                Status.getDisplayNumber(this.TimePassed % 10)));
        tensRectangle.setFill(Settings.ImageService.getImage(
                Status.getDisplayNumber((this.TimePassed / 10) % 10)));
        hundredsRectangle.setFill(Settings.ImageService.getImage(
                Status.getDisplayNumber(this.TimePassed / 100)));

        onesDigit.getChildren().add(onesRectangle);
        tensDigit.getChildren().add(tensRectangle);
        hundredsDigit.getChildren().add(hundredsRectangle);

        this.root.getChildren().add(hundredsDigit);
        this.root.getChildren().add(tensDigit);
        this.root.getChildren().add(onesDigit);
    }

    void updatePlayerScore(Status value, Action action) {
        try {
        	if(this.currentPlayer.getShieldCount()>0) {
        		this.currentPlayer.getScore().update(value, action,true);
        		if(value==Status.BOMBED) {
        			this.currentPlayer.setShieldCount(this.currentPlayer.getShieldCount()-1);
        			this.updateRemainingBombs(true);
        		}
        		System.out.println(this.currentPlayer.getName() + "'s Shield = " + this.currentPlayer.getShieldCount());
        		
        		}
        	else
        		this.currentPlayer.getScore().update(value, action,false);
            System.out.println(this.currentPlayer.getName() + "'s score = " + this.currentPlayer.getScore());
        } catch (NullPointerException e) {
            // Player not found
            System.err.println(e.getMessage());
        }
        this.updateScoreBoard();
    }

    private void updateRemainingBombs(boolean isFlagged) {
        this.bombsCount = isFlagged ? (this.bombsCount - 1) : (this.bombsCount + 1);
        int onesDigit = this.bombsCount % 10;
        int tensDigit = ((this.bombsCount / 10) % 10);
        int hundredsDigit = this.bombsCount / 100;

        this.remainingBombs[0].setFill(Settings.ImageService.getImage(Status.getDisplayNumber(onesDigit)));
        this.remainingBombs[1].setFill(Settings.ImageService.getImage(Status.getDisplayNumber(tensDigit)));
        this.remainingBombs[2].setFill(Settings.ImageService.getImage(Status.getDisplayNumber(hundredsDigit)));
    }

    void checkGameOver(int cellX, int cellY) {
        Player  player;
        Iterator<Player> iterator = this.players.iterator();
        while (iterator.hasNext()) {
            player = iterator.next();
            final String playername = player.getName();
            if (!player.getScore().isPositive()) {
            	if(this.result!=null){
            	Result res =this.result.results.stream().filter(r->r.getName()==playername).findAny().orElse(null);
            	res.setShieldCount(player.getShieldCount());
            	res.setFinalScore(player.getScore().getValue());
            	}
                iterator.remove();
            }
        }
        if (this.players.size() == 0) {
            this.endGame(cellX, cellY);
            return;
        }
        Cell cell;
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
                cell = this.grid[y][x];
                if (cell.hasBomb) {
                    if (!cell.getIsFlagged()&!cell.getIsOpened())
                        return;
                } else {
                    if (!cell.getIsOpened())
                        return;
                }
            }
        }
        this.over = true;
        for (Player rPlayer : this.players) {
        	for(int i=0;i<rPlayer.getShieldCount();i++)
            {
            	rPlayer.setScore(new Score(this.currentPlayer.getScore().getValue()+50));
            }
        	if(result!=null) {
        		Result res =this.result.results.stream().filter(r->r.getName()==rPlayer.getName()).findAny().orElse(null);
            	res.setShieldCount(rPlayer.getShieldCount());
            	res.setFinalScore(rPlayer.getScore().getValue());
        	}
        }
        
        this.stage.setTitle(getWinnersTitle());
        this.playing = false;
        if(result!=null )this.saveScore();
        if(this.saveReplay) {
        	this.SaveReplay();
        }
    }

    
    private void endGame(int cellX, int cellY) {
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
                this.grid[y][x].setOnMouseClicked(null);
                if ((x != cellX || y != cellY) && this.grid[y][x].hasBomb) {
                    this.grid[y][x].open();
                }
            }
        }
        Smile.setFill(Settings.ImageService.getImage(Status.DEAD_SMILEY));
        this.over = true;
        this.stage.setTitle(this.getWinnersTitle());
        this.playing = false;
        if(result!=null )this.saveScore();
        if(this.saveReplay) {
        	this.SaveReplay();
        }
    }
    
	public synchronized void changeTurn() {
        Iterator<Player> iterator = this.players.iterator();
        while (iterator.hasNext()) {
            if (this.currentPlayer == iterator.next()) {
            	
                this.currentPlayer = (iterator.hasNext()) ? iterator.next() : this.players.getFirst();
                          
                break;
            }
        }
        TimePassed = Settings.PLAY_TIME;
        String title = this.currentPlayer.getName();
        if (this.currentPlayer.getScore().getValue() < Settings.DISPLAY_SCORE_THRESHOLD)
            title += " (" + this.currentPlayer.getScore().getValue() + ")";
        title += " Turn";
        this.stage.setTitle(title);     
    }

	Action savedAction;
    public void onCellOpened(Cell cell,boolean changePlayer) {
        this.updatePlayerScore(cell.getValue(), Action.OPEN);
        if(changePlayer) {
        	if(this.Super_Shield.contains(cell))
        	{
        		this.updatePlayerScore(cell.getValue(), Action.SUPER_SHIELD);
                        //cell.setValue(Status.SUPER_SHIELD);
                        cell.rectangle.setFill(Settings.ImageService.getImage(Status.SUPER_SHIELD));
        		this.currentPlayer.setShieldCount(this.currentPlayer.getShieldCount()+2);
        		savedAction = Action.SUPER_SHIELD;
        		if(this.saveReplay) {
                	this.saveMove(cell,this.currentPlayer,savedAction);
                }
        	}
        	
        	savedAction = Action.OPEN;
        	if(this.saveReplay) {
            	this.saveMove(cell,this.currentPlayer,savedAction);
            }
        	this.changeTurn();
        	if(Settings.AutoSave) {
        		AutoSave();
        	}
        }
        this.checkGameOver(cell.getX(), cell.getY());
        //this.playing =true;
        
    }

    public void onCellFlag(Cell cell) {
        this.updatePlayerScore(cell.getValue(), cell.getIsFlagged() ? Action.FLAG : Action.UN_FLAG);
        
        this.updateRemainingBombs(cell.getIsFlagged());
        if(this.saveReplay) {
        	this.saveMove(cell,this.currentPlayer,cell.getIsFlagged() ? Action.FLAG : Action.UN_FLAG);
        }
        this.changeTurn();
        if(Settings.AutoSave) {
    		AutoSave();
    	}
        this.checkGameOver(cell.getX(), cell.getY());
    }

    @Override
    public void start(Stage primaryStage) {}

    protected abstract void initPlayers();

    protected abstract String getWinnersTitle();
    
    
    /// Save routine
    private void initSave() {
    	int i =0;
    	final Button temp = new Button("Save");
        temp.setId("" + 0);
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	savedGame();
            }
        });
        StackPane stackPane = new StackPane();
        stackPane.setTranslateY((1.5)*Settings.CELL_SIZE);
        stackPane.getChildren().add(temp);
        this.root.getChildren().add(stackPane);
        final Button temp2 = new Button("Quick Save");
        temp2.setId("" + 1);
        temp2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	QuickSave();
            }
        });
        StackPane stackPane2 = new StackPane();
        stackPane2.setTranslateY((1.5)*Settings.CELL_SIZE);
        stackPane2.setTranslateX((3.5)*Settings.CELL_SIZE);
        stackPane2.getChildren().add(temp2);
        this.root.getChildren().add(stackPane2);
    }
    
    
    private void savedGame() {
    	
    	savedGame gameToSave= new savedGame();
    	gameToSave.bombsCount = this.bombsCount;
        gameToSave.grid = new CellStatus[Settings.Y_CELLS][Settings.X_CELLS];
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
            	Cell cell = this.grid[y][x];
            	gameToSave.grid[y][x]=new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged());
            }
        }
        for(Cell cell:this.Super_Shield){
        	gameToSave.Super_Shield.add(new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged()));
        }
        gameToSave.players=this.players; 
        gameToSave.currentPlayer=this.currentPlayer;
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Save Files", "*.save");
    	fileChooser.getExtensionFilters().add(extFilter);
		File dest = fileChooser.showSaveDialog(null);
		if (dest != null) {
			GameSaverExecuter saver = new GameSaverExecuter(gameToSave,dest.getAbsolutePath());
	    	saver.save();
	    	saver.close();
    	}
		
    }
    private void QuickSave() {
    	File dir = new File(Settings.AUTOSAVEPath);
    	File [] files = dir.listFiles(new FilenameFilter() {
    	    @Override
    	    public boolean accept(File dir, String name) {
    	        return name.endsWith(".save");
    	    }
    	});
    	int i = (files!=null)?files.length:0;
    	File dest = new File(Settings.AUTOSAVEPath+"game"+i+".save");
    	while(dest.exists()) {
    		i++;
    		dest = new File(Settings.AUTOSAVEPath+"game"+i+".save");
    	}
    	savedGame gameToSave= new savedGame();
    	gameToSave.bombsCount = this.bombsCount;
        gameToSave.grid = new CellStatus[Settings.Y_CELLS][Settings.X_CELLS];
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
            	Cell cell = this.grid[y][x];
            	gameToSave.grid[y][x]=new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged());
            }
        }
        for(Cell cell:this.Super_Shield){
        	gameToSave.Super_Shield.add(new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged()));
        }
        gameToSave.players=this.players; 
        gameToSave.currentPlayer=this.currentPlayer;
        GameSaverExecuter saver = new GameSaverExecuter(gameToSave,dest.getAbsolutePath());
    	saver.save();
    	saver.close();
    }
    private void AutoSave() {
    	savedGame gameToSave= new savedGame();
    	gameToSave.bombsCount = this.bombsCount;
        gameToSave.grid = new CellStatus[Settings.Y_CELLS][Settings.X_CELLS];
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
            	Cell cell = this.grid[y][x];
            	gameToSave.grid[y][x]=new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged());
            }
        }
        for(Cell cell:this.Super_Shield){
        	gameToSave.Super_Shield.add(new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged()));
        }
        gameToSave.players=this.players; 
        gameToSave.currentPlayer=this.currentPlayer;
        File dest = new File(Settings.AUTOSAVEPath+"recent"+".save");
        GameSaverExecuter saver = new GameSaverExecuter(gameToSave,dest.getAbsolutePath());
    	saver.save();
    	saver.close();
		
	}
    /// Load routine
    public void start(Parent parent,savedGame loadedGame ) {
        this.stage = new Stage();
        Game g = this;
        this.initoldGame((Pane) parent,loadedGame);
        this.stage.setScene(new Scene(parent));
        this.stage.setResizable(true);
        this.stage.sizeToScene();
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
            	running =false;    
            }
            
         });
        new Thread() {

            // runnable for that thread
            public void run() {
                while(running) {
                    try {
                        // imitating work
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    
                    Platform.runLater(new Runnable() {

                        public void run() {
                        	// System.out.println("Second Passed");
                        	if(playing) g.SecondPassed();
                        }
                    });
                }
            }
        }.start();
        this.stage.show();
    }

	private void initoldGame(Pane root, main.savedGame loadedGame) {
		this.root = root;        
        this.initOldGrid(root,loadedGame);
        this.initOldPlayers(loadedGame);
        this.bombsCount = loadedGame.bombsCount;
        this.initRemainingBombs();
        this.initTime();
        this.initFace();
        initSave();
        this.initScoreBoard();
		
	}

	 protected abstract void initOldPlayers(main.savedGame loadedGame) ;

	private void initOldGrid(Pane root, main.savedGame loadedGame) {
		CellStatus cellstatus;
		Cell cell;
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
            	cellstatus = loadedGame.grid[y][x];
            	cell= new Cell(cellstatus.getX(),cellstatus.getY(),cellstatus.isHasBomb(),cellstatus.isHasShield(),this);
            	cell.setValue(cellstatus.getValue());
            	cell.setOpened(cellstatus.isOpened());
            	cell.setFlagged(cellstatus.isFlagged());
                this.grid[y][x] = cell;
                root.getChildren().add(cell);
            }
        }
        
        for(CellStatus shield:loadedGame.Super_Shield){
        	this.Super_Shield.add(this.grid[shield.getY()][shield.getX()]);
        }
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
                cell = this.grid[y][x];
                // Calculate number
                cell.init(this.grid,true);
            }
        }
		
	}
	
	/// SAVE_RELAY
	ReplayGame replay;
	public boolean isNotReplay =true;
	private void initReplay() {
		// TODO Auto-generated method stub
		replay = new ReplayGame();
		savedGame gameToSave = new savedGame(); 
		gameToSave.bombsCount = this.bombsCount;
        gameToSave.grid = new CellStatus[Settings.Y_CELLS][Settings.X_CELLS];
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
            	Cell cell = this.grid[y][x];
            	gameToSave.grid[y][x]=new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged());
            }
        }
        for(Cell cell:this.Super_Shield){
        	//gameToSave.Super_Shield.add(new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged()));
        	gameToSave.grid[cell.getY()][cell.getY()].setShield(false);
        }
        gameToSave.players=this.players; 
        gameToSave.currentPlayer=this.currentPlayer;
        replay.setGameInit(gameToSave);
		for(Player player:this.players) {
			PlayerReplay playerReplay = new PlayerReplay(player);
			replay.playerReplays.add(playerReplay);
		}
		
	}
	 protected void saveMove(Cell cell, Player currentPlayer,Action action) {
		 CellStatus cellstatus = new CellStatus(cell.getX(),cell.getY(),cell.getValue(),cell.isHasBomb(),cell.isHasShield(),cell.getIsOpened(),cell.getIsFlagged());
		 if(action!=Action.SUPER_SHIELD) {
				PlayerMove playerMove= new PlayerMove(cellstatus,action,this.OverAllTime);
				PlayerReplay playerReplay = (PlayerReplay)replay.playerReplays.stream().filter(pp->pp.getPlayer().getName()==currentPlayer.getName()).findAny().orElse(null);
				playerReplay.playerMoves.add(playerMove);
			}
			else {
				replay.superShieldsFinal.add(cellstatus);
			}
		}
	 private void SaveReplay() {
		 FileChooser fileChooser = new FileChooser();
		 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Replay Files", "*.replay");
		 fileChooser.getExtensionFilters().add(extFilter);
		 File dest = fileChooser.showSaveDialog(null);
		 if (dest != null) {
				GameSaverExecuter saver = new GameSaverExecuter(this.replay,dest.getAbsolutePath());
		    	saver.save();
		    	saver.close();
	    	}
		}
	 //ScoreBoard
	 private void initResults() {
			this.result = new ScoreBoard(new Date());
			for(Player player:this.players) {
				Result res = new Result(player.getName(), player.getScore().getValue(), player.getShieldCount());
				result.results.add(res);
			}
			
		}
	 private void saveScore() {
		 this.result.setGameEndDate(new Date());
		 List<ScoreBoard> allresult= (List<ScoreBoard>)ReadObjectFromFile(Settings.SAVEPath);
		 if(saveReplay) {
			 result.setReplay(replay); 
		 }
		 int id=(allresult!=null)?allresult.size():0;
		 result.setId(id);
		 if(allresult==null) allresult=new ArrayList();
		 allresult.add(result);
		 GameSaverExecuter saver = new GameSaverExecuter(allresult,Settings.SAVEPath);
	    	saver.save();
	    	saver.close();
			
		}
	 public Object ReadObjectFromFile(String filepath) {
	    	try {
	    		FileInputStream fileIn = new FileInputStream(filepath);
	    	    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
	    	    Object obj = objectIn.readObject();
	    	    System.out.println("The Object has been read from the file");
	    	    objectIn.close();
	    	    return obj;
	    	    } catch (Exception ex) {
	    	    	ex.printStackTrace();
	    	    	return null;
	    	    	}
	    	}
}


