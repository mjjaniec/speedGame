package pl.edu.agh.io.android.misc;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/12/13
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IProcedure<T> {
    void call(T arg);
}
