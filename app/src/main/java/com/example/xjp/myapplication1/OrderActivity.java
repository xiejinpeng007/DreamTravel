package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by XJP on 15/3/23.
 */
public class OrderActivity extends Activity {

    ArrayList<HashMap<String,Object>>list=new ArrayList<>();
    SimpleAdapter adapter;
    TextView textView22;
    ProgressBar progressBar10;
    View view;
    View popupview;
    PopupWindow popupWindow;
    String scenicname,scenicclass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_order);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏键盘
        popupview=this.getLayoutInflater().inflate(R.layout.activity_order_addcommit,null);


        final ListView listView3=(ListView)findViewById(R.id.listView3);
        textView22=(TextView)findViewById(R.id.textView22);
        progressBar10=(ProgressBar)findViewById(R.id.progressBar10);
        final Button button15=(Button)popupview.findViewById(R.id.button15);
        final EditText editText10=(EditText)popupview.findViewById(R.id.editText10);
        final SharedPreferences sharedPreferences=getSharedPreferences("flag", Context.MODE_PRIVATE);

        adapter=new SimpleAdapter(this,list,R.layout.listview3,
                new String[]{"price","time","scenicclass","scenicname"},
                new int[]{R.id.textView29,R.id.textView30,R.id.textView70,R.id.textView71});
        listView3.setAdapter(adapter);
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        final String date=year+"-"+month+"-"+day;

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object>map=(HashMap<String,Object>)listView3.getItemAtPosition(i);
                popupWindow=new PopupWindow(popupview,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(view,0,0,Gravity.RIGHT);
                scenicname=map.get("scenicname").toString();
                scenicclass=map.get("scenicclass").toString();
                switch (scenicclass){
                    case "周末游":scenicclass="weekend";break;
                    case "出境游":scenicclass="foreign";break;
                    case "邮轮游":scenicclass="ship";break;
                    case "普通游":scenicclass="scenic";break;
                }

            }
        });
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String comment=editText10.getText().toString();
                final String username=sharedPreferences.getString("username","");
                //提交评论线程
                final Thread commentthread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String uri="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientGetComment?username="+username+"&scenicname="+scenicname+"&flag="+scenicclass+"&comment="+comment+"&time="+date+"";
                            String result=null;
                            HttpClient client=new DefaultHttpClient();
                            HttpGet get=new HttpGet(uri);
                            HttpResponse response=client.execute(get);
                            int statuscode=response.getStatusLine().getStatusCode();
                            if (statuscode==HttpStatus.SC_OK)
                                result=EntityUtils.toString(response.getEntity(),"UTF-8");
                            if (result.equals("1"))
                                handler.sendEmptyMessage(1);
                            else if (result.equals("2"))
                                handler.sendEmptyMessage(2);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                commentthread.start();
            }
        });
        //设置按下效果
        button15.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button15.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button15.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });

        //获取订单线程
        final Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar10.setVisibility(View.VISIBLE);
                try{
                    String username=sharedPreferences.getString("username","");
                    String result=null;
                    String uri="http://mxly.wicp.net/DreamtimeTravel/servlet/ClientGetOrder?username="+username+"";
                    HttpClient client=new DefaultHttpClient();
                    HttpGet get=new HttpGet(uri);
                    HttpResponse response=client.execute(get);
                    int statuscode=response.getStatusLine().getStatusCode();
                    if (statuscode== HttpStatus.SC_OK)
                    {
                        result= EntityUtils.toString(response.getEntity(),"UTF-8");
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("OrderList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                            String price = jsonObject2.getString("price");
                            String scenicname = jsonObject2.getString("scenicname");
                            String time = jsonObject2.getString("time");
                            String scenicclass = jsonObject2.getString("scenicclass");
                            map.put("price","价格:"+price);
                            map.put("scenicname",scenicname);
                            map.put("time",time);
                            switch (scenicclass){
                                case "weekend":scenicclass="周末游";break;
                                case "foreign":scenicclass="出境游";break;
                                case "ship":scenicclass="邮轮游";break;
                                case "secnic":scenicclass="普通游";break;
                            }
                            map.put("scenicclass", scenicclass);
                            list.add(map);
                        }
                        //获取金币的值传给handler
                        JSONObject jsonObject2 = (JSONObject) jsonArray.opt(0);
                        String integral = jsonObject2.getString("integral");
                        Message msg=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("integral",integral);
                        msg.setData(bundle);
                        handler.sendEmptyMessage(0);
                        handler.sendMessage(msg);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        } );
        thread.start();
    }

    public void showTip(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    IHandler handler=new IHandler(this);
    public static class IHandler extends Handler{
        public final WeakReference<Activity> mActivity;
        public IHandler(OrderActivity activity){
            mActivity=new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int flag=msg.what;
            switch (flag){
                case 0:
                    ((OrderActivity)mActivity.get()).progressBar10.setVisibility(View.INVISIBLE);
                    String integral=msg.getData().getString("integral");
                    ((OrderActivity)mActivity.get()).textView22.setText("我的金币:  "+integral+"分");
                    ((OrderActivity)mActivity.get()).adapter.notifyDataSetChanged();
                    break;
                case 1:((OrderActivity)mActivity.get()).showTip("评论成功");
                       ((OrderActivity)mActivity.get()).popupWindow.dismiss();
                    break;
                case 2:((OrderActivity)mActivity.get()).showTip("评论失败");
                       ((OrderActivity)mActivity.get()).popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
