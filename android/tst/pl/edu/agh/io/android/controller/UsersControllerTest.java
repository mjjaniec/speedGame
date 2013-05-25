package pl.edu.agh.io.android.controller;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/19/13
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersControllerTest {
    @Test
    public void testSwap() throws Exception {

    }

    @Test
    public void testReset() throws Exception {
        UsersController x = UsersController.getInstance();
        UsersController.reset();
        UsersController y = UsersController.getInstance();

        Assert.assertNotSame(x,y);
        Assert.assertNotNull(x);
        Assert.assertNotNull(y);
    }

    @Test
    public void testAddUser() throws Exception {

    }

    @Test
    public void testRemoveUser() throws Exception {

    }

    @Test
    public void testGetUsers() throws Exception {

    }

    @Test
    public void testGetCurrent() throws Exception {

    }

    @Test
    public void testRotate() throws Exception {

    }

    @Test
    public void testEndGame() throws Exception {

    }

    @Test
    public void testOnLost() throws Exception {

    }

    @Test
    public void testOnTimeout() throws Exception {

    }

    @Test
    public void testGetInstance() throws Exception {
        UsersController x = UsersController.getInstance();
        UsersController y = UsersController.getInstance();

        Assert.assertSame(x,y);
        Assert.assertNotNull(x);
    }

    @Test
    public void testRefresh() throws Exception {

    }
}
