package passnote.poli.edu.co.PassNote.entities;

import javax.persistence.*;

@Entity
@Table(name = "numberaccounts", schema = "passnote", catalog = "")
@Access(AccessType.PROPERTY)
public class NumberaccountsEntity {
    private int idUser;
    private int accounts;
    private int accountsAllowed;

    @Id
    @Column(name = "idUser", nullable = false)
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Basic
    @Column(name = "accounts", nullable = false)
    public int getAccounts() {
        return accounts;
    }

    public void setAccounts(int accounts) {
        this.accounts = accounts;
    }

    @Basic
    @Column(name = "accountsAllowed", nullable = false)
    public int getAccountsAllowed() {
        return accountsAllowed;
    }

    public void setAccountsAllowed(int accountsAllowed) {
        this.accountsAllowed = accountsAllowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NumberaccountsEntity that = (NumberaccountsEntity) o;

        if (idUser != that.idUser) {
            return false;
        }
        if (accounts != that.accounts) {
            return false;
        }
        if (accountsAllowed != that.accountsAllowed) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = idUser;
        result = 31 * result + accounts;
        result = 31 * result + accountsAllowed;
        return result;
    }
}
