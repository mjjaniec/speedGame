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
import pl.edu.agh.io.android.activities.LoginActivity;
import pl.edu.agh.io.android.activities.NewAccountActivity;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.controller.tasks.LoginTask;
import pl.edu.agh.io.android.controller.tasks.RegisterTask;
import pl.edu.agh.io.android.controller.tasks.SendFileTask;
import pl.edu.agh.io.android.model.FileItem;

public class SpeedGameProxy {

    public enum Service{
        login,
        getFile,
        sendFile,
        register;

        int getPathID(){
            switch (this){
                case login:return R.string.config__login_path;
                case getFile:return R.string.config__get_file_path;
                case register:return R.string.config__register_path;
                case sendFile:return R.string.config__send_file_path;
            }
            return 0;
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

    public void register(NewAccountActivity view, String login, String password, String email, String avatar, String ring){
        new RegisterTask(view,login,password,email ,avatar,ring).execute(getServerUrl(Service.register));
    }

    public void sendFile(NewAccountActivity view,FileItem fileItem){
        new SendFileTask(view,fileItem).execute(getServerUrl(Service.sendFile));
    }

    private String getServerUrl(Service service){
        Resources res = context.getResources();
        StringBuilder sb=new StringBuilder();
        sb.append("http://");
        sb.append(res.getString(R.string.config__server_url)).append(":");
        sb.append(res.getString(R.string.config__server_port)).append("/");
        sb.append(res.getString(service.getPathID()));
        return sb.toString();
    }
}