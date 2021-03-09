package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
	this.primaryStage = primaryStage;
	this.primaryStage.setTitle("Smart Home");
	
	initRootLayout();
	showSmartHome();
	}

	public void initRootLayout(){
    	rootLayout = Singleton.getInstance()._rootLayout;//creating the instance
		Scene scene = Singleton.getInstance()._scene;
		scene.getStylesheets().add(Main.class.getResource("dark.css").toString());
		primaryStage.setScene(scene);
		primaryStage.show();
    }
	
	public void showSmartHome() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("smart_home.fxml"));
			AnchorPane SmartHome = (AnchorPane) loader.load();
			rootLayout.setCenter(SmartHome);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
