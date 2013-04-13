package pl.edu.agh.io.android.misc;

import android.view.View;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextValidator implements View.OnFocusChangeListener{
    private String regex;
    private String message;
    private int min, max;

    public TextValidator(String regex, int min, int max, String message){
        this.regex=regex;
        this.message=message;
        this.min=min;
        this.max=max;
    }

    public boolean validate(String string){
        if(string.length()<min)return false;
        if(string.length()>max)return false;
        if(!string.matches(regex))return false;
        return true;
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus){
            EditText edit = (EditText) view;
            String string = edit.getText().toString();
            if(string.length()<min)
                edit.setError("Field is to short, at least "+min+" characters.");
            else if(string.length()>max)
                edit.setError("Field is to long, at least "+max+" characters.");
            else if(!string.matches(regex))
                edit.setError(message);
        }
    }
}