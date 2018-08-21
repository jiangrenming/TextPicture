package main.demo.mobads.baidu.com.textpicturedemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangrenming on 2018/8/21.
 */

public class Utils {


    /**
     * 读取assets里的文件并装填到集合中
     * @param context
     * @return
     */
    public  static List<String> getNames(Context context){
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


    /**
     * 读取文字之后，将数据以一行2个字体进行切换行
     * @param name
     * @return
     */
    public   static String spliteNames(String name){
        StringBuffer sb  = new StringBuffer();
        if (!TextUtils.isEmpty(name)){
            int size = name.length();
            int index = 0;
            if (size >=2 && size <=12){
                for (int i = 0; i <size ; i++) {
                    if (i == 0){
                        index += 2;
                        String s = name.substring(0, index);
                        sb.append(s).append("\n");
                    }else {
                        if (size %2 == 0){
                            if (index == size){
                                break;
                            }
                            String s = name.substring(index, index + 2);
                            index += 2;
                            sb.append(s).append("\n");
                        }else {
                            if ((size -index) == 1){
                                String s = name.substring(index);
                                sb.append(s);
                                break;
                            }
                            if ((size -index) > 0 ){
                                String s = name.substring(index, index + 2);
                                index += 2;
                                sb.append(s).append("\n");
                            }
                        }
                    }
                }
            }
        }
        return  sb.toString();
    }



}
