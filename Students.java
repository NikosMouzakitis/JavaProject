package ent;

public class Students extends Users {


    public Students(int id, String login, String password, String email, String last, String first, String AM, int eksamino) {
        super(id, login, password, email, last, first );
        this.AM = AM;
        this.eksamino = eksamino;
    }
  
    private String AM; 
    private int eksamino;

    /**
     * @return the AM
     */
    public String getAM() {
        return AM;
    }

    /**
     * @return the eksamino
     */
    public int getSemester() {
        return eksamino;
    }
}

