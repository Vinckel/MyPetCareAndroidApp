package petcare.com.mypetcare;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by KS on 2017-03-19.
 */

public class ScreenUtil {
    public static int convertDpToPixel(int dp, Context context) {
        if (dp < 0 || context == null) {
            return -1;
        }

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static int convertPixelsToDp(int px, Context context) {
        if (px < 0 || context == null) {
            return -1;
        }

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }
}
