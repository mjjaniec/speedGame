package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.misc.TimeParser;

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
                    Intent intent = new Intent(view.getContext(),RegisterActivity.class);
                    startActivity(intent);

                }
            });
        }



        Button start = (Button) findViewById(R.id.newgame__start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int timeLeft;
                String timeString=((TextView)findViewById(R.id.newgame__time)).getText().toString();
                if(timeString.equals("")){
                    timeLeft = 300;
                }else {
                    try{
                        timeLeft = TimeParser.Parse(timeString);
                    }catch (Exception e){
                        ((TextView)findViewById(R.id.newgame__time)).setError(getString(R.string.newgame__proper_time));
                        return;
                    }
                }
                UsersController controller = UsersController.getInstance();

                CheckBox sound = ((CheckBox)findViewById(R.id.newgame__sound));
                controller.setSoundOn(UsersController.Sound.fromBoolean(sound.isChecked()));

                Spinner onTimeout = ((Spinner)findViewById(R.id.newgame__on_timeout));
                controller.setOnTimeout(UsersController.OnTimeout.fromString(onTimeout.getSelectedItem().toString()));


                controller.setTime(timeLeft);

                Intent myIntent = new Intent(view.getContext(),UsersActivity.class);
                startActivityForResult(myIntent,0);
            }
        });


        findViewById(R.id.newgame__time).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try{
                    TimeParser.Parse(getStr(R.id.newgame__time));
                }catch(Exception e){
                    ((TextView)findViewById(R.id.newgame__time)).setError(view.getContext().getString(R.string.newgame__proper_time));
                }
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(AppController.getInstance().isAdditionalPlayer()){
            findViewById(R.id.newgame__change_account).setVisibility(View.INVISIBLE);
        }
        AppController.getInstance().setAdditionalPlayer(true);
    }
}