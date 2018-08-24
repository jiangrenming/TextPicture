package main.demo.mobads.baidu.com.androidutils.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jiangrenming on 2018/8/22.
 * wifi 操作的相关方法
 */

public class OperWifiManager {

    public WifiManager getWifiManager() {
        return wifiManager;
    }
    private WifiManager wifiManager;
    private Context context;

    /**
     * ip方式
     */
    public enum IpAssignment{
        STATIC,
        DHCP
    }

    /**
     * 代理方式
     */
    public enum ProxySetting{
        NONE,
        STATIC,
        PAC
    }


    public OperWifiManager(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

    }
    /**
     * 得到 ConnectionInfo
     *
     */
    public WifiInfo getConnectionInfo(){
        return wifiManager.getConnectionInfo();
    }

    public boolean isOpenWifi(){
        return wifiManager.isWifiEnabled();
    }

    /**
     * 没打开则打开wifi
     * @return
     */
    public boolean openWifi() {
        return wifiManager.setWifiEnabled(true);
    }

    /**
     * 关闭wifi
     *
     * @return
     */
    public boolean closeWifi() {
        if (wifiManager.isWifiEnabled()) {
            return wifiManager.setWifiEnabled(false);
        }
        return true;
    }

    /**
     * 获取wifi列表
     *
     * @return
     */
    public List<ScanResult> startScan() {
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        return scanResults;
    }

    /**
     * 连接wifi
     * @param wifiInformation 要连接的wifi
     * @param password        密码
     * @param config          配置信息
     */
    public void connectWifi(final WifiInformation wifiInformation, WifiConfiguration config, String password ) {
        if (wifiInformation == null) {
            return;
        }
        if (config != null) {
            wifiManager.enableNetwork(config.networkId, true);
        } else {
            config = WifiUtil.createConfig(wifiInformation.getSSID(), password, wifiInformation.getSecurity(), wifiManager,0);
            int networkId = wifiManager.addNetwork(config);
            wifiManager.enableNetwork(networkId, true);
            wifiManager.saveConfiguration();
        }
    }

    public void connectWifi(WifiConfiguration config, String password ) {
        if (config != null) {
            wifiManager.enableNetwork(config.networkId, true);
        } else {
            config = WifiUtil.createConfig(WifiUtil.formatSSIDWithoutYinhao(config.SSID), password,WifiUtil.getSecurity(config), wifiManager,0);
            int networkId = wifiManager.addNetwork(config);
            wifiManager.enableNetwork(networkId, true);
        }
    }

    /**
     * 添加网络
     * @return
     */
    public boolean addNetWork(WifiConfig wifiConfig){
        WifiConfiguration config = WifiUtil.createConfig(wifiConfig.getSSID(), wifiConfig.getPwd(), wifiConfig.getSecurity(), wifiManager,2);
        try {
            setIpAndProxySetting(wifiConfig,config);
        } catch (Exception e) {
            e.printStackTrace();

        }
        int networkId = -1;

        networkId = wifiManager.addNetwork(config);
        connectWifi(wifiManager.getConfiguredNetworks().get(wifiManager.getConfiguredNetworks().size()-1),"");
        if (networkId != -1){
            return true;
        }
        return false;
    }

    /**
     * 添加网络
     * @return
     */
    public boolean addNetWork( WifiConfiguration config){
        int networkId = -1;
        networkId = wifiManager.addNetwork(config);
        if (networkId != -1){
            return true;
        }
        return false;
    }

