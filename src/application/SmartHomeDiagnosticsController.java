package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SmartHomeDiagnosticsController implements Initializable{
	private Scene firstScene;
	private Scene secondScene;
	@FXML
	private Button HomeButton;
	@FXML
	private Button UsageButton;
	
	public void setHomeScene(Scene scene) {
		firstScene = scene;
		
	}

	public void setUsageScene(Scene scene) {
		secondScene = scene;
		
	}

	// event listener for home button that sets the scene to home page
	public void homeButtonPressed(ActionEvent actionEvent) {
		Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		primaryStage.setScene(firstScene);
	}
	
	// event listener for usage button that sets the scene to usage page
	public void usageButtonPressed(ActionEvent actionEvent) {
		Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		primaryStage.setScene(secondScene);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	
	public void toggle (MouseEvent event) {
		Object toggleID = event.getSource();
		System.out.println(toggleID);
	}
}
