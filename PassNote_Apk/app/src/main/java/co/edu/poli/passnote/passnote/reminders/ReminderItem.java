package co.edu.poli.passnote.passnote.reminders;


import java.sql.Timestamp;

/**
 * Created by julianCastro on 19/10/2017.
 */

public class ReminderItem {
    private String name;
    private Timestamp date;

    public ReminderItem (String name,Timestamp date){
        this.name=name;
        this.date=date;
    }

    public String getName(){
        return this.name;
    }

    public Timestamp getDate(){
        return this.date;
    }
}
