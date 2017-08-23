package passnote.poli.edu.co.PassNote.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "PASSWORD_RESET_TOKENS")
public class PasswordResetTokenEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String token;

    @OneToOne
    private UserEntity user;

    private Date expirationDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return (this.expirationDate != null ? (Date) this.expirationDate.clone() : null);
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = (expirationDate != null ? (Date) expirationDate.clone() : null);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
