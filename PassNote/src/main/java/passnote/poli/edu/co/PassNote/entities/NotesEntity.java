package passnote.poli.edu.co.PassNote.entities;

import javax.persistence.*;

@Entity
@Table(name = "notes", schema = "passnote", catalog = "")
@Access(AccessType.PROPERTY)
public class NotesEntity {
    private int iduser;
    private int idNote;
    private String type;
    private String text;
    private String dateOfCreation;

    @Basic
    @Column(name = "iduser", nullable = false)
    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    @Id
    @Column(name = "idNote", nullable = false)
    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "dateOfCreation", nullable = false, length = 12)
    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotesEntity that = (NotesEntity) o;

        if (iduser != that.iduser) {
            return false;
        }
        if (idNote != that.idNote) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (text != null ? !text.equals(that.text) : that.text != null) {
            return false;
        }
        if (dateOfCreation != null ? !dateOfCreation.equals(that.dateOfCreation) : that.dateOfCreation != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = iduser;
        result = 31 * result + idNote;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (dateOfCreation != null ? dateOfCreation.hashCode() : 0);
        return result;
    }
}
