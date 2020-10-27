package JavaFiles;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListController {

    @FXML
    private TextField markersField;

    @FXML
    private TextField searchField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private ListView<Elector> listView;

    @FXML
    private ComboBox<String> canvassedComboBox;

    @FXML
    private ComboBox<String> intentionComboBox;

    @FXML
    private TextField notesField;

    @FXML
    private CheckBox canvassFilterButton;

    @FXML
    private CheckBox hasVotedFilterButton;

    @FXML
    private CheckBox electionViewCheckBox;

    private String routeName;

    //interact with database
    private ElectorQueries electorQueries;

    //store the list of electorObjects
    private final ObservableList<Elector> electorList = FXCollections.observableArrayList();

    private boolean canvassFilter = false;

    private boolean votedFilter = false;

    private boolean intentionFilterSelected = false;

    private String campaignName;

    private AlertsController alert = new AlertsController();


    /**
     * populate and set up list view using all elector query
     */
    public void initialize(String routeName, String campaignName) {
        this.routeName = "%" + routeName + "%";
        this.campaignName = campaignName;
        electorQueries = new ElectorQueries(campaignName);
        getAllEntries();
        listView.setItems(electorList);

        //initialise comboBoxButtons
        canvassedComboBox.getItems().addAll("Yes", "No", "Unavailable");

        //get candidate names and add to comboBox
        ArrayList<String> candidateNames = electorQueries.getCandidatesSearch(campaignName);
        intentionComboBox.getItems().addAll(candidateNames);

        //TODO HAVE LIST UPDATE TO REFLECT CHANGED RESULTS, CURRENTLY UPLOAD RESULTS SHOW RATHER THAN NEW RESULTS, HAVE TO REOPEN TO SEE UPDATES
        // change data when ListView changes
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue, oldValue, newValue) -> displayElectorInformation(newValue)
        );


    }

    /**
     * Populate all entries
     */
    private void getAllEntries() {
        electorList.setAll(electorQueries.getElectorsByRoad(routeName));
        selectFirstEntry();

    }

    /**
     * Select First entry from list view
     */
    private void selectFirstEntry() {
        listView.getSelectionModel().selectFirst();
    }

    /**
     * Display elector fields in interface
     * @param elector
     */
    private void displayElectorInformation(Elector elector) {
        if (elector != null) {
            numberField.setText(elector.getElectorNumber());
            markersField.setText(elector.getElectorMarkers());
            nameField.setText(elector.getElectorName());
            addressField.setText(elector.getAddress());
            notesField.setText(elector.getNotes());


            //set comboBox options to reflect database
            canvassedComboBox.setValue(elector.getCanvassed());
            if (canvassedComboBox.getValue().isEmpty()) {
                canvassedComboBox.setValue("No");
            }
            intentionComboBox.setValue(elector.getIntention());
            if (intentionComboBox.getValue().isEmpty()) {
                intentionComboBox.setValue("NA");
            }
            //add comments field if voter was unavailable


        } else {
            numberField.clear();
            markersField.clear();
            nameField.clear();
            addressField.clear();
        }
    }


    public void findButtonPressed(ActionEvent event) {
        List<Elector> list = electorQueries.getElectorsByRoad("%" +searchField.getText() + " " + routeName + "%");

        //display entries
        if (list.size() > 0) {
            electorList.setAll(list);
            selectFirstEntry();
        } else {
            alert.displayAlert(Alert.AlertType.INFORMATION, "No matches found", "There are no entries with details matching your search");
        }

    }

    /**
     * Check if filter boxes are selected
     * @param event
     */
    public void viewAllButtonPressed(ActionEvent event) {

        List<Elector> electors;

        if(canvassFilter && !votedFilter){
            electors = electorQueries.filterApplied(routeName, 1);

        }else if(votedFilter && !canvassFilter){
            electors = electorQueries.filterApplied(routeName, 2);

        }else if(votedFilter && canvassFilter){
            electors = electorQueries.filterApplied(routeName, 3);
            //intentionFilter = an election day view filtering not voted + no intention for candidate
        } else if (intentionFilterSelected){
            electors = electorQueries.filterApplied(routeName, 4);
        }
        else
            electors = electorQueries.getElectorsByRoad(routeName);

        //display entries
        if (electors.size() > 0) {
            electorList.setAll(electors);
            selectFirstEntry();
        } else {
            alert.displayAlert(Alert.AlertType.INFORMATION, "No matches found", "There are no entries with details matching your search");
        }

    }


    @FXML
    private void saveAndUpdate(ActionEvent event) {
        electorQueries.canvasserUpdate(intentionComboBox.getValue(), canvassedComboBox.getValue(), numberField.getText(), notesField.getText());
        alert.displayAlert(Alert.AlertType.INFORMATION, "Details Updated", "Elector details have been saved and updated for " + numberField.getText());

        //refresh list contents
        int index = listView.getSelectionModel().getSelectedIndex();
        getAllEntries();
        listView.getSelectionModel().select(index);
    }


    public void setCanvassedFilter(ActionEvent event){

        if (canvassFilterButton.isSelected()){
            canvassFilter = true;
            electionViewCheckBox.setSelected(false);
        } else canvassFilter = false;

    }

    public void setVotedFilter(ActionEvent event){

        if(hasVotedFilterButton.isSelected()){
            votedFilter = true;
            electionViewCheckBox.setSelected(false);
        } else votedFilter = false;
    }

    public void intentionFilter(ActionEvent event){
        if(electionViewCheckBox.isSelected()){
            intentionFilterSelected = true;
            //deselect other checkboxes as a result of election day view selection
            hasVotedFilterButton.setSelected(false);
            votedFilter = false;
            canvassFilter = false;
            canvassFilterButton.setSelected(false);
        }else intentionFilterSelected = false;
    }



}