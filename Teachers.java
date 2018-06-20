package ent;

public class Teachers extends Users {
    
    public Teachers(int id, String specialty, String interests, String tel, String login, String password, String email, String last, String first) {
        super(id, login, password, email, last, first);
        this.specialty = specialty;
        this.interests = interests;
        this.tel = tel;
    }
   
    private String specialty, interests, tel;

    public String getSpecialty() {
        return specialty;
    }

    public String getInterests() {
        return interests;
    }

    public String getTel() {
        return tel;
    }
}