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
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.adapters.UsersViewAdapter;
import pl.edu.agh.io.android.model.User;

public class GameActivity extends AbstractActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        UsersController controller = UsersController.getUsersController();
        TextView current_player = (TextView) findViewById(R.id.game__current);
        current_player.setText("");


        controller.configure(this);

        final ArrayAdapter<User> adapter = new UsersViewAdapter(this,controller.getUsers());
        ListView queue = (ListView) findViewById(R.id.game__queue);
        queue.setAdapter(adapter);


        final Button start = (Button) findViewById(R.id.game__next);
        start.setText(controller.getButtonCaption());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsersController controller = UsersController.getUsersController();
                controller.rotate();
                User current = controller.getCurrent();

                TextView current_player = (TextView) findViewById(R.id.game__current);
                current_player.setText(current.getName());

                start.setText(controller.getButtonCaption());
                start.setBackground(current.getAvatar());

                adapter.notifyDataSetChanged();
            }
        });
    }
}
