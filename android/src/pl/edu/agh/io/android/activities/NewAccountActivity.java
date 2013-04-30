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
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.tasks.RegisterTask;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.misc.SetText;
import pl.edu.agh.io.android.misc.TextValidator;
import pl.edu.agh.io.android.model.FileItem;

import java.util.concurrent.atomic.AtomicBoolean;

public class NewAccountActivity extends AbstractActivity{

    private static class FileItemHolder{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaccount);

        info =  (TextView) findViewById(R.id.newaccount__info);
        login = (EditText) findViewById(R.id.newaccount__login);
        password = (EditText) findViewById(R.id.newaccount__password);
        retype = (EditText) findViewById(R.id.newaccount__retype);
        email = (EditText) findViewById(R.id.newaccount__email);

        String basicRegex = "[a-z][0-9a-z_]+";
        String emailRegex = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";


        login.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16, "Username may consist of a-z, 0-9, underscores, begin with a letter."));
        password.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16,  "Password may consist of a-z, 0-9, underscores, begin with a letter."));
        email.setOnFocusChangeListener(new TextValidator(emailRegex, 6, 80, "Please enter valid email. eg. abc@example.com"));

        retype.setOnFocusChangeListener(new TextValidator(basicRegex,5 ,16, "not used") {
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
                register();
            }
        });
        findViewById(R.id.newaccount__change_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFileActivity.setCallback(new FieldSetter((TextView)findViewById(R.id.newaccount__avatar),avatar));
                ChooseFileActivity.setWhat(R.string.newaccount__choose_avatar);
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        findViewById(R.id.newaccount__change_ring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFileActivity.setCallback(new FieldSetter((TextView)findViewById(R.id.newaccount__ring),ring));
                ChooseFileActivity.setWhat(R.string.newaccount__choose_ring);
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    public void register(){
        if(!checkAll()){
            info.setText(R.string.newaccount__validate);
        }
        else if(!ring.uploaded.get()){
            handleFile(ring,R.string.newaccount__default_ring);
        }
        else if(!avatar.uploaded.get()){
            handleFile(avatar,R.string.newaccount__default_avatar);
        }else{
            if(!checkAll()){
                info.setText(R.string.newaccount__validate);
            }else{
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

    private boolean checkAll(){
        return checkEdit(login) &&
                checkEdit(password) &&
                checkEdit(retype) &&
                checkEdit(email);
    }

    private void handleFile(FileItemHolder fileItemHolder, int defaultString) {
        if(fileItemHolder.fileItem==null){
            fileItemHolder.name = getString(defaultString);
            fileItemHolder.uploaded.set(true);
            register();
        }else{
            if(!fileItemHolder.touched.get()){
                fileItemHolder.touched.set(true);
                fileItemHolder.name=fileItemHolder.fileItem.getName();
                info.setText(R.string.newaccount__sending_file);
                SpeedGameProxy.getInstance().sendFile(this,fileItemHolder.fileItem);
            }
        }
    }

    private boolean checkEdit(EditText edit){
        TextValidator validator = (TextValidator) edit.getOnFocusChangeListener();
        return validator.validate(edit.getText().toString());
    }

    private class FieldSetter implements IProcedure<FileItem>{
        private TextView view;
        private FileItemHolder fileItemHolder;

        public FieldSetter(TextView view,FileItemHolder holder){
            this.fileItemHolder=holder;
            this.view=view;
        }

        @Override
        public void call(FileItem arg) {
            fileItemHolder.fileItem=arg;
            runOnUiThread(new SetText(view,arg.getName()));
        }
    }

    public void onRegister(RegisterTask.RegisterResult result){

        switch (result){
            case OK:
                Intent myIntent = new Intent(this, NewGameActivity.class);
                startActivityForResult(myIntent, 0);
                break;
            case ERROR:
                runOnUiThread(new SetText(info,R.string.newaccount__error));
                break;
            case USER_EXISTS:
                runOnUiThread(new SetText(info, R.string.newaccount__user_exists));
                break;
            default: assert(false);
        }

    }

    public void onUpload(boolean success,FileItem fileItem){
        if(success){
            if(avatar.name==fileItem.getName()){
                avatar.uploaded.set(true);
                register();
            }else if(ring.name==fileItem.getName()){
                ring.uploaded.set(true);
                register();
            }else{
                assert false:"bad file name?";
            }
        }else{
            runOnUiThread(new SetText(info,R.string.newaccount__error));
        }
    }
}
