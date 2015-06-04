package com.example.xjp.myapplication1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XJP on 15/3/12.
 */
public class Fragment3 extends Fragment {
    View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);

        ListView listView2=(ListView)view.findViewById(R.id.listView2);
        final Button button=(Button)view.findViewById(R.id.button);
        final Button button2=(Button)view.findViewById(R.id.button2);
        final Button button5=(Button)view.findViewById(R.id.button5);
        final TextView textView3=(TextView)view.findViewById(R.id.textView3);
        final ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
        listView2.setDividerHeight(0); //去掉listview分割线

//判断登陆状态
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("flag",Context.MODE_PRIVATE);
        final Boolean flag=sharedPreferences.getBoolean("loginflag",false);
        final String username=sharedPreferences.getString("username","");
        if (flag==true){
            button.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button5.setVisibility(View.VISIBLE);
            textView3.setText(username);
            imageView.setImageResource(R.drawable.user2);
        }
        else {
            button.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button5.setVisibility(View.GONE);
        }
//登陆
        //设置按下效果
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
//注册
        //设置按下效果
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button2.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button2.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });
//注销
        //设置按下效果
        button5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    button5.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    button5.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("loginflag",false);
                editor.commit();
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button5.setVisibility(View.GONE);
                textView3.setText("请登录");
                imageView.setImageResource(R.drawable.user);
                Toast.makeText(getActivity(),"已注销",Toast.LENGTH_SHORT).show();
            }
        });



        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//ArrayList用于存储HashMap
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),list,R.layout.listview2,//创建简单适配器
                new String[]{"image","title"},
                new int[]{R.id.imageView3,R.id.textView12}
        );
        listView2.setAdapter(adapter);
        String[] titlestr=new String[]{"  我的积分","  我的订单","  我的评价"};

        for (int i=0;i<3;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();//HashMap用于存储map，每行数据
            String title=titlestr[i];
            if(i==0)
                map.put("image", R.drawable.money);
            else if(i==1)
                map.put("image",R.drawable.order);
            else if(i==2)
                map.put("image",R.drawable.comment);

            map.put("title", title);
            list.add(map);
        }

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//i是获取Item的位置，可以用于获取数据和处理跳转
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("flag",Context.MODE_PRIVATE);
                switch (i){
                    case 0:
                        if (sharedPreferences.getBoolean("loginflag",false)==true){
                        Intent intent=new Intent();
                        intent.setClass(getActivity(),OrderActivity.class);
                        startActivity(intent);}
                        else Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        if (sharedPreferences.getBoolean("loginflag",false)==true){
                            Intent intent=new Intent();
                            intent.setClass(getActivity(),OrderActivity.class);
                            startActivity(intent);}
                        else Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        if (sharedPreferences.getBoolean("loginflag",false)==true){
                            Intent intent=new Intent();
                            intent.setClass(getActivity(),CommitActivity.class);
                            startActivity(intent);}
                        else Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                        break;

            }}
        });


        return view;}
}