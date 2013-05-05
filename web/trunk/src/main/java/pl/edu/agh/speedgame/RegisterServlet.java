package pl.edu.agh.speedgame;

import org.apache.commons.lang3.StringUtils;
import pl.edu.agh.speedgame.dao.OurSessionReplacement;
import pl.edu.agh.speedgame.dao.SessionFactorySingleton;
import pl.edu.agh.speedgame.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DatabaseMetaData;
import static org.apache.commons.lang3.StringUtils.*;

public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write("call POST with register data");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String update = request.getParameter("update");
        boolean isUpdate = Boolean.parseBoolean(update);

        if(isUpdate) {
            handleUpdate(request, response);
        } else {
            handleRegistration(request, response);
        }

    }

    void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String avatar = request.getParameter("avatar");
        String ring = request.getParameter("ring");

        //!!!added
        String android = request.getParameter("from_android_app");
        boolean isAndroid = android != null && android.equals("true");

        User.UserBuilder builder = new User.UserBuilder();
        builder.login(login)
                .password(password)
                .email(email)
                .avatar(avatar)
                .ring(ring);

        User user = builder.build();

        try(OurSessionReplacement sessionReplacement = SessionFactorySingleton.getInstance().createSessionReplacement()) {

            User currentlySavedUser = (User) sessionReplacement.get(User.class, login);
            if(currentlySavedUser == null) {
                sessionReplacement.save(user);
                if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"OK");
                else  response.setStatus(HttpServletResponse.SC_OK);
            } else {
                if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"USER_EXISTS");
                else response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User with the same login already exists");
            }
        }
    }


    void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String login = user.getLogin();
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String avatar = request.getParameter("avatar");
        String ring = request.getParameter("ring");

        try(OurSessionReplacement sessionReplacement = SessionFactorySingleton.getInstance().createSessionReplacement()) {

            User currentlySavedUser = (User) sessionReplacement.get(User.class, login);
            User.UserBuilder builder = User.UserBuilder.fromUser(currentlySavedUser);

            if(isNotEmpty(password)) {
                builder.password(password);
            }

            if(isNotEmpty(email)) {
                builder.email(email);
            }

            if(isNotEmpty(avatar)) {
                builder.avatar(avatar);
            }

            if(isNotEmpty(ring)) {
                builder.ring(ring);
            }

            sessionReplacement.delete(currentlySavedUser);
            sessionReplacement.save(builder.build());

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
