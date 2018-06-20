 
package ent;
 
public class Lesson {
    
    public Lesson(int id, String desc, String  name) {
        this.idlesson = id;
        this.description = desc;
        this.name = name;
    }

    public int getID(){
        return this.idlesson;
    }

    public String getDesc(){
        return this.description;
    }
    public String getName(){
        return this.name;
    }
    private int idlesson;
    private String description, name;
}


