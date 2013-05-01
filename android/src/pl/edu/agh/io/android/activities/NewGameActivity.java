package pl.edu.agh.io.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameActivity extends AbstractActivity {

    private final int maxUsers = 200;
    private final int minUsers = 2;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);
        context = this;

        Button start = (Button) findViewById(R.id.newgame__start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String playersString =((TextView)findViewById(R.id.newgame__players)).getText().toString();
                String timeString=((TextView)findViewById(R.id.newgame__time)).getText().toString();
                int players = !playersString.equals("")?Integer.parseInt(playersString):2;
                int timeLeft = !timeString.equals("")?Integer.parseInt(timeString):5;
                if(players>maxUsers)players=maxUsers;
                if(players<minUsers)players=minUsers;

                UsersController controller = UsersController.getUsersController();

                for(int i=0; i<players; ++i)
                    controller.addUser(new User("Player "+(i+1),context));

                controller.setTime(timeLeft);

                Intent myIntent = new Intent(view.getContext(),GameActivity.class);
                startActivityForResult(myIntent,0);
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        UsersController.reset();
    }
}