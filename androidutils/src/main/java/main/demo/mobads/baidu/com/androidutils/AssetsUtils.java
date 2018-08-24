package main.demo.mobads.baidu.com.androidutils;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangrenming on 2018/8/22.
 */

public class AssetsUtils {


    /**
     * 读取assets里的文件并装填到集合中
     * @param context
     * @return
     */
    public  static List<String> getAssetsData(Context context){
        List<String> names = new ArrayList<>();
        try{
            InputStream is = context.getAssets().open("name.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer,"utf-8");
            Log.i("读取文件后的字符串是",text);
            String[] split = text.split(",");
            for (int i = 0; i < split.length; i++) {
                names.add(split[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  names;
    }
}
