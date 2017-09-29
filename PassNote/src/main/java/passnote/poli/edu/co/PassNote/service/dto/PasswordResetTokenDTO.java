package passnote.poli.edu.co.PassNote.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class PasswordResetTokenDTO {

    public static final int EXPIRATION_TIME_IN_MINUTES = 60 * 24;

    private String token;

    @JsonIgnore
    private UsersDTO user;

    private Date expirationDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return (this.expirationDate != null ? (Date) this.expirationDate.clone() : null);
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = (expirationDate != null ? (Date) expirationDate.clone() : null);
    }
}
