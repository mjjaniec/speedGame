package pl.edu.agh.io.android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.model.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/1/13
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemovableUsersViewAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final List<User> users;


    public RemovableUsersViewAdapter(Context context, List<User> values) {
        super(context, R.layout.row_usersview, values);

        this.context = context;
        this.users = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_removableusersview, parent, false);

        User user = users.get(position);
        TextView name = (TextView) rowView.findViewById(R.id.removableusersview__name);
        TextView time = (TextView) rowView.findViewById(R.id.removableusersview__time);
        Button remove = (Button) rowView.findViewById(R.id.removableusersview__remove);
        ImageView avatar = (ImageView) rowView.findViewById(R.id.removableusersview__avatar);


        avatar.setImageDrawable(user.getAvatar());
        name.setText(user.getName());

        if(user.isLost()){
            time.setText(R.string.game__lost);
            time.setTextColor(Color.RED);
        }else{
            time.setText(user.timeString());
            if(user.isNegative()){
                time.setTextColor(Color.RED);
            }
        }

        return rowView;
    }
}