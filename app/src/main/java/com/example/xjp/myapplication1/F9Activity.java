package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by XJP on 15/4/9.
 */

public class F9Activity extends Activity{
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
    SimpleAdapter adapter;
    ProgressBar progressBar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.f9activity);

        final ListView listView7=(ListView)findViewById(R.id.listView7);
        final EditText editText12=(EditText)findViewById(R.id.editText12);
        final EditText editText13=(EditText)findViewById(R.id.editText13);
        final Button button8=(Button)findViewById(R.id.button8);
        progressBar3=(ProgressBar)findViewById(R.id.progressBar3);

        adapter=new SimpleAdapter(this,list,R.layout.listview7,//创建简单适配器
                new String[]{"start","arrive","date","price"},
                new int[]{R.id.textView43,R.id.textView44,R.id.textView45,R.id.textView46});
        listView7.setAdapter(adapter);
        //设置按下效果
        button8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button8.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button8.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =(InputMethodManager)F9Activity.this.getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText12.getWindowToken(), 0); //隐藏键盘

//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘
                progressBar3.setVisibility(View.VISIBLE);
                //副线程
                final Thread thread = new Thread (new Runnable() {
                    @Override
                    public void run() {
                        //网络通讯
                        String from = editText12.getText().toString();
                        String to =editText13.getText().toString();
                        try {
                            String result ;
                            HttpClient client = new DefaultHttpClient();
                            String appkey = "2eb2ada792b0ea09a69a2ccd32c58cf1";
                            String uri = "http://op.juhe.cn/onebox/bus/query_ab?key="+appkey+"&from="+from+"&to="+to;
                            HttpGet get = new HttpGet(uri);
                            HttpResponse response = client.execute(get);
                            int statuscode = response.getStatusLine().getStatusCode();
                            if (statuscode == HttpStatus.SC_OK) {
                                result = EntityUtils.toString(response.getEntity(), "UTF-8");

                                //解析json
                                try {
                                    JSONObject jsonObject = new JSONObject(result).getJSONObject("result");
                                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                                    for(int i=0;i<jsonArray.length();i++) {
                                        HashMap<String, Object> map = new HashMap<String, Object>();//HashMap用于存储map，每行数据
                                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                        String start = jsonObject2.getString("start");
                                        String arrive = jsonObject2.getString("arrive");
                                        String date = jsonObject2.getString("date");
                                        String price = jsonObject2.getString("price");
                                        map.put("start", "出发站:"+start);
                                        map.put("arrive", "到达站:"+arrive);
                                        map.put("date","时刻:"+date);
                                        map.put("price","价格:"+price);
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

            }

        });


    }

    private void showTip(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(F9Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((F9Activity) mActivity.get()).progressBar3.setVisibility(View.INVISIBLE);
                    ((F9Activity) mActivity.get()).adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }


    }
}