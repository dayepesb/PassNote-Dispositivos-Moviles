package co.edu.poli.passnote.passnote;

import android.content.Context;

public class Application extends android.app.Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return applicationContext;
    }
}
