package application;

import java.sql.Statement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.ComboBox;
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
	private ComboBox<String> monthComboBox;
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
	
	// create the line graph that shows monthly usage
	public void createMonthGraph(int monthNumber, int days) throws SQLException {
		usageChart.getData().clear();
		
		Integer i = 1;
		
		// creating the series
		XYChart.Series electricity = new XYChart.Series();
		XYChart.Series water = new XYChart.Series();
		XYChart.Series waterEst = new XYChart.Series();
		
		// creating the lists to store the data points in
		List<XYChart.Series> elecList = new ArrayList<>();
		List<XYChart.Series> waterList = new ArrayList<>();
		List<XYChart.Series> waterEstList = new ArrayList<>();
		
		// naming the lines
		electricity.setName("Electricity");
		water.setName("Water");
		
		// adding x-axis constraints
		x.setAutoRanging(false);
		x.setLowerBound(1);
		x.setUpperBound(days);
		x.setTickUnit(1);
		x.setLabel("Day");
		
		// adding y-axis constraints
		y.setLabel("Dollars/Kilowatts/Gallons*");
		y.setAutoRanging(false);
		y.setLowerBound(0);
		y.setUpperBound(50);
		y.setTickUnit(5);
		
		String sqlQuery = String.format("SELECT * FROM electricity_bill WHERE CAST (start_date as CHAR) LIKE '%d%%'", monthNumber);
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
		

		String sqlQuery2 = String.format("SELECT * FROM water_bill WHERE CAST (start_date as CHAR) LIKE '%d%%'", monthNumber);
		Statement s2 = Main.c.createStatement();
		ResultSet queryResult2 = s2.executeQuery(sqlQuery2);
		i = 1;
		Integer gallons = 0;
		
		// going through each row that resulted from the query and adding the gallons to the graph
		while(i != 12) {
			queryResult2.next();
			gallons = (queryResult2.getInt("gallons"))/100;
			water.getData().add(new XYChart.Data(i,gallons));
			i++;
		} 
		
		i--;
		waterEst.getData().add(new XYChart.Data(i, gallons));
		i++;
		
		while(queryResult2.next()) {
			gallons = (queryResult2.getInt("gallons"))/100;
			waterEst.getData().add(new XYChart.Data(i, gallons));
			i++;
		}
		
		// closing the query thread
		queryResult2.close();
		
		
		// adding the data points to the series
		elecList.add(electricity);
		waterList.add(water);
		waterEstList.add(waterEst);
		
		// adding the series to the graph
		usageChart.getData().add(elecList.get(elecList.size() - 1));
		usageChart.getData().add(waterList.get(waterList.size() - 1));
		usageChart.getData().add(waterEstList.get(waterEstList.size() - 1));
		
		usageChart.setLegendVisible(false);
		
		if(monthNumber == 4) {
			// changing the electricity line to red
			Set<Node> elecNodes = usageChart.lookupAll(".series" + 0);
					
			// iterating through each node in the set
			for(Node node : elecNodes) {
				node.setStyle("-fx-stroke: #ff0000;\n" + "-fx-background-color: #ff0000, white;");
			}
			
			// changing the water line to blue
			Set<Node> waterNodes = usageChart.lookupAll(".series" + 1);
					
			// iterating through each node in the set
			for(Node node : waterNodes) {
				node.setStyle("-fx-stroke: #1184e8;\n" + "-fx-background-color: #1184e8, white;\n");
			}
			
			// changing the water estimate line
			Set<Node> waterEstNodes = usageChart.lookupAll(".series" + 2);
								
			// iterating through each node in the set
			for(Node node : waterEstNodes) {
				node.setStyle("-fx-stroke: #1184e8;\n" + "-fx-background-color: #1184e8, white;\n" + "-fx-stroke-dash-array: 1 1 2 10;");
			}
			
		}else {
			// changing the electricity line to red
			Set<Node> elecNodes = usageChart.lookupAll(".series" + 0);
		
			// iterating through each node in the set
			for(Node node : elecNodes) {
				node.setStyle("-fx-stroke: #ff0000;\n" + "-fx-background-color: #ff0000, white;");
			}
		
			// changing the water line to blue
			Set<Node> waterNodes = usageChart.lookupAll(".series" + 1);
		
			// iterating through each node in the set
			for(Node node : waterNodes) {
				node.setStyle("-fx-stroke: #1184e8;\n" + "-fx-background-color: #1184e8, white;\n");
			}
			
			// changing the water estimate line
			Set<Node> waterEstNodes = usageChart.lookupAll(".series" + 2);
											
			// iterating through each node in the set
			for(Node node : waterEstNodes) {
				node.setStyle("-fx-stroke: #1184e8;\n" + "-fx-background-color: #1184e8, white;\n");
			}
		}
	}
	
	// display the graph with the electricity and water usage
	public void selectMonthButtonPressed(ActionEvent actionEvent) throws SQLException {
		String month = monthComboBox.getValue();
		
		if (month == null) {
			System.out.println("Please select a month");
		} else if (month == "April") {
			// generate graph for April
			createMonthGraph(4, 30);
			System.out.println("April was selected");
		} else if (month == "March") {
			// generate graph for March
			createMonthGraph(3, 31);
			System.out.println("March was selected");
		} else {
			// month would be February
			// generate graph for February
			createMonthGraph(2, 28);
			System.out.println("February was selected");
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
		this.monthComboBox.getItems().removeAll(monthComboBox.getItems());
		this.monthComboBox.getItems().addAll(monthList);
		this.monthComboBox.getSelectionModel().select("April");

		
		try {
			createMonthGraph(4,30);
		} catch (SQLException e1) {
			System.out.println("Error creating graph.");
		}
		
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
