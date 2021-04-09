package application;

import java.sql.Statement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	private TableColumn<DatabaseTable, Double> wattageColumn;
	@FXML
	private TableColumn<DatabaseTable, Double> gallonsColumn;
	@FXML
	private TableColumn<DatabaseTable, Double> costColumn;
	
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
		usageChart.getData().clear();
		
		Integer i = 1;
		
		XYChart.Series electricity = new XYChart.Series();
		XYChart.Series water = new XYChart.Series();
		List<XYChart.Series> elecList = new ArrayList<>();
		List<XYChart.Series> waterList = new ArrayList<>();
		
		// naming the line
		electricity.setName("Electricity");
		water.setName("Water");
		
		// adding x-axis constraints
		x.setAutoRanging(false);
		x.setLowerBound(1);
		x.setUpperBound(28);
		x.setTickUnit(1);
		x.setLabel("Day");
		
		// adding y-axis constraints
		y.setLabel("Watts/Gallons");
		y.setAutoRanging(false);
		y.setLowerBound(0);
		y.setUpperBound(50000);
		y.setTickUnit(1000);
		
		String sqlQuery = "SELECT * FROM electricity_bill WHERE CAST (start_date as CHAR) LIKE '2%'";
		Statement s = Main.c.createStatement();
		ResultSet queryResult = s.executeQuery(sqlQuery);
		
		// going through each row that resulted from the query and adding the kilowatts to the graph
		while(queryResult.next()) {
			Long kilowatts = queryResult.getLong("kilowatts");
			electricity.getData().add(new XYChart.Data(i,kilowatts));
			i++;
		} 
		
		// closing the query thread
		queryResult.close();
		
		
		String sqlQuery2 = "SELECT * FROM water_bill WHERE CAST (start_date as CHAR) LIKE '2%'";
		Statement s2 = Main.c.createStatement();
		ResultSet queryResult2 = s2.executeQuery(sqlQuery2);
		i = 1;
		
		// going through each row that resulted from the query and adding the kilowatts to the graph
		while(queryResult2.next()) {
			Integer gallons = queryResult2.getInt("gallons");
			water.getData().add(new XYChart.Data(i,gallons));
			i++;
		} 
		
		// closing the query thread
		queryResult2.close();
		
		
		// adding the data points to the series
		elecList.add(electricity);
		waterList.add(water);
		
		// adding the series to the graph
		usageChart.getData().add(elecList.get(elecList.size() - 1));
		usageChart.getData().add(waterList.get(waterList.size() - 1));
		usageChart.setStyle(".default-color0.chart-series-line { -fx-stroke: #ff0000; }");
		
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
	public ObservableList<DatabaseTable> getData() throws SQLException {
		ObservableList<DatabaseTable> data = FXCollections.observableArrayList();
		String FEBWsqlQuery = "SELECT * FROM electricity_bill WHERE CAST (start_date as CHAR) LIKE '2%'";
		String FEBGsqlQuery = "SELECT * FROM water_bill WHERE CAST (start_date as CHAR) LIKE '2%'";
		String MARWsqlQuery = "SELECT * FROM electricity_bill WHERE CAST (start_date as CHAR) LIKE '3%'";
		String MARGsqlQuery = "SELECT * FROM water_bill WHERE CAST (start_date as CHAR) LIKE '3%'";
		String APRWsqlQuery = "SELECT * FROM electricity_bill WHERE CAST (start_date as CHAR) LIKE '4%'";
		String APRGsqlQuery = "SELECT * FROM water_bill WHERE CAST (start_date as CHAR) LIKE '4%'";
		// obtain database contents for table
		List<Double> totals = TableQuery(FEBWsqlQuery, FEBGsqlQuery);
		totals = roundingData(totals);
		data.add(new DatabaseTable("February", totals.get(0), totals.get(1), totals.get(2)));
		
		totals = TableQuery(MARWsqlQuery, MARGsqlQuery);
		totals = roundingData(totals);
		data.add(new DatabaseTable("March", totals.get(0), totals.get(1), totals.get(2)));
		
		totals = TableQuery(APRWsqlQuery, APRGsqlQuery);
		totals = roundingData(totals);
		data.add(new DatabaseTable("April", totals.get(0), totals.get(1), totals.get(2)));
		
		return data;
	}
	
	// retrieve database contents
	public List<Double> TableQuery(String WsqlQuery, String GsqlQuery) throws SQLException {
		List<Double> totals = Arrays.asList(0.0, 0.0, 0.0);
		double w = 0.0;
		double g = 0.0;
		double c = 0.0;
		
		// obtain data from electricity_bill
		Statement tw = Main.c.createStatement();
		ResultSet queryResultw = tw.executeQuery(WsqlQuery);
		
		while(queryResultw.next()) {
			Double kilowatts = queryResultw.getDouble("kilowatts");
			w = w + kilowatts;
			Double wcost = queryResultw.getDouble("total_amount");
			c = c + wcost;
		} 
		totals.set(0, w);
		queryResultw.close();
		
		// obtain data from water_bill
		Statement tg = Main.c.createStatement();
		ResultSet queryResultg = tg.executeQuery(GsqlQuery);
		while(queryResultg.next()) {
			Double gallons = queryResultg.getDouble("gallons");
			g = g + gallons;
			Double gcost = queryResultg.getDouble("amount");
			c = c + gcost;
		} 
		totals.set(1, g);
		totals.set(2, c);
		queryResultg.close();
		
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
		// TODO Auto-generated method stub
		
		// setting default month to April
		monthChoiceBox.setValue("April");
		monthChoiceBox.setItems(monthList);
		
		// setup for table columns
		monthColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, String>("month"));
		wattageColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, Double>("wattage"));
		gallonsColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, Double>("gallons"));
		costColumn.setCellValueFactory(new PropertyValueFactory<DatabaseTable, Double>("cost"));
		
		// fill in data for columns
		try {
			usageTable.setItems(getData());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
