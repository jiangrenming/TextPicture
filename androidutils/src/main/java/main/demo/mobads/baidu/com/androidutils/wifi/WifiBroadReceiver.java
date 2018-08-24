package main.demo.mobads.baidu.com.androidutils.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by fengxuan on 2017/2/20.
 */
public class WifiBroadReceiver extends BroadcastReceiver {

    private WifiState wifiState = WifiState.NONE;
    private OnWifiStateChangeListener listener;
    private int failCount = 0;  //连接失败次数，当输入密码错误时，pos会自动尝试连接5次，5次之后设置成已保存
    private String preSSID = "";

    public WifiBroadReceiver(OnWifiStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info= wifiManager.getConnectionInfo();
            String curSSID  = info.getSSID();
            if (networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.AUTHENTICATING)) {
                wifiState = WifiState.AUTHENTICATING;
            } else if (networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.CONNECTED)) {
                wifiState = WifiState.CONNNECTED;
            } else if (networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.CONNECTING)) {
                wifiState = WifiState.CONNECTING;
            } else if (networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.DISCONNECTED)) {
                wifiState = WifiState.DISCONNECTED;
                if (curSSID.equals(preSSID)){
                    failCount++;
                    if (failCount == 4){
                        wifiState = WifiState.FAILED;
                    }
                }else {
                    failCount = 0;
                    preSSID = curSSID;
                }
            } else if (networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.OBTAINING_IPADDR)) {
                wifiState = WifiState.OBTAINING_IPADDR;
            }else if (networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.FAILED)) {
                wifiState = WifiState.FAILED;
            }else if(networkInfo.getDetailedState().equals(NetworkInfo.DetailedState.SCANNING)){
                wifiState = WifiState.SCANNING;
            }else {
                return;
            }
            if (listener != null){
                listener.onChanged(wifiState,networkInfo.getState(),info.getSSID());
                wifiState = WifiState.NONE;
            }
        }
    }

    public void resetCount(){
        failCount = 0;
        preSSID = "";
    }

    public interface OnWifiStateChangeListener{
        void onChanged(WifiState state, NetworkInfo.State state2, String SSID);
    }

}
