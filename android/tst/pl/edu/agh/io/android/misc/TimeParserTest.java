package pl.edu.agh.io.android.misc;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/19/13
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeParserTest {
    @Test
    public void testParse1() throws Exception {
        String time = "13";
        int expected = 13*60;
        Assert.assertEquals(expected,TimeParser.Parse(time));
    }
    @Test
    public void testParse2() throws Exception {
        String time = "1:15";
        int expected = 75;
        Assert.assertEquals(expected,TimeParser.Parse(time));
    }

    @Test(expected = Exception.class)
    public void testParse3() throws Exception {
        String time = "4:1:15";
        TimeParser.Parse(time);
    }

    @Test(expected = Exception.class)
    public void testParse4() throws Exception {
        String time = "asfas";
        TimeParser.Parse(time);
    }

    @Test(expected = Exception.class)
    public void testParse5() throws Exception {
        String time = "";
        TimeParser.Parse(time);
    }

}
