package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.geom.Shape;
import com.sun.jdi.event.Event;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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

public class SmartHomeController<quickStatusField> implements Initializable{
	private int temperatureCurrent;
	private int temperatureSet;
	private int temperatureOutside;
	private Scene secondScene;
	private Scene thirdScene;
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
	// increments temperatureSet and displays "set to" temperature for three seconds before returning to current temperature TODO improve the text transition
	public void increaseTemperatureButtonPressed() {
		temperatureSet = temperatureSet + 1;
		temperatureTextField.setText("Set to " + String.valueOf(temperatureSet));
		PauseTransition visiblePause = new PauseTransition(
		        Duration.seconds(3)
		);
		visiblePause.setOnFinished(
		        event -> temperatureTextField.setText(String.valueOf(temperatureCurrent))
		);
		visiblePause.play();
		
		//TODO tell ac unit to do work
	}
	@FXML
	// decrements temperatureSet and displays "set to" temperature for three seconds before returning to current temperature TODO improve the text transition
	public void decreaseTemperatureButtonPressed() {
		temperatureSet = temperatureSet - 1;
		temperatureTextField.setText("Set to " + String.valueOf(temperatureSet));
		PauseTransition visiblePause = new PauseTransition(
		        Duration.seconds(3)
		);
		visiblePause.setOnFinished(
		        event -> temperatureTextField.setText(String.valueOf(temperatureCurrent))
		);
		visiblePause.play();
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
		temperatureTextField.setText(String.valueOf(temperatureCurrent));
		// TODO this house floor plan is a placeholder and not the actual house
//		setImageView(openImage("house.png"));
//		pane.getChildren().add(imageView);
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
}
