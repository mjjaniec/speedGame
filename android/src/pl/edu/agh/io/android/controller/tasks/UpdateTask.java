package pl.edu.agh.io.android.controller.tasks;

import pl.edu.agh.io.android.activities.RegisterActivity;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/10/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateTask extends UpdateAndRegisterBase {
    public UpdateTask(RegisterActivity view, String login, String password, String email, String avatar, String ring) {
        super(view, login, password, email, avatar, ring);
        map.put("update", "true");
    }
}
