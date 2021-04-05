package application;

import java.sql.Statement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	@FXML
	private NumberAxis x;
	@FXML
	private NumberAxis y;
	@FXML
	private LineChart<?,?> usageChart;
	
	@FXML
    private TableView<DatabaseTable> usageTable;
	@FXML
	private TableColumn<DatabaseTable, String> monthColumn;
	@FXML
	private TableColumn<DatabaseTable, Integer> wattageColumn;
	@FXML
	private TableColumn<DatabaseTable, Integer> gallonsColumn;
	@FXML
	private TableColumn<DatabaseTable, Integer> costColumn;
	
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
	
	public void createMonthGraphFeb() throws SQLException {
		Integer i = 1;

		String sqlQuery = "SELECT * FROM electricity_bill WHERE CAST (start_date as CHAR) LIKE '2%'";
		Statement s = Main.c.createStatement();
		ResultSet queryResult = s.executeQuery(sqlQuery);
		XYChart.Series electricity = new XYChart.Series();
		List<XYChart.Series> elecList = new ArrayList<>();
		
		// naming the line
		electricity.setName("Electricity");
		
		// adding x-axis constraints
		x.setAutoRanging(false);
		x.setLowerBound(1);
		x.setUpperBound(28);
		x.setTickUnit(1);
		x.setLabel("Day");
		
		// adding y-axis constraints
		y.setLabel("Watts/Gallons");
		y.setAutoRanging(false);
		y.setLowerBound(550);
		y.setUpperBound(1050);
		y.setTickUnit(50);
		
		// going through each row that resulted from the query and adding the kilowatts to the graph
		while(queryResult.next()) {
			Integer kilowatts = queryResult.getInt("kilowatts");
			electricity.getData().add(new XYChart.Data(i,kilowatts));
			i++;
		} 
		
		// closing the query thread
		queryResult.close();
		
		// adding the data points to the series
		elecList.add(electricity);
		
		// adding the series to the graph
		usageChart.getData().add(elecList.get(elecList.size() - 1));
	}
	
	
	// display the graph with the electricity and water usage
	public void selectMonthButtonPressed(ActionEvent actionEvent) throws SQLException {
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
			createMonthGraphFeb();
		}
	}
	
	//Returns list of data that can be inserted in the table
	public ObservableList<DatabaseTable> getData() {
		ObservableList<DatabaseTable> data = FXCollections.observableArrayList();
		data.add(new DatabaseTable("February", 390, 444, 1));
		data.add(new DatabaseTable("March", 300, 480, 1));
		data.add(new DatabaseTable("April", 300, 45, 5));
		
		return data;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		// setting default month to April
		monthChoiceBox.setValue("April");
		monthChoiceBox.setItems(monthList);
		
		// setup for table columns
		monthColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, String>("month"));
		wattageColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, Integer>("wattage"));
		gallonsColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, Integer>("gallons"));
		costColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, Integer>("cost"));
		
		// fill in data for columns
		usageTable.setItems(getData());
	}

}
