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
import android.widget.Button;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.controller.tasks.LoginTask;
import pl.edu.agh.io.android.misc.SetText;
import pl.edu.agh.io.android.model.User;

public class LoginActivity extends AbstractActivity {
    private LoginActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppController.getInstance().setNew(true);

        that=this;

        setContentView(R.layout.activity_login);

        Button login_btn = (Button) findViewById(R.id.login__login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = ((TextView) findViewById(R.id.login__login)).getText().toString();
                String password = ((TextView) findViewById(R.id.login__password)).getText().toString();
                SpeedGameProxy.getInstance().login(that,login,password);
            }
        });

        Button new_btn = (Button) findViewById(R.id.login__new);
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), NewAccountActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        findViewById(R.id.login__play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setLogged(false);
                UsersController.getInstance().
                        addUser(new User(getStr(R.id.login__login),view.getContext()));
                onOK();
            }
        });
    }

    private void onOK(){
        if(AppController.getInstance().isAdditionalPlayer())
            finish();
        else{
            Intent myIntent = new Intent(this, NewGameActivity.class);
            startActivityForResult(myIntent, 0);
        }
    }

    public void onLogin(LoginTask.LoginResult result) {
        final TextView loginInfo = (TextView) findViewById(R.id.login__info);

        switch (result){
            case OK:
               // UsersController.getInstance().addUser(new User(getStr(R.id.login__login),this));
                AppController.getInstance().setLogged(true);
                onOK();
                break;
            case ERROR:
                runOnUiThread(new SetText(loginInfo,R.string.login__error));
                break;
            case INVALID_PASSWORD:
                runOnUiThread(new SetText(loginInfo, R.string.login__invalid_password));
                break;
            case INVALID_LOGIN:
                runOnUiThread(new SetText(loginInfo, R.string.login__invalid_login));
                break;
            default: assert(false);
        }

    }


}
