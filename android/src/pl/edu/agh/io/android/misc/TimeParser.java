package pl.edu.agh.io.android.misc;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 5/14/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeParser {

    public static int Parse(String string)throws Exception{
        String[] split = string.split(":");
        if(split.length>2)
            throw new Exception("Parse exception");

        if(split.length==1)
            return Integer.parseInt(split[0])*60;
        else
            return Integer.parseInt(split[0])*60+Integer.parseInt(split[1]);
    }
}
