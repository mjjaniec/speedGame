package pl.edu.agh.io.android.model;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList;
import java.util.List;
import pl.edu.agh.io.android.activities.R;

public class UsersManager {
    private static UsersManager instance;
    private static Object lock = new Object();

    private List<User> users = new ArrayList<User>();
    private User current;
    private boolean isReady =false;
    private boolean first = true;
    private int players;

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
            instance=new UsersManager();
        }
    }

    private UsersManager(){	}

    public void configure(int players, int time){
        if(!isReady){
            this.players=players;

            for(int i=0; i<players-1; ++i){
                users.add(new User("Player "+(i+1),time));
            }
            current=new User("Player "+players,time);
            users.add(current);
            isReady=true;
        }
    }

    public List<User> getUsers(){
        return users;
    }

    public String getCurrentName(){
        if(!first)return current.getName();
        return "";
    }

    public void rotate(){
        if(first){
            first=false;
            users.remove(players-1);
        }

        current.stop();
        User tmp = current;
        current=users.get(0);
        for(int i=0; i<players-2; ++i)
            users.set(i, users.get(i+1));

        users.set(players-2,tmp);

        current.start();
    }

    public static UsersManager getUsersManager(){
        if(instance!=null)return instance;
        synchronized (lock) {
            if(instance==null)
                instance=new UsersManager();
        }
        return instance;

    }
}