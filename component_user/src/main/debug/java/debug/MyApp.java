package debug;

import android.app.Application;

import com.billy.cc.core.component.CC;

/**
 * @author billy.qi
 * @since 17/11/20 20:57
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);
    }
}
