package hipad.bill;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.DbManager;
import org.xutils.x;


/**
 * Created by user_wen on 2017/10/29.
 */

public class MyApplication extends Application {
    /**
     * 实现单例，任何一个页面都能拿到这个类的数据和对象
     */
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        Fresco.initialize(getApplicationContext());
    }

    /**
     * 获取数据库的管理器
     * 通过管理器进行增删改查
     */
    public DbManager getDbManager() {
        DbManager.DaoConfig daoconfig = new DbManager.DaoConfig();
        //默认在data/data/包名/database/数据库名称
//        daoconfig.setDbDir()
        daoconfig.setDbName("count.db");
//        daoconfig.setDbVersion(1);//默认1
        //通过manager进行增删改查
        return x.getDb(daoconfig);
    }

}
