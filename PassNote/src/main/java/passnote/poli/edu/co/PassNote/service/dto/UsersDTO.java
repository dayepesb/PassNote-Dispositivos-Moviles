package passnote.poli.edu.co.PassNote.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.Date;

public class UsersDTO extends BaseDTO {

    private int idUser;
    private String idCountry;
    private String names;
    private String surnames;
    private String mail;
    private String user;
    private Date registrationDate;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
        if (!super.equals(o)) {
            return false;
        }

        UsersDTO usersDTO = (UsersDTO) o;

        if (idUser != usersDTO.idUser) {
            return false;
        }
        if (idCountry != null ? !idCountry.equals(usersDTO.idCountry) : usersDTO.idCountry != null) {
            return false;
        }
        if (names != null ? !names.equals(usersDTO.names) : usersDTO.names != null) {
            return false;
        }
        if (surnames != null ? !surnames.equals(usersDTO.surnames) : usersDTO.surnames != null) {
            return false;
        }
        if (mail != null ? !mail.equals(usersDTO.mail) : usersDTO.mail != null) {
            return false;
        }
        if (user != null ? !user.equals(usersDTO.user) : usersDTO.user != null) {
            return false;
        }
        if (registrationDate != null ? !registrationDate
                .equals(usersDTO.registrationDate) : usersDTO.registrationDate != null) {
            return false;
        }
        return password != null ? password.equals(usersDTO.password) : usersDTO.password == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + idUser;
        result = 31 * result + (idCountry != null ? idCountry.hashCode() : 0);
        result = 31 * result + (names != null ? names.hashCode() : 0);
        result = 31 * result + (surnames != null ? surnames.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UsersDTO{" +
                "idUser=" + idUser +
                ", idCountry='" + idCountry + '\'' +
                ", names='" + names + '\'' +
                ", surnames='" + surnames + '\'' +
                ", mail='" + mail + '\'' +
                ", user='" + user + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
