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


    private SpeedGameProxy() {
    }

    private static SpeedGameProxy instance = new SpeedGameProxy();

    public static SpeedGameProxy getInstance() {
        return instance;
    }


    private boolean isChecked = false;
    private boolean online = false;
    private Context _context;

    private Context getContext() {
        //not an initialization -> synchronization not needed
        if (_context == null)
            _context = AppState.getInstance().getContext();
        return _context;
    }

    public boolean isOnline() {
        return online;
    }

    public void reset() {
        isChecked = false;
        online = false;
    }


    public void isOnlineAsync(final IProcedure<Boolean> callback) {
        if (!isChecked) {
            online = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress address = InetAddress.getByName(
                                getContext().getText(R.string.config__server_url).toString());
                        if (address != null && address.isReachable(500)) {
                            online = true;
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

    private URL formDownloadUrl(String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append(getServerUrl(Service.getFile));
        sb.append(getContext().getString(R.string.config__download_parameter));
        sb.append(filename);
        try {
            return new URL(sb.toString());
        } catch (MalformedURLException e) {
            Log.e("URL", e.toString());
            return null;
        }
    }

    public void makeUserFromJSON(final JSONObject object) {

        try {
            final Semaphore sem = new Semaphore(0);
            final String avatar_filename = object.getString("avatar");
            final URL ring_url = formDownloadUrl(object.getString("ring"));
            final String login = object.getString("login");

            new GetFileTask(new IProcedure<InputStream>() {
                @Override
                public void call(InputStream arg) {
                    Drawable avatar = null;
                    try {
                        avatar = Drawable.createFromStream(arg, avatar_filename);
                    } catch (Exception e) {
                        Log.w("login", "downloading error");
                    }
                    UsersController.getInstance().addUser(new User(login, avatar, ring_url));
                    sem.release();
                }
            }, new FileItem(object.getString("avatar"), null, false)).execute(getServerUrl(Service.getFile));

            sem.acquireUninterruptibly();

        } catch (JSONException e) {
            Log.e("JSON", e.toString());
        }
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
        Resources res = getContext().getResources();
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(res.getString(R.string.config__server_url)).append(":");
        sb.append(res.getString(R.string.config__server_port)).append("/");
        sb.append(res.getString(service.getPathID()));
        return sb.toString();
    }
}