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
import android.widget.EditText;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.AppState;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.controller.tasks.UpdateAndRegisterBase;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.misc.ViewTextSetter;
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

    private AppState state = AppState.getInstance();
    private FileItemHolder avatar = new FileItemHolder();
    private FileItemHolder ring = new FileItemHolder();
    private EditText login;
    private EditText password;
    private EditText retype;
    private EditText email;
    private TextView info;
    private boolean update;
    private User oldUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        update = !AppState.getInstance().isCreatingNewAccount();

        info = (TextView) findViewById(R.id.register__info);
        login = (EditText) findViewById(R.id.register__login);
        password = (EditText) findViewById(R.id.register__password);
        retype = (EditText) findViewById(R.id.register__retype);
        email = (EditText) findViewById(R.id.register__email);

        findViewById(R.id.register__avatar).setEnabled(false);
        findViewById(R.id.register__ring).setEnabled(false);

        if (update) {
            login.setEnabled(false);
            oldUser = UsersController.getInstance().getUsers().get(0);
            //updated user is always first user
            login.setText(oldUser.getName());
            setStr(R.id.register__avatar, "");
            setStr(R.id.register__ring, "");
            setStr(R.id.register__register, R.string.register__update);
        }


        findViewById(R.id.register__register).setOnClickListener(onRegister);
        findViewById(R.id.register__change_avatar).setOnClickListener(onChangeAvatar);
        findViewById(R.id.register__change_ring).setOnClickListener(onChangeRing);
    }

    private View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (state.isBusy())
                return;

            state.setBusy(true);
            register();
        }
    };

    private View.OnClickListener onChangeAvatar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AppState.getInstance().setFilesViewCallback(
                    new FieldSetter((TextView) findViewById(R.id.register__avatar), avatar));
            AppState.getInstance().setFilesViewTitleId(R.string.register__choose_avatar);
            startActivity(new Intent(view.getContext(), ChooseFileActivity.class));
        }
    };

    private View.OnClickListener onChangeRing = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AppState.getInstance().setFilesViewCallback(
                    new FieldSetter((TextView) findViewById(R.id.register__ring), ring));
            AppState.getInstance().setFilesViewTitleId(R.string.register__choose_ring);
            startActivity(new Intent(view.getContext(), ChooseFileActivity.class));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        state.setBusy(false);
        update = !AppState.getInstance().isCreatingNewAccount();

        String basicRegex = getString(R.string.config__basic_regex);
        String emailRegex = getString(R.string.config__email_regex);


        login.setOnFocusChangeListener(
                new TextValidator(basicRegex, 5, 16, update, getString(R.string.register__login_valid_error)));
        password.setOnFocusChangeListener(
                new TextValidator(basicRegex, 5, 16, update, getString(R.string.register__password_valid_error)));
        email.setOnFocusChangeListener(
                new TextValidator(emailRegex, 6, 80, update, getString(R.string.register__email_valid_error)));

        retype.setOnFocusChangeListener(new TextValidator(basicRegex, 5, 16, update, null) {

            @Override
            public boolean validate(String string) {
                return getStr(R.id.register__password).equals(getStr(R.id.register__retype));
            }

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (!validate(null)) {
                        retype.setError(getString(R.string.register__retype_valid_error));
                    }
                }
            }
        });
    }

    public void register() {
        if (!checkAll()) {
            info.setText(R.string.register__validate);
            state.setBusy(false);
            return;
        }
        if (!ring.uploaded.get()) {
            handleFile(ring, R.string.register__default_ring);
            return;
        }
        if (!avatar.uploaded.get()) {
            handleFile(avatar, R.string.register__default_avatar);
            return;
        }

        if (update)
            SpeedGameProxy.getInstance().update(
                    this,
                    login.getText().toString(),
                    password.getText().toString(),
                    email.getText().toString(),
                    avatar.name,
                    ring.name
            );
        else {
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

    private boolean checkAll() {
        return checkEdit(login) &&
                checkEdit(password) &&
                checkEdit(retype) &&
                checkEdit(email);
    }

    private void handleFile(FileItemHolder fileItemHolder, int defaultString) {
        if (fileItemHolder.fileItem == null) {
            if (update == false) {
                fileItemHolder.name = getString(defaultString);
            }
            fileItemHolder.uploaded.set(true);
            register();
        } else {
            if (!fileItemHolder.touched.get()) {
                fileItemHolder.touched.set(true);
                fileItemHolder.name = fileItemHolder.fileItem.getName();
                runOnUiThread(new ViewTextSetter(info, R.string.register__sending_file));
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
            runOnUiThread(new ViewTextSetter(view, arg.getName()));
        }
    }

    private URL formFileURL(FileItem item) {
        try {
            return new URL("file:// " + item.getPath());
        } catch (MalformedURLException e) {
            Log.e("URL", e.toString());
            return null;
        }
    }

    private void updateUser() {
        UsersController controller = UsersController.getInstance();
        //updated user is always first user
        controller.removeUser(0);

        controller.addUser(new User(
                oldUser.getName(),
                avatar.fileItem != null ? Drawable.createFromPath(avatar.fileItem.getPath()) : oldUser.getAvatar(),
                ring.fileItem != null ? formFileURL(ring.fileItem) : oldUser.getRingURL()
        ));

        finish();
    }

    private void addUser() {

        UsersController.getInstance().addUser(new User(
                getStr(R.id.register__login),
                avatar.fileItem != null ? Drawable.createFromPath(avatar.fileItem.getPath()) : null,
                ring.fileItem != null ? formFileURL(ring.fileItem) : null
        ));

        Intent intent = new Intent(this, UsersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onSuccessfulRegistration() {

        if (update) {
            updateUser();
        } else {
            addUser();
        }
    }

    public void onRegister(UpdateAndRegisterBase.RegisterResult result) {
        state.setBusy(false);
        switch (result) {
            case OK:
                onSuccessfulRegistration();
                break;
            case ERROR:
                runOnUiThread(new ViewTextSetter(info, R.string.register__error));
                break;
            case USER_EXISTS:
                runOnUiThread(new ViewTextSetter(info, R.string.register__user_exists));
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
            runOnUiThread(new ViewTextSetter(info, R.string.register__error));
        }
    }
}
