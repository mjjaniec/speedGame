package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.controller.tasks.UsersTaskBase;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.misc.SetText;
import pl.edu.agh.io.android.misc.TextValidator;
import pl.edu.agh.io.android.model.FileItem;
import pl.edu.agh.io.android.model.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterActivity extends AbstractActivity {

    private static class FileItemHolder {
        public String name;
        public FileItem fileItem = null;
        public AtomicBoolean touched = new AtomicBoolean(false);
        public AtomicBoolean uploaded = new AtomicBoolean(false);
    }

    private FileItemHolder avatar = new FileItemHolder();
    private FileItemHolder ring = new FileItemHolder();
    private EditText login;
    private EditText password;
    private EditText retype;
    private EditText email;
    private TextView info;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        update = !AppController.getInstance().isNew();

        info = (TextView) findViewById(R.id.register__info);
        login = (EditText) findViewById(R.id.register__login);
        password = (EditText) findViewById(R.id.register__password);
        retype = (EditText) findViewById(R.id.register__retype);
        email = (EditText) findViewById(R.id.register__email);

        findViewById(R.id.register__avatar).setEnabled(false);
        findViewById(R.id.register__ring).setEnabled(false);

        if (update) {
            login.setEnabled(false);
            login.setText(UsersController.getInstance().getCurrent().getName());
            setStr(R.id.register__avatar, "");
            setStr(R.id.register__ring, "");
            setStr(R.id.register__register, R.string.register__update);

            UsersController.reset();
        }


        Button register = (Button) findViewById(R.id.register__register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        findViewById(R.id.register__change_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setCallback(
                        new FieldSetter((TextView) findViewById(R.id.register__avatar), avatar));
                AppController.getInstance().setWhat(R.string.register__choose_avatar);
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


        findViewById(R.id.register__change_ring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setCallback(
                        new FieldSetter((TextView) findViewById(R.id.register__ring), ring));
                AppController.getInstance().setWhat(R.string.register__choose_ring);
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        update = !AppController.getInstance().isNew();

        String basicRegex = "[a-z][0-9a-z_]+";
        String emailRegex = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";


        login.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16, update, "Username may consist of a-z, 0-9, underscores, begin with a letter."));
        password.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16, update, "Password may consist of a-z, 0-9, underscores, begin with a letter."));
        email.setOnFocusChangeListener(new TextValidator(emailRegex, 6, 80, update, "Please enter valid email. eg. abc@example.com"));

        retype.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16, update, "not used") {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    EditText retype = (EditText) view;
                    EditText pass = (EditText) findViewById(R.id.register__password);

                    if (!pass.getText().toString().equals(retype.getText().toString())) {
                        retype.setError("Retyped password should match password");
                    }
                }
            }
        });
    }

    public void register() {
        if (!checkAll()) {
            info.setText(R.string.register__validate);
        } else if (!ring.uploaded.get()) {
            handleFile(ring, R.string.register__default_ring);
        } else if (!avatar.uploaded.get()) {
            handleFile(avatar, R.string.register__default_avatar);
        } else {
            if (!checkAll()) {
                info.setText(R.string.register__validate);
            } else {
                if(update)
                    SpeedGameProxy.getInstance().update(
                            this,
                            login.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString(),
                            avatar.name,
                            ring.name
                    );
                else{
                    SpeedGameProxy.getInstance().register(
                            this,
                            login.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString(),
                            avatar.name,
                            ring.name
                    );
                }
            }
        }
    }

    private boolean checkAll() {
        return checkEdit(login) &&
                checkEdit(password) &&
                checkEdit(retype) &&
                checkEdit(email);
    }

    private void handleFile(FileItemHolder fileItemHolder, int defaultString) {
        if (fileItemHolder.fileItem == null) {
            if(update == false){
                fileItemHolder.name = getString(defaultString);
            }
            fileItemHolder.uploaded.set(true);
            register();
        } else {
            if (!fileItemHolder.touched.get()) {
                fileItemHolder.touched.set(true);
                fileItemHolder.name = fileItemHolder.fileItem.getName();
                runOnUiThread(new SetText(info,R.string.register__sending_file));
                SpeedGameProxy.getInstance().sendFile(this, fileItemHolder.fileItem);
            }
        }
    }

    private boolean checkEdit(EditText edit) {
        TextValidator validator = (TextValidator) edit.getOnFocusChangeListener();
        return validator.validate(edit.getText().toString());
    }

    private class FieldSetter implements IProcedure<FileItem> {
        private TextView view;
        private FileItemHolder fileItemHolder;

        public FieldSetter(TextView view, FileItemHolder holder) {
            this.fileItemHolder = holder;
            this.view = view;
        }

        @Override
        public void call(FileItem arg) {
            fileItemHolder.fileItem = arg;
            runOnUiThread(new SetText(view, arg.getName()));
        }
    }

    public void onRegister(UsersTaskBase.RegisterResult result) {

        switch (result) {
            case OK:
                try{
                    UsersController.getInstance().addUser(new User(
                            getStr(R.id.register__login), this,
                            Drawable.createFromPath(avatar.fileItem.getPath()),
                            new URL("file://"+ring.fileItem.getPath())
                    ));
                }catch(MalformedURLException e){
                    Log.e("URL", e.toString());
                }
                if(AppController.getInstance().isAdditionalPlayer()){
                    Intent myIntent = new Intent(this, UsersActivity.class);
                    startActivityForResult(myIntent, 0);
                }else {
                    Intent myIntent = new Intent(this, NewGameActivity.class);
                    startActivityForResult(myIntent, 0);
                }
                break;
            case ERROR:
                runOnUiThread(new SetText(info, R.string.register__error));
                break;
            case USER_EXISTS:
                runOnUiThread(new SetText(info, R.string.register__user_exists));
                break;
            default:
                assert (false);
        }

    }

    public void onUpload(boolean success, FileItem fileItem) {
        if (success) {
            if (avatar.name == fileItem.getName()) {
                avatar.uploaded.set(true);
                register();
            } else if (ring.name == fileItem.getName()) {
                ring.uploaded.set(true);
                register();
            } else {
                assert false : "bad file name?";
            }
        } else {
            runOnUiThread(new SetText(info, R.string.register__error));
        }
    }
}
