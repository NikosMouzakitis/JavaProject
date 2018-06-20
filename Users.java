package ent;

public class Users {

    public Users(int id, String login, String password, String email, String last, String first) {
        this.password = password;
        this.email = email;
        this.login = login;
        this.first = first;
        this.last = last;
    }

    public int getID() {
        return this.id;
    }

    public String getPass() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLogin() {
        return this.login;
    }

    public String getFirst() {
        return this.first;
    }

    public String getLast() {
        return this.last;
    }

    private int id;
    private String email, password, first, last, login;
}