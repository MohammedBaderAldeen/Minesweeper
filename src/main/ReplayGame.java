package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import models.CellStatus;
import models.Player;
import settings.Action;

public class ReplayGame implements Serializable{
	
	private static final long serialVersionUID = -5767935203440167924L;
	private savedGame GameInit;
	
	public savedGame getGameInit() {
		return GameInit;
	}
	
	public void setGameInit(savedGame gameInit) {
		GameInit = gameInit;
	}
	public List<PlayerReplay> playerReplays = new ArrayList<>(); 
	public List<CellStatus> superShieldsFinal = new ArrayList<>();
}
class PlayerReplay implements Serializable{
	
	private static final long serialVersionUID = 8998863275806018071L;
	public PlayerReplay(Player player) {
		super();
		this.player = player;
		this.playerMoves = new ArrayList<>();
	}
	private Player player;
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	public List<PlayerMove> playerMoves;
}
class PlayerMove implements Serializable{
	
	private static final long serialVersionUID = 7955008209650418114L;
	private CellStatus CellStatus;
	private Action playerAction;
	private int ActionTime;
	public PlayerMove(models.CellStatus cellStatus, Action playerAction, int actionTime) {
		super();
		CellStatus = cellStatus;
		this.playerAction = playerAction;
		ActionTime = actionTime;
	}
	
	public CellStatus getCellStatus() {
		return CellStatus;
	}
	
	public Action getPlayerAction() {
		return playerAction;
	}
	
	public int getActionTime() {
		return ActionTime;
	} 
	
}

