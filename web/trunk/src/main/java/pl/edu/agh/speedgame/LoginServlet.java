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
        //!!!added
        String android = request.getParameter("from_android_app");
        boolean isAndroid = android != null && android.equals("true");

        try(OurSessionReplacement sessionReplacement = SessionFactorySingleton.getInstance().createSessionReplacement()){
            User user = (User) sessionReplacement.get(User.class, login);

            if(user == null) {
		//!!!changed
		if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"INVALID_LOGIN");
		else response.sendRedirect("/main_page.jsp?error=" + "User%20doesn't%20exists");
                return;
            }

            if(user.getPassword().equals(password)) {
		if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"OK");
		else{
                	request.getSession().setAttribute("login", login);
               		 request.getSession().setAttribute("password", password);
                	request.getRequestDispatcher("/html/new_game.html").forward(request, response);
		}
            } else {
		//!!!changed
		if(isAndroid)response.sendError(HttpServletResponse.SC_OK,"INVALID_PASSWORD");
                else response.sendRedirect("/main_page.jsp?error=" + "Wrong%20login%20or%20password");
            }
        }


    }
}
