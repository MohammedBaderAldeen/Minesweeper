package models;

import javafx.util.Pair;
import settings.Settings;
import java.io.Serializable;
import java.util.List;

public class Player  implements Serializable{
    private static final long serialVersionUID = 6584208909985710453L;
    private String name;
    private Score score = new Score();
    public int shieldCount = 0;

    public Player(String name) {
        this(name, new Score());
    }

    public Player(String name, Score score) {
        this.name = name;
        this.score = score;
    }

    

	public Player(String name,  int shieldCount) {
		super();
		this.name = name;
		
		this.shieldCount = shieldCount;
	}

	public void makeMove(List<Pair<Integer, Integer>> shuffledCoordinates) {}

    public String getName() {
        return name;
    }

    public Score getScore() {
        return this.score;
    }
    public void setScore(Score score) {
		this.score = score;
	}

	public int getShieldCount() {
		if(name=="Computer" ) {
			if(Settings.computerShield)return shieldCount;
			else
				return 0;
			
		}
		else
			return shieldCount;
	}

	public void setShieldCount(int shieldCount) {
		this.shieldCount = shieldCount;
	}
}
