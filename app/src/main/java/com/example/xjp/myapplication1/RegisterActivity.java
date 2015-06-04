package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.ServiceRulesException;
import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.ServiceRulesException2;
import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.UserService2;
import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.UserServiceImplement2;

import java.lang.ref.WeakReference;

/**
 * Created by XJP on 15/3/13.
 */


public  class RegisterActivity extends Activity {
    private IHandler handler = new IHandler(this);
    ProgressBar progressBar5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_register);
        final EditText et3=(EditText)findViewById(R.id.editText3);
        final EditText et4=(EditText)findViewById(R.id.editText4);
        final EditText et5=(EditText)findViewById(R.id.editText5);
        final EditText et6=(EditText)findViewById(R.id.editText6);
        final RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup);
        final TextView textView27=(TextView)findViewById(R.id.textView27);
        final Button bt4=(Button)findViewById(R.id.button4);
        progressBar5=(ProgressBar)findViewById(R.id.progressBar5);
        final UserService2 userService2=new UserServiceImplement2();
        final SharedPreferences sharedPreferences = getSharedPreferences("flag", Context.MODE_PRIVATE); //私有数据

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton4:textView27.setText("男");break;
                    case R.id.radioButton5:textView27.setText("女");break;
                    default:break;
                }
            }
        });
        //设置按下效果
        bt4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    bt4.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    bt4.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =(InputMethodManager)RegisterActivity.this.getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(et4.getWindowToken(), 0); //隐藏键盘
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘
//获取注册信息
                final String UserName=et3.getText().toString();
                final String UserPassword=et4.getText().toString();
                final String UserTrueName=et5.getText().toString();
                final String UserPhone=et6.getText().toString();


//读取选中radiobutton值

                final String UserSex=textView27.getText().toString();
                if (UserName.equals("")||UserPassword.equals("")||UserTrueName.equals("")||UserPhone.equals("")||UserSex.equals("")) {
                    Toast.makeText(RegisterActivity.this, "用户信息不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar5.setVisibility(View.VISIBLE);
                    //副线程
                final Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
//数据传到userlogin进行判断
                            userService2.userregister(UserName, UserPassword, UserSex, UserPhone, UserTrueName);
                            handler.sendEmptyMessage(0);//成功
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("username",UserName);
                            editor.putBoolean("loginflag",true);
                            editor.commit();
                        }
                        catch (ServiceRulesException e){
                            handler.sendEmptyMessage(1);//失败
                        }
                        catch (ServiceRulesException2 e){
                            handler.sendEmptyMessage(2);//用户存在
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            handler.sendEmptyMessage(3);
                        }

                    }
                });
                thread.start();

            }}
        });

    }


//跳转回MainActivity
    private void jump(){
        MainActivity.mainActivity.finish();//销毁MainActivity
        Intent intent=new Intent();
        intent.setClass(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }
    private void showTip(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    //本可使用Handler，这样弱引用LoginActivity可以使用其方法
    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public IHandler(RegisterActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 0:
                    ((RegisterActivity) mActivity.get()).showTip("注册成功");
                    ((RegisterActivity) mActivity.get()).jump();
                    ((RegisterActivity) mActivity.get()).progressBar5.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    ((RegisterActivity) mActivity.get()).showTip("注册失败");
                    ((RegisterActivity) mActivity.get()).jump();
                    ((RegisterActivity) mActivity.get()).progressBar5.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    ((RegisterActivity) mActivity.get()).showTip("用户名存在");
                    ((RegisterActivity) mActivity.get()).progressBar5.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    ((RegisterActivity)mActivity.get()).showTip("注册失败");
                    ((RegisterActivity) mActivity.get()).progressBar5.setVisibility(View.INVISIBLE);
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
