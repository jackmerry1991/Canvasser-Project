package JavaFiles;


import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.sql.*;
import java.util.ArrayList;

public class ResultTableModel extends AbstractTableModel {
    private final Connection connection;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private int numberOfRows;

    // keep track of database connection status
    private boolean connectedToDatabase;
    // constructor initializes resultSet and obtains metadata object
    public ResultTableModel(String url, String userName, String password, String query, String tableName) throws SQLException {


        //connect to database
        connection = DriverManager.getConnection(url, userName, password);

        //create statement
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        //update connection status
        connectedToDatabase = true;

        //set query
        setQuery(query + tableName);
    }

    //get class that represents column class
    public Class getColumnClass(int column) throws IllegalStateException {
        //check connection availability
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        //determine Java Class of column
        try {
            String className = resultSetMetaData.getColumnClassName(column + 1);
            return Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return object class if problems occur
        return Object.class;
    }

    //get number of columns in resultSet
    public int getColumnCount() throws IllegalStateException {
        //ensure connected to database
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        //find number of columns
        try {
            return resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //get name of column in resultSet
    public String getColumnName(int column) throws IllegalStateException {
        //check database connection
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        //find column name
        try {
            return resultSetMetaData.getColumnName(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    //get number of rows in resultSet
    public int getRowCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        return numberOfRows;

    }


    //get values in row or column
    public Object getValueAt(int row, int column) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        //get value
        try {
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";

    }

    public void setQuery(String query) throws SQLException, IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        //specify and execute query
        resultSet = statement.executeQuery(query);

        //get metadata resultset
        resultSetMetaData = resultSet.getMetaData();

        //determine number of rows and columns in resultSet
        resultSet.last();
        numberOfRows = resultSet.getRow();

        // notify JTable that model has changed
        fireTableDataChanged();

    }

    //update cell value
    public void setValueAt(Object value, int row, int col) {
        fireTableRowsUpdated(row, col);

    }

    //close connection
    public void disconnectFromDatabase(){
        if(connectedToDatabase){
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                connectedToDatabase = false;
            }
        }
    }
}