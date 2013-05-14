package pl.edu.agh.io.android.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.edu.agh.io.android.controller.AppController;
import pl.edu.agh.io.android.controller.UsersController;
import pl.edu.agh.io.android.misc.IProcedure;
import pl.edu.agh.io.android.misc.SetText;
import pl.edu.agh.io.android.model.FileItem;
import pl.edu.agh.io.android.model.User;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/9/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OfflineActivity extends AbstractActivity {

    private URLHolder ring = new URLHolder();
    private URLHolder avatar = new URLHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);


        Button play = (Button) findViewById(R.id.offline__play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable pic = null;
                if(avatar.value != null){
                    pic = Drawable.createFromPath(avatar.value.getPath());
                }
                UsersController.getInstance().addUser(
                        new User(getStr(R.id.offline__nick),
                                view.getContext(),
                                pic,
                                ring.value
                                ));
                finish();
            }
        });
        findViewById(R.id.offline__change_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setCallback(
                        new UrlSetter((TextView) findViewById(R.id.offline__avatar), avatar));
                AppController.getInstance().setWhat(R.string.newaccount__choose_avatar);
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        findViewById(R.id.offline__change_ring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setCallback(
                        new UrlSetter((TextView)findViewById(R.id.offline__ring),ring));
                AppController.getInstance().setWhat(R.string.newaccount__choose_ring);
                Intent myIntent = new Intent(view.getContext(), ChooseFileActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private static class URLHolder{
        public URL value = null;
    }

    private class UrlSetter implements IProcedure<FileItem>{

        private TextView view;
        private URLHolder holder;


        private UrlSetter(TextView view, URLHolder holder) {
            this.holder = holder;
            this.view = view;
        }

        @Override
        public void call(FileItem arg) {
            runOnUiThread(new SetText(view,arg.getName()));
            try{
                holder.value=new URL("file://"+arg.getPath());
            }catch (MalformedURLException e){
                Log.d("offline",e.getMessage());
            }
        }
    }
}
