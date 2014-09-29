package ua.mafiasms.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.inputmethod.InputMethodManager;

import java.util.Hashtable;

/**
 * Created by daniil on 9/26/14.
 */
public class Tools {

    private static final Hashtable<String, Typeface> fontsCache = new Hashtable<String, Typeface>();

    public static Typeface getFont(Context ctx, String name) {
        synchronized (fontsCache) {
            if (!fontsCache.containsKey(name)) {
                String path = "fonts/" + name;
                try {
                    Typeface t = Typeface.createFromAsset(ctx.getAssets(),
                            path + ".ttf");
                    fontsCache.put(name, t);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return fontsCache.get(name);
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
        }
    }
}
