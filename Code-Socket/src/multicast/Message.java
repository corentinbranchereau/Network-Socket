package multicast;
import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String name;
    private String date;

    Message(String msg,String name,String date){
        this.message = msg;
        this.name = name;
        this.date = date;
    }

    public String toString(){
        return name+": "+message;
    }

    public String getName(){
        return this.name;
    }
}
