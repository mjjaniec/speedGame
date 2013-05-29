package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import pl.edu.agh.io.android.adapters.UsersViewAdapter;
import pl.edu.agh.io.android.controller.AppState;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.misc.ViewTextSetter;
import pl.edu.agh.io.android.model.User;

public class GameActivity extends AbstractActivity {

    private ImageView avatar;
    private TextView clock;
    private TextView info;
    private ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        UsersController controller = UsersController.getInstance();
        controller.configure(this);

        findViewById(R.id.game__loose).setVisibility(View.INVISIBLE);

        adapter = new UsersViewAdapter(this, controller.getUsers());
        ListView queue = (ListView) findViewById(R.id.game__queue);
        queue.setAdapter(adapter);

        queue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                handleClick();
            }
        });
        findViewById(R.id.game__next).setOnClickListener(callHandleClick);
        findViewById(R.id.game__next2).setOnClickListener(callHandleClick);

        avatar = (ImageView) findViewById(R.id.game__avatar);
        clock = (TextView) findViewById(R.id.game__clock);
        info = (TextView) findViewById(R.id.game__info);

        info.setText(controller.getInfoStringId());

        findViewById(R.id.game__loose).setOnClickListener(onLoosePressed);
    }

    private View.OnClickListener callHandleClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            handleClick();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        UsersController.getInstance().setGameActivity(this);
        if (AppState.getInstance().isGameStarted()) {
            refreshScene();
        }
    }

    private void refreshScene() {
        findViewById(R.id.game__loose).setVisibility(View.VISIBLE);
        UsersController controller = UsersController.getInstance();
        User current = controller.getCurrent();

        setStr(R.id.game__current, current.getName());

        info.setText(controller.getInfoStringId());
        avatar.setImageDrawable(current.getAvatar());

        adapter.notifyDataSetChanged();
        clock.setText(current.timeString());

    }

    private View.OnClickListener onLoosePressed = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle(R.string.game__really_loose)
                    .setPositiveButton(R.string.common__yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UsersController.getInstance().getCurrent().setLost();
                        }
                    }).setNegativeButton(R.string.common__no, null).show();
        }
    };

    @Override
    public void onBackPressed() {
        UsersController.getInstance().pauseGame();
        new AlertDialog.Builder(this).setTitle(R.string.game__pause)
                .setMessage(R.string.game__back_to_game).
                setPositiveButton(R.string.common__yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UsersController.getInstance().resumeGame();
                    }
                }).setNegativeButton(R.string.common__no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UsersController.getInstance().restoreUsersList();
                endGame();
            }
        }
        ).show();
    }

    public void endGame() {
        startActivity(new Intent(this, RanksActivity.class));
    }

    public void handleClick() {
        AppState.getInstance().setGameStarted(true);
        UsersController controller = UsersController.getInstance();
        controller.rotateUsers();

        refreshScene();
    }

    public void refreshTime(String s) {
        runOnUiThread(new ViewTextSetter(clock, s));
    }
}
