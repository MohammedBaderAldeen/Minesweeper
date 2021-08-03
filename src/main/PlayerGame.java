package main;

import models.Player;
import settings.Settings;
import java.util.ArrayList;
import java.util.List;

public class PlayerGame extends Game {
	private static final long serialVersionUID = 5564348940533166818L;
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

	@Override
	protected void initOldPlayers(main.savedGame loadedGame) {
		this.players = loadedGame.players;
		this.currentPlayer = loadedGame.currentPlayer;
		this.stage.setTitle(this.currentPlayer.getName() + "'s (" + this.currentPlayer.getScore().getValue()
				+ ") Turn");
		}
}
