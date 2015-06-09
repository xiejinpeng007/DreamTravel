package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xjp.myapplication1.DATABASE.DB;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by XJP on 15/4/21.
 */
public class ShowDetail extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
            setContentView(R.layout.activity_showdetail);

            TextView textView2=(TextView)findViewById(R.id.textView2);
            TextView textView21=(TextView)findViewById(R.id.textView21);
            TextView textView28=(TextView)findViewById(R.id.textView28);
            TextView textView69=(TextView)findViewById(R.id.textView69);
            final Button button6=(Button)findViewById(R.id.button6);
            ImageView imageView7=(ImageView)findViewById(R.id.imageView7);
            ImageView imageView25=(ImageView)findViewById(R.id.imageView25);
            final SharedPreferences sharedPreferences=getSharedPreferences("flag", Context.MODE_PRIVATE);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏虚拟键盘
            Intent intent=getIntent();
            final String ScenicName=intent.getStringExtra("ScenicName");
            final String ScenicPrice=intent.getStringExtra("ScenicPrice");
            final String ScenicAddress=intent.getStringExtra("ScenicAddress");
            final String ScenicIntroduce=intent.getStringExtra("ScenicIntroduce");
            final String ScenicClass=intent.getStringExtra("ScenicClass");
            Bitmap ScenicPic=intent.getParcelableExtra("ScenicPic");
            textView2.setText(ScenicName);
            textView21.setText("价格: "+ScenicPrice);
            textView28.setText("地址: "+ScenicAddress);
            textView69.setText(ScenicIntroduce);
            imageView7.setImageBitmap(ScenicPic);
            Calendar calendar=Calendar.getInstance(Locale.CHINA);
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            final String date=year+"-"+month+"-"+day;
            //收藏功能
            imageView25.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleDateFormat simpleDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd");
                    String date  = simpleDateFormat.format(new java.util.Date());
                    String username=sharedPreferences.getString("username","visitor");
                    DB db=new DB(getApplicationContext());
                    SQLiteDatabase dbWrite=db.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("username",username);
                    contentValues.put("ScenicName",ScenicName);
                    contentValues.put("ScenicPrice",ScenicPrice);
                    contentValues.put("ScenicAddress",ScenicAddress);
                    contentValues.put("ScenicClass",ScenicClass);
                    contentValues.put("date",date);
                    dbWrite.insert("userfavorite", null, contentValues);
                    db.close();
                    Toast.makeText(getApplicationContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                }
            });
            //设置button按下效果
            button6.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                        button6.setBackgroundResource(R.drawable.button_shape2);
                    else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                        button6.setBackgroundResource(R.drawable.button_shape);
                    return false;
                }
            });
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Boolean flag=sharedPreferences.getBoolean("loginflag",false);
                            if (flag==false)
                                handler.sendEmptyMessage(0);
                            else {
                            try {
                                String username = sharedPreferences.getString("username","");
                                String result = null;
                                String uri ="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientOrder?username="+username+"&scenicname="+ScenicName+"&price="+ScenicPrice+"&time="+date+"&scenicclass="+ScenicClass+"";
                                HttpClient client = new DefaultHttpClient();
                                HttpGet get = new HttpGet(uri);
                                HttpResponse response = client.execute(get);
                                int statuscode = response.getStatusLine().getStatusCode();
                                if (statuscode == HttpStatus.SC_OK)
                                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                                System.out.println(result);
                                if (result.equals("1"))
                                    handler.sendEmptyMessage(1);
                                else if (result.equals("2"))
                                    handler.sendEmptyMessage(2);
                            }

                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }}
                    });
                    thread.start();
                }
            });}

    private void showTip(String str){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }


    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(ShowDetail activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((ShowDetail)mActivity.get()).showTip("请先登陆");
                    break;
                case 1:
                    ((ShowDetail)mActivity.get()).showTip("购买成功");
                    break;
                case 2:
                    ((ShowDetail)mActivity.get()).showTip("购买失败");
                    break;
                default:
                    break;
            }

        }


    }

        }

