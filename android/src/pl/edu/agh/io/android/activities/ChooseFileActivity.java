package pl.edu.agh.io.android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import pl.edu.agh.io.android.misc.IProcedure;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChooseFileActivity extends AbstractActivity{

    private static String what;
    private static IProcedure<String> callback;

    public static void setWhat(String what){
        ChooseFileActivity.what = what;
    }

    public static void setCallback(IProcedure<String> callback){
        ChooseFileActivity.callback=callback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosefile);

        ((TextView)findViewById(R.id.choosefile__what)).setText(what);

        findViewById(R.id.choosefile__ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView list = (ListView) findViewById(R.id.choosefile__list);
                String result=list.getSelectedItem().toString();
                callback.call(result);
                finish();
            }
        });
    }
}
