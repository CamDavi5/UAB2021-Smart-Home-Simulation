package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import javafx.scene.layout.StackPane;
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
	private Timeline hvacTimeline = new Timeline();
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
	private TextField temperatureSetTextField;
	@FXML
	private TextField temperatureOutsideTextField;
	@FXML
	private Pane pane;
	@FXML
	private StackPane temperaturePane;
	@FXML
	private StackPane currentPane;
	@FXML
	private StackPane setToPane;
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
	private Label outsideLabel;
	@FXML
	private Label setToLabel;
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
	private int temperatureDifference;
	private boolean tempStable;
	public ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();	
	
	// sets the usage scene
	public void setUsageScene(Scene scene) {
		secondScene = scene;
	}
	
	// sets the diagnostics scene
	public void setDiagnosticsScene(Scene scene) {
		thirdScene = scene;
	}
	
	public void setCurrentTemperature(int temperature) {
		this.temperatureCurrent = temperature;
		temperatureTextField.setText(String.valueOf(temperatureCurrent) + farenheight);
	}
	
	public void setSetTemperature(int temperature) {
		this.temperatureSet = temperature;
		temperatureSetTextField.setText(String.valueOf(temperatureSet) + farenheight);
	}

	public void setOutsideTemperature(int temperature) {
		temperatureOutside = temperature;
		temperatureOutsideTextField.setText(String.valueOf(temperatureOutside) + farenheight);
	}
	
	public int getCurrentTemperature() {
		return temperatureCurrent;
	}

	public int getSetTemperature() {
		return temperatureSet;
	}
	
	public int getOutsideTemperature() {
		return temperatureOutside;
	}
	
	public ScheduledExecutorService getScheduleTasks() {
		return ses;
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
	
	// every hour, update the outside temperature
	public void externalTempUpdater() throws SQLException {
		// schedules commands to run every hour
		ses.scheduleAtFixedRate(new Runnable() {
		    // query database for outside temp and set it
			@Override
		    public void run(){
		    	String sqlQuery = "SELECT weather.datetime, weather.temp FROM weather ORDER BY weather.datetime DESC LIMIT 1;";
				Statement s;
				try {
					s = Main.c.createStatement();
					ResultSet queryResult = s.executeQuery(sqlQuery);
					queryResult.next();
					setOutsideTemperature(queryResult.getInt("temp"));
					queryResult.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}, 0, 1, TimeUnit.HOURS);
	}
	
	// increments or decrements temperatureSet and displays "set to" temperature 
	// for three seconds before returning to inside temperature
	@FXML
	public void temperatureButtonPressed(ActionEvent buttonEvent) {
		if (buttonEvent.getSource() == increaseTemperatureButton) {
			setSetTemperature(getSetTemperature() + 1);
			currentPane.setVisible(false);
		}
		else {
			setSetTemperature(getSetTemperature() - 1);
			currentPane.setVisible(false);
		}
		// if a previous timeline exists, stop it to prevent animation from finishing
		if (timeline != null) {
			timeline.stop();
		}
		if (hvacTimeline != null) {
			hvacTimeline.stop();
		}
    	// after 3 seconds, return textField to "Inside" temperature
		KeyFrame keyFrame = new KeyFrame(
		        Duration.seconds(3),
		        event -> {
		        	currentPane.setVisible(true);
		        } 
		    );
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
		//handleSetTemperature(); TODO fix this function and then uncomment
		hvacTimeline.play();
	}
	
	/* TODO THIS DOES NOT WORK MIGHT NEED TO HANDLE IN THE DATABASE SIMILAR TO OTHER EVENTS
	private void handleSetTemperature() {
		temperatureDifference = getCurrentTemperature() - getSetTemperature();
		System.out.println(getCurrentTemperature());
		System.out.println(getSetTemperature());
		for (int i = 0; i < Math.abs(temperatureDifference); i++) {
			System.out.println(temperatureDifference);
			KeyFrame keyFrame = new KeyFrame(
					Duration.seconds(5),
					event -> {
						if (temperatureDifference < 0) {
							System.out.println("heating");
							setCurrentTemperature(getCurrentTemperature() + 1);
						}
						if (temperatureDifference > 0) {
							System.out.println("cooling");
							setCurrentTemperature(getCurrentTemperature() - 1);
						}
					});
			hvacTimeline.getKeyFrames().add(keyFrame);
		}
	}
	*/
	
	/*
	private void handleSetTemperature() {
		tempStable = false;
		temperatureDifference = getCurrentTemperature() - getSetTemperature();
		if (temperatureDifference < 0) {
			System.out.println("heating");
			temperatureDifference++;
			System.out.println(temperatureDifference);
			timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					setCurrentTemperature(getCurrentTemperature() + 1);
					updateCurrent();
				}
			};
			timer.schedule(timerTask, 10000);
		}
		else if (temperatureDifference > 0){
			System.out.println("cooling");
			temperatureDifference--;
			System.out.println(temperatureDifference);
			timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					setCurrentTemperature(getCurrentTemperature() - 1);
					updateCurrent();
				}
			};
			timer.schedule(timerTask, 10000);
		}
		else {
			tempStable = true;
		}
	}
	private void handleSetTemperature() {
		long delay = 60000;
		long period = 60000;
		temperatureDifference = getCurrentTemperature() - getSetTemperature();
		if (temperatureDifference != 0) {
			TimerTask timerTask = new TimerTask() {
				public void run() {
					if (temperatureDifference < 0) {
						System.out.println("heating");
						temperatureDifference++;
						setCurrentTemperature(getCurrentTemperature() + 1);
					}
					else if (temperatureDifference > 0){
						System.out.println("cooling");
						temperatureDifference--;
						setCurrentTemperature(getCurrentTemperature() - 1);
					}
					else {
						//TODO
					}
				}
			};
		timer.scheduleAtFixedRate(timerTask, delay, period);
		System.out.println("updating current temp");
		}
	}
	*/	
	
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
	public void itemClicked (MouseEvent event) throws SQLException {
		Shape itemClicked = (Shape) event.getSource();
		Paint currentColor = itemClicked.fillProperty().getValue();
		if (currentColor == Paint.valueOf("RED")) {
			toggleOff(itemClicked);
		} else {
		toggleOn(itemClicked);
		
		}
	}

	void diagnosticToggle(String id, int toggle) throws SQLException {
		Shape item = null;
		for (Node node : lightingOverlay.getChildren()) {
			if (node.getId().compareToIgnoreCase(id) == 0) {
				item = (Shape) node;
				if (toggle == 1) {
					toggleOn(item);
				} else if (toggle == 2) {
					toggleOff(item);
				}
			} 
		}
	}
	
	// Toggles an item on
	public void toggleOn (Shape itemClicked) throws SQLException {
		
		String startTime = getEventTime(itemClicked);
		String id = itemClicked.getId();
		
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
		
		// sql statement to check if sensor is in database already
		String sqlStatement = String.format("SELECT * FROM public.live_events WHERE event_id =\'%s\';", id);
		Statement s2 = Main.c.createStatement();
		ResultSet query = s2.executeQuery(sqlStatement);
		
		// checking if sensor is already in the event database
		if (query.next()) {
			// the sensor was already in the database, so just update the timestamp
			
			// sqlQuery to insert timestamp for toggleOn event
			String sqlQuery = String.format(
					"UPDATE public.live_events "
					+ "SET on_status = FALSE "
					+ "WHERE event_id=\'%s\';"
					+ "UPDATE public.live_events "
					+ "SET time_stamp = \'%s\' "
					+ "WHERE event_id=\'%s\';"
					+ "UPDATE public.live_events "
					+ "SET on_status = TRUE "
					+ "WHERE event_id=\'%s\';", id, startTime, id, id);
			Statement s = Main.c.createStatement();
			s.executeUpdate(sqlQuery);
		} else {
			// the sensor was not in the database, so insert sensor id and timestamp
			String sqlInsert = String.format(
					"INSERT INTO public.live_events "
					+ "VALUES (\'%s\', \'%s\', TRUE);", id, startTime);
			s2.executeUpdate(sqlInsert);
		} 
	}
	
	// toggles an item off
	public void toggleOff (Shape itemClicked) throws SQLException {
		
		String endTimeString = getEventTime(itemClicked);
		String startTimeString = null;
		String id = itemClicked.getId();
		
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
		
		// sqlQuery to set status to off
		String statusOff = String.format("UPDATE public.live_events "
				+ "SET on_status = FALSE "
				+ "WHERE event_id=\'%s\';", id);
		// sqlQuery to get the timestamp
		String sqlQuery = String.format("SELECT time_stamp from public.live_events "
				+ "WHERE event_id=\'%s\'", id);
		Statement statusUpdate = Main.c.createStatement();
		Statement timestamp = Main.c.createStatement();
		statusUpdate.executeUpdate(statusOff);
		ResultSet timestampResult = timestamp.executeQuery(sqlQuery);
		
		if (timestampResult.next()) {
			startTimeString = timestampResult.getString(1);
		}
		///TODO TODO TODO TODO TODO TODO TODO
		///TODO TODO TODO TODO TODO TODO TODO
		///TODO TODO TODO TODO TODO TODO TODO
		// USE THIS VALUE FOR THE CALCULATIONS
		long difference = timeDifference(strToTime(startTimeString), strToTime(endTimeString), ChronoUnit.SECONDS);
		///TODO TODO TODO TODO TODO TODO TODO
		///TODO TODO TODO TODO TODO TODO TODO
		///TODO TODO TODO TODO TODO TODO TODO
	}
	
	// changes the color and the status message
	public void changeColorAndMessage (Shape itemClicked, Paint color, String text) {
		itemClicked.setFill(color);
		quickStatusField.appendText("\n" + String.valueOf(itemClicked.getId()) + " " + text);
	}
	
	// creates a timestamp when called
	public String getEventTime(Shape itemClicked) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.toString();
	}
	
	// converts string from database back into ZonedDateTime;
	/*
	public ZonedDateTime stringToZonedDateTime(String timestring) {
		Timestamp timestamp = Timestamp.valueOf(timestring);
		LocalDateTime localDateTime = timestamp.toLocalDateTime();
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		return zonedDateTime;
	}
	*/
	
	public Timestamp strToTime(String timestring) {
		Timestamp timestamp = Timestamp.valueOf(timestring);
		return timestamp;
		
	}
	
	// calculates time difference between two Timestamps, first convert to LocalDateTime
	static long timeDifference(Timestamp d1, Timestamp d2, ChronoUnit unit){
		LocalDateTime localDateTimeStart = d1.toLocalDateTime();
		LocalDateTime localDateTimeEnd = d2.toLocalDateTime();
	    return unit.between(localDateTimeStart, localDateTimeEnd);
	}
	
	/*
	// initialize all toggle items in the database
	public void initToggleItems() throws SQLException {
		//TODO sqlQuery to add item by name if not already in table)
		String sqlQuery = String.format("foo");
		Statement s = Main.c.createStatement();
		for (Node node : lightingOverlay.getChildren()) {
			String event_id = node.getId();
			
		}
	} */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setCurrentTemperature(72);
		setSetTemperature(getCurrentTemperature());
		try {
			externalTempUpdater();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
