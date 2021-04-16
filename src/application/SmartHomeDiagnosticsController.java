package application;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class SmartHomeDiagnosticsController implements Initializable{
	private Scene firstScene;
	private Scene secondScene;
	private SmartHomeController homeController;
	
	@FXML
	private Button HomeButton;
	@FXML
	private Button UsageButton;
	@FXML
    private ToggleButton simulatewashingButton;
    @FXML
    private ToggleButton simulateshowerButton;
	@FXML
    private TextArea simulationField;
	@FXML
	private TextField lengthOfSimulationField;
	
	public Double timeToSimulate = 0.0;


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
	
	@FXML
	public void simulationMinutesUpdate() {
		if (lengthOfSimulationField.getText().isEmpty() == true) {
			this.lengthOfSimulationField.setText("1");
		} else {
			this.timeToSimulate = Double.parseDouble(lengthOfSimulationField.getText());
		}
	}
	
	// pause function for waiting during simulations
	public static void pause(int milisec) {
		try {
			Thread.sleep(milisec);
		} catch (InterruptedException e) {
			System.out.println("Error sleeping");
		}
	}
	
	// Calculates the shower event and outputs the results
	@FXML
    public void simulateshowerButtonPressed(ActionEvent event) {
		new Thread() {
			public void run() {
				simulationField.clear();
		
				// disabling buttons while simulation is running
				simulateshowerButton.setDisable(true);
				simulatewashingButton.setDisable(true);
				
				simulationMinutesUpdate();
				ToggleButton buttonID = (ToggleButton) event.getSource();
		
				String simulateStart = "\n Calculating shower event for "+String.valueOf(timeToSimulate)+" minutes...";
				simulationField.appendText(simulateStart);
				
				// I just commented this out for now
				/*if (buttonID.isSelected() == true) {
					homeController.diagnosticToggle("Master Bedroom Shower", 1);
					homeController.diagnosticToggle("Master Bedroom Exhaust Fan", 1);
					homeController.diagnosticToggle("Master Bath Overhead Light", 1);
					homeController.diagnosticToggle("Appliance - Water Heater", 1);
				} else if (buttonID.isSelected() == false) {
					homeController.diagnosticToggle("Master Bedroom Shower", 2);
					homeController.diagnosticToggle("Master Bedroom Exhaust Fan", 2);
					homeController.diagnosticToggle("Master Bath Overhead Light", 2);
					homeController.diagnosticToggle("Appliance - Water Heater", 2);
				} */
				
				// turning on shower, fan, and light
				homeController.diagnosticToggle("Master Bedroom Shower", 1);
				homeController.diagnosticToggle("Master Bedroom Exhaust Fan", 1);
				homeController.diagnosticToggle("Master Bath Overhead Light", 1);
				
				// waiting while person "takes" a shower
				pause(10000);
				
				// turning off shower, fan, and light
				homeController.diagnosticToggle("Master Bedroom Shower", 2);
				homeController.diagnosticToggle("Master Bedroom Exhaust Fan", 2);
				homeController.diagnosticToggle("Master Bath Overhead Light", 2);
				
				// turning on water heater
				homeController.diagnosticToggle("Appliance - Water Heater", 1);
				
				// waiting while water heater heats more water
				pause(10000);
				
				// turning off water heater
				homeController.diagnosticToggle("Appliance - Water Heater", 2);
				
		
				// Shower (+ Water Heater) calculations
				List<Double> totals = simulationCalculation(0, 25, timeToSimulate/60, 0.65);
				double tempw = totals.get(0); 
				double tempg = totals.get(1);
				double tempc = totals.get(2);
		
				// Exhaust fan calculations
				totals = simulationCalculation(30, 0, timeToSimulate/60, 0.0);
				totals.set(0, totals.get(0)+tempw);
				totals.set(1, totals.get(1)+tempg);
				totals.set(2, totals.get(2)+tempc);
				tempw = totals.get(0); 
				tempg = totals.get(1);
				tempc = totals.get(2);
		
				// Overhead light calculations
				totals = simulationCalculation(60, 0, timeToSimulate/60, 0.0);
				totals.set(0, totals.get(0)+tempw);
				totals.set(1, totals.get(1)+tempg);
				totals.set(2, totals.get(2)+tempc);
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
		}.start();
	}
	
	// Calculates the dishwasher event and outputs the results
    @FXML
    void simulatewashingButtonPressed(ActionEvent event) {
    	new Thread() {
    		public void run() {
    			simulationField.clear();
    			
    	
    			simulationMinutesUpdate();
    			ToggleButton buttonID = (ToggleButton) event.getSource();
		
    			String simulateStart = "\n Calculating washing event for "+String.valueOf(timeToSimulate)+" minutes...";
    			simulationField.appendText(simulateStart);
    			
    			/*if (buttonID.isSelected() == true) {
    				homeController.diagnosticToggle("Appliance - Dishwasher", 1);
    				homeController.diagnosticToggle("Dishwasher Water", 1);
    				homeController.diagnosticToggle("Kitchen Overhead Light", 1);
    				homeController.diagnosticToggle("Appliance - Water Heater", 1);
    			} else if (buttonID.isSelected() == false) {
    				homeController.diagnosticToggle("Appliance - Dishwasher", 2);
    				homeController.diagnosticToggle("Dishwasher Water", 2);
    				homeController.diagnosticToggle("Kitchen Overhead Light", 2);
    				homeController.diagnosticToggle("Appliance - Water Heater", 2);
    			}*/
    			
    			// turning on dishwasher, dishwasher water, and kitchen light
    			homeController.diagnosticToggle("Appliance - Dishwasher", 1);
				homeController.diagnosticToggle("Dishwasher Water", 1);
				homeController.diagnosticToggle("Kitchen Overhead Light", 1);
				
				// waiting while dishes are washed
				pause(10000);
				
				// turning off dishwasher, dishwasher water, and kitchen light
				homeController.diagnosticToggle("Appliance - Dishwasher", 2);
				homeController.diagnosticToggle("Dishwasher Water", 2);
				homeController.diagnosticToggle("Kitchen Overhead Light", 2);
				
				// turning on water heater 
				homeController.diagnosticToggle("Appliance - Water Heater", 1);
				
				// waiting while water heater heats water
				pause(10000);
				
				// turning water heater off
				homeController.diagnosticToggle("Appliance - Water Heater", 2);
		
    			// Dishwasher (+ Water Heater) calculation
    			List<Double> totals = simulationCalculation(1800, 6, timeToSimulate/60, 1.0);
    			double tempw = totals.get(0); 
    			double tempg = totals.get(1);
    			double tempc = totals.get(2);
		
		
    			// Kitchen-light calculation 
    			totals = simulationCalculation(60, 0, timeToSimulate/60, 0.0);
    			totals.set(0, totals.get(0)+tempw);
    			totals.set(1, totals.get(1)+tempg);
    			totals.set(2, totals.get(2)+tempc);
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
    	}.start();
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
    	simulationField.setEditable(false);
	}
	
	public void toggleSimulation2 (ActionEvent event) {
		simulationMinutesUpdate();
		ToggleButton buttonID = (ToggleButton) event.getSource();
		String toggleID = buttonID.getId().toString();
		
		if (buttonID.isSelected() == true) {
			homeController.diagnosticToggle(toggleID, 1);
		} else if (buttonID.isSelected() == false) {
			homeController.diagnosticToggle(toggleID, 2);
		}
		
		// TODO: Calculations
		// TODO: Have to implement this so it discerns what calculations to run based on type of device

		UsageCalculations UC = new UsageCalculations();
		
		// TODO: Go over Usage Calculations to be sure all calculate correctly
		// Sample Calls of Usage Calculations Class Functions
//		Double powerUsage = UC.lightsUsage(timeToSimulate);
//		Double waterUsage = UC.waterCubicFeetUsage(0);
//		
		// TODO: Update Simulation Results Status Window with Calculations		
		// TODO:  Need to formalize this so it outputs the same way for all items
		// Sample methods that point to status window instead of console
//		System.out.println (powerUsage.toString());
//		System.out.println (waterUsage.toString());

		
		
	}
		

	public void setHomeController(SmartHomeController smartHomeController) {
		this.homeController = smartHomeController;
	}

}
