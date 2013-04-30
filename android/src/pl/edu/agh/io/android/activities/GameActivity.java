package pl.edu.agh.io.android.activities;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import pl.edu.agh.io.android.model.User;
import pl.edu.agh.io.android.controller.UsersController;

public class GameActivity extends AbstractActivity {
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


        UsersController controller = UsersController.getUsersController();
        TextView current_player = (TextView) findViewById(R.id.game__current);
        current_player.setText(controller.getCurrentName());

        User.setDisplay((TextView) findViewById(R.id.game__clock));

        controller.configure(players, timeLeft);


        final Button start = (Button) findViewById(R.id.game__next);
        start.setText(controller.getButtonCaption());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsersController controller = UsersController.getUsersController();
                controller.rotate();

                TextView current_player = (TextView) findViewById(R.id.game__current);
                current_player.setText(controller.getCurrentName());

                start.setText(controller.getButtonCaption());
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ArrayAdapter<User>(getBaseContext(), android.R.layout.simple_list_item_1, controller.getUsers());
        ListView queue = (ListView) findViewById(R.id.game__queue);
        queue.setAdapter(adapter);
    }
}
