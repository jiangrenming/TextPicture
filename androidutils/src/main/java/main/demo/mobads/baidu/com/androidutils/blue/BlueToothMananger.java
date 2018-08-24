package main.demo.mobads.baidu.com.androidutils.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author jiangrenming
 * @date 2018/8/22
 * 蓝牙封装方法类（只需要操作界面上的逻辑即可）
 */

public class BlueToothMananger {

    /**
     * 蓝牙管理器
     */
    private BlueToothMananger(){}
    private static class BlueMananger{
        private static BlueToothMananger btManager = new BlueToothMananger();
    }
    public static BlueToothMananger getInstance(){
        return BlueMananger.btManager;
    }

    /**
     * 蓝牙状态接口回调
     */
    private BlueToothCallBack blueToothCallBack;
    public  interface  BlueToothCallBack{

        void onBluetoothDevice(BluetoothDevice device); //搜索到新设备
        void onBltIng(BluetoothDevice device);         //连接中
        void onBltEnd(BluetoothDevice device);        //连接完成
        void onBltNone(BluetoothDevice device);      //未连接
        void onBltOpen();                           //蓝牙已打开
    }


    private ArrayList<BluetoothDevice> connectedDevices;
    /**
     * 获取系统中已经配对的蓝牙设备
     */
    public ArrayList<BluetoothDevice> getAlreadyDevices(){
        if (getmBlueAdapter() == null){
            return null;
        }
        connectedDevices = new ArrayList<>();
        Set<BluetoothDevice> bondedDevices = getmBlueAdapter().getBondedDevices();
        if (bondedDevices  != null && bondedDevices.size() > 0) {
            for (Iterator<BluetoothDevice> it = bondedDevices.iterator(); it.hasNext(); ) {
                BluetoothDevice device = it.next();
                connectedDevices.add(device);
            }
        }
        return  connectedDevices;
    }

