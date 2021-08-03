package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import main.ComputerGame;
import main.Game;
import main.GameReplay;
import main.PlayerGame;
import main.ReplayGame;
import main.ScoreBoard;
import main.ScoreMain;
import main.savedGame;
import models.Player;
import settings.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    CheckBox custom;
    @FXML
    CheckBox ComputerShield;

    @FXML
    TextField width;
    @FXML
    TextField height;
    @FXML
    TextField bombs;
    @FXML
    TextField playersCount;
    @FXML
    TextField shieldCount;
    @FXML
    TextField shieldInit;
    @FXML
    TextField PlayTime;
    @FXML
    TextField MovingShield;
    @FXML
    RadioButton singleMode;
    @FXML
    RadioButton multiPlayerMode;
    @FXML
    RadioButton vsComputerMode;
    @FXML
    CheckBox record;
    @FXML
    TextField TextNmaes;
    @FXML
    CheckBox CCC;

    private ToggleGroup toggleGroup = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initRadioButtons();
        this.initSettingsValues();
    }

    public void onPlayButtonClicked() {
        Settings.update(Integer.parseInt(this.width.getText()), Integer.parseInt(this.height.getText()),
                Integer.parseInt(this.playersCount.getText()), Integer.parseInt(this.bombs.getText()), Integer.parseInt(this.shieldCount.getText()), Integer.parseInt(this.shieldInit.getText()), Integer.parseInt(this.PlayTime.getText()), Integer.parseInt(this.MovingShield.getText()),this.ComputerShield.isSelected(),this.record.isSelected(),this.TextNmaes.getText(),this.CCC.isSelected());

        Game game = (this.toggleGroup.getSelectedToggle().getUserData().equals("vsComputerMode"))
                ? new ComputerGame()
                : new PlayerGame();
        game.start(this.createMainContent());
    }
    public void onLoadButtonClicked() {
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Save Files", "*.save");
    	fileChooser.getExtensionFilters().add(extFilter);
		File dest = fileChooser.showOpenDialog(null);
		if (dest != null) {
			savedGame gameToLoad = (savedGame) ReadObjectFromFile(dest.getAbsolutePath());
			if(gameToLoad!=null) {
				Settings.update(gameToLoad.X_CELLS, gameToLoad.Y_CELLS,gameToLoad.PLAYERS_COUNT, gameToLoad.BOMBS_COUNT,gameToLoad.SHIELDS_COUNT,gameToLoad.SHIELDS_INIT,gameToLoad.PLAY_TIME, gameToLoad.SUPER_SHIELDS_COUNT,gameToLoad.computerShield,gameToLoad.saveRecord,gameToLoad.names,gameToLoad.autoSave);
				boolean comGame =false;
				for(Player player:gameToLoad.players) {
					if(player.getName()=="Computer") comGame =true;
				}
				 Game game = (comGame)
			                ? new ComputerGame()
			                : new PlayerGame();
			     game.start(this.createMainContent(),gameToLoad);
			}
		}        
    }
    public void onRecetButtonClicked() {
		File dest = new File(Settings.RecentSAVEPath+"recent.save");
		if (dest.exists()) {
			savedGame gameToLoad = (savedGame) ReadObjectFromFile(dest.getAbsolutePath());
			if(gameToLoad!=null) {
				Settings.update(gameToLoad.X_CELLS, gameToLoad.Y_CELLS,gameToLoad.PLAYERS_COUNT, gameToLoad.BOMBS_COUNT,gameToLoad.SHIELDS_COUNT,gameToLoad.SHIELDS_INIT,gameToLoad.PLAY_TIME, gameToLoad.SUPER_SHIELDS_COUNT,gameToLoad.computerShield,gameToLoad.saveRecord,gameToLoad.names,true);
				boolean comGame =false;
				for(Player player:gameToLoad.players) {
					if(player.getName()=="Computer") comGame =true;
				}
				 Game game = (comGame)
			                ? new ComputerGame()
			                : new PlayerGame();
			     game.start(this.createMainContent(),gameToLoad);
			}
		}        
    }
    public void onReplayButtonClicked() {
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Replay Files", "*.replay");
		 fileChooser.getExtensionFilters().add(extFilter);
		File dest = fileChooser.showOpenDialog(null);
		if (dest != null) {
			ReplayGame gameToLoad = (ReplayGame) ReadObjectFromFile(dest.getAbsolutePath());
			if(gameToLoad!=null) {
				Settings.update(gameToLoad.getGameInit().X_CELLS, gameToLoad.getGameInit().Y_CELLS,gameToLoad.getGameInit().PLAYERS_COUNT, gameToLoad.getGameInit().BOMBS_COUNT,gameToLoad.getGameInit().SHIELDS_COUNT,gameToLoad.getGameInit().SHIELDS_INIT,gameToLoad.getGameInit().PLAY_TIME, gameToLoad.getGameInit().SUPER_SHIELDS_COUNT,gameToLoad.getGameInit().computerShield,false,gameToLoad.getGameInit().names,false);
				GameReplay game = new GameReplay();
			     game.start(this.createMainContent(),gameToLoad);
			}
		}        
    }
   
    public void onScoreButtonClicked() {
    	List<ScoreBoard> allresult= (List<ScoreBoard>)ReadObjectFromFile(Settings.SAVEPath);
    	if(allresult!=null) {
    		ScoreMain score = new ScoreMain();  
    		score.allResult=allresult;
    		score.start(this.createMainContent2());
    	}
    	
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

    /**
     * Main options pane
     * @return Pane
     */
    private Pane createMainContent() {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #7f7f7f;");
        root.setPrefSize(Settings.WIDTH, Settings.HEIGHT + (3* Settings.CELL_SIZE));       
        return root;
    }
    private Pane createMainContent2() {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #7f7f7f;");
        //root.setPrefSize(800, 700);
        return root;
    }

    private void initRadioButtons() {
        this.singleMode.setUserData("singleMode");
        this.singleMode.setToggleGroup(this.toggleGroup);
        this.multiPlayerMode.setUserData("multiPlayerMode");
        this.multiPlayerMode.setToggleGroup(this.toggleGroup);
        this.vsComputerMode.setUserData("vsComputerMode");
        this.vsComputerMode.setToggleGroup(this.toggleGroup);

        // Enable/Disable player count field
        this.toggleGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            boolean isMultiPlayer = newValue.getUserData().equals("multiPlayerMode");
            this.playersCount.setDisable(!isMultiPlayer);
        }));
    }

    /**
     * Toggle (enable/disable) fields on custom checkbox clicked
     */
    public void customOnAction() {
        this.toggleFields(!this.custom.isSelected());
    }

    private void toggleFields(boolean selected) {
        this.initSettingsValues();
        this.width.setDisable(selected);
        this.height.setDisable(selected);
        this.bombs.setDisable(selected);
        this.shieldCount.setDisable(selected);
        this.shieldInit.setDisable(selected);
        this.PlayTime.setDisable(selected);
        this.MovingShield.setDisable(selected);
        this.ComputerShield.setDisable(selected);
        this.record.setDisable(selected);
        this.TextNmaes.setDisable(selected);
    }

    /**
     * Set width & height with settings values
     */
    private void initSettingsValues() {
        this.width.setText(String.valueOf(Settings.X_CELLS));
        this.height.setText(String.valueOf(Settings.Y_CELLS));
        this.bombs.setText(String.valueOf(Settings.BOMBS_COUNT));
        this.playersCount.setText(String.valueOf(Settings.PLAYERS_COUNT));
        this.shieldCount.setText(String.valueOf(Settings.SHIELDS_COUNT));
        this.shieldInit.setText(String.valueOf(Settings.SHIELDS_INIT));
        this.PlayTime.setText(String.valueOf(Settings.PLAY_TIME));
        this.MovingShield.setText(String.valueOf(Settings.SUPER_SHIELDS_COUNT));
        this.TextNmaes.setText(Settings.PlayerNames);
    }
}
