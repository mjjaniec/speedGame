package pl.edu.agh.io.android.model;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */


public class SpeedGameProxy {
    private static SpeedGameProxy instance;
    private static Object lock = new Object();

    private SpeedGameProxy() {

    }

    public static SpeedGameProxy getInstance() {
        if (instance != null) return instance;
        synchronized (lock) {
            if (instance == null)
                instance = new SpeedGameProxy();
        }
        return instance;
    }

    public boolean Login(String login, String password) {
        return true;
    }

    public boolean Logount(String login) {
        return false;
    }
}