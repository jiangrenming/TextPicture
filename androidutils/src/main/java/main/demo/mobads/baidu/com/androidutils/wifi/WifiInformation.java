package main.demo.mobads.baidu.com.androidutils.wifi;

/**
 * Created by fengxuan on 2017/1/17.
 * 已连接Wifi的一些信息
 */
public class WifiInformation {

    private String SSID = "";                //wifi的SSID
    private String BSSID = "";               //wifi的BSSID
    private int level = -100;                  //信号强度，系统值，int型
    private WifiStrength wifiStrength = WifiStrength.LOW;  //信号强度
    private String ip = "0.0.0.0";                  //ip地址
    private String linkSpeed = "";           //连接速度
    private String frequence = "";           //频率
    private String security = "";            //安全性
    private WifiState state = WifiState.NONE;  //状态信息

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLinkSpeed() {
        return linkSpeed;
    }

    public void setLinkSpeed(String linkSpeed) {
        this.linkSpeed = linkSpeed;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public WifiState getState() {
        return state;
    }

    public void setState(WifiState state) {
        this.state = state;
    }

    public WifiStrength getWifiStrength() {
        return wifiStrength;
    }

    public void setWifiStrength(WifiStrength wifiStrength) {
        this.wifiStrength = wifiStrength;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }
}
