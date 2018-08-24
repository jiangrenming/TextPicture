package main.demo.mobads.baidu.com.androidutils.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by fengxuan on 2017/1/18.
 * wifi工具类
 */
public class WifiUtil {

    /**
     * 配置wifi
     * @param SSID
     * @param password
     * @param type
     * @return
     */
    public static WifiConfiguration createConfig(String SSID,
                                                 String password, String type, WifiManager wifiManager, int status)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        config.status=status;
        WifiConfiguration tempConfig = isExsits(SSID, wifiManager);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        if (type.equals(Constants.SECURITY_WEP)) {
            // WEP加密
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }else if (type.equals(Constants.SECURITY_PSK)){
            // wpa/wpa2加密
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }else if (type.equals(Constants.SECURITY_WPA_EAP)){
            //EAP加密
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.preSharedKey = "\"" + password + "\"";
        }else {
            //无加密
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    /**
     * 判断wifi是否已保存
     *
     * @param SSID
     * @param wifiManager
     * @return
     */
    private static WifiConfiguration isExsits(String SSID,
                                              WifiManager wifiManager)
    {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 转换wifi信号强度
     * @return
     */
    public static WifiStrength convertWifiStrength(ScanResult scanResult){
        if (scanResult == null){
            return null;
        }
        if (Math.abs(scanResult.level) > 70){
            return WifiStrength.LOW;
        }else if (Math.abs(scanResult.level) > 60){
            return WifiStrength.MEDIUM;
        }
        return WifiStrength.HIGH;
    }

    public static String formatSSIDWithYinhao(String SSID){
        return "\"" + SSID + "\"";
    }

    public static String formatSSIDWithoutYinhao(String SSID){
        return SSID.replace("\"","");
    }

    /**
     * 将子网掩码转换为网络前缀长度
     * @param netMask
     * @return
     */
    public static int rechangeNetMask(String netMask){
        int prefix = 0;
        String[] items = netMask.split("\\.");
        for (String s:items){
            //转成二进制
            String binaryString = Integer.toBinaryString(Integer.valueOf(s));
            for (Character c:binaryString.toCharArray()){
                if (c.equals('1')){
                    prefix++;
                }
            }
        }
        return prefix;
    }

    /**
     * 获取wifi的加密方式
     * @return 安全性类别
     */
    public static String getSecurity(ScanResult scanResult){

        if (scanResult == null){
            if (scanResult == null){
                return "";
            }
        }

        String capabilities = scanResult.capabilities;
        if (!TextUtils.isEmpty(capabilities)){
            if (capabilities.contains("WPA") || capabilities.contains("wpa")){
                return Constants.SECURITY_PSK;
            }else if (capabilities.contains("WEP") || capabilities.contains("wep")){
                return Constants.SECURITY_WEP;
            }else if (capabilities.contains("EAP") || capabilities.contains("eap")){
                return Constants.SECURITY_WPA_EAP;
            }else {
                return Constants.SECURITY_NONE;
            }
        }
        return Constants.SECURITY_NONE;
    }

    /**
     * 获取wifi的安全性
     * @param wifiConfiguration
     * @return
     */
    public static String getSecurity(WifiConfiguration wifiConfiguration){
        if (wifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)){
            return Constants.SECURITY_PSK;
        }else if (wifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) ||
                wifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)){
            return Constants.SECURITY_WPA_EAP;
        }else if (wifiConfiguration.wepKeys[0] != null){
            return Constants.SECURITY_WEP;
        }
        return Constants.SECURITY_NONE;
    }

    /**
     * 将十进制IP地址转化成二进制标准格式
     * @param ipAddress
     * @return
     */
    public static String rechangeIp(int ipAddress){

        String result = "";
        byte[] addressByte = {
                (byte)(0xff & ipAddress),
                (byte)(0xff & (ipAddress >> 8)),
                (byte)(0xff & (ipAddress >> 16)),
                (byte)(0xff & (ipAddress >> 24))
        };
        InetAddress inet = null;
        try {
            inet = InetAddress.getByAddress(addressByte);
            result = inet.getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    public static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        return newInstance(className, new Class<?>[0], new Object[0]);
    }

    public static Object newInstance(String className, Class<?>[] parameterClasses, Object[] parameterValues) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException
    {
        Class<?> clz = Class.forName(className);
        Constructor<?> constructor = clz.getConstructor(parameterClasses);
        return constructor.newInstance(parameterValues);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException
    {
        Class<Enum> enumClz = (Class<Enum>) Class.forName(enumClassName);
        return Enum.valueOf(enumClz, enumValue);
    }

    public static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
    {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.set(object, value);
    }

    public static <T> T getField(Object object, String fieldName, Class<T> type) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
    {
        Field field = object.getClass().getDeclaredField(fieldName);
        return type.cast(field.get(object));
    }

    public static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, InvocationTargetException {
        Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
            parameterClasses[i] = Class.forName(parameterTypes[i]);

        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
        method.invoke(object, parameterValues);
    }

    public static Object getDeclaredField(Object obj, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        Object out = f.get(obj);
        return out;
    }

    public static void setDeclardFildObject(Object obj, String name, Object object){
        Field f = null;
        try {
            f = obj.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            f.set(obj,object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setEnumField(Object obj, String value, String name)
            throws SecurityException, NoSuchFieldException,IllegalArgumentException, IllegalAccessException {

        Field f = obj.getClass().getField(name);
        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }
}
