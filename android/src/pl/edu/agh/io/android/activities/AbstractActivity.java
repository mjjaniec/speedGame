package pl.edu.agh.io.android.activities;

import android.app.Activity;
import android.widget.TextView;

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
}
