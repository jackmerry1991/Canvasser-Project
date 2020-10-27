package JavaFiles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * Add User controller, works with addUser.fxml file. Loaded when AllocateUser.fxml add user button is pressed.
 */
public class AddUserController {

    @FXML
    TextField userNameField;
    @FXML
    TextField passWordField;
    @FXML
    ComboBox<String> roleComboBox;

    UserQueries userQueries = new UserQueries();

    AlertsController alert = new AlertsController();

    private String campaignName;

    public void initialize(String campaignName){
        roleComboBox.getItems().addAll("Administrator", "Canvasser", "Teller");
        this.campaignName = campaignName;
    }

    public void addUserButtonPressed(ActionEvent event) {
        //check comboBox has item has been selected
        boolean isMyComboBoxEmpty = roleComboBox.getSelectionModel().isEmpty();

        //Check that fields have been completed
        if (userNameField.getText().isEmpty() || passWordField.getText().isEmpty() || isMyComboBoxEmpty) {
            //AlertsController alertTwo = new AlertsController();
            alert.displayAlert(Alert.AlertType.INFORMATION, "Missing Fields", "You must complete all fields before selecting 'Add User'");

            //If fields have been completed
        } else {
            //check if user already exists by checking if userDetails is null
            String[] userDetails = userQueries.getUserDetails(userNameField.getText());
            for(String s: userDetails) {
                System.out.println(s);
            }
            //Check if user already exists
            if (userDetails[0].isEmpty()) {
                userQueries.addUser(userNameField.getText(), passWordField.getText(), roleComboBox.getValue(), campaignName);
                alert.displayAlert(Alert.AlertType.INFORMATION, "User Successfully Added", "User " + userNameField.getText() + " has been added as a new user");

                //If user does exist, check allocated campaigns do not match with current
            }else if(!userQueries.onCampaign(userNameField.getText(), campaignName)){
                userQueries.addExistingUserToCampaign(userNameField.getText(), campaignName);
                alert.displayAlert(Alert.AlertType.INFORMATION, "Campaign Added to User", "User " + userNameField.getText() + " already existed and has been added to the campaign");

                //alert if user does already exist on same campaign
            } else {
                AlertsController alertsController = new AlertsController();
                alertsController.displayAlert(Alert.AlertType.INFORMATION, "User Already Exists", "The user already exists on this campaign.");
            }
        }
    }

}