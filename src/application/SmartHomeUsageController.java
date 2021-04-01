package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class SmartHomeUsageController implements Initializable{
	
	private Scene firstScene;
	private Scene thirdScene;
	@FXML
	private Button homeButton;
	@FXML
	private Button diagnosticsButton;
	@FXML
	private ChoiceBox<String> monthChoiceBox;
	@FXML
	private Button selectMonthButton;
	
	ObservableList<String> monthList = FXCollections.observableArrayList("February", "March", "April");
	
	public void setHomeScene(Scene scene) {
		firstScene = scene;
		
	}

	public void setDiagnosticsScene(Scene scene) {
		thirdScene = scene;
		
	}

	// event listener for home button that sets the scene to home page
	public void homeButtonPressed(ActionEvent actionEvent) {
		Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		primaryStage.setScene(firstScene);
	}
	
	// event listener for diagnostics button that sets the scene to diagnostics page
	public void diagnosticsButtonPressed(ActionEvent actionEvent) {
		Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		primaryStage.setScene(thirdScene);
	}
	
	
	// display the graph with the electricity and water usage
	public void selectMonthButtonPressed(ActionEvent actionEvent) {
		String month = monthChoiceBox.getValue();
		
		if (month == null) {
			System.out.println("Please select a month");
		} else if (month == "April") {
			// generate graph for April
			System.out.println("April was selected");
		} else if (month == "March") {
			// generate graph for March
			System.out.println("March was selected");
		} else {
			// month would be February
			// generate graph for February
			System.out.println("February was selected");
		}
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		// setting default month to April
		monthChoiceBox.setValue("April");
		monthChoiceBox.setItems(monthList);
	}

}
