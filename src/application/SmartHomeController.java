package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SmartHomeController implements Initializable{
	private int temperature;
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
	private Pane pane;
	@FXML
	private ImageView imageView;
		
	@FXML
	public void increaseTemperatureButtonPressed() {
		temperature = temperature + 1;
		temperatureTextField.setText(String.valueOf(temperature));
		//tell ac unit to do work
	}
	@FXML
	public void decreaseTemperatureButtonPressed() {
		temperature = temperature - 1;
		temperatureTextField.setText(String.valueOf(temperature));
		//tell ac unit to do work
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
		temperature = 72;
		temperatureTextField.setText(String.valueOf(temperature));
		setImageView(openImage("house.png"));
		pane.getChildren().add(imageView);
		
	}
}
