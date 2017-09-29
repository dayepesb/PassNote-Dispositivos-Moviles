package passnote.poli.edu.co.PassNote.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class AccountsEntityPK implements Serializable {
    private int idUser;
    private String type;
    private String user;

    @Column(name = "idUser", nullable = false)
    @Id
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Column(name = "type", nullable = false, length = 32)
    @Id
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "user", nullable = false, length = 255)
    @Id
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountsEntityPK that = (AccountsEntityPK) o;

        if (idUser != that.idUser) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
