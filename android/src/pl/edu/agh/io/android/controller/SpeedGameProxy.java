package pl.edu.agh.io.android.controller;


/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.agh.io.android.activities.LoginActivity;
import pl.edu.agh.io.android.activities.RegisterActivity;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.controller.tasks.*;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.model.FileItem;
import pl.edu.agh.io.android.model.User;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Semaphore;


public class SpeedGameProxy {


    public enum Service {
        login,
        getFile,
        sendFile,
        register;

        int getPathID() {
            switch (this) {
                case login:
                    return R.string.config__login_path;
                case getFile:
                    return R.string.config__get_file_path;
                case register:
                    return R.string.config__register_path;
                case sendFile:
                    return R.string.config__send_file_path;
            }
            return 0;
        }
    }


    private static SpeedGameProxy instance;
    private static Object lock = new Object();
    private Context context;
    private boolean isChecked = false;
    private boolean online = false;

    public boolean isOnline() {
        return online;
    }

    public void reset(){
        isChecked = false;
        online = false;
    }


    public boolean isOnlineAsync(final IProcedure<Boolean> callback) {
        if (isChecked) {
            return online;
        }
        synchronized (lock) {
            if (!isChecked) {
                online = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InetAddress address = InetAddress.getByName(
                                    context.getText(R.string.config__server_url).toString());
                            if (address != null) {
                                if (address.isReachable(500)) {
                                    online = true;
                                }
                            }
                        } catch (Exception e) {
                            Log.w("SpeedGame", e.toString());

                        }
                        isChecked = true;
                        callback.call(online);
                    }
                }).start();
            }
        }
        return online;
    }

    private SpeedGameProxy() {

    }

    public void makeUser(final JSONObject object) {

        try {
            final Semaphore sem = new Semaphore(0);
            final String avatar_filename = object.getString("avatar");
            final String ring_filename = object.getString("ring");
            final URL ring_url = new URL(getServerUrl(Service.getFile)+"?getfile="+ring_filename);
           // final String email = object.getString("email");
            final String login = object.getString("login");

            new GetFileTask(new IProcedure<InputStream>() {
                @Override
                public void call(InputStream arg) {
                    Drawable avatar = null;
                    try{
                        avatar = Drawable.createFromStream(arg, avatar_filename);
                    }catch(Exception e){
                        Log.w("login","downloading error");
                    }
                    UsersController.getInstance().addUser(new User(login,context,avatar,ring_url));
                    sem.release();
                }
            }, new FileItem(object.getString("avatar"), null, false)).execute(getServerUrl(Service.getFile));

            sem.acquireUninterruptibly();

        } catch (JSONException e) {
            Log.e("JSON", e.toString());
        }catch (MalformedURLException e){
            Log.e("URL", e.toString());
        }
    }

    public void setContext(Context context) {
        this.context = context;
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
        new LoginTask(view, login, password).execute(getServerUrl(Service.login));
    }

    public void register(RegisterActivity view, String login, String password, String email, String avatar, String ring) {
        new RegisterTask(view, login, password, email, avatar, ring).execute(getServerUrl(Service.register));
    }

    public void update(RegisterActivity view, String login, String password, String email, String avatar, String ring) {
        new UpdateTask(view, login, password, email, avatar, ring).execute(getServerUrl(Service.register));
    }

    public void sendFile(RegisterActivity view, FileItem fileItem) {
        new SendFileTask(view, fileItem).execute(getServerUrl(Service.sendFile));
    }

    private String getServerUrl(Service service) {
        Resources res = context.getResources();
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(res.getString(R.string.config__server_url)).append(":");
        sb.append(res.getString(R.string.config__server_port)).append("/");
        sb.append(res.getString(service.getPathID()));
        return sb.toString();
    }
}