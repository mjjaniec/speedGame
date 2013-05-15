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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SessionFactorySingleton.class, OurSessionReplacement.class})
public class RegisterServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OurSessionReplacement hibernateSession;
    private HttpSession httpSession;
    private User user;
    private User newUser;

    @Before
    public void setUp() {
        request = createLoginRequestMock();
        hibernateSession = createOurSessionReplacementMock();
        httpSession = mock(HttpSession.class);

        when(request.getSession()).thenReturn(httpSession);

        user = new User.UserBuilder().login("bob").password("ala").email("lal").avatar("obo").ring("kon").build();
        newUser = new User.UserBuilder().login("bob").password("lal").email("obo").avatar("kon").ring("ala").build();

        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testRegister() throws Exception {
        // given

        when(request.getParameter("login")).thenReturn("bob");
        when(request.getParameter("password")).thenReturn("ala");
        when(request.getParameter("email")).thenReturn("lal");
        when(request.getParameter("avatar")).thenReturn("obo");
        when(request.getParameter("ring")).thenReturn("kon");

        // when

        new RegisterServlet().doPost(request, response);

        // then

        verify(hibernateSession).save(user);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testUpdate() throws Exception {
        // given

        when(request.getParameter("update")).thenReturn("true");

        when(httpSession.getAttribute("user")).thenReturn(user);

        when(request.getParameter("password")).thenReturn("lal");
        when(request.getParameter("email")).thenReturn("obo");
        when(request.getParameter("avatar")).thenReturn("kon");
        when(request.getParameter("ring")).thenReturn("ala");

        when(hibernateSession.get(User.class, "bob")).thenReturn(user);

        // when

        new RegisterServlet().doPost(request, response);

        // then

        verify(hibernateSession).delete(user);
        verify(hibernateSession).save(newUser);

        verify(response).setStatus(HttpServletResponse.SC_OK);
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

        return request;
    }

}
