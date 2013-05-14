package pl.edu.agh.io.android.controller.tasks;

import android.os.AsyncTask;
import android.util.Log;
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
public class GetFileTask extends AsyncTask<String,Double,Void> {

    private FileItem fileItem;
    private  IProcedure<InputStream> handler;

    public GetFileTask(IProcedure<InputStream> handler, FileItem fileItem){
        this.fileItem = fileItem;
        this.handler = handler;
    }

    private InputStream download(String url){
        try{
            URLConnection ucon = new URL(url).openConnection();
            InputStream in = ucon.getInputStream();
            /*ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte [] buffer = new byte[1024];

            while(in.read(buffer)>0){
                result.write(buffer);
            }
            return result.toByteArray();
            */

            return in;
        }catch(Exception e){
            Log.e("HTTP Failed", e.toString());
        }
        return null;
    }

    @Override
    protected Void doInBackground(String... urls) {
        String url = urls[0] + "?getfile=" + fileItem.getName();
        handler.call(download(url));
        return null;
    }

}
