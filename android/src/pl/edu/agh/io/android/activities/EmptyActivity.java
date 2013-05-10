package pl.edu.agh.io.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import pl.edu.agh.io.android.controller.SpeedGameProxy;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/9/13
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmptyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SpeedGameProxy.getInstance().isOnline()) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        } else {
            Intent myIntent = new Intent(this, NewGameActivity.class);
            startActivity(myIntent);
        }
    }
}
