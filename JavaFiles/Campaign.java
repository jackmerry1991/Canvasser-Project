package JavaFiles;

public class Campaign {
    private String campaignName;
    private String candidateName;
    private String date;
    private String ward;

    public Campaign(String campaignName, String candidateName, String date, String ward) {
        this.campaignName = campaignName;
        this.candidateName = candidateName;
        this.date = date;
        this.ward = ward;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "campaignName='" + campaignName + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", date='" + date + '\'' +
                ", ward='" + ward + '\'' +
                '}';
    }
}
