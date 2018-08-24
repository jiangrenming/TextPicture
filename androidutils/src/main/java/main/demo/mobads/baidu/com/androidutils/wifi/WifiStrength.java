package main.demo.mobads.baidu.com.androidutils.wifi;

/**
 * Created by fengxuan on 2017/1/17.
 */
public enum WifiStrength {
    LOW("弱"),
    MEDIUM("中等"),
    HIGH("强");

    String value;

    WifiStrength(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
