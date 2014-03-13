package pl.edu.agh.io.android.controller.tasks;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import pl.edu.agh.io.android.activities.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class UpdateAndRegisterBase extends AsyncTask<String, Long, Void> {

    public enum RegisterResult {
        OK, USER_EXISTS, ERROR
    }

    protected Map<String, String> map = new HashMap<String, String>();

    private String login;
    private String password;
    private String email;
    private String avatar;
    private String ring;
    private RegisterActivity view;
    private HttpClient httpClient;
    private HttpPost httpPost;

    public UpdateAndRegisterBase(RegisterActivity view, String login, String password, String email, String avatar, String ring) {
        this.view = view;
        this.login = login;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.ring = ring;

        if(this.avatar==null)
            this.avatar="";
        if(this.ring==null)
            this.ring="";
    }

    @Override
    protected Void doInBackground(String... urls) {
        view.onRegister(doRegister(urls[0]));
        return null;
    }

    private RegisterResult doRegister(String url) {
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        HttpResponse httpResponse;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login", login));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("avatar", avatar));
            nameValuePairs.add(new BasicNameValuePair("ring", ring));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }


            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e("HTTP Failed", e.toString());
            return RegisterResult.ERROR;
        }
        String status = httpResponse.getStatusLine().getReasonPhrase();
        if (status.equals("OK")) return RegisterResult.OK;
        if (status.equals("User with the same login already exists")) return RegisterResult.USER_EXISTS;
        return RegisterResult.ERROR;
    }
}