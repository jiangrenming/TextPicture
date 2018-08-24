package main.demo.mobads.baidu.com.textpicturedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import main.demo.mobads.baidu.com.androidutils.wifi.Constants;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiInformation;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiState;
import main.demo.mobads.baidu.com.androidutils.wifi.WifiStrength;


/**
 * Created by fengxuan on 2017/1/17.
 */
public class WifiAdapter extends BaseAdapter {

    private Context context;
    private List<WifiInformation> wifiInformations = new ArrayList<>();
    private ViewHolder holder = null;

    public WifiAdapter(Context context, List<WifiInformation> wifiInformations) {
        this.context = context;
        this.wifiInformations = wifiInformations;
    }

    @Override
    public int getCount() {
        return wifiInformations.size();
    }

    @Override
    public Object getItem(int position) {
        return wifiInformations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wifi_listview,null);
            holder = new ViewHolder();
            holder.imageStrength = (ImageView) convertView.findViewById(R.id.img_strength);
            holder.tvSSID = (TextView) convertView.findViewById(R.id.tv_SSID);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            holder.wifi_details = (ImageView)convertView.findViewById(R.id.wifi_details);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final WifiInformation wifiInformation = wifiInformations.get(position);
        if (wifiInformation.getWifiStrength() == WifiStrength.LOW){
            if (wifiInformation.getSecurity() == Constants.SECURITY_NONE){
                holder.imageStrength.setImageResource(R.mipmap.icon_1_signal);
            }else {
                holder.imageStrength.setImageResource(R.mipmap.icon_1_signal_locked);
            }
        }else if (wifiInformation.getWifiStrength() == WifiStrength.MEDIUM){
            if (wifiInformation.getSecurity() == Constants.SECURITY_NONE){
                holder.imageStrength.setImageResource(R.mipmap.icon_2_signal);
            }else {
                holder.imageStrength.setImageResource(R.mipmap.icon_2_signal_locked);
            }
        }else if (wifiInformation.getWifiStrength() == WifiStrength.HIGH){
            if (wifiInformation.getSecurity() == Constants.SECURITY_NONE){
                holder.imageStrength.setImageResource(R.mipmap.icon_3_signal);
            }else {
                holder.imageStrength.setImageResource(R.mipmap.icon_3_signal_locked);
            }
        }
        holder.tvSSID.setText(wifiInformation.getSSID());
        if (wifiInformation.getState() != WifiState.NONE){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(wifiInformation.getState().getValue());
        }else {
            holder.tvStatus.setText("");
            holder.tvStatus.setVisibility(View.GONE);
        }
        holder.wifi_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wifiInformation.getState() != WifiState.NONE){
                    String[] items = new String[2];
                    items[0] = "取消保存网络";
                    items[1] = "修改网络";
                    mCallBack.getDetials(position,items);
                }else {
                    //点击未连接的wifi
                    String[] items = new String[1];
                    items[0] = "连接到网络";
                    mCallBack.getDetials(position,items);
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView imageStrength;
        TextView tvSSID;
        TextView tvStatus;
        ImageView wifi_details;
    }

    private  WifiDetailsCallBack mCallBack;
    public void setWifiDetailsCallBack(WifiDetailsCallBack callBack){
        this.mCallBack = callBack;
    }
    public  interface  WifiDetailsCallBack{
        void getDetials(int position,String [] items);
    }

}
