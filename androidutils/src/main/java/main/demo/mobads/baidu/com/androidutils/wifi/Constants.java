package main.demo.mobads.baidu.com.androidutils.wifi;

/**
 * Created by jiangrenming on 2018/8/22.
 */

public class Constants {
    //WIFI加密方式
    public static final String SECURITY_NONE = "无";  //无加密
    public static final String SECURITY_PSK = "WPA/WPA2 PSK";   //最安全家用加密
    public static final String SECURITY_WPA_EAP = "WPA EAP";   //迄今最安全
    public static final String SECURITY_WEP = "WEP";   //安全较差

    public static final String ACTION_WIFI_STATE_CHANGE = "android.net.wifi.STATE_CHANGE";
    public static final int PERIOD = 5 * 1000; //刷新间隔5秒
}
