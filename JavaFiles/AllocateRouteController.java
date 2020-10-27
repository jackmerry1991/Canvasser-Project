package JavaFiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows route to be added to any user, eventual aim is for this role to be reserved for those with administrator role.
 */
public class AllocateRouteController {

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private ListView<String> routeList;

    @FXML
    private TextField streetField;

    private UserQueries userQueries = new UserQueries();

    private User user;

    private String campaignName;

    private final ObservableList<Elector> electorList = FXCollections.observableArrayList();


    public void initialize(User user, String campaignName) {
        //set user
        this.user = user;
        this.campaignName = campaignName;

        //get user list by calling user query
        //userQueries.getAllUsers(campaignName);
        ArrayList<User> list = (ArrayList<User>) userQueries.getAllUsers(campaignName);//userQueries.getUserList();
        for (User u : list) {
            userComboBox.getItems().add(u.getUserName());
        }

        //getlistview to populate allocated routes
    }

    public void backButtonPressed(ActionEvent event) throws Exception {

        FXMLLoader secondaryLoader = new FXMLLoader();
        secondaryLoader.setLocation(getClass().getResource("/Resources/MainMenu.fxml"));
        Parent parent = secondaryLoader.load();
        MainMenuController mainMenuController = secondaryLoader.<MainMenuController>getController();

        mainMenuController.initialize(userQueries.getUserDetails(user.getUserName()), campaignName);

        //get stage information
        Scene menuScene = new Scene(parent, 800, 1000);
        menuScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());


        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();
    }

    public void addUserPressed(ActionEvent event) throws Exception {
        FXMLLoader secondaryLoader = new FXMLLoader();
        secondaryLoader.setLocation(getClass().getResource("/Resources/AddUser.fxml"));
        Parent addUserPage = secondaryLoader.load();
        AddUserController addUserController = secondaryLoader.<AddUserController>getController();

        addUserController.initialize(campaignName);
        Stage window = new Stage();
        window.setTitle("Add User");
        Scene scene = new Scene(addUserPage, 600, 800);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        window.setScene(scene);
        window.show();
    }

    /**
     * Populate routes list based on the selected user.
     * @param event
     */
    public void userSelected(ActionEvent event){
        //populate list with all of user's routes
        List<String> list;
        routeList.getItems().clear();
        list = userQueries.getUsersRoutes(userComboBox.getValue(), campaignName);

        //add results to routeList

        for(String s: list){
            routeList.getItems().add(s);
        }
    }

    public void allocateButtonSelected(ActionEvent event) throws Exception{
        ElectorQueries electorQueries = new ElectorQueries(campaignName);
        //Check entered address returns results
        electorList.setAll(electorQueries.getElectorsByRoad("%"+streetField.getText()+"%"));
        if(electorList.size() > 0) {
            userQueries.addNewRoute(streetField.getText(), userComboBox.getValue(), campaignName);
            AlertsController alerts = new AlertsController();
            alerts.displayAlert(Alert.AlertType.INFORMATION, "Street Allocated","Street has been added to user " + userComboBox.getValue());

            //repopulate list
            List<String> list;
            routeList.getItems().clear();
            list = userQueries.getUsersRoutes(userComboBox.getValue(), campaignName);

            //add results to routeList
            for(String s: list) {
                routeList.getItems().add(s);
            }
        }else {
           AlertsController alertsController = new AlertsController();
           alertsController.displayAlert(Alert.AlertType.ERROR, "No Matching Streets", "No streets match the entered value");

        }
    }

    /**
     * Remove route from user's route list.
     * @param event
     */
    public void deleteRoute(ActionEvent event){
        userQueries.removeRoute(userComboBox.getValue(), campaignName, routeList.getSelectionModel().getSelectedItem());
        AlertsController alert = new AlertsController();
        alert.displayAlert(Alert.AlertType.INFORMATION, "Route Removed", "This route has been removed");

        //repopulate list
        List<String> list;
        routeList.getItems().clear();
        list = userQueries.getUsersRoutes(userComboBox.getValue(), campaignName);

        //add results to routeList

        for(String s: list){
            routeList.getItems().add(s);
        }
    }



}
