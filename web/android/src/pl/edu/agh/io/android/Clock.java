package pl.edu.agh.io.android;

/**
 * Created with IntelliJ IDEA.
 * User: Michał Janiec
 * Date: 3/25/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class Clock extends Activity {

    private static int timeleft;
    private static int players;
    private ArrayAdapter<User> adapter;

    public static void setTimeleft(int timeleft){
        Clock.timeleft=timeleft;
    }

    public static void setPlayers(int players){
        Clock.players=players;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clock);

        UsersManager manager = UsersManager.getUsersManager();
        TextView current_player= (TextView)findViewById(R.id.current);
        current_player.setText(manager.getCurrentName());

        User.setDisplay((TextView)findViewById(R.id.clock));

        manager.configure(players, timeleft);


        final Button start = (Button) findViewById(R.id.next);
        start.setText(manager.getButtonCaption());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsersManager manager=UsersManager.getUsersManager();
                manager.rotate();

                TextView current_player= (TextView)findViewById(R.id.current);
                current_player.setText(manager.getCurrentName());

                start.setText(manager.getButtonCaption());
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ArrayAdapter<User>(getBaseContext(), android.R.layout.simple_list_item_1,manager.getUsers());
        ListView queue = (ListView)findViewById(R.id.queue);
        queue.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.clock, menu);
        return true;
    }

}
