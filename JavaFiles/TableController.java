package JavaFiles;


import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.swing.table.TableModel;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

public class TableController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField filterTextField;

    @FXML
    private SwingNode swingNode;

    private AlertsController alertsController;

    //database url, password and username
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    //set default query
    private static final String DEFAULTQUERY = "SELECT * FROM mydb.";
    private String tableName;

    //used to configure JTable
    private ResultTableModel tableModel;
    private TableRowSorter<TableModel> sorter;

    private JTable resultTable;

    //setTableName


    public void initialize(String tableName){

        try {
            //create resulttablemodel and display database table
            tableName = tableName.toLowerCase();
            tableName = tableName.replace(' ', '_');
            tableName = tableName + "electoral_register";
            tableModel = new ResultTableModel(URL, USERNAME, PASSWORD, DEFAULTQUERY, tableName);


            //populate JTable based on database
            resultTable = new JTable(tableModel);
            //setComboBoxColumn(resultTable, resultTable.getColumn(resultTable.getColumnName(resultTable.getColumnCount()-1)));

            //set up row sorting
            sorter = new TableRowSorter<TableModel>(tableModel);
            resultTable.setRowSorter(sorter);
            //borderPane = new BorderPane();


            swingNode.setContent(new JScrollPane(resultTable));


        }catch (SQLException e){
            alertsController.displayAlert(Alert.AlertType.ERROR, "Database Error",
                    e.getMessage());
            //close connection
            tableModel.disconnectFromDatabase();
        }
    }

    @FXML
    void applyFilterButtonPressed(ActionEvent event) {
        //TODO
        String text = filterTextField.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        }

        try{
            sorter.setRowFilter(RowFilter.regexFilter(text));
        }catch (PatternSyntaxException pse) {
            alertsController.displayAlert(Alert.AlertType.ERROR, "Regex Error", "Bad regex pattern");

        }
    }

    public JTable getResultTable(){
        return resultTable;
    }




}


