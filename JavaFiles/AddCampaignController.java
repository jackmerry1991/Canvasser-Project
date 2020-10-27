package JavaFiles;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sql.ConnectionPoolDataSource;

/**
 * Activated by add table button on main menu, works with AddCampaign.fxml.
 */
public class AddCampaignController {


    @FXML
    private TextField filePathField;

    @FXML
    private TextFlow textFlowInstructions;

    @FXML
    private TextField supportedCandidateField;

    @FXML
    private TextField campaignNameField;

    @FXML
    private DatePicker electionDatePicker;

    @FXML
    private TextField wardNameField;

    @FXML
    private TextField candidateNumberField;

    @FXML
    private TextField otherCandidatesField;

    @FXML
    private VBox otherCandidatesBox;

    private TextField[] candidateFields;


    private Text text1;
    private Text text2;
    private Text text3;

    private String userName;
    
    public void initialize(String userName) {
        this.userName = userName;
        text1 = new Text();
        text2 = new Text();
        text3 = new Text();
        text1.setText("\n1) The uploaded Electoral Register must consist of 13 columns.\n");
        text2.setText("2) The first three columns must contain the electoral number when combined.\n");
        text3.setText("3) The sixth column must contain the elector's full name, the next column the postcode and all following columns must for the elector's address when combined.");
        textFlowInstructions.getChildren().addAll(text1, text2, text3);
    }

    public void createTable(ActionEvent event) throws Exception{

        //test if fields have been completed
        if (checkFields() == true) {
            TableCreator create = new TableCreator();

            create.fillDB(filePathField.getText(), campaignNameField.getText());
            create.saveCampaign(campaignNameField.getText(), supportedCandidateField.getText(), electionDatePicker.getValue().toString(), wardNameField.getText(), userName);

            ArrayList<String> candList = new ArrayList<>();
            candList.add(otherCandidatesField.getText());
            for(TextField t: candidateFields){
                candList.add(t.getText());
                System.out.println(t.getText());
            }


            create.saveCandidates(campaignNameField.getText(), supportedCandidateField.getText(), candList);

            AlertsController alertsController = new AlertsController();
            alertsController.displayAlert(Alert.AlertType.INFORMATION, "Campaign Successfully added", "Your new campaign has been added successfully.");
            windowClose(event);
        }else{
            AlertsController alertsController = new AlertsController();
            alertsController.displayAlert(Alert.AlertType.ERROR, "Complete All Fields", "To create a campaign you must complete all fields.");
        }
    }


    /**
     * Set fileLocationTextField to reflect file path after user has selected file using file Chooser.
     * file path to be used when create button is selected, if user is uploading csv file in bulk.
     *
     * @param Event
     */
    public void bulkUpload(ActionEvent Event) {
        FileChooser fileChooser = new FileChooser();
        Stage fileStage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(fileStage);
        String fileName = selectedFile.getPath();
        filePathField.setText(fileName);

    }

    public void addCandidateButtonPressed(ActionEvent event) {
        if(candidateNumberField.getText().isEmpty()){
            AlertsController alert = new AlertsController();
            alert.displayAlert(Alert.AlertType.INFORMATION, "Must enter number of candidates", "Enter number of candidates before pressing the '+' button.");
        }else {
            int count = Integer.parseInt(candidateNumberField.getText()) - 1;
            candidateFields = new TextField[count];
            for (int i = 0; i < count; i++) {
                candidateFields[i] = new TextField();
            }


            //add arrayList to pane
            for (int i = 0; i < count; i++) {
                otherCandidatesBox.getChildren().add(candidateFields[i]);
            }
        }
    }

    public boolean checkFields(){
        if(campaignNameField.getText().isEmpty() || wardNameField.getText().isEmpty() || electionDatePicker.getValue().toString().isEmpty()){
            return false;
        }else return true;
    }

    public void windowClose(ActionEvent event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }



}


