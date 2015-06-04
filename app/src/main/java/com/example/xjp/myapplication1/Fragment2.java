package com.example.xjp.myapplication1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XJP on 15/3/12.
 */

public class Fragment2 extends Fragment {
    View view;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
    ProgressBar progressBar13;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);
        final ListView listView13=(ListView)view.findViewById(R.id.listView13);
        progressBar13=(ProgressBar)view.findViewById(R.id.progressBar13);

        adapter=new SimpleAdapter(getActivity(),list,R.layout.listview13,
                new String[]{"ScenicPic","ScenicName","ScenicPrice","ScenicAddress"},
                new int[]{R.id.smartimageview5,R.id.textView73,R.id.textView77,R.id.textView78});
        listView13.setAdapter(adapter);
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
                progressBar13.setVisibility(View.VISIBLE);
                try {
                    String uri="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientScenicList?flag=hot";
                    String result=null;
                    HttpClient client=new DefaultHttpClient();
                    HttpGet get=new HttpGet(uri);
                    HttpResponse response=client.execute(get);
                    int statuscode=response.getStatusLine().getStatusCode();
                    if (statuscode==HttpStatus.SC_OK)
                        result= EntityUtils.toString(response.getEntity(),"UTF-8");
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        JSONArray jsonArray=jsonObject.getJSONArray("ScenicList");
                        for (int i=0;i<jsonArray.length();i++){
                            HashMap<String,Object>map=new HashMap<>();
                            JSONObject jsonObject2=(JSONObject)jsonArray.opt(i);
                            String ScenicName=jsonObject2.getString("ScenicName");
                            String ScenicPrice=jsonObject2.getString("ScenicPrice");
                            String ScenicAddress=jsonObject2.getString("ScenicAddress");
                            String ScenicPic=jsonObject2.getString("ScenicPic");
                            String ScenicIntroduce=jsonObject2.getString("ScenicIntroduce");
                            Bitmap bitmap=null;
                            try {
                                URL url=new URL(ScenicPic);
                                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                                InputStream inputStream=conn.getInputStream();
                                bitmap= BitmapFactory.decodeStream(inputStream);
                            }
                            catch (MalformedURLException e){
                                e.printStackTrace();}
                            catch (IOException e){
                                e.printStackTrace();
                            }
                            map.put("ScenicName",ScenicName);
                            map.put("ScenicPrice",ScenicPrice);
                            map.put("ScenicAddress",ScenicAddress);
                            map.put("ScenicPic",bitmap);
                            map.put("ScenicIntroduce",ScenicIntroduce);
                            list.add(map);
                        }
                        handler2.sendEmptyMessage(0);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        listView13.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object>map=(HashMap<String,Object>)listView13.getItemAtPosition(i);
                final SmartImageView smartImageView5=(SmartImageView)view.findViewById(R.id.smartimageview5);
                Intent intent=new Intent();
                intent.putExtra("ScenicName",map.get("ScenicName").toString());
                intent.putExtra("ScenicPrice",map.get("ScenicPrice").toString());
                intent.putExtra("ScenicAddress",map.get("ScenicAddress").toString());
                intent.putExtra("ScenicIntroduce",map.get("ScenicIntroduce").toString());
                intent.putExtra("ScenicClass","foreign");
                intent.putExtra("ScenicPic",((BitmapDrawable)smartImageView5.getDrawable()).getBitmap());

                intent.setClass(getActivity(),ShowDetail.class);
                startActivity(intent);
            }
        });

        return view;}


    public  void showTip(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }
    IHandler2 handler2=new IHandler2(this);
    private static class IHandler2 extends Handler{
        private final WeakReference<Fragment> mFragment;
        public  IHandler2(Fragment2 fragment){
            mFragment=new WeakReference<Fragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int flag=msg.what;
            switch (flag){
                case 0:
                    ((Fragment2)mFragment.get()).progressBar13.setVisibility(View.INVISIBLE);
                    ((Fragment2)mFragment.get()).adapter.notifyDataSetChanged();
                    break;
                default:break;
            }
        }
    }
}