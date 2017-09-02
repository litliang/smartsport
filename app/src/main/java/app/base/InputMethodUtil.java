package app.base;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
/***
 * 输入法工具类
 * @author Administrator
 *
 */
public class InputMethodUtil {
    public static void showInput(final Context context, final View view) {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                InputMethodManager m = (InputMethodManager)

                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                m.showSoftInput(view != null ? view : ((Activity) context).getCurrentFocus(), InputMethodManager.SHOW_FORCED);

            }

        }, 500);
    }

    public static void hideInput(final Context context, final View view) {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                InputMethodManager m = (InputMethodManager)

                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }

        }, 500);
    }
}
