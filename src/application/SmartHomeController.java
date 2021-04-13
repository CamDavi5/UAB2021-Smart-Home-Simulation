package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SmartHomeController implements Initializable{
	private int temperatureCurrent;
	private int temperatureSet;
	private int temperatureOutside;
	private Scene secondScene;
	private Scene thirdScene;
	private Timeline timeline = new Timeline();
	private String farenheight = "°F";
	@FXML
	private Button increaseTemperatureButton;
	@FXML
	private Button decreaseTemperatureButton;
	@FXML
	private Button homeButton;
	@FXML
	private Button diagnosticsButton;
	@FXML
	private Button usageButton;
	@FXML
	private Button allLightsButton;
	@FXML
	private Button allDoorsLockedButton;
	@FXML
	private Button garageDoorOpenButton;
	@FXML
	private Button entertainmentOnButton;
	@FXML
	private TextField temperatureTextField;
	@FXML
	private TextField temperatureOutsideTextField;
	@FXML
	private Pane pane;
	@FXML
	private ImageView imageView;
	@FXML
	private TextArea quickStatusField;
	@FXML
	public Pane lightingOverlay;
	@FXML
	private Rectangle door_toGarage;
	@FXML
	private Rectangle door_front;
	@FXML
	private Rectangle door_garage_1;
	@FXML
	private Rectangle door_garage_2;
	@FXML
	private Rectangle door_back;
	@FXML
	private Label insideLabel;
	@FXML
	public Rectangle app_livingroom_TV;
	@FXML
	public Circle lamp_Livinga;
	@FXML
	public Circle overheadLight_LR;
	@FXML
	public Circle lamp_Livingb;
	
	private double dailyElectricUsage;
	private double dailyWaterUsage;
	private double dailyOverallCost;
	
	@FXML
	// increments temperatureSet and displays "set to" temperature for three seconds before returning to current temperature
	public void increaseTemperatureButtonPressed() {
		temperatureSet = temperatureSet + 1;
		if (timeline != null) {
			timeline.stop();
		}
		temperatureTextField.setStyle("-fx-background-color: #42c5f5;");
    	temperatureTextField.setText(String.valueOf(temperatureSet + farenheight));
    	insideLabel.setText("Set To");
		KeyFrame keyFrame = new KeyFrame(
		        Duration.seconds(3),
		        event -> {
		        	temperatureTextField.setStyle("-fx-background-color: white;");
		        	temperatureTextField.setText(String.valueOf(temperatureCurrent + farenheight));
		        	insideLabel.setText("Inside");
		        } 
		    );
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
		//TODO tell ac unit to do work
	}
	@FXML
	// decrements temperatureSet and displays "set to" temperature for three seconds before returning to current temperature
	public void decreaseTemperatureButtonPressed() {
		temperatureSet = temperatureSet - 1;
		if (timeline != null) {
			timeline.stop();
		}
		temperatureTextField.setStyle("-fx-background-color: #42c5f5;");
    	temperatureTextField.setText(String.valueOf(temperatureSet + farenheight));
    	insideLabel.setText("Set To");
		KeyFrame keyFrame = new KeyFrame(
		        Duration.seconds(3),
		        event -> {
		        	temperatureTextField.setStyle("-fx-background-color: white;");
		        	temperatureTextField.setText(String.valueOf(temperatureCurrent + farenheight));
		        	insideLabel.setText("Inside");
		        } 
		    );
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
		//tell ac unit to do work
	}
	
	// sets the usage scene
	public void setUsageScene(Scene scene) {
		secondScene = scene;
	}
	
	// sets the diagnostics scene
	public void setDiagnosticsScene(Scene scene) {
		thirdScene = scene;
	}
	
	@FXML
	// event listener for usage button that sets the scene to usage page
	public void usageButtonPressed(ActionEvent actionEvent) {
		Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		primaryStage.setScene(secondScene);
	}
	
	@FXML
	// event listener for diagnostics button that sets the scene to diagnostics page
	public void diagnosticsButtonPressed(ActionEvent actionEvent) {
		Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		primaryStage.setScene(thirdScene);
		actionEvent.consume();
	}
	
	// Creates an Image object from a String url
	public Image openImage(String url) {
		Image image = null;
		try {
			image = new Image(new FileInputStream(url));
		} catch (FileNotFoundException e) {
			System.out.println("Background image not found!");
			e.printStackTrace();
		}
		return image;
	  }
	
	// sets the background image centered and scaled to the Pane
	public void setImageView(Image image) {
		imageView = new ImageView();
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		imageView.fitHeightProperty().bind(pane.heightProperty());
		imageView.fitWidthProperty().bind(pane.widthProperty());
	}
	
	public void setExternalTemp() throws SQLException {
		String sqlQuery = "SELECT weather.datetime, weather.temp FROM weather ORDER BY weather.datetime DESC LIMIT 1;";
		Statement s = Main.c.createStatement();
		ResultSet queryResult = s.executeQuery(sqlQuery);
		queryResult.next();
		temperatureOutside = queryResult.getInt("temp");
		temperatureOutsideTextField.setText(String.valueOf(temperatureOutside + farenheight));
		queryResult.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		temperatureCurrent = 72;
		temperatureSet = temperatureCurrent;
		temperatureTextField.setText(String.valueOf(temperatureCurrent + farenheight));
		
		try {
			setExternalTemp();
		} catch (SQLException e) {
			System.out.println("Error setting outside temp");
		}
	}
	
	@FXML
	public void allLightsButtonPressed() {
		// If button is "All Lights On", turn all lights on and set button to "All Lights off"
		if (allLightsButton.getText().equals("All Lights On")){
			for (Node node : lightingOverlay.getChildren()) {
				if (node instanceof Circle) {
					((Circle) node).setFill(Color.RED);
				}
			}
			allLightsButton.setText("All Lights Off");
			quickStatusField.appendText("\n" + "All Lights On");
		} else {
			for (Node node : lightingOverlay.getChildren()) {
				if (node instanceof Circle) {
					((Circle) node).setFill(Color.YELLOW);
				}
			}
			allLightsButton.setText("All Lights On");
			quickStatusField.appendText("\n" + "All Lights Off");
		}
	}
	
	@FXML
	public void allDoorsLockedButtonPressed() {
		if (allDoorsLockedButton.getText().equals("All Doors Locked")) {
			door_toGarage.setFill(Color.RED);
			door_front.setFill(Color.RED);
			door_back.setFill(Color.RED);
			allDoorsLockedButton.setText("All Doors Unlocked");
			quickStatusField.appendText("\n" + "All Doors Locked");
		} else {
			door_toGarage.setFill(Color.DARKGREEN);
			door_front.setFill(Color.DARKGREEN);
			door_back.setFill(Color.DARKGREEN);
			quickStatusField.appendText("\n" + "All Doors Unlocked");
			allDoorsLockedButton.setText("All Doors Locked");
		}
	}
	
	@FXML
	public void garageDoorOpenButtonPressed() {
		Paint onColor = Color.DARKGREEN;
		Paint offColor = Color.RED;
		if (garageDoorOpenButton.getText().equals("Garage Door Open")) {
			door_garage_1.setFill(offColor);
			door_garage_2.setFill(offColor);
			garageDoorOpenButton.setText("Garage Door Close");
			quickStatusField.appendText("\n" + "Garage doors opened");
		}
		else {
			door_garage_1.setFill(onColor);
			door_garage_2.setFill(onColor);
			quickStatusField.appendText("\n" + "Garage doors closed");
			garageDoorOpenButton.setText("Garage Door Open");
		}
		
		
	}
	
	@FXML
	public void entertainmentOnButtonPressed() {
		Paint onColor = Color.RED;
		Paint tvOffColor = Color.DODGERBLUE;
		Paint offColor = Color.YELLOW;
		if (entertainmentOnButton.getText().equals("Entertainment On")) {
			app_livingroom_TV.setFill(onColor);
			lamp_Livinga.setFill(onColor);
			overheadLight_LR.setFill(onColor);
			lamp_Livingb.setFill(onColor);
			entertainmentOnButton.setText("Entertainment Off");
			quickStatusField.appendText("\n" + "Entertainment On");
		}
		else {
			app_livingroom_TV.setFill(tvOffColor);
			lamp_Livinga.setFill(offColor);
			overheadLight_LR.setFill(offColor);
			lamp_Livingb.setFill(offColor);
			quickStatusField.appendText("\n" + "Entertainment Off");
			entertainmentOnButton.setText("Entertainment On");
		}
	}
	
	// When an item is clicked on Home Screen
	public void itemClicked (MouseEvent event) {
		Shape itemClicked = (Shape) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		if (currentColor == Paint.valueOf("RED")) {
			toggleOff(itemClicked);
		} else {
		toggleOn(itemClicked);
		
		}
	}


	void toggleTEST(String id, int toggle) {
		Shape item = null;
		System.out.println(id);
		System.out.println(toggle);

		for (Node node : lightingOverlay.getChildren()) {
			System.out.println(node);
			if (node.getId() == id) {
				item = (Shape) node;
				System.out.println(item);
			} else {
				System.out.println ("No Nodes here by that name.");
			}
		if (toggle == 1) {
			toggleOn(item);
		} else if (toggle == 2) {
			toggleOff(item);
		}
		}
	}
	
	// Toggles an item on
	public void toggleOn (Shape itemClicked) {
		System.out.println(itemClicked);
		if (itemClicked.getClass().getTypeName().endsWith("Circle")) {
			changeColorAndMessage(itemClicked, Color.RED, "on.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Rectangle") && itemClicked.getId().contains("App")) {
			changeColorAndMessage(itemClicked, Color.RED, "on.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Rectangle") && itemClicked.getId().contains("Window")) {
			changeColorAndMessage(itemClicked, Color.RED, "open.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Rectangle") && itemClicked.getId().contains("Door")) {
			changeColorAndMessage(itemClicked, Color.RED, "open.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Circle") && itemClicked.getId().contains("exhaustFan")) {
			changeColorAndMessage(itemClicked, Color.RED, "on.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Polygon")) {
			changeColorAndMessage(itemClicked, Color.RED, "flowing.");
		} else {
			System.out.println ("OOPS! No such indicator to toggle on.");
		}
	}
	
	// toggles an item off
	public void toggleOff (Shape itemClicked) {
		if (itemClicked.getClass().getTypeName().endsWith("Circle")) {
			changeColorAndMessage (itemClicked, Color.YELLOW, "off.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Rectangle") && itemClicked.getId().contains("App")) {
			changeColorAndMessage(itemClicked, Color.DODGERBLUE, "off.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Rectangle") && itemClicked.getId().contains("Window")) {
			changeColorAndMessage(itemClicked, Color.BLUEVIOLET, "closed.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Rectangle") && itemClicked.getId().contains("Door")) {
			changeColorAndMessage(itemClicked, Color.DARKGREEN, "closed.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Circle") && itemClicked.getId().contains("exhaustFan")) {
			changeColorAndMessage(itemClicked, Color.YELLOW, "off.");
		} else if (itemClicked.getClass().getTypeName().endsWith("Polygon")) {
			changeColorAndMessage(itemClicked, Color.AQUA, "not flowing.");
		} else {
			System.out.println ("OOPS! No such indicator to toggle off.");
		}
	}
	
	// changes the color and the status message
	public void changeColorAndMessage (Shape itemClicked, Paint color, String text) {
		itemClicked.setFill(color);
		quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " " + text);
	}
}
