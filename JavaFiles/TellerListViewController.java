package JavaFiles;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class TellerListViewController {
    @FXML
    ListView<Elector> electorList;
    @FXML
    TextField electorNumberField;
    @FXML
    TextField electorNameField;
    @FXML
    TextField searchField;
    @FXML
    ComboBox<String> hasVotedComboBox;

    private AlertsController alerts = new AlertsController();

    //interact with database
    private ElectorQueries electorQueries;

    //store the list of electorObjects
    private final ObservableList<Elector> list = FXCollections.observableArrayList();

    /**
     * populate and set up list view using all elector query
     */

    public void initialize(String campaignName) {
        electorQueries = new ElectorQueries(campaignName);
        getAllEntries();
        electorList.setItems(list);

        //initialise comboBoxButtons
        hasVotedComboBox.getItems().addAll("Yes", "No");

        electorList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue, oldValue, newValue) -> displayElectorInformation(newValue)
        );


    }

    /**
     * Populate all entries
     */
    private void getAllEntries() {
        list.setAll(electorQueries.getAllElectors());
        selectFirstEntry();
    }

    /**
     * Select First entry from list view
     */
    private void selectFirstEntry() {
        electorList.getSelectionModel().selectFirst();
    }

    //display elector information
    private void displayElectorInformation(Elector elector) {
        if (elector != null) {
            electorNumberField.setText(elector.getElectorNumber());
            electorNameField.setText(elector.getElectorName());
            hasVotedComboBox.setValue(elector.getHasVoted());

            //set comboBox options to reflect database
            //String newValue = hasVotedComboBox.getValue();
            if (hasVotedComboBox.getValue().isEmpty()) {
                hasVotedComboBox.setValue("No");
            }

        } else {
            electorNumberField.clear();
            electorNameField.clear();
        }
    }

    public void searchButtonPressed(ActionEvent event) {
        //TODO CHECK IF LONG WHERE MADE UP OF || SEARCH WOULD WORK TO CHECK EVERY LISTED ATTRIBUTE
        list.setAll(electorQueries.getElectorsByElectorNumber("%"+searchField.getText()));
    }

    public void saveAndUpdatePressed(ActionEvent event) {
        electorQueries.tellerUpdate(hasVotedComboBox.getValue(), electorNumberField.getText());
        alerts.displayAlert(Alert.AlertType.INFORMATION, "Details Updated", "Elector " +electorNumberField.getText() + " Voting Status has been updated");

        //update listView
        int index = electorList.getSelectionModel().getSelectedIndex();
        getAllEntries();
        electorList.getSelectionModel().select(index);
    }


}
