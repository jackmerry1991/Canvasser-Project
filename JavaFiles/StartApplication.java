package JavaFiles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Load loginscreen on application start.
 */

public class StartApplication extends Application {
    public Parent root;
    public Stage mainWindow;
    public Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainWindow = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Resources/LoginScreen.fxml"));
        root = loader.load();

        scene = new Scene(root, 700, 1000);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        mainWindow.setScene(scene);
        mainWindow.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

}
