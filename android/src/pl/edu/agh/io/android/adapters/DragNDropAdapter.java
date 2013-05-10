package pl.edu.agh.io.android.adapters;
/*
 * Copyright (C) 2010 Eric Harlow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.model.User;

import java.util.List;

public final class DragNDropAdapter extends BaseAdapter {

    public static interface DropListener {
        void onDrop(int from, int to);
    }


    public static interface RemoveListener {
        void onRemove(int which);
    }

    private DropListener onDropListener = null;
    private RemoveListener onRemoveListener = null;

    private final Context context;
    private final List<User> users;

    public DragNDropAdapter(Context context, List<User> users) {
        super();
        this.users=users;
        this.context=context;

    }

    public void setOnDropListener(DropListener onDropListener) {
        this.onDropListener = onDropListener;
    }

    public void setOnRemoveListener(RemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    /*internal*/ void onDrop(int form, int to){
        if(onDropListener!=null)
            onDropListener.onDrop(form,to);
    }



    /**
     * The number of items in the list
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        return users.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficient to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public User getItem(int position) {
        return users.get(position);
    }

    /**
     * Use the array index as a unique id.
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_dnd_usersview, parent, false);

        User user = users.get(position);
        TextView name = (TextView) rowView.findViewById(R.id.dnd_usersview__name);
        TextView time = (TextView) rowView.findViewById(R.id.dnd_usersview__time);
        ImageButton remove = (ImageButton) rowView.findViewById(R.id.dnd_usersview__remove);
        ImageView avatar = (ImageView) rowView.findViewById(R.id.dnd_usersview__avatar);


        avatar.setImageDrawable(user.getAvatar());
        name.setText(user.getName());

        if (user.isLost()) {
            time.setText(R.string.game__lost);
            time.setTextColor(Color.RED);
        } else {
            time.setText(user.timeString());
            if (user.isNegative()) {
                time.setTextColor(Color.RED);
            }
        }

        remove.setOnClickListener(new Handler(position));

        return rowView;
    }

    private class Handler implements View.OnClickListener {

        private int row;

        public Handler(int row) {
            this.row = row;
        }

        @Override
        public void onClick(View view) {
            if (onRemoveListener != null)
                onRemoveListener.onRemove(row);
        }
    }



}