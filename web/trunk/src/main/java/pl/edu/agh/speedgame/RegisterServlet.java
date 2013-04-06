package pl.edu.agh.speedgame;

import pl.edu.agh.speedgame.dao.OurSessionReplacement;
import pl.edu.agh.speedgame.dao.SessionFactorySingleton;
import pl.edu.agh.speedgame.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write("call POST with register data");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String avatar = request.getParameter("avatar");
        String ring = request.getParameter("ring");

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
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User with the same login already exists");
            }
        }


    }
}
