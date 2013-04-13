package pl.edu.agh.io.android.model.tasks;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import pl.edu.agh.io.android.activities.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginTask extends AsyncTask<String,Long,Void> {

    public enum LoginResult{
        OK,INVALID_LOGIN,INVALID_PASSWORD,ERROR
    }

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
    protected Void doInBackground(String... urls) {
        view.onLogin(doLogin(urls[0]));
        return null;
    }

    private LoginResult doLogin(String url){
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        HttpResponse httpResponse;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login", login));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            nameValuePairs.add(new BasicNameValuePair("from_android_app","true"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse=httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e("HTTP Failed", e.toString());
            return LoginResult.ERROR;
        }
        String status=httpResponse.getStatusLine().getReasonPhrase();
        if(status.equals("OK"))return LoginResult.OK;
        if(status.equals("INVALID_LOGIN"))return LoginResult.INVALID_LOGIN;
        if(status.equals("INVALID_PASSWORD"))return LoginResult.INVALID_PASSWORD;
        return LoginResult.ERROR;
    }
}
