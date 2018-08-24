package main.demo.mobads.baidu.com.textpicturedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.demo.mobads.baidu.com.androidutils.wifi.Constants;
import main.demo.mobads.baidu.com.androidutils.wifi.OperWifiManager;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiBroadReceiver;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiConfig;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiInformation;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiState;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiUtil;

/**
 *
 * @author jiangrenming
 * @date 2018/8/23
 * wifi界面
 */

public class WifiActivity extends Activity implements View.OnClickListener{

    private ImageView mSwitch;
    private ListView wifi_list;
    private List<WifiInformation> wifis = new ArrayList<>();
    private OperWifiManager mOperWifi;
    private  WifiAdapter mAdapter;
    private WifiBroadReceiver.OnWifiStateChangeListener onWifiStateChangeListener;
    private  WifiBroadReceiver mReceiver;
    private WifiInformation curFailedInfo = null;       //当前连接失败的
    private  WifiInformation curConnectingInfo = null;   //当前连接中的wifi

    //开启一个定时器刷新wifi
    private TimerTask mTask = null;
    private  Timer mTime = null;

    private boolean isClickOpen = false;
    private boolean isOpen = false;  //wifi网络是否可用
    private boolean isRemoveKeep = false; //点击取消保存时使用
    private boolean isSettingProxyOrIp = false; //当前wifi是否有设置代理与ip信息
    private  int refreshTimes = 3; //连接次数最大次数
    private WifiConfig wifiConfig;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        initView();
        initData();
        initClick();
        initWifi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册wifi扫描广播
        registerWifiReceiver();
        if (mOperWifi.isOpenWifi()){
            startTimer(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        unregisterReceiver(mReceiver);
    }


    private  void startTimer (long delay){
        stopTimer();
        mTime = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initWifiInformations();
                    }
                });
            }
        };
        mTime.schedule(mTask,delay,Constants.PERIOD);
    }

    /**
     * 扫描wifi信息
     */
    private void initWifiInformations() {

        if (isOpen){
            //获取wifi 列表
            List<ScanResult> scanResults = mOperWifi.startScan();
            if (scanResults != null && scanResults.size() >0){
                 wifis.clear();
                for (ScanResult scanResult:scanResults) {
                    WifiInformation wifiInformation = new WifiInformation();
                    wifiInformation.setBSSID(scanResult.BSSID);
                    wifiInformation.setSSID(scanResult.SSID);
                    wifiInformation.setFrequence(scanResult.frequency+"");
                    wifiInformation.setLevel(scanResult.level);
                    wifiInformation.setWifiStrength(WifiUtil.convertWifiStrength(scanResult));
                    wifiInformation.setSecurity(WifiUtil.getSecurity(scanResult));

                    //为避免有SSID相同的wifi添加到列表，参考系统设置做法，这里过滤相同SSID的wifi，只保留一个
                    boolean hasWifi = false;
                    for (WifiInformation infos : wifis){
                        if (wifiInformation.getSSID().equals(infos.getSSID())){
                            hasWifi = true;
                        }
                    }
                    if (!hasWifi){
                        wifis.add(wifiInformation);
                    }
                }
                //按信号强弱排序wifi
                sortWifi();
                mAdapter.notifyDataSetChanged();
            }else {
                wifi_list.setVisibility(View.GONE);
            }
        }
    }
    //按照1.已连接，2.已保存，3.信号强弱进行排序
    private void sortWifi() {
       if (wifis.size() == 0){
           return;
       }
        //先按照信号强弱进行排序
        for (int i = 0; i <wifis.size()-1 ; i++) {
            for (int j = i+1; j < wifis.size(); j++) {
                if (wifis.get(i).getLevel() < wifis.get(j).getLevel()){
                    WifiInformation wifiInformation = wifis.get(j);
                    wifis.remove(j);
                    wifis.add(i,wifiInformation);
                }
            }
        }
        //之后按照已连接，已保存进行排序
        //1.先将扫描的wifi状态都设置为未连接
        for (WifiInformation wifiInfos :wifis) {
            wifiInfos.setState(WifiState.NONE);
        }
        //获取保存过的wifi
        WifiInformation tempConnInfo = null;
        List<WifiConfiguration> storeWifiList = mOperWifi.getStoreWifiList();
        for (int i = 0; i < wifis.size(); i++) {
            WifiInformation wifiInformation = wifis.get(i);
            for (int j = 0; j < storeWifiList.size(); j++) {
                WifiConfiguration wifiConfiguration = storeWifiList.get(j);
                if (wifiConfiguration.SSID.equals(WifiUtil.formatSSIDWithYinhao(wifiInformation.getSSID()))){
                      if (mOperWifi.compareConnectedWifi(wifiInformation)){  //已连接
                          wifiInformation.setState(WifiState.CONNNECTED);
                          tempConnInfo = wifiInformation;
                      }else if (curFailedInfo != null && curFailedInfo.getSSID().equals(wifiInformation.getSSID())){  //连接失败
                          tempConnInfo = wifiInformation;
                      }else {  //已保存
                          wifiInformation.setState(WifiState.STORED);
                          //已保存的网络显示在前面
                          wifis.remove(wifiInformation);
                          wifis.add(0,wifiInformation);
                      }
                }
            }
        }
        if (tempConnInfo != null){
            wifis.remove(tempConnInfo);
            wifis.add(0,tempConnInfo);
        }
        for (int i = 0; i < wifis.size(); i++) {
            if (TextUtils.isEmpty(wifis.get(i).getSSID())){
                wifis.remove(wifis.get(i));
            }
        }
    }


    private void stopTimer() {
        if (mTime != null) {
            mTime.cancel();
            mTime = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private  void registerWifiReceiver(){
        IntentFilter intent = new IntentFilter(Constants.ACTION_WIFI_STATE_CHANGE);
        onWifiStateChangeListener = new WifiBroadReceiver.OnWifiStateChangeListener() {
            @Override
            public void onChanged(WifiState state, NetworkInfo.State state2, String SSID) {
                // 由于广播有延迟，如果是取消保存操作不更新状态以免显示bug
                if (isRemoveKeep){
                    isRemoveKeep = false;
                    return;
                }
                //SSID不在已搜到的wifi列表里不更新状态
                if (!checkSSID(WifiUtil.formatSSIDWithoutYinhao(SSID))){
                    return;
                }
                WifiInformation tempInfo = new WifiInformation();
                List<WifiConfiguration> storeWifiList = mOperWifi.getStoreWifiList();
                for (WifiInformation info:wifis){
                    if (info.getSSID().equals(WifiUtil.formatSSIDWithoutYinhao(SSID))){
                        info.setState(state);
                        tempInfo = info;
                        wifis.add(0,tempInfo);
                        break;
                    }
                }
                if (state == WifiState.CONNECTING){
                    if (!isClickOpen || refreshTimes == 0){
                        refreshTimes = 3;
                        stopTimer();
                    }else {
                        refreshTimes--;
                    }
                    curConnectingInfo = tempInfo;
                    curFailedInfo = null;
                }else if (state == WifiState.FAILED){
                    curFailedInfo = tempInfo;
                }else if (state == WifiState.CONNNECTED){
                    curConnectingInfo = null;
                    //如果有设置代理或者ip，会在连接后配置config重连
                    if (isSettingProxyOrIp){
                        isSettingProxyOrIp = false;
                        try {
                            mOperWifi.setIpAndProxySetting(wifiConfig,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        startTimer(Constants.PERIOD);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        mReceiver = new WifiBroadReceiver(onWifiStateChangeListener);
        registerReceiver(mReceiver,intent);
    }
    /**
     * 由于系统广播有时候会发送CONNECTING状态但是名称为空的网络，这时候不校验就会出现wifi名称空白的状态
     */
    private boolean checkSSID(String SSID){
        boolean result = false;
        for (WifiInformation wifiInformation:wifis){
            if (wifiInformation.getSSID().equals(SSID)){
                result = true;
                break;
            }
        }
        return result;
    }

    AlertDialog connectedInfoDialog = null;
    AlertDialog toConnectDialog = null;
    AlertDialog connectingDialog = null;
    AlertDialog longClickDialog = null;
    AlertDialog addWifiDialog = null;
    AlertDialog failedDialog = null;

    private void initWifi() {
        isOpen = mOperWifi.isOpenWifi();
        if (isOpen){
            wifi_list.setVisibility(View.VISIBLE);
            mSwitch.setImageResource(R.mipmap.img_open);
        }else {
            wifi_list.setVisibility(View.GONE);
            mSwitch.setImageResource(R.mipmap.img_close);
        }
    }

    private void initClick() {
        mSwitch.setOnClickListener(this);
        //点击进入wifi详情
        mAdapter.setWifiDetailsCallBack(new WifiAdapter.WifiDetailsCallBack() {
            @Override
            public void getDetials(int position, WifiInformation wifiInformation) {
                if (wifiInformation != null){
                    if (wifiInformation.getState() == WifiState.CONNNECTED){

                    }else if (wifiInformation.getState() == WifiState.NONE ){

                    }else if (wifiInformation.getState() == WifiState.STORED|| wifiInformation.getState() == WifiState.DISCONNECTED){

                    }else {

                    }
                }
            }
        });
        //弹出弹出窗
        wifi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                WifiInformation wifiInformation = wifis.get(position);
                if (wifiInformation.getState() != WifiState.NONE){
                    String[] items = new String[2];
                    items[0] = "取消保存网络";
                    items[1] = "修改网络";
                }else {
                    //点击未连接的wifi
                    String[] items = new String[1];
                    items[0] = "连接到网络";
                }
            }
        });

    }

    private  void connectionDialog(WifiInformation wifiInfo){

    }

    /**
     * 取消保存网络
     * @param netId
     * @param position
     */
    private void cancelKeep(int netId, int position){
        stopTimer();
        boolean result = false;
        if (netId == -1){
            //取消保存当前连接网络
            result = mOperWifi.removeWifi();
        }else {
            //取消保存指定网络
            result = mOperWifi.removeWifi(netId);
        }
        if (result) {
            isRemoveKeep = true;
            curConnectingInfo = null;
            wifis.get(position).setState(WifiState.NONE);
            mAdapter.notifyDataSetChanged();
            mReceiver.resetCount();
            initWifiInformations();
        }else {
        }
    }

    private void initData() {
        mOperWifi = new OperWifiManager(this);
        mAdapter = new WifiAdapter(this,wifis);
        wifi_list.setAdapter(mAdapter);
    }

    private void initView() {
        mSwitch = (ImageView)findViewById(R.id.onOff);
        wifi_list = (ListView) findViewById(R.id.wifi_list);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.onOff:
                if (isOpen){
                    isOpen = false;
                    isClickOpen = false;
                    stopTimer();
                    mOperWifi.closeWifi();
                    wifis.clear();
                    mAdapter.notifyDataSetChanged();
                }else {
                    isOpen = true;
                    isClickOpen = true;
                    mOperWifi.openWifi();
                    startTimer(Constants.PERIOD);
                }
                initWifi();
                break;
            default:
                break;
        }
    }
}
