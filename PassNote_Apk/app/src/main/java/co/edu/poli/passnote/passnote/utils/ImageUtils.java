package co.edu.poli.passnote.passnote.utils;

import android.content.Context;

public class ImageUtils {
    public static int getImageIdByName(Context context, String entryName) {
        return context.getResources().getIdentifier("co.edu.poli.passnote.passnote:drawable/" + entryName, null, null);
    }
}
