package main.demo.mobads.baidu.com.androidutils.wifi;

/**
 * Created by fengxuan on 2017/2/21.
 */
public enum WifiState {

    NONE("未连接"),
    STORED("已保存"),
    CONNECTING("正在连接"),
    AUTHENTICATING("正在进行身份验证"),
    CONNNECTED("已连接"),
    OBTAINING_IPADDR("正在获取IP地址..."),
    DISCONNECTED("身份验证出现问题"),
    FAILED("身份验证出现问题"),
    SCANNING ("连接中");

    private String value;

    WifiState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
