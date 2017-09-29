package passnote.poli.edu.co.PassNote.entities;

import javax.persistence.*;

@Entity
@Table(name = "token", schema = "passnote", catalog = "")
@Access(AccessType.PROPERTY)
public class TokenEntity {
    private int idUser;
    private String token;
    private String tokenDate;

    @Id
    @Column(name = "idUser", nullable = false)
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "token", nullable = false, length = 255)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "tokenDate", nullable = false, length = 12)
    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TokenEntity that = (TokenEntity) o;

        if (idUser != that.idUser) {
            return false;
        }
        if (token != null ? !token.equals(that.token) : that.token != null) {
            return false;
        }
        if (tokenDate != null ? !tokenDate.equals(that.tokenDate) : that.tokenDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (tokenDate != null ? tokenDate.hashCode() : 0);
        return result;
    }
}
