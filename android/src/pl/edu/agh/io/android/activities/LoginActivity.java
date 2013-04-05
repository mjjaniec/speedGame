package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.edu.agh.io.android.model.SpeedGameProxy;


public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn = (Button) findViewById(R.id.login__login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = ((TextView)findViewById(R.id.login__login)).getText().toString();
                String password = ((TextView)findViewById(R.id.login__password)).getText().toString();

                if(SpeedGameProxy.getInstance().Login(login,password)){
                    Intent myIntent = new Intent(view.getContext(),NewGameActivity.class);
                    startActivityForResult(myIntent,0);
                }else{
                    TextView info = ((TextView)findViewById(R.id.login__info));
                    info.setText(R.string.login__invalid);
                }
            }
        });

        Button new_btn = (Button) findViewById(R.id.login__new);
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),NewAccountActivity.class);
                startActivityForResult(myIntent,0);
            }
        });
    }
}
