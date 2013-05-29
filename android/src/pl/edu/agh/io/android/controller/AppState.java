package pl.edu.agh.io.android.controller;

import android.content.Context;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.model.FileItem;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/10/13
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppState {
    private static AppState instance = new AppState();
    private boolean gameStarted;

    private AppState() {
    }

    public static AppState getInstance() {
        return instance;
    }

    private boolean busy;
    private Context context;
    private boolean additionalPlayer;
    private boolean creatingNewAccount;
    private IProcedure<FileItem> filesViewCallback;
    private int filesViewTitleId;
    private boolean logged = false;
    private boolean _isFirstTime = true;

    public boolean isLogged() {
        return logged;
    }

    public void reset() {
        additionalPlayer = false;
        logged = false;
        _isFirstTime = true;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isFirstTime() {
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

    public IProcedure<FileItem> getFilesViewCallback() {
        return filesViewCallback;
    }

    public void setFilesViewCallback(IProcedure<FileItem> filesViewCallback) {
        this.filesViewCallback = filesViewCallback;
    }

    public boolean isCreatingNewAccount() {
        return creatingNewAccount;
    }

    public void setCreatingNewAccount(boolean creatingNewAccount) {
        this.creatingNewAccount = creatingNewAccount;
    }

    public int getFilesViewTitleId() {
        return filesViewTitleId;
    }

    public void setFilesViewTitleId(int filesViewTitleId) {
        this.filesViewTitleId = filesViewTitleId;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
