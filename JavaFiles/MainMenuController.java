package JavaFiles;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainMenuController {

    @FXML private ListView<String> routeList;


//load the chosen database

    //store list of table objects
    private  ObservableList<String> list = FXCollections.observableArrayList();

    @FXML
    private Text loginText;

    @FXML
    private Text routeText;

    @FXML
    private Text userRole;

    @FXML Button openRouteButton;

    @FXML
    private Button allocateRouteButton;

    @FXML
    private Button openTableButton;

    @FXML
    private Button tellerViewButton;

    private UserQueries userQueries = new UserQueries();

    private User user;

    private String campaignName;


    public void initialize(String[] userDetails, String campaignName){
        //instantiate user
        this.user = new User(userDetails[0], userDetails[1], userDetails[2]);
        this.campaignName = campaignName;

        //populate tablesList for admins
        loginText.setText(userDetails[0]);
        userRole.setText(userDetails[2]);

        if(userRole.getText().equals("Administrator")) {

        }else if(userRole.getText().equals("Canvasser")){
            allocateRouteButton.setVisible(false);
            tellerViewButton.setVisible(false);
            openTableButton.setVisible(false);
        }else{
            routeList.setVisible(false);
            allocateRouteButton.setVisible(false);
            tellerViewButton.setVisible(true);
            openTableButton.setVisible(false);
            routeText.setVisible(false);
            openRouteButton.setVisible(false);
        }
        //populate routesList based on userName

        List<String> list;
        list = userQueries.getUsersRoutes(loginText.getText(), campaignName);

        //add results to routeList
        for(String s: list) {
            routeList.getItems().add(s);

        }
    }

    /**
     * Activated by Open button. Opens table based on table selected in list.
     * @param event
     * @throws Exception
     */
    public void openTable(ActionEvent event)throws Exception{


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Resources/Table.fxml"));
        Parent openTable = loader.load();
        TableController tableController = loader.<TableController>getController();
        //pass table name to tableController
        tableController.initialize(campaignName);
        Stage openTableStage = new Stage();
        openTableStage.setTitle(campaignName + "Electoral Register");
        openTableStage.setScene(new Scene(openTable));
        openTableStage.setMaximized(true);
        openTableStage.show();

    }

    public void openRoute(ActionEvent event) throws Exception{
        FXMLLoader secondaryLoader = new FXMLLoader();
        secondaryLoader.setLocation(getClass().getResource("/Resources/List.fxml"));
        Parent openRouteList = secondaryLoader.load();
        ListController listController = secondaryLoader.<ListController>getController();

        //call initialize method in listController, which then calls elector queries for routeList name
        listController.initialize(routeList.getSelectionModel().getSelectedItem(), campaignName);

        Stage openRouteStage = new Stage();
        openRouteStage.setTitle("My Route: " + routeList.getSelectionModel().getSelectedItem());
        Scene scene = new Scene(openRouteList);
        openRouteStage.setScene(scene);        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        openRouteStage.setMaximized(true);
        openRouteStage.show();



    }

    public void allocateRoutePressed(ActionEvent event) throws Exception{
        FXMLLoader secondaryLoader = new FXMLLoader();
        secondaryLoader.setLocation(getClass().getResource("/Resources/AllocateRoutes.fxml"));
        Parent allocateRoutePage = secondaryLoader.load();
        AllocateRouteController allocateRouteController = secondaryLoader.<AllocateRouteController>getController();

        allocateRouteController.initialize(user, campaignName);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(allocateRoutePage);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        window.setScene(scene);

        window.setTitle("Allocate Routes");
        window.show();

    }


    public void tellerViewPressed(ActionEvent event) throws IOException {
        FXMLLoader secondaryLoader = new FXMLLoader();
        secondaryLoader.setLocation(getClass().getResource("/Resources/TellerListView.fxml"));
        Parent openRouteList = secondaryLoader.load();
        TellerListViewController tellerListViewController = secondaryLoader.<TellerListViewController>getController();

        //call initialize method in listController, which then calls elector queries for routeList name
        tellerListViewController.initialize(campaignName);

        Stage openRouteStage = new Stage();
        Scene scene = new Scene(openRouteList);
        openRouteStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        openRouteStage.setMaximized(true);
        openRouteStage.show();
    }

    public void backButtonPressed(ActionEvent event) throws IOException{

        FXMLLoader secondaryLoader = new FXMLLoader();
        secondaryLoader.setLocation(getClass().getResource("/Resources/CampaignsTable.fxml"));
        Parent parent = secondaryLoader.load();
        CampaignsTableController campaignsTableController = secondaryLoader.<CampaignsTableController>getController();
        campaignsTableController.initialize(userQueries.getUserDetails(loginText.getText()));
        Scene menuScene = new Scene(parent, 800, 1000);
        menuScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuScene);
        window.show();

    }


}