package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameActivity extends AbstractActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);

        Spinner onTimeout = ((Spinner)findViewById(R.id.newgame__on_timeout));
        onTimeout.setAdapter(new ArrayAdapter<UsersController.OnTimeout>(
                this, android.R.layout.simple_spinner_item, UsersController.OnTimeout.values()
        ));

        if(!SpeedGameProxy.getInstance().isOnline() || !AppController.getInstance().isLogged()){
            findViewById(R.id.newgame__change_account).setVisibility(View.INVISIBLE);
        }else {
            findViewById(R.id.newgame__change_account).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.getInstance().setNew(false);
                    Intent intent = new Intent(view.getContext(),NewAccountActivity.class);
                    startActivity(intent);
                }
            });
        }



        Button start = (Button) findViewById(R.id.newgame__start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UsersController controller = UsersController.getInstance();

                CheckBox sound = ((CheckBox)findViewById(R.id.newgame__sound));
                controller.setSoundOn(UsersController.Sound.fromBoolean(sound.isChecked()));

                Spinner onTimeout = ((Spinner)findViewById(R.id.newgame__on_timeout));
                controller.setOnTimeout(UsersController.OnTimeout.fromString(onTimeout.getSelectedItem().toString()));

                String timeString=((TextView)findViewById(R.id.newgame__time)).getText().toString();
                int timeLeft = !timeString.equals("")?Integer.parseInt(timeString):300;


                controller.setTime(timeLeft);

                Intent myIntent = new Intent(view.getContext(),UsersActivity.class);
                startActivityForResult(myIntent,0);
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        AppController.getInstance().setAdditionalPlayer(true);
    }
}