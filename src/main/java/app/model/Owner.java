package app.model;

public class Owner {
    private final String username;
    private final String password;

    public Owner(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
