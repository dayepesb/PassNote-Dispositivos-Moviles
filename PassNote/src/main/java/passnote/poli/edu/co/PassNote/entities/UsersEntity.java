package passnote.poli.edu.co.PassNote.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import passnote.poli.edu.co.PassNote.service.BCryptPasswordDeserializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users", schema = "passnote", catalog = "")
@Access(AccessType.PROPERTY)
public class UsersEntity {
    private int idUser;
    private String idCountry;
    private String names;
    private String surnames;
    private String mail;
    private String user;
    private String password;
    private Date registrationDate;

    @Id
    @Column(name = "idUser", nullable = false)
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "idCountry", nullable = false, length = 24)
    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    @Basic
    @Column(name = "names", nullable = false, length = 32)
    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    @Basic
    @Column(name = "surnames", nullable = false, length = 32)
    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    @Basic
    @Column(name = "mail", nullable = false, length = 32)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Basic
    @Column(name = "user", nullable = false, length = 255)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = BCryptPasswordDeserializer.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "registrationDate", nullable = false, length = 12)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsersEntity that = (UsersEntity) o;

        if (idUser != that.idUser) {
            return false;
        }
        if (idCountry != null ? !idCountry.equals(that.idCountry) : that.idCountry != null) {
            return false;
        }
        if (names != null ? !names.equals(that.names) : that.names != null) {
            return false;
        }
        if (surnames != null ? !surnames.equals(that.surnames) : that.surnames != null) {
            return false;
        }
        if (mail != null ? !mail.equals(that.mail) : that.mail != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }
        if (password != null ? !password.equals(that.password) : that.password != null) {
            return false;
        }
        if (registrationDate != null ? !registrationDate
                .equals(that.registrationDate) : that.registrationDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + (idCountry != null ? idCountry.hashCode() : 0);
        result = 31 * result + (names != null ? names.hashCode() : 0);
        result = 31 * result + (surnames != null ? surnames.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        return result;
    }
}
