package com.example.xjp.myapplication1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by XJP on 15/3/13.
 */

public class F3Activity extends Activity {

    ListView listView12;
    EditText editText7;
    ProgressBar progressBar11;
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.f3activity);
        final Button button13=(Button)findViewById(R.id.button13);
        editText7=(EditText)findViewById(R.id.editText7);
        listView12=(ListView)findViewById(R.id.listView12);
        progressBar11=(ProgressBar)findViewById(R.id.progressBar11);

        adapter=new SimpleAdapter(this,list,R.layout.listview12,//创建简单适配器
                new String[]{"weather","temp","date"},
                new int[]{R.id.textView72,R.id.textView75,R.id.textView76});
        listView12.setAdapter(adapter);

        //设置按下效果
        button13.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button13.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button13.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });
        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =(InputMethodManager)F3Activity.this.getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText7.getWindowToken(), 0); //隐藏键盘

//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘
                progressBar11.setVisibility(View.VISIBLE);
                //副线程
                final Thread thread = new Thread (new Runnable() {
                    @Override
                    public void run() {
                        //网络通讯
                        String cityname = editText7.getText().toString();

                        try {
                            String result ;
                            HttpClient client = new DefaultHttpClient();
                            String appkey = "ca2151d36e232c65c5cc878d6e024187";
                            String uri = "http://v.juhe.cn/weather/forecast3h?key="+appkey+"&cityname="+cityname;
                            HttpGet get = new HttpGet(uri);
                            HttpResponse response = client.execute(get);
                            int statuscode = response.getStatusLine().getStatusCode();
                            if (statuscode == HttpStatus.SC_OK) {
                                result = EntityUtils.toString(response.getEntity(), "UTF-8");

                                //解析json
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                                    for(int i=0;i<jsonArray.length();i=i+7) {
                                        HashMap<String, Object> map = new HashMap<String, Object>();//HashMap用于存储map，每行数据
                                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                        String weather = jsonObject2.getString("weather");
                                        String temp1 = jsonObject2.getString("temp1");
                                        String temp2 = jsonObject2.getString("temp2");
                                        String date = jsonObject2.getString("date");
                                        String temp=temp1+"℃-"+temp2+"℃";
                                        map.put("weather", "天气:"+weather);
                                        map.put("temp", "温度:"+temp);
                                        map.put("date","    "+date);
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
        public IHandler(F3Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((F3Activity) mActivity.get()).progressBar11.setVisibility(View.INVISIBLE);
                    ((F3Activity) mActivity.get()).adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
