package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.AppState;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.controller.tasks.LoginTask;
import pl.edu.agh.io.android.misc.ViewTextSetter;
import pl.edu.agh.io.android.model.User;

public class LoginActivity extends AbstractActivity {
    private LoginActivity self;
    private AppState state = AppState.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppState.getInstance().setCreatingNewAccount(true);

        self = this;

        setContentView(R.layout.activity_login);

        findViewById(R.id.login__login_btn).setOnClickListener(onLogin);
        findViewById(R.id.login__new).setOnClickListener(onNewAccount);
        findViewById(R.id.login__play).setOnClickListener(onPlay);
    }

    private View.OnClickListener onPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AppState.getInstance().setLogged(false);
            UsersController.getInstance().
                    addUser(new User(getStr(R.id.login__login)));
            afterUserAdded();
        }
    };

    private View.OnClickListener onNewAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(view.getContext(), RegisterActivity.class));
        }
    };

    private View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(state.isBusy())return;

            state.setBusy(true);
            String login = ((TextView) findViewById(R.id.login__login)).getText().toString();
            String password = ((TextView) findViewById(R.id.login__password)).getText().toString();
            SpeedGameProxy.getInstance().login(self, login, password);
        }
    };

    private void afterUserAdded() {
        if (AppState.getInstance().isAdditionalPlayer())
            finish();
        else {
            startActivity(new Intent(this, NewGameActivity.class));
        }
    }

    public void onLogin(LoginTask.LoginResult result) {
        state.setBusy(false);
        final TextView loginInfo = (TextView) findViewById(R.id.login__info);

        switch (result) {
            case OK:
                AppState.getInstance().setLogged(true);
                afterUserAdded();
                break;
            case ERROR:
                runOnUiThread(new ViewTextSetter(loginInfo, R.string.login__error));
                break;
            case INVALID_PASSWORD:
            case INVALID_LOGIN:
                runOnUiThread(new ViewTextSetter(loginInfo, R.string.login__invalid_login_or_password));
                break;
            default:
                assert (false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        state.setBusy(false);
    }
}
