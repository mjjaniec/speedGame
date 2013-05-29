package pl.edu.agh.io.android.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import pl.edu.agh.io.android.adapters.DragNDropAdapter;
import pl.edu.agh.io.android.controller.AppState;
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
        AppState.getInstance().setAdditionalPlayer(true);

        adapter = new DragNDropAdapter(this, controller.getUsers());

        adapter.setOnDropListener(new DragNDropAdapter.DropListener() {

            @Override
            public void onDrop(int from, int to) {
                controller.swapPlayers(from, to);
                adapter.notifyDataSetChanged();
            }

        });

        adapter.setOnRemoveListener(new DragNDropAdapter.RemoveListener() {
            @Override
            public void onRemove(int row) {
                controller.removeUser(row);
                adapter.notifyDataSetChanged();
            }

        });

        ((ListView) findViewById(R.id.users__list)).setAdapter(adapter);

        findViewById(R.id.users__play).setOnClickListener(onPlay);

        findViewById(R.id.users__add).setOnClickListener(onAdd);
    }

    private View.OnClickListener onPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (UsersController.getInstance().getGameEnds()) {
                case one:
                    if (UsersController.getInstance().getUsers().size() < 2) {
                        new AlertDialog.Builder(view.getContext()).
                                setPositiveButton(R.string.common__ok, null).
                                setMessage(R.string.users__at_least_two).
                                show();
                        return;
                    }
                    break;
                case none:
                    if (UsersController.getInstance().getUsers().size() < 1) {
                        new AlertDialog.Builder(view.getContext()).
                                setPositiveButton(R.string.common__ok, null).
                                setMessage(R.string.users__at_least_one).
                                show();
                        return;
                    }
                default:
                    assert false;
            }

            startActivity(new Intent(view.getContext(), GameActivity.class));
        }
    };


    private View.OnClickListener onAdd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AppState.getInstance().setCreatingNewAccount(false);
            Class<?> intent;
            if (SpeedGameProxy.getInstance().isOnline()) {
                intent = LoginActivity.class;
            } else {
                intent = OfflineActivity.class;
            }
            Intent newIntent = new Intent(view.getContext(), intent);
            startActivity(newIntent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
