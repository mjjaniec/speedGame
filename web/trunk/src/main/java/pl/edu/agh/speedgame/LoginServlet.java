package pl.edu.agh.speedgame;

import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.agh.speedgame.dao.OurSessionReplacement;
import pl.edu.agh.speedgame.dao.SessionFactorySingleton;
import pl.edu.agh.speedgame.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write("call POST with login data");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        String android = request.getParameter("from_android_app");
        boolean isAndroid = android != null && android.equals("true");

        try(OurSessionReplacement sessionReplacement = SessionFactorySingleton.getInstance().createSessionReplacement()){
            User user = (User) sessionReplacement.get(User.class, login);

            String exists = request.getParameter("exists");
            boolean isExists = Boolean.parseBoolean(exists);

            if(user == null) {

                if(isExists) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"INVALID_LOGIN");
                else response.sendRedirect(response.encodeRedirectURL("/main_page.jsp?error=" + "User doesn't exists"));
                        return;
            }

            if(user.getPassword().equals(password)) {


                if(isExists) {
                    response.setContentType("application/json");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("login", user.getLogin());
                        object.put("email", user.getEmail());
                        object.put("avatar", user.getAvatar());
                        object.put("ring", user.getRing());
                        response.getWriter().write(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

		        if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"OK");
		        else{
                    	request.getSession().setAttribute("user", user);
                    	response.sendRedirect("/jsp/logged.jsp");
        		}
            } else {

                if(isExists) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

        		if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"INVALID_PASSWORD");
                else response.sendRedirect(response.encodeRedirectURL("/main_page.jsp?error=" + "Wrong login or password"));
            }

        }


    }
}
