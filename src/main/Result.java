package main;

import java.io.Serializable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Result implements Serializable {
	
	private static final long serialVersionUID = -8165587703242370276L;

	private String name;
	
	private int finalScore;
	private int ShieldCount;
	
	private transient StringProperty playerName;
    private transient IntegerProperty playerScore;
    private transient IntegerProperty playerShield;
	
	public String getName() {
		return name;
	}
	
	public int getFinalScore() {
		return finalScore;
	}
	public void setFinalScore(int x) {
		this.finalScore = x;
		this.playerScore= new SimpleIntegerProperty(x);
	}
	
	public int getShieldCount() {
		return ShieldCount;
		
	}
	public void setShieldCount(int x) {
		this.ShieldCount = x;
		this.playerShield = new SimpleIntegerProperty(x);
	}
	public Result(String name, int finalScore, int shieldCount) {
		super();
		this.name = name;
		this.playerName = new SimpleStringProperty(name);
		this.finalScore = finalScore;
		ShieldCount = shieldCount;
	}
	
	public StringProperty getPlayerName() {
		return new SimpleStringProperty(this.name);
	}
	
	public IntegerProperty getPlayerScore() {
		return new SimpleIntegerProperty(this.finalScore);
	}
	
	public IntegerProperty getPlayerShield() {
		return new SimpleIntegerProperty(this.ShieldCount);
	}
	
}