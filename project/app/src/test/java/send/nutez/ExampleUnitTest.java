package send.nutez;

import org.junit.Test;

import static org.junit.Assert.*;

import static send.nutez.MainActivity.DEBUG_STRING;

import android.util.Log;

import java.util.Locale;

import send.nutez.utils.DataGetterThingy;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        System.out.println("µg".toUpperCase(Locale.ROOT));
        System.out.println("mg".toUpperCase(Locale.ROOT));
    }
}