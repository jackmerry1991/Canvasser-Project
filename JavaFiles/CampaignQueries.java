package JavaFiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampaignQueries {

    private Connection connection;
    private PreparedStatement statement;


    /**
     * CampaignQueriesConstructor
     */
    public CampaignQueries() {
        try {
            connection = DBConnection.getConnection();

            statement = connection.prepareStatement("SELECT * FROM campaigns_schema.campaigns_list, canvass_admin_schema.user_campaign  WHERE canvass_admin_schema.user_campaign.campaignName = campaigns_schema.campaigns_list.CampaignName AND username = ?;");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Campaign> getCampaignDetails(String userName) {
        try {
            statement.setString(1, userName);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //execute returns
        try (ResultSet resultSet = statement.executeQuery()) {
            List<Campaign> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(new Campaign(
                        resultSet.getString("CampaignName"),
                        resultSet.getString("SupportedCandidate"),
                        resultSet.getString("ElectionDate"),
                        resultSet.getString("Ward")));

            }
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

}