    /**
     * 1.蓝牙适配器,属于单例模式
     */
    private BluetoothAdapter mBlueAdapter;
    public BluetoothAdapter getmBlueAdapter(){
        if (mBlueAdapter == null){
            mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return  mBlueAdapter;
    }
    /**
     * 2.蓝牙开关是否打开(是否支持蓝牙设备)
     */
    public  boolean checkBlueDevices(){
        if (getmBlueAdapter() != null){
             return  getmBlueAdapter().isEnabled();
        }
        return  false;
    }

    /**
     * 3.注册蓝牙广播，搜索周围的蓝牙设备
     */
    public  void registerReciver(Context context ,BlueToothCallBack toothCallBack){
        this.blueToothCallBack = toothCallBack;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        context.registerReceiver(searchDevices, intentFilter);
    }

    /**
     * 蓝牙接收广播
     */
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        //接收
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();
            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
            }
            BluetoothDevice device;
            // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                blueToothCallBack.onBluetoothDevice(device);
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        blueToothCallBack.onBltIng(device);
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        blueToothCallBack.onBltEnd(device);
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        blueToothCallBack.onBltNone(device);
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // 蓝牙状态改变（开启&关闭）
                if(getmBlueAdapter().isEnabled()) {
                    startSearchBlue(context);
                    blueToothCallBack.onBltOpen();
                }
            }
        }
    };

    /**
     * 开始搜索蓝牙(当搜索到新设备的时候，就停止搜索蓝牙)
     * @param context
     * @return
     */
    public  boolean startSearchBlue(Context context){
        if (getmBlueAdapter()!= null && getmBlueAdapter().isDiscovering()){
            stopSearchBlue();
        }
        return  getmBlueAdapter().startDiscovery();
    }

    /**
     * 停止蓝牙搜索
     * @return
     */
    public boolean stopSearchBlue(){
        return  getmBlueAdapter().cancelDiscovery();
    }

    /**
     * 自定义蓝牙状态的相关码
     */
    //搜索蓝牙设备，在BroadcastReceiver显示结果
    public  static  final int BLUE_TOOTH_SEARTH = 1000;
    //本机蓝牙启用
    public  static  final int BLUE_TOOTH_OPEN = 1001;
    //本机蓝牙禁用
    public  static  final int BLUE_TOOTH_CLOSE = 1002;
    //本机蓝牙可以在120s内被搜索到
    public  static  final int BLUE_TOOTH_MY_SEARTH = 1003;
    //本机蓝牙关闭当前连接
    public  static  final int BLUE_TOOTH_CLEAR = 1004;
    /**
     *  4.蓝牙事件操作
     */
    public  void searchBlueClick(Context context, int status){
        switch (status){
            case BLUE_TOOTH_SEARTH:
                if (getmBlueAdapter().isEnabled()) {
                    startSearchBlue(context);
                } else {
                    getmBlueAdapter().enable();
                }
                break;
            case BLUE_TOOTH_OPEN:
                if (getmBlueAdapter() != null)
                    getmBlueAdapter().enable();
                break;
            case BLUE_TOOTH_CLOSE:
                if (getmBlueAdapter() != null)
                    getmBlueAdapter().disable();
                break;
            case BLUE_TOOTH_MY_SEARTH:
                Method setDiscoverableTimeout = null;
                try {
                    setDiscoverableTimeout =BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
                    setDiscoverableTimeout.setAccessible(true);
                    Method setScanMode =BluetoothAdapter.class.getMethod("setScanMode", int.class,int.class);
                    setScanMode.setAccessible(true);
                    setDiscoverableTimeout.invoke(getInstance(), 300);
                    setScanMode.invoke(getInstance(), BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,300);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case BLUE_TOOTH_CLEAR:
                try {
                    if (getmBluetoothSocket() != null)
                        getmBluetoothSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 5.检查蓝牙是否配对并尝试连接蓝牙设备(配对)
     */
    public  void startConnection(BluetoothDevice device, Handler mHandler){
        //在建立连接之前，搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
        if (getmBlueAdapter()!= null && getmBlueAdapter().isDiscovering()){
            getmBlueAdapter().cancelDiscovery();
        }
       if (device != null && device.getBondState() == BluetoothDevice.BOND_NONE){   //未配对的情况下
            // 配对
           try {
               Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
               createBondMethod.invoke(device);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }else {
            //已配对的，直接连接
            connectBlue(device,mHandler);
       }
    }


    //配对成功后的蓝牙套接字
    private BluetoothSocket mBluetoothSocket;
    //蓝牙UUID
    public static UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //自定义蓝牙配对的状态码
    public  static  final int connecting = 1;
    public  static  final  int connected = 2;
    public  static  final  int unconnected = 3;
    /**
     * 针对已配对的设备来连接蓝牙
     */
    private void connectBlue(BluetoothDevice device, Handler mHandler) {

        //通过和服务器协商的uuid来进行连接
        //api <2.3 createRfcommSocketToServiceRecord，大于的用createInsecureRfcommSocketToSer
        try {
            int sdk = Integer.parseInt(Build.VERSION.SDK);
            if (sdk >= 10) {
                mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            } else {
                mBluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            }
            Message message1 = new Message();
            message1.what = connecting;
            message1.obj = device;
            mHandler.sendMessage(message1);
            //如果当前socket处于非连接状态则调用连接
            if (!mBluetoothSocket.isConnected()) {
                mBluetoothSocket.connect();
            }
            mBluetoothSocket.close();
            mBluetoothSocket=null;
            if (mHandler == null)return;
            //连接成功结果回调
            Message message = new Message();
            message.what = connected;
            message.obj = device;
            mHandler.sendMessage(message);

        }catch (IOException e){
            //由于4.3之后使用UUID连接有问题，所有才有反射机制来实现连接
            try {
                mBluetoothSocket=(BluetoothSocket)device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,2);
                mBluetoothSocket.connect();
                mBluetoothSocket.close();
                mBluetoothSocket=null;
                if (mHandler == null) return;
                //连接成功结果回调
                Message message = new Message();
                message.what = connected;
                message.obj = device;
                mHandler.sendMessage(message);
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    //连接失败结果回调
                    Message message = new Message();
                    message.what = unconnected;
                    message.obj = device;
                    mHandler.sendMessage(message);
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取蓝牙连接套子头
     * @return
     */
    public BluetoothSocket getmBluetoothSocket() {
        return mBluetoothSocket;
    }

    /**
     * 反注册广播取消蓝牙的配对
     * @param context
     */
    public void unregisterReceiver(Context context){
        if (getmBlueAdapter() != null)
            stopSearchBlue();
        context.unregisterReceiver(searchDevices);
    }

    /**
     * 移除配对设备
     */
    public  boolean removeBlueDevices(BluetoothDevice btDevice){
        try{
            if (btDevice != null){
                Method removeBondMethod = btDevice.getClass().getMethod("removeBond");
                Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
                return   returnValue.booleanValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
