package controllers;


import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import main.GameReplay;
import main.ReplayGame;
import main.ScoreMain;
import settings.Settings;

public class ScoreDetailsController {

	
	 @FXML
         private TableView<main.ScoreBoard> scoreTable;
	 @FXML
	 private TableColumn<main.ScoreBoard, Integer> IdColumn;
	 @FXML
	 private TableColumn<main.ScoreBoard, Date> StartDateColumn;
	 @FXML
	 private TableColumn<main.ScoreBoard, Date> endDateColumn;
	 @FXML
	 private TableView<main.Result> detailsTable;
	 @FXML
	 private TableColumn<main.Result, String> nameColumn;
	 @FXML
	 private TableColumn<main.Result, Integer> scoreColumn;
	 @FXML
	 private TableColumn<main.Result, Integer> shieldColumn;
	 
	 @FXML
	 private Button replayButtom;
	 
	 private ScoreMain mainapp;
	
	public void setMainapp(ScoreMain mainapp) {
		this.mainapp = mainapp;
		scoreTable.setItems(mainapp.getResultData());
	}
	public ScoreDetailsController() {
		
	}
	@FXML
    private void initialize() {
        // Initialize the person table with the two columns.
		IdColumn.setCellValueFactory(cellData -> cellData.getValue().getIdGame().asObject());
		StartDateColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDate());
		endDateColumn.setCellValueFactory(cellData -> cellData.getValue().getEndDate());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerName());
		scoreColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerScore().asObject());
		shieldColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerShield().asObject());
		
		scoreTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> showDetails(newValue));
    }
	private void showDetails(main.ScoreBoard res) {
		this.result =res;
	    if (res != null) {
	    	detailsTable.setItems(res.getDetailsData());
	    	
	    	if(res.getReplay()!=null)
	    	{
	    		replayButtom.setDisable(false);
	    	}
	    	else
	    		replayButtom.setDisable(true);
	    } else {
	    	detailsTable.setItems(null);
	    	replayButtom.setDisable(true);
	    }
	}
	main.ScoreBoard result;
	public void onReplayButton() {
		if(result!=null) {
			ReplayGame gameToLoad = result.getReplay();
			if(gameToLoad!=null) {
				Settings.update(gameToLoad.getGameInit().X_CELLS, gameToLoad.getGameInit().Y_CELLS,gameToLoad.getGameInit().PLAYERS_COUNT, gameToLoad.getGameInit().BOMBS_COUNT,gameToLoad.getGameInit().SHIELDS_COUNT,gameToLoad.getGameInit().SHIELDS_INIT,gameToLoad.getGameInit().PLAY_TIME, gameToLoad.getGameInit().SUPER_SHIELDS_COUNT,gameToLoad.getGameInit().computerShield,false,"",false);
				GameReplay game = new GameReplay();
			     game.start(this.createMainContent(),gameToLoad);
			}
		}
		
	}
	private Parent createMainContent() {
		Pane root = new Pane();
        root.setStyle("-fx-background-color: #7f7f7f;");
        root.setPrefSize(Settings.WIDTH, Settings.HEIGHT + (3* Settings.CELL_SIZE));
        return root;
	}
	 
}
