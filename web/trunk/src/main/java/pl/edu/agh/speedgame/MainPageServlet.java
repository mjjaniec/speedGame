package pl.edu.agh.speedgame;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MainPageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        System.out.println(session);
        if(session.getAttribute("login") == null) {
            request.getRequestDispatcher("/html/main_page.html").forward(request, response);
        } else {
            request.getRequestDispatcher("/jsp/game.jsp").forward(request, response);
        }

    }
}
