package pl.edu.agh.io.android.model;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/5/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.controller.UsersController;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class User {

    private CountDownTimer countDownTimer;
    private Timer negativePointsTimer;
    private boolean lost;
    private int seconds;
    private boolean negative;
    private String name;
    private Drawable avatar;
    private URL ringURL;
    private User self;

    public User(String name, Context context){
        this(name,context,null,null);
    }

    public Drawable getAvatar(){
        return avatar;
    }

    public URL getRingURL(){
        return ringURL;
    }

    public User(String name, Context context,URL avatar, URL ring) {
        this.name = name;
        this.self = this;
        this.ringURL = ring;

        //that below may by moved to UsersController

        if(avatar==null){
            //use default avatar - from local resources
            this.avatar = context.getResources().getDrawable(R.drawable.default_avatar);
        }else {
            //download remote avatar
            throw new Error("Unimplemented");
        }
    }


    public String getName() {
        return name;
    }

    public void setLost(){
        lost = true;
        UsersController.getInstance().onLost(this);
    }

    public boolean isLost(){
        return lost;
    }

    public void setTime(int time){
        seconds=time;
    }

    public void start(){
        UsersController.getInstance().refresh(self);

        if(!negative){
            countDownTimer = CreateCountDownTimer();
            countDownTimer.start();
        }else{
            negativePointsTimer = new Timer(true);
            negativePointsTimer.scheduleAtFixedRate(negativeTimerTask,1000,1000);
        }
    }

    private CountDownTimer CreateCountDownTimer(){
        return new CountDownTimer(1000*(seconds +2),1000) {
            private boolean first = true;
            private boolean finished=false;
            @Override
            public void onTick(long millisUntilFinished) {
                if(first){
                    first=false;
                    return;
                }
                if(seconds == 0){
                    onFinish();
                    return;
                }
                --seconds;
                UsersController.getInstance().refresh(self);
            }

            @Override
            public void onFinish() {
                if(!finished){
                    finished=false;
                    switch(UsersController.getInstance().onTimeout(self)){
                        case loose: break;
                        case negativePoints:
                            seconds = 0;
                            negative = true;
                            negativePointsTimer = new Timer(true);
                            negativePointsTimer.scheduleAtFixedRate(negativeTimerTask,1000,1000);
                    }
                }
            }

        };
    }

    private TimerTask negativeTimerTask = new TimerTask() {
        @Override
        public void run() {
            seconds++;
            UsersController.getInstance().refresh(self);
        }
    };


    public void stop(){
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if(negativePointsTimer != null){
            negativePointsTimer.cancel();
            negativePointsTimer = null;
        }
    }

    public String timeString(){
        StringBuilder sb=new StringBuilder();
        int minutes = seconds /60;
        int seconds = this.seconds %60;

        if(negative)sb.append("-");
        if(minutes<10)sb.append("0");
        sb.append(minutes);
        sb.append(":");
        if(seconds<10)sb.append("0");
        sb.append(seconds);
        return sb.toString();
    }
    @Override
    public String toString() {
        return name + "\n" + timeString();
    }

    public boolean isNegative() {
        return negative;
    }
}