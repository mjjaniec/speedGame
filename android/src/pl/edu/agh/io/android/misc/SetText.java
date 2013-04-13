package pl.edu.agh.io.android.misc;

import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */

public class SetText implements Runnable{
    private TextView view;
    private int msg;
    private String msg2;

    public SetText(TextView view, int msg){
        this.view=view;
        this.msg=msg;
    }

    public SetText(TextView view, String msg){
        this.view=view;
        msg2=msg;
    }


    @Override
    public void run(){
        if(msg2==null)
            view.setText(msg);
        else
            view.setText(msg2);
    }
}