package ent;
 
public class Prerequisite {
  
    public Prerequisite(int id1, int id2){
        this.idl = id1;
        this.idl1 = id2;
    }
    
    public int getLesson(){
        return this.idl1;
    }
    
    public int getReq(){
        return this.idl;
    }

    private int idl1, idl;
}
