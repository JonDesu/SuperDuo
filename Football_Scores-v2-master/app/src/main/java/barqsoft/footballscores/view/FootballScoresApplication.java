package barqsoft.footballscores.view;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class FootballScoresApplication extends Application{

    @Override
    public void onCreate() {

        super.onCreate();

        //https://github.com/dlew/joda-time-android
        JodaTimeAndroid.init(this);
    }

}
