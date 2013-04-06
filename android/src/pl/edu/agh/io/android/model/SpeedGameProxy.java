package pl.edu.agh.io.android.model;


/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import pl.edu.agh.io.android.activities.LoginActivity;
import pl.edu.agh.io.android.activities.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeedGameProxy {

    public enum Service{
        login;

        int getPathID(){
            switch (this){
                case login:return R.string.config__login_path;
            }
            return 0;
        }
    }

    private class LoginTask extends AsyncTask<String,Long,Boolean> {

        private final String login;
        private final String password;
        private LoginActivity view;
        private HttpClient httpClient;
        private HttpPost httpPost;

        public LoginTask(LoginActivity view, String login, String password){
            this.view=view;
            this.login=login;
            this.password=password;
        }
        @Override
        protected Boolean doInBackground(String... urls) {
            Boolean ret = true;
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://localhost:8080/login");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("login", login));
                nameValuePairs.add(new BasicNameValuePair("password",password));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpClient.execute(httpPost);

            } catch (Exception e) {
                Log.e("HTTP Failed", e.toString());
                ret = false;
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Boolean aBool) {
            super.onPostExecute(aBool);
            try{
                ResponseHandler<String> responseHandler=new BasicResponseHandler();
                String responseBody = httpClient.execute(httpPost, responseHandler);

                view.onLogin(Boolean.parseBoolean(responseBody));
            }catch(IOException e){
                Log.e("HTTP Failed",e.toString());
            }
        }
    }

    private class SendFileTask extends AsyncTask<String,Long,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
    private static SpeedGameProxy instance;
    private static Object lock = new Object();
    private Context context;

    private SpeedGameProxy() {

    }

    public void setContext(Context context){
        this.context=context;
    }

    public static SpeedGameProxy getInstance() {
        if (instance != null) return instance;
        synchronized (lock) {
            if (instance == null)
                instance = new SpeedGameProxy();
        }
        return instance;
    }

    public void login(LoginActivity view, String login, String password) {
        new LoginTask(view,login,password).execute(getServerUrl(Service.login));
    }


    public boolean sendFile(){
        return false;
    }

    private String getServerUrl(Service service){
        Resources res = context.getResources();
        StringBuilder sb=new StringBuilder();
        sb.append(res.getString(R.string.config__server_url)).append(":");
        sb.append(res.getString(R.string.config__server_port)).append("/");
        sb.append(res.getString(service.getPathID()));
        return sb.toString();
    }
}