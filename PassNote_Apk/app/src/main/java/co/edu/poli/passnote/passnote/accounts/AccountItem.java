package co.edu.poli.passnote.passnote.accounts;

public class AccountItem {
    private int iconId;
    private String text;

    public AccountItem(int iconId, String text) {
        this.iconId = iconId;
        this.text = text;
    }

    public int getIconId() {
        return iconId;
    }

    public String getText() {
        return text;
    }
}
