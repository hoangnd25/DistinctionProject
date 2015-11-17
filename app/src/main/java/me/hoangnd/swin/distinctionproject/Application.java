package me.hoangnd.swin.distinctionproject;

import com.parse.Parse;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(
                this,
                getResources().getString(R.string.parse_app_id),
                getResources().getString(R.string.parse_client_key)
        );
    }
}
