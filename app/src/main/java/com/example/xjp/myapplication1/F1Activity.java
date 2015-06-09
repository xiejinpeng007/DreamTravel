package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.image.SmartImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by XJP on 15/4/9.
 */

public class F1Activity extends Activity{
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
//    MyAdapter adapter;
    ProgressBar progressBar9;
    String Jsonresult=null;
    ListView listView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.f1activity);
        final MyAdapter adapter=new MyAdapter(this);
        progressBar9=(ProgressBar)findViewById(R.id.progressBar9);
        listView5=(ListView)findViewById(R.id.listView5);
        listView5.setAdapter(adapter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //初始化imageloader
        ImageLoaderConfiguration imageLoaderConfiguration=ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(imageLoaderConfiguration);

        //获取网络数据
        getwebdata();


        //下拉刷新
        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getwebdata();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_LONG).show();
                //设置弹回延时时间
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        //点击后进入详细页面并传入数据
        listView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object>map=(HashMap<String,Object>)listView5.getItemAtPosition(i);
                final SmartImageView smartImageView4=(SmartImageView)view.findViewById(R.id.smartimageview4);
                Intent intent=new Intent();
                intent.putExtra("ScenicName",map.get("ScenicName").toString());
                intent.putExtra("ScenicPrice",map.get("ScenicPrice").toString());
                intent.putExtra("ScenicAddress",map.get("ScenicAddress").toString());
                intent.putExtra("ScenicIntroduce",map.get("ScenicIntroduce").toString());
                intent.putExtra("ScenicClass","secnic");
                intent.putExtra("ScenicPic",((BitmapDrawable)smartImageView4.getDrawable()).getBitmap());

                intent.setClass(F1Activity.this,ShowDetail.class);
                startActivity(intent);
            }
        });
    }
    //继承并重写BaseAdapter
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /*构造函数*/
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        /*书中详细解释该方法*/
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.listview5,null);
            holder=new ViewHolder();
            holder.textView66=(TextView)convertView.findViewById(R.id.textView66);
            holder.textView67=(TextView)convertView.findViewById(R.id.textView67);
            holder.textView68=(TextView)convertView.findViewById(R.id.textView68);
            holder.smartImageView4=(SmartImageView)convertView.findViewById(R.id.smartimageview4);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.textView66.setText(list.get(position).get("ScenicPrice").toString());
        holder.textView67.setText(list.get(position).get("ScenicAddress").toString());
        holder.textView68.setText(list.get(position).get("ScenicName").toString());

        final DisplayImageOptions options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(
                list.get(position).get("ScenicPic").toString(),
                holder.smartImageView4,options);

        return convertView;
    }
}
public final class ViewHolder{
    public TextView textView66;
    public TextView textView67;
    public TextView textView68;
    public SmartImageView smartImageView4;
}


    //获取数据
public void getwebdata() {

    //使用Volley库进行网络通讯
    String uri = "http://mxly.wicp.net/DreamtimeTravel/servlet/ClientScenicList?flag=scenic";
    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
    StringRequest stringRequest = new StringRequest(uri, new Response.Listener<String>() {
        @Override
        public void onResponse(String result) {
            Jsonresult = result;
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("ErrorResponse", "ErrorResponse");
        }
    });

    requestQueue.add(stringRequest);
    if (Jsonresult == null)
        Log.v("tag", "json is null");
    else
        Log.v("tag", "json is not null");

    //解析JSON
    try {
        JSONObject jsonObject = new JSONObject(Jsonresult);
        JSONArray jsonArray = jsonObject.getJSONArray("ScenicList");
        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, Object> map = new HashMap<>();//HashMap用于存储map，每行数据
            JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
            String ScenicPrice = jsonObject2.getString("ScenicPrice");
            String ScenicAddress = jsonObject2.getString("ScenicAddress");
            String ScenicName = jsonObject2.getString("ScenicName");
            String ScenicPic = jsonObject2.getString(("ScenicPic"));
            String ScenicIntroduce = jsonObject2.getString(("ScenicIntroduce"));
            Toast.makeText(getApplicationContext(), ScenicAddress, Toast.LENGTH_SHORT).show();
            Log.v("Json", ScenicAddress + "    " + ScenicName + "    " + ScenicPic + "    " + ScenicPrice);
            map.put("ScenicPrice", ScenicPrice);
            map.put("ScenicAddress", ScenicAddress);
            map.put("ScenicName", ScenicName);
            map.put("ScenicPic", ScenicPic);
            map.put("ScenicIntroduce", ScenicIntroduce);
            list.add(map);
        }
        progressBar9.setVisibility(View.INVISIBLE);
    } catch (JSONException e) {
        Log.v("tag", "json exception");
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
        Log.v("tag", "exception");
        Toast.makeText(getApplicationContext(), "无法连接服务器", Toast.LENGTH_SHORT).show();
    }
}


}
