package co.edu.poli.passnote.passnote.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import co.edu.poli.passnote.passnote.R;

public class NotificationUtils {

    public static void showNotification(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public static void showGeneralError(Context context) {
        Toast.makeText(context, R.string.genericError, Toast.LENGTH_SHORT).show();
    }

    public static void showGeneralError(Context context, Exception e) {
        Log.e(context.getClass().getName(), "General Error", e);
        Toast.makeText(context, R.string.genericError, Toast.LENGTH_SHORT).show();
    }
}
