package main.demo.mobads.baidu.com.textpicturedemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends Activity {

    private final int WORDNUM = 2;  //转化成图片时  每行显示的字数
    private final int WIDTH = 800;   //设置图片的宽度
    private final int HEIGHT = 800;   //设置图片的宽度
    private ImageView img ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView)findViewById(R.id.img);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    List<String> names = Utils.getNames(MainActivity.this);
                    for (int i = 0; i < names.size(); i++) {
                        String spliteNames = Utils.spliteNames(names.get(i));
                        TextPaint textPaint = new TextPaint();
                        textPaint.setColor(Color.BLACK);
                        textPaint.setTextAlign(Paint.Align.CENTER);
                        textPaint.setTextSize(40);
                        StaticLayout layout = new StaticLayout(spliteNames, textPaint,400,
                                Layout.Alignment.ALIGN_CENTER, 1.3f, 0.5f, true);
                        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), 400, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.translate(10, 10);
                        canvas.drawColor(Color.WHITE);
                        layout.draw(canvas);
                        String path = Environment.getExternalStorageDirectory() + "/image"+System.currentTimeMillis()+".png";
                        Glide.with(MainActivity.this).load(path).into(img);
                        System.out.println(path);
                        FileOutputStream os = new FileOutputStream(new File(path));
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        os.flush();
                        os.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WifiActivity.class));
            }
        });
    }


}
