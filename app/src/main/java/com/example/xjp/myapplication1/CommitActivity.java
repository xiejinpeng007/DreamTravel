package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XJP on 15/3/23.
 */
public class CommitActivity extends Activity {

    ArrayList<HashMap<String,Object>>list=new ArrayList<>();
    SimpleAdapter adapter;
    ProgressBar progressBar14;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_commit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏键盘

        final SharedPreferences sharedPreferences=getSharedPreferences("flag", Context.MODE_PRIVATE);
        progressBar14=(ProgressBar)findViewById(R.id.progressBar14);
        final ListView listView4=(ListView)findViewById(R.id.listView4);
        adapter=new SimpleAdapter(this,list,R.layout.listview4,
                new String[]{"ScenicClass","time","ScenicName","comment"},
                new int[]{R.id.textView82,R.id.textView83,R.id.textView84,R.id.textView85});
        listView4.setAdapter(adapter);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar14.setVisibility(View.VISIBLE);
                try{
                    String username=sharedPreferences.getString("username","");
                    String uri="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientPostComment?username="+username+"";
                    String result=null;
                    HttpClient client=new DefaultHttpClient();
                    HttpGet get=new HttpGet(uri);
                    HttpResponse response=client.execute(get);
                    int statuscode=response.getStatusLine().getStatusCode();
                    if (statuscode== HttpStatus.SC_OK)
                        result= EntityUtils.toString(response.getEntity(),"UTf-8");
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        JSONArray jsonArray=jsonObject.getJSONArray("Comment");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=(JSONObject)jsonArray.opt(i);
                            HashMap<String,Object>map=new HashMap<>();
                            String ScenicName=jsonObject2.getString("scenicname");
                            String ScenicClass=jsonObject2.getString("scenicclass");
                            String time=jsonObject2.getString("time");
                            String comment=jsonObject2.getString("comment");
                            switch (ScenicClass){
                                case "weekend":ScenicClass="周末游";break;
                                case "foreign":ScenicClass="出境游";break;
                                case "ship":ScenicClass="邮轮游";break;
                                case "scenic":ScenicClass="普通游";break;
                            }
                            map.put("ScenicName",ScenicName);
                            map.put("ScenicClass",ScenicClass);
                            map.put("time",time);
                            map.put("comment","我的评价:"+comment);

                            list.add(map);
                        }
                        handler.sendEmptyMessage(0);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    private void showTip(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(CommitActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((CommitActivity)mActivity.get()).progressBar14.setVisibility(View.INVISIBLE);
                    ((CommitActivity) mActivity.get()).adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }


    }


}