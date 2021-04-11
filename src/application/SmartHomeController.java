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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
	private Pane lightingOverlay;
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
	private Rectangle livingroom_TV;
	@FXML
	private Circle lamp_Livinga;
	@FXML
	private Circle overheadLight_LR;
	@FXML
	private Circle lamp_Livingb;
	
	
		
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
	public void toggleLightingPower (MouseEvent event) {
		Circle itemClicked = (Circle) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		String onColor = "Red";
		String offColor = "Yellow";
		if (currentColor == Paint.valueOf(offColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(onColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " power toggled");
		} else if (currentColor == Paint.valueOf(onColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(offColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " power toggled");
		}
	}

	@FXML
	public void allLightsButtonPressed() {
		// If button is "All Lights On", turn all lights on and set button to "All Lights off"
		if (allLightsButton.getText().equals("All Lights On")){
			for (Node node : lightingOverlay.getChildren()) {
				if (node instanceof Circle) {
					((Circle) node).fillProperty().setValue(Paint.valueOf("Red"));
				}
			}
			allLightsButton.setText("All Lights Off");
			quickStatusField.appendText("\n" + "All Lights Powered On");
		}
		
		else {
			for (Node node : lightingOverlay.getChildren()) {
				if (node instanceof Circle) {
					((Circle) node).fillProperty().setValue(Paint.valueOf("Yellow"));
				}
			}
			allLightsButton.setText("All Lights On");
			quickStatusField.appendText("\n" + "All Lights Powered Off");
		}
	}
	
	@FXML
	public void allDoorsLockedButtonPressed() {
		String onColor = "DarkGreen";
		String offColor = "Red";
		if (allDoorsLockedButton.getText().equals("All Doors Locked")) {
			door_toGarage.fillProperty().setValue(Paint.valueOf(onColor));
			door_front.fillProperty().setValue(Paint.valueOf(onColor));
			door_back.fillProperty().setValue(Paint.valueOf(onColor));
			allDoorsLockedButton.setText("All Doors Unlocked");
			quickStatusField.appendText("\n" + "All Doors Locked");
		}
		else {
			door_toGarage.fillProperty().setValue(Paint.valueOf(offColor));
			door_front.fillProperty().setValue(Paint.valueOf(offColor));
			door_back.fillProperty().setValue(Paint.valueOf(offColor));
			quickStatusField.appendText("\n" + "All Doors Unlocked");
			allDoorsLockedButton.setText("All Doors Locked");
		}
	}
	
	@FXML
	public void garageDoorOpenButtonPressed() {
		String onColor = "DarkGreen";
		String offColor = "Red";
		if (garageDoorOpenButton.getText().equals("Garage Door Open")) {
			door_garage_1.fillProperty().setValue(Paint.valueOf(offColor));
			door_garage_2.fillProperty().setValue(Paint.valueOf(offColor));
			garageDoorOpenButton.setText("Garage Door Close");
			quickStatusField.appendText("\n" + "Garage doors opened");
		}
		else {
			door_garage_1.fillProperty().setValue(Paint.valueOf(onColor));
			door_garage_2.fillProperty().setValue(Paint.valueOf(onColor));
			quickStatusField.appendText("\n" + "Garage doors closed");
			garageDoorOpenButton.setText("Garage Door Open");
		}
		
		
	}
	
	@FXML
	public void entertainmentOnButtonPressed() {
		String tvOnColor = "Red";
		String tvOffColor = "DodgerBlue";
		String onColor = "Red";
		String offColor = "Yellow";
		if (entertainmentOnButton.getText().equals("Entertainment On")) {
			livingroom_TV.fillProperty().setValue(Paint.valueOf(tvOnColor));
			lamp_Livinga.fillProperty().setValue(Paint.valueOf(onColor));
			overheadLight_LR.fillProperty().setValue(Paint.valueOf(onColor));
			lamp_Livingb.fillProperty().setValue(Paint.valueOf(onColor));
			entertainmentOnButton.setText("Entertainment Off");
			quickStatusField.appendText("\n" + "Entertainment On");
		}
		else {
			livingroom_TV.fillProperty().setValue(Paint.valueOf(tvOffColor));
			lamp_Livinga.fillProperty().setValue(Paint.valueOf(offColor));
			overheadLight_LR.fillProperty().setValue(Paint.valueOf(offColor));
			lamp_Livingb.fillProperty().setValue(Paint.valueOf(offColor));
			quickStatusField.appendText("\n" + "Entertainment Off");
			entertainmentOnButton.setText("Entertainment On");
		}
	}
	
	@FXML
	public void toggleDoor (MouseEvent event) {
		Rectangle itemClicked = (Rectangle) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		String onColor = "DarkGreen";
		String offColor = "Red";
		if (itemClicked.getId().toLowerCase().startsWith("garage door")) {
			if (currentColor == Paint.valueOf(offColor)) {
				itemClicked.fillProperty().setValue(Paint.valueOf(onColor));
				quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " closed");
			} else if (currentColor == Paint.valueOf(onColor)) {
				itemClicked.fillProperty().setValue(Paint.valueOf(offColor));
				quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " opened");
			}
		}
		else if (itemClicked.getId().toLowerCase().contains("door")) {
			if (currentColor == Paint.valueOf(offColor)) {
				itemClicked.fillProperty().setValue(Paint.valueOf(onColor));
				quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " locked");
			} else if (currentColor == Paint.valueOf(onColor)) {
				itemClicked.fillProperty().setValue(Paint.valueOf(offColor));
				quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " unlocked");
			}
		}
	}
	
	@FXML
	public void toggleAppliancePower (MouseEvent event) {
		Rectangle itemClicked = (Rectangle) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		String onColor = "Red";
		String offColor = "DodgerBlue";
		if (currentColor == Paint.valueOf(offColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(onColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " power toggled");
		} else if (currentColor == Paint.valueOf(onColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(offColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " power toggled");
		}
	}
	
	@FXML
	public void toggleWindows (MouseEvent event) {
		Rectangle itemClicked = (Rectangle) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		String onColor = "Red";
		String offColor = "BlueViolet";
		if (currentColor == Paint.valueOf(offColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(onColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " sensor open");
		} else if (currentColor == Paint.valueOf(onColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(offColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " sensor closed");
		}
	}
	
	@FXML
	public void toggleWater (MouseEvent event) {
		Polygon itemClicked = (Polygon) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		String onColor = "Red";
		String offColor = "Aqua";
		if (currentColor == Paint.valueOf(offColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(onColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " flowing");
		} else if (currentColor == Paint.valueOf(onColor)) {
			itemClicked.fillProperty().setValue(Paint.valueOf(offColor));
			quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " not flowing");
		}
	}
}
