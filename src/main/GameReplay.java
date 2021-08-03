package main;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Cell;
import models.CellStatus;
import models.Player;
import settings.Action;
import settings.Settings;

public class GameReplay extends Game {

	/**
	 * 
	 */
	private static final long serialVersionUID = -728486638110333435L;

	public GameReplay() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initPlayers() {
		String[] names=Settings.PlayerNames.split(";");
        for (int i = 0; i < Settings.PLAYERS_COUNT; i++) {
        	if(i<names.length) {
        		this.players.add(new Player(names[i],Settings.SHIELDS_INIT));
        	}
        	else
        		this.players.add(new Player("Player " + (i + 1),Settings.SHIELDS_INIT));
        }
        this.currentPlayer = this.players.getFirst();
        this.stage.setTitle(this.currentPlayer.getName() + "'s (" + this.currentPlayer.getScore().getValue()
         + ") Turn");	
	}

	@Override
	protected String getWinnersTitle() {
		StringBuilder winnersTitle = new StringBuilder("Player");
        int maxScore = Integer.MIN_VALUE;

        List<Player> winners = new ArrayList<>();
        for (Player player : this.players) {
            if (player.getScore().getValue() >= maxScore) {
                if (player.getScore().getValue() > maxScore) {
                    winners.clear();
                }
                winners.add(player);
                maxScore = player.getScore().getValue();
            }
        }
        if (winners.size() == 0) {
            winnersTitle = new StringBuilder("No player");
        }
        winnersTitle.append((winners.size() > 1) ? "s " : " ");
        for (Player player : winners) {
            winnersTitle.append(player.getName()).append(" (").append(player.getScore().getValue()).append(") ");
        }
        winnersTitle.append("won!");
        return winnersTitle.toString();
	}


	protected void initOldPlayers(main.ReplayGame loadedGame) {
		for(PlayerReplay player:loadedGame.playerReplays) {
			this.players.add(new Player(player.getPlayer().getName(),settings.Settings.SHIELDS_INIT));
		}
		this.currentPlayer = this.players.getFirst();
		this.stage.setTitle(this.currentPlayer.getName() + "'s (" + this.currentPlayer.getScore().getValue()
				+ ") Turn");
	}
	public void start(Parent parent,ReplayGame loadedGame ) {
        this.stage = new Stage();
        this.isNotReplay=false;
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

	private void initoldGame(Pane root, ReplayGame loadedGame) {
		super.root = root; 
		super.replay = loadedGame;
                super.re=true;
        this.initOldGrid(root,loadedGame);
        this.initOldPlayers(loadedGame);
        super.bombsCount = loadedGame.getGameInit().bombsCount;
        this.initRemainingBombs();
        this.initTime();
        super.initFace();        
        super.initScoreBoard();
		
	}

	private void initOldGrid(Pane root, ReplayGame loadedGame) {
		CellStatus cellstatus;
		Cell cell;
        for (int y = 0; y < Settings.Y_CELLS; y++) {
            for (int x = 0; x < Settings.X_CELLS; x++) {
            	cellstatus = loadedGame.getGameInit().grid[y][x];
            	cell= new Cell(cellstatus.getX(),cellstatus.getY(),cellstatus.isHasBomb(),cellstatus.isHasShield(),this);
            	cell.setValue(cellstatus.getValue());
            	cell.setOpened(cellstatus.isOpened());
            	cell.setFlagged(cellstatus.isFlagged());
            	cell.listenToMouse=false;
                this.grid[y][x] = cell;
                root.getChildren().add(cell);
            }
        }
        
        for(CellStatus shield:loadedGame.superShieldsFinal){
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
	
	@Override
	protected void SecondPassed() {
		if(TimePassed == 0)
		{
			
			this.changeTurn();
			if (!this.over && this.currentPlayer.getName().equals("Computer")) {
	            this.currentPlayer.makeMove(this.shuffledCoordinates);
			}
		}
		else
		{
			TimePassed--;
		}
		this.showTime(TimePassed);
		this.OverAllTime++;
		PlayerReplay playerReplay = (PlayerReplay)replay.playerReplays.stream().filter(pp->pp.getPlayer().getName()==currentPlayer.getName()).findAny().orElse(null);
		if(playerReplay!= null) {
			PlayerMove playerMove = playerReplay.playerMoves.stream().filter(pp->pp.getActionTime()==this.OverAllTime).findAny().orElse(null);
			if(playerMove!=null) {
				Cell cell =this.grid[playerMove.getCellStatus().getY()][playerMove.getCellStatus().getX()];
				if (playerMove.getPlayerAction()==Action.OPEN) {
                    cell.onLeftClick();
                } else {
                    cell.onRightClick();
                }
			}
		}
		
	}

	@Override
	protected void initOldPlayers(main.savedGame loadedGame) {
		// TODO Auto-generated method stub
		
	}
        

}
