package pl.edu.agh.io.android.activities;

import android.content.Intent;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
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
    protected void onResume() {
        super.onResume();
        if(AppController.getInstance().isFirstTime()){
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
        } else {
            AppController.getInstance().reset();
            SpeedGameProxy.getInstance().reset();
            UsersController.reset();
            finish();
          //  android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
