package co.edu.poli.passnote.passnote.reminders;


import java.sql.Timestamp;

/**
 * Created by julianCastro on 19/10/2017.
 */

public class ReminderItem {
    private String name;
    private Timestamp date;
    private String userId;

    public ReminderItem(){

    }

    public ReminderItem (String name,Timestamp date){
        this.name=name;
        this.date=date;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDate(Timestamp date){
        this.date=date;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }

    public String getName(){
        return this.name;
    }

    public Timestamp getDate(){
        return this.date;
    }

    public String getUserId(){return this.userId;}
}
