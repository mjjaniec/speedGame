package pl.edu.agh.speedgame;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.edu.agh.speedgame.dao.OurSessionReplacement;
import pl.edu.agh.speedgame.dao.SessionFactorySingleton;
import pl.edu.agh.speedgame.dto.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SessionFactorySingleton.class, OurSessionReplacement.class})
public class LoginServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OurSessionReplacement hibernateSession;
    private HttpSession httpSession;
    private User user;

    @Before
    public void setUp() {
        request = createLoginRequestMock();
        hibernateSession = createOurSessionReplacementMock();
        httpSession = mock(HttpSession.class);

        when(request.getSession()).thenReturn(httpSession);

        user = new User.UserBuilder().login("bob").password("ala").email("ccc").avatar("lal").ring("obo").build();

        when(hibernateSession.get(User.class, "bob")).thenReturn(user);

        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testLogin() throws Exception {
        // given

        // standard setup

        // when

        new LoginServlet().doPost(request, response);

        // then

        verify(httpSession).setAttribute(eq("user"), eq(user));
        verify(response).sendRedirect("/jsp/logged.jsp");
    }

    @Test
    public void testExists() throws Exception {
        // given

        when(request.getParameter("exists")).thenReturn("true");

        PrintWriter printWriterMock = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(printWriterMock);

        JSONObject object = new JSONObject();

        object.put("login", user.getLogin());
        object.put("email", user.getEmail());
        object.put("avatar", user.getAvatar());
        object.put("ring", user.getRing());

        // when

        new LoginServlet().doPost(request, response);

        // then

        verify(response).setContentType("application/json");

        verify(printWriterMock).write(object.toString());
    }

    private OurSessionReplacement createOurSessionReplacementMock() {
        SessionFactorySingleton factoryMock = mock(SessionFactorySingleton.class);

        mockStatic(SessionFactorySingleton.class);
        when(SessionFactorySingleton.getInstance()).thenReturn(factoryMock);

        OurSessionReplacement hibernateSession = mock(OurSessionReplacement.class);
        when(factoryMock.createSessionReplacement()).thenReturn(hibernateSession);
        return hibernateSession;
    }

    private HttpServletRequest createLoginRequestMock() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("login")).thenReturn("bob");
        when(request.getParameter("password")).thenReturn("ala");
        return request;
    }

}
