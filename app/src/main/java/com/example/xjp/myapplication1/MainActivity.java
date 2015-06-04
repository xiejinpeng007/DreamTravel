package com.example.xjp.myapplication1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends Activity {
    static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_main);
        mainActivity=this;

        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.hide(fm.findFragmentById(R.id.fragment2));
        ft.hide(fm.findFragmentById(R.id.fragment3));
        ft.commit();
        Toast.makeText(this, "欢迎使用梦想旅游！", Toast.LENGTH_SHORT).show();

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
