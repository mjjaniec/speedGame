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
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import pl.edu.agh.io.android.adapters.UsersViewAdapter;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.model.User;

public class GameActivity extends AbstractActivity {

    private ImageView avatar;
    private View next;
    private TextView caption;
    private ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        UsersController controller = UsersController.getInstance();
        TextView current_player = (TextView) findViewById(R.id.game__current);
        current_player.setText("");


        controller.configure(this);

        findViewById(R.id.game__loose).setVisibility(View.INVISIBLE);

        adapter = new UsersViewAdapter(this,controller.getUsers());
        ListView queue = (ListView) findViewById(R.id.game__queue);
        queue.setAdapter(adapter);

        queue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                handleClick();
            }
        });

        avatar = (ImageView)findViewById(R.id.game__avatar);
        next = findViewById(R.id.game__next);
        caption = (TextView) findViewById(R.id.game__caption);


        caption.setText(controller.getButtonCaption());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick();
            }
        });

        findViewById(R.id.game__loose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.game__really_loose)
                        .setPositiveButton(R.string.common__yes,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UsersController.getInstance().getCurrent().setLost();
                                handleClick();
                            }
                        }).setNegativeButton(R.string.common__no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //pass
                    }
                }).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        UsersController.getInstance().pause();
        new AlertDialog.Builder(this).setTitle(R.id.game__pause)
                .setMessage(R.id.game__back_to_game).
                setPositiveButton(R.string.common__yes,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UsersController.getInstance().unpause();
            }
        }).setNegativeButton(R.string.common__no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UsersController.getInstance().endGame();
            }
        }).show();
        //super.onBackPressed();
    }

    private void handleClick(){
        findViewById(R.id.game__loose).setVisibility(View.VISIBLE);
        UsersController controller = UsersController.getInstance();
        controller.rotate();
        User current = controller.getCurrent();

        TextView current_player = (TextView) findViewById(R.id.game__current);
        current_player.setText(current.getName());

        caption.setText(controller.getButtonCaption());
        avatar.setImageDrawable(current.getAvatar());

        adapter.notifyDataSetChanged();
    }
}
