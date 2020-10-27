package JavaFiles;

import javafx.scene.control.Alert;

/**
 * To use with the Alerts.fxml file, to be updated when other screens are completed.
 *
 */
public class AlertsController {

    public void displayAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();

    }
}
