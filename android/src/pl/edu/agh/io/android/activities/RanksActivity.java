package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import pl.edu.agh.io.android.adapters.UsersViewAdapter;
import pl.edu.agh.io.android.controller.UsersController;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/28/13
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class RanksActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks);

        ((ListView)findViewById(R.id.ranks__list)).setAdapter(
                new UsersViewAdapter(this,UsersController.getInstance().getUsers()));

        findViewById(R.id.ranks__ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
    }

    private void exit(){
        UsersController.reset();

        Intent intent = new Intent(this, NewGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
