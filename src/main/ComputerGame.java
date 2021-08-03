package main;

import models.Cell;
import models.Computer;
import models.Player;
import settings.Action;
import settings.Settings;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

public class ComputerGame extends Game {

	private static final long serialVersionUID = 2797419543889107447L;

	@Override
    protected void initPlayers() {
		String[] names=Settings.PlayerNames.split(";");
		if(names.length>0)  this.players.add(new Player(names[0],Settings.SHIELDS_INIT) );
		else this.players.add(new Player("your",Settings.SHIELDS_INIT) );
        this.players.add(new Computer(this.grid));
        this.currentPlayer = this.players.getFirst();
        this.stage.setTitle(this.currentPlayer.getName() + "'s (" + this.currentPlayer.getScore().getValue()
                + ") Turn");
        this.scoreBoard = new Label[2];
    }

    @Override
    public void onCellOpened(Cell cell,boolean changePlayer) {
        this.updatePlayerScore(cell.getValue(), Action.OPEN);
        if(changePlayer) {
        	super.savedAction = Action.OPEN;
        	if(this.saveReplay) {
        		super.saveMove(cell,this.currentPlayer,savedAction);
            }
        	super.changeTurn();
        }
        super.checkGameOver(cell.getX(), cell.getY());
        if (!this.over && this.currentPlayer.getName().equals("Computer")) {
            this.currentPlayer.makeMove(this.shuffledCoordinates);
        }
    }

    @Override
    public void onCellFlag(Cell cell) {
        super.onCellFlag(cell);
        if (!this.over && this.currentPlayer.getName().equals("Computer")) {
            this.currentPlayer.makeMove(this.shuffledCoordinates);
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
	}

    @Override
    protected String getWinnersTitle() {
        StringBuilder winnersTitle = new StringBuilder();
        List<Player> winners = new ArrayList<>();
        int maxScore = Integer.MIN_VALUE;
        for (Player player : this.players) {
            if (player.getScore().getValue() >= maxScore) {
                winners.add(player);
                maxScore = player.getScore().getValue();
            }
        }
        if (winners.size() == 0) {
            winnersTitle.append("No player");
        } else {
            for (Player player : winners) {
                winnersTitle.append(player.getName()).append(" (").append(player.getScore().getValue()).append(") ");
            }
        }
        winnersTitle.append("won");
        return winnersTitle.toString();
    }

	@Override
	protected void initOldPlayers(main.savedGame loadedGame) {
		for(Player player:loadedGame.players) {
			if(player.getName()!="Computer") {
				this.players.add(player);				
			}
			else {
				Computer comPlayer = new Computer(this.grid);
				comPlayer.setScore(player.getScore());
				comPlayer.setShieldCount(player.getShieldCount());
				this.players.add(comPlayer);
			}
		}
		this.currentPlayer = this.players.getFirst();
        this.stage.setTitle(this.currentPlayer.getName() + "'s (" + this.currentPlayer.getScore().getValue()
                + ") Turn");
        this.scoreBoard = new Label[2];
		
	}
}
