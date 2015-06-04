package com.example.xjp.myapplication1;

import android.app.Activity;
import android.app.DatePickerDialog;
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
 * Created by XJP on 15/4/9.
 */

public class F8Activity extends Activity{

    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
    SimpleAdapter adapter;
    TextView textView36;
    ProgressBar progressBar2;
    int year;
    int month;
    int day;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
            setContentView(R.layout.f8activity);
            final EditText editText8=(EditText)findViewById(R.id.editText8);
            final EditText editText9=(EditText)findViewById(R.id.editText9);
            final Button button7=(Button)findViewById(R.id.button7);
            textView36=(TextView)findViewById(R.id.textView36);
            progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
            final ListView listView6=(ListView)findViewById(R.id.listView6);


            adapter=new SimpleAdapter(this,list,R.layout.listview6,//创建简单适配器
                        new String[]{"train_no","start_time","arrive_time","lishi"},
                        new int[]{R.id.textView38,R.id.textView39,R.id.textView41,R.id.textView40});
            listView6.setAdapter(adapter);

        Calendar calendar=Calendar.getInstance(Locale.CHINA);//获取本地时间
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        if (month<10) {
            if (day<10)
                textView36.setText(year + "-0" + month + "-0" + day);
            else
                textView36.setText(year + "-0" + month + "-" + day);
        }
        else {
            if (day<10)
                textView36.setText(year + "-0" + month + "-0" + day);
            else
                textView36.setText(year + "-0" + month + "-" + day);
        }
        textView36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(F8Activity.this, onDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
        //设置按下效果
        button7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button7.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button7.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager inputMethodManager =(InputMethodManager)F8Activity.this.getApplicationContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText8.getWindowToken(), 0); //隐藏键盘

//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘
                    progressBar2.setVisibility(View.VISIBLE);

                    //副线程
                    final Thread thread = new Thread (new Runnable() {
                        @Override
                        public void run() {
                            //网络通讯
                            String from = editText8.getText().toString();
                            String to =editText9.getText().toString();
                            String date=textView36.getText().toString();
                            try {
                                String result ;
                                HttpClient client = new DefaultHttpClient();
                                String appkey = "9838b65248b424a6b4523ccc59a174b8";
                                String uri = "http://apis.juhe.cn/train/yp?key="+appkey+"&from="+from+"&to="+to+"&date="+date;
                                HttpGet get = new HttpGet(uri);
                                HttpResponse response = client.execute(get);
                                int statuscode = response.getStatusLine().getStatusCode();
                                if (statuscode == HttpStatus.SC_OK) {
                                    result = EntityUtils.toString(response.getEntity(), "UTF-8");

                                    //解析json
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                                        for(int i=0;i<jsonArray.length();i++) {
                                            HashMap<String, Object> map = new HashMap<String, Object>();//HashMap用于存储map，每行数据
                                            JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                            String train_no = jsonObject2.getString("train_no");
                                            String start_time = jsonObject2.getString("start_time");
                                            String arrive_time = jsonObject2.getString("arrive_time");
                                            String lishi = jsonObject2.getString("lishi");
                                            map.put("train_no", ""+train_no);
                                            map.put("start_time", "出发:"+start_time);
                                            map.put("arrive_time","到达:"+arrive_time);
                                            map.put("lishi","历时:"+lishi);
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


    DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
            year=myear;
            month=mmonth+1;
            day=mday;
            update();
        }
        public void update(){
            if (month<10) {
                if (day<10)
                    textView36.setText(year + "-0" + month + "-0" + day);
                else
                    textView36.setText(year + "-0" + month + "-" + day);
            }
            else {
                if (day<10)
                    textView36.setText(year + "-0" + month + "-0" + day);
                else
                    textView36.setText(year + "-0" + month + "-" + day);

            }        }
    };

private void showTip(String str){
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
}

    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(F8Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((F8Activity) mActivity.get()).progressBar2.setVisibility(View.INVISIBLE);
                    ((F8Activity) mActivity.get()).adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }


    }
}