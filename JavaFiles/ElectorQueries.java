package JavaFiles;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * preparedStatements for any elector based queries.
 */
public class ElectorQueries {


    private Connection connection;
    private PreparedStatement selectAllElectors;
    private PreparedStatement selectByRoad;
    private PreparedStatement selectByElectorNumber;
    private PreparedStatement canvasserUpdateStatement;
    private PreparedStatement tellerUpdateStatement;
    private PreparedStatement getCandidates;
    private PreparedStatement getSupportedCandidate;
    private Statement filterSearch;

    private String campaignName;


    /**
     * Constructor, establish db connection.
     * And provide prepared statement arguments.
     */
    public ElectorQueries(String campaignName) {
        this.campaignName = campaignName;

        campaignName = campaignName.toLowerCase();
        campaignName = campaignName.replace(' ', '_');
        System.out.println("mydb."+campaignName+"electoral_register");
        try {
            connection = DBConnection.getConnection();

            //create  selectAllElectors query
            selectAllElectors = connection.prepareStatement("SELECT * FROM mydb."+ campaignName + "electoral_register ORDER BY ElectorNumber");

            //create query to select by road
            selectByRoad = connection.prepareStatement("SELECT * From mydb."+ campaignName + "electoral_register WHERE Address like ? ORDER BY CAST(SUBSTRING_INDEX(Address, 'Flat %', 1) AS UNSIGNED)");

            //create query to select by electorNumber
            selectByElectorNumber = connection.prepareStatement("SELECT * FROM mydb."+ campaignName + "electoral_register WHERE ElectorNumber like ?  ORDER BY ElectorNumber");

            //query to update elector information if role is canvasser
            canvasserUpdateStatement = connection.prepareStatement("UPDATE mydb."+ campaignName + "electoral_register SET Canvassed = ?, Intention = ?, Notes = ? WHERE ElectorNumber = ?;");

            //query to update elector information if role is teller
            tellerUpdateStatement = connection.prepareStatement("UPDATE mydb."+ campaignName + "electoral_register SET Has_Voted = ? WHERE ElectorNumber = ?;");

            getCandidates = connection.prepareStatement("SELECT CandidateName FROM campaigns_schema.candidates_list WHERE CampaignName = ?;");

            getSupportedCandidate = connection.prepareStatement("SELECT SupportedCandidate FROM campaigns_schema.campaigns_list WHERE CampaignName = ?;");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Select all electors and populate list
     * Used for the teller view.
     */
    public List<Elector> getAllElectors(){
        //executing query returns the results set
        try (ResultSet resultSet = selectAllElectors.executeQuery()){

            List<Elector> electorList = new ArrayList<>();

            while (resultSet.next()) {
                electorList.add(new Elector(
                        resultSet.getString("ElectorNumber"),
                        resultSet.getString("ElectorMarkers"),
                        resultSet.getString("ElectorName"),
                        resultSet.getString("Address"),
                        resultSet.getString("Canvassed"),
                        resultSet.getString("Intention"),
                        resultSet.getString("Notes"),
                        resultSet.getString("Has_Voted")));

            }
            return electorList;

        }catch(SQLException e){
            e.printStackTrace();

        }
        return null;
    }

    /**
     * Filter the results on the List.fxml page
     * address determined the searched road.
     * Int value of argument determines which filter
     * has been applied.
     * @param address, canvassFilter, votedFilter
     */
    public List<Elector> filterApplied(String address, int argument) {
        //get supported candidate
        String supportedCandidate = findSupportedCandidate(campaignName);

        System.out.println(campaignName);
        this.campaignName = campaignName.toLowerCase();
        this.campaignName = campaignName.replace(' ', '_');

        //order by statement
        String filterQuery = "SELECT * FROM mydb."+ campaignName +"electoral_register WHERE Address like ";
        String orderBy = "ORDER BY CAST(SUBSTRING_INDEX(Address, 'Flat %', 1) AS UNSIGNED)";


        //if statement to determine filter options
        switch (argument) {
            case 1:
                filterQuery += "'" + address + "' AND Canvassed != 'Yes' " + orderBy;
                break;
            case 2:
                filterQuery +="'" + address + "' AND Has_Voted != 'Yes' " + orderBy;
                break;
            case 3:
                filterQuery += "'" + address + "' AND Canvassed != 'Yes' AND Has_Voted !='Yes' " + orderBy;
                break;
            case 4:
                //TODO Test
                filterQuery += "'" + address + "' AND Has_Voted != 'Yes' AND Intention = '" + supportedCandidate + "'" + orderBy;
        }
        try {
            filterSearch = connection.createStatement();
        }catch (SQLException e){
            e.printStackTrace();
        }
        //execute returns
        System.out.println(filterQuery);
        try (ResultSet resultSet = filterSearch.executeQuery(filterQuery)) {
            List<Elector> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(new Elector(
                        resultSet.getString("ElectorNumber"),
                        resultSet.getString("ElectorMarkers"),
                        resultSet.getString("ElectorName"),
                        resultSet.getString("Address"),
                        resultSet.getString("Canvassed"),
                        resultSet.getString("Intention"),
                        resultSet.getString("Notes"),
                        resultSet.getString("Has_Voted")));
            }
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Return list of electors based on street address.
     * Used to populate list on List.fxml page.
     * @param address
     * @return List
     */
    public List<Elector> getElectorsByRoad(String address) {

        try {
            //if statement to determine filter options
            selectByRoad.setString(1,  address);



        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //execute returns
        try (ResultSet resultSet = selectByRoad.executeQuery()) {
            List<Elector> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(new Elector(
                        resultSet.getString("ElectorNumber"),
                        resultSet.getString("ElectorMarkers"),
                        resultSet.getString("ElectorName"),
                        resultSet.getString("Address"),
                        resultSet.getString("Canvassed"),
                        resultSet.getString("Intention"),
                        resultSet.getString("Notes"),
                        resultSet.getString("Has_Voted")));
            }
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * To implement for searching specific electors using searchBar.
     * Also useful for marking off voters on election day
     * @param electorNumber
     * @return List
     */
    public List<Elector> getElectorsByElectorNumber(String electorNumber) {
        //set electorNumber
        try {
            selectByElectorNumber.setString(1, electorNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //execute returns
        try (ResultSet resultSet = selectByElectorNumber.executeQuery()) {
            List<Elector> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(new Elector(
                        resultSet.getString("ElectorNumber"),
                        resultSet.getString("ElectorMarkers"),
                        resultSet.getString("ElectorName"),
                        resultSet.getString("Address"),
                        resultSet.getString("Canvassed"),
                        resultSet.getString("Intention"),
                        resultSet.getString("Notes"),
                        resultSet.getString("Has_Voted")));
            }
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Update the information held in the electoral register table for the
     * accessed campaign. Update made via the List.fxml page.
     * @param intention
     * @param canvassed
     * @param number
     * @param notes
     */
    public void canvasserUpdate(String intention, String canvassed, String number, String notes){
        try{
            canvasserUpdateStatement.setString(1, canvassed);
            canvasserUpdateStatement.setString(2, intention);
            canvasserUpdateStatement.setString(3, notes);
            canvasserUpdateStatement.setString(4, number);
            canvasserUpdateStatement.executeUpdate();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void tellerUpdate(String hasVoted, String electorNumber){
        try{
            tellerUpdateStatement.setString(1, hasVoted);
            tellerUpdateStatement.setString(2, electorNumber);
            tellerUpdateStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Return all candidates listed under the campaign name
     * @param campaignName
     * @return
     */
    public ArrayList<String> getCandidatesSearch(String campaignName){
        try{
            getCandidates.setString(1, campaignName);
            ResultSet resultSet = getCandidates.executeQuery();
            ArrayList<String> candidateList = new ArrayList<>();
            String candidateName;

            while (resultSet.next()) {
                candidateName = resultSet.getString("CandidateName");
                candidateList.add(candidateName);
            }
            return candidateList;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * retrieve supported candidate from campaigns list table for filter of elector list
     * @param campaignName
     * @return String
     */
    public String findSupportedCandidate(String campaignName) {
        try {
            getSupportedCandidate.setString(1, campaignName);
            ResultSet resultSet = getSupportedCandidate.executeQuery();
            String supportedCandidate = "";
           while(resultSet.next()) {
               supportedCandidate = resultSet.getString("SupportedCandidate");
           }
           return supportedCandidate;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
        public void close () {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
