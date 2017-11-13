package co.edu.poli.passnote.passnote.accounts;

import com.google.firebase.firestore.Exclude;

public class AccountItem {
    private String id;
    private String imageEntryName;
    private int localImageId;
    private String name;
    private String URL;
    private String usuario;
    private String password;
    private String userId;

    public AccountItem() {
    }

    public AccountItem(String id, int localImageId, String name) {
        this.localImageId = localImageId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUsername() {
        return usuario;
    }

    public void setUsername(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageEntryName() {
        return imageEntryName;
    }

    public void setImageEntryName(String imageEntryName) {
        this.imageEntryName = imageEntryName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    public int getLocalImageId() {
        return localImageId;
    }

    public void setLocalImageId(int localImageId) {
        this.localImageId = localImageId;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
