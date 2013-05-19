package pl.edu.agh.io.android.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import pl.edu.agh.io.android.adapters.DragNDropAdapter;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/9/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersActivity extends AbstractActivity {

    private DragNDropAdapter adapter;
    private UsersController controller = UsersController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        AppController.getInstance().setAdditionalPlayer(true);

        adapter = new DragNDropAdapter(this,controller.getUsers());

        adapter.setOnDropListener(new DragNDropAdapter.DropListener(){

            @Override
            public void onDrop(int from,int to) {
                if(!AppController.getInstance().isAfterGame()){
                    controller.swap(from, to);
                    adapter.notifyDataSetChanged();
                }
            }

        });;

        adapter.setOnRemoveListener(new DragNDropAdapter.RemoveListener() {
            @Override
            public void onRemove(int row) {
                if(!AppController.getInstance().isAfterGame()){
                    controller.removeUser(row);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ((ListView)findViewById(R.id.users__list)).setAdapter(adapter);

        findViewById(R.id.users__play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AppController.getInstance().isAfterGame()){
                    if(UsersController.getInstance().getUsers().size()<2){
                        new AlertDialog.Builder(view.getContext()).
                                setPositiveButton(R.string.common__ok,null).
                                setMessage(R.id.game__more_than_one).
                                show();
                    } else {
                        Intent myIntent = new Intent(view.getContext(), GameActivity.class);
                        startActivity(myIntent);
                    }
                } else {
                    UsersController.reset();
                    finish();
                }
            }
        });

        findViewById(R.id.users__add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AppController.getInstance().isAfterGame()){
                    AppController.getInstance().setNew(false);
                    Class<?> intent;
                    if(SpeedGameProxy.getInstance().isOnline()){
                        intent=LoginActivity.class;
                    }else {
                        intent=OfflineActivity.class;
                    }
                    Intent newIntent = new Intent(view.getContext(),intent);
                    startActivity(newIntent);
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
        /*if(AppController.getInstance().isAfterGame()){
            AppController.getInstance().setAfterGame(false);
            finish();
        }*/
        /*
        if(AppController.getInstance().isAfterGame()){
            ((Button)findViewById(R.id.users__play)).setText(R.string.users__back);
            findViewById(R.id.users__add).setVisibility(View.INVISIBLE);
        }*/
    }

/*
    @Override
    protected void onDestroy() {
        AppController.getInstance().setAfterGame(false);
        super.onDestroy();
    }*/

}
