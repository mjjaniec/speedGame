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
import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.agh.io.android.activities.LoginActivity;
import pl.edu.agh.io.android.controller.SpeedGameProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private LoginResult handleResopnse(HttpResponse httpResponse){
        String status=httpResponse.getStatusLine().getReasonPhrase();

        if(status.startsWith("OK")){
            try{
                BufferedReader br = null;
                JSONObject object;
                try{
                    br = new BufferedReader( new InputStreamReader(httpResponse.getEntity().getContent()));
                    object = new JSONObject(br.readLine());
                }finally{
                    if(br!=null)
                        br.close();
                }
                SpeedGameProxy.getInstance().makeUser(object);
            }catch (JSONException e){
                return LoginResult.ERROR;
            } catch (IOException e) {
                return LoginResult.ERROR;
            }
            return LoginResult.OK;
        }
        if(status.equals("Bad request")){
            return LoginResult.INVALID_LOGIN;
        }
        return LoginResult.ERROR;
    }

    private LoginResult doLogin(String url){

        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        HttpResponse httpResponse;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login", login));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            nameValuePairs.add(new BasicNameValuePair("exists","true"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse=httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e("HTTP Failed", e.toString());
            return LoginResult.ERROR;
        }

        return handleResopnse(httpResponse);
    }

}