package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.loopj.android.image.SmartImageView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XJP on 15/4/24.
 */
public class F2Activity extends Activity{
    SimpleAdapter adapter;
    ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
    ProgressBar progressBar12;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.f2activity);
        final ListView listView14=(ListView)findViewById(R.id.listView14);
        progressBar12=(ProgressBar)findViewById(R.id.progressBar12);

        adapter=new SimpleAdapter(this,list,R.layout.listview14,
                new String[]{"ScenicName","ScenicPrice","ScenicAddress","ScenicPic"},
                new int[]{R.id.textView79,R.id.textView80,R.id.textView81,R.id.smartimageview6});
        listView14.setAdapter(adapter);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // 判断是否为我们要处理的对象
                if (view instanceof SmartImageView && data instanceof Bitmap) {
                    SmartImageView iv = (SmartImageView) view;

                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else
                    return false;
            }
        });

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar12.setVisibility(View.VISIBLE);
                try{
                    String uri="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientgetHotel";
                    String result=null;
                    HttpClient client=new DefaultHttpClient();
                    HttpGet get=new HttpGet(uri);
                    HttpResponse response=client.execute(get);
                    int statuscode=response.getStatusLine().getStatusCode();
                    if (statuscode== HttpStatus.SC_OK)
                        result= EntityUtils.toString(response.getEntity(),"UTF-8");
                    try{
                        JSONObject jsonObject=new JSONObject(result);
                        JSONArray jsonArray=jsonObject.getJSONArray("Hotel");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=(JSONObject)jsonArray.opt(i);
                            HashMap<String,Object>map=new HashMap<>();
                            String ScenicName=jsonObject2.getString("name");
                            String ScenicPrice=jsonObject2.getString("price");
                            String ScenicAddress=jsonObject2.getString("address");
                            String ScenicPic=jsonObject2.getString("imag");
                            String ScenicIntroduce=jsonObject2.getString("introduce");
                            Bitmap bitmap=null;
                            try{
                                URL url=new URL(ScenicPic);
                                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                                InputStream inputStream=conn.getInputStream();
                                bitmap= BitmapFactory.decodeStream(inputStream);
                            }

                            catch (MalformedURLException e){
                                e.printStackTrace();}

                            map.put("ScenicName",ScenicName);
                            map.put("ScenicPrice",ScenicPrice);
                            map.put("ScenicAddress",ScenicAddress);
                            map.put("ScenicPic",bitmap);
                            map.put("ScenicIntroduce",ScenicIntroduce);
                            list.add(map);
                            }
                        handler.sendEmptyMessage(0);
                    }
                    catch (JSONException e)
                    {e.printStackTrace();}
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        listView14.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object>map=(HashMap<String,Object>)listView14.getItemAtPosition(i);
                final SmartImageView smartImageView6=(SmartImageView)view.findViewById(R.id.smartimageview6);
                Intent intent=new Intent();
                intent.putExtra("ScenicName",map.get("ScenicName").toString());
                intent.putExtra("ScenicPrice",map.get("ScenicPrice").toString());
                intent.putExtra("ScenicAddress",map.get("ScenicAddress").toString());
                intent.putExtra("ScenicIntroduce",map.get("ScenicIntroduce").toString());
                intent.putExtra("ScenicClass","hotel");
                intent.putExtra("ScenicPic",((BitmapDrawable)smartImageView6.getDrawable()).getBitmap());

                intent.setClass(F2Activity.this,ShowDetail.class);
                startActivity(intent);
            }
        });
    }




    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {

        private final WeakReference<Activity> mActivity;
        private IHandler(F2Activity activity){
            mActivity=new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int flag=msg.what;
            switch (flag){
                case 0:
                    ((F2Activity)mActivity.get()).progressBar12.setVisibility(View.INVISIBLE);
                    ((F2Activity)mActivity.get()).adapter.notifyDataSetChanged();
                    break;
            }

        }
    }

}
