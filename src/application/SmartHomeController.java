package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
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
	// increments temperatureSet and displays "set to" temperature for three seconds before returning to current temperature TODO improve the text transition
	public void increaseTemperatureButtonPressed() {
		temperatureSet = temperatureSet + 1;
		if (timeline != null) {
			timeline.stop();
		}
		temperatureTextField.setStyle("-fx-background-color: #42c5f5;");
    	temperatureTextField.setText(String.valueOf(temperatureSet + farenheight));
		KeyFrame keyFrame = new KeyFrame(
		        Duration.seconds(3),
		        event -> {
		        	temperatureTextField.setStyle("-fx-background-color: white;");
		        	temperatureTextField.setText(String.valueOf(temperatureCurrent + farenheight));
		        } 
		    );
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
		//TODO tell ac unit to do work
	}
	@FXML
	// decrements temperatureSet and displays "set to" temperature for three seconds before returning to current temperature TODO improve the text transition
	public void decreaseTemperatureButtonPressed() {
		temperatureSet = temperatureSet - 1;
		if (timeline != null) {
			timeline.stop();
		}
		temperatureTextField.setStyle("-fx-background-color: #42c5f5;");
    	temperatureTextField.setText(String.valueOf(temperatureSet + farenheight));
		KeyFrame keyFrame = new KeyFrame(
		        Duration.seconds(3),
		        event -> {
		        	temperatureTextField.setStyle("-fx-background-color: white;");
		        	temperatureTextField.setText(String.valueOf(temperatureCurrent + farenheight));
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		temperatureCurrent = 72;
		temperatureSet = temperatureCurrent;
		temperatureTextField.setText(String.valueOf(temperatureCurrent + farenheight));
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
					((Circle) node).fillProperty().setValue(Paint.valueOf("Yellow"));
				}
			}
			allLightsButton.setText("All Lights Off");
			quickStatusField.appendText("\n" + "All Lights Powered On");
		}
		
		else {
			for (Node node : lightingOverlay.getChildren()) {
				if (node instanceof Circle) {
					((Circle) node).fillProperty().setValue(Paint.valueOf("Red"));
				}
			}
			allLightsButton.setText("All Lights On");
			quickStatusField.appendText("\n" + "All Lights Powered Off");
		}	
	}
}
