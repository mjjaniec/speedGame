package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import pl.edu.agh.io.android.model.User;
import pl.edu.agh.io.android.model.UsersManager;

public class GameActivity extends Activity {
    private static int timeLeft;
    private static int players;
    private ArrayAdapter<User> adapter;

    public static void setTimeLeft(int timeLeft) {
        GameActivity.timeLeft = timeLeft;
    }

    public static void setPlayers(int players) {
        GameActivity.players = players;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        UsersManager manager = UsersManager.getUsersManager();
        TextView current_player = (TextView) findViewById(R.id.game__current);
        current_player.setText(manager.getCurrentName());

        User.setDisplay((TextView) findViewById(R.id.game__clock));

        manager.configure(players, timeLeft);


        final Button start = (Button) findViewById(R.id.game__next);
        start.setText(manager.getButtonCaption());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsersManager manager = UsersManager.getUsersManager();
                manager.rotate();

                TextView current_player = (TextView) findViewById(R.id.game__current);
                current_player.setText(manager.getCurrentName());

                start.setText(manager.getButtonCaption());
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ArrayAdapter<User>(getBaseContext(), android.R.layout.simple_list_item_1, manager.getUsers());
        ListView queue = (ListView) findViewById(R.id.game__queue);
        queue.setAdapter(adapter);
    }
}
