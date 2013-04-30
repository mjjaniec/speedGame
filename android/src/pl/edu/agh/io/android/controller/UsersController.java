package pl.edu.agh.io.android.controller;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;
import pl.edu.agh.io.android.activities.GameActivity;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class UsersController {


    public static enum OnTimeout{
        loose, negativePoints
    }

    private static UsersController instance;
    private static Object lock = new Object();

    private OnTimeout onTimeoutAction = OnTimeout.loose;
    private Queue<User> users = new LinkedList<User>();
    private User current;
    private GameActivity gameActivity;
    private boolean isReady =false;
    private boolean first = true;
    private int players;
    private int lostPlayers;

    public int getButtonCaption(){
        if(isReady && !first)
            return R.string.game__next_player;
        return R.string.common__start_the_game;

    }

    public static void reset(){
        if(instance!=null)
            if(instance.current!=null)
                instance.current.stop();
        synchronized(lock){
            instance=new UsersController();
        }
    }

    private UsersController(){	}

    /**
     * call only after adding all users
     * @param gameActivity GameActivity as view
     * @param onTimeout TimeOut method
     * @param time time per player in seconds
     */
    public void configure(GameActivity gameActivity, OnTimeout onTimeout, int time){
        if(!isReady){
            this.gameActivity = gameActivity;

            this.onTimeoutAction = onTimeout;
            this.lostPlayers=0;
            players=users.size();
            for(User user: users)
                user.setTime(time);
            isReady=true;
        }
    }

    public void addUser(User user){
        current=user;
        users.add(user);
    }

    public List<User> getUsers(){
        return (LinkedList<User>)users;
    }

    public String getCurrentName(){
        if(!first)return current.getName();
        return "";
    }

    public void rotate(){
        if(first){
            first=false;
            //quite ugly, I know;
            ((LinkedList<User>)(users)).remove(players-1);
        }

        current.stop();

        do{
            User tmp = current;
            current=users.poll();
            users.offer(tmp);
        }while(current.isLost());

        current.start();
    }

    private User findWinner() {
        for(User u : users)
            if(!u.isLost())
                return u;
        return null;
    }

    public void onLost(User who){
        if(++lostPlayers<players-1){
            new AlertDialog.Builder(gameActivity)
                    .setTitle(
                            gameActivity.getString(R.string.game__timeout_loose1)+
                                    " " + who.getName() + " " +
                                    gameActivity.getString(R.string.game__timeout_loose2)
                    )
                    .setPositiveButton(R.string.common__ok, null).show();
        }
        else{
            User winner = findWinner();

            new AlertDialog.Builder(gameActivity)
                    .setTitle(
                            gameActivity.getString(R.string.game__win1)+
                                    " " + winner.getName() + " " +
                                    gameActivity.getString(R.string.game__win2)
                    )
                    .setPositiveButton(R.string.common__ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gameActivity.finish();
                        }
                    }).show();
        }
    }

    public OnTimeout onTimeout(User who){
        switch(onTimeoutAction){
            case loose:

                who.setLost();
                break;

            case negativePoints:
                Toast.makeText(gameActivity,
                        (gameActivity.getText(R.string.game__timeout_negative1) +
                                " " + who.getName() + " " +
                                gameActivity.getText(R.string.game__timeout_negative2)),
                        Toast.LENGTH_LONG).show();
                break;
        }
        return onTimeoutAction;
    }


    public static UsersController getUsersController(){
        if(instance!=null)return instance;
        synchronized (lock) {
            if(instance==null)
                instance=new UsersController();
        }
        return instance;

    }

    public void refresh(final User user) {
        final TextView textView =(TextView)gameActivity.findViewById(R.id.game__clock);
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(user.timeString());
            }
        });
    }

}