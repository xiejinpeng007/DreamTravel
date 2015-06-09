package com.example.xjp.myapplication1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;


public class MainActivity extends Activity implements View.OnClickListener{
    static MainActivity mainActivity;
    ResideMenu resideMenu;
    ResideMenu.OnMenuListener menuListener;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemfavor;
    private ResideMenuItem itemSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_main);
        mainActivity=this;

        //初始化界面
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.hide(fm.findFragmentById(R.id.fragment2));
        ft.hide(fm.findFragmentById(R.id.fragment3));
        ft.commit();
        Toast.makeText(this, "欢迎使用梦想旅游！", Toast.LENGTH_SHORT).show();

        //初始化右边栏
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.topic1);
        resideMenu.attachToActivity(this);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        itemfavor = new ResideMenuItem(this, R.drawable.icon_calendar, "favorite");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemfavor.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemfavor, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);

        resideMenu.setMenuListener(menuListener);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);//禁止右边滑动


        //设置radiobutton样式和点击事件
        final RadioButton rb=(RadioButton)findViewById(R.id.radioButton);
        final RadioButton rb2=(RadioButton)findViewById(R.id.radioButton2);
        final RadioButton rb3=(RadioButton)findViewById(R.id.radioButton3);
        RadioGroup rg=(RadioGroup)findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton:
                    {
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.show(fm.findFragmentById(R.id.fragment));
                        ft.hide(fm.findFragmentById(R.id.fragment2));
                        ft.hide(fm.findFragmentById(R.id.fragment3));
                        ft.commit();
                        //设置drawable top
                        Drawable drawable = getResources().getDrawable( R.drawable.home2);
                        Drawable drawable2 = getResources().getDrawable( R.drawable.flag);
                        Drawable drawable3 = getResources().getDrawable( R.drawable.my);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                        rb.setCompoundDrawables(null, drawable, null, null);//left,top,right,buttom
                        rb2.setCompoundDrawables(null, drawable2, null, null);
                        rb3.setCompoundDrawables(null, drawable3, null, null);

                    }
                        break;
                    case R.id.radioButton2:
                    {
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.show(fm.findFragmentById(R.id.fragment2));
                        ft.hide(fm.findFragmentById(R.id.fragment));
                        ft.hide(fm.findFragmentById(R.id.fragment3));
                        ft.commit();
                        //设置drawable top
                        Drawable drawable = getResources().getDrawable( R.drawable.home);
                        Drawable drawable2 = getResources().getDrawable( R.drawable.flag2);
                        Drawable drawable3 = getResources().getDrawable( R.drawable.my);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());                        rb.setCompoundDrawables(null, drawable, null, null);//left,top,right,buttom
                        rb2.setCompoundDrawables(null, drawable2, null, null);
                        rb3.setCompoundDrawables(null, drawable3, null, null);                    }
                        break;
                    case R.id.radioButton3:
                    {
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.show(fm.findFragmentById(R.id.fragment3));
                        ft.hide(fm.findFragmentById(R.id.fragment2));
                        ft.hide(fm.findFragmentById(R.id.fragment));
                        ft.commit();
                        //设置drawable top
                        Drawable drawable = getResources().getDrawable( R.drawable.home);
                        Drawable drawable2 = getResources().getDrawable( R.drawable.flag);
                        Drawable drawable3 = getResources().getDrawable( R.drawable.my2);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());                        rb.setCompoundDrawables(null, drawable, null, null);//left,top,right,buttom
                        rb2.setCompoundDrawables(null, drawable2, null, null);
                        rb3.setCompoundDrawables(null, drawable3, null, null);
                    }
                        break;

                }
            }
        });



    }
    //重写右边栏点击事件
    @Override
    public void onClick(View view) {

        if (view == itemHome){
            Toast.makeText(this,"click home,功能制作中.",Toast.LENGTH_SHORT).show();
        }else if (view == itemProfile){
            Toast.makeText(this,"click profile,功能制作中.",Toast.LENGTH_SHORT).show();
        }else if (view == itemfavor){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,Favorite.class);
            startActivity(intent);
        }else if (view == itemSettings){
            Toast.makeText(this,"click settings,功能制造中.",Toast.LENGTH_SHORT).show();
        }

        resideMenu.closeMenu();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
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
