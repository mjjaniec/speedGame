package pl.edu.agh.io.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import pl.edu.agh.io.android.custom.FilesViewAdapter;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.model.FileItem;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class ChooseFileActivity extends AbstractActivity {

    private static IProcedure<FileItem> callback;
    private static int what;

    public static void setWhat(int what){
        ChooseFileActivity.what=what;
    }

    public static void setCallback(IProcedure<FileItem> callback){
        ChooseFileActivity.callback=callback;
    }

    private TextView myPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosefile);

        ((TextView)findViewById(R.id.choosefile_what)).setText(what);

        myPath = (TextView)findViewById(R.id.choosefile_path);

        final Context context = this;

        ListView listView = (ListView) findViewById(R.id.choosefile_list);
        FilesViewAdapter adapter= FilesViewAdapter.createInstanced(this);
        adapter.setOnItemClick(new FilesViewAdapter.OnItemClick(){

            @Override
            public void onItemClick(FileItem fileItem, boolean picked) {
                if(picked){
                    File file = new File(fileItem.getPath());
                    if(file.getTotalSpace()>Integer.parseInt(getString(R.string.config__max_file_size))){
                        Toast.makeText(context,context.getText(R.string.common__file_is_to_big),Toast.LENGTH_SHORT).show();
                    }else{
                        callback.call(fileItem);
                        finish();
                    }
                }else{
                    myPath.setText(fileItem.getPath());
                }
            }
        });
        listView.setAdapter(adapter);
    }

}

