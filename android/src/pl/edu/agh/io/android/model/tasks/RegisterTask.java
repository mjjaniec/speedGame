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
import pl.edu.agh.io.android.activities.NewAccountActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterTask extends AsyncTask<String,Long,Void> {

    public enum RegisterResult{
        OK,USER_EXISTS,ERROR
    }

    private final String login;
    private final String password;
    private final String email;
    private final String avatar;
    private final String ring;
    private NewAccountActivity view;
    private HttpClient httpClient;
    private HttpPost httpPost;

    public RegisterTask(NewAccountActivity view, String login, String password, String email, String avatar, String ring){
        this.view=view;
        this.login=login;
        this.password=password;
        this.email=email;
        this.avatar=avatar;
        this.ring=ring;
    }
    @Override
    protected Void doInBackground(String... urls) {
        view.onRegister(doRegister(urls[0]));
        return null;
    }

    private RegisterResult doRegister(String url){
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        HttpResponse httpResponse;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login", login));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            nameValuePairs.add(new BasicNameValuePair("email",email));
            nameValuePairs.add(new BasicNameValuePair("avatar",avatar));
            nameValuePairs.add(new BasicNameValuePair("ring",ring));
            nameValuePairs.add(new BasicNameValuePair("from_android_app","true"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse=httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e("HTTP Failed", e.toString());
            return RegisterResult.ERROR;
        }
        String status=httpResponse.getStatusLine().getReasonPhrase();
        if(status.equals("OK"))return RegisterResult.OK;
        if(status.equals("USER_EXISTS"))return RegisterResult.USER_EXISTS;
        return RegisterResult.ERROR;
    }
}
