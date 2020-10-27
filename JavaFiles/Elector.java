package JavaFiles;


/**
 * Instantiate Elector Objects based on entries in the mydb.canvasstable view in mysql.
 */

public class Elector {

    private String electorNumber;
    private String electorMarkers;
    private String electorName;
    private String address;
    private String canvassed;
    private String intention;
    private String notes;
    private String hasVoted;

    //constructor
    public Elector(String electorNumber, String electorMarkers, String electorName, String address, String canvassed, String intention, String notes, String hasVoted) {
        setElectorNumber(electorNumber);
        setElectorMarkers(electorMarkers);
        setElectorName(electorName);
        setAddress(address);
        setCanvassed(canvassed);
        setIntention(intention);
        setNotes(notes);
        setHasVoted(hasVoted);
    }

    //getters and setters for elector fields
    public String getElectorNumber() {
        return electorNumber;
    }

    public void setElectorNumber(String electorNumber) {
        this.electorNumber = electorNumber;
    }

    public String getElectorMarkers() {
        return electorMarkers;
    }

    public void setElectorMarkers(String electorMarkers) {
        this.electorMarkers = electorMarkers;
    }

    public String getElectorName() {
        return electorName;
    }

    public void setElectorName(String electorName) {
        this.electorName = electorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCanvassed() {
        return canvassed;
    }

    public void setCanvassed(String canvassed) {
        this.canvassed = canvassed;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getHasVoted(){
        return hasVoted;
    }

    public void setHasVoted(String hasVoted){
        this.hasVoted = hasVoted;
    }



    @Override
    public String toString() {
        return "Address: " + address + " Elector Number: " + electorNumber;

    }
}