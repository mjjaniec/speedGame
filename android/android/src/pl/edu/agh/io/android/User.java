package pl.edu.agh.io.android;

/**
 * Created with IntelliJ IDEA.
 * User: Michał Janiec
 * Date: 3/25/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */

import android.os.CountDownTimer;
import android.widget.TextView;

public class User {
    private static TextView display;
    private CountDownTimer timer;
    private String name;
    private int secondsLeft;
    public User(String name, int secondsLeft) {
        super();
        this.name = name;
        this.secondsLeft = secondsLeft;
    }
    public static void setDisplay(TextView display){
        User.display=display;
    }
    public String getName() {
        return name;
    }

    public void start(){
        if(secondsLeft==0)return;
        display.setText(timeString());
        timer=new CountDownTimer(1000*(secondsLeft+2),1000) {
            private boolean first = true;
            private boolean finished=false;
            @Override
            public void onTick(long millisUntilFinished) {
                if(first){
                    first=false;
                    return;
                }
                if(secondsLeft==0){
                    onFinish();
                    return;
                }
                --secondsLeft;
                display.setText(timeString());
            }

            @Override
            public void onFinish() {
                if(!finished){
                    finished=false;
                }
            }
        };
        timer.start();
    }

    public void stop(){
        if(timer!=null)timer.cancel();
        timer=null;
    }

    public String timeString(){
        StringBuilder sb=new StringBuilder();
        int minutes =secondsLeft/60;
        int seconds =secondsLeft%60;
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
}
