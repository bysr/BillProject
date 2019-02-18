package hipad.bill.db;

import android.content.Context;

/**
 * Created by user_wen on 2017/10/29.
 */

public class XUtil {

    private static XUtil instance;
    private static Context appContext;

    public static XUtil getInstance(Context context) {

        if (instance == null) {
            instance = new XUtil();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
        }
        return instance;
    }



}
