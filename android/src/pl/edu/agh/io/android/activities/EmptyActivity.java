package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.os.Bundle;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.misc.IProcedure;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/9/13
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmptyActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getInstance().setAdditionalPlayer(false);
        SpeedGameProxy.getInstance().isOnlineAsync(new IProcedure<Boolean>() {
            @Override
            public void call(Boolean arg) {
                if (arg) {
                    Intent myIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(getBaseContext(), NewGameActivity.class);
                    startActivity(myIntent);
                }
            }
        });
    }

}
