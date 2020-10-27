package JavaFiles;

import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.print.Book;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CampaignsTableController {

    @FXML
    private TableView<Campaign> campaignsTableView;
    @FXML
    private TableColumn<Campaign, String> campaignNameColumn;
    @FXML
    private TableColumn<Campaign, String> wardNameColumn;
    @FXML
    private TableColumn<Campaign, String> candidateNameColumn;
    @FXML
    private TableColumn<Campaign, String> electionDateColumn;

    @FXML
    private Text roleText;

    @FXML
    private Text userNameText;

    private User user;

    private CampaignQueries campaignQueries;

    private ObservableList<Campaign> campaignsList;

    private String campaignName;


    public void initialize(String[] userDetails) {
        this.user = new User(userDetails[0], userDetails[1], userDetails[2]);
        campaignQueries = new CampaignQueries();
        campaignsList = FXCollections.observableArrayList();
        campaignsList.setAll(campaignQueries.getCampaignDetails(user.getUserName()));
        userNameText.setText(userDetails[0]);
        roleText.setText(userDetails[2]);

        setTableValues();

    }
        public void setTableValues() {

            campaignNameColumn.setCellValueFactory(new PropertyValueFactory<>("campaignName"));
            wardNameColumn.setCellValueFactory(new PropertyValueFactory<>("ward"));
            candidateNameColumn.setCellValueFactory(new PropertyValueFactory<>("candidateName"));
            electionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

            campaignsTableView.setItems(campaignsList);

        }

        public void selectButtonPressed(ActionEvent event) {
            //Select campaignName and store as string
            int index = campaignsTableView.getSelectionModel().getSelectedIndex();
            Campaign campaignItem = campaignsTableView.getItems().get(index);
            campaignName = campaignItem.getCampaignName();


            //load main menu class with campaign name and user details
            FXMLLoader secondaryLoader = new FXMLLoader();
            secondaryLoader.setLocation(getClass().getResource("/Resources/MainMenu.fxml"));
            Parent mainMenu = null;
            try {
                mainMenu = secondaryLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainMenuController mainMenuController = secondaryLoader.<MainMenuController>getController();

            UserQueries userQueries = new UserQueries();
            //call initialize method in listController, which then calls elector queries for routeList name
            mainMenuController.initialize(userQueries.getUserDetails(user.getUserName()), campaignName);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(mainMenu);
            scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

            window.setScene(scene);
            window.show();

        }

        /**
         * Add new campaign, openes the addCampaign FXML file and controller
         * @param event
         */
        public void addCampaignPressed (ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Resources/AddCampaign.fxml"));
            Parent addCampaign = loader.load();
            AddCampaignController addCampaignController = loader.<AddCampaignController>getController();
            //pass table name to tableController
            addCampaignController.initialize(user.getUserName());
            Stage addCampaignStage = new Stage();
            addCampaignStage.setScene(new Scene(addCampaign));
            addCampaignStage.show();
        }



}