    /**
     * 获取已连接wifi的信息
     *
     * @return
     */
    public WifiInfo getWifiInfo() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取代表联网状态的networkinfo对象
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED &&
                netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return wifiInfo;
        }
        return null;
    }

    /**
     * 取消保存当前已连接的网络
     *
     * @return
     */
    public boolean removeWifi() {

        if (getWifiInfo() == null) {
            return false;
        }
        return removeWifi(getWifiInfo().getNetworkId());
    }

    /**
     * 取消保存此networkId的网络
     *
     * @param networkId
     * @return
     */
    public boolean removeWifi(int networkId) {

        boolean result = wifiManager.removeNetwork(networkId);
        wifiManager.disableNetwork(networkId);
        wifiManager.saveConfiguration();
        return result;
    }

    public int getNetIdBySSID(String SSID){
        List<WifiConfiguration> configuredNetworks = getStoreWifiList();
        for (WifiConfiguration config:configuredNetworks){
            if (config.SSID.equals(SSID)){
                return config.networkId;
            }
        }
        return -1;
    }

    /**
     * 获取已保存的网络
     *
     * @return
     */
    public List<WifiConfiguration> getStoreWifiList() {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        return configuredNetworks;
    }

    /**
     * 从已保存的网络中获取当前欲连接网络的配置信息
     *
     * @param wifiInformation
     * @return
     */
    public WifiConfiguration compareStoreWifi(WifiInformation wifiInformation) {
        //获取保存的网络
        List<WifiConfiguration> configurationList = getStoreWifiList();
        WifiConfiguration config = null;
        for (WifiConfiguration wifiConfiguration : configurationList) {
            if (wifiConfiguration.SSID.equals(WifiUtil.formatSSIDWithYinhao(wifiInformation.getSSID()))) {
                //如果已经保存有配置信息,直接使用此配置
                config = wifiConfiguration;
                return config;
            }
        }
        return null;
    }

    /**
     * 获取当前Wifi的连接配置
     *
     * @return
     */
    public WifiConfiguration getCurrentWifiConfiguration() {
        if (!wifiManager.isWifiEnabled())
            return null;
        List<WifiConfiguration> configurationList = getStoreWifiList();
        WifiConfiguration configuration = null;
        int curNetId = wifiManager.getConnectionInfo().getNetworkId();
        for (int i = 0; i < configurationList.size(); ++i) {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            if (wifiConfiguration.networkId == curNetId)
                configuration = wifiConfiguration;
        }
        return configuration;
    }

    /**
     * 比较传入的wifi是否是当前已连接的wifi
     *
     * @param wifiInformation
     * @return
     */
    public boolean compareConnectedWifi(WifiInformation wifiInformation) {

        if (wifiInformation == null) {
            return false;
        }
        //当前未连接wifi
        if (getWifiInfo() == null) {
            return false;
        }

        String infoSSID = WifiUtil.formatSSIDWithYinhao(wifiInformation.getSSID());   //获取的curSSID会自带一个""号，因此这里加上
        String infoBSSID = wifiInformation.getBSSID();
        String curSSID = getWifiInfo().getSSID();
        String curBSSID = getWifiInfo().getBSSID();

        if (infoSSID.equals(curSSID)
                && infoBSSID.equals(curBSSID)) {
            //同时比较SSID与BSSID,防止同名的SSID
            return true;
        }
        return false;
    }

    /**
     * 设置wifi ip与代理
     * @param wifiConfig
     * @throws Exception
     */
    public void setIpAndProxySetting(WifiConfig wifiConfig, WifiConfiguration config)throws Exception{
        if (config == null){
            config = getCurrentWifiConfiguration();
        }
        WifiConfiguration configuration = setHttpPorxySetting(wifiConfig, config);
        configuration = setIpConfig(wifiConfig, configuration);
        save(configuration);
    }

    /**
     * 设置wifi代理
     * @param wifiConfig
     * @throws Exception
     */
    public WifiConfiguration setHttpPorxySetting(WifiConfig wifiConfig,WifiConfiguration wifiConfiguration) throws Exception {
        WifiConfiguration config;
        if (wifiConfig.getProxySetting() == ProxySetting.STATIC){
            config = setStaticProxy(wifiConfig,wifiConfiguration);
        }else if (wifiConfig.getProxySetting() == ProxySetting.PAC){
            config = setPacProxy(wifiConfig,wifiConfiguration);
        }else {
            config = setNoneProxy(wifiConfiguration);
        }
        return config;
    }

    /**
     * 设置wifi ip
     * @param wifiConfig
     */
    public WifiConfiguration setIpConfig(WifiConfig wifiConfig,WifiConfiguration wifiConfiguration) throws Exception {
        WifiConfiguration config;
        if (wifiConfig.getIpAssignment() == IpAssignment.STATIC){
            config = setStaticIpConfiguration(wifiConfig,wifiConfiguration);
        }else {
            config = setDhcpIpConfiguration(wifiConfiguration);
        }
        return config;
    }

    /**
     * 设置手动代理
     * @param wifiConfig
     * @throws Exception
     */
    private WifiConfiguration setStaticProxy(WifiConfig wifiConfig,WifiConfiguration config) throws Exception {
        ProxyInfo mInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<String> exclList = new ArrayList<>(Arrays.asList(wifiConfig.getExclList()));
            mInfo = ProxyInfo.buildDirectProxy(wifiConfig.getHost(), wifiConfig.getPort(), exclList);
        }
        if (config != null && mInfo != null) {
            //系统的接口被隐藏了，这里使用反射的方式来设置，下面设置ip同理
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Class parmars = Class.forName("android.net.ProxyInfo");
            Method method = clazz.getMethod("setHttpProxy", parmars);
            method.invoke(config, mInfo);
            Object mIpConfiguration = WifiUtil.getDeclaredField(config, "mIpConfiguration");

            WifiUtil.setEnumField(mIpConfiguration, "STATIC", "proxySettings");
            WifiUtil.setDeclardFildObject(config, "mIpConfiguration", mIpConfiguration);
        }
        return config;
    }

    /**
     * 取消代理设置
     */
    public WifiConfiguration setNoneProxy(WifiConfiguration config)
            throws Exception {
        ProxyInfo mInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mInfo = ProxyInfo.buildDirectProxy(null, 0);
        }
        if (config != null) {
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Class parmars = Class.forName("android.net.ProxyInfo");
            Method method = clazz.getMethod("setHttpProxy", parmars);
            method.invoke(config, mInfo);
            Object mIpConfiguration = WifiUtil.getDeclaredField(config, "mIpConfiguration");
            WifiUtil.setEnumField(mIpConfiguration, "NONE", "proxySettings");
            WifiUtil.setDeclardFildObject(config, "mIpConfiguration", mIpConfiguration);
        }
        return config;
    }

    /**
     * 设置pac自动代理
     */
    public WifiConfiguration setPacProxy(WifiConfig wifiConfig,WifiConfiguration config) throws Exception{
        ProxyInfo mInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<String> exclList = new ArrayList<>(Arrays.asList(wifiConfig.getExclList()));
            mInfo = ProxyInfo.buildPacProxy(wifiConfig.getPac());
        }
        if (config != null && mInfo != null) {
            //系统的接口被隐藏了，这里使用反射的方式来设置，下面设置ip同理
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Class parmars = Class.forName("android.net.ProxyInfo");
            Method method = clazz.getMethod("setHttpProxy", parmars);
            method.invoke(config, mInfo);
            Object mIpConfiguration = WifiUtil.getDeclaredField(config, "mIpConfiguration");

            WifiUtil.setEnumField(mIpConfiguration, "PAC", "proxySettings");
            WifiUtil.setDeclardFildObject(config, "mIpConfiguration", mIpConfiguration);
        }
        return config;
    }

    /**
     * 获取wifi代理方式
     * @param config
     * @return
     */
    public ProxySetting getProxySetting(WifiConfiguration config){

        ProxySetting proxySetting = null;
        try {
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Method method = clazz.getMethod("getProxySettings");
            Object object = method.invoke(config);
            String name = ((Enum)object).name();
            if (name.equalsIgnoreCase("NONE")){
                proxySetting = ProxySetting.NONE;
            }else if (name.equalsIgnoreCase("STATIC")){
                proxySetting = ProxySetting.STATIC;
            }else {
                proxySetting = ProxySetting.PAC;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return proxySetting;

    }

    /**
     * 获取wifi代理信息
     * @param config
     * @return proxyInfo
     */
    public ProxyInfo getProxyInfo(WifiConfiguration config){
        ProxyInfo proxyInfo = null;
        try {
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Method method = clazz.getMethod("getHttpProxy");
            proxyInfo = (ProxyInfo) method.invoke(config);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return proxyInfo;
    }

    /**
     * 设置静态ip
     *
     * @param wifiConfig
     */
    @SuppressWarnings("unchecked")
    public WifiConfiguration setStaticIpConfiguration(WifiConfig wifiConfig,WifiConfiguration config) throws Exception{

        // 设置ip方式为静态
        Object ipAssignment = WifiUtil.getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
        WifiUtil.callMethod(config, "setIpAssignment", new String[]{"android.net.IpConfiguration$IpAssignment"}, new Object[]{ipAssignment});
        Object staticIpConfig = WifiUtil.newInstance("android.net.StaticIpConfiguration");
        Object linkAddress = WifiUtil.newInstance("android.net.LinkAddress", new Class<?>[]{InetAddress.class, int.class}
                , new Object[]{InetAddress.getByName(wifiConfig.getIpAddress()), wifiConfig.getPrefix()});
        WifiUtil.setField(staticIpConfig, "ipAddress", linkAddress);
        WifiUtil.setField(staticIpConfig, "gateway", InetAddress.getByName(wifiConfig.getGateWay()));
        WifiUtil.getField(staticIpConfig, "dnsServers", ArrayList.class).clear();
        WifiUtil.getField(staticIpConfig, "dnsServers", ArrayList.class).add(InetAddress.getByName(wifiConfig.getDns1()));
        WifiUtil.getField(staticIpConfig, "dnsServers", ArrayList.class).add(InetAddress.getByName(wifiConfig.getDns2()));
        WifiUtil.callMethod(config, "setStaticIpConfiguration", new String[]{"android.net.StaticIpConfiguration"}, new Object[]{staticIpConfig});
        return config;
    }

    /**
     * 设置ip方式为DHCP
     */
    public WifiConfiguration setDhcpIpConfiguration(WifiConfiguration config) throws Exception {
        // 设置ip方式为静态
        Object ipAssignment = WifiUtil.getEnumValue("android.net.IpConfiguration$IpAssignment", "DHCP");
        WifiUtil.callMethod(config, "setIpAssignment", new String[]{"android.net.IpConfiguration$IpAssignment"}, new Object[]{ipAssignment});
        return config;
    }

    /**
     * 获取ip方式
     * @param config
     * @return
     */
    public IpAssignment getIpAssignment(WifiConfiguration config){
        IpAssignment ipAssignment = null;

        try {
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Method method = clazz.getMethod("getIpAssignment");
            Object object = method.invoke(config);
            String name = ((Enum)object).name();
            if (name.equalsIgnoreCase("STATIC")){
                ipAssignment = IpAssignment.STATIC;
            }else {
                ipAssignment = IpAssignment.DHCP;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ipAssignment;
    }

    /**
     * 获取wifi 静态Ip相关信息
     * @return
     */
    public DhcpInfo getIpInfo(){
        return wifiManager.getDhcpInfo();
    }

    /**
     * 保存配置，重新连接wifi
     * @param config
     */
    public void save(WifiConfiguration config) {
        wifiManager.updateNetwork(config);
        wifiManager.saveConfiguration();
        wifiManager.disconnect();
        wifiManager.reconnect();
    }

    /**
     * disableNetwork   wifi
     * @param netId
     */
    public boolean disableNetwork(int netId) {
        return wifiManager.disableNetwork(netId);
    }

    /**
     * 断开   wifi
     * @param
     */
    public boolean  disconnect() {
        return wifiManager.disconnect();
    }

    /**
     * 更新   wifi信息
     * @param
     */
    public void   updateNetwork(WifiConfiguration config) {
        wifiManager.updateNetwork(config);
    }

}
