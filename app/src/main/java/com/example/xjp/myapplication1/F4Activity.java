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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Created by XJP on 15/4/9.
 */

public class F4Activity extends Activity{

    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
    SimpleAdapter adapter;
    TextView textView35;
    TextView textView58;
    TextView textView59;
    ProgressBar progressBar6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.f4activity);
        textView35=(TextView)findViewById(R.id.textView35);
        textView58=(TextView)findViewById(R.id.textView58);
        textView59=(TextView)findViewById(R.id.textView59);
        progressBar6=(ProgressBar)findViewById(R.id.progressBar6);
        final ListView listView9=(ListView)findViewById(R.id.listView9);

        adapter=new SimpleAdapter(this,list,R.layout.listview9,//创建简单适配器
                new String[]{"ScenicPrice","ScenicAddress","ScenicName","ScenicPic"},
                new int[]{R.id.textView35,R.id.textView58,R.id.textView59,R.id.smartimageview});
        listView9.setAdapter(adapter);
        //重新传入bitmap数据时的获取地址方式
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                //副线程
                final Thread thread = new Thread (new Runnable() {
                    @Override
                    public void run() {

                        //网络通讯
                        try {
                            String result ;
                            HttpClient client = new DefaultHttpClient();
                            String uri="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientScenicList?flag=weekend";
                            HttpGet get = new HttpGet(uri);
                            HttpResponse response = client.execute(get);
                            int statuscode = response.getStatusLine().getStatusCode();
                            if (statuscode == HttpStatus.SC_OK) {
                                result = EntityUtils.toString(response.getEntity(), "UTF-8");

                                //解析json
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("ScenicList");
                                    for(int i=0;i<jsonArray.length();i++) {
                                        HashMap<String, Object> map = new HashMap<String, Object>();//HashMap用于存储map，每行数据
                                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                        String ScenicPrice = jsonObject2.getString("ScenicPrice");
                                        String ScenicAddress = jsonObject2.getString("ScenicAddress");
                                        String ScenicName = jsonObject2.getString("ScenicName");
                                        String ScenicPic = jsonObject2.getString(("ScenicPic"));
                                        String ScenicIntroduce = jsonObject2.getString(("ScenicIntroduce"));
                                        //解析uri图片
                                        Bitmap bitmap = null;
                                        try {
                                            URL url = new URL(ScenicPic);
                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                            InputStream inputStream = conn.getInputStream();
                                            bitmap = BitmapFactory.decodeStream(inputStream);

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        map.put("ScenicPrice",ScenicPrice);
                                        map.put("ScenicAddress",ScenicAddress);
                                        map.put("ScenicName",ScenicName);
                                        map.put("ScenicPic",bitmap);
                                        map.put("ScenicIntroduce",ScenicIntroduce);
                                        list.add(map);
                                    }
                                    handler.sendEmptyMessage(0);
                                } catch (JSONException e) {
                                    System.out.println("Json parse error");
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
        listView9.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object>map=(HashMap<String,Object>)listView9.getItemAtPosition(i);
                final SmartImageView smartImageView=(SmartImageView)view.findViewById(R.id.smartimageview);
                Intent intent=new Intent();
                intent.putExtra("ScenicName",map.get("ScenicName").toString());
                intent.putExtra("ScenicPrice",map.get("ScenicPrice").toString());
                intent.putExtra("ScenicAddress",map.get("ScenicAddress").toString());
                intent.putExtra("ScenicIntroduce",map.get("ScenicIntroduce").toString());
                intent.putExtra("ScenicClass","weekend");
                intent.putExtra("ScenicPic",((BitmapDrawable) ((SmartImageView) smartImageView).getDrawable()).getBitmap());

                intent.setClass(F4Activity.this,ShowDetail.class);
                startActivity(intent);
            }
        });
            }




    private void showTip(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(F4Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((F4Activity)mActivity.get()).progressBar6.setVisibility(View.INVISIBLE);
                    ((F4Activity) mActivity.get()).adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }


    }
}