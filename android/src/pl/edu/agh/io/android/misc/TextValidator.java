package pl.edu.agh.io.android.misc;

import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.controller.AppState;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextValidator implements View.OnFocusChangeListener {
    private String regex;
    private String message;
    private int min, max;
    private boolean legalEmpty;

    public TextValidator(String regex, int min, int max, boolean legalEmpty, String message) {
        this.regex = regex;
        this.message = message;
        this.min = min;
        this.max = max;
        this.legalEmpty = legalEmpty;
    }


    public boolean validate(String string) {
        if (legalEmpty){
            if(string == null || string.equals("")){
                return true;
            }
        }
        if (string == null) return false;
        if (string.length() < min) return false;
        if (string.length() > max) return false;
        if (!string.matches(regex)) return false;
        return true;
    }

    private String toShortStr() {
        Resources res = AppState.getInstance().getContext().getResources();
        return res.getString(R.string.common__field_to_short1)
                + " " + min + " " +
                res.getString(R.string.common__field_to_short2);
    }

    private String toLongStr() {
        Resources res = AppState.getInstance().getContext().getResources();
        return res.getString(R.string.common__field_to_long1)
                + " " + min + " " +
                res.getString(R.string.common__field_to_long2);
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            EditText edit = (EditText) view;
            String string = edit.getText().toString();

            if (legalEmpty && (string == null || string.equals("")))
                return;

            if (string.length() < min)
                edit.setError(toShortStr());
            else if (string.length() > max)
                edit.setError(toLongStr());
            else if (!string.matches(regex))
                edit.setError(message);
        }
    }
}