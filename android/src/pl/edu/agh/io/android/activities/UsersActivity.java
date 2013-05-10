package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import pl.edu.agh.io.android.adapters.DragNDropAdapter;
import pl.edu.agh.io.android.controller.SpeedGameProxy;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/9/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersActivity extends AbstractActivity {

    private DragNDropAdapter adapter;
    private UsersController controller = UsersController.getUsersController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users);

        for(int i=0; i<5; ++i)
            controller.addUser(new User("sex "+i,this));


        adapter = new DragNDropAdapter(this,controller.getUsers());

        adapter.setOnDropListener(new DragNDropAdapter.DropListener(){

            @Override
            public void onDrop(int from,int to) {
                controller.swap(from, to);
                adapter.notifyDataSetChanged();
            }

        });;

        adapter.setOnRemoveListener(new DragNDropAdapter.RemoveListener() {
            @Override
            public void onRemove(int row) {
                controller.removeUser(row);
                adapter.notifyDataSetChanged();
            }
        });

        ((ListView)findViewById(R.id.users__list)).setAdapter(adapter);

        findViewById(R.id.users__play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(view.getContext(), GameActivity.class);
                startActivity(myIntent);
            }
        });

        findViewById(R.id.users__add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> intent;
                if(SpeedGameProxy.getInstance().isOnline()){
                    intent=LoginActivity.class;
                }else {
                    intent=OfflineActivity.class;
                }

                Intent newIntent = new Intent(view.getContext(),intent);
                startActivity(newIntent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
