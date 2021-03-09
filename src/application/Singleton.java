package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Singleton {
	
	private BorderPane rootLayout;
	
	private static Singleton uniqueInstance;
	public Scene _scene;
	public BorderPane _rootLayout;
	private Singleton() {
		try {
	    	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			_rootLayout = rootLayout;
		} catch (IOException e) {
			e.printStackTrace();
		}
         Scene scene = new Scene(rootLayout);
         _scene = scene;
	}
	
	public static Singleton getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Singleton();
		}
		return uniqueInstance;
	}
}