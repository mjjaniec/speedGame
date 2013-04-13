package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.misc.SetText;
import pl.edu.agh.io.android.misc.TextValidator;
import pl.edu.agh.io.android.model.SpeedGameProxy;
import pl.edu.agh.io.android.model.tasks.RegisterTask;

public class NewAccountActivity extends AbstractActivity{

    private View pendingView;
    NewAccountActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaccount);

        that=this;

        EditText login = (EditText) findViewById(R.id.newaccount__login);
        EditText password = (EditText) findViewById(R.id.newaccount__password);
        EditText retype = (EditText) findViewById(R.id.newaccount__retype);
        EditText email = (EditText) findViewById(R.id.newaccount__email);

        String basicRegex = "[a-z][0-9a-z_]+";
        String emailRegex = "([a-z][a-z0-9_]?\\.)*([a-z][a-z0-9_]?)@([a-z][a-z0-9_]?\\.)+([a-z][a-z0-9_]?)";


        login.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16, "Username may consist of a-z, 0-9, underscores, begin with a letter."));
        password.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16,  "Password may consist of a-z, 0-9, underscores, begin with a letter."));
        email.setOnFocusChangeListener(new TextValidator(emailRegex, 6, 80, "Please enter valid email. eg. abc@example.com"));

        retype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    EditText retype = (EditText) view;
                    EditText pass = (EditText) findViewById(R.id.newaccount__password);

                    if(! pass.getText().toString().equals(retype.getText().toString())){
                        retype.setError("Retyped password should match password");
                    }
                 }
            }
        });

        Button register = (Button) findViewById(R.id.newaccount__register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingView = view;
                String login = getStr(R.id.newaccount__login);
                String password = getStr(R.id.newaccount__password);
                String email = getStr(R.id.newaccount__email);
                String avatar = "avatar1";
                String ring = "ring2";
                SpeedGameProxy.getInstance().register(that,login,password,email,avatar,ring);
            }
        });

        findViewById(R.id.newaccount__change_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFileActivity.setWhat(getString(R.string.newaccount__choose_avatar));
                ChooseFileActivity.setCallback(new FieldSetter((TextView)findViewById(R.id.newaccount__avatar)));
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        findViewById(R.id.newaccount__change_ring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFileActivity.setWhat(getString(R.string.newaccount__choose_ring));
                ChooseFileActivity.setCallback(new FieldSetter((TextView)findViewById(R.id.newaccount__ring)));
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    private class FieldSetter implements IProcedure<String>{
        private TextView view;

        public FieldSetter(TextView view){
            this.view=view;
        }

        @Override
        public void call(String arg) {
            runOnUiThread(new SetText(view,arg));
        }
    }

    public void onRegister(RegisterTask.RegisterResult result){
        final TextView loginInfo = (TextView) findViewById(R.id.newaccount__info);

        switch (result){
            case OK:
                Intent myIntent = new Intent(pendingView.getContext(), NewGameActivity.class);
                startActivityForResult(myIntent, 0);
                break;
            case ERROR:
                runOnUiThread(new SetText(loginInfo,R.string.login__error));
                break;
            case USER_EXISTS:
                runOnUiThread(new SetText(loginInfo, R.string.login__invalid_password));
                break;
            default: assert(false);
        }

    }
}
