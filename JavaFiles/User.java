package JavaFiles;


import java.util.ArrayList;

/**
 * For creation of user object, may not be necessary as checking through user db now.
 */
public class User {
    private String userName;
    private String password;
    private String role;

    private ArrayList<String> routes = new ArrayList<String>();

    public User(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;

    }

    public String getUserName(){
        return userName;
    }
}
