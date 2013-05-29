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
    /**
     * get string directly from child view
     *
     * @param viewId child view id
     * @return string from child view
     */
    protected String getStr(int viewId) {
        TextView view = (TextView) findViewById(viewId);
        if (view == null) return null;
        return view.getText().toString();
    }

    /**
     * set string directly to child view
     *
     * @param viewID child view id
     * @param string string to be set
     */
    protected void setStr(int viewID, String string) {
        TextView view = (TextView) findViewById(viewID);
        if (view == null) return;
        view.setText(string);
    }


    /**
     * set string directly to child view
     *
     * @param viewID   child view id
     * @param stringID id of string to be set
     */
    protected void setStr(int viewID, int stringID) {
        TextView view = (TextView) findViewById(viewID);
        if (view == null) return;
        view.setText(stringID);
    }

}
