package pl.edu.agh.io.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String playersString =((TextView)findViewById(R.id.players)).getText().toString();
                String timeString=((TextView)findViewById(R.id.time)).getText().toString();
                int players = !playersString.equals("")?Integer.parseInt(playersString):2;
                int timeleft = !timeString.equals("")?Integer.parseInt(timeString):5;
                if(players>20)players=20;
                if(players<2)players=2;

                Clock.setTimeleft(timeleft*60);
                Clock.setPlayers(players);

                Intent myIntent = new Intent(view.getContext(),Clock.class);
                startActivityForResult(myIntent,0);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        UsersManager.reset();
    }

}
