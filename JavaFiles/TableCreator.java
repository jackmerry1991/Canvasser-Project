package JavaFiles;


import javafx.scene.control.TextField;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Eventually to be only accessible to administrator to allow creation of database via user interface. Aspects of upload file need to be fixed.
 */
public class TableCreator {

    String baseTable = "CREATE TABLE mydb.tempTable (ElectorNumberPrefix VARCHAR(6), ElectorNumber VARCHAR(10), ElectorNumberSuffix VARCHAR(5), ElectorMarkers VARCHAR(10), ElectorDOB VARCHAR(10), ElectorName VARCHAR (60), PostCode VARCHAR(10), Address1 VARCHAR(70), Address2 VARCHAR (60), Address3 VARCHAR(60), Address4 VARCHAR (40), Address5 VARCHAR (40), Address6 VARCHAR(40), Canvassed VARCHAR(12), Intention VARCHAR(70), Notes VARCHAR(150), Has_Voted VARCHAR(3));";

    // public void fillDB(String tableName, String filePath, TextField[] columnNames, TextField[] colMaxLength) {
    public void fillDB(String filePath, String campaignName) {



        campaignName = campaignName.replace(' ', '_');
        //establish connection and create db
        try {
            Connection con = DBConnection.getConnection();

            //create statement object
            Statement statement = con.createStatement();

            //create electoral original register with split values table before filling it with user information from csv file
            statement.execute(baseTable);

            //check if file path is present and call fillDB method if so
            if (filePath != "") {
                uploadFile(filePath, con);
            }

            //create permanent table
            System.out.print("CREATE TABLE mydb."+ campaignName + "Electoral_Register as SELECT concat(ElectorNumberPrefix, ElectorNumber, ElectorNumberSuffix) as 'ElectorNumber', ElectorMarkers, ElectorDOB, ElectorName, PostCode, concat (Address1, ' ', Address2, ' ', Address3, ' ', Address4, ' ', Address5, ' ', Address6) as 'Address', Canvassed, Intention, Notes, Has_Voted FROM mydb.temptable;");
            statement.execute("CREATE TABLE mydb."+ campaignName + "Electoral_Register as SELECT concat(ElectorNumberPrefix, ElectorNumber, ElectorNumberSuffix) as 'ElectorNumber', ElectorMarkers, ElectorDOB, ElectorName, PostCode, concat (Address1, ' ', Address2, ' ', Address3, ' ', Address4, ' ', Address5, ' ', Address6) as 'Address', Canvassed, Intention, Notes, Has_Voted FROM mydb.temptable;");

            //drop temp table
            statement.execute("DROP TABLE mydb.tempTable");
            System.out.println(campaignName);
            statement.execute("ALTER TABLE mydb." + campaignName +  "Electoral_Register ADD PRIMARY KEY (ElectorNumber);");


        } catch (SQLException ex) {
            Logger.getLogger(TableCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void uploadFile(String filePath, Connection con) throws SQLException {

        String file = filePath;
        List<String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            //Some error logging
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (String[] s : list.subList(1, list.size())) {
            String input = "";
            int i = 0;
            while (i < s.length) {
                //Switch to fill empty trailing columns will Null
                switch (s.length) {
                    case 12:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 11:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '' ";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 10:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 9:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 8:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 7:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 6:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 5:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', '', ', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    case 4:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "'" + ", '', '', '', '', '', '', '', '', '', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                    default:
                        if (i == s.length - 1) {
                            input += "'" + s[i] + "', '', '', '', ''";
                        } else input += "'" + s[i] + "', ";
                        break;
                }


                i++;
            }

            Statement insertStatement = con.createStatement();
            insertStatement.execute("INSERT INTO mydb.tempTable VALUES (" + input + ");");
            System.out.println("INSERT INTO mydb.tempTable VALUES (" + input + ");");

            //execute based SQL insert statement based on concatinated input string


        }

    }

    public void saveCampaign(String campaignName, String supportedCandidate, String electionDate, String wardName, String userName){

        try{
            //update campaign table
            Connection con = DBConnection.getConnection();
            PreparedStatement createTable = con.prepareStatement("INSERT INTO campaigns_schema.campaigns_list VALUES(?, ?, ?, ?);");
            createTable.setString(1, campaignName);
            createTable.setString(2, supportedCandidate);
            createTable.setString(3, electionDate);
            createTable.setString(4, wardName);

            createTable.executeUpdate();

            //update userstable
            PreparedStatement addUserToCampaign = con.prepareStatement("INSERT INTO canvass_admin_schema.user_campaign VALUES (?, ?);");
            addUserToCampaign.setString(1, userName);
            addUserToCampaign.setString(2, campaignName);
            addUserToCampaign.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void saveCandidates(String campaignName, String supportedCandidate, ArrayList<String> otherCandidates) throws Exception{
        Connection con = DBConnection.getConnection();
        PreparedStatement candUpdate = con.prepareStatement("INSERT INTO campaigns_schema.candidates_list VALUES(?, ?)");

        //add supported candidate then those in array
        candUpdate.setString(1, supportedCandidate);
        candUpdate.setString(2, campaignName);
        candUpdate.executeUpdate();

        for(String s: otherCandidates){
            candUpdate.setString(1, s);
            candUpdate.setString(2, campaignName);
            candUpdate.executeUpdate();
        }
    }

}

