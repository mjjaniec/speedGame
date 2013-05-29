package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import pl.edu.agh.io.android.controller.AppState;
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

        Spinner onTimeout = ((Spinner) findViewById(R.id.newgame__on_timeout));
        onTimeout.setAdapter(new ArrayAdapter<UsersController.OnTimeout>(
                this, android.R.layout.simple_spinner_item, UsersController.OnTimeout.values()
        ));

        Spinner gameEnds = (Spinner) findViewById(R.id.newgame__game_ends);
        gameEnds.setAdapter(new ArrayAdapter<UsersController.GameEnds>(
                this, android.R.layout.simple_spinner_item, UsersController.GameEnds.values()
        ));

        initializeChangeAccountButton();


        findViewById(R.id.newgame__next).setOnClickListener(onNext);
        findViewById(R.id.newgame__time).setOnFocusChangeListener(timeValidator);
    }

    private View.OnFocusChangeListener timeValidator = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            try {
                getTime();
            } catch (Exception e) {
                ((TextView) findViewById(R.id.newgame__time)).setError(view.getContext().getString(R.string.newgame__proper_time));
            }
        }
    };


    private int getTime() throws Exception {
        String timeString = getStr(R.id.newgame__time);

        if (timeString.equals("")) {
            getString(R.string.config__default_time);
        }
        return TimeParser.Parse(timeString);
    }

    private View.OnClickListener onNext = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int timeLeft;
            try {
                timeLeft = getTime();
            } catch (Exception e) {
                ((TextView) findViewById(R.id.newgame__time)).setError(view.getContext().getString(R.string.newgame__proper_time));
                return;
            }

            UsersController controller = UsersController.getInstance();

            CheckBox sound = ((CheckBox) findViewById(R.id.newgame__sound));
            controller.setSoundOn(UsersController.Sound.fromBoolean(sound.isChecked()));

            Spinner onTimeout = ((Spinner) findViewById(R.id.newgame__on_timeout));
            controller.setOnTimeout(UsersController.OnTimeout.fromString(onTimeout.getSelectedItem().toString()));

            Spinner gameEnds = (Spinner) findViewById(R.id.newgame__game_ends);
            controller.setGameEnds(UsersController.GameEnds.fromString(gameEnds.getSelectedItem().toString()));

            controller.setTime(timeLeft);

            AppState.getInstance().setGameStarted(false);

            startActivity(new Intent(view.getContext(), UsersActivity.class));
        }
    };

    private void initializeChangeAccountButton() {
        if (!SpeedGameProxy.getInstance().isOnline() || !AppState.getInstance().isLogged()) {
            findViewById(R.id.newgame__change_account).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.newgame__change_account).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppState.getInstance().setCreatingNewAccount(false);
                    Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AppState.getInstance().reset();
        AppState.getInstance().isFirstTime();
        UsersController.reset();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppState.getInstance().isAdditionalPlayer()) {
            findViewById(R.id.newgame__change_account).setVisibility(View.INVISIBLE);
        }
        AppState.getInstance().setAdditionalPlayer(true);
    }
}