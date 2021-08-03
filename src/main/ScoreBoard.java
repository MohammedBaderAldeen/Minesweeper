package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScoreBoard implements Serializable {

    private transient IntegerProperty idGame;
    private transient ObjectProperty<Date> startDate;
    private transient ObjectProperty<Date> endDate;
    int id;
    private Date gameEndDate;
	private Date gameStartDate;
	
	private ReplayGame replay;
    
	private static final long serialVersionUID = 2485712476931596158L;
	public ScoreBoard(Date gameStartDate) {
		super();
		this.gameStartDate = gameStartDate;
		this.startDate =new SimpleObjectProperty<Date>(gameStartDate);
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
		idGame= new SimpleIntegerProperty(id);
	}
	
	public List<Result> results=new ArrayList();
	
	public Date getGameEndDate() {
		return gameEndDate;
	}
	
	public void setGameEndDate(Date gameEndDate) {
		this.gameEndDate = gameEndDate;
		this.endDate =new SimpleObjectProperty<Date>(gameEndDate);
	}

	
	public IntegerProperty getIdGame() {
		return new SimpleIntegerProperty(this.id);
	}

	
	public ObjectProperty<Date> getStartDate() {
		return new SimpleObjectProperty<Date>(this.gameStartDate);
	}

	
	public ObjectProperty<Date> getEndDate() {
		return new SimpleObjectProperty<Date>(this.gameEndDate);
	}

	
	public Date getGameStartDate() {
		return gameStartDate;
	}
	public ObservableList<Result> getDetailsData() {
		ObservableList<Result> oList = FXCollections.observableArrayList(results);
		 return oList;
	}

	
	public ReplayGame getReplay() {
		return replay;
	}

	
	public void setReplay(ReplayGame replay) {
		this.replay = replay;
	}
	
	
}


