package main.demo.mobads.baidu.com.androidutils.wifi;

import android.net.Uri;


/**
 * Created by fengxuan on 2017/2/9.
 */
public class WifiConfig {

    private String SSID = "";       //网络名称
    private String security = Constants.SECURITY_NONE;  //加密方式
    private String pwd = "";        //密码
    private OperWifiManager.ProxySetting proxySetting = OperWifiManager.ProxySetting.NONE;     //代理方式
    private Uri pac;        //自动代理PAC网址
    private String host = "";       //主机
    private int port = -1;        //端口
    private String[] exclList;  //不需要代理的网址
    private OperWifiManager.IpAssignment ipAssignment = OperWifiManager.IpAssignment.DHCP;        //ip方式
    private String ipAddress = "";  //ip地址
    private String gateWay = "";    //网关
    private int prefix = -1;        //网络前缀长度
    private String dns1 = "";       //DNS1
    private String dns2 = "";       //DNS2

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Uri getPac() {
        return pac;
    }

    public void setPac(Uri pac) {
        this.pac = pac;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getExclList() {
        return exclList;
    }

    public void setExclList(String[] exclList) {
        this.exclList = exclList;
    }

    public OperWifiManager.ProxySetting getProxySetting() {
        return proxySetting;
    }

    public void setProxySetting(OperWifiManager.ProxySetting proxySetting) {
        this.proxySetting = proxySetting;
    }

    public OperWifiManager.IpAssignment getIpAssignment() {
        return ipAssignment;
    }

    public void setIpAssignment(OperWifiManager.IpAssignment ipAssignment) {
        this.ipAssignment = ipAssignment;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }
}