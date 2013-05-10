package pl.edu.agh.io.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.SpeedGameProxy;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractActivity extends Activity {
    protected String getStr(int viewId){
        TextView view = (TextView) findViewById(viewId);
        if(view==null)return null;
        return view.getText().toString();
    }

    protected void setStr(int viewID,String string){
        TextView view = (TextView) findViewById(viewID);
        if(view==null)return;
        view.setText(string);
    }

    protected void setStr(int viewID,int stringID){
        TextView view = (TextView) findViewById(viewID);
        if(view==null)return;
        view.setText(stringID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeedGameProxy.getInstance().setContext(getApplicationContext());
    }
}
