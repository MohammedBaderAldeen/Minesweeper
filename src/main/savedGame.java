package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import models.CellStatus;
import models.Player;
import settings.Settings;

public class savedGame  implements Serializable{
	private static final long serialVersionUID = 7915002652645113663L;
	public CellStatus[][] grid = new CellStatus[Settings.Y_CELLS][Settings.X_CELLS];
	public List<CellStatus> Super_Shield = new ArrayList<>();
	public int bombsCount;
	public LinkedList<Player> players = new LinkedList<>();
	public Player currentPlayer;
	//Settings
	public int X_CELLS=Settings.X_CELLS;
	public int Y_CELLS=Settings.Y_CELLS;
	public int WIDTH=Settings.WIDTH;
	public int HEIGHT=Settings.HEIGHT;
	public int PLAYERS_COUNT=Settings.PLAYERS_COUNT;
	public int BOMBS_COUNT=Settings.BOMBS_COUNT;
	public int SHIELDS_COUNT=Settings.SHIELDS_COUNT;
	public int SHIELDS_INIT=Settings.SHIELDS_INIT;
	public int PLAY_TIME=Settings.PLAY_TIME;
	public int SUPER_SHIELDS_COUNT=Settings.SUPER_SHIELDS_COUNT;
	public boolean computerShield=Settings.computerShield;
	public boolean saveRecord=Settings.SAVE_REPLAY;
	public boolean autoSave=Settings.SAVE_REPLAY;
	public String names = Settings.PlayerNames;

	 
}