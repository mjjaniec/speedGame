package pl.edu.agh.io.android.controller;

import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.model.FileItem;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/10/13
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppController {
    private static AppController instance=new AppController();
    private boolean afterGame;

    public static AppController getInstance(){
        return instance;
    }
    private AppController(){

    }

    private boolean additionalPlayer;
    private boolean isNew;
    private IProcedure<FileItem> callback;
    private int what;
    private boolean logged = false;
    private boolean _isFirstTime = true;

    public boolean isLogged() {
        return logged;
    }

    public void reset(){
        logged = false;
        _isFirstTime = true;
    }


    public boolean isFirstTime(){
        boolean ret = _isFirstTime;
        _isFirstTime = false;
        return ret;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isAdditionalPlayer() {
        return additionalPlayer;
    }

    public void setAdditionalPlayer(boolean additionalPlayer) {
        this.additionalPlayer = additionalPlayer;
    }

    public IProcedure<FileItem> getCallback() {
        return callback;
    }

    public void setCallback(IProcedure<FileItem> callback) {
        this.callback = callback;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }


    public void setAfterGame(boolean afterGame) {
        this.afterGame = afterGame;
    }

    public boolean isAfterGame() {
        return afterGame;
    }
}
