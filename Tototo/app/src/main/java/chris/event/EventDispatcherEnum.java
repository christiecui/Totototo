package chris.event;

/**
 * Created by christiecui on 2014/11/6.
 * modified by christiecui on 2016/3/15
 */
public class EventDispatcherEnum {

    // --------------------------------------------
    // UI Event
    // --------------------------------------------
    public static final int UI_EVENT_BEGIN = 1000;
    /**开始下载*/
    public static final int UI_EVENT_APP_DOWNLOAD_START = UI_EVENT_BEGIN + 1;
    /**下载中*/
    public static final int UI_EVENT_APP_DOWNLOAD_DOWNLOADING = UI_EVENT_APP_DOWNLOAD_START + 1;

    /** app从前台切到后台 */
    public static final int UI_EVENT_APP_GOBACKGROUND = UI_EVENT_APP_DOWNLOAD_START + 1;
    /** app从后台切到前台 */
    public static final int UI_EVENT_APP_GOFRONT = UI_EVENT_APP_GOBACKGROUND + 1;
    /**扫描成功*/
    public static final int UI_EVENT_SCAN_SUCCESS = UI_EVENT_APP_GOFRONT + 1;
    /**扫描失败*/
    public static final int UI_EVENT_SCAN_FAILED = UI_EVENT_SCAN_SUCCESS + 1;

    public static final int UI_EVENT_END = 2000;
    // --------------------------------------------
    // Cache Event
    // --------------------------------------------
    public static final int CACHE_EVENT_START = 3000;


    /* 文件持久化事件定义 */
    public static final int FILE_PERSISTANCE_EVENT_SUCCESS = 3010;
    public static final int FILE_PERSISTANCE_EVENT_FAIL = FILE_PERSISTANCE_EVENT_SUCCESS + 1;


    public static final int CACHE_EVENT_END = 4000;

    // --------------------------------------------
    // Connection Event
    // --------------------------------------------
    public static final int CONNECTION_EVENT_START = 5000;


    public static final int CONNECTION_EVENT_END = 5100;

    /** app异常退出 */
    public static final int UI_EVENT_APP_exit = CONNECTION_EVENT_END+1;
}
