package pl.edu.agh.io.android.controller.tasks;

import android.os.AsyncTask;
import android.util.Log;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.controller.AppState;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.model.FileItem;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/29/13
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetFileTask extends AsyncTask<String, Double, Void> {

    private FileItem fileItem;
    private IProcedure<InputStream> handler;

    public GetFileTask(IProcedure<InputStream> handler, FileItem fileItem) {
        this.fileItem = fileItem;
        this.handler = handler;
    }

    private InputStream download(String url) {
        try {
            URLConnection ucon = new URL(url).openConnection();
            InputStream in = ucon.getInputStream();

            return in;
        } catch (Exception e) {
            Log.e("HTTP Failed", e.toString());
        }
        return null;
    }

    @Override
    protected Void doInBackground(String... urls) {
        StringBuilder sb = new StringBuilder(urls[0]);
        sb.append(AppState.getInstance().getContext().getString(R.string.config__download_parameter));
        sb.append(fileItem.getName());
        handler.call(download(sb.toString()));
        return null;
    }

}
