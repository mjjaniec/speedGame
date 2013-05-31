package pl.edu.agh.io.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import pl.edu.agh.io.android.adapters.FilesViewAdapter;
import pl.edu.agh.io.android.controller.AppState;
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

    private TextView myPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosefile);

        setTitle(AppState.getInstance().getFilesViewTitleId());

        myPath = (TextView) findViewById(R.id.choosefile_path);

        final Context context = this;

        ListView listView = (ListView) findViewById(R.id.choosefile_list);
        FilesViewAdapter adapter = FilesViewAdapter.createInstanced(this);
        adapter.setOnItemClick(new FilesViewAdapter.OnItemClick() {

            @Override
            public void onItemClick(FileItem fileItem, boolean picked) {
                if (picked) {
                    File file = new File(fileItem.getPath());
                    long maxsize = Long.parseLong(getString(R.string.config__max_file_size));
                    long size = file.length();
                    if (size > maxsize) {
                        Toast.makeText(context, context.getText(R.string.common__file_is_to_big), Toast.LENGTH_SHORT).show();
                    } else {
                        AppState.getInstance().getFilesViewCallback().call(fileItem);
                        finish();
                    }
                } else {
                    myPath.setText(fileItem.getPath());
                }
            }
        });
        listView.setAdapter(adapter);
    }

}

