package co.edu.poli.passnote.passnote.notes;

/**
 * Created by johansteven on 19/10/2017.
 */

public class NotesItem {
    private String text;
    private String note;

    public NotesItem(String text, String note){
        this.text=text;
        this.note=note;
    }
    public String getText(){
        return text;
    }

    public String getNote(){
        return note;
    }
}
