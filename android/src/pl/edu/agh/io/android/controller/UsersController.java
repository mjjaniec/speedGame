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
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import pl.edu.agh.io.android.activities.GameActivity;
import pl.edu.agh.io.android.activities.NewGameActivity;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class UsersController {


    public void swap(int from, int to) {
        if (current == users.get(from)) {
            current = users.get(to);
        } else if (current == users.get(to)) {
            current = users.get(from);
        }
        User tmp = users.get(from);
        users.set(from, users.get(to));
        users.set(to, tmp);
    }

    public void pause() {
        current.stop();
    }

    public void unpause() {
        current.start();
    }

    public static enum OnTimeout {
        loose, negativePoints;

        private static final String looseStr = "Lose game";
        private static final String negativeStr =  "Negative Points";

        @Override
        public String toString() {
            switch (this) {
                case loose:
                    return looseStr;
                case negativePoints:
                    return negativeStr;
                default:
                    throw new Error("Unimplemented on timeout");
            }
        }

        public static OnTimeout fromString(String str) {
            if (str.equals(looseStr))
                return loose;
            if (str.equals(negativeStr))
                return negativePoints;

            throw new Error("Unimplemented on timeout");
        }


    }

    public static enum Sound {
        on, off;

        public static Sound fromBoolean(boolean checked) {
            return checked ? on : off;
        }
    }

    private static UsersController instance;
    private static Object lock = new Object();

    private OnTimeout onTimeoutAction = OnTimeout.loose;
    private Sound soundOn = Sound.on;
    //I'd like to use queue but it makes more problems than profits
    private LinkedList<User> users = new LinkedList<User>();
    private User current;
    private GameActivity gameActivity;
    private boolean isReady = false;
    private boolean first = true;
    private int players;
    private int lostPlayers;
    private MediaPlayer mediaPlayer;
    private int time;

    public int getButtonCaption() {
        if (isReady && !first)
            return R.string.game__next_player;
        return R.string.game__start_the_game;

    }

    public static void reset() {
        if (instance != null) {
            if (instance.current != null)
                instance.current.stop();
            if (instance.mediaPlayer != null) {
                instance.mediaPlayer.release();
            }
        }
        synchronized (lock) {
            instance = new UsersController();
        }
    }

    private UsersController() {
    }

    public void setOnTimeout(OnTimeout onTimeout) {
        onTimeoutAction = onTimeout;
    }

    public void setSoundOn(Sound on) {
        soundOn = on;
    }

    public void setTime(int time) {
        this.time = time;
        for (User user : users) {
            user.setTime(time);
        }
    }


    public void configure(GameActivity gameActivity) {
        if (!isReady) {
            this.gameActivity = gameActivity;
            this.lostPlayers = 0;
            players = users.size();
            isReady = true;

            if (soundOn == Sound.on) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                } else {
                    mediaPlayer.reset();
                }
            }
        }
    }

    public void addUser(User user) {
        user.setTime(time);
        current = user;
        users.add(user);
    }

    public void removeUser(int who) {
        users.remove(who);
    }

    public List<User> getUsers() {
        return users;
    }

    public User getCurrent() {
        return current;
    }

    public void rotate() {
        if (first) {
            first = false;
            users.remove(players - 1);
        }

        current.stop();

        do {
            User tmp = current;
            current = users.poll();
            users.offer(tmp);
        } while (current.isLost());

        current.start();

        if (soundOn == Sound.on)
            play(current);
    }

    public void endGame() {
        Intent intent = new Intent(gameActivity, NewGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        UsersController.reset();
        gameActivity.startActivity(intent);
    }

    private void play(User current) {
        URL ringUrl = current.getRingURL();
        try {
            mediaPlayer.reset();
            if (ringUrl == null) {
                AssetFileDescriptor descriptor = gameActivity.getAssets().openFd("default_ring.mp3");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor());
                descriptor.close();
            } else {
                mediaPlayer.setDataSource(gameActivity, Uri.parse(ringUrl.toString()));
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            //pass it quite silently, it's not critical problem.
            Log.e("mediaPlayer", "error while opening default_ring.mp3 file");
        } catch (Exception e) {
            Log.e("mediaPlayer", e.toString());
        }
    }

    private User findWinner() {
        for (User u : users)
            if (!u.isLost())
                return u;
        return null;
    }

    private boolean isGameEnd(){
        return lostPlayers == players -1;
    }

    private void userLostAlert(User who){
        new AlertDialog.Builder(gameActivity)
                .setTitle(
                        gameActivity.getString(R.string.game__timeout_loose1) +
                                " " + who.getName() + " " +
                                gameActivity.getString(R.string.game__timeout_loose2)
                )
                .setPositiveButton(R.string.common__ok, null).show();
    }

    private void userWinAlert(User winner){
        new AlertDialog.Builder(gameActivity)
                .setTitle(
                        gameActivity.getString(R.string.game__win1) +
                                " " + winner.getName() + " " +
                                gameActivity.getString(R.string.game__win2)
                )
                .setPositiveButton(R.string.common__ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        endGame();
                    }
                }).show();
    }

    public void onLost(User who) {
        ++lostPlayers;
        if (!isGameEnd()) {
            userLostAlert(who);
        } else {
            User winner = findWinner();

            users.add(current);
            //AppController.getInstance().setAfterGame(true);

            userWinAlert(winner);
        }
    }

    public OnTimeout onTimeout(User who) {
        switch (onTimeoutAction) {
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

    public static UsersController getInstance() {
        if (instance != null) return instance;
        synchronized (lock) {
            if (instance == null)
                instance = new UsersController();
        }
        return instance;

    }

    public void refresh(final User user) {
        final TextView textView = (TextView) gameActivity.findViewById(R.id.game__clock);
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(user.timeString());
            }
        });
    }

}