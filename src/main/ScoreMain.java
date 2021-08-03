package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controllers.ScoreDetailsController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScoreMain extends Application {

	Stage stage;
	protected Pane root;
	
	public List<ScoreBoard> allResult = new ArrayList(); 
	public ObservableList<ScoreBoard> getResultData() {
		ObservableList<ScoreBoard> oList = FXCollections.observableArrayList(allResult);
		 return oList;
	}
       
	public ScoreMain() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void start(Stage arg0) throws Exception {
		
		
	}
	public void start(Pane parent) {
		this.stage = new Stage();
		this.root = parent;
		showScoreview();
		this.stage.setScene(new Scene(parent));
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.show();
		
	}
	public void showScoreview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainGame.class.getResource("/views/ScoreDetails.fxml"));
            AnchorPane scoreView = (AnchorPane) loader.load();
            scoreView.setTranslateX(0);
            scoreView.setTranslateY(0);
            
            
            ScoreDetailsController controller = loader.getController();
            controller.setMainapp(this);
            
            this.root.getChildren().add(scoreView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
