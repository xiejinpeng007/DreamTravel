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

public class F7Activity extends Activity {
    int year;
    int month;
    int day;
    TextView textView57;
    ListView listView8;
    EditText editText14;
    EditText editText15;
    ProgressBar progressBar;
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.f7activity);
        final Button button11=(Button)findViewById(R.id.button11);
        textView57=(TextView)findViewById(R.id.textView57);
        editText14=(EditText)findViewById(R.id.editText14);
        editText15=(EditText)findViewById(R.id.editText15);
        listView8=(ListView)findViewById(R.id.listView8);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        adapter=new SimpleAdapter(this,list,R.layout.listview8,//创建简单适配器
                new String[]{"DepTime","ArrTime","FlightNum","Airline"},
                new int[]{R.id.textView52,R.id.textView53,R.id.textView54,R.id.textView55});
        listView8.setAdapter(adapter);

        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        if (month<10) {
            if (day<10)
                textView57.setText(year + "-0" + month + "-0" + day);
            else
                textView57.setText(year + "-0" + month + "-" + day);
        }
        else {
            if (day<10)
                textView57.setText(year + "-0" + month + "-0" + day);
            else
                textView57.setText(year + "-0" + month + "-" + day);
        }
        textView57.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(F7Activity.this, onDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        //设置按下效果
        button11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button11.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button11.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =(InputMethodManager)F7Activity.this.getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText14.getWindowToken(), 0); //隐藏键盘

//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘
                progressBar.setVisibility(View.VISIBLE);
                //副线程
                final Thread thread = new Thread (new Runnable() {
                    @Override
                    public void run() {
                        //网络通讯
                        String start = editText14.getText().toString();
                        String end =editText15.getText().toString();
                        String date=textView57.getText().toString();
                        try {
                            String result ;
                            HttpClient client = new DefaultHttpClient();
                            String appkey = "d9b9444bf4104ac039e9fff3a5e311c4";
                            String uri = "http://apis.juhe.cn/plan/bc?key="+appkey+"&start="+start+"&end="+end+"&date="+date;
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
                                        String ArrTime = jsonObject2.getString("ArrTime");
                                        String DepTime = jsonObject2.getString("DepTime");
                                        String FlightNum = jsonObject2.getString("FlightNum");
                                        String Airline = jsonObject2.getString("Airline");
                                        map.put("DepTime", "出发:"+DepTime);
                                        map.put("ArrTime", "到达:"+ArrTime);
                                        map.put("FlightNum","    "+FlightNum);
                                        map.put("Airline","    "+Airline);
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
                    textView57.setText(year + "-0" + month + "-0" + day);
                else
                    textView57.setText(year + "-0" + month + "-" + day);
            }
            else {
                if (day<10)
                    textView57.setText(year + "-0" + month + "-0" + day);
                else
                    textView57.setText(year + "-0" + month + "-" + day);

            }        }
    };

    private void showTip(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    IHandler handler = new IHandler(this);
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(F7Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((F7Activity) mActivity.get()).progressBar.setVisibility(View.INVISIBLE);
                    ((F7Activity) mActivity.get()).adapter.notifyDataSetChanged();
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
