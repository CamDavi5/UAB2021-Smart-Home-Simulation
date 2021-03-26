package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Singleton_DEPRECATED {
	
	private BorderPane rootLayout;
	
	private static Singleton_DEPRECATED uniqueInstance;
	public Scene _scene;
	public BorderPane _rootLayout;
	private Singleton_DEPRECATED() {
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
	
	public static Singleton_DEPRECATED getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Singleton_DEPRECATED();
		}
		return uniqueInstance;
	}
}