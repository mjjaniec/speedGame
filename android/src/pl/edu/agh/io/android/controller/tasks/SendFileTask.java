package pl.edu.agh.io.android.controller.tasks;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import pl.edu.agh.io.android.activities.RegisterActivity;
import pl.edu.agh.io.android.model.FileItem;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/29/13
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendFileTask extends AsyncTask<String, Double, Void> {

    private FileItem fileItem;
    private RegisterActivity view;

    public SendFileTask(RegisterActivity view, FileItem fileItem) {
        this.view = view;
        this.fileItem = fileItem;
    }

    private boolean upload(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            multipartEntity.addPart(fileItem.getName(), new FileBody(new File(fileItem.getPath())));

            httpPost.setEntity(multipartEntity);

            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() != 200)
                return false;
        } catch (Exception e) {
            Log.e("HTTP Failed", e.toString());
            return false;
        }
        return true;
    }

    @Override
    protected Void doInBackground(String... urls) {
        String url = urls[0];
        view.onUpload(upload(url), fileItem);
        return null;
    }

}
