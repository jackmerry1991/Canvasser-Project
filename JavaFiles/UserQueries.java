package JavaFiles;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserQueries class for user based queries, will populate user's routes lists
 * based on contents of canvass_admin_schema.users_table contents
 */
public class UserQueries{


    private Connection connection;
    private PreparedStatement selectUserRoutes;
    private PreparedStatement selectAllUsers;
    private PreparedStatement addRoute;
    private PreparedStatement getUserDetails;
    private PreparedStatement addNewUser;
    private PreparedStatement checkUserCampaign;
    private PreparedStatement addUserCampaign;
    private PreparedStatement addUserDBAccess;
    private PreparedStatement grantPrivileges;
    private PreparedStatement deleteRoute;

    private ArrayList<User> userList;




    /**
     * Constructor, establish db connection.
     */
    public UserQueries() {
        try {
            connection = DBConnection.getConnection();
            //connection = DriverManager.getConnection(URL, username, password);

            //create  selectAllElectors query
            selectUserRoutes = connection.prepareStatement("SELECT roadName FROM canvass_admin_schema.route_table WHERE Username = ? AND campaignName = ?;");
            selectAllUsers = connection.prepareStatement("SELECT * from canvass_admin_schema.users_table, canvass_admin_schema.user_campaign where canvass_admin_schema.users_table.userName = canvass_admin_schema.user_campaign.userName and campaignName = ?;");
            addRoute = connection.prepareStatement("INSERT INTO canvass_admin_schema.route_table VALUES (?, ?, ?);");
            getUserDetails = connection.prepareStatement("SELECT userName, userPassword, role FROM canvass_admin_schema.users_table WHERE userName = ?;");
            checkUserCampaign = connection.prepareStatement("SELECT username FROM canvass_admin_schema.user_campaign WHERE userName = ? AND campaignName = ?");
            addNewUser = connection.prepareStatement("INSERT INTO canvass_admin_schema.users_table VALUES (?, ?, ?);");
            addUserCampaign = connection.prepareStatement("INSERT INTO canvass_admin_schema.user_campaign VALUES(?, ?);");
            addUserDBAccess = connection.prepareStatement("CREATE USER ?@? IDENTIFIED BY ?;");
            grantPrivileges = connection.prepareStatement("GRANT ? TO ?");
            deleteRoute =connection.prepareStatement("DELETE FROM canvass_admin_schema.route_table WHERE userName =? AND campaignName =? AND roadName = ?");


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Select all electors and populate list
     */
    public List<User> getAllUsers(String campaignName){
        //executing query returns the results set
        try {
            selectAllUsers.setString(1, campaignName);
        }catch(SQLException e){
            e.printStackTrace();
        }
        try (ResultSet resultSet = selectAllUsers.executeQuery()){

            userList = new ArrayList<>();

            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getString("userName"),
                        resultSet.getString("userPassword"),
                        resultSet.getString("Role")));
            }
            return userList;

        }catch(SQLException e){
            e.printStackTrace();

        }
        return null;

    }

    /**
     * Select user's routes
     */
    public List<String> getUsersRoutes(String username, String campaignName) {
        //set road
        try {
            selectUserRoutes.setString(1, username);
            selectUserRoutes.setString(2, campaignName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //execute returns
        try (ResultSet resultSet = selectUserRoutes.executeQuery()) {
            List<String> results = new ArrayList();
            while (resultSet.next()) {
                results.add(
                        resultSet.getString("roadName"));
            }
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }


    }

    public ArrayList<User> getUserList(){
        return userList;
    }

    /**
     * Add route to userList on allocate route page
     */
    public void addNewRoute(String roadName, String userName, String campaignName){
        try {
            addRoute.setString(1, roadName);
            addRoute.setString(2, userName);
            addRoute.setString(3, campaignName);
            addRoute.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean passwordCheck(String userName, String password){

        boolean userVerified = false;
        try{
            getUserDetails.setString(1, userName);
            ResultSet resultSet = getUserDetails.executeQuery();

            String nameField = "";
            String passwordField = "";
            while (resultSet.next()) {
                nameField = resultSet.getString(1);
                passwordField = resultSet.getString("userPassword");
            }

            if(nameField.equals(userName) && passwordField.equals(password)){
                userVerified = true;
            }

            return userVerified;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return userVerified;

    }

    /**
     * Set addNewUser Query parameters and execute to update users table.
     * @param userName
     * @param passWord
     * @param role
     */
    public void addUser(String userName, String passWord, String role, String campaignName) {
        try {
            //add new user to user table
            addNewUser.setString(1, userName);
            addNewUser.setString(2, passWord);
            addNewUser.setString(3, role);

            addUserCampaign.setString(1, userName);
            addUserCampaign.setString(2, campaignName);

            //add new user to sql db access
            addUserDBAccess.setString(1, userName);

            addUserDBAccess.setString(2, "192.168.0.24");
            addUserDBAccess.setString(3, passWord);

            //addUserSQLRole
            grantPrivileges.setString(1, role);
            grantPrivileges.setString(2, userName);

            //execute
            System.out.println(addUserCampaign);
            addNewUser.executeUpdate();
            //addUserCampaign.executeUpdate();
            addUserToCampaign(addUserCampaign);
            addUserDBAccess.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addUserToCampaign(PreparedStatement preparedStatement){
        try{
            preparedStatement.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String [] getUserDetails(String userName) {
        try {
            getUserDetails.setString(1, userName);
            ResultSet resultSet = getUserDetails.executeQuery();
            String userID = "";
            String password = "";
            String role = "";
            while (resultSet.next()) {
                userID = resultSet.getString("userName");
                password = resultSet.getString("userPassword");
                role = resultSet.getString("role");
            }
            String[] userDetails = {userID, password, role};
            return userDetails;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if user is already assigned to the campaign if they already exist
     * return true if so and false if not, meaning user exists but on a different campaign.
     * @param campaignName
     * @return
     */
    public boolean onCampaign(String usersName, String campaignName) {
        try {
            checkUserCampaign.setString(1, usersName);
            checkUserCampaign.setString(2, campaignName);
            System.out.println(checkUserCampaign);
            ResultSet resultSet = checkUserCampaign.executeQuery();
            String name = "";
            while(resultSet.next()){
                name = resultSet.getNString("userName");
            }
            if(name.isEmpty()){
                System.out.print(name);
                return false;
            }else {System.out.print(name);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addExistingUserToCampaign(String userName, String campaignName){
        try {
            addUserCampaign.setString(1, userName);
            addUserCampaign.setString(2, campaignName);
            addUserCampaign.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removeRoute(String userName, String campaignName, String roadName) {
        try {
            deleteRoute.setString(1, userName);
            deleteRoute.setString(2, campaignName);
            deleteRoute.setString(3, roadName);
            deleteRoute.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
