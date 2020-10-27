package JavaFiles;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



/**
 * Login Screen, successful login takes to main menu.
 */

public class LoginScreenController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    private UserQueries userQueries = new UserQueries();


    /**
     * Check user details and load MainMenu.FXML is details match user database details.
     * @param event
     */
    public void signInButtonPressed(ActionEvent event) throws Exception {

        //check if password and username match userDB entry
        boolean correctDetails = userQueries.passwordCheck(userNameField.getText(), passwordField.getText());
        //load correct page if login details match
        FXMLLoader secondaryLoader = new FXMLLoader();
        if (correctDetails) {

            secondaryLoader.setLocation(getClass().getResource("/Resources/CampaignsTable.fxml"));
            Parent parent = secondaryLoader.load();
            CampaignsTableController campaignsTableController = secondaryLoader.<CampaignsTableController>getController();

            //call initialize method in MainMenu to pass userName to initialize method along with user details array
            campaignsTableController.initialize(userQueries.getUserDetails(userNameField.getText()));

            //get stage information
            Scene menuScene = new Scene(parent, 800, 1000);
            menuScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());


            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();

        }else{
            //load wrong password alert
            AlertsController alertsController = new AlertsController();
            alertsController.displayAlert(Alert.AlertType.ERROR, "Incorrect Username or Password", "Please check that you use the correct username and password");
        }

    }

}
