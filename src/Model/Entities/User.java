package model.entities;

public class User {
    public String firstName;
    public String lastName;
    public String login;
    public String password;
    public int id;

    public User(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("('%s', '%s', '%s', '%s')", firstName, lastName, login, password);
    }
}
