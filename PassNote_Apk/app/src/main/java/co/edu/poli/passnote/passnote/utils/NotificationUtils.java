package co.edu.poli.passnote.passnote.utils;

import android.util.Log;
import android.widget.Toast;

import co.edu.poli.passnote.passnote.R;

import static co.edu.poli.passnote.passnote.Application.getAppContext;

public class NotificationUtils {

    public static void showNotification(int messageId) {
        Toast.makeText(getAppContext(), messageId, Toast.LENGTH_SHORT).show();
    }

    public static void showGeneralError() {
        Toast.makeText(getAppContext(), R.string.genericError, Toast.LENGTH_SHORT).show();
    }

    public static void showGeneralError(Exception e) {
        Log.e(getAppContext().getClass().getName(), "General Error", e);
        Toast.makeText(getAppContext(), R.string.genericError, Toast.LENGTH_SHORT).show();
    }
}
