package application;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class SmartHomeDiagnosticsController implements Initializable{
	private Scene firstScene;
	private Scene secondScene;
	
	@FXML
	private Button HomeButton;
	@FXML
	private Button UsageButton;
	@FXML
    private Button simulatewashingButton;
    @FXML
    private Button simulateshowerButton;
	@FXML
    private TextArea simulationField;
	@FXML
	private ChoiceBox<String> lengthOfSimulationChoiceBox;

	public SmartHomeController HC = new SmartHomeController();


	ObservableList<String> lengthsOfSimulation = FXCollections.observableArrayList("5 minutes", "10 minutes", "15 minutes", "20 minutes");
	
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
	
	// Calculates the shower event and outputs the results
	@FXML
    public void simulateshowerButtonPressed(ActionEvent event) {
		simulationField.clear();
		simulationField.appendText("\n Calculating shower event...");
		
		// Shower simulation
//		UC.toggleOn(UC.app_livingroom_TV.getClass());
//		HC.toggleTEST("Master Bedroom Overhead Lamp", 1);
		
		
		// Shower calculations
		List<Double> totals = simulationCalculation(0, 25, .33, 0.65);
		totals = roundingData(totals);
		
		// TextArea output
		String watts = "\n Calculated kilowatts used: ";
		String gallons = "\n Calculated gallons used: ";
		String cost = "\n Calculated overall cost: ";
		String addon = Double.toString(totals.get(0));
		simulationField.appendText(watts+addon);
		addon = Double.toString(totals.get(1));
		simulationField.appendText(gallons+addon);
		addon = Double.toString(totals.get(2));
		simulationField.appendText(cost+addon);
    }
	
	// Calculates the dishwasher event and outputs the results
    @FXML
    void simulatewashingButtonPressed(ActionEvent event) {
    	simulationField.clear();
		simulationField.appendText("\n Calculating washing event...");
		
		// Washing with sink simulation
		List<Double> totals = simulationCalculation(0, 25, .083, 1);
		double sinkw = totals.get(0); 
		double sinkg = totals.get(1);
		double sinkc = totals.get(2);
		
		// Washing with dishwasher simulation
		totals = simulationCalculation(1800, 6, .75, 1);
		totals.set(0, totals.get(0)+sinkw);
		totals.set(1, totals.get(1)+sinkg);
		totals.set(2, totals.get(2)+sinkc);
		totals = roundingData(totals);
		
		// TextArea output
		String watts = "\n Calculated kilowatts used: ";
		String gallons = "\n Calculated gallons used: ";
		String cost = "\n Calculated overall cost: ";
		String addon = Double.toString(totals.get(0));
		simulationField.appendText(watts+addon);
		addon = Double.toString(totals.get(1));
		simulationField.appendText(gallons+addon);
		addon = Double.toString(totals.get(2));
		simulationField.appendText(cost+addon);
    }
	
    // Calculates for any given simulation
    public List<Double> simulationCalculation(int watts, int gallons, double time, double hotpercent) {
		List<Double> totals = Arrays.asList(0.0, 0.0, 0.0);
		double w = 0.0;
		double g = gallons;
		double c = 0.0;
		
		// watts calculation
		if (watts != 0) {
			w = w + ((watts*time)/1000);
		}
		
		// hot water heater calculation
		if (hotpercent != 0) {
			w = w + (((4000*hotpercent) * (4*gallons)/60)/1000);
		}
		
		// cost calculation combining the wattage and water cost
		c = c + (0.12 * w);
		c = c + (2.52 * (g/748));
		totals.set(0, w);
		totals.set(1, g);
		totals.set(2, c);
		
		return totals;
    }
	
    public List<Double> roundingData(List<Double> totals) {
		int z = 0;
		double setas;
		while (z <= 2) {
			setas = totals.get(z);
			setas = Math.round(setas*100.0)/100.0;
			totals.set(z, setas);
			z++;
		}
		return totals;
	}
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.lengthOfSimulationChoiceBox.getItems().removeAll(lengthOfSimulationChoiceBox.getItems());
		this.lengthOfSimulationChoiceBox.getItems().addAll(lengthsOfSimulation);
		this.lengthOfSimulationChoiceBox.getSelectionModel().select("5 minutes");
	}

	
	public void toggle (MouseEvent event) {
		Object toggleID = event.getSource();
		System.out.println(toggleID);
	}
}
